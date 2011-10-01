package coreref.common

class Project {
	String id
	String projectId
	String ownerId
	String name
	String description
	Map metadata

	Project(Map map = [:]) {
		id = map._id ?: map.id
		projectId = map.projectId
		if (map.containsKey('owner')) {
			ownerId = map?.owner?.id
		}
		ownerId = map.ownerId
		name = map.name
		description = map.description
		if (map.metadata) {
			metadata = map.metadata
		} else {
			metadata = [:]
			map.each { k, v ->
				if (k.startsWith('metadata.')) {
					metadata[k - 'metadata.'] = v
				}
			}
		}
	}

	Map save(Map map = [:]) {
		map.projectId = projectId
		map.ownerId = ownerId
		map.name = name
		map.description = description
		map.metadata = metadata ?: [:]
		map
	}

	def getErrors() {
		def errors = [:]
		['projectId', 'ownerId', 'name'].each { field ->
			if (!DomainUtils.isSet(this, field)) {
				errors[field] = 'Required field'
			}
		}
		if (!errors.projectId && !DomainUtils.isUnique(this, 'projectId', mongoCollection)) {
			errors.projectId = "'${projectId}' already in use"
		}
		errors
	}

	String toString() {
		"${name} (${projectId})"
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