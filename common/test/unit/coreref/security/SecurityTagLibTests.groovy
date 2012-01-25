package coreref.security

@TestFor(SecurityTagLib)
class SecurityTagLibTests {

	void testIfLoggedIn() {
		tagLib.session.user = 'User'

		def out = tagLib.ifLoggedIn([:], { -> 'Logged In' }).toString()
		assert 'Logged In' == out
	}

	void testIfNotLoggedIn() {
		def out = tagLib.ifNotLoggedIn([:], { -> 'Not Logged In' }).toString()
		assert 'Not Logged In' == out
	}
}
