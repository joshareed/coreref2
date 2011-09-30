<g:set var="p" value="${activity.project}" />
${p.name}
<g:if test="${p?.metadata?.description}">
<p>${p?.metadata?.description}</p>
</g:if>