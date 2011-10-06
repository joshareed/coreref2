package coreref.common

class User {
	String id
	String firstName
	String lastName
	String email
	String password
	List roles
	boolean enabled
	Date lastLogin

	User(Map map = [:]) {
		id = map._id ?: map.id
		firstName = map.firstName
		lastName = map.lastName
		email = map.email
		password = map.password
		roles = DomainUtils.coerceList(map.roles)
		enabled = DomainUtils.coerceBoolean(map.enabled)
		lastLogin = map.lastLogin
	}

	Map toMap(Map map = [:]) {
		map.firstName = firstName
		map.lastName = lastName
		map.email = email
		map.password = password
		map.roles = roles
		map.enabled = enabled
		map.lastLogin = lastLogin
		map
	}

	// role helpers
	void addRole(role) {
		if (!roles.find { it == role }) {
			roles.add(role)
		}
	}
	void removeRole(role) { roles.remove(role) }
	boolean hasRole(role) { (roles.find { it == role } != null) }
	boolean isAdmin() { hasRole('ADMIN') }

	// project-related roles
	boolean canView(project) {
		project.isPublic() || isOwner(project) || isEditor(project) || isMember(project)
	}
	boolean isMember(project) { hasRole("MEMBER_${project.id}".toString()) }
	boolean isEditor(project) { hasRole("EDITOR_${project.id}".toString()) }
	boolean isOwner(project) { id == project.ownerId }

	def getErrors() {
		def errors = [:]
		['firstName', 'lastName', 'email', 'password'].each { field ->
			if (!DomainUtils.isSet(this, field)) {
				errors[field] = 'Required field'
			}
		}
		if (!errors.email && !DomainUtils.isUnique(this, 'email', mongoCollection)) {
			errors.email = "'${email}' already in use"
		}
		errors
	}

	def getProjects() {
		Project.findAllInstances(ownerId: id).sort { it.projectId }
	}

	def getMemberProjects() {
		roles.findAll {
			it.startsWith('MEMBER_')
		}.collect {
			Project.findInstance(projectId: (it - 'MEMBER_'))
		}.sort { it.projectId }
	}

	String toString() {
		"${firstName} ${lastName}"
	}

	static mongo = [
		collection: '_users',
		index: ['email', 'password', 'roles']
	]
}