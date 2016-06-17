$(document).ready(function() {

	//*** js Riccardo ***//

	//
	var header = $("#button-wrapper");
    var button = $(".collapse-custom + .button-container");
	$(window).scroll(function() {    
	var scroll = $(window).scrollTop();
		if (scroll >= 20) {
			header.addClass("fix-button");
			button.addClass("collapse-custom-hide");
			$(".fixedHeader-floating").addClass("show");
			$(".collapse-custom , .back-top").removeClass("hide");

		} else {
			header.removeClass("fix-button");
			button.removeClass("collapse-custom-hide").removeClass("collapse-custom-show");
			$(".collapse-custom , .back-top").addClass("hide");
			$(".fixedHeader-floating").removeClass("show");
		}
	});

	//hide and show in mobile the option bar
	$(".collapse-custom").on("click tap",function(){
		$(".collapse-custom + .button-container").toggleClass("collapse-custom-show , collapse-custom-hide");
	});

	//back to top
	$(".back-top").on('click', function(event){
		event.preventDefault();
		$('body,html').animate({
			scrollTop: 0 ,
		 	}, 700
		);
	});

	//inizialize datatable
	$("#v2").DataTable({
		"paging":         false,
		fixedHeader: {
        header: false,
        footer: true
    	}
	});

	//perfect-scrollbar
	$(".table-custom").on("scroll onscroll",function() {
		var fixedElement=$(".table-custom").scrollLeft();
		$(".fixedHeader-floating").css("left",-fixedElement+20);
	});
	function changeSize() {
	    var width = $("#v2").val();
	    var height = $("#v2").val();

	    $(".table-custom").width(width).height(height);

	    // update scrollbars
	    $(window).perfectScrollbar('update');
	}
	var is_xs = window.innerWidth <= 768;
	if (!is_xs) {
	 	$(function() {
    		$('.table-custom').perfectScrollbar();
		});
	}

	// includere js sistemato
});


