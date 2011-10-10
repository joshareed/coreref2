package coreref

import grails.test.*

import coreref.common.*

class ProjectAdminControllerTests extends ControllerUnitTestCase {
	def mongoService

	protected void setUp() {
		super.setUp()

		mongoService = new coreref.mongo.MongoService()
		mongoService.map(Project)

		Project.mongoCollection.add(projectId: 'test', name: 'Test Project', desc: 'The description', priv: 1)
	}

	protected void tearDown() {
		super.tearDown()

		Project.mongoCollection.drop()
	}

	void testInfoNoId() {
		controller.info()
		assert [uri: '/'] == controller.redirectArgs
		assert "No project id specified" == controller.flash.error
	}

	void testInfoInvalidId() {
		controller.params.id = 'does-not-exist'
		controller.info()
		assert [uri: '/'] == controller.redirectArgs
		assert "No project with id 'does-not-exist'" == controller.flash.error
	}

	void testInfoNotLoggedIn() {
		controller.params.id = 'test'
		controller.info()
		assert [controller: 'login'] == controller.redirectArgs
	}

	void testInfoNotOwner() {
		controller.session.user = [ isOwner: { p -> false } ]
		controller.params.id = 'test'
		controller.info()

		assert 'Not the project admin' == controller.flash.error
		assert [controller: 'project', action: 'overview', id: 'test'] == controller.redirectArgs
	}

	void testInfo() {
		controller.session.user = [ isOwner: { p -> true } ]
		controller.params.id = 'test'
		def p = controller.info()

		assert p
		assert 1 == p.size()
		assert p.project instanceof Project
		assert 'test' == p.project.projectId
	}

	void testMembers() {
		controller.projectService = [
			pending: { p ->
				assert 'test' == p.projectId
				['pending']
			}
		]
		controller.session.user = [ isOwner: { p -> true } ]
		controller.params.id = 'test'
		def p = controller.members()

		assert p
		assert 2 == p.size()
		assert p.project instanceof Project
		assert 'test' == p.project.projectId
		assert ['pending'] == p.pending
	}

	void testApproveNoEmail() {
		controller.session.user = [ isOwner: { p -> true } ]
		controller.params.id = 'test'
		controller.approve()

		assert 'No user with that email' == controller.flash.error
		assert [action: 'members', id: 'test'] == controller.redirectArgs
	}

	void testApproveNoEmailAjax() {
		controller.request.addHeader "X-Requested-With", "XMLHttpRequest"
		controller.session.user = [ isOwner: { p -> true } ]
		controller.params.id = 'test'
		controller.approve()

		assert 404 == controller.response.status
	}

	void testApproveWithEmail() {
		controller.metaClass.getUser = { params -> 'User' }
		controller.projectService = [
			approve: { project, user ->
				assert 'test' == project?.projectId
				assert 'User' == user
			}
		]

		controller.session.user = [ isOwner: { p -> true } ]
		controller.params.id = 'test'
		controller.approve()

		assert 'Approved User' == controller.flash.message
		assert [action: 'members', id: 'test'] == controller.redirectArgs
	}

	void testApproveWithEmailAjax() {
		controller.request.addHeader "X-Requested-With", "XMLHttpRequest"
		controller.metaClass.getUser = { params -> 'User' }
		controller.projectService = [
			approve: { project, user ->
				assert 'test' == project?.projectId
				assert 'User' == user
			}
		]

		controller.session.user = [ isOwner: { p -> true } ]
		controller.params.id = 'test'
		controller.approve()

		assert 'Approved User' == controller.response.contentAsString
	}
}
