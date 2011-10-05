<html>
	<head>
		<title>Project :: ${project.projectId}</title>
		<meta name="layout" content="main" />
		<style type="text/css" media="screen">
			.private {
				margin-top: 50px;
				font-style: italic;
				text-align: center;
			}
		</style>
	</head>
	<body>
		<g:render template="header" model="[project: project]"/>
		<div class="alert-message warning private">
			<p>This project is limited to members only.	 Click the join button above to request access.</p>
		</div>
	</body>
</html>
