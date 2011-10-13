package coreref.security

import grails.test.*

import coreref.mongo.MongoService
import coreref.common.User
import com.mongodb.*

class SecurityServiceTests extends GrailsUnitTestCase {
	def service
	def mongoService

	protected void setUp() {
		super.setUp()

		mongoService = new MongoService()
		mongoService.map(User)
		assert User.mongoCollection

		service = new SecurityService()
		service.mongoService = mongoService
	}

	protected void tearDown() {
		super.tearDown()

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
		service.grailsApplication = [
			controllerClasses: [[name: 'Test', clazz: TestController]]
		]
		service.initialize()

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
