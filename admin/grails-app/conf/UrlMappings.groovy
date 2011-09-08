class UrlMappings {

	static mappings = {
		"/" {
			controller = { session?.user ? 'home' : 'login' }
		}

		"/$controller/$action?/$id?" {
			constraints {
				// apply constraints here
			}
		}

		"500"(view:'/error')
	}
}
