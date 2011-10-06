package coreref.common

import grails.test.*

class ProjectInviteTests extends GrailsUnitTestCase {
	protected void setUp() {
		super.setUp()
	}

	protected void tearDown() {
		super.tearDown()
	}

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
		assert [email: 'test@example.com', projectId: 'test', invited: true] == invite.toMap()
	}
}
