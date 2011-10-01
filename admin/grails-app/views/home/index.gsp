<html>
	<head>
		<title>CoreRef Admin :: Home</title>
		<meta name="layout" content="main" />
	</head>
	<body>
		<section class="span11 feed">
			<div class="header">
				<h1>Recent Activity</h1>
			</div>
			<g:each in="${feed}" var="a">
				<a:brief src="${a}"/>
			</g:each>
		</section>
		<br style="clear: both"/>
	</body>
</html>
