package coreref.common

class Application {
	String id
	String appId
	boolean enabled
	String contact
	String site

	Application(Map map = [:]) {
		id = map._id ?: map.id
		appId = map.appId
		enabled = DomainUtils.coerceBoolean(map.enabled)
		contact = map.contact
		site = map.site
	}

	Map save(Map map = [:]) {
		map.appId = appId
		map.enabled = enabled
		map.contact = contact
		map.site = site
		map
	}

	def getErrors() {
		def errors = [:]
		['appId', 'contact'].each { field ->
			if (!DomainUtils.isSet(this, field)) {
				errors[field] = 'Required field'
			}
		}
		if (!errors.appId && !DomainUtils.isUnique(this, 'appId', mongoCollection)) {
			errors.appId = "'${appId}' already in use"
		}
		errors
	}

	String toString() {
		"${appId} (${contact})"
	}

	static mongo = [
		collection: '_apps',
		index: ['appId']
	]

	static String randomId() {
		UUID.randomUUID().toString()[0..7]
	}
}
