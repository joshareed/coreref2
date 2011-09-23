<%@ page import="coreref.common.User" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="main" />
		<title>Users</title>
	</head>
	<body>
		<div class="header-actions">
			<g:link class="btn primary" action="create">New</g:link>
		</div>
		<h1>Users</h1>
		<table>
			<thead>
				<tr>
					<th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th>Status</th>
					<th class="actions-col"></th>
				</tr>
			</thead>
			<tbody>
				<g:if test="${!list}">
					<tr>
						<td colspan="5" class="empty-list">
							<em>No users</em>
						</td>
					</tr>
				</g:if>
				<g:each in="${list}" status="i" var="user">
				<tr>
					<td>
						<g:link action="show" id="${user.id}">${user.firstName}</g:link>
					</td>
					<td>
						<g:link action="show" id="${user.id}">${user.lastName}</g:link>
					</td>
					<td>${user.email}</td>
					<td>${user.enabled ? 'Enabled' : 'Disabled'}</td>
					<td class="actions-col">
						<div class="buttons right">
							<g:link class="btn" action="edit" id="${user.id}">Edit</g:link>
							<g:form style="margin: 0; padding: 0; display: inline">
			                    <g:hiddenField name="id" value="${user.id}" />
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
