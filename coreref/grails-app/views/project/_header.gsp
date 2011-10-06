<g:set var="user" value="${session.user}" />
<div class="project">
	<div class="header">
		<g:if test="${user}">
			<g:if test="${user.isOwner(project)}">
				<g:link class="btn slim primary right" controller="project" action="admin" id="${project.projectId}">Admin</g:link>
			</g:if>
			<g:else>
				<g:if test="${user.isMember(project)}">
					<g:link class="btn slim right" controller="project" action="leave" id="${project.projectId}">Leave</g:link>
				</g:if>
				<g:else>
					<g:link class="btn slim primary right" controller="project" action="join" id="${project.projectId}">Join</g:link>
				</g:else>
			</g:else>
		</g:if>
		<h1>${project.projectId}: ${project.name}</h1>
	</div>
	<g:if test="${project.desc}">
		<div class="description">
			${project.desc}
		</div>
	</g:if>
	<g:else>
		<div class="description empty">
			No description provided
		</div>
	</g:else>
</div>