<%@ page import="coreref.common.Application" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="main" />
		<title>Applications</title>
	</head>
	<body>
		<div class="header-actions">
			<g:link class="btn primary" action="create">New</g:link>
		</div>
		<h1>Applications</h1>
		<table>
			<thead>
				<tr>
					<th>Id</th>
					<th>Contact</th>
					<th>Site</th>
					<th>Status</th>
					<th class="actions-col"></th>
				</tr>
			</thead>
			<tbody>
				<g:if test="${!list}">
					<tr>
						<td colspan="5" class="empty-list">
							<em>No applications</em>
						</td>
					</tr>
				</g:if>
				<g:each in="${list}" status="i" var="app">
				<tr>
					<td>
						<g:link action="show" id="${app.id}">${app.appId}</g:link>
					</td>
					<td>${app.contact}</td>
					<td>
						<g:if test="${app.site}">
							<a href="${app.site}">${app.site}</a>
						</g:if>
					</td>
					<td>${app.enabled ? 'Enabled' : 'Disabled'}</td>
					<td class="actions-col">
						<div class="buttons right">
							<g:link class="btn" action="edit" id="${app.id}">Edit</g:link>
							<g:form style="margin: 0; padding: 0; display: inline">
			                    <g:hiddenField name="id" value="${app.id}" />
			                    <g:actionSubmit class="btn danger" action="delete" value="Delete"
									onclick="return confirm('Are you sure?');" />
			                </g:form>
						</div>
					</td>
				</tr>
				</g:each>
			</tbody>
		</table>
	</body>
</html>
