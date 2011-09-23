package coreref

import coreref.common.Project

class ProjectController {
	def lexiconService

	private getProjects() { Project.mongoCollection }
	private def withProject = { Map map, closure ->
		def id = map.id
		if (!id) {
			flash.message = "No project id specified"
			redirect uri: '/'
			return
		}

		def project = projects.find(projectId: id)
		if (!project) {
			project = projects.get(id)
			if (!project) {
				flash.message = "No project with id '${id}'"
				redirect uri: '/'
				return
			}
		}

		return closure.call(new Project(project))
	}

    def overview = {
		withProject(params) { project ->
			[project: project]
		}
	}
}
