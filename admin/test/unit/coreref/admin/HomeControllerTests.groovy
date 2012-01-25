package coreref.admin

import coreref.common.*
import coreref.mongo.MongoService

@TestFor(HomeController)
class HomeControllerTests {

	@Before
    public void setUp() {
		def mongoService = new MongoService()
		mongoService.map(Application)
		mongoService.map(User)
		mongoService.map(Project)
		mongoService.map(Activity)
		controller.activityService = new ActivityService()
    }

    void testIndex() {
		def map = controller.index()
		assert 1 == map.size()
		assert map.containsKey('feed')
    }
}
