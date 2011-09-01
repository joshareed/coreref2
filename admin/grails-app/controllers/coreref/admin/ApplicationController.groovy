package coreref.admin

import coreref.common.Application

class ApplicationController {
	static defaultAction = 'list'
	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def mongoService

	private getApps() { mongoService.getCollection(Application.mongo.collection) }
	private def withApp = { Map map, closure ->
		if (map['_id']) {
			def app = apps.get(map['_id'])
			if (app) {
				return closure.call(app)
			}
		}
		def id = map.id
		if (!id) {
			flash.message = "No application id specified"
			redirect action: 'list'
			return
		}

		def app = apps.find(appId: id)
		if (!app) {
			flash.message = "No application with id '${id}'"
			redirect action: 'list'
			return
		}

		return closure.call(app)
	}

	def list = {
		[list: apps.list()]
	}

	def show = {
		withApp(params) { app ->
			[app: new Application(app)]
		}
	}

	def create = {
		[app: new Application(), uuid: Application.randomId()]
	}

	def save = {
		def app = new Application(params)
		if (!app.errors) {
			apps.add(app)
			redirect action: 'list'
		} else {
			flash.message = 'Application has errors'
			render view: 'create', model: [app: app]
		}
	}

	def edit = {
		withApp(params) { app ->
			[app: new Application(app)]
		}
	}

	def update = {
		withApp(params) { app ->
			def update = new Application(params)
			if (!update.errors) {
				apps.update(app, update.save())
				flash.message = "Application '${update.appId}' updated"
				redirect action: 'show', id: update.appId
			} else {
				flash.message = 'Application has errors'
				render view: 'edit', model: [app: update]
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
