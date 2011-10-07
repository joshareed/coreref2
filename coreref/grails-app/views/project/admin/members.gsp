<%@ page import="coreref.common.Project" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="main" />
		<title>Admin :: ${project.projectId}</title>
	</head>
	<body>
		<g:render template="admin/header" model="[project: project]"/>
		<div class="row">
			<div class="span4">
				<g:render template="admin/menu" model="[project: project]"/>
			</div>
			<div class="span12">
				Invite/Approve/Kick/Ignore Members
			</div>
		</div>
	</body>
</html>
