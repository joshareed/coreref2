<html>
	<head>
		<title>Dashboard</title>
		<meta name="layout" content="main" />
	</head>
	<body>
		<h1>Dashboard</h1>
		<div>
			<span class="label warning">TODO</span> Activity Feed
		</div>
		<g:set var="projects" value="${user.projects}" />
		<g:if test="${projects}">
			<h2>Projects</h2>
			<ul>
			<g:each in="${projects}" var="p">
				<li><g:link controller="project" action="overview" id="${p.projectId}">${p}</g:link></li>
			</g:each>
			</ul>
		</g:if>
	</body>
</html>
