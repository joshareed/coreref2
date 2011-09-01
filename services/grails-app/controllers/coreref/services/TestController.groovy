package coreref.services

class TestController {
	def mongoService

    def index = {
		render "Mongo: ${mongoService}"
	}
}
