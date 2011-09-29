<html>
	<head>
		<title>Dashboard</title>
		<meta name="layout" content="main" />
	</head>
	<body>
		<div class="row">
			<section class="span11 activity">
				<div class="header">
					<span style="float: right; padding-top: 15px; font-style: italic"><prettytime:display date="${session['user-last-login']}"/></span>
					<h1>Recent Activity</h1>
				</div>
				<span class="label warning">TODO</span> Activity Feed
			</section>
			<section class="span5 projects">
				<g:set var="projects" value="${user.projects}" />
				<div class="header">
					<g:link controller="project" action="create" class="btn small new right">New Project</g:link>
					<h1>Projects (${projects.size()})</h1>
				</div>
				<ul class="unstyled">
					<g:each in="${projects}" var="p" status="i">
					<li class="project owned">
						<g:link controller="project" action="overview" id="${p.projectId}">
							${p.projectId}

							<%-- TODO: flesh out with activity count --%>
							<%--
							<g:if test="${p.}">
								<span class="label important right">1</span>
							</g:if>
							--%>
						</g:link>
					</li>
					</g:each>
				</ul>
			</section>
		</div>
	</body>
</html>
