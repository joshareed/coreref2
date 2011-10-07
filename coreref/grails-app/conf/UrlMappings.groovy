class UrlMappings {

	static mappings = {
		"/"(controller: 'home')

		"/projects/$id/$action?/$subaction?"(controller: 'project')

		"/$controller/$action?/$id?" {
			constraints {
				// apply constraints here
			}
		}

		"500"(view:'/error')
	}
}
