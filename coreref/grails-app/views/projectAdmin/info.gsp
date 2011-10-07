<%@ page import="coreref.common.Project" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="main" />
		<title>Admin :: ${project.projectId}</title>
		<script type="text/javascript" charset="utf-8">
			$(function() {
				$('.admin-menu').css('height', $('.admin-content').height() + 'px');
			});
		</script>
	</head>
	<body>
		<g:render template="header" model="[project: project]"/>
		<div class="row">
			<div class="span4">
				<g:render template="menu" model="[project: project]"/>
			</div>
			<div class="span12 admin-content">
				<g:form action="update" class="form-stacked">
					<g:hiddenField name="ownerId" value="${session?.user?.id}"/>
					<g:hiddenField name="id" value="${project?.id}"/>
					<div class="clearfix <g:if test="${errors?.projectId}">error</g:if>">
						<label for="projectId">Project Id</label>
						<div class="input">
							<g:textField name="projectId" value="${project?.projectId}" class="span7" />
							<span class="help-inline">
								<g:if test="${errors?.projectId}">
									${errors?.projectId}
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
								<g:if test="${errors?.name}">
									${errors?.name}
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
								<g:if test="${errors?.desc}">
									${errors?.desc}
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
						<g:submitButton name="update" class="btn" value="Update" />
					</div>
				</g:form>
			</div>
		</div>
	</body>
</html>
