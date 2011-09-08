class SecurityFilters {
	def securityService

	def filters = {
		loginCheck(controller:'login', invert: true) {
			before = {
				def roles = securityService.getRequiredRoles(request.forwardURI - request.contextPath)
				if (roles) {
					def user = session.user
					if (user) {
						if (roles.disjoint(user.roles ?: [])) {
							flash.error = 'You are not authorized to access this page'
						} else {
							return true
						}
					}

					// redirect to the login page
					session['login-forward-uri'] = request.forwardURI
					redirect controller: 'login'
					return false
				}
			}
		}
	}
}