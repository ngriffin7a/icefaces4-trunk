/* HTML5 camera */
(function(){
	navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia ||
								navigator.mozGetUserMedia ||
								navigator.mzGetUserMedia;
	console.log('navigator.getUserMedia support = ' + !!navigator.getUserMedia);
	
	window.URL = window.URL || window.mozURL || window.webkitURL;
	console.log('window.URL support = ' + !!window.URL);

	ice.mobi.cameraBtnOnclick = function(id, buttonLabel, captureLabel, postURL, sessionId, maxwidth, maxheight){

		function launchHTML5Camera(){

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
						cameraButton.innerHTML = captureLabel;
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
									updateThumbnail(thumbDataURL);
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

			function renderHTML5Camera(){
				var popup = document.createElement('div'),
					closeBtn = document.createElement('a'),
					video,
					videoCtr = document.createElement('div'),
					startbutton = document.createElement('a'),
					keepbutton = document.createElement('a'),
					redobutton = document.createElement('a'),
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
				
				keepbutton.className = keepbutton.className + ' mobi-hidden';
				redobutton.className = redobutton.className + ' mobi-hidden';
				
				redobutton.innerHTML = 'Redo';
				
				keepbutton.innerHTML = 'Keep';
				
				popup.appendChild(closeBtn);
				popup.appendChild(videoCtr);
				popup.appendChild(canvas);
				popup.appendChild(photo);
				popup.appendChild(startbutton);
				popup.appendChild(redobutton);
				popup.appendChild(keepbutton);
				document.body.appendChild(popup);

				new ice.mobi.button(startbutton.id);
				new ice.mobi.button(keepbutton.id);
				new ice.mobi.button(redobutton.id);

				if( !options.width ){
					options.width = popup.clientWidth - 40;
					options.height = Math.floor(options.width / 1.3333333);
					//adjust width and height down if height approaches window height
					if( options.height > (mobi._windowHeight()*0.9)){
						options.height = Math.floor(options.height * 0.8);
						options.width = Math.floor(options.width * 0.8);
					}
				}
				
				options.video = true;
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

				ctx.clearRect(0, 0, options.width, options.height);

				var successCallback = function(stream){
					
					popup.style.width = ''; //workaround
					video = document.createElement('video');
					videoCtr.appendChild(video);
					
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
							options.height = Math.floor(options.width*(3/4));
							console.log('then options.height=' + options.height + ', options.width=' + options.width + ' mobi._windowHeight()=' + mobi._windowHeight());
							//adjust width and height down if height approaches window height
							while( options.height > (mobi._windowHeight()*0.9)){
								console.log('options.height=' + options.height + ', options.width=' + options.width + ' mobi._windowHeight()=' + mobi._windowHeight());
								options.height = options.height * 0.8;
								options.width = options.width * 0.8;
							}
							options.height = Math.floor(options.height);
							options.width = Math.floor(options.width);
							video.setAttribute('width', options.width);
							video.setAttribute('height', options.height);
							canvas.setAttribute('width', options.width);
							canvas.setAttribute('height', options.height);
							streaming = true;
						}
					});   
					setTimeout(function(){ 
						popup.style.opacity = 1;
					},10);   
				},
				errorCallback = function(err){
					ice.log.debug(ice.log, "mobi:camera activated: getUserMedia is available, but no camera available, falling back to file upload");
					document.body.removeChild(document.getElementById(id+'_popup'));
					renderCameraFallbackFileUpload();
				};
				
				function takepicture() {
					var data;
					
					canvas.width = options.width;
					canvas.height = options.height;
					
					canvas.getContext('2d').drawImage(video, 0, 0, options.width, options.height);
					
					data = canvas.toDataURL('image/png');
					photo.setAttribute('src', data);

					videoCtr.className = 'mobi-hidden';
					photo.style = 'width:' + options.width + 'px;height' + options.height + 'px;';
					photo.className = '';
					startbutton.classList.add('mobi-hidden');
					keepbutton.classList.remove('mobi-hidden');
					redobutton.classList.remove('mobi-hidden');
				}
				
				function redopicture(){
					photo.classList.add('mobi-hidden');
					keepbutton.classList.add('mobi-hidden');
					redobutton.classList.add('mobi-hidden');
					startbutton.classList.remove('mobi-hidden');
					videoCtr.classList.remove('mobi-hidden');
					cameraButton.innerText = buttonLabel;
				}
				
				function keeppicture(){
					var cameraForm = ice.mobi.formOf(cameraButton);
					document.body.removeChild(popup);
					cameraButton.innerHTML = captureLabel;
					createThumbnailForVideo();
					submitImage(photo.src);
				}

				function createThumbnailForVideo(){
					var thumbCanvas = document.createElement('canvas');
					var thumbCtx = thumbCanvas.getContext('2d');
					thumbCanvas.width = 64;
					thumbCanvas.height = 64;
					thumbCtx.drawImage(photo, 0, 0, options.width, options.height, 0, 0, 64, 64);
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
				
				navigator.getUserMedia(options, successCallback, errorCallback);
			}

			var ctr = document.getElementById(id),
				streaming = false,
				cameraButton = document.getElementById(id+"_button");

			if( 'getUserMedia' in navigator && navigator.getUserMedia && 'URL' in window ){
			   renderHTML5Camera();
			}
			else{
				renderCameraFallbackFileUpload();
			}
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
				launchHTML5Camera();
			}
			else{
				origLaunchFailed(compId);
			}
		}

		var origNotSupported = bridgeit.notSupported;
		bridgeit.notSupported = function(compId, command){
			if( command === 'camera' && compId === id){
				launchHTML5Camera();
			}
			else{
				origNotSupported(compId, command);
			}
		}

		bridgeit.camera(id, 'callback'+id, cameraOptions );

	};

})();