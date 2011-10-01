package coreref.common

import grails.test.*

class ProjectTests extends GrailsUnitTestCase {
	def mongoService

	protected void setUp() {
		super.setUp()
		mongoService = new coreref.mongo.MongoService()
		mongoService.map(Project)
	}

	protected void tearDown() {
		super.tearDown()
		Project.mongoCollection.drop()
	}

	void testCreateNoMetadata() {
		def proj = new Project(projectId: 'test-proj', ownerId: 'owner', name: 'Test Project', priv: 1, desc: 'The description')
		assert null == proj.id
		assert 'test-proj' == proj.projectId
		assert 'owner' == proj.ownerId
		assert 'Test Project' == proj.name
		assert 'The description' == proj.desc
		assert 1 == proj.priv
		assert [:] == proj.metadata
	}

	void testCreateWithMetadataMap() {
		def proj = new Project(projectId: 'test-proj', ownerId: 'owner', name: 'Test Project', desc: 'The description', priv: 2, metadata: [opt: true])
		assert null == proj.id
		assert 'test-proj' == proj.projectId
		assert 'owner' == proj.ownerId
		assert 'Test Project' == proj.name
		assert 'The description' == proj.desc
		assert 2 == proj.priv
		assert [opt: true] == proj.metadata
	}

	void testCreateWithMetadataKeys() {
		def proj = new Project(projectId: 'test-proj', ownerId: 'owner', name: 'Test Project', desc: 'The description', priv: 3, 'metadata.opt': true)
		assert null == proj.id
		assert 'test-proj' == proj.projectId
		assert 'owner' == proj.ownerId
		assert 'Test Project' == proj.name
		assert 'The description' == proj.desc
		assert 3 == proj.priv
		assert [opt: true] == proj.metadata
	}

	void testSave() {
		def proj = new Project(projectId: 'test-proj', ownerId: 'owner', name: 'Test Project', desc: 'The description', 'metadata.opt': true)
		assert [projectId: 'test-proj', ownerId: 'owner', name: 'Test Project', desc: 'The description', priv: 1, metadata: [opt:true]] == proj.toMap()
	}

	void testToString() {
		def proj = new Project(projectId: 'test-proj', ownerId: 'owner', name: 'Test Project', desc: 'The description', 'metadata.opt': true)
		assert 'Test Project (test-proj)' == proj.toString()
	}

	void testErrors() {
		assert [ownerId: 'Required field', projectId: 'Required field', name: 'Required field'] == new Project().errors
		assert [projectId: 'Required field', name: 'Required field'] == new Project(ownerId: 'owner').errors
		assert [name: 'Required field'] == new Project(ownerId: 'owner', projectId: 'projectId').errors
		assert [:] == new Project(ownerId: 'owner', projectId: 'projectId', name: 'name').errors

		Project.mongoCollection.add(projectId: 'test')

		assert "'test' already in use" == new Project(ownerId: 'owner', projectId: 'test', name: 'name').errors.projectId
	}
}
