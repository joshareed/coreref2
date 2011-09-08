package coreref.admin

import coreref.common.*
import coreref.security.Secured

@Secured('ADMIN')
class HomeController {
	def mongoService

    def index = {
		[
			applications: Application.mongoCollection.count(),
			users: User.mongoCollection.count(),
			projects: Project.mongoCollection.count()
		]
	}
}
