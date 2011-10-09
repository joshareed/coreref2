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

	void testLeaveNotAMember() {
		assert 'Not a member' == projectService.leave(project, user)
	}

	void testLeave() {
		user.addRole("MEMBER_${project.id}")
		assert user.isMember(project)

		assert 'Left the project' == projectService.leave(project, user)
		assert !user.isMember(project)
	}

	void testLeaveRemovesAllRoles() {
		user.addRole("MEMBER_${project.id}")
		assert user.isMember(project)

		user.addRole("EDITOR_${project.id}")
		assert user.isEditor(project)

		assert 'Left the project' == projectService.leave(project, user)
		assert !user.isMember(project)
		assert !user.isEditor(project)
	}

	void testKick() {
		user.addRole("MEMBER_${project.id}")
		assert user.isMember(project)

		assert projectService.kick(project, user)
		assert !user.isMember(project)

		def invite = ProjectInvite.find(email: user.email, projectId: project.id)
		assert invite
		assert invite.blocked
	}

	void testProcessInvites() {
		assert !user.isMember(project)

		ProjectInvite.mongoCollection.add(email: user.email, projectId: project.id, invited: true)
		projectService.processInvites(user)

		assert user.isMember(project)
	}

	void testProcessInvitesNotInvited() {
		assert !user.isMember(project)

		ProjectInvite.mongoCollection.add(email: user.email, projectId: project.id)
		projectService.processInvites(user)

		assert !user.isMember(project)
	}

	void testInvite() {
		assert !user.isMember(project)

		ProjectInvite.mongoCollection.add(email: 'foo@bar.com', projectId: project.id, invited: false)
		assert 1 == ProjectInvite.mongoCollection.count()

		projectService.invite(project, ['test@test.com', 'foo@bar.com', 'bar@baz.com'])
		user = User.getInstance(user.id)
		assert user.isMember(project)
		assert 2 == ProjectInvite.mongoCollection.count()
		assert ProjectInvite.find(email: 'foo@bar.com', projectId: project.id, invited: true)
		assert ProjectInvite.find(email: 'bar@baz.com', projectId: project.id, invited: true)
	}
}
