package coreref.common

import grails.test.*

@TestFor(ActivityTagLib)
class ActivityTagLibTests {

    void testBrief() {
		views['/activity/_brief.gsp'] = 'brief template'
		def a = [
			user: new User(id: 'userId', firstName: 'Josh', lastName: 'Reed'),
			action: 'created',
			project: new Project(id: 'id', projectId: 'projectId'),
			ts: new Date()
		]
		def out = tagLib.brief(src: a).toString()
		assert "brief template" == out
    }

	void testFull() {
		views['/activity/_full.gsp'] = 'full template'
		def a = [
			user: new User(id: 'userId', firstName: 'Josh', lastName: 'Reed'),
			action: 'created',
			project: new Project(id: 'id', projectId: 'projectId', name: 'Test Project', desc: 'Some Desc'),
			ts: new Date()
		]
		def out = tagLib.full(src: a).toString()
		assert "full template" == out
	}
}
