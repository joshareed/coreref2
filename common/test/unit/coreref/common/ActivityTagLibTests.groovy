package coreref.common

import grails.test.*

class ActivityTagLibTests extends TagLibUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testBrief() {
		def a = [
			user: [id: 'userId', firstName: 'Josh', lastName: 'Reed'],
			action: 'created',
			project: [id: 'projectId', name: 'Test Project']
		]
		tagLib.brief(src: a)
		assert '/activity/brief' == tagLib.renderArgs.template
		assert 'common' == tagLib.renderArgs.plugin
		assert a == tagLib.renderArgs.model.activity
		assert 'created' == tagLib.renderArgs.model.action
		assert null == tagLib.renderArgs.model.content

    }

	void testFull() {
		def a = [
			user: [id: 'userId', firstName: 'Josh', lastName: 'Reed'],
			action: 'created',
			project: [id: 'projectId', name: 'Test Project']
		]
		tagLib.full(src: a)
		assert '/activity/full' == tagLib.renderArgs.template
		assert 'common' == tagLib.renderArgs.plugin
		assert a == tagLib.renderArgs.model.activity
		assert 'created' == tagLib.renderArgs.model.action
		assert null != tagLib.renderArgs.model.content
	}
}
