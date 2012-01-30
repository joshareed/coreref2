<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>CoreRef: Admin :: Login</title>
		<style type="text/css" media="screen">
			form {
				margin: 0;
				padding: 0;
			}
		</style>
    </head>
    <body>
		<div class="modal" style="position: relative; top: 50px; left: auto; margin: 0 auto; z-index: 1">
			<g:form action="auth">
			<div class="modal-header">
				<h3>Administrator Login</h3>
			</div>
			<div class="modal-body">
				<div class="clearfix">
					<label for="email">Email</label>
					<div class="input">
						<g:textField name="email" value="${user?.email}" />
					</div>
				</div>
				<div class="clearfix">
					<label for="password">Password</label>
					<div class="input">
						<g:passwordField name="password" value="" />
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<g:submitButton name="auth" class="btn primary" value="Login" />
			</div>
			</g:form>
		</div>
		<script type="text/javascript" charset="utf-8">
			$('#email').focus();
		</script>
    </body>
</html>
