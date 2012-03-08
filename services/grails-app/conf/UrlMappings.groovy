class UrlMappings {

	static mappings = {
		"/projects/$type/$id"(controller: 'project', action: 'index')
		"/$controller/$action?/$id?" {
			constraints {
				// apply constraints here
			}
		}

		"/"(view:"/index")
		"500"(view:'/error')
	}
}
