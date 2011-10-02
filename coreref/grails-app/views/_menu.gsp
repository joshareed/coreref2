<div class="topbar">
	<div class="fill">
		<div class="container">
			<h3>
				<g:link uri="/">CoreRef</g:link>
			</h3>
			<sec:ifLoggedIn>
			<ul>
				<g:if test="${session.user}">
					<li class="${params.controller == 'home' ? 'active' : ''}">
						<g:link controller="home" action="dashboard">Dashboard</g:link>
					</li>
				</g:if>
			</ul>
			</sec:ifLoggedIn>
			<ul class="secondary-nav">
				<li class="login">
					<sec:ifLoggedIn>
						<g:link controller="login" action="logout">Logout</g:link>
					</sec:ifLoggedIn>
					<sec:ifNotLoggedIn>
						<g:link controller="login">Login</g:link>
					</sec:ifNotLoggedIn>
				</li>
			</ul>
		</div>
	</div>
</div>