package coreref.common

class ProjectInvite {
	String id
	String projectId
	String email
	boolean invited = false
	boolean blocked = false

	ProjectInvite(Map map) {
		id = map._id ?: map.id
		projectId = map.projectId
		email = map.email
		invited = DomainUtils.coerceBoolean(map.invited)
		blocked = DomainUtils.coerceBoolean(map.blocked)
	}

	ProjectInvite(email, projectId, invited = false, blocked = false) {
		this.email = email
		this.projectId = projectId
		this.invited = invited
		this.blocked = blocked
	}

	Map toMap(Map map = [:]) {
		map.projectId = projectId
		map.email = email
		map.invited = invited
		map.blocked = blocked
		map
	}

	static mongo = [
		collection: '_invites',
		index: ['projectId', 'email', 'invited', 'blocked']
	]
}