package coreref.security

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

import coreref.common.User

class SecurityService {
	static transactional = false
	def mongoService
	def grailsApplication

	private def classRoleMap = [:]
	private def actionRoleMap = [:]

	String encodePassword(String password) {
		return md5Hex(password)
	}

	User authenticate(String email, String password) {
		def u = User.mongoCollection.find(email: email, password: encodePassword(password), enabled: true)
		if (u) {
			return new User(u)
		} else {
			return null
		}
	}

	def getRequiredRoles(String url) {
		def (controller, action, id) = ((url - '/').split('\\/') as List)

		// don't handle pages
		// TODO: allow default configuration
		if (!controller) { return null }

		def roles

		// check our actionRoleMap
		if (!roles && actionRoleMap[controller] && action) {
			roles = actionRoleMap[controller][action]
		}

		// check our classRoleMap
		if (!roles) {
			roles = classRoleMap[controller]
		}

		// expand roles to include the id
		if (roles && id) {
			return roles.collect { it.endsWith('_') ? "${it}${id}".toString() : it }
		} else {
			return roles
		}
	}

	private initialize() {
		grailsApplication.controllerClasses.each { controller ->
			// parse controller annotation
			def name = uncap(controller.name)
			def controllerAnnotation = controller.clazz.getAnnotation(Secured)
			if (controllerAnnotation) {
				classRoleMap[name] = (controllerAnnotation.value() as Set)
			}

			// parse field and method annotations
			def map = [:]
			controller.clazz.declaredFields.each { field ->
				def annotation = field.getAnnotation(Secured)
				if (annotation) {
					map[field.name] = (annotation.value() as Set)
				}
			}
			controller.clazz.declaredMethods.each { method ->
				def annotation = method.getAnnotation(Secured)
				if (annotation) {
					map[method.name] = (annotation.value() as Set)
				}
			}
			if (map) {
				actionRoleMap[name] = map
			}
		}
	}

	private uncap(String str) {
		return str[0].toLowerCase() + str[1..-1]
	}

	private String md5Hex(final String s) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("MD5")
			digest.update(s.getBytes())
			return new BigInteger(1, digest.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("No MD5 algorithm available!")
		}
	}
}
