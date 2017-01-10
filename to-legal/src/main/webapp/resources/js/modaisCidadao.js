


/**
 * Created by lucas-mp on 10/10/14.
 */

function ModaisCidadao(options) {
    var me = this;
    this.cidadao = options.cidadao;

    if (options.nomeTela == 'conclusaoDadosPerfil'){
        me.inicializaEnderecoAtualizaPerfil();
    }

    me.modalAtualizarPerfil();
    me.modalNovaReclamacao();
    me.modalAlterarSenha();
    me.modalMinhasNotasEmDetalhe();
    me.modalDetalhamentoBilehtes();
    me.modalDetalhamentoPontos();
    me.trocaModalPontuacaoParaBilhetes();
    me.trocaModalBilhetesParaPontuacao();
    me.fecharPopup();
    me.modalResgatar();
    me.buscarAgencias();
    me.buscarNomeDaAgencia();
    me.gravarContaBancaria();
    me.selecionaTipoResgate();
    me.selecionaBanco();
    me.calculaValoresResgate();
    me.eventoReclamacaoDetalhe();
    
    $(".datepicker").each(function() {
        me.initDatePicker($(this));
    });

    $("#modalHome").on('hidden.bs.modal', function () {
        $("#messagesContainerModal").html("");
    })

    $( "#formPerfilCidadao" ).tooltip({
        position: {
            my: "left top",
            at: "right+5 top-5"
        }
    }).off("focusin focusout");
}

