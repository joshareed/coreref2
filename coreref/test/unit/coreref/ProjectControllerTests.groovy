package coreref

import coreref.common.*

@TestFor(ProjectController)
class ProjectControllerTests {
	def mongoService

	@Before
	public void setUp() {
		mongoService = new coreref.mongo.MongoService()
		mongoService.map(Project)

		Project.mongoCollection.add(projectId: 'test', name: 'Test Project', desc: 'The description', priv: 1)
	}

	@After
	public void tearDown() {
		Project.mongoCollection.drop()
	}

	void testOverviewNoId() {
		controller.overview()
		assert '/' == response.redirectedUrl
		assert "No project id specified" == controller.flash.error
	}

	void testOverviewInvalidId() {
		controller.params.id = 'does-not-exist'
		controller.overview()
		assert '/' == response.redirectedUrl
		assert "No project with id 'does-not-exist'" == controller.flash.error
	}

	void testOverviewNotPublicAndNotLoggedIn() {
		def project = Project.findInstance(projectId: 'test')
		project.priv = 2
		Project.mongoCollection.update(project.mongoObject, project)

		assert !project.isPublic()
		controller.params.id = 'test'
		controller.overview()
		assert '/login' == response.redirectedUrl
	}

	void testOverviewNotPublicRenderPrivate() {
		def project = Project.findInstance(projectId: 'test')
		project.priv = 2
		Project.mongoCollection.update(project.mongoObject, project)

		assert !project.isPublic()
		controller.params.id = 'test'
		controller.session.user = [
			canView: { p -> false }
		]
		controller.overview()
		assert '/project/private' == view
	}

	void testOverviewNotPublicButCanView() {
		def project = Project.findInstance(projectId: 'test')
		project.priv = 2
		Project.mongoCollection.update(project.mongoObject, project)

		assert !project.isPublic()
		controller.params.id = 'test'
		controller.session.user = [
			canView: { p -> true }
		]
		def map = controller.overview()
		def p = map.project
		assert p
		assert p instanceof Project
		assert 'test' == p.projectId
		assert 'Test Project' == p.name
		assert 'The description' == p.desc
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

	void testJoin() {
		controller.projectService = [
			join: { project, user ->
				assert 'test' == project.projectId
				assert 'User' == user
			}
		]

		controller.session.user = 'User'
		controller.params.id = 'test'
		controller.join()

		assert '/project/overview/test' == response.redirectedUrl
	}

	void testLeave() {
		controller.projectService = [
			leave: { project, user ->
				assert 'test' == project.projectId
				assert 'User' == user
			}
		]

		controller.session.user = 'User'
		controller.params.id = 'test'
		controller.leave()

		assert '/project/overview/test' == response.redirectedUrl
	}

	void testCreate() {
		assert [errors: [:]] == controller.create()
	}

	void testSaveInvalid() {
		assert 1 == Project.mongoCollection.count()
		controller.save()
		assert 1 == Project.mongoCollection.count()

		assert model.project
		assert model.project instanceof Project

		def e = model.errors
		assert e
		assert 3 == e.size()
		assert 'Required field' == e.ownerId
		assert 'Required field' == e.projectId
		assert 'Required field' == e.name

		assert 'Project has errors' == controller.flash.message

		assert '/project/create' == view
	}

	void testSave() {
		controller.activityService = [
			logProjectCreated: { user, project ->
				assert 'test2' == project?.projectId
				assert 'User' == user
			}
		]

		assert 1 == Project.mongoCollection.count()
		controller.session.user = 'User'
		controller.params.putAll(ownerId: 'user', projectId: 'test2', name: 'Test Project')
		controller.save()
		assert 2 == Project.mongoCollection.count()

		assert 'Project test2 created' == controller.flash.message

		def p = Project.findInstance(projectId: 'test2')
		assert p
		assert 'user' == p.ownerId
		assert 'test2' == p.projectId
		assert 'Test Project' == p.name

		assert '/project/overview/test2' == response.redirectedUrl
	}

	void testSearch() {
		assert null == controller.search()
	}
}
