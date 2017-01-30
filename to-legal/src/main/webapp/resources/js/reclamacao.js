function Reclamacao(options){
    var me = this;
    me.build(options);
    me.urlBase = options.urlBase;
    me.numeroCnpj = options.numeroCnpj;
    $(".textCnpj").html(toCnpjFormat($(".textCnpj").html()));
    $(".textCpf").html(toCpfFormat($(".textCpf").html()));
    $(".textMoney").html(toMoneyFormat($(".textMoney").html()));
    me.incluirComplemento(options.numeroCnpj, options.urlBase);
    me.modalReclamacaoDetalhe();
    me.mudancaAcaoReclamacao();
    me.alterarReclamacao();
    me.gerarRelatorioReclamacoes();
    me.limparCamposRel();
}


Reclamacao.prototype.build = function(options){
    var me = this;
    if(options != null && options.tela == "reclamacaoIndex"){
        me.gridReclamacaoUsuario();
    }

    $(".datepicker").each(function() {
        me.initDatePicker($(this));
    });

    if(options != null && options.tela == "consultar"){
        me.eventoListar(options.urlBase, options.numeroCnpj);
    }
}

Reclamacao.prototype.limparCamposRel = function() {
    $("#btnLimparReclamacoesRel").on("click", function (e) {
        $("#dataRecRel").val("");
        $("#dataRecRelFim").val("");
        $("#selectSituacao").val(0);
        $("input[name=radioMotivoReclamacaoRel][value=" + 0+ "]").prop('checked', true);
        $("input[name=radioTipoDocReclamacaoRel][value=" + 0+ "]").prop('checked', true);
        $("input[name=radioPrazoRel][value=" + 0+ "]").prop('checked', true);
    });
}

Reclamacao.prototype.gerarRelatorioReclamacoes = function(){
    $("#btnGerarRelReclamacao").on("click", function(e){

        var dataRecRel = $("#dataRecRel").val();
        if(dataRecRel==null || dataRecRel== "__/__/____"){
            dataRecRel="";
        }

        var dataRecRelFim = $("#dataRecRelFim").val();
        if(dataRecRelFim==null || dataRecRelFim== "__/__/____"){
            dataRecRelFim="";
        }


        var situacao = $("#selectSituacao").val();


        var motivoReclamacao = $("input[type='radio'][name='radioMotivoReclamacaoRel']:checked").val();
        var tipoDocFiscalReclamacao = $("input[type='radio'][name='radioTipoDocReclamacaoRel']:checked").val();
        var recNoPrazo = $("input[type='radio'][name='radioPrazoRel']:checked").val();

        var url = enderecoSite
                +"/portal/reclamacao/usuario/gerarRelatorio"
            //+"/reclamacao/usuario/gerarRelatorio"
            +"?data="+dataRecRel
            +"&dataFim="+dataRecRelFim
            +"&situacao="+situacao
            +"&motivoReclamacao="+motivoReclamacao
            +"&tipoDocFiscalReclamacao="+tipoDocFiscalReclamacao
            +"&recNoPrazo="+recNoPrazo;

        window.open(url, '_blank');

    });

}

Reclamacao.prototype.gridReclamacaoUsuario = function(){
    var me = this;
    me.pagination = new NFGPagination({
        url: enderecoSite + "/portal/reclamacao/usuario/listarReclamacoes",
        //url: enderecoSite + "/reclamacao/usuario/listarReclamacoes",
        containerSelector: "#containerReclamacoesUsuario",
        templateSelector: "#tabelaReclamacoesUsuario",
        formFilterSelector: "#formReclamacoesUsuario",

        beforeLoad: function() {
            bloqueioDeTela("#dadosReclamacoesCadastradas");
        },
        afterLoad: function() {
            $("#dadosReclamacoesCadastradas").unblock();
        }
    });
    me.pagination.init(0);
}

Reclamacao.prototype.eventoListar = function(url, cnpj){
    nfgMensagens.remove();
    var me = this;
    me.pagination = new NFGPagination({
        url: enderecoSite + url + "listar",
        containerSelector: "#containerReclamacao", 
        templateSelector:  "#tabelaReclamacaoEmpresaContador"
    });

    var hash = window.location.hash.replace("#","");
    var page = parseInt(hash) ? parseInt(hash) : 1;
    me.pagination.afterLoad= me.mostrarComplemento.bind(me);
    me.pagination.init(page, {numeroCnpj : cnpj, urlBase: url});
}

