<%@ page import="coreref.common.Application" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>Application :: ${app.appId}</title>
    </head>
    <body>
		<div class="page-header">
			<h1>${app.appId}</h1>
		</div>
		<div class="clearfix">
			<label for="appId">App Id</label>
			<div class="value">${app.appId}</div>
		</div>
		<div class="clearfix">
			<label for="contact">Contact</label>
			<div class="value">${app.contact}</div>
		</div>
		<div class="clearfix">
			<label for="site">Site</label>
			<div class="value">${app.site}</div>
		</div>
		<div class="clearfix">
			<label id="status">Status</label>
			<div class="value">${app.enabled ? 'Enabled' : 'Disabled'}</div>
		</div>
		<div class="actions">
			<g:link class="btn primary" action="edit" id="${app.id}">Edit</g:link>
			<g:link action="list" class="btn">Applications</g:link>
		</div>
    </body>
</html>
