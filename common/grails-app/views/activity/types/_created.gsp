<g:set var="p" value="${activity.project}" />
${p.name}
<g:if test="${p?.description}">
<p>${p?.description}</p>
</g:if>