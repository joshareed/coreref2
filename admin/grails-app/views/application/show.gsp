<%@ page import="coreref.common.Application" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>Application :: ${app.appId}</title>
    </head>
    <body>
		<h1>Application: ${app.appId}</h1>
		<g:form>
			<fieldset>
				<div class="clearfix">
					<label for="appId">App Id</label>
					<div class="input">
						<span class="uneditable-input">${app.appId}</span>
					</div>
				</div>
				<div class="clearfix">
					<label for="contact">Contact</label>
					<div class="input">
						<span class="uneditable-input">${app.contact}</span>
					</div>
				</div>
				<div class="clearfix">
					<label for="site">Site</label>
					<div class="input">
						<span class="uneditable-input">${app.site}</span>
					</div>
				</div>
				<div class="clearfix">
					<label id="status">Status</label>
					<div class="input">
						<ul class="inputs-list">
							<li>
								<label class="disabled">
									<input type="checkbox" name="enabled" <g:if test="${app.enabled}">checked="checked"</g:if> disabled />
									<span>Enabled</span>
								</label>
							</li>
						</ul>
					</div>
				</div>
				<div class="actions">
					<g:link class="btn primary" action="edit" id="${app.id}">Edit</g:link>
					<g:link action="list" class="btn">Applications</g:link>
				</div>
			</fieldset>
		</g:form>
    </body>
</html>
