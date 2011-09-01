package coreref.security

import grails.test.*

class AuthenticationServiceTests extends GrailsUnitTestCase {
	def service

    protected void setUp() {
        super.setUp()
		service = new AuthenticationService()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testEncodePassword() {
		def pwd = service.encodePassword('test-pass')
		assert pwd
		assert pwd == service.encodePassword('test-pass')
    }
}
