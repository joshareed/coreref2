class UrlMappings {

	static mappings = {
		"/" {
			controller = { session?.user ? 'home' : 'login' }
		}

		"/projects/$id/$action?"(controller: 'project')

		"/$controller/$action?/$id?" {
			constraints {
				// apply constraints here
			}
		}

		"500"(view:'/error')
	}
}
