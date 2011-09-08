package coreref.security

class SecurityTagLib {
	static namespace = 'sec'

	def ifLoggedIn = { attrs, body ->
		if (session.user != null) {
			out << body()
		}
	}

	def ifNotLoggedIn = { attrs, body ->
		if (session.user == null) {
			out << body()
		}
	}
}
