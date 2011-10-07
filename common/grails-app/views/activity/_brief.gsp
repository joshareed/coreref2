<g:set var="u" value="${activity.user}" />
<g:set var="p" value="${activity.project}" />
<div class="activity brief">
	<div class="headline">
		<g:link controller="user" id="${u.id}">${u}</g:link>
		<span class="action">${action}</span>
		<g:if test="${p}">
			<g:link controller="project" id="${p.projectId}">${p.projectId}</g:link>
		</g:if>
		<span class="time"><prettytime:display date="${activity.ts}"/></span>
	</div>
</div>