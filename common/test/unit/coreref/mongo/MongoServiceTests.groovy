package coreref.mongo

import com.mongodb.*

class MongoServiceTests {
	def mongoService
	def obj

	@Before
	public void setUp() {
		mongoService = new MongoService()
		mongoService.map(TestObject)

		def col = mongoService.getCollection('_test_object')
		assert col.insert([name: 'Test'] as BasicDBObject)

		def i = col.findOne([name: 'Test'] as BasicDBObject)
		assert i
		obj = new TestObject(i)
	}

	@After
	public void tearDown() {
		mongoService.getCollection('_test_object').drop()
	}

	void testGetCollection() {
		def coll = mongoService.getCollection('_test_object')
		assert null != coll
	}

	void testGetCollectionNoName() {
		assert null == mongoService.getCollection('')
		assert null == mongoService.getCollection(null)
	}

	void testGetCollectionDoesNotExist() {
		def coll = mongoService.getCollection('doesnotexist')
		assert null == coll
	}

	void testPropertyMissing() {
		assert null != mongoService._test_object
		assert null == mongoService.doesnotexist
	}

	void testMapAddsStaticMethods() {
		assert mongoService == TestObject.mongoService
		assert null != TestObject.mongoCollection
		assert null != TestObject.getMongoObject(obj.id)
		assert null != TestObject.getInstance(obj.id)
		assert null == TestObject.getMongoObject('someid')
		assert null == TestObject.getInstance('someid')
		assert 1 == TestObject.list().size()
		assert TestObject.listInstances()[0] instanceof TestObject
	}

	void testMappAddsInstanceMethods() {
		assert mongoService == obj.mongoService
		assert null != obj.mongoCollection
		assert null != obj.mongoObject
		obj.id = ''
		assert null == obj.mongoObject
	}

	void testDecoratesDBCollection() {
		def col = TestObject.mongoCollection

		// count
		assert 0 == col.count(name: 'Foo')
		assert 1 == col.count(name: 'Test')

		// get
		assert null == col.get('someid')
		assert null != col.get(obj.id)

		// list
		assert 1 == col.list().size()

		// find
		assert null == col.find(name: 'Foo')
		assert null != col.find(name: 'Test')

		// find w/ filter
		assert 'Test' == col.find(name: 'Test', [name: true]).name
		assert null == col.find(name: 'Test', [name: false]).name

		// findBy
		assert null != col.findByName('Test')

		// add
		col.add(name: 'Test')
		assert 2 == col.count(name: 'Test')

		// findAllBy
		assert 2 == col.findAllByName('Test').size()

		// findAll
		assert null != TestObject.mongoCollection.findAll(name: 'Test')
		def all = TestObject.mongoCollection.findAll([name: 'Test'], [name: true]).collect { it }
		assert 2 == all.size()
		assert 2 == all[0].size()
		assert all[0].containsKey('_id')
		assert 'Test' == all[0].name

		// update
		assert 0 == col.count(name: 'Josh')
		def instance = TestObject.findInstance(name: 'Test')
		instance.name = 'Josh'
		col.update(instance.mongoObject, instance.toMap())
		assert 1 == col.count(name: 'Josh')

		// method missing
		try {
			col.doesNotExist()
			assert false
		} catch (e) {
			assert true
		}
	}

	void testDecoratesDBCursor() {
		assert null == TestObject.mongoCollection.findAll(name: 'Foo').first()
		assert null != TestObject.mongoCollection.findAll(name: 'Test').first()
	}
}

class TestObject {
	String id
	String name

	TestObject(map = [:]) {
		id = map._id ?: map.id
		name = map.name
	}

	Map toMap(map = [:]) {
		map.name = name
		map
	}

	static mongo = [
		collection: '_test_object',
		index: ['name']
	]
}
