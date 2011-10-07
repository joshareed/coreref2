<ul class="admin-menu">
	<li class="${params.action == 'info' ? 'active' : ''}">
		<g:link controller="projectAdmin" action="info" id="${project.projectId}">Info</g:link>
	</li>
	<li class="${params.action == 'members' ? 'active' : ''}">
		<g:link controller="projectAdmin" action="members" id="${project.projectId}">Members</g:link>
	</li>
</ul>