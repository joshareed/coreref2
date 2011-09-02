<%@ page import="coreref.common.Application" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>Application :: New</title>
    </head>
    <body>
		<h1>Edit Application</h1>
		<g:form action="update">
			<g:hiddenField name="id" value="${app.id}"/>
			<fieldset>
				<div class="clearfix <g:if test="${errors?.appId}">error</g:if>">
					<label for="appId">App Id</label>
					<div class="input">
						<g:textField name="appId" value="${app?.appId ?: uuid}" />
						<span class="help-inline">${errors.appId}</span>
					</div>
				</div>
				<div class="clearfix <g:if test="${errors?.contact}">error</g:if>">
					<label for="contact">Contact</label>
					<div class="input">
						<g:textField name="contact" value="${app?.contact}" />
						<span class="help-inline">${errors.contact}</span>
					</div>
				</div>
				<div class="clearfix <g:if test="${errors?.site}">error</g:if>">
					<label for="site">Site</label>
					<div class="input">
						<g:textField name="site" value="${app?.site}" />
						<span class="help-inline">${errors.site}</span>
					</div>
				</div>
				<div class="clearfix">
					<label id="status">Status</label>
					<div class="input">
						<ul class="inputs-list">
							<li>
								<label>
									<input type="checkbox" name="enabled" <g:if test="${app.enabled}">checked="checked"</g:if> />
									<span>Enabled</span>
								</label>
							</li>
						</ul>
					</div>
				</div>
				<div class="actions">
					<g:submitButton name="create" class="btn primary" value="Update" />
					<g:link action="show" id="${app.id}" class="btn">Cancel</g:link>
				</div>
			</fieldset>
		</g:form>
    </body>
</html>
