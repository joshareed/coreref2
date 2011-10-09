package coreref

import coreref.security.Secured
import coreref.common.*

@Secured('USER')
class ProjectAdminController {
	private static final EMAIL = ~/(?i)\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}\b/
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

		// check if user
		def user = session.user
		if (!user) {
			// NOTE: this should only occur during development
			session['login-forward-uri'] = request.forwardURI
			redirect controller: 'login'
			return
		}

		// check if admin
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
			[project: project, pending: ProjectInvite.findAllInstances(projectId: project.id, blocked: false)]
		}
	}

	def update = {
		withProject(params) { project ->
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
		}
	}

	def invite = {
		withProject(params) { project ->
			def emails = (params.invites =~ EMAIL).collect { it }
			if (emails) {
				projectService.invite(project, emails)
				flash.message = 'Invites sent'
				redirect action: 'members', id: project.projectId
			} else {
				redirect action: 'members', id: project.projectId
			}
		}
	}
}
