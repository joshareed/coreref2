package coreref.common

class ActivityService {
    static transactional = false

	private id(it) {
		if (!it) {
			return null
		} else if (it.hasProperty('id')) {
			return it.id
		} else {
			it.toString()
		}
	}

    def log(user, action, project, text, timestamp = new Date()) {
		Activity.mongoCollection.add(userId: id(user), action: action, projectId: id(project), text: text, timestamp: timestamp)
	}

	def userFeed(user, int limit = 25) {
		assert null != user
		Activity.findAll(userId: id(user)).sort(timestamp: -1).limit(limit).collect { it }
	}

	def projectFeed(project, int limit = 25) {
		assert null != project
		Activity.findAll(projectId: id(project)).sort(timestamp: -1).limit(limit).collect { it }
	}

	def homeFeed(user, int limit = 25) {
		assert null != user
		def projects = user.projects.collect { id(it) }
		projects.addAll(user.memberProjects.collect { id(it) })
		Activity.findAll(projectId: ['$in': projects.unique()]).sort(timestamp: -1).limit(limit).collect { it }
	}
}
