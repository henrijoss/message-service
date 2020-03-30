$(document).ready(domReady);
function domReady() {
    locateMe();
    $('input').powerTip();
    $('.input-message').keyup(function() {
        $('.input-message-count').html("Zeichenanzahl: " + $(this).val().length + "/500");
    });
}

function locateMe() {
    if (navigator.geolocation) {
        window.navigator.geolocation.getCurrentPosition(geolocationSuccess);
    } else {
        $('.city').html("Standort nicht verfügbar");
    }
}

function geolocationSuccess(position) {
    var coords = position.coords;
    var url = 'https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=' + coords.latitude + '&lon=' + coords.longitude;
    fetch(url).then(function (response) {
        return response.json();
    }).then(function (json) {
        $('.city').html(json.address.city);
    });
}


