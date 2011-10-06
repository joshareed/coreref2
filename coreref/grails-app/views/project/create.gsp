<%@ page import="coreref.common.Project" %>
<%@ page import="coreref.common.User" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="main" />
		<title>Project :: New</title>
	</head>
	<body>
		<div class="page-header">
			<h1>Create a New Project</h1>
		</div>
		<g:form action="save">
			<g:hiddenField name="ownerId" value="${session?.user?.id}"/>
			<div class="clearfix <g:if test="${errors?.projectId}">error</g:if>">
				<label for="projectId">Project Id</label>
				<div class="input">
					<g:textField name="projectId" value="${project?.projectId}" class="span7" />
					<span class="help-inline">
						<g:if test="${errors.projectId}">
							${errors.projectId}
						</g:if>
						<g:else>
							must be unique, e.g. and1-1b
						</g:else>
					</span>
				</div>
			</div>
			<div class="clearfix <g:if test="${errors?.name}">error</g:if>">
				<label for="name">Name</label>
				<div class="input">
					<g:textField class="span7" name="name" value="${project?.name}" />
					<span class="help-inline">
						<g:if test="${errors.projectId}">
							${errors.projectId}
						</g:if>
						<g:else>
							the name of the project
						</g:else>
					</span>
				</div>
			</div>
			<div class="clearfix">
				<label for="desc">Description</label>
				<div class="input">
					<textarea name="desc" class="span7">${project?.desc}</textarea>
					<span class="help-inline">
						<g:if test="${errors.desc}">
							${errors.desc}
						</g:if>
						<g:else>
							(optional)
						</g:else>
					</span>
				</div>
			</div>
			<div class="clearfix">
				<label id="priv">Visibility</label>
				<div class="input">
					<ul class="inputs-list">
						<li>
							<label>
								<input type="radio" name="priv" value="1" <g:if test="${!project || project?.public}">checked="checked"</g:if> />
								<span>Public &mdash; anyone can see and join the project</span>
							</label>
						</li>
						<li>
							<label>
								<input type="radio" name="priv" value="2" <g:if test="${project && !project?.public}">checked="checked"</g:if> />
								<span>Private &mdash; only users you approve can see and join the project</span>
							</label>
						</li>
					</ul>
				</div>
			</div>
			<div class="actions" style="border: none">
				<g:submitButton name="create" class="btn primary" value="Create" />
				<g:link uri="/" class="btn">Cancel</g:link>
			</div>
		</g:form>
	</body>
</html>
