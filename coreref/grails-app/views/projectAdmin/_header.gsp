<g:set var="user" value="${session.user}" />
<div class="admin-header">
	<g:link class="btn slim primary right" controller="project" action="overview" id="${project.projectId}">Back</g:link>
	<h1>${project.projectId}: ${project.name}</h1>
</div>