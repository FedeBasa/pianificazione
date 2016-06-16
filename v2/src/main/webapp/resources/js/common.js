$(document).ready(function(){
    $(".navbar-toggle").on("click", function () {
		$(this).toggleClass("active");
	});
	$('#file').change(function(){
		$('#subfile').val($(this).val());
	});
	$('.dropdown').on('show.bs.dropdown', function(e){
	  $(this).find('.dropdown-menu').first().stop(true, true).slideDown();
	});

	$('.dropdown').on('hide.bs.dropdown', function(e){
	  $(this).find('.dropdown-menu').first().stop(true, true).slideUp();
	});
});
