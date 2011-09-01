package coreref.admin

import coreref.common.*

class ApplicationController {
	static defaultAction = 'list'

	def mongoService

	private getApps() { mongoService.'_apps' }

	private def withApp = { Map map, closure ->
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

	private Map t(Map map) {
		map.enabled = (map.enabled && map.enabled == 'on')
		map.limited = (map.limited && map.limited == 'on')
		map
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
		[app: new Application(), uuid: UUID.randomUUID().toString()[0..7]]
	}

	def save = {
		def app = new Application(t(params))
		if (app.isValid()) {
			apps.add(app)
			redirect action: 'list'
		} else {
			flash.message = 'Application must specify an appId'
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
			def update = new Application(t(params))
			if (update.isValid()) {
				apps.update(app, update.save())
				flash.message = "Application '${update.appId}' updated"
				redirect action: 'show', id: update.appId
			} else {
				flash.message = 'Application must specify an appId'
				render view: 'edit', model: [app: new Application(app)]
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

	def cleanup = {
		apps.findAll(appId:null).each {
			apps.remove(it)
		}
		flash.message = 'Removed invalid applications'
		redirect action: 'list'
	}
}
