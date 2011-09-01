package coreref.admin

import coreref.common.User

class UserController {
	static defaultAction = 'list'
	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def mongoService

	private getUsers() { mongoService.getCollection(User.mongo.collection) }
	private def withUser = { Map map, closure ->
		def id = map._id
		if (!id) {
			flash.message = "No user id specified"
			redirect action: 'list'
			return
		}

		def user = users.get(id)
		if (!user) {
			flash.message = "No user with id '${id}'"
			redirect action: 'list'
			return
		}

		return closure.call(user)
	}

	def list = {
		[list: users.list()]
	}

	def show = {
		withUser(params) { user ->
			[user: new User(user)]
		}
	}

	def create = {
		[user: new User()]
	}

	def save = {
		def user = new User(params)
		if (!user.errors) {
			users.add(user)
			redirect action: 'list'
		} else {
			flash.message = 'User has errors'
			render view: 'create', model: [user: new User(user)]
		}
	}

	def edit = {
		withUser(params) { user ->
			[user: new User(user)]
		}
	}

	def update = {
		withUser(params) { user ->
			def update = new User(params)
			if (!update.errors) {
				users.update(user, update.save())
				flash.message = "${update} updated"
				redirect action: 'show', id: update._id
			} else {
				flash.message = 'User has errors'
				render view: 'edit', model: [user: update]
			}
		}
	}

	def delete = {
		withUser(params) { user ->
			users.remove(user)
			flash.message = "Deleted user '${user}'"
			redirect action: 'list'
		}
	}

	def disable = {
		withUser(params) { user ->
			def update = new User(user)
			update.enabled = false
			users.update(user, update.save())
			flash.message = "Disabled user '${user}'"
			redirect action: 'show', id: user._id
		}
	}

	def enable = {
		withUser(params) { user ->
			def update = new User(user)
			update.enabled = true
			users.update(user, update.save())
			flash.message = "Enabled user '${user}'"
			redirect action: 'show', id: user._id
		}
	}
}
