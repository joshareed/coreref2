class SecurityFilters {
	def filters = {
		loginCheck(controller:'login', invert: true) {
			before = {
				if (!session.user) {
					session['login-forward-uri'] = request.forwardURI
					redirect controller: 'login'
					return false
				}
			}
		}
	}
}