package coreref.common

import grails.test.*

class LexiconTagLibTests extends TagLibUnitTestCase {
	protected void setUp() {
		super.setUp()
		tagLib.lexiconService = new LexiconService()
		tagLib.lexiconService.register('test', 'The Label')
		tagLib.lexiconService.register('test2', 'The Label', null, null, 'template', 'plugin')
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testLabel() {
		tagLib.label(key: 'test')
		assert 'The Label' == tagLib.out.toString()
	}

	void testValueNoTemplate() {
		tagLib.value(key: 'test', src: [foo: 'bar'], value: 'The Value')
		assert 'The Value' == tagLib.out.toString()
	}

	void testValueTemplate() {
		tagLib.value(key: 'test2', src: [foo: 'bar'], value: 'The Value')
		assert '/lexicon/template' == tagLib.renderArgs.template
		assert 'plugin' == tagLib.renderArgs.plugin
		assert [foo: 'bar'] == tagLib.renderArgs.model.src
		assert 'The Value' == tagLib.renderArgs.model.value
		assert 'test2' == tagLib.renderArgs.model.key
		assert null != tagLib.renderArgs.model.entry
	}
}
