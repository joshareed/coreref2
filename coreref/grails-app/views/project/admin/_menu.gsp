<ul class="admin-menu">
	<li class="${params.subaction == 'info' ? 'active' : ''}">
		<g:link controller="project" action="admin" id="${project.projectId}" params="[subaction: 'info']">Info</g:link>
	</li>
	<li class="${params.subaction == 'members' ? 'active' : ''}">
		<g:link controller="project" action="admin" id="${project.projectId}" params="[subaction: 'members']">Members</g:link>
	</li>
</ul>