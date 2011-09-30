<g:set var="u" value="${activity.user}" />
<g:set var="p" value="${activity.project}" />
<div class="activity full">
	<div class="headline">
		<g:link controller="user" id="${u.id}">${u}</g:link>
		<span class="action">${action}</span>
		<g:link controller="project" id="${p.projectId}">${p.projectId}</g:link>
		<span class="time"> about <prettytime:display date="${activity.timestamp}"/></span>
	</div>
	<div class="body">
		${content}
	</div>
</div>