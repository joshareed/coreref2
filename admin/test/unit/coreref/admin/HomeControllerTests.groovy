package coreref.admin

import grails.test.*

import coreref.common.*
import coreref.mongo.MongoService

class HomeControllerTests extends ControllerUnitTestCase {
    protected void setUp() {
        super.setUp()

		def mongoService = new MongoService()
		mongoService.map(Application)
		mongoService.map(User)
		mongoService.map(Project)
		mongoService.map(Activity)
		controller.activityService = new ActivityService()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testIndex() {
		def map = controller.index()
		assert 1 == map.size()
		assert map.containsKey('feed')
    }
}
