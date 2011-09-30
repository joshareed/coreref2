package coreref

class HomeController {
	def activityService

    def index = {
		if (session.user) {
			flash.message = flash.message
			flash.error = flash.error
			redirect action: 'dashboard'
		}
	}

	def dashboard = {
		[user: session.user, feed: activityService.homeFeed(session.user)]
	}
}
