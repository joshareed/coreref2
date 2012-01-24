package coreref.common

@TestFor(ActivityService)
class ActivityServiceTests {
	def mongoService
	def service

	def u1, u2, p

	@Before
	public void setUp() {
		mongoService = new coreref.mongo.MongoService()
		mongoService.map(Activity)
		mongoService.map(User)
		mongoService.map(Project)
		service = new ActivityService()
	}

	@After
	public void tearDown() {
		Activity.mongoCollection.drop()
		User.mongoCollection.drop()
		Project.mongoCollection.drop()
	}

	void testLog() {
		assert 0 == Activity.mongoCollection.count()
		service.log('user', 'action', 'project', 'text')
		def a = Activity.mongoCollection.findOne()
		assert a
		assert 'user' == a.userId
		assert 'action' == a.action
		assert 'project' == a.projectId
		assert 'text' == a.data
		assert null != a.ts
	}

	void testLogCreateProject() {
		assert 0 == Activity.mongoCollection.count()
		service.logProjectCreated('user', 'project')
		def a = Activity.mongoCollection.findOne()
		assert a
		assert 'user' == a.userId
		assert 'created' == a.action
		assert 'project' == a.projectId
		assert null == a.data
		assert null != a.ts
	}

	void testId() {
		assert null == service.id(null)
		assert null == service.id([])

		assert 'user' == service.id('user')

		assert 'project' == service.id(new Project(id: 'project'))
	}

	private void setupUsersAndProjects() {
		User.mongoCollection.add(firstName: 'User', lastName: 'One', email: 'one@example.com', password: 'password')
		User.mongoCollection.add(firstName: 'User', lastName: 'Two', email: 'two@example.com', password: 'password')

		u1 = User.findInstance(email: 'one@example.com')
		assert u1
		u2 = User.findInstance(email: 'two@example.com')
		assert u2

		Project.mongoCollection.add(projectId: 'test-project', name: 'Test Project', ownerId: u1.id)
		p = Project.findInstance(projectId: 'test-project')
		assert p

		service.log(u1, 'created', p, 'some text')
	}

	void testUserFeed() {
		setupUsersAndProjects()
		assert 1 == service.userFeed(u1).size()
		assert 0 == service.userFeed(u2).size()
	}

	void testProjectFeed() {
		setupUsersAndProjects()
		assert 1 == service.projectFeed(p).size()
		assert 0 == service.projectFeed('does not exist').size()
	}

	void testHomeFeed() {
		setupUsersAndProjects()
		assert 1 == service.homeFeed(u1).size()
		assert 0 == service.homeFeed(u2).size()

		u2.addRole("MEMBER_${p.id}")
		assert 1 == service.homeFeed(u2).size()
	}
}
