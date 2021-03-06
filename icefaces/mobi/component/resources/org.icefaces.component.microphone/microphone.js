/* HTML5 microphone */
(function(){
	navigator.getUserMedia = navigator.getUserMedia || navigator.webkitGetUserMedia ||
								navigator.mozGetUserMedia ||
								navigator.mzGetUserMedia;
	console.log('navigator.getUserMedia support = ' + (!!navigator.getUserMedia || (navigator.mediaDevices && navigator.mediaDevices.getUserMedia)));
	
	window.URL = window.URL || window.mozURL || window.webkitURL;
	console.log('window.URL support = ' + !!window.URL);

	ice.mobi.microphoneBtnOnclick = function(id, buttonLabel, captureLabel, postURL, sessionId, recorderCfg){

		var ctr = document.getElementById(id);
		var microphoneButton = document.getElementById(id+"_button");

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

		function renderMicrophoneFallbackFileUpload(){

			function getFileInput(){
				var input = document.getElementById(id+'_fileupload');
				if( !input ){
					input = document.createElement('input');
					input.id = id + '_fileupload';
					input.type = 'file';
					var isIE = navigator.userAgent.toLowerCase().indexOf('msie') > -1;
					input.accept = isIE ? 'audio/*;capture=microphone' : 'audio/*';
					input.capture = 'microphone';
					input.name = id;
					input.addEventListener('change', convertAudioFromFile, false);
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

			function submitAudioFile(file, success, failure){
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

			function convertAudioFromFile(){
				var microphoneForm = ice.mobi.formOf(microphoneButton),
					hiddenInput = getHiddenInput(),
					fileInput = getFileInput(),
					file = fileInput.files[0],
					audio = document.createElement('audio');

				function onSubmitSuccess(){
					var reader = new FileReader();					
					var markup = '<span class="ui-button-text">'+captureLabel+'</span>';
					microphoneButton.innerHTML = markup;
					reader.onload = function(event){
						audio.onload = function(){
							setTimeout(function(){
								fileInput.parentElement.removeChild(fileInput);
								microphoneButton.style.display = 'inline-block';
								stopSpinner();
							},0);
							
						}
						audio.src = event.target.result;
					}
					reader.readAsDataURL(file);
				}

				function onSubmitFailure(error){
					stopSpinner();
					alert('Error uploading file: ' + error.responseText);
				}

				//check for audio
				if( file.type.indexOf('audio') === -1 ){
					console.log('ERROR: microphone file upload selected non-audio: ' + file.type);
					return;
				}

				startSpinner();
				submitAudioFile(file, onSubmitSuccess, onSubmitFailure);
			}

			var input = getFileInput();
			ctr.appendChild(input);
			microphoneButton.style.display = 'none';
		}

		function launchHTML5Microphone(){

			function renderHTML5Microphone(){
				var popup = document.createElement('div'),
					closeBtn = document.createElement('a'),
					audioCtr = document.createElement('div'),
					recordbutton = document.createElement('a'),
					keepbutton = document.createElement('a'),
					cancelbutton = document.createElement('a'),
					audio = document.createElement('audio'),
					timer = document.createElement('div'),
					timerSpan = document.createElement('span'),
					recDotCanvas = document.createElement('canvas'),
					options = {},
					audioContext = new AudioContext();
					audioInput = null,
					realAudioInput = null,
					inputPoint = null,
					audioRecorder = null;

				popup.id = id + '_popup';
				popup.className = 'mobi-microphone-popup';
				popup.style.opacity = 0;

				closeBtn.id = 'close_' + id;
				ice.mobi.addListener(closeBtn, 'click', function(){ document.body.removeChild(popup)});
				closeBtn.className = 'mobi-microphone-close no-drag';
				closeBtn.innerHTML = "<i class='icon-remove'></i>";
				
				audioCtr.id = id + '_audioCtr';

				timer.id = 'timer_' + id;
				timer.style.padding = '10px';
				timerSpan.innerHTML = '00:00';
				timerSpan.style.marginLeft = '10px';
				recDotCanvas.width = 16;
				recDotCanvas.height = 16;
				
				recordbutton.id = 'record_' + id;
				
				recordbutton.innerHTML = 'Start Recording';
				
				keepbutton.id = 'keep_' + id;
				cancelbutton.id = 'cancel_' + id;
				
				keepbutton.className = keepbutton.className + ' mobi-hidden';
				cancelbutton.className = cancelbutton.className;

				cancelbutton.innerHTML = 'Cancel';
				
				keepbutton.innerHTML = 'Keep Recording';
				
				popup.appendChild(closeBtn);
				popup.appendChild(audioCtr);
				audio.setAttribute('controls', 'controls');
				audio.classList.add('mobi-hidden');
				popup.appendChild(audio);
				timer.appendChild(recDotCanvas);
				timer.appendChild(timerSpan);
				popup.appendChild(timer);
				popup.appendChild(recordbutton);
				popup.appendChild(keepbutton);
				popup.appendChild(cancelbutton);

				document.body.appendChild(popup);

				new ice.mobi.button(recordbutton.id);
				new ice.mobi.button(keepbutton.id);
				new ice.mobi.button(cancelbutton.id);

				document.getElementById(recordbutton.id).style.marginLeft = '10px';
				document.getElementById(keepbutton.id).style.marginLeft = '10px';
				document.getElementById(cancelbutton.id).style.marginLeft = '10px';

				var successCallback = function(stream){
					
					popup.style.width = ''; //workaround
					
					inputPoint = audioContext.createGain();

					// Create an AudioNode from the stream.
					realAudioInput = audioContext.createMediaStreamSource(stream);
					audioInput = realAudioInput;
					audioInput.connect(inputPoint);

					audioRecorder = new Recorder( inputPoint, recorderCfg );

					zeroGain = audioContext.createGain();
					zeroGain.gain.value = 0.0;
					inputPoint.connect( zeroGain );
					zeroGain.connect( audioContext.destination );
					
					setTimeout(function(){ 
						popup.style.opacity = 1;
					},10);   
				},
				errorCallback = function(err){
					ice.log.debug(ice.log, "mobi:microphone activated: getUserMedia is available, but no microphone available, falling back to file upload");
					document.body.removeChild(document.getElementById(id+'_popup'));
					renderMicrophoneFallbackFileUpload();
				};

				var recording = false;
				
				function togglerecording() {
					if (recording) {
						audioRecorder.stop();
						audioRecorder.exportWAV(doneEncoding);
						recording = false;
						stopTimer();
						audio.classList.remove('mobi-hidden');
						keepbutton.classList.remove('mobi-hidden');
						recordbutton.innerHTML = 'Redo Recording';
						var markup = buttonLabel;
					} else {
						if (!audioRecorder)
							return;
						audioRecorder.clear();
						audioRecorder.record();
						recording = true;
						audio.classList.add('mobi-hidden');
						keepbutton.classList.add('mobi-hidden');
						recordbutton.innerHTML = 'Stop Recording';
						startTimer();
						delete audio.src;
					}
				}
				
				function keeprecording(){
					var microphoneForm = ice.mobi.formOf(microphoneButton);
					document.body.removeChild(popup);
					var markup = '<span class="ui-button-text">'+captureLabel+'</span>';
					microphoneButton.innerHTML = markup;
					submitRecording(audio.src);
				}

				function cancelrecording(){
					document.body.removeChild(popup);
				}

				var timerObserver;

				function startTimer() {
					var context = recDotCanvas.getContext('2d');
					context.beginPath();
					context.arc(8, 8, 8, 0, 2 * Math.PI, false);
					context.fillStyle = 'red';
					context.fill();

					timerSpan.innerHTML = '00:00';
					timer.classList.remove('mobi-hidden');
					var totalSeconds = 0, seconds, minutes;
					timerObserver = setInterval(function() {
						++totalSeconds;
						seconds = totalSeconds % 60;
						minutes = parseInt(totalSeconds / 60);
						sSeconds = seconds < 10 ? '0' + seconds : seconds;
						sMinutes = minutes < 10 ? '0' + minutes : minutes;
						timerSpan.innerHTML = sMinutes + ':' + sSeconds;
					}, 1000);
				}

				function stopTimer() {
					//timer.classList.add('mobi-hidden');
					clearInterval(timerObserver);
					var context = recDotCanvas.getContext('2d');
					context.clearRect(0, 0, 16, 16);
				}

				function doneEncoding(blob) {
					blobToDataURL(blob, function(dataURL){
						audio.src = dataURL;
					});
				}

				function submitRecording(dataURL){
					var encodedForm = '';
					var formData = new FormData();
					//TODO missing extra parameters ?
					formData.append(id, dataURLToBlob(dataURL));
					var request = new XMLHttpRequest();
					request.open("POST", postURL);
					request.setRequestHeader("JSESSIONID", sessionId);
					request.send(formData);
				}

				function blobToDataURL(blob, callback) {
					var reader = new FileReader();
					reader.onload = function(e) {callback(e.target.result);}
					reader.readAsDataURL(blob);
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
				
				ice.mobi.addListener(recordbutton, 'click', function(ev){
					togglerecording();
					ev.preventDefault();
				});
				
				ice.mobi.addListener(keepbutton, 'click', function(ev){
					keeprecording();
					ev.preventDefault();
				});

				ice.mobi.addListener(cancelbutton, 'click', function(ev){
					cancelrecording();
					ev.preventDefault();
				});
				
				function adjustAudioElementWidth() {
					if (screen && screen.width <= 640) audio.setAttribute('style', 'width: ' + (screen.width / 2) + 'px');
				}
				window.addEventListener('orientationchange', adjustAudioElementWidth);
				adjustAudioElementWidth();

				options = {
					'audio': {
						'mandatory': {
							'googEchoCancellation': 'false',
							'googAutoGainControl': 'false',
							'googNoiseSuppression': 'false',
							'googHighpassFilter': 'false'
						},
						'optional': []
						}
					};

				if (navigator.mediaDevices && navigator.mediaDevices.getUserMedia) {
					navigator.mediaDevices.getUserMedia(options).then(successCallback).catch(errorCallback);
				} else if (navigator.getUserMedia) {
					navigator.getUserMedia(options, successCallback, errorCallback);
				}
			}

			renderHTML5Microphone();
		}

		var microphoneOptions =  {
			postURL: postURL,
			cookies:{
				JSESSIONID: sessionId
			}
		};

		if ('URL' in window && (navigator.getUserMedia || (navigator.mediaDevices && navigator.mediaDevices.getUserMedia))) {
			launchHTML5Microphone();
		} else {
			renderMicrophoneFallbackFileUpload();
		}
	};

	ice.mobi.microphoneBtnOnclick.getMobileOperatingSystem = function() {
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