Reclamacao.prototype.mostrarComplemento = function(data){
    $("#messagesContainer").html("");
    $("#inputIdReclamacao").val("");
    $("#descricaoComplemento").val("");
    $("#selectAcoesDisponiveisEmpresa").val("");
    $(".optradio-reclamacao").off("click");
    $(".optradio-reclamacao").on("click", function(e) {

        var idReclamacao = $(this).val();
        $.ajax({
            url: enderecoSite + data.urlBase + "/selectStatus",
            data:{
                idReclamacao: idReclamacao
            },
            type: 'POST',
            success: function(response) {
                if(response.statusDisponiveis.length > 0){
                    $("#containerComplemento").css('display', 'block');
                    $("#containerComplemento").find("#inputIdReclamacao").val(idReclamacao);
                    var select = $("#selectAcoesDisponiveisEmpresa");
                    select.find('option')
                        .remove()
                        .end()
                        .append('<option value="-1" >Selecione</option>');
                    $.each(response.statusDisponiveis, function() {
                        select.append($("<option />").val(this.codigo).text(this.tipoSituacaoAcao));
                    });
                    
                    $.ajax({
                        url: enderecoSite + data.urlBase + "verReclamacaoDetalhe/"+idReclamacao,
                        data:{
                            idReclamacao: idReclamacao
                        },
                        type: 'POST',
                        success: function(response) {
                           
                        	$("#numReclamacao").html(response.reclamacao.id);
                        	$("#dataReclamacaoStr2").html(response.dataReclamacaoStr);
                        	$("#numeroDocumento2").html(response.reclamacao.numero);
                        	$("#nomeFantasia2").html(response.reclamacao.nomeFantasiaEmpresa);
                        	$("#dataEmissaoDocumento2").html(response.dataEmissaoStr);
                        	$("#valorDocumento2").html(response.reclamacao.valor);
                        	$("#motivoReclamacao2").html(response.reclamacao.motivoReclamacao);
                        	$("#situacaoAtual2").html(response.reclamacao.statusAndamentoStr);
                        }
                    });
                }else{
                    $("#containerComplemento").css('display', 'none');
                }
            }
        });
    });
}

Reclamacao.prototype.incluirComplemento = function(cnpj, urlBase){
    $("#btnComplemento").on("click", function(e){

        var descricaoComplemento = $("#descricaoComplemento").val().trim();
        var codigoAcao = $("#selectAcoesDisponiveisEmpresa").val();
        var idReclamacao = $("#inputIdReclamacao").val();

        if(idReclamacao == ""){
            nfgMensagens.show(ALERT_TYPES.ERROR,'Selecione a Reclamaç&#225;o',false);
            return;
        }

        if(codigoAcao == -1){
            nfgMensagens.show(ALERT_TYPES.ERROR,'Selecione o Status',false);
            $("#selectAcoesDisponiveisEmpresa").focus();
            return;
        }

        $.ajax({
            url: enderecoSite + urlBase + "/incluirComplemento",
            data: {
                descricaoComplemento : descricaoComplemento,
                codigoAcao : codigoAcao,
                idReclamacao : idReclamacao,
                cnpj: cnpj
            },
            type: 'POST',
            success: function(response) {
                if(response.sucesso){
                    nfgMensagens.show(ALERT_TYPES.SUCCESS,'Reclamaç&#225;o atualizada com sucesso. Continue acompanhando-a na listagem de andamento da reclamaç&#225;o.',false);
                    $("#containerComplemento").css('display', 'none');
                }else{
                    nfgMensagens.show(ALERT_TYPES.ERROR,'Erro ao tentar atualizar a situaç&#225;o da reclamaç&#225;oo. Tente de novo posteriormente.',false);
                }
            }
        });
    });
}

Reclamacao.prototype.modalReclamacaoDetalhe = function(){
    var me = this;
    $(".abrirReclamacaoLink").off("click");
    $("body").on("click", ".abrirReclamacaoLink", function(e) {
        var idReclamacaoLink = $(this).parent().find('.idReclamacaoLink').html().trim();

        if(idReclamacaoLink==null || idReclamacaoLink.length==0){
            nfgMensagens.show(ALERT_TYPES.ERROR,"Erro na visualiza��o da reclama��o. Tente de novo posteriormente.");
            return;
        }

        bloqueioDeTela("#divPaineisTelaInicial");
        $("#modalHomeBody").load('verReclamacaoDetalhe/'+idReclamacaoLink,
            function( response, status, xhr ) {
                if ( status == "error" ) {
                    window.location.replace(enderecoSite+"/cidadao/login");
                } else {
                    $("#modalHomeTitle").html('Reclama��o em detalhe');
                    $(".modal-dialog").attr("class","modal-dialog modal-lg"); //modal largo

                    $("#modalhome").modal('show');
                    me.carregaGridAndamentoReclamacao();
                    me.resetaElementosAcaoReclamacao();
                    $("#divPaineisTelaInicial").unblock();
                }
            }
        );
    });
}

