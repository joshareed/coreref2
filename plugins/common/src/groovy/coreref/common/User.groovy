package coreref.common

import org.codehaus.groovy.grails.commons.ApplicationHolder

class User {
	def mongoService = ApplicationHolder.application.mainContext.getBean('mongoService')

	// roles
	public static final String ROLE_ADMIN = "_admin"
	public static final String ROLE_USER = "_user"
	public static final String ROLE_MEMBER = "_member_"
	public static final String ROLE_EDITOR = "_editor_"

	String id
	String firstName
	String lastName
	String email
	String password
	List roles
	boolean enabled

	User(Map map = [:]) {
		id = map._id ?: map.id
		firstName = map.firstName
		lastName = map.lastName
		email = map.email
		password = map.password
		roles = map.roles ?: []
		enabled = DomainUtils.coerceBoolean(map.enabled)
	}

	Map save(Map map = [:]) {
		map.firstName = firstName
		map.lastName = lastName
		map.email = email
		map.password = password
		map.roles = roles
		map.enabled = enabled
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
	boolean isAdmin() { hasRole(ROLE_ADMIN) }
	boolean isMember(project) { hasRole("${ROLE_MEMBER}${project}") }
	boolean isEditor(project) { hasRole("${ROLE_EDITOR}${project}") }

	def getErrors() {
		def errors = [:]
		['firstName', 'lastName', 'email', 'password'].each { field ->
			if (!DomainUtils.isSet(this, field)) {
				errors[field] = 'Required field'
			}
		}
		if (!errors.email && !DomainUtils.isUnique(this, 'email', mongoService[mongo.collection])) {
			errors.email = "'${email}' already in use"
		}
		errors
	}

	String toString() {
		"${firstName} ${lastName} (${email})"
	}

	static mongo = [
		collection: '_users',
		index: ['email', 'password', 'roles']
	]
}