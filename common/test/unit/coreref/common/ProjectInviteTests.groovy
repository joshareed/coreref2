package coreref.common

class ProjectInviteTests {

	void testCreateMap() {
		def invite = new ProjectInvite(email: 'test@example.com', projectId: 'test', invited: true)
		assert 'test@example.com' == invite.email
		assert 'test' == invite.projectId
		assert invite.invited
	}

	void testCreate() {
		def invite = new ProjectInvite('test@example.com', 'test')
		assert 'test@example.com' == invite.email
		assert 'test' == invite.projectId
		assert !invite.invited
	}

	void testToMap() {
		def invite = new ProjectInvite('test@example.com', 'test', true)
		assert [email: 'test@example.com', projectId: 'test', invited: true, blocked: false] == invite.toMap()
	}
}
