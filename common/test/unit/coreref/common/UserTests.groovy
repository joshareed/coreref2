package coreref.common

class UserTests {
	def mongoService

	@Before
	public void setUp() {
		mongoService = new coreref.mongo.MongoService()
		mongoService.map(User)
		mongoService.map(Project)
	}

	@After
	public void tearDown() {
		User.mongoCollection.drop()
		Project.mongoCollection.drop()
	}

	void testCreate() {
		def user = new User(id: 'id', firstName: 'firstName', lastName: 'lastName', email: 'email', password: 'password', roles: ['ADMIN'], enabled: true)

		assert 'id' == user.id
		assert 'firstName' == user.firstName
		assert 'lastName' == user.lastName
		assert 'email' == user.email
		assert 'password' == user.password
		assert ['ADMIN'] == user.roles
		assert user.enabled
	}

	void testToMap() {
		def user = new User(id: 'id', firstName: 'firstName', lastName: 'lastName', email: 'email', password: 'password', roles: ['ADMIN'], enabled: true)

		assert [
			firstName: 'firstName',
			lastName: 'lastName',
			email: 'email',
			password: 'password',
			roles: ['ADMIN'],
			enabled: true,
			lastLogin: null
		] == user.toMap()
	}

	void testToString() {
		def user = new User(id: 'id', firstName: 'firstName', lastName: 'lastName', email: 'email', password: 'password', roles: ['ADMIN'], enabled: true)
		assert 'firstName lastName' == user.toString()
	}

	void testGetErrors() {
		assert [firstName: 'Required field', lastName: 'Required field', email: 'Required field', password: 'Required field'] == new User().errors
		assert [lastName: 'Required field', email: 'Required field', password: 'Required field'] == new User(firstName: 'firstName').errors
		assert [email: 'Required field', password: 'Required field'] == new User(firstName: 'firstName', lastName: 'lastName').errors
		assert [password: 'Required field'] == new User(email: 'email', firstName: 'firstName', lastName: 'lastName').errors
		assert [:] == new User(email: 'email', firstName: 'firstName', lastName: 'lastName', password: 'password').errors

		User.mongoCollection.add(email: 'test@example.com')

		assert "'test@example.com' already in use" == new User(firstName: 'Test', lastName: 'Last', password: 'password', email: 'test@example.com').errors.email
	}

	void testAddRemoveHasRole() {
		def user = new User(roles: ['ADMIN'])
		assert user.hasRole('ADMIN')
		assert !user.hasRole('EDITOR')
		user.addRole('EDITOR')
		assert user.hasRole('EDITOR')
		assert 2 == user.roles.size()
		user.addRole('EDITOR')
		assert 2 == user.roles.size()
		user.removeRole('EDITOR')
		assert !user.hasRole('EDITOR')
		assert 1 == user.roles.size()
	}

	void testIsAdmin() {
		def user = new User(roles: ['ADMIN'])
		assert user.isAdmin()
		assert !new User(roles: []).isAdmin()
	}

	void testProjectRoles() {
		def user1 = new User(id: 'user1', roles: ['MEMBER_test', 'EDITOR_test'])
		def project = new Project(ownerId: 'user1', id: 'test', priv: 2)

		assert user1.isMember(project)
		assert user1.isEditor(project)
		assert user1.isOwner(project)
		assert user1.canView(project)

		def user2 = new User(id: 'user2', roles: [])
		assert !user2.isMember(project)
		assert !user2.isEditor(project)
		assert !user2.isOwner(project)
		assert !user2.canView(project)
	}

	void testGetProjects() {
		def user = new User(id: 'user')
		assert !user.projects

		Project.mongoCollection.add(projectId: 'test', ownerId: 'user')
		Project.mongoCollection.add(projectId: 'test2', ownerId: 'user')
		assert 2 == user.projects.size()
	}

	void testGetMemberProjects() {
		assert 0 == Project.mongoCollection.count()
		def user = new User(id: 'user', roles: [])
		assert !user.memberProjects

		Project.mongoCollection.add(projectId: 'test1', ownerId: 'foo')
		Project.mongoCollection.add(projectId: 'test2', ownerId: 'foo')
		Project.findAllInstances(ownerId: 'foo').each {
			user.addRole("MEMBER_${it.id}")
		}

		assert 2 == user.memberProjects.size()
	}
}
