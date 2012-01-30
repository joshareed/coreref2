<%@ page import="coreref.common.Project" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="main" />
		<title>Project :: Search</title>
		<style type="text/css" media="screen">
			.results {
				margin-top: 40px;
			}
		</style>
	</head>
	<body>
		<g:form action="search" method="get">
			<div class="clearfix">
				<label for="q">Find a Project</label>
				<div class="input">
					<g:textField name="q" value="${q}" class="span8" />
					<input type="submit" value="Search" class="btn primary"/>
				</div>
			</div>
		</g:form>
		<div class="results">
			<g:each in="${results}" var="p">
				<div class="project">
					<g:link controller="project" id="${p.projectId}">${p}</g:link>
				</div>
			</g:each>
			<g:if test="${!results && q}">
				No projects matching '<em>${q}</em>' found
			</g:if>
		</div>
	</body>
</html>
