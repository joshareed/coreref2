package coreref.admin

import coreref.common.*
import coreref.security.Secured

@Secured('ADMIN')
class HomeController {
	def activityService

    def index = {
		[ feed: activityService.adminFeed() ]
	}
}
