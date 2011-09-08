package coreref.admin

import coreref.common.Project
import coreref.security.Secured

@Secured('ADMIN')
class ProjectController {
	static defaultAction = 'list'
	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def mongoService

	private getProjects() { Project.mongoCollection }
	private def withProject = { Map map, closure ->
		def id = map.id
		if (!id) {
			flash.message = "No project id specified"
			redirect action: 'list'
			return
		}

		def project = projects.get(id)
		if (!project) {
			flash.message = "No project with id '${id}'"
			redirect action: 'list'
			return
		}

		return closure.call(project)
	}

	def list = {
		[list: projects.list().collect { new Project(it) }]
	}

	def show = {
		withProject(params) { project ->
			[project: new Project(project)]
		}
	}

	def create = {
		[project: new Project(), errors: [:]]
	}

	def save = {
		def project = new Project(params)
		def errors = project.errors
		if (!errors) {
			projects.add(project)
			flash.message = "Project ${project.projectId} created"
			redirect action: 'list'
		} else {
			flash.message = 'Project has errors'
			render view: 'create', model: [project: project, errors: errors]
		}
	}

	def edit = {
		withProject(params) { project ->
			[project: new Project(project), errors: [:]]
		}
	}

	def update = {
		withProject(params) { project ->
			def update = new Project(params)
			def errors = update.errors
			if (!errors) {
				projects.update(project, update)
				flash.message = "Project '${update.projectId}' updated"
				redirect action: 'show', id: project._id
			} else {
				flash.message = 'Project has errors'
				render view: 'edit', model: [project: update, errors: errors]
			}
		}
	}

	def delete = {
		withProject(params) { project ->
			projects.remove(project)
			flash.message = "Deleted project '${project.projectId}'"
			redirect action: 'list'
		}
	}
}
