package coreref.common

class LexiconTagLib {
	static namespace = 'l'

	def lexiconService

	def label = { attrs ->
		def key = attrs.key
		if (!key) { return }

		def entry = lexiconService.getEntry(key)
		if (entry) {
			out << entry.name
		}
	}

	def value = { attrs ->
		def key = attrs.key
		if (!key) { return }

		def src = attrs.src
		def value = attrs.value
		def entry = lexiconService.getEntry(key)
		if (src && entry) {
			def plugin = entry.plugin ?: 'common'
			def template = entry.template
			if (template && !template.startsWith('/')) {
				template = '/lexicon/' + template
			}
			if (template) {
				out << render(plugin: plugin, template: template, model: [src: src, key: key, value: value, entry: entry])
			} else {
				out << value
			}
		}
	}
}