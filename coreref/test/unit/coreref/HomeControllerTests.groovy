package coreref

import coreref.common.*

@TestFor(HomeController)
class HomeControllerTests {

	void testIndexNoUser() {
		assert [:] == controller.index()
	}

	void testIndexWithUser() {
		controller.session.user = 'Some User'
		controller.index()
		assert '/home/dashboard' == response.redirectedUrl
	}

	void testDashboard() {
		controller.session.user = 'Some User'
		controller.activityService = [homeFeed: { u -> ['mocked'] }]
		def map = controller.dashboard()
		assert [user: 'Some User', feed: ['mocked']] == map
	}
}
