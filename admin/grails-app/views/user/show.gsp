<%@ page import="coreref.common.User" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>User :: ${user}</title>
    </head>
    <body>
		<div class="page-header">
			<h1>${user}</h1>
		</div>
		<form>
			<div class="clearfix">
				<label for="firstName">First Name</label>
				<div class="input">
					<span class="value">${user.firstName}</span>
				</div>
			</div>
			<div class="clearfix">
				<label for="lastName">Last Name</label>
				<div class="input">
					<span class="value">${user.lastName}</span>
				</div>
			</div>
			<div class="clearfix">
				<label for="email">Email</label>
				<div class="input">
					<span class="value">${user.email}</span>
				</div>
			</div>
			<div class="clearfix">
				<label for="password">Password</label>
				<div class="input">
					<span class="value">********</span>
				</div>
			</div>
			<div class="clearfix">
				<label id="status">Status</label>
				<div class="input">
					<span class="value">${user.enabled ? 'Enabled' : 'Disabled'}</span>
				</div>
			</div>
			<div class="clearfix">
				<label for="email">Roles</label>
				<div class="input">
					<span class="value">${user.roles.join(', ')}</span>
				</div>
			</div>
			<div class="actions">
				<g:link class="btn primary" action="edit" id="${user.id}">Edit</g:link>
				<g:link action="list" class="btn">Users</g:link>
			</div>
		</form>
    </body>
</html>
