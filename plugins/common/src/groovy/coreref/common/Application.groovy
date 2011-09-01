package coreref.common

class Application {
	String appId
	boolean enabled = true
	boolean limited = false
	String contact
	String site

	Application(Map map = [:]) {
		appId = map.appId
		enabled = DomainUtils.coerceBoolean(map.enabled, true)
		limited = DomainUtils.coerceBoolean(map.limited, false)
		contact = map.contact
		site = map.site
	}

	Map save(Map map = [:]) {
		map.appId = appId
		map.enabled = enabled
		map.limited = limited
		map.contact = contact
		map.site = site
		map
	}

	boolean isValid() {
		appId != null && appId?.trim() != ''
	}

	def getErrors() {
		def errors = [:]
		['appId', 'contact'].each { field ->
			if (!DomainUtils.isSet(this, field)) {
				errors[field] = 'Required field'
			}
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
