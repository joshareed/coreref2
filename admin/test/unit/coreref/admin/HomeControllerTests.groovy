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
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testIndex() {
		def map = controller.index()
		assert 3 == map.size()
		assert map.containsKey('applications')
		assert map.containsKey('users')
		assert map.containsKey('projects')
    }
}
