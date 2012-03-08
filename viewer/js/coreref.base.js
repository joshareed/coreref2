// requires: jquery

// setup the coreref namespace
var coreref = coreref || {};

// coreref.Stream provide a sorted stream of data
coreref.Stream = function(options) {
	// private variables
	var $this = this,
		$data = [],
		$ready = false,
		$readyQueue = [];

	function fireReady() {
		$ready = true;
		for (var i in $readyQueue) {
			$readyQueue[i]($this);
		}
	}

	// handle json data
	function handleData(json) {
		for (var i in json.data) {
			$this.add(json.data[i]);
		}
		if (json.paging && json.paging.next) {
			jQuery.getJSON($this.root + json.paging.next, function(foo) {
				handleData(foo);
			});
		} else {
			fireReady();
		}
	}

	// public fields
	this.root;
	this.name;
	this.type;

	// public methods
	this.all = function() {
		return $data;
	}
	this.add = function(item) {
		var insert = 0;
		while (insert < $data.length && item.top >= $data[insert].top) {
			insert++;
		}
		$data.splice(insert, 0, item);
	}
	this.ready = function(func) {
		if ($ready) {
			func.call(this);
		} else {
			$readyQueue.push(func);
		}
	}
	this.init = function(options) {
		this.name = options.name;
		this.type = options.type;
		this.root = options.root || "";

		if (options.data) {
			for (var i in options.data) {
				this.add(options.data[i]);
			}
			fireReady();
		} else if (options.url) {
			jQuery.getJSON($this.root + options.url, function(json) {
				handleData(json);
			});
		}
	}

	// initialization code
	if (options) {
		this.init(options);
	}
}

coreref.Project = function(options) {
	// private vairables
	var $this = this, 
		$init = false,
		$readyCount = 0,
		$readyQueue = [];

	function handleStreamReady(stream) {
		$readyCount--;
		if ($readyCount <= 0) {
			for (var i in $readyQueue) {
				$readyQueue[i]($this);
			}
		}
	}

	// public fields
	this.root;
	this.id;
	this.name;
	this.description;
	this.metadata;
	this.streams;

	this.init = function(options) {
		$init = true;
		this.id = options.id || "";
		this.name = options.name || "";
		this.description = options.description || "";
		this.metadata = options.metadata || {};

		// initialize our streams
		this.streams = [];
		if (options.streams) {
			for (var i in options.streams) {
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
	}
	this.ready = function(func) {
		if ($init && $readyCount <= 0) {
			func(this);
		} else {
			$readyQueue.push(func);
		}
	}

	// initialize the project
	if (options) {
		this.root = options.root || "";
		if (options.url) {
			jQuery.getJSON(this.root + options.url, function(json) {
				$this.init(json);
			});
		} else {
			this.init(options);
		}
	}
}