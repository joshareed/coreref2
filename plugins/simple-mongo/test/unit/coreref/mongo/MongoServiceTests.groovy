package coreref.mongo

import grails.test.*
import com.mongodb.*

class MongoServiceTests extends GrailsUnitTestCase {
	def mongoService
	def mongo

    protected void setUp() {
        super.setUp()
		mongoService = new MongoService()
		mongo = new MockMongo()
		mongoService._mongo = mongo
    }

    protected void tearDown() {
        super.tearDown()
    }

	void testGetCollection() {
		mongo.collections.test = []
		def coll = mongoService.getCollection('test')
		assert null != coll
	}

	void testGetCollectionNoName() {
		assert null == mongoService.getCollection('')
		assert null == mongoService.getCollection(null)
	}

	void testGetCollectionDoesNotExist() {
		mongo.collections.test = []
		def coll = mongoService.getCollection('doesnotexist')
		assert null == coll
	}

	void testPropertyMissing() {
		mongo.collections.test = []
		assert null != mongoService.test
		assert null == mongoService.doesnotexist
	}

	void testMap() {
		// not set to begin with
		try {
			(new TestObject()).mongoService
			assert false
		} catch (e) {
			assert true
		}

		// map
		mongoService.map(TestObject)

		// test static methods are injected
		assert mongoService == TestObject.mongoService
		assert null != TestObject.mongoCollection
		assert null == TestObject.getMongoObject('someid')
		assert null == TestObject.getInstance('someid')

		// test instance methods are injected
		def obj = new TestObject()
		assert mongoService == obj.getMongoService()
		assert null != obj.mongoCollection
		assert null == obj.mongoObject
	}
}

class MockMongo {
	def servers
	def collections = [:]
	def db = [
		getCollection: { name -> collections[name] },
		collectionExists: { name -> (collections[name] != null) },
		createCollection: { name, map -> collections[name] = [] }
	]

	MockMongo(servers) {
		this.servers = servers
	}

	def getDB(name) {
		db.collectionNames = collections.collect { it.key }
		return db
	}
}

class TestObject {
	String name

	static mongo = [
		collection: '_test_object',
		index: []
	]
}
