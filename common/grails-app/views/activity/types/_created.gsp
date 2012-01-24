<g:set var="p" value="${activity.project}" />
<div class="subject">${p?.name}:</div>
<g:if test="${p?.desc}">
<blockquote>${p?.desc}</blockquote>
</g:if>