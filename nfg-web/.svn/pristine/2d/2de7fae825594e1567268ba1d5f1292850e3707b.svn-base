/**
 * Created by lucas-mp on 10/10/14.
 */

function FrameCidadao() {
    var me = this;

    $(".inputCpf").mask("999.999.999-99");
    $(".inputCep").mask("99999-999");
    $(".inputTelefone").mask("(99) 9999-9999?9");

    $(".textCpf").html(toCpfFormat($(".textCpf").html()));
    $(".textTelefone").html(toTelFormat($(".textTelefone").html()));
    $(".textCep").html(toCepFormat($(".textCep").html()));


    me.linkCadastrar();
    me.linkCertificado();
    me.linkLogout();
    me.loginCidadaoSite();
    me.loginSubmitTeste();



    $("#formLoginCidadao[data-site='novo-site']").onkeydown=function(e){
        var evt = e || window.event;
        if(evt.keyCode=='13'){
            $(this).submit();
        }
    }

    document.onkeydown=function(e){
        var evt = e || window.event;
        if(evt.keyCode=='13'){
            $(".btn-submitform").click();
        }
    }
}


FrameCidadao.prototype.linkLogout = function(){
    $("#linkLogout").click(function(){
        window.parent.location.href = "efetuarlogoutSite";
    });
}

FrameCidadao.prototype.linkCadastrar = function(){
    $("#linkCadastrar").click(function(){
        window.parent.location.href = "cadastrar";
    });
}

FrameCidadao.prototype.loginCidadaoSite = function(){

    $("#linkRecuperarSenhaSite").click(function(e) {
        window.parent.location.href = "esqueciSenha";
    });
}

FrameCidadao.prototype.linkCertificado = function(){
    $("#linkCertificado").click(function(){
        window.parent.location.href = "certificado";
    });
}

FrameCidadao.prototype.loginSubmitTeste = function( ){
    var me = this;
    $("#loginSubmitTeste").click(function(e) {
        var challenge = null;
        var captchaResponse = null;

        if  ($("#recaptcha_challenge_field").val()!=undefined){
            challenge = $("#recaptcha_challenge_field").val();
        }

        if  ($("#recaptcha_response_field").val()!=undefined){
            captchaResponse = $("#recaptcha_response_field").val();
        }

        $.ajax({
            url: enderecoSite + "/cidadao/efetuarloginSite",
            data:{
                cpf: me.removeMascaraCpf($("#loginCpf").val()),
                senha: $("#loginSenha").val(),
                challenge: challenge,
                captchaResponse: captchaResponse
            },
            type: 'POST',
            success: function(response) {
                if (response.loginInvalido!=null){
                    window.parent.location.replace(enderecoSite+response.urlRedirect);

                    try{
                        Recaptcha.reset();
                    }catch (e){}
                    if(response.ativarCaptchaLogin){
                        me.ativarCaptcha("#captchaLogin","containerCaptchaLogin");
                    }


                }else{
                    try{
                        Recaptcha.destroy();
                    }catch (e){}
                    window.parent.location.replace(enderecoSite+response.urlRedirect);
                }
            }
        });
    });
};


FrameCidadao.prototype.removeMascaraCpf = function(cpf)
{
    return removeCharacteres(cpf,['_','.','-','.']);
};