Reclamacao.prototype.carregaGridAndamentoReclamacao = function(){
    var me = this;
    me.pagination = new NFGPagination({
    	url: enderecoSite + "/cidadao/listarAndamentoReclamacao",
    	//url: enderecoSite + "/portal/reclamacao/usuario/listarAndamentoReclamacao",
        //url: enderecoSite + "/reclamacao/usuario/listarAndamentoReclamacao",
        containerSelector: "#containerAndamentoReclamacao",
        templateSelector: "#tabelaAndamentoReclamacao",
        formFilterSelector: "#formAndamentoReclamacao",
        btnFilterSelector: "#btnAndamentoReclamacao",
        beforeLoad: function(data) {
            if(data){
                data.idReclamacao = $("#idReclamacaoLink").val();
            }
        }
    });
    var hash = window.location.hash.replace("#","");
    var page = parseInt(hash) ? parseInt(hash) : 1;
    me.pagination.init(page);
}

Reclamacao.prototype.resetaElementosAcaoReclamacao = function(){
    var me= this;

    $("#divInfoReclamacao").css('display','none');
    $("#btnAlterarReclamacao").css('display','none');
    $("#selectNovoStatusReclamacao").val("-1");

    var codgSituacao = $("#codgSituacaoReclamacao").val();
    if(codgSituacao==15){
        $("#painelAcoesUsuarioReclamacao").css('display','none');
    }else{
        $("#painelAcoesUsuarioReclamacao").css('display','block');
    }
}

Reclamacao.prototype.mudancaAcaoReclamacao = function(){
    var me= this;
    $("body").on("change", "#selectNovoStatusReclamacao", function() {
        if($(this).val()=="-1"){
            $("#btnAlterarReclamacao").css('display','none');
        }else{
            $("#btnAlterarReclamacao").css('display','inline');
        }

        switch ($(this).val()){
            case "-1": /**Aguardar*/
            $("#divInfoReclamacao").css('display','none');
                return;
            case "11": /**Cancelar*/
            $("#divInfoReclamacao").css('display','none');
                return;
            case "12":  /**Acionar fiscaliza��o e notificar empresa*/
            $("#divInfoReclamacao").css('display','block');
                return;
            case "13": /**Notificar cidad�o que reclama��o n�o procede*/
            $("#divInfoReclamacao").css('display','block');
                return;
        }
    });
}

Reclamacao.prototype.alterarReclamacao = function(){
    var me= this;
    $("#btnAlterarReclamacao").off("click");
    $("#painelAcoesUsuarioReclamacao").on("click", "#btnAlterarReclamacao", function() {
        var codgTipoCompl = $("#selectNovoStatusReclamacao option:selected").val();
        var comentario= $("#infoReclamacao").val();
        var idReclamacao = $("#idReclamacao").val();

        ajaxBloqueioDeTela(this,
            enderecoSite+'/portal/reclamacao/usuario/alterarSituacaoReclamacao',
            //enderecoSite+'/reclamacao/usuario/alterarSituacaoReclamacao',
            {idReclamacao:idReclamacao,novoCodgTipoCompl:codgTipoCompl,infoReclamacao:comentario},
            'POST',
            function(response){

                if(response.sucesso){
                    nfgMensagens.show(ALERT_TYPES.SUCCESS,"Atualiza��o realizada com sucesso. Continue acompanhando-a na listagem de andamento da reclama��o.",true);
                }else{
                    nfgMensagens.show(ALERT_TYPES.ERROR,"Erro ao tentar atualizar a situa��o da reclama��o. Tente de novo posteriormente.",true);
                }

                $("#btnAndamentoReclamacao").click();
                $("#btnReclamacoes").click();
                me.carregaComboAcoesDisponiveisReclamacao();
                me.resetaElementosAcaoReclamacao();
            }
        )
    });
}

Reclamacao.prototype.carregaComboAcoesDisponiveisReclamacao = function(){
    var me= this;
    var idReclamacao = $("#idReclamacao").val();
    $.ajax({
        url: enderecoSite + "/portal/reclamacao/usuario/selectAcoesDisponiveis",
        //url: enderecoSite + "/reclamacao/usuario/selectAcoesDisponiveis",
        data:{
            idReclamacao: idReclamacao
        },
        type: 'POST',
        success: function(response) {
            var select = $("#selectNovoStatusReclamacao");
            select.find('option')
                .remove()
                .end()
                .append('<option value="-1" >Aguardar</option>');
            $.each(response.acoesDisponiveis, function() {
                select.append($("<option />").val(this.codigo).text(this.tipoSituacaoAcao));
            });
        }
    });
}

Reclamacao.prototype.initDatePicker = function($campo) {
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
        mask: true,
        closeOnDateSelect: true,
        yearStart:1900,
        validateOnBlur: false
    });

    $(".datepickerIcon").click(function() {
        $campo.datetimepicker('show');
    }).css("top","0").css("cursor","pointer");
};