<%@ page import="coreref.common.Application" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="main" />
		<g:set var="entityName" value="${message(code: 'application.label', default: 'Application')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<div class="nav">
			<span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
			<span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
		</div>
		<div class="body">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message">${flash.message}</div>
			</g:if>
			<div class="list">
				<table>
					<thead>
						<tr>
							<th>${message(code: 'application.appId.label', default: 'App Id')}</th>
							<th>${message(code: 'application.enabled.label', default: 'Enabled')}</th>
							<th>${message(code: 'application.limited.label', default: 'Limited')}</th>
							<th>${message(code: 'application.contact.label', default: 'Contact')}</th>
							<th>${message(code: 'application.site.label', default: 'Site')}</th>
						</tr>
					</thead>
					<tbody>
					<g:each in="${list}" status="i" var="app">
						<tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
							<td>
								<g:link action="edit" id="${app.appId}">${app.appId}</g:link>
							</td>
							<td><g:formatBoolean boolean="${app.enabled}" /></td>
							<td><g:formatBoolean boolean="${app.limited}" /></td>
							<td>${app.contact}</td>
							<td>${app.site}</td>
						</tr>
					</g:each>
					</tbody>
				</table>
			</div>
		</div>
	</body>
</html>
