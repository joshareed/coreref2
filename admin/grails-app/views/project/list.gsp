<%@ page import="coreref.common.Project" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="main" />
		<title>Projects</title>
		<script type="text/javascript" charset="utf-8">
			$(function() {
				$('.buttons').hide();
				$('tbody tr').hover(
					function() { $('.buttons', this).show(); },
					function() { $('.buttons', this).hide(); }
				).css('height', '48px');
			});
		</script>
	</head>
	<body>
		<h1>Projects</h1>
		<table>
			<thead>
				<tr>
					<th>Id</th>
					<th>Owner</th>
					<th>Name</th>
					<th class="actions-col">
						<g:link class="btn primary right" action="create">New</g:link>
					</th>
				</tr>
			</thead>
			<tbody>
			<g:each in="${list}" status="i" var="project">
				<tr>
					<td>
						<g:link action="show" id="${project.id}">${project.projectId}</g:link>
					</td>
					<td>${project.owner}</td>
					<td>${project.name}</td>
					<td>
						<div class="buttons right">
							<g:link class="btn" action="edit" id="${project.id}">Edit</g:link>
							<g:form style="margin: 0; padding: 0; display: inline">
			                    <g:hiddenField name="id" value="${project.id}" />
			                    <g:actionSubmit class="btn danger" action="delete" value="Delete"
									onclick="return confirm('Are you sure?');" />
			                </g:form>
						</div>
					</td>
				</tr>
			</g:each>
			</tbody>
		</table>
	</body>
</html>
