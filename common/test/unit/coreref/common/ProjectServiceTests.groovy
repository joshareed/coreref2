package coreref.common

import grails.test.*

class ProjectServiceTests extends GrailsUnitTestCase {
	def mongoService
	def projectService

	def project
	def user

	protected void setUp() {
		super.setUp()

		// setup the service
		projectService = new ProjectService()

		// setup mongo
		mongoService = new coreref.mongo.MongoService()
		mongoService.map(User)
		mongoService.map(Project)
		mongoService.map(ProjectInvite)

		// create a project
		Project.mongoCollection.add(projectId: 'test', name: 'Test Project', priv: 2)
		project = Project.findInstance(projectId: 'test')
		assert project

		User.mongoCollection.add(firstName: 'Test', lastName: 'User', email: 'test@test.com')
		user = User.findInstance(email: 'test@test.com')
		assert user
	}

	protected void tearDown() {
		super.tearDown()

		User.mongoCollection.drop()
		Project.mongoCollection.drop()
		ProjectInvite.mongoCollection.drop()
	}

	void testJoinAlreadyMember() {
		user.roles = ["MEMBER_${project.id}".toString()]
		User.mongoCollection.update(user.mongoObject, user)
		assert user.isMember(project)

		assert 'Already a member' == projectService.join(project, user)
		assert 0 == ProjectInvite.mongoCollection.count()
	}

	void testJoinPublic() {
		project.priv = 1
		Project.mongoCollection.update(project.mongoObject, project)
		assert project.isPublic()

		assert 'Joined project' == projectService.join(project, user)
		assert user.isMember(project)
		assert 0 == ProjectInvite.mongoCollection.count()
	}

	void testJoinAlreadyInvited() {
		ProjectInvite.mongoCollection.add(email: user.email, projectId: project.id, invited: true)

		assert 'Joined project' == projectService.join(project, user)
		assert user.isMember(project)
		assert 0 == ProjectInvite.mongoCollection.count()
	}

	void testJoinAlreadyRequested() {
		ProjectInvite.mongoCollection.add(email: user.email, projectId: project.id, invited: false)

		assert 'Already requested to join' == projectService.join(project, user)
		assert !user.isMember(project)
		assert 1 == ProjectInvite.mongoCollection.count()
	}

	void testJoinRequest() {
		'Your join request was sent to the project admin' == projectService.join(project, user)
		assert !user.isMember(project)
		assert 1 == ProjectInvite.mongoCollection.count()
	}
}
