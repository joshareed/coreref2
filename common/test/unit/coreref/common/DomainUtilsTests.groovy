package coreref.common

class DomainUtilsTests {
	def mongoService

	@Before
	public void setUp() {
		mongoService = new coreref.mongo.MongoService()
	}

	void testCoerceBoolean() {
		assert !DomainUtils.coerceBoolean(null)
		assert DomainUtils.coerceBoolean(null, true)
		assert DomainUtils.coerceBoolean(true)
		assert DomainUtils.coerceBoolean('true')
		assert DomainUtils.coerceBoolean('on')
		assert !DomainUtils.coerceBoolean('false')
	}

	void testCoerceList() {
		assert [] == DomainUtils.coerceList(null)
		assert ['Test'] == DomainUtils.coerceList(null, ['Test'])
		assert ['Test'] == DomainUtils.coerceList(['Test'])
		assert ['Test1', 'Test2'] == DomainUtils.coerceList('Test1, Test2')
	}

	void testIsSet() {
		def i1 = new TestObject(name: 'Test')
		def i2 = new TestObject(name: '')
		def i3 = new TestObject(name: null)

		assert DomainUtils.isSet(i1, 'name')
		assert !DomainUtils.isSet(i2, 'name')
		assert !DomainUtils.isSet(i3, 'name')
		assert !DomainUtils.isSet(i1, 'foo')
	}

	void testIsUnique() {
		mongoService.map(Application)
		Application.mongoCollection.add(new Application(appId: 'app1', contact: 'contact'))
		Application.mongoCollection.add(new Application(appId: 'app2', contact: 'contact'))

		assert !DomainUtils.isUnique(new Application(appId: 'app2'), 'appId', Application.mongoCollection)
		assert DomainUtils.isUnique(new Application(appId: 'app3'), 'appId', Application.mongoCollection)

		def app1 = new Application(Application.mongoCollection.find(appId: 'app1'))
		app1.appId = 'app2'
		assert !DomainUtils.isUnique(app1, 'appId', Application.mongoCollection)
		app1.appId = 'app3'
		assert DomainUtils.isUnique(app1, 'appId', Application.mongoCollection)
	}
}

class TestObject {
	String name
}
