package coreref

import coreref.security.Secured
import coreref.common.Project

class ProjectController {
	static defaultAction = 'overview'

	def activityService
	def lexiconService

	private def withProject = { Map map, closure ->
		def id = map.id
		if (!id) {
			flash.message = "No project id specified"
			redirect uri: '/'
			return
		}

		def project = Project.findInstance(projectId: id)
		if (!project) {
			project = Project.getInstance(id)
		}
		if (!project) {
			flash.message = "No project with id '${id}'"
			redirect uri: '/'
			return
		}

		// TODO: check visibility

		return closure.call(project)
	}

	def overview = {
		withProject(params) { project ->
			[project: project]
		}
	}

	@Secured('USER')
	def create = {
		[errors: [:]]
	}

	@Secured('USER')
	def save = {
		def project = new Project(params)
		def errors = project.errors
		if (!errors) {
			Project.mongoCollection.add(project)
			activityService.logProjectCreated(session.user, Project.findInstance(projectId: project.projectId))
			flash.message = "Project ${project.projectId} created"
			redirect controller: 'project', action: 'overview', id: project.projectId
		} else {
			flash.message = 'Project has errors'
			render view: 'create', model: [project: project, errors: errors]
		}
	}

	def search = {

	}
}
