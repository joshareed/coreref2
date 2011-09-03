<%@ page import="coreref.common.User" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>User :: ${user}</title>
    </head>
    <body>
		<div class="page-header">
			<h1>Edit User</h1>
		</div>
		<g:form action="update">
			<g:hiddenField name="id" value="${user.id}"/>
			<fieldset>
				<div class="clearfix">
					<label for="firstName">First Name</label>
					<div class="input">
						<span class="uneditable-input">${user.firstName}</span>
					</div>
				</div>
				<div class="clearfix">
					<label for="lastName">Last Name</label>
					<div class="input">
						<span class="uneditable-input">${user.lastName}</span>
					</div>
				</div>
				<div class="clearfix">
					<label for="email">Email</label>
					<div class="input">
						<span class="uneditable-input">${user.email}</span>
					</div>
				</div>
				<div class="clearfix">
					<label for="password">Password</label>
					<div class="input">
						<g:passwordField name="password" value="**********" disabled="disabled" class="uneditable-input" />
					</div>
				</div>
				<div class="clearfix">
					<label id="status">Status</label>
					<div class="input">
						<ul class="inputs-list">
							<li>
								<label>
									<input type="checkbox" name="enabled" checked="checked" disabled/>
									<span>Enabled</span>
								</label>
							</li>
						</ul>
					</div>
				</div>
				<div class="actions">
					<g:link class="btn primary" action="edit" id="${user.id}">Edit</g:link>
					<g:link action="list" class="btn">Users</g:link>
				</div>
			</fieldset>
		</g:form>
    </body>
</html>
