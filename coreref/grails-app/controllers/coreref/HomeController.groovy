package coreref

import coreref.common.*
import coreref.security.Secured

class HomeController {
	def activityService

	def index = {
		if (session.user) {
			flash.message = flash.message
			flash.error = flash.error
			redirect action: 'dashboard'
		} else {
			[:]
		}
	}

	@Secured('USER')
	def dashboard = {
		[user: session.user, feed: activityService.homeFeed(session.user)]
	}
}
