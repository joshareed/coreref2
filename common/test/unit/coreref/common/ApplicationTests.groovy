package coreref.common

import grails.test.*

class ApplicationTests extends GrailsUnitTestCase {
	def mongoService

    protected void setUp() {
        super.setUp()
		mongoService = new coreref.mongo.MongoService()
		mongoService.map(Application)
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testRandomUUID() {
		assert null != Application.randomId()
    }

	void testCreate() {
		def app1 = new Application(id: 'id', appId: 'appId', contact: 'contact', enabled: true, site: 'site')
		assert 'id' == app1.id
		assert 'appId' == app1.appId
		assert 'contact' == app1.contact
		assert app1.enabled
		assert 'site' == app1.site
	}

	void testSave() {
		def app1 = new Application(id: 'id', appId: 'appId', contact: 'contact', enabled: true, site: 'site')
		assert [appId: 'appId', contact: 'contact', enabled: true, site: 'site'] == app1.save()
	}

	void testToString() {
		assert 'appId (contact)' == new Application(id: 'id', appId: 'appId', contact: 'contact', enabled: true, site: 'site').toString()
	}

	void testErrors() {
		assert [appId: 'Required field', contact: 'Required field'] == new Application().errors

		Application.mongoCollection.add(appId: 'app1', contact: 'contact')
		assert "'app1' already in use" == new Application(appId: 'app1', contact: 'contact').errors.appId
	}
}