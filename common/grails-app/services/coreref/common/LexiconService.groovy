package coreref.common

import grails.util.GrailsUtil

class LexiconService {
	def grailsApplication
	static transactional = false

	def parsed = false
	def entries = [:]

	def register(Map map) {
		assert null != map.key
		assert null != map.name

		entries[map.key] = map
	}

	def register(key, name, description = null, help = null, template = null, plugin = null) {
		parseConfig()
		register(key: key, name: name, description: description, help: help, template: template, plugin: plugin)
	}

	def getEntry(key) {
		parseConfig()
		return entries[key]
	}

	def getAll() {
		parseConfig()
		return entries
	}

	private parseConfig() {
		if (!parsed && grailsApplication) {
			parsed = true

			def config = grailsApplication?.config
			if (config) {
				GroovyClassLoader classLoader = new GroovyClassLoader(getClass().classLoader)
				config.merge(new ConfigSlurper(GrailsUtil.environment).parse(classLoader.loadClass('DefaultLexiconConfig')))
				config.lexicon.each { k, v ->
					register(v + [key: k])
				}
			}
		}
	}
}
