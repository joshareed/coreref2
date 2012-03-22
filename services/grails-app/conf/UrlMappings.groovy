class UrlMappings {

	static mappings = {
		"/1/"(controller: 'api1', action: 'index')
		"/1/project/$type/$id"(controller: 'api1', action: 'project')
		"/1/image"(controller: 'api1', action: 'image')

		"/"(view:"/index")
		"500"(view:'/error')
	}
}