ModaisCidadao.prototype.initDatePicker = function($campo) {
    if (isIE()) {
        $campo.on('keydown', function() {
            return false;
        });
    }
    $campo.datetimepicker({
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

    $(".datepickerIcon").click(function() {
        $campo.datetimepicker('show');
    }).css("top","0").css("cursor","pointer");
};

ModaisCidadao.prototype.modalNovaReclamacao  = function() {
    var me = this;
    $("#btnNovaReclamacao").click(function (e) {
        $("#modalHomeBody").load('novaReclamacao',
            function (response, status, xhr) {
                if (status == "error") {
                    window.location.replace(enderecoSite + "/cidadao/login");
                }
            }
        );
        $("#modalHomeTitle").html('Cadastrar Nova Reclamaç&#225;o');
        $(".modal-dialog").attr("class", "modal-dialog modal-lg");

        bloqueioDeTela("#divPaineisTelaInicial");

        setTimeout(function () {
            $("#modalHome").modal('show');
            $(":file").filestyle({buttonText: "Procurar arquivo"});
            $("#divPaineisTelaInicial").unblock();
        }, 2000);

        //setTimeout(function () {
        //$("#emailCidadaoPerfil").focus();
        //}, 3500);
    });
}

ModaisCidadao.prototype.modalDetalhamentoPontos = function() {
    var me = this;
    $("#linkDetalhamentoPontos").off("click");
    $("#linkDetalhamentoPontos").click(function (e) {
        me.carregaTelaPontos();
    });
}



ModaisCidadao.prototype.fecharPopup = function () {
    $("body").on('click', '#linkSair',function (e) {
        $("#modalHome").modal('hide');
    });
}

ModaisCidadao.prototype.trocaModalBilhetesParaPontuacao = function() {
    var me = this;
    $("body").off("click", '#linkBilhetesParaPontuacao');
    $("body").on('click', '#linkBilhetesParaPontuacao',function (e) {
        $("#modalHome").modal('hide');
        me.carregaTelaPontos();
    });
}

ModaisCidadao.prototype.modalDetalhamentoBilehtes = function() {
    var me = this;
    $("#linkDetalhamentoBilhetes").off('click');
    $("#linkDetalhamentoBilhetes").click(function (e) {
        me.carregaTelaBilhetes();
    });
}

ModaisCidadao.prototype.trocaModalPontuacaoParaBilhetes = function() {
    var me = this;
    $("#linkPontuacaoParaBilhetes").off('click');
    $("#linkPontuacaoParaBilhetes").click(function (e) {
        $("#modalHome").modal('hide');
        me.carregaTelaBilhetes();
    });
}


ModaisCidadao.prototype.carregaTelaPontos = function() {
    var me = this;
    var idSorteio = $("#selectSorteios").val();

    $("#modalHomeTitle").html('Minha Pontua&ccedil;&atilde;o');
    $(".modal-dialog").attr("class", "modal-dialog modal-lg");

    $("#modalHomeBody").load('detalhePontos/'+idSorteio,
        function (response, status, xhr) {
            if (status == "error") {
                window.location.replace(enderecoSite + "/cidadao/login");
            }
        }
    );

    bloqueioDeTela("#divPaineisTelaInicial");

    //timeout para esperar o load de /detalhePontos
    setTimeout(function () {
        me.idSorteio = idSorteio;
        $("#modalHome").modal('show');
        $("#divPaineisTelaInicial").unblock();
    },2000);

    //timeouts para sincronizar "carregaPaginacaoPontuacao" dos grids logo após o show do modal
    setTimeout(function () {
        me.carregaPaginacaoPontuacao(me,'PontosDasNotas',".panelPontosDasNotas");
    },2500);

    setTimeout(function () {
        me.carregaPaginacaoPontuacao(me,'PontosBonus',".panelPontosBonus");
    },2500);

}

ModaisCidadao.prototype.carregaTelaBilhetes = function() {
    var me = this;
    var idSorteio = $("#selectSorteios").val();

    $("#modalHomeTitle").html('Meus Bilhetes');
    $(".modal-dialog").attr("class", "modal-dialog modal-lg");

    $("#modalHomeBody").load('detalheBilhetes/'+idSorteio,
        function (response, status, xhr) {
            if (status == "error") {
                window.location.replace(enderecoSite + "/cidadao/login");
            }
        }
    );

    bloqueioDeTela("#divPaineisTelaInicial");

    //timeout para esperar o load de /detalhePontos
    setTimeout(function () {
        me.idSorteio = idSorteio;
        $("#modalHome").modal('show');
        $("#divPaineisTelaInicial").unblock();
    },2000);

    //timeouts para sincronizar "carregaPaginacaoPontuacao" dos grids logo após o show do modal
    setTimeout(function () {
        me.carregaPaginacaoBilhetes(me, 'Bilhetes',".panelBilhetes");
    },2500);

}

ModaisCidadao.prototype.buscarNomeDaAgencia = function(){
    var me = this;
    var nomeDaAgencia;

    $("body").on("blur", "#numeroAgencia", function(e) {
        var idBanco = $('#selectBanco').val();
        if(idBanco!='Selecione'){
            var numeroAgencia = $('#numeroAgencia').val();
            var url = enderecoSite + "/cidadao/carregaNomeAgencia";
            $.get(url, {idBanco: idBanco, numeroAgencia: numeroAgencia}, function(dataReturn) {
                nomeDaAgencia = dataReturn.nomeAgencia;
                $('#nomeAgencia').html(nomeDaAgencia);
            });
        }else{
            nfgMensagens.show(ALERT_TYPES.ERROR, "Informe o Banco",true);
        }
    });
}

ModaisCidadao.prototype.buscarAgencias = function(){
    var me = this;
    var listaAgencias;

    $("body").on('change', '#selectBanco', function(e) {
        var idBanco = $('#selectBanco').val();
        var url = enderecoSite + "/cidadao/carregaAgencias";
        $.get(url, {id: idBanco}, function(dataReturn) {
            listaAgencias = dataReturn.agenciasBancaria;
        });

    });
}

ModaisCidadao.prototype.modalResgatar = function(){
    var me = this;

    $("body").on('click', '#linkResgatar',function (e) {

        var idPremioBilhete = $(this).parent().parent().find('.idPremioBilhete').html().trim();
        var sorteioPremio = $(this).parent().parent().find('.sorteioPremio').html().trim();
        var numeroBilhetePremio = $(this).parent().parent().find('.numeroBilhetePremio').html().trim();
        var valorPremio = $(this).parent().parent().find('.valorPremio').html().trim();

        var vlrTributoGravado = $(this).parent().parent().find('.vlrTributoPremioBilhete').html().trim();
        var vlrTaxasGravado = $(this).parent().parent().find('.vlrTaxasPremioBilhete').html().trim();
        var tipoResgatePremioBilhete = $(this).parent().parent().find('.tipoResgatePremioBilhete').html().trim();

        var codgBanco = $(this).parent().parent().find('.codgBanco').html().trim();
        var codigoAgencia = $(this).parent().parent().find('.codigoAgencia').html().trim();
        var numeroConta = $(this).parent().parent().find('.numeroConta').html().trim();
        var tipoConta = $(this).parent().parent().find('.tipoConta').html().trim();
        var digito = $(this).parent().parent().find('.digito').html().trim();


        var dataResgatePremioBilhete = $(this).parent().parent().find('.dataResgatePremioBilhete').html().trim();
        var dataLimitePremioBilhete = $(this).parent().parent().find('.dataLimitePremioBilhete').html().trim();
        var dataSolicitacaoPremioBilhete = $(this).parent().parent().find('.dataSolicitacaoPremioBilhete').html().trim();


        var dataLimiteDate = formatDateToSave(dataLimitePremioBilhete);
        var dataResgateDate = formatDateToSave(dataResgatePremioBilhete);
        var dataHoje = Date.now();


        var valorPremioCalc = $(this).parent().parent().find('.valorPremioCalc').html().trim();
        var vlrTributoParam = $(this).parent().parent().find('.vlrTributoParam').html().trim();
        var valorTaxasChequeParam = $(this).parent().parent().find('.valorTaxasChequeParam').html().trim();
        var valorTaxasTransfParam = $(this).parent().parent().find('.valorTaxasTransfParam').html().trim();
        var vlrPremioLiquido;


        $("#modalHomeBody").load('resgatar',
            function (response, status, xhr) {
                if (status == "error") {
                    window.location.replace(enderecoSite + "/cidadao/login");
                }
            }
        );
        $("#modalHomeTitle").html('Incluir dados do resgate');
        $(".modal-dialog").attr("class", "modal-dialog modal-md");
        bloqueioDeTela("#divPaineisTelaInicial");

        setTimeout(function () {
            $("#modalHome").modal('show');

            me.inicializaDadosBancarios(me);
            $("#nrSorteio").html(sorteioPremio);
            $("#nrBilhete").html(numeroBilhetePremio);
            $("#valorPremio").html(valorPremio);

            $("#dataLimiteResgatePremio").html(dataLimitePremioBilhete);

            if(tipoResgatePremioBilhete.length>0){
                $("#selectTipoResgate").val(tipoResgatePremioBilhete);
            }

            if(dataResgatePremioBilhete!=undefined && dataResgatePremioBilhete.length>0){
                $("#dataResgatePremio").html(dataResgatePremioBilhete);
                $("#gravarResgate").css('display','none');
                $("#lblBotaoInativo").html("N&#225;o é possível gravar solicitaç&#225;o, pois o resgate já foi efetuado.");
                $("#lblBotaoInativo").css('display','inline');
            }else {
                if( dataLimitePremioBilhete.length > 0 && dataLimiteDate!=undefined && dataHoje > dataLimiteDate){
                    $("#gravarResgate").css('display','none');
                    $("#lblBotaoInativo").html("N&#225;o é possível gravar solicitaç&#225;o, pois a data limite para resgate já foi ultrapassada.");
                    $("#lblBotaoInativo").css('display','inline');
                }else{
                    $("#lblBotaoInativo").css('display','none');
                    $("#gravarResgate").css('display','inline');
                }

                $("#dataResgatePremio").html('Resgate ainda n&#225;o efetuado.');
            }


            if(tipoResgatePremioBilhete=="C"){
                $("#panelTransf").css('display','none');
                $("#panelCheque").css('display','inline');
            }else  {
                $("#panelCheque").css('display','none');
                $("#panelTransf").css('display','inline');
            }


            $("#dataResgatePremioBilhete").val(dataResgatePremioBilhete);
            $("#vlrTributoGravado").val(vlrTributoGravado);
            $("#vlrTaxasGravado").val(vlrTaxasGravado);
            $("#tipoResgatePremioBilhete").val(tipoResgatePremioBilhete);
            $("#valorPremioCalc").val(valorPremioCalc);
            $("#vlrTributoParam").val(vlrTributoParam);
            $("#valorTaxasChequeParam").val(valorTaxasChequeParam);
            $("#valorTaxasTransfParam").val(valorTaxasTransfParam);
            $("#idPremioBilhete").val(idPremioBilhete);

            $("#divPaineisTelaInicial").unblock();
        }, 5000);

        setTimeout(function () {
            if(codgBanco!=null && codgBanco.length>0){
                $("#selectBanco").val(codgBanco);
                $("#numeroAgencia").val(codigoAgencia);
                $("#contaBancaria").val(numeroConta);
                $("#selectConta").val(tipoConta);
                $("#digito").val(digito);
                me.calculaValoresResgate(tipoResgatePremioBilhete,dataResgatePremioBilhete,
                    valorPremioCalc,vlrTributoParam,valorTaxasChequeParam,valorTaxasTransfParam,vlrTributoGravado,vlrTaxasGravado);

                //$("#vlrTaxasPremioBilhete").html(toMoneyFormat($("#vlrTaxasPremioBilhete").html()));
                //$("#vlrTributoPremioBilhete").html(toMoneyFormat($("#vlrTributoPremioBilhete").html()));
                //$("#vlrPremioLiquido").html(toMoneyFormat($("#vlrPremioLiquido").html()));
            }
        }, 6000);


    });
}


ModaisCidadao.prototype.calculaValoresResgate = function(tipoResgate,dataResgate,valorPremioCalc,vlrTributoParam,valorTaxasChequeParam,valorTaxasTransfParam,
                                                         valorTributoGravado,valorTaxasGravado){
    var vlrPremioLiquido;
    if(dataResgate!=null && dataResgate.length>0 ){
        $("#vlrTaxasPremioBilhete").html(toMoneyFormat(valorTaxasGravado));
        $("#vlrTributoPremioBilhete").html(toMoneyFormat(valorTributoGravado));
        vlrPremioLiquido=parseFloat(valorPremioCalc)-(parseFloat(valorTaxasGravado)+parseFloat(valorTributoGravado))

    }else{
        $("#vlrTributoPremioBilhete").html(toMoneyFormat(vlrTributoParam));
        if (tipoResgate=="C"){
            $("#vlrTaxasPremioBilhete").html(toMoneyFormat(valorTaxasChequeParam));
            vlrPremioLiquido=parseFloat(valorPremioCalc)-(parseFloat(valorTaxasChequeParam)+parseFloat(vlrTributoParam))
        }else{
            var bancoSelecionado = $("#selectBanco").val();
            if(bancoSelecionado!=undefined && bancoSelecionado!= null && $("#selectBanco").val() == 104){
                $("#vlrTaxasPremioBilhete").html(toMoneyFormat(0.0));
                vlrPremioLiquido=parseFloat(valorPremioCalc)-parseFloat(vlrTributoParam)
            }else{
                $("#vlrTaxasPremioBilhete").html(toMoneyFormat(valorTaxasTransfParam));
                vlrPremioLiquido=parseFloat(valorPremioCalc)-(parseFloat(valorTaxasTransfParam)+parseFloat(vlrTributoParam))
            }
        }
    }
    $("#vlrPremioLiquido").html(toMoneyFormat(vlrPremioLiquido));
}

ModaisCidadao.prototype.selecionaBanco = function(){
    var me= this;

    $("#selectBanco").on("change", function(e) {
        var valorPremioCalc =  $("#valorPremioCalc").val();
        var vlrTributoParam = $("#vlrTributoParam").val();
        var valorTaxasChequeParam= $("#valorTaxasChequeParam").val();
        var valorTaxasTransfParam= $("#valorTaxasTransfParam").val();
        var dataResgatePremioBilhete = $("#dataResgatePremioBilhete").val();
        var vlrTributoGravado = $("#vlrTributoGravado").val();
        var vlrTaxasGravado = $("#vlrTaxasGravado").val();
        var tipoResgate = $("#selectTipoResgate").val();
        me.calculaValoresResgate(tipoResgate,dataResgatePremioBilhete,valorPremioCalc,vlrTributoParam,valorTaxasChequeParam,valorTaxasTransfParam,vlrTributoGravado,vlrTaxasGravado);

    });
}

ModaisCidadao.prototype.selecionaTipoResgate = function(){
    var me= this;

    $("#selectTipoResgate").on("change", function(e) {
        var valorPremioCalc =  $("#valorPremioCalc").val();
        var vlrTributoParam = $("#vlrTributoParam").val();
        var valorTaxasChequeParam= $("#valorTaxasChequeParam").val();
        var valorTaxasTransfParam= $("#valorTaxasTransfParam").val();
        var dataResgatePremioBilhete = $("#dataResgatePremioBilhete").val();
        var vlrTributoGravado = $("#vlrTributoGravado").val();
        var vlrTaxasGravado = $("#vlrTaxasGravado").val();
        var tipoResgate = $("#selectTipoResgate").val();

        if(tipoResgate=="C"){
            $("#panelTransf").css('display','none');
            $("#panelCheque").css('display','inline');
        }else  {
            $("#panelCheque").css('display','none');
            $("#panelTransf").css('display','inline');
        }

        me.calculaValoresResgate(tipoResgate,dataResgatePremioBilhete,valorPremioCalc,vlrTributoParam,valorTaxasChequeParam,valorTaxasTransfParam,vlrTributoGravado,vlrTaxasGravado);
    });
}

ModaisCidadao.prototype.gravarContaBancaria = function(){
    var me = this;
    $("body").on('click', '#gravarResgate', function(e){
        var form = $("#formPremiacao");
        var formParams = serializeFormToObject(form);
        var tipoResgate = $("#selectTipoResgate").val();
        var data;

        if(tipoResgate=="C"){
            data = {
                tipoResgate: tipoResgate,
                vlrTributo:$("#vlrTributoPremioBilhete").html(),
                vlrTaxas:$("#vlrTaxasPremioBilhete").html(),
                selectBanco: null,
                numeroAgencia: null,
                selectConta: null,
                contaBancaria: null,
                digito: null,
                idPremioBilhete: $("#idPremioBilhete").val()
            }

        }else  {
            data = {
                tipoResgate: tipoResgate,
                vlrTributo:$("#vlrTributoPremioBilhete").html(),
                vlrTaxas:$("#vlrTaxasPremioBilhete").html(),
                selectBanco: $("#selectBanco").val(),
                numeroAgencia: $("#numeroAgencia").val(),
                selectConta: $("#selectConta").val(),
                contaBancaria: $("#contaBancaria").val(),
                digito: $("#digito").val(),
                idPremioBilhete: $("#idPremioBilhete").val()
            }

        }

        ajaxBloqueioDeTela("#gravarResgate",
            enderecoSite + "/cidadao/gravarResgate",
            data,
            'POST',
            function(response) {
                window.location.replace(enderecoSite+response.urlRedirect);
            }
        );
    });
}



ModaisCidadao.prototype.modalAtualizarPerfil = function() {
    var me = this;
    $("#linkHomeAtualizarPerfil").click(function (e) {
        $("#modalHomeBody").load('atualizaPerfil',
            function (response, status, xhr) {
                if (status == "error") {
                    window.location.replace(enderecoSite + "/cidadao/login");
                }
            }
        );
        $("#modalHomeTitle").html('Atualizar meu Perfil');
        $(".modal-dialog").attr("class", "modal-dialog modal-lg");

        bloqueioDeTela("#divPaineisTelaInicial");

        setTimeout(function () {
            $("#modalHome").modal('show');
            me.inicializaEnderecoAtualizaPerfil();
            $("#divPaineisTelaInicial").unblock();
        }, 3000);

        setTimeout(function () {
            $("#emailCidadaoPerfil").focus();
        }, 3500);
    });
}

ModaisCidadao.prototype.modalAlterarSenha = function() {
    $("#linkHomeAlterarSenha").click(function(e) {
        $("#modalHomeBody").load('alterarSenhaPerfil',
            function( response, status, xhr ) {
                if ( status == "error" ) {
                    window.location.replace(enderecoSite+"/cidadao/login");
                }
            }
        );

        $("#modalHomeTitle").html('Alterar minha Senha');
        $(".modal-dialog").attr("class","modal-dialog modal-md");

        bloqueioDeTela("#divPaineisTelaInicial");

        setTimeout(function () {
            $("#divPaineisTelaInicial").unblock();
            $("#modalHome").modal('show');
        }, 1000);

        setTimeout(function () {
            $("#senhaPerfilAntiga").focus();
        }, 1500);
    });
}

ModaisCidadao.prototype.modalMinhasNotasEmDetalhe = function() {
    var me = this;
    $("#linkMinhasNotasEmDetalhe").off('click');
    $("#linkMinhasNotasEmDetalhe").click(function(e) {
        $("#modalHomeBody").load('viewMinhasNotasEmDetalhe',
            function( response, status, xhr ) {
                if ( status == "error" ) {
                    window.location.replace(enderecoSite+"/cidadao/login");
                }
            }
        );
        $("#modalHomeTitle").html('Consultar minhas notas em detalhe');
        $(".modal-dialog").attr("class","modal-dialog modal-lg"); //modal largo
        bloqueioDeTela("#divPaineisTelaInicial");
        setTimeout(function () {
            $("#modalHome").modal('show');
            me.listarNotasCidadaoEmDetalhe();
            $("#divPaineisTelaInicial").unblock();
        }, 1000);
    });
}

ModaisCidadao.prototype.listarNotasCidadaoEmDetalhe = function() {
    var me = this;
    me.pagination = new NFGPagination({
        url: enderecoSite + "/cidadao/listarNotasCidadaoEmDetalhe",
        containerSelector: "#containerMinhasNotasEmDetalhe",
        templateSelector: "#tabelaMinhasNotasEmDetalhe",
        formFilterSelector: "#formMinhasNotasEmDetalhe",
        btnFilterSelector: "#filtrarMinhasNotasEmDetalhe",
        beforeLoad: function(data) {
            $("#modalHomeBody").block({
                css: {
                    backgroundColor: 'transparent', color: 'transparent', border: '1'
                },
                overlayCSS:  {
                    backgroundColor: '#000',
                    opacity:         0.2,
                    cursor:          'wait'
                }
            });
            if(data){
                data.cpfFiltro = $("#cpfFiltroDetalhe").val();
                //data.cpfFiltro = '12312312387';

                if(data.referenciaInicial=="__/__/____"){
                    data.referenciaInicial="";
                }

                if(data.referenciaFinal=="__/__/____"){
                    data.referenciaFinal="";
                }
            }
        },
        afterLoad: function(data) {
            $("#modalHomeBody").unblock();
        }
    });

    var hash = window.location.hash.replace("#","");
    var page = parseInt(hash) ? parseInt(hash) : 1;
    me.pagination.init(page);
}

ModaisCidadao.prototype.carregaGridAndamentoReclamacao = function(idReclamacao){
    
    var me = this;
    me.pagination = new NFGPagination({
	url : enderecoSite + "/cidadao/listarAndamentoReclamacao",
	containerSelector : "#containerAndamentoReclamacao",
	templateSelector : "#tabelaAndamentoReclamacao",
	formFilterSelector : "#formAndamentoReclamacao",
	btnFilterSelector : "#btnAndamentoReclamacao",
	beforeLoad : function(data) {
		if (data) {
			data.idReclamacao = idReclamacao;
		}
	},
	afterLoad : function(data) {

		if (data) {
			data.idReclamacao = idReclamacao;
		}
	}
});
 var hash = window.location.hash.replace("#","");
    var page = parseInt(hash) ? parseInt(hash) : 1;
    me.pagination.init(page);
}


ModaisCidadao.prototype.carregaPaginacaoBilhetes = function(me, tipo,element){
    me.pagination = new NFGPagination({
        url: enderecoSite + "/cidadao/listar" +tipo,
        containerSelector: "#container"+tipo,
        templateSelector: "#tabela"+tipo,
        formFilterSelector: "#formBilhetes",
        beforeLoad: function(data) {
            $(element).block({
                message: '<img src="/to-legal/images/loading.gif" width="80px;"/>',
                css: {
                    backgroundColor: 'transparent', color: 'transparent', border: '1'
                },
                overlayCSS:  {
                    backgroundColor: '#000',
                    opacity:         0.2,
                    cursor:          'wait'
                }
            });
            if(data){
                //data.idSorteio = $("#idSorteioSelecionado").val();
                data.idSorteio = me.idSorteio;
            }
        },
        afterLoad: function(data) {
            $(element).unblock();
        }
    });

    var hash = window.location.hash.replace("#","");
    var page = parseInt(hash) ? parseInt(hash) : 1;
    me.pagination.init(page);
}

ModaisCidadao.prototype.carregaPaginacaoPontuacao= function(me, tipo,element){
    me.pagination = new NFGPagination({
        url: enderecoSite + "/cidadao/listar"+tipo,
        containerSelector: "#container"+tipo,
        templateSelector: "#tabela"+tipo,
        formFilterSelector: "#formPontuacao",
        beforeLoad: function(data) {
            $(element).block({
                message: '<img src="/to-legal/images/loading.gif" width="80px;"/>',
                css: {
                    backgroundColor: 'transparent', color: 'transparent', border: '1'
                },
                overlayCSS:  {
                    backgroundColor: '#000',
                    opacity:         0.2,
                    cursor:          'wait'
                }
            });
            if(data){
                data.idSorteio = me.idSorteio;
            }
        },
        afterLoad: function(data,response) {
            $(element).unblock();
            return response;
        }
    });
    var hash = window.location.hash.replace("#","");
    var page = parseInt(hash) ? parseInt(hash) : 1;
    me.pagination.init(page);
}

ModaisCidadao.prototype.inicializaDadosBancarios = function(me){
    if($("#nome").val()!=null){

        $.when(me.cidadao.carregaBancos($("#codigo").val())).done(function(){
            setTimeout(function () {
                $("#selectBanco option").filter(
                    function () {
                        if($("#nomeBanco").val()!=null){
                            return $(this).text() == $("#nomeBanco").val();
                        }
                    }
                ).prop('selected', true);
            }, 300);
        });
    }

}

ModaisCidadao.prototype.eventoReclamacaoDetalhe = function(){
    var me = this;
    $("body").on("click", ".abrirReclamacaoLink", function(e) {
        var idReclamacaoLink = $(this).parent().find('.idReclamacaoLink').html().trim();

        if(idReclamacaoLink==null || idReclamacaoLink.length==0){
            nfgMensagens.show(ALERT_TYPES.ERROR,"Erro na visualizaç&#225;o da reclamaç&#225;o. Tente de novo posteriormente.");
            return;
        }

        $("#modalHomeBody").load('verReclamacaoDetalhe/'+idReclamacaoLink,
            function( response, status, xhr ) {
                if ( status == "error" ) {
                    window.location.replace(enderecoSite+"/cidadao/login");
                }
            }
        );
        $("#modalHomeTitle").html('Reclamaç&#225;o em detalhe');
        $(".modal-dialog").attr("class","modal-dialog modal-lg"); //modal largo
        bloqueioDeTela("#divPaineisTelaInicial");
        me.carregaGridAndamentoReclamacao(idReclamacaoLink);
        $("#modalHome").modal('show');
//        setTimeout(function () {
//            
//            
//            me.cidadao.resetaElementosAcaoReclamacao();
//            $("#divPaineisTelaInicial").unblock();
//        }, 1000);
    });
}

ModaisCidadao.prototype.inicializaEnderecoAtualizaPerfil = function(){
    var me = this;
    
    if ($("#nomeUf").val()!=null) {
        $("#selectUf option").filter(function() {
            return $(this).text() == $("#nomeUf").val();
        }).prop('selected', true);

        if($("#nomeMunicipio").val()!=null){
            $.when(me.cidadao.carregaMunicipios($("#nomeUf").val())).done(function(){
                setTimeout(function () {
                    $("#selectMunicipio option").filter(
                        function () {
                            if($("#nomeMunicipio").val()!=null){
                                return $(this).text() == $("#nomeMunicipio").val();
                            }
                        }
                    ).prop('selected', true);
                }, 1000);
            });
        }
    }

    var enderecoHomolog = $("#enderecoHomolog").val();


    if(enderecoHomolog != null && enderecoHomolog=='S'){
        $('#inputsEndereco :input').attr('readonly', true);
        $('#inputsEndereco :input').attr('tabindex',-1);

        $('#inputsEndereco :input').attr('title',"");

        $('#inputsEndereco :input').keydown(function (evt) {
            return false;
        });

        $(".combo-endereco").attr('disabled',true);
        $("#enderecoHomologado").val('S');
        $("#labelEndHomolog").css("display","block");
    }else{
        $(".combo-endereco").attr('disabled',false);
        $('#inputsEndereco :input').attr('readonly', false);
        $("#enderecoHomologado").val('N');
        $("#labelEndHomolog").css("display","none");
    }

}

