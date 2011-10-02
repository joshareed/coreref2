import grails.test.*

import org.codehaus.groovy.grails.plugins.web.filters.DefaultGrailsFiltersClass
import org.codehaus.groovy.grails.plugins.web.filters.FilterConfig

class SecurityFiltersTests extends GroovyTestCase {
	def filter

	protected void setUp() {
		super.setUp()
		filter = new SecurityFilters()
	}

	protected void tearDown() {
		super.tearDown()
	}

	void testRedirectToLoginNoUser() {
		FilterConfig config = findConfig('loginCheck')
		assert config

		filter.securityService = [getRequiredRoles: { uri -> ['ADMIN'] }]

		def request = [forwardURI: '/forward/uri', contextPath: '']
		config.metaClass.getRequest = { -> request }
		def flash = [:]
		config.metaClass.getFlash = { -> flash }
		def session = [:]
		config.metaClass.getSession = { -> session }

		Map redirectParams = [:]
		config.metaClass.redirect = { Map m ->
			redirectParams.putAll m
		}

		assert false == config.before()
		assert [controller: 'login'] == redirectParams
		assert '/forward/uri' == session['login-forward-uri']
	}

	void testRedirectToLoginWrongRole() {
		FilterConfig config = findConfig('loginCheck')
		assert config

		filter.securityService = [getRequiredRoles: { uri -> ['ADMIN'] }]

		def request = [forwardURI: '', contextPath: '']
		config.metaClass.getRequest = { -> request }
		def flash = [:]
		config.metaClass.getFlash = { -> flash }
		def session = [user: [roles: ['USER']]]
		config.metaClass.getSession = { -> session }

		Map redirectParams = [:]
		config.metaClass.redirect = { Map m ->
			redirectParams.putAll m
		}

		assert false == config.before()
		assert [controller: 'login'] == redirectParams
		assert 'You are not authorized to access this page' == flash.error
	}

	void testNoRedirectWithProperRole() {
		FilterConfig config = findConfig('loginCheck')
		assert config

		filter.securityService = [getRequiredRoles: { uri -> ['USER'] }]

		def request = [forwardURI: '', contextPath: '']
		config.metaClass.getRequest = { -> request }
		def flash = [:]
		config.metaClass.getFlash = { -> flash }
		def session = [user: [roles: ['USER']]]
		config.metaClass.getSession = { -> session }

		Map redirectParams = [:]
		config.metaClass.redirect = { Map m ->
			redirectParams.putAll m
		}

		assert config.before()
	}

	void testNoRedirectWithNoRole() {
		FilterConfig config = findConfig('loginCheck')
		assert config

		filter.securityService = [getRequiredRoles: { uri -> [] }]

		def request = [forwardURI: '', contextPath: '']
		config.metaClass.getRequest = { -> request }
		def flash = [:]
		config.metaClass.getFlash = { -> flash }
		def session = [user: [roles: ['USER']]]
		config.metaClass.getSession = { -> session }

		Map redirectParams = [:]
		config.metaClass.redirect = { Map m ->
			redirectParams.putAll m
		}

		assert config.before()
	}

	private FilterConfig findConfig(name) {
		return findConfigs().find { it.name == name }
	}

	private List<FilterConfig> findConfigs() {
		return new DefaultGrailsFiltersClass(SecurityFilters).getConfigs(filter)
	}

}