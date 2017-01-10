/**
 * Created by lucas-mp on 10/10/14.
 */

function Batch(job) {
    var me = this;
    me.geracaoDeBilhetes();
    me.geracaoDePontuacao();
    me.alteraSorteio();
    me.alteraRegraPontuacao();
    $(".datepicker").each(function() {
        me.initDatePicker($(this));
    });
    switch (job) {
        case 'pontuacao':
            me.loadStatusPontuacao();
            break;
        case 'bilhetes':
            me.loadStatusBilhetes();
    }
    moment.locale('pt-BR');
}

Batch.prototype.setStatus = function (status) {
    $("#status").text(status);
};

Batch.prototype.loadStatusPontuacao = function () {
    var me = this;
    $.ajax({
        url: enderecoSite + "/admin/status/pontuacao",
        success: function (response) {
            me.setStatus(response.status);
        }
    })
};

Batch.prototype.loadStatusBilhetes = function () {
    var me = this;
    $.ajax({
        url: enderecoSite + "/admin/status/bilhetes",
        success: function (response) {
            me.setStatus(response.status);
        }
    })
};

Batch.prototype.initDatePicker = function($campo) {
    if (isIE()) {
        $campo.on('keydown', function() {
            return false;
        });
    }
    $campo.datetimepicker({
        formatDate: 'd-m-Y',
        format: 'd/m/Y',
        lang: 'pt',
        timepicker: false,
        allowBlank: true,
        maxDate: 0,
        mask: false,
        closeOnDateSelect: true,
        yearStart:1900,
        validateOnBlur: false
    });

    $(".datepickerIcon").click(function() {
        $campo.datetimepicker('show');
    }).css("top","0").css("cursor","pointer");
};

Batch.prototype.geracaoDePontuacao = function() {
    var me = this;
    $("#btnGerarPontuacao").click(function(e) {
        var password = $("#password").val();
        var idRegra = $("#selectRegraPontuacao").val();
        var dataLimitePontuacao = $("#dataLimitePontuacao").val();
        $(".form-group").removeClass('has-error');
        if (idRegra == 'Selecione') {
            $("#form-regra").addClass('has-error');
            return false;
        }
        if (dataLimitePontuacao == '') {
            $("#form-data-limite").addClass('has-error');
            return false;
        }
        if (password == '') {
            $("#form-password").addClass('has-error');
            return false;
        }
        ajaxBloqueioDeTela('#btnGerarPontuacao',
            enderecoSite + "/admin/chamarBatchParaGerarPontuacao",
            {
                idRegra: idRegra,
                dataLimitePontuacao: moment(dataLimitePontuacao, "DD/MM/YYYY").format('MM/DD/YYYY'),
                password: password
            },
            'POST',
            function(response){
                nfgMensagens.show(ALERT_TYPES.SUCCESS, response.message);
                me.setStatus("(STARTED)");
            }
        );
    });
};

Batch.prototype.geracaoDeBilhetes = function() {
    var me = this;
    $("#btnGerarBilhetes").click(function(e) {
        var password = $("#password").val();
        if (password == '') {
            $("#form-password").addClass('has-error');
            return false;
        }
        ajaxBloqueioDeTela('#btnGerarBilhetes',
            enderecoSite + "/admin/chamarBatchParaGerarBilhetes",
            {idRegraSorteio:$("#selectSorteios").val(), password: password},
            'POST',
            function(response){
                nfgMensagens.show(ALERT_TYPES.SUCCESS, response.message);
                me.setStatus("(STARTED)");
            }
        );
    });
}


Batch.prototype.alteraRegraPontuacao = function(){
    var me= this;
    $("#selectRegraPontuacao").on("change", function(e) {
        if($("#selectRegraPontuacao").val()!='Selecione'){
            $("#dadosRegraSelecionada").css('display','block');
            me.carregaDadosRegra($("#selectRegraPontuacao").val());
        }else{
            $("#dadosRegraSelecionada").css('display','none');
        }
    });
}

Batch.prototype.alteraSorteio = function(){
    var me= this;
    $("#selectSorteios").on("change", function(e) {
        if($("#selectSorteios").val()!='Selecione'){
            $("#dadosSorteioSelecionado").css('display','block');
            me.carregaDadosSorteio($("#selectSorteios").val());
        }else{
            $("#dadosSorteioSelecionado").css('display','none');
        }
    });
}

Batch.prototype.carregaDadosRegra = function(regraId){
    if(regraId!=null){//dadosRegraSelecionada
        ajaxBloqueioDeTela("#dadosRegraSelecionada",
            enderecoSite+"/admin/carregaDadosDaRegraPontuacao",
            {idRegra:regraId},
            'POST',function(response){
                $("#numrMaximoPontoDocumento").val(response.regraPontuacao.numrMaximoPontoDocumento);
                $("#valorFatorConversao").val(response.regraPontuacao.valorFatorConversao);
                $("#numrMaximoPontoRef").val(response.regraPontuacao.numrMaximoPontoRef);
                $("#numrMaximoEstabRef").val(response.regraPontuacao.numrMaximoEstabRef);
                $("#numrMaximoDocRef").val(response.regraPontuacao.numrMaximoDocRef);
                $("#dataInicioRegra").val(response.regraPontuacao.dataInicioRegraStr);
                $("#dataFimRegra").val(response.regraPontuacao.dataFimRegraStr);

            });
    }
}

Batch.prototype.carregaDadosSorteio = function(idSorteio){
    if(idSorteio!=null){
        ajaxBloqueioDeTela("#dadosSorteioSelecionado",
            enderecoSite+"/admin/carregaDadosDoSorteioParaUsuario",
            {idSorteio:idSorteio},
            'POST',
            function(response){
                $("#dtSorteio").val(response.sorteio.dataRealizacaoStr);
                $("#textDtLoteria").val(response.sorteio.dataExtracaoLoteriaStr);

                $("#dataLimiteCadastroPessoa").val(response.sorteio.dataLimiteCadastroPessoaStr);
                $("#numrMaximoPontosDocsFiscais").val(response.sorteio.numeroMaxDocFisc);
                $("#fatorConversaoPontoBilhete").val(response.sorteio.numeroConversao);

                $("#textSorteioRealizado").val(response.sorteio.realizadoBoolean? "Sim":"Não");

                $("#statusSorteio").val(response.sorteio.statusStr);

                $("#textNumeroLoteria").val(response.sorteio.numeroLoteria);
                $("#textFimDataRegra").val(response.dataFimRegra);

                if(response.sorteio.status == "1" || response.sorteio.status == "2" ){
                    $("#linkDetalhamentoBilhetes").prop("disabled", true);
                    $("#linkDetalhamentoPontos").prop("disabled", true);
                }else{
                    $("#linkDetalhamentoBilhetes").prop("disabled", false);
                    $("#linkDetalhamentoPontos").prop("disabled", false);
                }
            }
        );
        return true;
    }else{
        return false;
    }
}