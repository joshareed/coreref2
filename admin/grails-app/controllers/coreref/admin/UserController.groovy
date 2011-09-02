package coreref.admin

import coreref.common.User

class UserController {
	static defaultAction = 'list'
	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

	def mongoService
	def authenticationService

	private getUsers() { mongoService.getCollection(User.mongo.collection) }
	private def withUser = { Map map, closure ->
		def id = map.id
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
		[list: users.list().collect { new User(it) }]
	}

	def show = {
		withUser(params) { user ->
			[user: new User(user)]
		}
	}

	def create = {
		[user: new User(params)]
	}

	def save = {
		def user = new User(params)
		if (params.password) {
			user.password = authenticationService.encodePassword(params.password)
		}
		if (!user.errors) {
			users.add(user)
			redirect action: 'list'
		} else {
			flash.message = 'User has errors'
			render view: 'create', model: [user: user]
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
			if (params.password) {
				update.password = authenticationService.encodePassword(params.password)
			} else {
				update.password = user.password
			}
			if (!update.errors) {
				users.update(user, update.save())
				flash.message = "${update} updated"
				redirect action: 'show', id: user._id
			} else {
				flash.message = 'User has errors'
				render view: 'edit', model: [user: update]
			}
		}
	}

	def delete = {
		withUser(params) { user ->
			users.remove(user)
			flash.message = "Deleted user '${new User(user)}'"
			redirect action: 'list'
		}
	}
}
