package coreref.common

import grails.test.*

class LexiconServiceTests extends GrailsUnitTestCase {
	def service

	protected void setUp() {
		super.setUp()
		service = new LexiconService()
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testRegister() {
		assert !service.all
		assert null == service.getEntry('test')

		service.register('test', 'Test Entry', 'Some Description Text', 'Help Text', 'template', 'plugin')
		assert [key: 'test', name: 'Test Entry', description: 'Some Description Text', help: 'Help Text', template: 'template', plugin: 'plugin'] == service.getEntry('test')

		assert 1 == service.all.size()
	}
}
