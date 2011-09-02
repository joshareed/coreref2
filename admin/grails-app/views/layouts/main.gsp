<!DOCTYPE html>
<html>
	<head>
		<title><g:layoutTitle default="Grails" /></title>
		<link rel="stylesheet" href="${resource(dir:'css',file:'bootstrap-1.1.1.min.css')}" />
		<link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
		<link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
		<g:javascript library="jquery" plugin="jquery"/>
		<script type="text/javascript" charset="utf-8">
			$(function() {
				$('a.close').click(function(e) {
					$(this).parent().hide();
					e.preventDefault();
				});
			});
		</script>
		<g:layoutHead />
	</head>
	<body>
		<div class="topbar">
			<div class="fill">
				<div class="container">
					<h3>
						<g:link uri="/">CoreRef: Admin</g:link>
					</h3>
					<ul>
						<li class="${!params.controller ? 'active' : ''}">
							<g:link uri="/">Home</g:link>
						</li>
						<li class="${params.controller == 'application' ? 'active' : ''}">
							<g:link controller="application">Applications</g:link>
						</li>
						<li class="${params.controller == 'user' ? 'active' : ''}">
							<g:link controller="user">Users</g:link>
						</li>
					</ul>
				</div>
			</div>
		</div>
		<div class="container">
			<g:if test="${flash.message}">
				<div class="alert-message info">
	        		<a class="close" href="#">&times;</a>
	        		<p>${flash.message}</p>
	      		</div>
			</g:if>
			<g:elseif test="${flash.error}">
	      		<div class="alert-message error">
	        		<a class="close" href="#">&times;</a>
	        		<p>${flash.error}</p>
	      		</div>
			</g:elseif>
			<g:layoutBody />
		</div>
	</body>
</html>