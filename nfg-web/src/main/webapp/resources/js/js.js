// JavaScript Document
$(document).ready(function()
{
	$(window).load(function()
	{
	
		$('.menu-hide').click(function()
		{
			if($('header .menu').hasClass('menu-open'))
			{
				$('header .menu').removeClass('menu-open');
			}
			else
			{
				$('header .menu').addClass('menu-open');	
			}
		});
	});
});