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

		// check visibility
		if (project.isPublic() || (session.user && session.user.canView(project))) {
			return closure.call(project)
		}

		// not public and not logged in, so prompt a login
		if (!session.user) {
			flash.message = 'You must be logged in to see this project'
			session['login-forward-uri'] = request.forwardURI
			redirect controller: 'login'
			return
		} else {
			render view: 'private', model: [project: project]
		}
	}

	def overview = {
		withProject(params) { project ->
			[project: project]
		}
	}

	@Secured('USER')
	def admin = {

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
