/**
 * @author henrique-rh
 * @since 21/07/2014
 */
ALERT_TYPES = {ERROR: "alert-danger", WARN: "alert-warning", INFO: "alert-info", SUCCESS: "alert-success"};

function NFGMensagens() {
    var source   = $("#message").html();
    this.templateMessage = Handlebars.compile(source);
}

NFGMensagens.prototype.show = function(type, msg, modal) {
    var data = {type:type, message:msg};
    var html = this.templateMessage(data);
    if (modal != null && modal == true){
        $("#messagesContainerModal").html("").append(html);
    }else{
        $("#messagesContainer").html("").append(html);
    }
    calculaAlturaContent();
    $(".alert-dismissible").on("closed.bs.alert", function() {
        calculaAlturaContent();
    });
};
NFGMensagens.prototype.remove = function() {
    $("#messagesContainer").html("");
    calculaAlturaContent();
};

var nfgMensagens = new NFGMensagens();