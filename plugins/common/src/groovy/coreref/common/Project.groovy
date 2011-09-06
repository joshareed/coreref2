package coreref.common

class Project {
	def id
	String projectId
	String ownerId

	Project(Map map = [:]) {
		id = map._id ?: map.id
		projectId = map.projectId
		if (map.containsKey('owner')) {
			ownerId = map?.owner?.id
		}
		ownerId = map.ownerId
	}

	Map save(Map map = [:]) {
		map.projectId = projectId
		map.ownerId = ownerId
		map
	}

	User getOwner() {
		ownerId ? User.getInstance(ownerId) : null
	}

	void setOwner(User owner) {
		ownerId = owner.id
	}

	static mongo = [
		collection: '_projects',
		index: ['projectId', 'ownerId']
	]
}