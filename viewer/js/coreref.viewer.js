coreref.Viewer = function(options) {
	var $this = this,
		$project;

	this.render = function(project) {
		document.write(project.id + ': ' + project.name + '<br/>');
		document.write('<p>' + project.description + '</p>');
		document.write('<ul>');
		for (var i = 0; i < project.streams.length; i++) {
			var stream = project.streams[i];
			document.write('<li>' + stream.name + '[' + stream.type + ']');
			var all = stream.all();
			document.write('<ul>');
			for (var j = 0; j < all.length; j++) {
				var item = all[j];
				document.write('<li>' +  item.top + '-' + item.base + ': ' + item.url + '</li>');
			}
			document.write('</ul>');
			document.write('</li>');
		}
		document.write('</ul>');
	};

	this.init = function(options) {
		if (options.project) {
			var project = options.project;
			if (typeof project === "string") {
				$project = new coreref.Project({ url: project });
			} else {
				$project = project;
			}
			if ($project) {
				$project.ready(this.render);
			}
		}
	}

	if (options) {
		this.init(options);
	}
}