package coreref.services

class IodpProjectService {

    def getProject(String id) {
		String site = id[0..-2]
		String hole = id[-1]
		def project = [
			id: "iodp-${site}${hole}",
			name: "IODP ${site}${hole}",
			description: "TODO: get this from somewhere",
			metadata: [
				program: "iodp",
				expedition: "TODO: get this from somewhere",
				site: site,
				hole: hole,
				latitude: "TODO",
				longitude: "TODO",
				'start-date': "TODO",
				'end-date': "TODO",
				'water-depth': "TODO",
				'terminal-depth': "TODO"
			],
			"streams": [
				[ name: "Split Core Images", type: "http://coreref.org/types/image/split-core", data: getSplitCoreImages(site, hole) ]
			]
		]
    }

	private getSplitCoreImages(String site, String hole) {
		query("""\
			PREFIX iodp: <http://data.oceandrilling.org/core/1/>
			SELECT DISTINCT ?sourceURL ?top ?length
				FROM <http://data.oceandrilling.org/coreimages/>
			WHERE {
				?uri iodp:site "${site}" .
				?uri iodp:hole "${hole}" .
				?uri dc:source ?sourceURL .
				?uri iodp:sectiontop ?top .
				?uri iodp:curatedlength ?length .
				FILTER regex(?sourceURL, "_gy.tif" )
			}"""
		).collect { row ->
			def (url, top, length) = [row[0][1..-2], row[1] as BigDecimal, row[2] as BigDecimal]
			[url: url, top: top, base: (top + length)]
		}
	}

	private query(sparql) {
		def endpoint = new URL("http://data.oceandrilling.org/sparql?query=${sparql.encodeAsURL()}")
		def connection = endpoint.openConnection()
		connection.addRequestProperty("Accept", "text/csv")
		connection.connect()
		def data = []
		boolean first = true
		connection.inputStream.text.eachLine { line ->
			if (!first) { data << line.split(",") }
			first = false
		}
		data
	}
}
