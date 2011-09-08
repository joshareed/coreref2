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
		<g:render template="/menu"/>
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