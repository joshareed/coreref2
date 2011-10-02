package coreref

import grails.test.*
import coreref.common.*

class ProjectControllerTests extends ControllerUnitTestCase {
	def mongoService

	protected void setUp() {
		super.setUp()

		mongoService = new coreref.mongo.MongoService()
		mongoService.map(Project)
		mongoService.map(Activity)

		Project.mongoCollection.add(projectId: 'test', name: 'Test Project', desc: 'The description', priv: 1)
	}

	protected void tearDown() {
		super.tearDown()

		Project.mongoCollection.drop()
		Activity.mongoCollection.drop()
	}

	void testOverviewNoId() {
		controller.overview()
		assert [uri: '/'] == controller.redirectArgs
		assert "No project id specified" == controller.flash.message
	}

	void testOverviewInvalidId() {
		controller.params.id = 'does-not-exist'
		controller.overview()
		assert [uri: '/'] == controller.redirectArgs
		assert "No project with id 'does-not-exist'" == controller.flash.message
	}

	void testOverviewWithProjectId() {
		controller.params.id = 'test'
		def map = controller.overview()
		def p = map.project
		assert p
		assert p instanceof Project
		assert 'test' == p.projectId
		assert 'Test Project' == p.name
		assert 'The description' == p.desc
		assert 1 == p.priv
	}

	void testOverviewWithObjectId() {
		def project = Project.findInstance(projectId: 'test')
		assert project

		controller.params.id = project.id
		def map = controller.overview()
		def p = map.project
		assert p
		assert p instanceof Project
		assert 'test' == p.projectId
		assert 'Test Project' == p.name
		assert 'The description' == p.desc
		assert 1 == p.priv
	}

	void testCreate() {
		assert [errors: [:]] == controller.create()
	}

	void testSaveInvalid() {
		assert 1 == Project.mongoCollection.count()
		def map = controller.save()
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

		assert 'create' == controller.renderArgs.view
	}

	void testSave() {
		controller.params.putAll(ownerId: 'user', projectId: 'test2', name: 'Test Project')
		controller.activityService = new ActivityService()

		assert 1 == Project.mongoCollection.count()
		assert 0 == Activity.mongoCollection.count()
		def map = controller.save()
		assert 2 == Project.mongoCollection.count()
		assert 1 == Activity.mongoCollection.count()

		assert 'Project test2 created' == controller.flash.message

		def p = Project.findInstance(projectId: 'test2')
		assert p
		assert 'user' == p.ownerId
		assert 'test2' == p.projectId
		assert 'Test Project' == p.name

		assert [controller: 'project', action: 'overview', id: 'test2'] == controller.redirectArgs
	}

	void testSearch() {
		assert null == controller.search()
	}
}
