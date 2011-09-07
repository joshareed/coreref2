<%@ page import="coreref.common.Application" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="main" />
		<title>Applications</title>
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
		<h1>Applications</h1>
		<table>
			<thead>
				<tr>
					<th>Id</th>
					<th>Contact</th>
					<th>Site</th>
					<th>Status</th>
					<th class="actions-col">
						<g:link class="btn primary right" action="create">New</g:link>
					</th>
				</tr>
			</thead>
			<tbody>
			<g:each in="${list}" status="i" var="app">
				<tr>
					<td>
						<g:link action="show" id="${app.id}">${app.appId}</g:link>
					</td>
					<td>${app.contact}</td>
					<td>
						<g:if test="${app.site}">
							<a href="${app.site}">${app.site}</a>
						</g:if>
					</td>
					<td>${app.enabled ? 'Enabled' : 'Disabled'}</td>
					<td>
						<div class="buttons right">
							<g:link class="btn" action="edit" id="${app.id}">Edit</g:link>
							<g:form style="margin: 0; padding: 0; display: inline">
			                    <g:hiddenField name="id" value="${app.id}" />
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
