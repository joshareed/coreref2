package coreref.common

class ProjectTests {
	def mongoService

	@Before
	public void setUp() {
		mongoService = new coreref.mongo.MongoService()
		mongoService.map(Project)
		mongoService.map(User)
	}

	@After
	public void tearDown() {
		Project.mongoCollection.drop()
		User.mongoCollection.drop()
	}

	void testCreateNoMetadata() {
		def proj = new Project(projectId: 'test-proj', ownerId: 'owner', name: 'Test Project', priv: Project.PUBLIC, desc: 'The description')
		assert null == proj.id
		assert 'test-proj' == proj.projectId
		assert 'owner' == proj.ownerId
		assert 'Test Project' == proj.name
		assert 'The description' == proj.desc
		assert Project.PUBLIC == proj.priv
		assert [:] == proj.metadata
	}

	void testCreateWithMetadataMap() {
		def proj = new Project(projectId: 'test-proj', ownerId: 'owner', name: 'Test Project', desc: 'The description', priv: Project.PRIVATE, metadata: [opt: true])
		assert null == proj.id
		assert 'test-proj' == proj.projectId
		assert 'owner' == proj.ownerId
		assert 'Test Project' == proj.name
		assert 'The description' == proj.desc
		assert Project.PRIVATE == proj.priv
		assert [opt: true] == proj.metadata
	}

	void testCreateWithMetadataKeys() {
		def proj = new Project(projectId: 'test-proj', ownerId: 'owner', name: 'Test Project', desc: 'The description', priv: Project.STEALTH, 'metadata.opt': true)
		assert null == proj.id
		assert 'test-proj' == proj.projectId
		assert 'owner' == proj.ownerId
		assert 'Test Project' == proj.name
		assert 'The description' == proj.desc
		assert Project.STEALTH == proj.priv
		assert [opt: true] == proj.metadata
	}

	void testCreateWithOwner() {
		def user = new User(id: 'user')
		def proj = new Project(owner: user)
		assert 'user' == proj.ownerId
	}

	void testGetSetOwner() {
		User.mongoCollection.add(firstName: 'Test', lastName: 'User', email: 'test@test.com')
		def user = User.findInstance(email: 'test@test.com')

		def proj = new Project(projectId: 'test-proj', ownerId: 'owner', name: 'Test Project')
		assert 'owner' == proj.ownerId

		proj.owner = user
		assert user.id == proj.ownerId
		assert user.id == proj.owner.id
	}

	void testGetMembers() {
		Project.mongoCollection.add(projectId: 'test-proj', ownerId: 'owner', name: 'Test Project')

		def proj = Project.findInstance(projectId: 'test-proj')
		assert proj

		User.mongoCollection.add(firstName: 'Test', lastName: 'User', email: 'test@test.com', roles: ['MEMBER_' + proj.id])
		def members = proj.members
		assert 1 == members.size()
		assert members[0] instanceof User
	}

	void testToMap() {
		def proj = new Project(projectId: 'test-proj', ownerId: 'owner', name: 'Test Project', desc: 'The description', 'metadata.opt': true)
		assert [projectId: 'test-proj', ownerId: 'owner', name: 'Test Project', desc: 'The description', priv: 1, metadata: [opt:true]] == proj.toMap()
	}

	void testToString() {
		def proj = new Project(projectId: 'test-proj', ownerId: 'owner', name: 'Test Project', desc: 'The description', 'metadata.opt': true)
		assert 'Test Project (test-proj)' == proj.toString()
	}

	void testGetErrors() {
		assert [ownerId: 'Required field', projectId: 'Required field', name: 'Required field'] == new Project().errors
		assert [projectId: 'Required field', name: 'Required field'] == new Project(ownerId: 'owner').errors
		assert [name: 'Required field'] == new Project(ownerId: 'owner', projectId: 'projectId').errors
		assert [:] == new Project(ownerId: 'owner', projectId: 'projectId', name: 'name').errors

		Project.mongoCollection.add(projectId: 'test')

		assert "'test' already in use" == new Project(ownerId: 'owner', projectId: 'test', name: 'name').errors.projectId
	}

	void testIsPublic() {
		assert !new Project(priv: 2).isPublic()
		assert new Project().isPublic()
	}
}
