package coreref.common

class Application {
	String appId
	boolean enabled = true
	boolean limited = false
	String contact
	String site

	Application(Map map = [:]) {
		appId = map.appId
		enabled = map.containsKey('enabled') ? map.enabled : true
		limited = map.containsKey('limited') ? map.limited : false
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

	static mongo = [
		collection: '_apps',
		index: ['appId']
	]
}
