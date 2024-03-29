class UrlMappings {

	static mappings = {
		"/"(controller: 'home')

		"/projects/$id/admin/$action?"(controller: 'projectAdmin')
		"/projects/search"(controller: 'project', action: 'search')
		"/projects/$id/$action?"(controller: 'project')

		"/$controller/$action?/$id?" {
			constraints {
				// apply constraints here
			}
		}

		"500"(view:'/error')
	}
}
