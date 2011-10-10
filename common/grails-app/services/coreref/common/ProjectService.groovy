package coreref.common

class ProjectService {
	static transactional = false

	def pending(project) {
		ProjectInvite.findAllInstances(projectId: project.id, blocked: false)
	}

	def join(project, user) {
		// check if already a member
		if (user.isMember(project)) {
			cleanupInvites(project, user)
			return 'Already a member'
		}

		// add if public
		if (project.isPublic()) {
			user.addRole("MEMBER_${project.id}")
			save(user)
			cleanupInvites(project, user)
			return 'Joined project'
		}

		// add if already invited
		def invite = ProjectInvite.find(email: user.email, projectId: project.id)
		if (invite) {
			if (invite.invited) {
				user.addRole("MEMBER_${project.id}")
				save(user)
				cleanupInvites(project, user)
				return 'Joined project'
			} else {
				return 'Already requested to join'
			}
		}

		// request access
		invite = new ProjectInvite(email: user.email, projectId: project.id)
		ProjectInvite.mongoCollection.add(invite)
		return 'Your join request was sent to the project admin'
	}

	def leave(project, user) {
		if (!user.isMember(project)) {
			return 'Not a member'
		}

		// remove the roles
		user.roles = user.roles.findAll { !it.contains(project.id) }
		save(user)

		// remove any invites
		cleanupInvites(project, user)
		return 'Left the project'
	}

	def invite(project, users) {
		users.each {
			def email = it.trim()
			def user = User.findInstance(email: email)
			if (user) {
				// user already exists, so join them
				user.addRole("MEMBER_${project.id}")
				save(user)
				cleanupInvites(project, user)
			} else {
				def invite = ProjectInvite.find(email: email, projectId: project.id)
				if (invite) {
					if (!invite.invited) {
						// should never happen since only way to create a request is if there is a user
						invite.invited = true
						ProjectInvite.mongoCollection.save(invite)
					}
				} else {
					// add a new invite
					ProjectInvite.mongoCollection.add(email: email, projectId: project.id, invited: true, blocked: false)
				}
			}
		}
	}

	def approve(project, user) {
		user.addRole("MEMBER_${project.id}")
		save(user)
		cleanupInvites(project, user)
	}

	def kick(project, user) {
		leave(project, user)
		block(project, user.email)
	}

	def block(project, email) {
		ProjectInvite.mongoCollection.remove(email: email, projectId: project.id)
		ProjectInvite.mongoCollection.add(email: email, projectId: project.id, invited: false, blocked: true)
	}

	def processInvites(user) {
		ProjectInvite.mongoCollection.findAll(email: user.email, invited: true).each { invite ->
			def project = Project.getInstance(invite.projectId)
			if (project) {
				join(project, user)
			}
		}
	}

	private void save(user) {
		User.mongoCollection.update(user.mongoObject, user)
	}

	private void cleanupInvites(project, user) {
		ProjectInvite.mongoCollection.remove(projectId: project.id, email: user.email)
	}
}
