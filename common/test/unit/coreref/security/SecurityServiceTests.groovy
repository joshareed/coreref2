package coreref.security

import coreref.mongo.MongoService
import coreref.common.User
import com.mongodb.*

@TestFor(SecurityService)
class SecurityServiceTests {
	def mongoService

	@Before
	public void setUp() {
		mongoService = new MongoService()
		mongoService.map(User)
		assert User.mongoCollection

		service.mongoService = mongoService
	}

	@After
	public void tearDown() {
		User.mongoCollection.drop()
	}

	void testEncodePassword() {
		def pwd = service.encodePassword('test-pass')
		assert pwd
		assert pwd == service.encodePassword('test-pass')
	}

	void testUncap() {
		assert 'camelCase' == service.uncap('CamelCase')
	}

	void testAuthenticate() {
		def pass = service.encodePassword('test-pass')

		assert null == service.authenticate('test@example.com', 'test-pass')

		User.mongoCollection.add(email: 'text@example.com', password: pass, enabled: true)
		assert 1 == User.mongoCollection.count()

		def u = service.authenticate('text@example.com', 'test-pass')
		assert u
		assert u instanceof User
	}

	void testRequiredRoles() {
		service.initialize([[name: 'Test', clazz: TestController]])

		assert ['USER'] == service.getRequiredRoles('/test') as List
		assert ['EDITOR'] == service.getRequiredRoles('/test/method') as List
		assert ['ADMIN'] == service.getRequiredRoles('/test/secure') as List
		assert ['USER'] == service.getRequiredRoles('/test/foo') as List
		assert ['OWNER_100'] == service.getRequiredRoles('/test/owner/100')
		assert null == service.getRequiredRoles('/foo')
	}
}

@Secured('USER')
class TestController {

	def show = { }

	@Secured('ADMIN')
	def secure = { }

	@Secured('EDITOR')
	def method() { }

	@Secured('OWNER_')
	def owner = { }
}
