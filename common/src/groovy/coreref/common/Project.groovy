package coreref.common

class Project {
	static final int PUBLIC = 1
	static final int PRIVATE = 2
	static final int STEALTH = 3

	String id
	String projectId
	String ownerId
	String name
	String desc
	int priv
	Map metadata

	Project(Map map = [:]) {
		id = map._id ?: map.id
		projectId = map.projectId
		ownerId = map.ownerId
		if (map.containsKey('owner')) {
			ownerId = map?.owner?.id
		}
		name = map.name
		desc = map.desc
		if (map.containsKey('priv')) {
			priv = map.priv
		} else {
			priv = PUBLIC
		}
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
		map.desc = desc
		map.priv = priv
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
		index: ['projectId', 'ownerId', 'priv']
	]
}