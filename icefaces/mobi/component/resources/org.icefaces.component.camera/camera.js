/* HTML5 camera */
(function(){
	navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia ||
								navigator.mozGetUserMedia ||
								navigator.mzGetUserMedia;
	console.log('navigator.getUserMedia support = ' + (!!navigator.getUserMedia || (navigator.mediaDevices && navigator.mediaDevices.getUserMedia)));
	
	window.URL = window.URL || window.mozURL || window.webkitURL;
	console.log('window.URL support = ' + !!window.URL);

	var videoDevices = [];
	var multipleCameras = false;
	if (navigator.mediaDevices) {
		var promise = navigator.mediaDevices.enumerateDevices();
		if (promise) {
			promise.then(function(devices) {
				var i;
				for (i = 0; i < devices.length; i++) {
					var device = devices[i];
					if (device.kind == 'videoinput') {
						videoDevices.push(device);
					}
				}
				if (videoDevices.length > 1) multipleCameras = true;
			});
		}
	}

	ice.mobi.cameraBtnOnclick = function(id, buttonLabel, captureLabel, postURL, sessionId, maxwidth, maxheight, buttonImage, captureButtonImage){

		var ctr = document.getElementById(id);
		var cameraButton = document.getElementById(id+"_button");

		function getHiddenInput(){
			var hiddenInputId = id + "_hidden";
			var hiddenInput = document.getElementById(hiddenInputId);
			if( !hiddenInput ){
				hiddenInput = document.createElement('input');
				hiddenInput.type = 'hidden';
				hiddenInput.name = id;
				hiddenInput.id = hiddenInputId;

			}
			return hiddenInput;
		}

		function getThumbnail(){
			var thumbId = id + "-thumb";
			var thumbnail = document.getElementById(thumbId);
			return thumbnail;
		}

		function updateThumbnail(dataURL){
			var thumbnail = getThumbnail();
			if( thumbnail ){
				thumbnail.src = dataURL;
				var cl = thumbnail.parentNode.classList;
				cl.remove('mobi-thumb');
				cl.add('mobi-thumb-done');
			}
		}

		function hideThumbnail(){
			var thumbnail = getThumbnail();
			if( thumbnail ){
				thumbnail.style.display = "none";
			}
		}

		function renderCameraFallbackFileUpload(){

			function getFileInput(){
				var input = document.getElementById(id+'_fileupload');
				if( !input ){
					input = document.createElement('input');
					input.id = id + '_fileupload';
					input.type = 'file';
					var isIE = navigator.userAgent.toLowerCase().indexOf('msie') > -1;
					input.accept = isIE ? 'image/*;capture=camera' : 'image/*';
					input.capture = true;
					input.name = id;
					input.addEventListener('change', convertImageFromFile, false);
				}
				return input;
			}

			function startSpinner(){
				var input = getFileInput();
				if( input ){
					var spinner = document.createElement('i');
					spinner.id = id + '_spinner';
					spinner.className = 'fa fa-spinner fa-spin';
					input.parentNode.appendChild(spinner);
				}
			}

			function stopSpinner(){
				var spinner = document.getElementById(id + '_spinner');
				if( spinner ){
					spinner.parentNode.removeChild(spinner);
				}
			}

			function submitImageFile(file, success, failure){
				var encodedForm = '';
				var formData = new FormData();
				//TODO missing extra parameters ?
				formData.append(id, file);
				var request = new XMLHttpRequest();
				request.addEventListener("load", success, false);
				request.addEventListener("error", failure, false);
				request.open("POST", postURL);
				request.setRequestHeader("JSESSIONID", sessionId);
				request.send(formData);
			}

			function convertImageFromFile(){
				var cameraForm = ice.mobi.formOf(cameraButton),
					hiddenInput = getHiddenInput(),
					fileInput = getFileInput(),
					file = fileInput.files[0],
					img = new Image();

				function onSubmitSuccess(){
					var reader = new FileReader();					
					var markup = '<span class="ui-button-text">'+captureLabel+'</span>';
					if (buttonImage) markup = captureButtonImage ? '<img src="'+captureButtonImage+'" />' : '<img src="'+buttonImage+'" />';
					cameraButton.innerHTML = markup;
					reader.onload = function(event){
						img.onload = function(){
							setTimeout(function(){								
								var thumbDataURL;
								var canvas = document.createElement('canvas');
								var ctx = canvas.getContext('2d');
								canvas.width = 64;
								canvas.height = 64;
								ctx.drawImage(img, 0, 0, img.width, img.height, 0, 0, 64, 64);
								thumbDataURL = canvas.toDataURL();
								canvas = null;
								fileInput.parentElement.removeChild(fileInput);
								cameraButton.style.display = 'inline-block';
								if (ice.mobi.cameraBtnOnclick.getMobileOperatingSystem() == 'iOS')
									updateThumbnail(event.target.result);
								else updateThumbnail(thumbDataURL);
								stopSpinner();
							},0);
							
						}
						img.src = event.target.result;
					}
					reader.readAsDataURL(file);
				}

				function onSubmitFailure(error){
					stopSpinner();
					alert('Error uploading file: ' + error.responseText);
				}

				//check for image
				if( file.type.indexOf('image') === -1 ){
					console.log('ERROR: camera file upload selected non-image: ' + file.type);
					return;
				}

				startSpinner();
				submitImageFile(file, onSubmitSuccess, onSubmitFailure);
				


				

				
			}

			var input = getFileInput();
			ctr.appendChild(input);
			cameraButton.style.display = 'none';
		}

		function launchHTML5Camera(){

			function renderHTML5Camera(deviceId){
					  if (window.stream && ice.mobi.cameraBtnOnclick.getMobileOperatingSystem() == 'Android') {
						window.stream.getTracks().forEach(function(track){
						  track.stop();
						})
					  }
				var popup = document.createElement('div'),
					closeBtn = document.createElement('a'),
					video,
					videoCtr = document.createElement('div'),
					startbutton = document.createElement('a'),
					keepbutton = document.createElement('a'),
					redobutton = document.createElement('a'),
					cancelbutton = document.createElement('a'),
					togglebutton = document.createElement('a'),
					canvas = document.createElement('canvas'),
					ctx = canvas.getContext('2d'),
					photo = document.createElement('img'),
					options = {},
					img = new Image();

				popup.id = id + '_popup';
				popup.className = 'mobi-camera-popup';
				popup.style.opacity = 0; //TODO test IE
				photo.className = canvas.className = 'mobi-hidden';
				if( options.width ){
					popup.style.width = '' + (options.width + 40) + 'px';
				}

				closeBtn.id = 'close_' + id;
				ice.mobi.addListener(closeBtn, 'click', function(){ document.body.removeChild(popup)});
				closeBtn.className = 'mobi-camera-close no-drag';
				closeBtn.innerHTML = "<i class='icon-remove'></i>";
				
				videoCtr.id = id + '_videoCtr';
				
				startbutton.id = 'start_' + id;
				
				startbutton.innerHTML = 'Take Picture';
				
				canvas.className = startbutton.className + ' mobi-hidden';
				keepbutton.id = 'keep_' + id;
				redobutton.id = 'redo_' + id;
				cancelbutton.id = 'cancel_' + id;
				togglebutton.id = 'toggle_' + id;
				
				keepbutton.className = keepbutton.className + ' mobi-hidden';
				redobutton.className = redobutton.className + ' mobi-hidden';
				cancelbutton.className = cancelbutton.className;
				togglebutton.className = togglebutton.className;
				
				redobutton.innerHTML = 'Redo';

				cancelbutton.innerHTML = 'Cancel';

				togglebutton.innerHTML = 'Next Camera';
				
				keepbutton.innerHTML = 'Keep';
				
				popup.appendChild(closeBtn);
				popup.appendChild(videoCtr);
				popup.appendChild(canvas);
				popup.appendChild(photo);
				popup.appendChild(startbutton);
				popup.appendChild(redobutton);
				popup.appendChild(keepbutton);
				popup.appendChild(cancelbutton);
				if (multipleCameras) popup.appendChild(togglebutton);

				document.body.appendChild(popup);

				new ice.mobi.button(startbutton.id);
				new ice.mobi.button(keepbutton.id);
				new ice.mobi.button(redobutton.id);
				new ice.mobi.button(cancelbutton.id);
				if (multipleCameras) new ice.mobi.button(togglebutton.id);

				document.getElementById(startbutton.id).style.marginLeft = '10px';
				document.getElementById(keepbutton.id).style.marginLeft = '10px';
				document.getElementById(cancelbutton.id).style.marginLeft = '10px';
				if (multipleCameras) document.getElementById(togglebutton.id).style.marginLeft = '10px';

				if( !options.width ){
					options.width = popup.clientWidth - 40;
					options.height = Math.floor(options.width / 1.3333333);
					//adjust width and height down if height approaches window height
					if( options.height > (mobi._windowHeight()*0.9)){
						options.height = Math.floor(options.height * 0.8);
						options.width = Math.floor(options.width * 0.8);
					}
				}

				// use first videoinput device in the list
				if (!deviceId) {
					if (videoDevices.length > 0) deviceId = videoDevices[0].deviceId;
				}

				if (deviceId) {
					if (ice.mobi.cameraBtnOnclick.getMobileOperatingSystem() == 'Android') {
						options.video = { 'optional': [ { sourceId: deviceId } ] };
					} else {
						options.video = { 'deviceId': { exact: deviceId } };
					}
				} else options.video = true;
				options.audio = false;
				//below params for shim
				options.el = id + '_videoCtr';
				options.extern = null;
				options.append = true;
				options.mode = 'callback';
				options.canvasContext = ctx;
				options.context = "";
				options.img = new Image();
				options.image = ctx.getImageData(0, 0, options.width, options.height);

				var successCallback = function(stream){
					window.stream = stream;
					popup.style.width = ''; //workaround
					video = document.createElement('video');
					video.id = id + '_video';
					videoCtr.appendChild(video);
					var bounds = video.getBoundingClientRect();
					var videoHeight = bounds.bottom - bounds.top;
					ctx.clearRect(0, 0, options.width, videoHeight);

					if (navigator.mozGetUserMedia) {
						video.mozSrcObject = stream;
					} 
					else {
						video.src = window.URL.createObjectURL(stream);
					}

					video.play();
					
					ice.mobi.addListener(video, 'canplay', function(ev){
						if (!streaming) {
							options.width = popup.clientWidth - 40;
							console.log('begin options.height=' + options.height + ', options.width=' + options.width + ' mobi._windowHeight()=' + mobi._windowHeight());
							options.height = Math.floor(options.width*(0.75));
							console.log('then options.height=' + options.height + ', options.width=' + options.width + ' mobi._windowHeight()=' + mobi._windowHeight());
							//adjust width and height down if height approaches window height
							while( options.height > (mobi._windowHeight()*0.9)){
								console.log('options.height=' + options.height + ', options.width=' + options.width + ' mobi._windowHeight()=' + mobi._windowHeight());
								options.height = options.height * 0.8;
								options.width = options.width * 0.8;
							}
							options.height = Math.floor(options.height);
							options.width = Math.floor(options.width);
							video.width = options.width;
							//video.height = options.height;
							canvas.width = options.width;
							canvas.height = videoHeight;
							streaming = true;

							if (ice.mobi.cameraBtnOnclick.getMobileOperatingSystem() == 'Android') {
								addOverlay();
							}
						}
					});   
					setTimeout(function(){ 
						popup.style.opacity = 1;
					},10);
				},
				errorCallback = function(err){
					var errorMessage = '';
					if (typeof err == 'string') errorMessage = err;
					else {
						for (var p in err) errorMessage += p + ' -> ' + err[p] + ', ';
					}
					ice.log.debug(ice.log, "mobi:camera activated: getUserMedia is available, but no camera available, falling back to file upload. Error: " + errorMessage);
					document.body.removeChild(document.getElementById(id+'_popup'));
					renderCameraFallbackFileUpload();
				};

				function orientationChangeListener() {
					removeOverlay();
					setTimeout(addOverlay, 1000);
				}

				function addOverlay() {
					var notice = document.createElement('div');
					notice.id = id + '_notice';
					var bounds = video.getBoundingClientRect();
					notice.setAttribute('style', 'position:fixed;z-index:10;'+
						'background-color:#000;color:#fff;font-family:Verdana;font-size:x-large;font-weight:bold;'+
						'text-align:center;width:'+video.offsetWidth+'px;height:'+video.offsetHeight+'px;'+
						'top:'+bounds.top+'px;left:'+bounds.left+'px;opacity:0.3');
					notice.innerHTML = 'Touch to Start Camera Stream';
					notice.setAttribute('touchstart', 'document.getElementById("'+id+'_videoCtr").removeChild(document.getElementById("'+id+'_notice"));'+
					'document.getElementById("'+id+'_video").play();');
					notice.setAttribute('onclick',  'document.getElementById("'+id+'_videoCtr").removeChild(document.getElementById("'+id+'_notice"));'+
					'document.getElementById("'+id+'_video").play();');
					videoCtr.appendChild(notice);
					window.addEventListener('orientationchange', orientationChangeListener);
				}

				function removeOverlay() {
					var notice = document.getElementById(id + '_notice');
					if (notice && videoCtr) videoCtr.removeChild(notice);
					window.removeEventListener("orientationchange", orientationChangeListener);					
				}
				
				function takepicture() {
					var data;
					
					canvas.width = video.width;
					var bounds = video.getBoundingClientRect();
					var videoHeight = bounds.bottom - bounds.top;
					canvas.height = videoHeight;

					var width = video.width;
					var height = videoHeight;

					if (maxwidth > 0 || maxheight > 0) {
						if (maxwidth && options.width > maxwidth) {
							width = maxwidth;
							height = (maxwidth * videoHeight) / video.width;
							height = maxheight && height > maxheight ? maxheight : height;
						} else if (maxheight && videoHeight > maxheight) {
							height = maxheight;
							width = (maxheight * video.width) / videoHeight;
						}
						try {
							canvas.width = width;
							canvas.height = height;
							canvas.getContext('2d').drawImage(video, 0, 0, video.width, videoHeight, 0, 0, width, height);
						} catch (e) {
							canvas.width = video.width;
							canvas.height = videoHeight;
							canvas.getContext('2d').drawImage(video, 0, 0, video.width, videoHeight);
							width = video.width;
							height = videoHeight;
						}
					} else {
						canvas.getContext('2d').drawImage(video, 0, 0, video.width, videoHeight);
					}
					
					data = canvas.toDataURL('image/png');
					photo.setAttribute('src', data);

					videoCtr.className = 'mobi-hidden';
					photo.width = width;
					photo.height = height;
					photo.className = '';
					startbutton.classList.add('mobi-hidden');
					togglebutton.classList.add('mobi-hidden');
					keepbutton.classList.remove('mobi-hidden');
					redobutton.classList.remove('mobi-hidden');
				}
				
				function redopicture(){
					photo.classList.add('mobi-hidden');
					keepbutton.classList.add('mobi-hidden');
					redobutton.classList.add('mobi-hidden');
					startbutton.classList.remove('mobi-hidden');
					togglebutton.classList.remove('mobi-hidden');
					videoCtr.classList.remove('mobi-hidden');
					var markup = '<span class="ui-button-text">' + buttonLabel + '</span>';
					if (buttonImage) markup = '<img src="'+buttonImage+'" />';
					cameraButton.innerHTML = markup;
				}
				
				function keeppicture(){
					var cameraForm = ice.mobi.formOf(cameraButton);
					document.body.removeChild(popup);
					var markup = '<span class="ui-button-text">'+captureLabel+'</span>';
					if (buttonImage) markup = captureButtonImage ? '<img src="'+captureButtonImage+'" />' : '<img src="'+buttonImage+'" />';
					cameraButton.innerHTML = markup;
					createThumbnailForVideo();
					submitImage(photo.src);
				}

				function cancelpicture(){
					document.body.removeChild(popup);
				}

				function togglecamera(){
					removeOverlay();
					  if (window.stream && ice.mobi.cameraBtnOnclick.getMobileOperatingSystem() == 'Android') {
						if (video) {
							video.src = null;
							if (video.parentElement) video.parentElement.removeChild(video);
						}
						window.stream.getTracks().forEach(function(track){
						  track.stop();
						})
					  }
					var nextDeviceId = null;
					var i;
					for (i = 0; i < videoDevices.length; i++) {
						var device = videoDevices[i];
						if (device.deviceId == deviceId) { // found current device, use next
							i++;
							if (i < videoDevices.length) {
								nextDeviceId = videoDevices[i].deviceId;
							} else {
								nextDeviceId = videoDevices[0].deviceId;
							}
							break;
						}
					}
					streaming = false;
					if (nextDeviceId) {
						deviceId = nextDeviceId;
						if (ice.mobi.cameraBtnOnclick.getMobileOperatingSystem() == 'Android') {
							options.video = { 'optional': [ { sourceId: nextDeviceId } ] };
						} else {
							options.video = { 'deviceId': { exact: nextDeviceId } };
						}
					} else options.video = true;
					if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia
							&& ice.mobi.cameraBtnOnclick.getMobileOperatingSystem() != 'Android') {
						navigator.mediaDevices.getUserMedia(options).then(successCallback).catch(errorCallback);
					} else if (navigator.getUserMedia) {
						navigator.getUserMedia(options, successCallback, errorCallback);
					}
				}

				function createThumbnailForVideo(){
					var thumbCanvas = document.createElement('canvas');
					var thumbCtx = thumbCanvas.getContext('2d');
					var thumb = getThumbnail();

					var thumbWidth, thumbHeight;
					if (photo.width >= photo.height) {
						thumbWidth = 64;
						thumb.setAttribute('width', '64');
						thumbHeight = (photo.height * 64.0) / photo.width;
						thumb.setAttribute('height', '');
					} else {
						thumbHeight = 64;
						thumb.setAttribute('height', '64');
						thumbWidth = (photo.width * 64.0) / photo.height;
						thumb.setAttribute('width', '');
					}

					thumbCanvas.width = thumbWidth;
					thumbCanvas.height = thumbHeight;
					thumbCtx.drawImage(photo, 0, 0, photo.width, photo.height, 0, 0, thumbWidth, thumbHeight);
					updateThumbnail(thumbCanvas.toDataURL('image/png'));
				}

				function submitImage(dataURL){
					var encodedForm = '';
					var formData = new FormData();
					//TODO missing extra parameters ?
					formData.append(id, dataURLToBlob(dataURL));
					var request = new XMLHttpRequest();
					request.open("POST", postURL);
					request.setRequestHeader("JSESSIONID", sessionId);
					request.send(formData);
				}

				function dataURLToBlob(dataURL){
					var parts = dataURL.split(',');
					var byteString = atob(parts[1]);
					var mimeString = parts[0].split(':')[1].split(';')[0];
					var ab = new ArrayBuffer(byteString.length);
					var ia = new Uint8Array(ab);
					for( var i = 0 ; i < byteString.length ; i ++ ){
						ia[i] = byteString.charCodeAt(i);
					}
					var bb = new Blob([ab], {"type": mimeString});
					return bb;
				}
				
				ice.mobi.addListener(startbutton, 'click', function(ev){
					takepicture();
					ev.preventDefault();
				});
				
				ice.mobi.addListener(keepbutton, 'click', function(ev){
					keeppicture();
					ev.preventDefault();
				});
				
				ice.mobi.addListener(redobutton, 'click', function(ev){
					redopicture();
					ev.preventDefault();
				});

				ice.mobi.addListener(cancelbutton, 'click', function(ev){
					cancelpicture();
					ev.preventDefault();
				});

				if (multipleCameras) ice.mobi.addListener(togglebutton, 'click', function(ev){
					togglecamera();
					ev.preventDefault();
				});
				
				if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia
						&& ice.mobi.cameraBtnOnclick.getMobileOperatingSystem() != 'Android') {
					navigator.mediaDevices.getUserMedia(options).then(successCallback).catch(errorCallback);
				} else if (navigator.getUserMedia) {
					navigator.getUserMedia(options, successCallback, errorCallback);
				}
			}

			var streaming = false;

			renderHTML5Camera();
		}

		var cameraOptions =  {
			postURL: postURL,
			cookies:{
				JSESSIONID: sessionId
			}
		};
		if( maxwidth ){
			cameraOptions.maxwidth = maxwidth;
		}
		if( maxheight ){
			cameraOptions.maxheight = maxheight;
		}

		//setup fallback callback as a chain of responsibility
		var origLaunchFailed = bridgeit.launchFailed;
		bridgeit.launchFailed = function(compId){
			if( compId === id ){
				origLaunchFailed(compId);
				if (window[id + '_fallbackObserver']) {
					clearTimeout(window[id + '_fallbackObserver']);
				}
				window[id + '_fallbackObserver'] = setTimeout(function() {
					renderCameraFallbackFileUpload();
				}, 100);
			}
			else{
				origLaunchFailed(compId);
			}
		}

		if (ice.mobi.cameraBtnOnclick.getMobileOperatingSystem() == 'iOS') {
			renderCameraFallbackFileUpload();
		} else {
			var origNotSupported = bridgeit.notSupported;
			bridgeit.notSupported = function(compId, command){
				if( command === 'camera' && compId === id){
					origNotSupported(compId, command);
					if (window[id + '_fallbackObserver']) {
						clearTimeout(window[id + '_fallbackObserver']);
					}
					window[id + '_fallbackObserver'] = setTimeout(function() {
						renderCameraFallbackFileUpload();
					}, 100);
				}
				else{
					origNotSupported(compId, command);
				}
			}

			if ('URL' in window && (navigator.getUserMedia || (navigator.mediaDevices && navigator.mediaDevices.getUserMedia))) {
				launchHTML5Camera();
			} else {
				bridgeit.camera(id, 'callback'+id, cameraOptions );
			}
		}
	};

	ice.mobi.cameraBtnOnclick.getMobileOperatingSystem = function() {
	  var userAgent = navigator.userAgent || navigator.vendor || window.opera;

		  // Windows Phone must come first because its UA also contains "Android"
		if (/windows phone/i.test(userAgent)) {
			return "Windows Phone";
		}

		if (/android/i.test(userAgent)) {
			return "Android";
		}

		// iOS detection from: http://stackoverflow.com/a/9039885/177710
		if (/iPad|iPhone|iPod/.test(userAgent) && !window.MSStream) {
			return "iOS";
		}

		return "unknown";
	};

})();