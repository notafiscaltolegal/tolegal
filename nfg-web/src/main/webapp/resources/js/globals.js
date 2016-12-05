/**
 * @author henrique-rh
 * @since 25/08/2014
 */
var enderecoSite;
var enderecoSitePortal;
if (typeof numeral != "undefined") {
    numeral.language('pt-br');
}
/* ===========================================================
 * jquery-loadingbar.js v1
 * ===========================================================
 * Copyright 2013 Pete Rojwongsuriya.
 * http://www.thepetedesign.com
 *
 * Add a Youtube-like loading bar
 * to all your AJAX links
 *
 * https://github.com/peachananr/loading-bar
 *
 * ========================================================== */




// Endereço site
(function() {
    var protocol = location.protocol;
    var host = location.host;
    enderecoSite = protocol+"//"+host+"/nfg-web";
    enderecoSitePortal = protocol+"//"+host+"/nfg-web/portal";
})();


(function() {
    // Pensar em um setTimeout pra deixar mais fancy!
    // Ajax error
    $(document).ajaxError(function(response, xhr) {
        trataErroDoServidor(xhr);
    }).ajaxSend(function() {
        createLoading();
    }).ajaxStart(function() {
        showLoading();
        //nfg-webMensagens.remove();
    }).ajaxSuccess(function() {
        hideLoading();
    }).ajaxComplete(function() {
        requestCompleted();
    });

})();

function ehEmail(email) {
    var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    return regex.test(email);
}




function autoheight(a) {
    if (!$(a).prop('scrollTop')) {
        do {
            var b = $(a).prop('scrollHeight');
            var h = $(a).height();
            $(a).height(h - 5);
        }
        while (b && (b != $(a).prop('scrollHeight')));
    };
    $(a).height($(a).prop('scrollHeight') + 20);
}

function ajaxBloqueioDeTela(element, url, data, type, success, error) {
    return $.ajax({
        url:url,
        data:data,
        type:type,
        beforeSend:function(){
            bloqueioDeTela(element);
        },
        success:function(response){
            success(response);
            $(element).unblock();
        },
        error:function(response){
            if(error!=null){
                error(response);
            }else{
                trataErroDoServidor(response);
            }
            $(element).unblock();
        }
    });
}

function formatDateToSave(dateStringParam)
{
    var day = dateStringParam.substring(0,2);
    var month = dateStringParam.substring(3,5) -1;
    var year = dateStringParam.substring(6,10);

    return new Date(year, month, day, 0, 0, 0, 0);
}

function retornaData35DiasAtras(){
    var date = new Date();
    var diaPrimeiro = date.setDate(1);
    var diaPrimeiroDate = new Date(diaPrimeiro);
    var dataMinima = diaPrimeiroDate.setDate(-35);
    return new Date(dataMinima);
}

function retornaMesAno(date){
    var yyyy = date.getFullYear().toString();
    var mm = (date.getMonth()).toString();
    return new Date(yyyy, mm, 1);
}

function formatDateToString(date){
    var yyyy = date.getFullYear().toString();
    var mm = (date.getMonth()+1).toString(); // getMonth() is zero-based
    var dd  = date.getDate().toString();
    var dataFormatada = (dd < 10 ? "0" + dd : dd) + "/" + (mm < 10 ? "0"+mm : mm) + "/" + yyyy;
    return dataFormatada;
}

function bloqueioDeTela(element) {
    $(element).block({
        message: '<img src="/nfg-web/images/loading.gif" width="80px;"/>'
        ,css: {
            backgroundColor: 'transparent', color: 'transparent', border: '0'
        },
        overlayCSS:  {
            backgroundColor: '#000',
            opacity:         0.1,
            cursor:          'wait'
        }
    });
}

function trataErroDoServidor(xhr) {
    if (xhr.responseJSON) {
        nfgMensagens.show(ALERT_TYPES.ERROR, xhr.responseJSON.message, xhr.responseJSON.modal);
        if(xhr.responseJSON.message.indexOf("Verificador incorreto") > 0){
            try{
                Recaptcha.reset();
            }catch (e){}
        }
    } else {
        var mensagem = "O sistema se comportou de forma inesperada - " + moment().format('DD/MM/YYYY HH:mm:ss') + ". Tente novamente após 5 minutos.";
        nfgMensagens.show(ALERT_TYPES.ERROR, mensagem);
    }
    hideLoading();
}


var removeCharacteres = function(param, char_list)
{
    var newString=param;
    _.forEach((char_list), function(char)
    {
        newString = String(newString).replace(char,"");
    });
    return newString;
};

