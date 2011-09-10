eventCompileStart = { msg ->
	new File("grails-app/views/_git.gsp").text = ("git rev-parse HEAD".execute().text)
}