<%@ page import="coreref.common.Project" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>Project :: ${project.projectId}</title>
		<style type="text/css" media="screen">
			.metadata {
				margin-top: 28px;
			}
		</style>
    </head>
    <body>
		<div class="page-header">
			<h1>${project.projectId}</h1>
		</div>
		<div class="clearfix">
			<label>Name</label>
			<div class="value">${project.name}</div>
		</div>
		<div class="clearfix">
			<label>Project Id</label>
			<div class="value">${project.projectId}</div>
		</div>
		<div class="clearfix">
			<label>Owner</label>
			<div class="value">${project.owner}</div>
		</div>
		<div class="actions">
			<g:link class="btn primary" action="edit" id="${project.id}">Edit</g:link>
			<g:link action="list" class="btn">Projects</g:link>
		</div>
    </body>
</html>
