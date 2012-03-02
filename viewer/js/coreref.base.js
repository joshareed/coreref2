// requires: jquery

// setup the coreref namespace
var coreref = coreref || {};

// coreref.Stream provide a sorted stream of data
coreref.Stream = function(options) {
	// private variables
	var _this = this;
	var _data = [];
	var _ready = false;
	var _readyQueue = [];

	// handle json data
	var handleData = function(json) {
		for (var i = 0; i < json.data.length; i++) {
			_this.add(json.data[i]);
		}
		if (json.paging && json.paging.next) {
			jQuery.getJSON(root + json.paging.next, function(foo) {
				handleData(foo);
			});
		} else {
			_ready = true;
			for (var i = 0; i < _readyQueue.length; i++) {
				_readyQueue[i](_this);
			}
		}
	}

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
	this.ready = function(func) {
		if (_ready) {
			func.call(this);
		} else {
			_readyQueue.push(func);
		}
	}

	// initialization code
	if (options.data) {
		for (var i = 0; i < options.data.length; i++) {
			this.add(options.data[i]);
		}
		_ready = true;
	} else if (options.url) {
		var root = options.root || '';
		jQuery.getJSON(root + options.url, function(json) {
			handleData(json);
		});
	}
}

coreref.Project = function(options) {
	// private vairables
	var _this;
	var _init = false;
	var _readyCount = 0;
	var _readyQueue = [];

	var handleStreamReady = function(stream) {
		_readyCount--;
		if (_readyCount <= 0) {
			for (var i = 0; i < _readyQueue.length; i++) {
				_readyQueue[i](_this);
			}
		}
	}

	// public fields
	this.id;
	this.name;
	this.description;
	this.metadata;
	this.streams;
	this.root;

	this.init = function(options) {
		if (!options) { return; }
		_init = true;
		_this = this;
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
					_readyCount++;
					s.ready(handleStreamReady);
				} else {
					if (this.root) {
						s.root = this.root;
					}
					var stream = new coreref.Stream(s);
					this.streams.push(stream);
					_readyCount++;
					stream.ready(handleStreamReady);
				}
			}
		}
	}
	this.ready = function(func) {
		if (_init && _readyCount <= 0) {
			func(this);
		} else {
			_readyQueue.push(func);
		}
	}

	// initialize the project
	this.init(options);
}