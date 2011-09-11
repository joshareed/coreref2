<%@ page import="coreref.common.Project" %>
<%@ page import="coreref.common.User" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>Project :: ${project.projectId}</title>
    </head>
    <body>
		<div class="page-header">
			<h1>Edit ${project.projectId}</h1>
		</div>
		<g:form action="update">
			<g:hiddenField name="id" value="${project.id}"/>
			<div class="clearfix <g:if test="${errors?.name}">error</g:if>">
				<label for="name">Name</label>
				<div class="input">
					<g:textField class="xlarge" name="name" value="${project?.name}" />
					<span class="help-inline">${errors.name}</span>
				</div>
			</div>
			<div class="clearfix <g:if test="${errors?.projectId}">error</g:if>">
				<label for="projectId">Project Id</label>
				<div class="input">
					<g:textField name="projectId" value="${project?.projectId}" />
					<span class="help-inline">${errors.projectId}</span>
				</div>
			</div>
			<div class="clearfix <g:if test="${errors?.ownerId}">error</g:if>">
				<label for="ownerId">Owner</label>
				<div class="input">
					<g:select name="ownerId" from="${User.mongoCollection.list()}" value="${project.ownerId}"
						optionKey="_id" optionValue="${{it.firstName + ' ' + it.lastName}}"/>
					<span class="help-inline">${errors.ownerId}</span>
				</div>
			</div>
			<fieldset class="metadata">
				<legend>Metadata</legend>
				<g:each in="${lexicon}" var="l">
					<g:set var="entry" value="${l.value}" />
					<g:set var="value" value="${project.metadata[entry.key]}"/>
					<div class="clearfix js-${entry.key}">
						<label>${entry.name}</label>
						<div class="input">
							<g:textField name="metadata.${entry.key}" value="${value ?: ''}" />
							<g:if test="${entry.help}">
								<span class="help-inline">${entry.help}</span>
							</g:if>
						</div>
					</div>
				</g:each>
			</fieldset>
			<div class="actions">
				<g:submitButton name="create" class="btn primary" value="Update" />
				<g:link action="show" id="${project.id}" class="btn">Cancel</g:link>
			</div>
		</g:form>
    </body>
</html>
