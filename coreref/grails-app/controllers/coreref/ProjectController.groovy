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
	def admin = {
		withProject(params) { project ->
			if (session.user.isOwner(project)) {
				switch(params.subaction) {
				case 'info':
					render view: 'admin/info', model: [project: project]
					break
				case 'members':
					render view: 'admin/members', model: [project: project]
					break
				default:
					redirect action: 'admin', id: project.projectId, params: [subaction: 'info']
				}
			} else {
				flash.error = 'Not the project admin'
				redirect controller: 'project', action: 'overview', id: project.projectId
			}
		}
	}

	@Secured('USER')
	def adminUpdate = {
		withProject(params) { project ->
			if (session.user.isOwner(project)) {
				def update = new Project(params)
				def errors = update.errors
				if (!errors) {
					def diff = project.toMap() - update.toMap()
					Project.mongoCollection.update(project.mongoObject, update)
					activityService.logProjectUpdated(session.user, project, diff)
					flash.message = "Project updated"
					redirect action: 'admin', id: project.projectId, params: [subaction: 'info']
				} else {
					flash.message = 'Project has errors'
					render view: 'admin/info', model: [project: update, errors: errors]
				}
			} else {
				flash.error = 'Not the project admin'
				redirect controller: 'project', action: 'overview', id: project.projectId
			}
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
