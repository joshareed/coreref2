package coreref

import grails.test.*

import grails.util.GrailsWebUtil

class SecurityFiltersTests extends GroovyTestCase {
	def filterInterceptor
	def grailsApplication
	def grailsWebRequest
	def configService

	protected void setUp() {
		super.setUp()
	}

	protected void tearDown() {
		super.tearDown()
	}

	def simulateRequest(controllerName, actionName, session = [:]) {
		grailsWebRequest = GrailsWebUtil.bindMockWebRequest(grailsApplication.mainContext)
		grailsWebRequest.controllerName = controllerName
		grailsWebRequest.actionName = actionName
		session.each { k, v ->
			grailsWebRequest.session[k] = v
		}
		filterInterceptor.preHandle(grailsWebRequest.request, grailsWebRequest.response, null)
	}

	def getResponse() {
		grailsWebRequest.currentResponse
	}

	def testRedirectToLogin() {
		def result = simulateRequest('home', 'dashboard')
		assert false == result
		assert '/login' == response.redirectedUrl
	}

	void testRedirectToLoginWithWrongRole() {
		def result = simulateRequest('home', 'dashboard', [user: [roles: ['ADMIN']]])
		assert false == result
		assert '/login' == response.redirectedUrl
	}

	def testNoRedirect() {
		def result = simulateRequest('home', 'dashboard', [user: [roles: ['USER']]])
		assert result
	}
}
