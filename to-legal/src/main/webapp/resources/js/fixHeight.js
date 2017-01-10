/**
 * @author henrique-rh
 * @since 06/08/2014
 * Ajusta altura do #content para que o #footer-wrapper fique colado no bottom
 */
var calculaAlturaContent = function() {
    var alturaJanela = $(window).height();
    var alturaHeader = $("header").height();
    var alturaMessages = $("#messages").height();
    alturaMessages = alturaMessages > 0 ? alturaMessages + 19 : -1;
    var alturaFooter = $("footer").height() + 21;
    var alturaSpaces = $(".space").height() * 2;
    var alturaContent = alturaJanela - alturaHeader - alturaMessages - alturaFooter - alturaSpaces;
    $("#content").css("min-height", alturaContent);
};
$(window).resize(function() {
    calculaAlturaContent();
});
calculaAlturaContent();