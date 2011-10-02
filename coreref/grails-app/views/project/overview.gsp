<html>
	<head>
		<title>Project :: ${project.projectId}</title>
		<meta name="layout" content="main" />
	</head>
	<body>
		<h1>${project.projectId}: ${project.name}</h1>
		<g:if test="${project.desc}">
			<div class="description">
				${project.desc}
			</div>
		</g:if>
	</body>
</html>
