package coreref

class HomeController {

    def index = {
		if (session.user) {
			redirect action: 'dashboard'
		}
	}

	def dashboard = {
		[user: session.user]
	}
}
