coreref.ui.ImageTrack = function(options) {
	"use strict";
	var $this = this,
		$project,
		$stream,
		$container,
		$track,
		_drag,
		_offset = 0;

	this.lookAt = function(phys) {
		_offset = -phys * options.scale;
		if ($container) {
			$container.css('left', _offset + 'px');
		}
	};

	this.fetch = function() {
		var left = 0 - $container.position().left;
		var right = left + $track.width();
		var images = $('.img[data-ready=false]', $track);
		for (var i = 0; i < images.length; i++) {
			var img = $(images[i]);
			var l = img.position().left;
			if (l >= left && l <= right) {
				$this.loadImage(img);
			}
			if (l > right) { break; }
		}
	};

	this.loadImage = function(div) {
		div.addClass('loading');
		div.attr('data-ready', true);
		var img = new Image();
		$(img).load(function() {
			div.removeClass('loading')
				.addClass('ready')
				.append(img);
			$track.css('height', Math.max($track.height(), $(this).height()) + 'px');
		}).css('width', div.width() + 'px')
		.attr('src', div.attr('data-url'));
	};

	this.render = function(project) {
		if (!$stream) { return; }
		var data = $stream.all();
		for (var i = 0; i < data.length; i++) {
			var image = data[i];
			$('<div></div>').addClass('img')
				.attr('data-top', image.top)
				.attr('data-base', image.base)
				.attr('data-url', image.url)
				.attr('data-ready', false)
				.css({
					width: ((image.base - image.top) * options.scale) + 'px',
					left: (image.top * options.scale) + 'px'
				})
			.appendTo($container);
		}
		$this.fetch();
	};

	this.ready = function(project) {
		if (options.stream) {
			for (var i = 0; i < project.streams.length; i++) {
				if (project.streams[i].name === options.stream) {
					$stream = project.streams[i];
				}
			}
		}

		if (options.track) {
			$track = $(options.track);
			$container = $('<div></div>').addClass('container').css({
				position: 'absolute',
				left: _offset + 'px'
			}).appendTo($track);
			$track.bind('dragstart', function(event) {
				_drag = event;
			}).bind('drag', function(event) {
				_offset -= (_drag.screenX - event.screenX);
				$container.css('left', _offset + 'px');
				_drag = event;
				$this.fetch();
			}).bind('dragend', function(event) {
				$this.fetch();
			});
		}

		$this.render(project);
	};

	this.init = function(options) {
		// grab our project
		if (options.project) {
			var project = options.project;
			if (typeof project === "string") {
				$project = new coreref.Project({ url: project });
			} else {
				$project = project;
			}
		}

		// hook up to the render method
		if ($project) {
			$project.ready($this.ready);
		}
	};

	if (options) {
		$this.init(options);
	}
};