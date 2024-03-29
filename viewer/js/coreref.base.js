// requires: jquery

// setup the coreref namespace
var coreref = coreref || {
	buildURL: function(root, url) {
		"use strict";
		if (!root || !url) { return url; }
		var foo = url.toLowerCase();
		if (foo.indexOf("http:") === 0 || foo.indexOf("https:") === 0 || foo.indexOf("file:") === 0 ||
			foo.indexOf("/") === 0 || foo.indexOf(root) === 0) {
			return url;
		} else {
			return root + url;
		}
	},
	extractRoot: function(root, url) {
		"use strict";
		if (root || !url) { return root; }
		var i = url.lastIndexOf("/");
		if (i >= 0) {
			return url.substring(0, i + 1);
		} else {
			return "";
		}
	}
};

// coreref.Stream provide a sorted stream of data
coreref.Stream = function(options) {
	"use strict";
	// private variables
	var $this = this,
		$data = [],
		$ready = false,
		$readyQueue = [];

	function fireReady() {
		$ready = true;
		for (var i = 0; i < $readyQueue.length; i++) {
			$readyQueue[i]($this);
		}
	}

	// handle json data
	function handleData(json) {
		for (var i = 0; i < json.data.length; i++) {
			$this.add(json.data[i]);
		}
		if (json.paging && json.paging.next) {
			jQuery.getJSON(coreref.buildURL($this.root, json.paging.next), function(foo) {
				handleData(foo);
			});
		} else {
			fireReady();
		}
	}

	// public fields
	this.root = "";
	this.name = "";
	this.type = "";

	// public methods
	this.all = function() {
		return $data;
	};
	this.add = function(item) {
		var insert = 0;
		while (insert < $data.length && item.top >= $data[insert].top) {
			insert++;
		}
		$data.splice(insert, 0, item);
	};
	this.ready = function(func) {
		if ($ready) {
			func.call(this);
		} else {
			$readyQueue.push(func);
		}
	};
	this.init = function(options) {
		this.name = options.name || "";
		this.type = options.type || "";
		this.root = coreref.extractRoot(options.root || "", options.url || "");

		if (options.data) {
			for (var i = 0; i < options.data.length; i++) {
				this.add(options.data[i]);
			}
			fireReady();
		} else if (options.url) {
			jQuery.getJSON(coreref.buildURL($this.root, options.url || ""), function(json) {
				handleData(json);
			});
		}
	};

	// initialization code
	if (options) {
		this.init(options);
	}
};

coreref.Project = function(options) {
	"use strict";
	// private vairables
	var $this = this, 
		$init = false,
		$readyCount = 0,
		$readyQueue = [];

	function handleStreamReady(stream) {
		$readyCount--;
		if ($readyCount <= 0) {
			for (var i = 0; i < $readyQueue.length; i++) {
				$readyQueue[i]($this);
			}
		}
	}

	// public fields
	this.root = "";
	this.id = "";
	this.name = "";
	this.description = "";
	this.metadata = "";
	this.streams = [];

	this.init = function(options) {
		$init = true;
		this.id = options.id || "";
		this.name = options.name || "";
		this.description = options.description || "";
		this.metadata = options.metadata || {};

		// initialize our streams
		this.streams = [];
		if (options.streams) {
			for (var i = 0; i < options.streams.length; i++) {
				var s = options.streams[i];
				if (s instanceof coreref.Stream) {
					this.streams.push(s);
					$readyCount++;
					s.ready(handleStreamReady);
				} else {
					if (this.root) {
						s.root = this.root;
					}
					var stream = new coreref.Stream(s);
					this.streams.push(stream);
					$readyCount++;
					stream.ready(handleStreamReady);
				}
			}
		}
	};
	this.ready = function(func) {
		if ($init && $readyCount <= 0) {
			func(this);
		} else {
			$readyQueue.push(func);
		}
	};

	// initialize the project
	if (options) {
		this.root = coreref.extractRoot(options.root || '', options.url || '');
		if (options.url) {
			jQuery.getJSON(coreref.buildURL(this.root, options.url), function(json) {
				$this.init(json);
			});
		} else {
			this.init(options);
		}
	}
};