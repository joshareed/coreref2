package coreref.common

class Activity {
	String userId
	String action
	String projectId
	Date timestamp
	String data

	Activity(Map map = [:]) {
		userId = map.userId
		action = map.action
		projectId = map.projectId
		timestamp = (map.timestamp ?: new Date())
		data = map.data
	}

	Map save(Map map = [:]) {
		map.userId = userId
		map.action = action
		map.projectId = projectId
		map.timestamp = timestamp
		map.data = data
		map
	}

	User getUser() {
		userId ? User.getInstance(userId) : null
	}

	Project getProject() {
		projectId ? Project.getInstance(projectId) : null
	}

	def getErrors() { [:] }

	String toString() {
		"${user.firstName} $action ${project.projectId}"
	}

	static mongo = [
		collection: '_activity',
		index: ['user', 'project', 'timestamp']
	]
}

class ActivityType {
	static final String PROJECT_CREATED = 'created'
	static final String PROJECT_UPDATED = 'updated'
}
