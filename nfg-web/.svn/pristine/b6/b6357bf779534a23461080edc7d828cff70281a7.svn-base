/**
 * Created by henrique-rh on 30/01/15.
 */
function PreCidadao() {
    this.applyMasks();
    this.applyValidatiion();
    this.signSubmit();
    //this.signOpenTermoAcordo();
}

//PreCidadao.prototype.signOpenTermoAcordo = function() {
//    $('.termoAcordo').on('click', function() {
//        var textoAcordo = "" +
//            "<p class='text-justify'>&#8226; O pré-cadastro realizado será homologado pela Secretaria da Fazenda através de suas bases de dados e as da Receita Federal;</p>" +
//            "<p class='text-justify' style='margin-top: 10px !important;'>&#8226; O cidadão deverá aguardar o recebimento de e-mail com a confirmação do pré-cadastro contendo senha para acesso à sua área restrita no site do Nota Fiscal Goiana;</p>" +
//            "<p class='text-justify' style='margin-top: 10px !important;'>&#8226; Depois do recebimento de e-mail com a validação do pré-cadastro, o cidadão deverá acessar o site do programa e, no menu “Cidadão”, informar seu CPF e a senha enviada anteriormente para o seu e-mail. Após realizado o login, deverá complementar as informações cadastrais solicitadas para ter acesso completo às funcionalidades do programa (Perfil, Consulta Notas, Placar, Pontuação, Bilhetes, Sorteio e Resgate de Prêmios);</p>"+
//            "<p class='text-justify' style='margin-top: 10px !important;'>Nota 1: O cidadão somente estará apto a concorrer aos prêmios se os dados informados forem validados.</p>"+
//            "<p class='text-justify' style='margin-top: 10px !important;'>Nota 2: A inserção das notas fiscais emitidas com CPF serão efetivadas diretamente pelas empresas. </p>";
//        var buttons = "<button class='btn btn-primary' style='margin-right: 50px' data-dismiss='modal'' aria-label='Fechar'>Fechar</button>";
//        var modal = new GenericModal("Termo Acordo Cidadão", textoAcordo, buttons);
//        modal.open();
//    });
//};

PreCidadao.prototype.isValid = function(data) {
    var dataNascimento = $(".datepicker").val();
    if (!dataNascimento || dataNascimento == "__/__/____") {
        nfgMensagens.show(ALERT_TYPES.ERROR, "Favor preencher o campo data de nascimento.");
        return false;
    }
    if (!isCPF(onlyNumbers(data.cpf))) {
        nfgMensagens.show(ALERT_TYPES.ERROR, 'Favor digitar um CPF válido.');
        return false;
    }
    return true;
};

PreCidadao.prototype.signSubmit = function() {
    var me = this;
    $('#formPreCadastro').on('submit', function(e) {
        var data = serializeFormToObject($(this));

        if (!me.isValid(data)) {
            e.stopImmediatePropagation();
            e.preventDefault();
            return false;
        } else {
            return true;
        }
    });
};

PreCidadao.prototype.applyMasks = function() {
    this.initDatePicker($(".datepicker"));
    $('.phone').mask("(99) 9999-9999?9");
    $('.cpf').mask("999.999.999-99");
};

PreCidadao.prototype.applyValidatiion = function() {
    $('.cpf').on('blur', function() {
        if (onlyNumbers($(this).val()) != "" && !isCPF(onlyNumbers($(this).val()))) {
            nfgMensagens.show(ALERT_TYPES.ERROR, 'Favor digitar um CPF válido.');
            $(this).val('');
        } else {
            nfgMensagens.remove();
        }
    })
};

PreCidadao.prototype.initDatePicker = function($campo) {
    if (isIE()) {
        $campo.on('keydown', function() {
            return false;
        });
    }
    $campo.datetimepicker({
        formatDate: 'd/m/Y',
        format: 'd/m/Y',
        lang: 'pt',
        timepicker: false,
        allowBlank: true,
        maxDate: 0,
        mask: true,
        closeOnDateSelect: true,
        yearStart:1900,
        validateOnBlur: false
    });
    $campo.parents(".input-group").find(".datepickerIcon").click(function() {
        $campo.datetimepicker('show');
    }).css("top","0").css("cursor","pointer");
};
