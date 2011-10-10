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
			[project: project, pending: projectService.pending(project)]
		}
	}

	def update = {
		withProject(params) { project ->
			def update = new Project(params)
			def errors = update.errors
			if (!errors) {
				def diff = project.toMap() - update.toMap()
				if (diff) {
					Project.mongoCollection.update(project.mongoObject, update)
					activityService.logProjectUpdated(session.user, update, diff)
				}
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
			}
			redirect action: 'members', id: project.projectId
		}
	}

	def approve = {
		withProject(params) { project ->
			def user = getUser(params)
			if (request.xhr) {
				if (user) {
					projectService.approve(project, user)
					render "Approved ${user}"
				} else {
					response.sendError(404, 'No user with that email')
				}
			} else {
				if (user) {
					projectService.approve(project, user)
					flash.message = "Approved ${user}"
				} else {
					flash.error = 'No user with that email'
				}
				redirect action: 'members', id: project.projectId
			}
		}
	}

	def ignore = {
		withProject(params) { project ->
			def email = params.email
			if (request.xhr) {
				if (email) {
					projectService.block(project, email)
					render "Ignored ${email}"
				} else {
					response.sendError(404, 'No user with that email')
				}
			} else {
				if (email) {
					projectService.block(project, email)
					flash.message = "Ignored ${email}"
				} else {
					flash.error = 'No user with that email'
				}
				redirect action: 'members', id: project.projectId
			}
		}
	}

	def kick = {
		withProject(params) { project ->
			def user = getUser(params)
			if (request.xhr) {
				if (user) {
					projectService.kick(project, user)
					render "Kicked ${user}"
				} else {
					response.sendError(404, 'No user with that email')
				}
			} else {
				if (user) {
					projectService.kick(project, user)
					flash.message = "Kicked ${user}"
				} else {
					flash.error = 'No user with that email'
				}
				redirect action: 'members', id: project.projectId
			}
		}
	}

	private getUser(params) {
		params.email ? User.findInstance(email: params.email) : null
	}
}
