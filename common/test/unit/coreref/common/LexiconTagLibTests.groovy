package coreref.common

@TestFor(LexiconTagLib)
class LexiconTagLibTests {
	def lexiconService

	@Before
	public void setUp() {
		lexiconService = new LexiconService()
		lexiconService.register('test', 'The Label')

		// wire our dependency into the taglib
		def taglib = applicationContext.getBean(LexiconTagLib)
		taglib.lexiconService = lexiconService
	}

	void testLabel() {
		assert 'The Label' == applyTemplate('<l:label key="test"/>')
	}

	void testValueNoTemplate() {
		assert 'The Value' == applyTemplate("""<l:value key="test" src="${[foo: 'bar']}" value="The Value"/>""")
	}
}
