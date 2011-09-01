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
		mongoService.mongo = mongo
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
}

class MockMongo {
	def servers
	def collections = [:]
	def db = [
		getCollection: { name -> collections[name] }
	]

	MockMongo(servers) {
		this.servers = servers
	}

	def getDB(name) {
		db.collectionNames = collections.collect { it.key }
		return db
	}
}
