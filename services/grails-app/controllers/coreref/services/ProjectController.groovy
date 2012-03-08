package coreref.services

import grails.converters.JSON

class ProjectController {
	def grailsApplication

    def index(String type, String id) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Methods"));
		response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
		response.setHeader("Access-Control-Max-Age", "86400");

		if (grailsApplication.mainContext.containsBean("${type}ProjectService")) {
			def projectService = grailsApplication.mainContext.getBean("${type}ProjectService")
			render projectService.getProject(id) as JSON
		} else {
			response.sendError 400, "Invalid project type: ${type}"
		}
	}
}
