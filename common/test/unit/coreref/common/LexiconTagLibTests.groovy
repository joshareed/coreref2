package coreref.common

@TestFor(LexiconTagLib)
class LexiconTagLibTests {
	def lexiconService

	@Before
	public void setUp() {
		lexiconService = new LexiconService()
		lexiconService.register('test', 'The Label')
		lexiconService.register('test2', 'The Label', null, null, 'test2', 'common')

		// wire our dependency into the taglib
		def taglib = applicationContext.getBean(LexiconTagLib)
		taglib.lexiconService = lexiconService
	}

	void testLabel() {
		assert 'The Label' == applyTemplate('<l:label key="test"/>')
	}

	void testValueNoTemplate() {
		views['/lexicon/_test.gsp'] = 'test template'
		assert 'The Value' == applyTemplate("""<l:value key="test" src="${[foo: 'bar']}" value="The Value"/>""")
	}

	void testValueTemplate() {
		views['/lexicon/_test2.gsp'] = 'test template'
		assert 'test template' == applyTemplate("""<l:value key="test2" src="${[foo: 'bar']}" value="The Value"/>""")
	}

	void testValueTemplateNotFound() {
		assert 'The Value' == applyTemplate("""<l:value key="test2" src="${[foo: 'bar']}" value="The Value"/>""")
	}
}
