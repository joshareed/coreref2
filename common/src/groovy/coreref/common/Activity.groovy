package coreref.common

class Activity {
	String userId
	String action
	String projectId
	Date timestamp
	String text

	Activity(Map map = [:]) {
		userId = map.userId
		action = map.action
		projectId = map.projectId
		timestamp = (map.timestamp ?: new Date())
		text = map.text
	}

	Map save(Map map = [:]) {
		map.userId = userId
		map.action = action
		map.projectId = projectId
		map.timestamp = timestamp
		map.text = text
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
