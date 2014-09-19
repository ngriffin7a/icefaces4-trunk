function restoreURL(e) {
    if (e.state) {
        location.href = e.state.src;
    }
}

function backButton() {
    if( window.history && window.history.pushState ){
        if (window.addEventListener) {
            window.addEventListener("popstate", restoreURL);
        } else {
            window.attachEvent("popstate", restoreURL);
        }
    }
}

if (window.addEventListener) {
    window.addEventListener('load', backButton, true);
} else {
    window.attachEvent('onload', backButton);
}

function updateAddressBarURL(grp, exp){
    var hypertextReference = '//' + location.host + location.pathname + '?grp=' + grp + '&exp=' + exp;
    if( window.history && window.history.pushState ){ //check if browser supports HTML5 history pushState
        history.pushState({ src: location.href }, null, hypertextReference); //set the current url, pass in a null state object
    }
}