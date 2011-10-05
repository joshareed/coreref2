<html>
	<head>
		<title>Dashboard</title>
		<meta name="layout" content="main" />
		<style type="text/css" media="screen">
			.welcome p {
				margin-top: 10px;
				font-size: 16px;
				line-height: 24px
			}
		</style>
	</head>
	<body>
		<div class="row">
			<section class="span11 feed">
				<g:each in="${feed}" var="a">
					<a:full src="${a}"/>
				</g:each>
				<g:if test="${!feed}">
					<div class="welcome">
						<h2>Welcome to CoreRef!</h2>
						<p>
							CoreRef is a collaborative platform for scientific drilling projects, large and small. It provides
							services and visualizations for working with core imagery and data.
						</p>
						<p>
							Get started by <g:link controller="project" action="search">finding an existing project</g:link> or <g:link controller="project" action="create">creating your own</g:link>!
						</p>
					</div>
				</g:if>
			</section>
			<section class="projects">
				<g:set var="projects" value="${user.projects}" />
				<g:set var="memberProjects" value="${user.memberProjects}" />
				<div class="header">
					<g:link controller="project" action="create" class="btn primary slim right">New Project</g:link>
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
				<g:if test="${!projects && !memberProjects}">
					<div class="empty">
						No projects?! <g:link controller="project" action="search">Find</g:link> one
						or <g:link controller="project" action="create">create</g:link> your own!
					</div>
				</g:if>
			</section>
		</div>
	</body>
</html>
