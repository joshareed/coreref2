<!DOCTYPE html>
<html>
	<head>
		<title><g:layoutTitle default="CoreRef" /></title>
		<link rel="stylesheet" href="${resource(dir:'css',file:'bootstrap-1.1.1.min.css')}" />
		<link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
		<link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
		<g:javascript library="jquery" plugin="jquery"/>
		<style type="text/css" media="screen">
			.secondary-nav .login a {
				background: url(<g:resource dir="images" file="lock.png"/>) no-repeat left center;
				padding-left: 20px;
			}
		</style>
		<g:layoutHead />
	</head>
	<body>
		<div class="topbar">
			<div class="fill">
				<div class="container">
					<h3>
						<g:link uri="/">CoreRef</g:link>
					</h3>
					<ul>
						<li class="${params.controller == 'home' ? 'active' : ''}">
							<g:link controller="home" action="index">Home</g:link>
						</li>
					</ul>
					<ul class="secondary-nav">
						<li class="login">
							<g:if test="${session.user}">
								<g:link controller="login" action="logout">Logout</g:link>
							</g:if>
							<g:else>
								<g:link controller="login">Login</g:link>
							</g:else>
						</li>
					</ul>
				</div>
			</div>
		</div>
		<div class="container-fluid">
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
			<div class="sidebar">
				<g:pageProperty name="page.sidebar"/>
			</div>
			<div class="content">
				<g:layoutBody />
			</div>
		</div>
	</body>
</html>