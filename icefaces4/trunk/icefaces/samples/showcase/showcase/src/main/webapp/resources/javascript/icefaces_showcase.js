var backButton = function() {
    if( window.history && window.history.pushState ){
        if (window.addEventListener) {
            window.addEventListener("popstate", function(e) {
                    location.reload();
            });
        } else {
            window.attachEvent("popstate", function(e) {
                    location.reload();
            });
        }
    }
};

if (window.addEventListener) {
    window.addEventListener('load', backButton, true);
} else {
    window.attachEvent('onload', backButton);
}

function updateAddressBarURL(grp, exp){
    var hypertextReference = '//' + location.host + location.pathname + '?grp=' + grp + '&exp=' + exp;
    if( window.history && window.history.pushState ){ //check if browser supports HTML5 history pushState
        history.pushState({ src: hypertextReference }, null, hypertextReference); //set the current url, pass in a null state object
    }
}