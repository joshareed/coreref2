package coreref.common

class User {
	// roles
	public static final String ROLE_ADMIN = "_admin"
	public static final String ROLE_USER = "_user"
	public static final String ROLE_MEMBER = "_member_"
	public static final String ROLE_EDITOR = "_editor_"

	String firstName
	String lastName
	String email
	String username
	String password
	List roles
	boolean enabled

	User(Map map = [:]) {
		firstName = map.firstName
		lastName = map.lastName
		email = map.email
		username = map.username
		password = map.password
		roles = map.roles ?: [ ROLE_USER ]
		enabled = map.enabled
	}

	Map save(Map map = [:]) {
		map.firstName = firstName
		map.lastName = lastName
		map.email = email
		map.username = username
		map.password = password
		map.roles = roles
		map.enabled = enabled
	}

	// role helpers
	void addRole(role) {
		if (!roles.find { it == role} ) {
			roles.add(role)
		}
	}
	void removeRole(role) { roles.remove(role) }
	boolean hasRole(role) { (roles.find { it == role } != null) }
	boolean isAdmin() { hasRole(ROLE_ADMIN) }
	boolean isMember(project) { hasRole("${ROLE_MEMBER}${project}") }
	boolean isEditor(project) { hasRole("${ROLE_EDITOR}${project}") }

	static mongo = [
		collection: '_users',
		index: ['username']
	]
}