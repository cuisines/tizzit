$(document).ready(function() {

	$("a#single_1").fancybox();
		
	$("a#single_2").fancybox({
		'zoomOpacity'			: true,
		'overlayShow'			: false,
		'zoomSpeedIn'			: 500,
		'zoomSpeedOut'			: 500
	});
	
	$("a#single_3").fancybox({
		'overlayShow'			: false,
		'zoomSpeedIn'			: 600,
		'zoomSpeedOut'			: 500,
		'easingIn'				: 'easeOutBack',
		'easingOut'				: 'easeInBack'
	});
	
	$("a.group").fancybox({
		'hideOnContentClick': false
	});

});