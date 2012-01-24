package coreref.common

import grails.test.*

@TestFor(ActivityTagLib)
class ActivityTagLibTests {

    void testBrief() {
		def a = [
			user: new User(id: 'userId', firstName: 'Josh', lastName: 'Reed'),
			action: 'created',
			project: new Project(id: 'id', projectId: 'projectId'),
			ts: new Date()
		]
		def out = tagLib.brief(src: a)
		assert out.contains("""<div class="activity brief">""")
		assert out.contains("""<a href="/user?id=userId">Josh Reed</a>""")
		assert out.contains("""<span class="action">created</span>""")
		assert out.contains("""<a href="/project?id=projectId">projectId</a>""")
    }

	void testFull() {
		def a = [
			user: new User(id: 'userId', firstName: 'Josh', lastName: 'Reed'),
			action: 'created',
			project: new Project(id: 'id', projectId: 'projectId', name: 'Test Project', desc: 'Some Desc'),
			ts: new Date()
		]
		def out = tagLib.full(src: a)
		assert out.contains("""<div class="activity full">""")
		assert out.contains("""<a href="/user?id=userId">Josh Reed</a>""")
		assert out.contains("""<span class="action">created</span>""")
		assert out.contains("""<a href="/project?id=projectId">projectId</a>""")
		assert out.contains("""<div class="body">""")
		assert out.contains("""<div class="subject">Test Project:</div>""")
		assert out.contains("""<blockquote>Some Desc</blockquote>""")
	}
}
