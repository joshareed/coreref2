package coreref.common

@TestFor(LexiconService)
class LexiconServiceTests {

	void testRegister() {
		assert null == service.getEntry('test')

		service.register('test', 'Test Entry', 'Some Description Text', 'Help Text', 'template', 'plugin')
		assert [key: 'test', name: 'Test Entry', description: 'Some Description Text', help: 'Help Text', template: 'template', plugin: 'plugin'] == service.getEntry('test')
	}
}
