package coreref

import coreref.common.User

class LoginController {
    def securityService

    def index = { }

	def auth = {
		def user = securityService.authenticate(params.email, params.password)
		session.user = user
		if (session.user) {
			// update the last login
			session['user-last-login'] = (user?.lastLogin ?: new Date())
			user.lastLogin = new Date()
			User.mongoCollection.update(user.mongoObject, user)

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
