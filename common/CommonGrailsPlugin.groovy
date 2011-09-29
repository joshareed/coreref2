class CommonGrailsPlugin {
	// the plugin version
	def version = "0.1"
	// the version or versions of Grails the plugin is designed for
	def grailsVersion = "1.3.7 > *"
	// the other plugins this plugin depends on
	def dependsOn = [:]
	// resources that are excluded from plugin packaging
	def pluginExcludes = [
		"grails-app/views/error.gsp"
	]

	// TODO Fill in these fields
	def author = "Josh Reed"
	def authorEmail = "jareed@andrill.org"
	def title = "CoreRef Common"
	def description = '''\\
Contains all of the common models and functionality for CoreRef
'''

	// URL to the plugin's documentation
	def documentation = "http://grails.org/plugin/common"

	def doWithWebDescriptor = { xml ->
		// TODO Implement additions to web.xml (optional), this event occurs before
	}

	def doWithSpring = {
		// TODO Implement runtime spring config (optional)
	}

	def doWithDynamicMethods = { ctx ->
		// TODO Implement registering dynamic methods to classes (optional)
	}

	def doWithApplicationContext = { applicationContext ->
		// register our classes
		if (applicationContext.containsBean('mongoService')) {
			def mongoService = applicationContext.getBean('mongoService')
			mongoService.map(coreref.common.User)
			mongoService.map(coreref.common.Project)
			mongoService.map(coreref.common.Application)
			mongoService.map(coreref.common.Activity)
		}

		// configure the security service
		if (applicationContext.containsBean('securityService')) {
			applicationContext.getBean('securityService').initialize()
		}
	}

	def onChange = { event ->
		// TODO Implement code that is executed when any artefact that this plugin is
		// watching is modified and reloaded. The event contains: event.source,
		// event.application, event.manager, event.ctx, and event.plugin.
	}

	def onConfigChange = { event ->
		// TODO Implement code that is executed when the project configuration changes.
		// The event is the same as for 'onChange'.
	}
}