var onlyNumbers = function(value) {
    if ($.trim(value) == "") {
        return value;
    } else {
        return value.match(/\d/g).join("");
    }
};

function isCPF(cpf) {
    var i, x, y;
    cpf = jQuery.trim(cpf);
    while(cpf.length < 11) cpf = "0"+ cpf;
    var expReg = /^0+$|^1+$|^2+$|^3+$|^4+$|^5+$|^6+$|^7+$|^8+$|^9+$/;
    var a = [];
    var b = new Number;
    var c = 11;
    for (i=0; i < 11; i++){
        a[i] = cpf.charAt(i);
        if (i < 9) b += (a[i] * --c);
    }
    if ((x = b % 11) < 2) { a[9] = 0 } else { a[9] = 11-x }
    b = 0;
    c = 11;
    for (y=0; y < 10; y++) b += (a[y] * c--);
    if ((x = b % 11) < 2) { a[10] = 0; } else { a[10] = 11-x; }
    if ((cpf.charAt(9) != a[9]) || (cpf.charAt(10) != a[10]) || cpf.match(expReg)) return false;
    return true;
}

var serializeFormToObject = function serializeObject($form) {
    var obj = {};
    var array = $form.serializeArray();

    $.each(array, function () {
        var current = obj;
        var objsInString = this.name.split(".");
        while(objsInString.length > 1){
            var nested = objsInString.shift();
            current = current[nested] = current[nested] || {};
        }
        current[objsInString.shift()] = this.value;
    });
    return obj;
};

var toCnpjFormat = function(cnpj){
    if(cnpj==null) return "";
    var element = document.createElement("input");
    $(element).css("display", "none");
    document.body.appendChild(element);
    $(element).val(cnpj);
    $(element).mask("99.999.999/9999-99");
    var masked = $(element).val();
    document.body.removeChild(element);
    return masked;
};

var toMoneyFormat = function(value){
    if(value==null) return "";

    var masked = value.toString();
    if(!(masked.indexOf(".") > -1)){
        masked+='.00';
    }else{
        var centavos = masked.substring(masked.indexOf(".")+1,masked.length);
        if(centavos.length < 2){
            masked+='0';
        }
    }
    masked = masked.replace(".",",");
    return "R$ " + masked;
};

var toCpfFormat = function(cpf){
    if(cpf==null) return "";
    var element = document.createElement("input");
    $(element).css("display", "none");
    document.body.appendChild(element);
    $(element).val(cpf);
    $(element).mask("999.999.999-99");
    var masked = $(element).val();
    document.body.removeChild(element);
    return masked;
};

var toTelFormat = function(tel){
    if(tel==null) return "";
    var element = document.createElement("input");
    $(element).css("display", "none");
    document.body.appendChild(element);
    $(element).val(tel);
    $(element).mask("(99) 9999-9999?9");
    var masked = $(element).val();
    document.body.removeChild(element);
    return masked;
};

var toCepFormat = function(cep){
    if(cep==null) return "";
    var element = document.createElement("input");
    $(element).css("display", "none");
    document.body.appendChild(element);
    $(element).val(cep);
    $(element).mask("99999-999");
    var masked = $(element).val();
    document.body.removeChild(element);
    return masked;
};


var toDateSlashesFormat = function(date){
    var aux = date.split("/");

    var ano = aux[2];
    var mes = aux[1];
    var dia = aux[0];
    dia++;

    var timezone = "T"

    return new Date(ano+"-"+mes+"-"+dia+timezone+"00:00:00");
};

var requestCompleted = function() {
    $("#loadingbar").width("101%").delay(200).fadeOut(400, function() {
        $(this).remove();
    });
};

var appendingLoading = function(container) {
    var loading = "<div class='spinner'><div class='bounce1'></div><div class='bounce2'></div><div class='bounce3'></div></div>";
    container.append(loading);
};
var removeLoading = function(container) {
    container.find(".spinner").remove();
};

var createLoading = function() {
    if ($("#loadingbar").length === 0) {
        $("body").append("<div id='loadingbar'></div>")
        $("#loadingbar").addClass("waiting").append($("<dt/><dd/>"));
        $("#loadingbar").width((50 + Math.random() * 30) + "%");
    }
};

var showLoading = function() {
    //$(".loading").show();
};

var hideLoading = function() {
    //$(".loading").hide();
};

var isIE = function () {
    var ua = window.navigator.userAgent;
    var msie = ua.indexOf("MSIE ");

    return msie > 0;
};