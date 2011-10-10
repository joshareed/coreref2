<%@ page import="coreref.common.Project" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="main" />
		<title>Admin :: ${project.projectId}</title>
		<style type="text/css" media="screen">
			.notes {
				padding-left: 15px;
				color: #AAA;
				font-size: 12px;
			}
			.well {
				padding-top: 5px;
				padding-bottom: 30px;
				margin-top: 5px;
			}
			#invites {
				width: 650px;
			}
			.invite input {
				display: block;
				margin: 10px 0px;
			}
			.admin-content .btn.slim {
				margin-top: -2px;
			}
			.admin-content .btn.slim.danger {
				margin-left: 4px;
			}
			.hover.btn.slim {
				display: none;
			}
			.admin-content td, .admin-content th {
				border-left: 0;
				border-right: 0;
				height: 20px;
			}
			.admin-content tbody tr:hover .hover {
				display: block;
			}
		</style>
		<script type="text/javascript" charset="utf-8">
			function approve(email, ref) {
				$.post(
					'${createLink(controller: "projectAdmin", action: "approve", id: project.projectId)}',
					{'email': email}
				).success(function(data) {
					ref.fadeOut();
				}).error(function(data) {
					alert(data);
				});
			}

			function ignore(email, ref) {
				$.post(
					'${createLink(controller: "projectAdmin", action: "ignore", id: project.projectId)}',
					{'email': email}
				).success(function(data) {
					ref.fadeOut();
				}).error(function(data) {
					alert(data);
				});
			}

			function kick(email, ref) {
				$.post(
					'${createLink(controller: "projectAdmin", action: "kick", id: project.projectId)}',
					{'email': email}
				).success(function(data) {
					ref.fadeOut();
				}).error(function(data) {
					alert(data);
				});
			}

			$(function() {
				$('.admin-menu').css('height', $('.admin-content').height() + 'px');
				$('[data-approve]').click(function() {
					approve($(this).attr('data-approve'), $(this).parent().parent());
					return false;
				});
				$('[data-ignore]').click(function() {
					ignore($(this).attr('data-ignore'), $(this).parent().parent());
					return false;
				});
				$('[data-kick]').click(function() {
					kick($(this).attr('data-kick'), $(this).parent().parent());
					return false;
				});
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
				<div class="admin-pending">
					<g:if test="${pending}">
						<h2>Pending</h2>
						<table>
							<thead>
								<tr>
									<th>Email</th>
									<th>Status</th>
									<th style="width: 120px">&nbsp;</th>
								</tr>
							</thead>
							<tbody>
								<g:each in="${pending}" var="p">
								<tr>
									<td>${p.email}</td>
									<td>
										<g:if test="${p.invited}">
											Invited
										</g:if>
										<g:else>
											Needs Approval
										</g:else>
									</td>
									<td>
										<g:if test="${p.invited}">
											<a href="#revoke" data-ignore="${p.email}" class="hover btn slim danger right">Revoke</a>
										</g:if>
										<g:else>
											<a href="#ignore" data-ignore="${p.email}" class="btn slim danger right">Ignore</a>
											<a href="#approve" data-approve="${p.email}" class="btn slim right">Approve</a>
										</g:else>
									</td>
								</tr>
								</g:each>
							</tbody>
						</table>
					</g:if>
				</div>
				<div class="admin-members">
					<h2>Members</h2>
					<div class="well invite">
						<g:form action="invite">
							<g:hiddenField name="id" value="${project.projectId}" />
							<h3>Add new members<span class="notes">type email addresses separated by commas</span></h3>
							<textarea name="invites" id="invites" class="" rows="3"></textarea>
							<input type="submit" class="btn right" value="Invite"/>
						</g:form>
					</div>
					<g:set var="members" value="${project.members}" />
					<table>
						<thead>
							<tr>
								<th>First</th>
								<th>Last</th>
								<th>Email</th>
								<th style="width: 50px">&nbsp;</th>
							</tr>
						</thead>
						<tbody>
							<g:each in="${members}" var="u">
							<tr>
								<td>${u.firstName}</td>
								<td>${u.lastName}</td>
								<td>${u.email}</td>
								<td>
									<a href="#kick" data-kick="${u.email}" class="hover btn slim danger right">Kick</a>
								</td>
							</tr>
							</g:each>
							<g:if test="${!members}">
								<tr>
									<td colspan="4" class="empty">
										No members
									</td>
								</tr>
							</g:if>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</body>
</html>
