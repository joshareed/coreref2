package coreref

import grails.test.*
import coreref.common.*

class HomeControllerTests extends ControllerUnitTestCase {

	protected void setUp() {
		super.setUp()
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testIndexNoUser() {
		assert null == controller.index()
	}

	void testIndexWithUser() {
		controller.session.user = 'Some User'
		controller.index()
		assert [action: 'dashboard'] == controller.redirectArgs
	}

	void testDashboard() {
		controller.session.user = 'Some User'
		controller.activityService = [homeFeed: { u -> ['mocked'] }]
		def map = controller.dashboard()
		assert [user: 'Some User', feed: ['mocked']] == map
	}
}
