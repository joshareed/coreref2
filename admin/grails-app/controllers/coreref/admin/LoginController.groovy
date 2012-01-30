package coreref.admin

import coreref.common.User

class LoginController {
	def securityService

    def index = {
		if (User.mongoCollection.count() == 0) {
			flash.message = 'Create admin user'
			redirect controller: 'user', action: 'create'
			return
		}
	}

	def auth = {
		session.user = securityService.authenticate(params.email, params.password)
		if (session.user) {
			redirect uri: (session['login-forward-uri'] ?: '/')
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
