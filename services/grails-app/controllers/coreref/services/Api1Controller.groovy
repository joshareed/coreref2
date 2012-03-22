package coreref.services

import grails.converters.JSON
import util.*

class Api1Controller {
	def grailsApplication

	static {
		def AUTH = [
			'brg.ldeo.columbia.edu': [user: 'iodp', pass: 'imagestrips']
		]

		Authenticator.setDefault(new Authenticator() {
			PasswordAuthentication getPasswordAuthentication() {
				def auth = AUTH[getRequestingHost()]
				return auth ? new PasswordAuthentication(auth.user, auth.pass.toCharArray()) : null
			}
		});
	}

    def project(String type, String id) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Methods"));
		response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
		response.setHeader("Access-Control-Max-Age", "86400");

		if (grailsApplication.mainContext.containsBean("${type}ProjectService")) {
			def projectService = grailsApplication.mainContext.getBean("${type}ProjectService")
			render projectService.getProject(id) as JSON
		} else {
			response.sendError 400, "Invalid project type: ${type}"
		}
	}

	def image(String url, String rot, double physcm, double dpcm) {
		if (!url) {
			response.sendError(400, 'Missing URL param')
			return
		}

		// Fetch URL
		def remote = url.toURL()
		def remoteFile = remote.file.split('/')[-1]
		def temp = File.createTempFile('fetch', remoteFile)
		temp.withOutputStream { out -> out << remote.newInputStream() }

		// Process Image
		def out = File.createTempFile('proc', '.jpg')
		def convert = new ImageMagick()
		convert.in(temp).out(out)
		if (rot) {
			convert.option("-rotate", "-90")
		}
		if (physcm && dpcm) {
			def pixels = physcm*dpcm
			convert.option("-resize", "${pixels}x")
		}
		convert.run()

		// Output Image
		response.setHeader('Content-length', "${out.length()}")
		response.contentType = 'image/jpeg'
		response.outputStream << out.newInputStream()
		response.outputStream.flush()
	}
}
