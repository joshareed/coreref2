<%@ page import="coreref.common.User" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>User :: ${user}</title>
		<script type="text/javascript" charset="utf-8">
			$(function() {
				$('#password').focusin(function() {
					$('#updatePassword').attr('checked', 'checked');
				}).focusout(function() {
					if ($(this).val() == '') {
						$('#updatePassword').removeAttr('checked');
					}
				});
			});
		</script>
    </head>
    <body>
		<div class="page-header">
			<h1>Edit User</h1>
		</div>
		<g:form action="update">
			<g:hiddenField name="id" value="${user.id}"/>
			<fieldset>
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
						<div class="input-prepend">
							<label class="add-on"><input type="checkbox" name="updatePassword" id="updatePassword" /></label>
							<g:passwordField name="password" value="" style="width: 185px" />
							<span class="help-inline">${errors.password}</span>
						</div>
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
						<g:textField name="roles" value="${user.roles.join(', ')}" />
						<span class="help-inline">${errors.roles}</span>
					</div>
				</div>
				<div class="actions">
					<g:submitButton name="create" class="btn primary" value="Update" />
					<g:link action="list" class="btn">Cancel</g:link>
				</div>
			</fieldset>
		</g:form>
    </body>
</html>

