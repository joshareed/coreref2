package coreref.security

import grails.test.*

class SecurityTagLibTests extends TagLibUnitTestCase {
	protected void setUp() {
		super.setUp()
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testIfLoggedIn() {
		tagLib.session.user = 'User'

		tagLib.ifLoggedIn([:], { -> 'Logged In' })
		assert 'Logged In' == tagLib.out.toString()
	}

	void testIfNotLoggedIn() {
		tagLib.ifNotLoggedIn([:], { -> 'Not Logged In' })
		assert 'Not Logged In' == tagLib.out.toString()
	}
}
