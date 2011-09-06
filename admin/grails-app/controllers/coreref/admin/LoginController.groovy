package coreref.admin

class LoginController {
	def securityService

    def index = { }

	def auth = {
		session.user = securityService.authenticate(params.email, params.password)
		if (session.user) {
			if (session['login-forward-uri']) {
				redirect url: session['login-forward-uri']
			} else {
				redirect uri: '/'
			}
		} else {
			flash.error = 'Invalid user or password'
			render view: 'index', model: [user: [email: params.email]]
		}
	}

	def logout = {
		session.user = null
		redirect uri: '/'
	}
}
