package coreref

import coreref.common.*

@TestFor(LoginController)
class LoginControllerTests {
	def mongoService

	@Before
	public void setUp() {
		mongoService = new coreref.mongo.MongoService()
		mongoService.map(User)
		User.mongoCollection.add(email: 'test@test.com', password: '')
	}

	@After
	public void tearDown() {
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
		assert '/login/index' == view
		assert [user: [email: 'test@test.com']] == model
	}

	void testAuth() {
		controller.securityService = [authenticate: { email, pass -> User.findInstance(email: 'test@test.com') }]

		controller.params.putAll(email: 'test@test.com', password: '')
		controller.auth()

		assert '/' == response.redirectedUrl
		assert controller.session.user
		assert null != controller.session.user.lastLogin
		assert null != controller.session['user-last-login']
	}

	void testAuthForwards() {
		controller.securityService = [authenticate: { email, pass -> User.findInstance(email: 'test@test.com') }]

		controller.params.putAll(email: 'test@test.com', password: '')
		controller.session['login-forward-uri'] = '/forward'
		controller.auth()

		assert '/forward' == response.redirectedUrl
		assert controller.session.user
		assert null != controller.session.user.lastLogin
		assert null != controller.session['user-last-login']
	}

	void testLogout() {
		controller.session.user = 'Some User'
		controller.logout()
		assert null == controller.session.user
		assert '/' == response.redirectedUrl
	}
}
