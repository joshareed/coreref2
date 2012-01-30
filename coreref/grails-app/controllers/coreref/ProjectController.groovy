package coreref

import coreref.security.Secured
import coreref.common.Project

class ProjectController {
	static defaultAction = 'overview'

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

		// not public and not logged in
		if (!project.isPublic() && !session.user) {
			flash.message = 'You must be logged in to see this project'
			session['login-forward-uri'] = request.forwardURI
			redirect controller: 'login'
			return
		}

		if (closure.maximumNumberOfParameters == 1) {
			return closure.call(project)
		} else {
			return closure.call(project, session.user ? session.user.canView(project) : true)
		}
	}

	def overview = {
		withProject(params) { project, canView ->
			if (canView) {
				[project: project]
			} else {
				render view: 'private', model: [project: project]
			}
		}
	}

	@Secured('USER')
	def join = {
		withProject(params) { project ->
			flash.message = projectService.join(project, session.user)
			redirect controller: 'project', action: 'overview', id: project.projectId
		}
	}

	@Secured('USER')
	def leave = {
		withProject(params) { project ->
			flash.message = projectService.leave(project, session.user)
			redirect controller: 'project', action: 'overview', id: project.projectId
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
			projectService.index(project)
			activityService.logProjectCreated(session.user, Project.findInstance(projectId: project.projectId))
			flash.message = "Project ${project.projectId} created"
			redirect controller: 'project', action: 'overview', id: project.projectId
		} else {
			flash.message = 'Project has errors'
			render view: 'create', model: [project: project, errors: errors]
		}
	}

	def search = {
		def results = params.q ? projectService.search(params.q) : []
		[q: params?.q, results: results]
	}
}
