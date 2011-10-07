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

	def userFeed(user, int limit = 25) {
		assert null != user
		Activity.findAll(userId: id(user)).sort(ts: -1).limit(limit).collect { new Activity(it) }
	}

	def projectFeed(project, int limit = 25) {
		assert null != project
		Activity.findAll(projectId: id(project)).sort(ts: -1).limit(limit).collect { new Activity(it) }
	}

	def homeFeed(user, int limit = 25) {
		assert null != user
		def projects = user.projects.collect { id(it) }
		projects.addAll(user.memberProjects.collect { id(it) })
		Activity.findAll(projectId: ['$in': projects.unique()]).sort(ts: -1).limit(limit).collect { new Activity(it) }
	}

	def adminFeed(int limit = 50) {
		Activity.mongoCollection.find().sort(ts: -1).limit(limit).collect { new Activity(it) }
	}

	def log(user, action, project, String data = null, Date timestamp = new Date()) {
		Activity.mongoCollection.add(ts: timestamp, userId: id(user), action: action, projectId: id(project), data: data)
	}

	def logProjectCreated(user, project) {
		log(user, ActivityType.PROJECT_CREATED, project)
	}

	def logProjectUpdated(user, project, diff) {
		def text
		if (diff) {
			text = diff.collect { it.key }.join('|')
		}
		log(user, ActivityType.PROJECT_UPDATED, project, text)
	}
}
