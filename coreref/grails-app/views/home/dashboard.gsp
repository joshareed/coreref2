<html>
	<head>
		<title>Dashboard</title>
		<meta name="layout" content="main" />
	</head>
	<body>
		<div class="row">
			<section class="span11 feed">
				<g:each in="${feed}" var="a">
					<a:full src="${a}"/>
				</g:each>
			</section>
			<section class="projects">
				<g:set var="projects" value="${user.projects}" />
				<div class="header">
					<g:link controller="project" action="create" class="btn primary small new right">New Project</g:link>
					<h1>Projects (${projects.size()})</h1>
				</div>
				<ul class="unstyled">
					<g:each in="${projects}" var="p">
					<li class="project owned">
						<g:link controller="project" id="${p.projectId}">
							${p.projectId}
						</g:link>
					</li>
					</g:each>
					<g:each in="${memberProjects}" var="p">
					<li class="project member">
						<g:link controller="project" id="${p.projectId}">
							${p.projectId}
						</g:link>
					</li>
					</g:each>
				</ul>
			</section>
		</div>
	</body>
</html>
