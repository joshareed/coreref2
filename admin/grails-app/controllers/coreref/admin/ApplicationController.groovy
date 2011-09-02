package coreref.admin

import coreref.common.Application

class ApplicationController {
	static defaultAction = 'list'
	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def mongoService

	private getApps() { mongoService.getCollection(Application.mongo.collection) }
	private def withApp = { Map map, closure ->
		def id = map.id
		if (!id) {
			flash.message = "No application id specified"
			redirect action: 'list'
			return
		}

		def app = apps.get(id)
		if (!app) {
			flash.message = "No application with id '${id}'"
			redirect action: 'list'
			return
		}

		return closure.call(app)
	}

	def list = {
		[list: apps.list().collect { new Application(it) }]
	}

	def show = {
		withApp(params) { app ->
			[app: new Application(app)]
		}
	}

	def create = {
		[app: new Application(), errors: [:], uuid: Application.randomId()]
	}

	def save = {
		def app = new Application(params)
		def errors = app.errors
		if (!errors) {
			apps.add(app)
			flash.message = "Application ${app.appId} created"
			redirect action: 'list'
		} else {
			flash.message = 'Application has errors'
			render view: 'create', model: [app: app, errors: errors]
		}
	}

	def edit = {
		withApp(params) { app ->
			[app: new Application(app), errors: [:]]
		}
	}

	def update = {
		withApp(params) { app ->
			def update = new Application(params)
			def errors = app.errors
			if (!errors) {
				apps.update(app, update.save())
				flash.message = "Application '${update.appId}' updated"
				redirect action: 'show', id: app._id
			} else {
				flash.message = 'Application has errors'
				render view: 'edit', model: [app: update, errors: errors]
			}
		}
	}

	def delete = {
		withApp(params) { app ->
			apps.remove(app)
			flash.message = "Deleted application '${app.appId}'"
			redirect action: 'list'
		}
	}
}
