package coreref

import grails.test.*
import coreref.common.*

class LoginControllerTests extends ControllerUnitTestCase {
	def mongoService

	protected void setUp() {
		super.setUp()

		mongoService = new coreref.mongo.MongoService()
		mongoService.map(User)
		User.mongoCollection.add(email: 'test@test.com', password: '')
	}

	protected void tearDown() {
		super.tearDown()

		User.mongoCollection.drop()
	}

	void testIndex() {
		assert null == controller.index()
	}

	void testAuthInvalid() {
		controller.securityService = [authenticate: { email, password -> null }]

		controller.params.putAll(email: 'test@test.com', password: 'password')
		controller.auth()

		assert 'Invalid user or password' == controller.flash.error
		assert 'index' == controller.renderArgs.view
		assert [user: [email: 'test@test.com']] == controller.renderArgs.model
	}

	void testAuth() {
		controller.securityService = [authenticate: { email, pass -> User.findInstance(email: 'test@test.com') }]

		controller.params.putAll(email: 'test@test.com', password: '')
		controller.auth()

		assert [uri: '/'] == controller.redirectArgs
		assert controller.session.user
		assert null != controller.session.user.lastLogin
		assert null != controller.session['user-last-login']
	}

	void testAuthForwards() {
		controller.securityService = [authenticate: { email, pass -> User.findInstance(email: 'test@test.com') }]

		controller.params.putAll(email: 'test@test.com', password: '')
		controller.session['login-forward-uri'] = '/forward'
		controller.auth()

		assert [url: '/forward'] == controller.redirectArgs
		assert controller.session.user
		assert null != controller.session.user.lastLogin
		assert null != controller.session['user-last-login']
	}

	void testLogout() {
		controller.session.user = 'Some User'
		controller.logout()
		assert null == controller.session.user
		assert [uri: '/'] == controller.redirectArgs
	}
}
