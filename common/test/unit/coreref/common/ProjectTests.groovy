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
		def proj = new Project(projectId: 'test-proj', ownerId: 'owner', name: 'Test Project', description: 'The description')
		assert null == proj.id
		assert 'test-proj' == proj.projectId
		assert 'owner' == proj.ownerId
		assert 'Test Project' == proj.name
		assert 'The description' == proj.description
		assert [:] == proj.metadata
	}

	void testCreateWithMetadataMap() {
		def proj = new Project(projectId: 'test-proj', ownerId: 'owner', name: 'Test Project', description: 'The description', metadata: [opt: true])
		assert null == proj.id
		assert 'test-proj' == proj.projectId
		assert 'owner' == proj.ownerId
		assert 'Test Project' == proj.name
		assert 'The description' == proj.description
		assert [opt: true] == proj.metadata
	}

	void testCreateWithMetadataKeys() {
		def proj = new Project(projectId: 'test-proj', ownerId: 'owner', name: 'Test Project', description: 'The description', 'metadata.opt': true)
		assert null == proj.id
		assert 'test-proj' == proj.projectId
		assert 'owner' == proj.ownerId
		assert 'Test Project' == proj.name
		assert 'The description' == proj.description
		assert [opt: true] == proj.metadata
	}

	void testSave() {
		def proj = new Project(projectId: 'test-proj', ownerId: 'owner', name: 'Test Project', description: 'The description', 'metadata.opt': true)
		assert [projectId: 'test-proj', ownerId: 'owner', name: 'Test Project', description: 'The description', metadata: [opt:true]] == proj.save()
	}

	void testToString() {
		def proj = new Project(projectId: 'test-proj', ownerId: 'owner', name: 'Test Project', description: 'The description', 'metadata.opt': true)
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
