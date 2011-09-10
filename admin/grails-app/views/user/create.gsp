<%@ page import="coreref.common.User" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>User :: New</title>
    </head>
    <body>
		<div class="page-header">
			<h1>New User</h1>
		</div>
		<g:form action="save">
			<div class="clearfix <g:if test="${errors?.firstName}">error</g:if>">
				<label for="firstName">First Name</label>
				<div class="input">
					<g:textField name="firstName" value="${user.firstName}" />
					<span class="help-inline">${errors.firstName}</span>
				</div>
			</div>
			<div class="clearfix <g:if test="${errors?.lastName}">error</g:if>">
				<label for="lastName">Last Name</label>
				<div class="input">
					<g:textField name="lastName" value="${user.lastName}" />
					<span class="help-inline">${errors.lastName}</span>
				</div>
			</div>
			<div class="clearfix <g:if test="${errors?.email}">error</g:if>">
				<label for="email">Email</label>
				<div class="input">
					<g:textField name="email" value="${user.email}" />
					<span class="help-inline">${errors.email}</span>
				</div>
			</div>
			<div class="clearfix <g:if test="${errors?.password}">error</g:if>">
				<label for="password">Password</label>
				<div class="input">
					<g:passwordField name="password" value="" />
					<span class="help-inline">${errors.password}</span>
				</div>
			</div>
			<div class="clearfix">
				<label id="status">Status</label>
				<div class="input">
					<ul class="inputs-list">
						<li>
							<label>
								<input type="checkbox" name="enabled" checked="checked" />
								<span>Enabled</span>
							</label>
						</li>
					</ul>
				</div>
			</div>
			<div class="clearfix <g:if test="${errors?.roles}">error</g:if>">
				<label for="roles">Roles</label>
				<div class="input">
					<g:textField name="roles" value="${user?.roles?.join(', ') ?: 'USER'}" />
					<span class="help-inline">${errors.roles}</span>
				</div>
			</div>
			<div class="actions">
				<g:submitButton name="create" class="btn primary" value="Create" />
				<g:link action="list" class="btn">Cancel</g:link>
			</div>
		</g:form>
    </body>
</html>
