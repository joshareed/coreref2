package coreref.common

class Activity {
	String userId
	String action
	String projectId
	Date ts
	String data

	Activity(Map map = [:]) {
		userId = map.userId
		action = map.action
		projectId = map.projectId
		ts = (map.ts ?: new Date())
		data = map.data
	}

	Map toMap(Map map = [:]) {
		map.userId = userId
		map.action = action
		map.projectId = projectId
		map.ts = ts
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
		index: ['user', 'project', 'ts']
	]
}

class ActivityType {
	static final String PROJECT_CREATED = 'created'
	static final String PROJECT_UPDATED = 'updated'
}
