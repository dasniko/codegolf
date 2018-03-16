$(document).ready(function() {
  $('.dropdown-button').dropdown({hover: false});
  $('.button-collapse').sideNav();
});

var lockScreen = function() {
  $('#lock').toggleClass('hide');
};

var loadSourcecode = function(username, e) {
  if (!$(e).hasClass('active')) {
    $.get('/results/sourcecode', {username: username}, function(data) {
      $(e).next().children().text(data);
    }).fail(function(ex) {
      $(e).next().children().addClass('red-text');
      $(e).next().children().text(ex.responseJSON.message);
    });
  }
};
