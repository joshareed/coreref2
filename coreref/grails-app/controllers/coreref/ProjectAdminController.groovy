package coreref

import coreref.security.Secured
import coreref.common.Project

@Secured('USER')
class ProjectAdminController {
	static defaultAction = 'info'

	def activityService
	def lexiconService
	def projectService

	private def withProject = { Map map, closure ->
		def id = map.id
		if (!id) {
			flash.error = "No project id specified"
			redirect uri: '/'
			return
		}

		def project = Project.findInstance(projectId: id)
		if (!project) {
			project = Project.getInstance(id)
		}
		if (!project) {
			flash.error = "No project with id '${id}'"
			redirect uri: '/'
			return
		}

		// check if admin
		def user = session.user
		if (user.isOwner(project)) {
			return closure.call(project)
		} else {
			flash.error = 'Not the project admin'
			redirect controller: 'project', action: 'overview', id: project.projectId
			return
		}
	}

	def info = {
		withProject(params) { project ->
			[project: project]
		}
	}

	def members = {
		withProject(params) { project ->
			[project: project]
		}
	}

	def update = {
		withProject(params) { project ->
			if (session.user.isOwner(project)) {
				def update = new Project(params)
				def errors = update.errors
				if (!errors) {
					def diff = project.toMap() - update.toMap()
					Project.mongoCollection.update(project.mongoObject, update)
					activityService.logProjectUpdated(session.user, project, diff)
					flash.message = "Project updated"
					redirect action: 'info', id: project.projectId
				} else {
					flash.message = 'Project has errors'
					render view: 'info', model: [project: update, errors: errors]
				}
			} else {
				flash.error = 'Not the project admin'
				redirect controller: 'project', action: 'overview', id: project.projectId
			}
		}
	}
}
