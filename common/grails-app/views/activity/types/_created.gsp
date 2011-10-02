<g:set var="p" value="${activity.project}" />
<div style="padding-bottom: 3px">${p.name}:</div>
<g:if test="${p?.desc}">
<blockquote style="font-style: italic">${p?.desc}</blockquote>
</g:if>