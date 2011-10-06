package coreref.common

class ProjectInvite {
	String projectId
	String email
	boolean invited = false

	ProjectInvite(Map map) {
		projectId = map.projectId
		email = map.email
		invited = DomainUtils.coerceBoolean(map.invited)
	}

	ProjectInvite(email, projectId, invited = false) {
		this.email = email
		this.projectId = projectId
		this.invited = invited
	}

	Map toMap(Map map = [:]) {
		map.projectId = projectId
		map.email = email
		map.invited = invited
		map
	}

	static mongo = [
		collection: '_invites',
		index: ['projectId', 'email']
	]
}