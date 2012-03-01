// requires: jquery

// setup the coreref namespace
var coreref = coreref || {};

// coreref.Stream provide a sorted stream of data
coreref.Stream = function(options) {
	// private variables
	var _this = this;
	var _data = [];

	// public fields
	this.name = options.name;
	this.type = options.type;

	// public methods
	this.all = function() {
		return _data;
	}
	this.add = function(item) {
		var insert = 0;
		while (insert < _data.length && item.top >= _data[insert].top) {
			insert++;
		}
		_data.splice(insert, 0, item);
	}

	// initialization code
	if (options.data) {
		for (var i = 0; i < options.data.length; i++) {
			this.add(options.data[i]);
		}
	} else if (options.url) {
		var root = options.root || '';
		jQuery.getJSON(root + options.url, function(data) {
			console.log(data);
		});
	}
}

coreref.Project = function(options) {
	// private vairables


	// public fields
	this.id;
	this.name;
	this.description;
	this.metadata;
	this.streams;
	this.root;

	this.init = function(options) {
		this.id = options.id || "";
		this.name = options.name || "";
		this.description = options.description || "";
		this.metadata = options.metadata || {};
		this.root = options.root || '';

		// initialize our streams
		this.streams = [];
		if (options.streams) {
			for (var i = 0; i < options.streams.length; i++) {
				var s = options.streams[i];
				if (s instanceof coreref.Stream) {
					this.streams.push(s);
				} else {
					this.streams.push(new coreref.Stream(s));
				}
			}
		}
	}

	// initialize the project
	this.init(options);
}