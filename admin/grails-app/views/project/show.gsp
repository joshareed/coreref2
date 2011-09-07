<%@ page import="coreref.common.Project" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>Project :: ${project.projectId}</title>
    </head>
    <body>
		<div class="page-header">
			<h1>Project: ${project.projectId}</h1>
		</div>
		<g:form>
			<fieldset>
				<div class="clearfix">
					<label for="name">Name</label>
					<div class="input">
						<span class="uneditable-input" style="width: 270px">${project.name}</span>
					</div>
				</div>
				<div class="clearfix">
					<label for="projectId">Project Id</label>
					<div class="input">
						<span class="uneditable-input">${project.projectId}</span>
					</div>
				</div>
				<div class="clearfix">
					<label for="owner">Owner</label>
					<div class="input">
						<span class="uneditable-input">${project.owner}</span>
					</div>
				</div>
				<div class="actions">
					<g:link class="btn primary" action="edit" id="${project.id}">Edit</g:link>
					<g:link action="list" class="btn">Projects</g:link>
				</div>
			</fieldset>
		</g:form>
    </body>
</html>
