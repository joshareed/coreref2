package coreref.common

class ActivityTagLib {
	static namespace = 'a'

	def full = { attrs ->
		def a = attrs.src
		if (a) {
			def content = render(template: "/activity/types/${a.action}", plugin: 'common', model: [activity: a])
			out << render(template: "/activity/full", plugin: 'common', model: [activity: a, content: content, action: a.action])
		}
	}

	def brief = { attrs ->
		def a = attrs.src
		if (a) {
			out << render(template: "/activity/brief", plugin: 'common', model: [activity: a, action: a.action])
		}
	}
}
