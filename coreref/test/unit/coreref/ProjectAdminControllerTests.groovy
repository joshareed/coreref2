package coreref

import grails.test.*

import coreref.common.*

class ProjectAdminControllerTests extends ControllerUnitTestCase {
	def mongoService

	protected void setUp() {
		super.setUp()

		mongoService = new coreref.mongo.MongoService()
		mongoService.map(Project)

		Project.mongoCollection.add(projectId: 'test', ownerId: 'user', name: 'Test Project', desc: 'The description', priv: 1)
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

	void testIgnoreNoEmail() {
		controller.session.user = [ isOwner: { p -> true } ]
		controller.params.id = 'test'
		controller.ignore()

		assert 'No user with that email' == controller.flash.error
		assert [action: 'members', id: 'test'] == controller.redirectArgs
	}

	void testIgnoreNoEmailAjax() {
		controller.request.addHeader "X-Requested-With", "XMLHttpRequest"
		controller.session.user = [ isOwner: { p -> true } ]
		controller.params.id = 'test'
		controller.ignore()

		assert 404 == controller.response.status
	}

	void testIgnoreWithEmail() {
		controller.projectService = [
			block: { project, user ->
				assert 'test' == project?.projectId
				assert 'User' == user
			}
		]

		controller.session.user = [ isOwner: { p -> true } ]
		controller.params.id = 'test'
		controller.params.email = 'User'
		controller.ignore()

		assert 'Ignored User' == controller.flash.message
		assert [action: 'members', id: 'test'] == controller.redirectArgs
	}

	void testIgnoreWithEmailAjax() {
		controller.request.addHeader "X-Requested-With", "XMLHttpRequest"
		controller.projectService = [
			block: { project, user ->
				assert 'test' == project?.projectId
				assert 'User' == user
			}
		]

		controller.session.user = [ isOwner: { p -> true } ]
		controller.params.id = 'test'
		controller.params.email = 'User'
		controller.ignore()

		assert 'Ignored User' == controller.response.contentAsString
	}

	void testKickNoEmail() {
		controller.session.user = [ isOwner: { p -> true } ]
		controller.params.id = 'test'
		controller.kick()

		assert 'No user with that email' == controller.flash.error
		assert [action: 'members', id: 'test'] == controller.redirectArgs
	}

	void testKickNoEmailAjax() {
		controller.request.addHeader "X-Requested-With", "XMLHttpRequest"
		controller.session.user = [ isOwner: { p -> true } ]
		controller.params.id = 'test'
		controller.kick()

		assert 404 == controller.response.status
	}

	void testKickWithEmail() {
		controller.metaClass.getUser = { params -> 'User' }
		controller.projectService = [
			kick: { project, user ->
				assert 'test' == project?.projectId
				assert 'User' == user
			}
		]

		controller.session.user = [ isOwner: { p -> true } ]
		controller.params.id = 'test'
		controller.kick()

		assert 'Kicked User' == controller.flash.message
		assert [action: 'members', id: 'test'] == controller.redirectArgs
	}

	void testKickWithEmailAjax() {
		controller.request.addHeader "X-Requested-With", "XMLHttpRequest"
		controller.metaClass.getUser = { params -> 'User' }
		controller.projectService = [
			kick: { project, user ->
				assert 'test' == project?.projectId
				assert 'User' == user
			}
		]

		controller.session.user = [ isOwner: { p -> true } ]
		controller.params.id = 'test'
		controller.kick()

		assert 'Kicked User' == controller.response.contentAsString
	}

	void testInvite() {
		controller.projectService = [
			invite: { project, users ->
				assert 'test' == project?.projectId
				assert ['one@example.com', 'two@example.com', 'three@example.com'] == users
			}
		]

		controller.session.user = [ isOwner: { p -> true } ]
		controller.params.id = 'test'
		controller.params.invites = 'one@example.com, "Test User" <two@example.com>		three@example.com'
		controller.invite()

		assert [action: 'members', id: 'test'] == controller.redirectArgs
	}

	void testUpdateWithErrors() {
		controller.session.user = [ isOwner: { p -> true } ]
		controller.params.id = 'test'

		def map = controller.update()
		assert 1 == Project.mongoCollection.count()

		assert map.project
		assert map.project instanceof Project

		def e = map.errors
		assert e
		assert 3 == e.size()
		assert 'Required field' == e.ownerId
		assert 'Required field' == e.projectId
		assert 'Required field' == e.name

		assert 'Project has errors' == controller.flash.message

		assert 'info' == controller.renderArgs.view
	}

	void testUpdate() {
		controller.activityService = [
			logProjectUpdated: { user, project, diff ->
				assert 'test' == project?.projectId
				assert 'New Name' == project?.name
				assert user
				assert diff
			}
		]

		assert 1 == Project.mongoCollection.count()
		controller.session.user = [ isOwner: { p -> true } ]
		controller.params.putAll(id: Project.findInstance(projectId: 'test').id, projectId: 'test', name: 'New Name', ownerId: 'user', desc: 'The description', priv: 1)
		def map = controller.update()
		assert 1 == Project.mongoCollection.count()

		assert 'Project updated' == controller.flash.message

		assert [action: 'info', id: 'test'] == controller.redirectArgs
	}
}
