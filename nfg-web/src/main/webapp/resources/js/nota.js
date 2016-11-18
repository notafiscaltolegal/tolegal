/**
 * Created by Remisson Silva on 30/09/2014.
 */

function Nota(options,tipoDocumentoFiscal,idNota)
{
    var self = this;
    this.inscricao = options.inscricaoEstadual;
    this.tipoDocumentoFiscal = tipoDocumentoFiscal;
    this.idNota = idNota;
    this.urlBase = options.urlBase;

    this.build();
    this.eventoInserir();
    this.eventoAlterar();
    this.eventoTipoDocumento();
    this.eventoExibirBotoes();
    this.eventoAlterarOk();
    this.apliqueMaskFilter();
    this.eventoConfirmacao();
    this.eventoCancelarAcao();
    this.eventoExcluir();
    this.limparFiltros();
    this.eventoListar();
    this.eventoFiltrar();
    this.eventoCarregarPelaLista();
    this.limparCampos();


}

Nota.prototype.build = function()
{
    var self = this;

    moment.locale('pt-BR');

    $(".btn-salvar").prop("disabled", true);
    $(".btn-excluir").prop("disabled", true);

    $(".datepicker").each(function() {
        var data = $(this).data();
        self.initDatePicker($(this), new Date());
    });

    self.showHiddenFields(self,$(
        "#selectTipoDocumentoFiscal"
    ).val(1));

    self.carregarParametros(self);

    self.msgModalHabilitacaoBotaoIncluir();

};


Nota.prototype.eventoCarregarPelaLista = function(){
    //alert("123");
    var self = this;
    $("body").on("click", ".editLink", function(e) {
        //$(".editLink").click(function(e){
        var nrDocList = $(this).parent().parent().find('.nrDocList').html().trim();
        var tipoDocList = $(this).parent().parent().find('.tipoDocList').html().trim();
        var serieList = $(this).parent().parent().find('.serieList').html().trim();
        var subSerieList = $(this).parent().parent().find('.subSerieList').html().trim();
        var dataEmissaoList = $(this).parent().parent().find('.dataEmissaoList').html().trim();
        var serieVal = null;
        var tipoDocVal = null;
        $('#numeroDocumentoFiscal').val(nrDocList);
        $('#subSerieNotaFiscalInput').val(subSerieList);
        $('#dataEmissao').val(dataEmissaoList);

        switch(serieList.trim()) {
            case 'D':
                serieVal='0';
                break;
            case 'Única':
                serieVal='1';
                break;
            case 'D - Única':
                serieVal='2';
                break;
        }

        switch(tipoDocList.trim()) {
            case 'ECF - Antigo':
                tipoDocVal='0';
                break;
            case 'Modelo 1':
                tipoDocVal='1';
                break;
            case 'Modelo 2':
                tipoDocVal='2';
                break;
        }

        $('#selectSerieNotaFiscal option').eq(serieVal).prop('selected', true);
        $('#selectTipoDocumentoFiscal option').eq(tipoDocVal).prop('selected', true);

        self.showHiddenFields(self,$(
            "#selectTipoDocumentoFiscal"
        ).val());


        self.carregarParametros(self);
        $("#cpf").focus();
    });
}


Nota.prototype.apliqueMaskFilter = function(){
    $("#subSerieNotaFiscalInput").val($("#subSerieNotaFiscalInput").val().toUpperCase());
    $(".mascaraCpf").mask("999.999.999-99");
    $(".mascaraValor").maskMoney();
    $(".mascaraValor").maskMoney('mask');
    $(".numericInput").numericInput();
};


Nota.prototype.eventoFiltrar = function() {
    $("#filtrarBuscaNota").click();
}

Nota.prototype.eventoListar = function() {
    var me = this;
    me.pagination = new NFGPagination({
        url: enderecoSite + me.urlBase + "buscar",
        containerSelector: "#containerUltimasNotasInseridas",
        templateSelector: "#tabelaUltimasNotasInseridas",
        formFilterSelector: "#formFiltroNota",
        btnFilterSelector: "#filtrarBuscaNota",
        beforeLoad: function(data) {
            if(data){
                if(data.cpfFiltro!=null){
                    data.cpfFiltro = onlyNumbers(data.cpfFiltro);
                }
                if(data.dataEmissaoFiltro=="__/__/____"){
                    data.dataEmissaoFiltro="";
                }
            }
        }
    });

    var hash = window.location.hash.replace("#","");
    var page = parseInt(hash) ? parseInt(hash) : 1;
    me.pagination.init(page);
};

Nota.prototype.eventoInserir = function()
{
    var self = this;
    $("#btnInserirNf").click(function(e)
    {
        self.salvarNota();
    });
};



Nota.prototype.eventoAlterar = function()
{
    var self = this;
    $("#btnAlterarNf").click(function(e)
    {

        var title        = "<strong>Tem certezar que deseja alterar esta nota ?</strong>";
        var buttonOk     = "<button  type='submit' class='btn btn-default col-sm-6 btn-alterar-ok btn-cancelar-acao' >Sim</button>";
        var buttonCancel = "<button class='btn btn-danger col-sm-6 btn-cancelar-acao' type='submit'>N&atilde;o</button>";

        self.confirmModal = new ConfirmModal(title,null,buttonOk,buttonCancel);
        self.confirmModal.open();

    });
};


Nota.prototype.limparFiltros = function()
{
    var self = this;
    $(".limparFiltro").click(function() {
        $(this).closest('form').find("input[type=text], textarea").val("");
        self.eventoFiltrar();
    });
}

Nota.prototype.eventoAlterarOk = function()
{
    var self = this;
    $("body").on("click", ".btn-alterar-ok", function(e) {
        self.salvarNota();
    });
};


Nota.prototype.salvarNota = function(){
    var self = this;
    var form = $("#documentoFiscal");
    var formParams = serializeFormToObject(form);
    var url = enderecoSite + self.urlBase + "cadastrar";

    formParams.valorTotal = self.removeMascaraValorTotal(formParams.valorTotal);
    formParams.cpf = self.removeMascaraCpf(formParams.cpf);

    //LOGICA DEFEITUOSA, e em redundancia com o server.

    //if(!self.isDataLimiteValida())
    //{
    //   nfgMensagens.show(ALERT_TYPES.ERROR, "Data de Emiss&#225;o Inválida!");
    //   return;
    //}

    if(!self.isParamsIncluirValidos(formParams.cpf))
    {
        nfgMensagens.show(ALERT_TYPES.ERROR, "CPF Inválido!");
        return;
    }

    var dataEmissao = self.formatDateToSave(formParams.dataEmissao);

    var subSerieNF = formParams.subSerieNotaFiscal.toUpperCase();

    $.ajax({
        url: url,
        data:
        {
            inscricaoEstadual: self.inscricao,
            tipoDocumentoFiscal: formParams.tipoDocumentoFiscal,
            numeroDocumentoFiscal: formParams.numeroDocumentoFiscal,
            serieNotaFiscal: formParams.serieNotaFiscal,
            subSerieNotaFiscal: subSerieNF,
            dataEmissao: dataEmissao,
            cpf: formParams.cpf,
            valorTotal: formParams.valorTotal
        },
        type: "POST",
        success: function (resposta) {
            if (resposta.success)
            {
                nfgMensagens.show(ALERT_TYPES.SUCCESS, resposta.message);
                self.eventoLimparCampos();
            }
            else
            {
                nfgMensagens.show(ALERT_TYPES.ERROR, resposta.message);
            }
            self.carregarParametros(self);
            self.eventoFiltrar();
            $("#numeroDocumentoFiscal").focus();
        }
    });
}


Nota.prototype.eventoTipoDocumento = function(){
    var self = this;

    $("body").on("click keyup", "#selectTipoDocumentoFiscal", function(e){
        self.showHiddenFields(self,$(this).val());
    });

    $("body").on("change", "#selectTipoDocumentoFiscal", function(e){
        self.msgModalHabilitacaoBotaoIncluir();
    });
};

Nota.prototype.msgModalHabilitacaoBotaoIncluir = function(){
    var self = this;
    var title        = "<strong>Aten&#231;&#227;o</strong>";
    var elementoVazio     = "<div class='col-sm-3'></div>";
    var buttonOk     = "<button type='submit' class='btn btn-primary col-sm-6 btn-cancelar-acao' id='btnEntendi'>Entendi</button>";

    if( $('#selectTipoDocumentoFiscal option').eq('0').prop('selected')){
    	self.confirmModal = new ConfirmModal(title,"Para habilitar o bot&#227;o de inclus&#227;o preencha o N&uacute;mero do documento e a Data de emiss&#227;o.",elementoVazio,buttonOk);
    }else{
    	self.confirmModal = new ConfirmModal(title,"Para habilitar o bot&#227;o de inclus&#227;o preencha o N&uacute;mero do documento, a S&#233;rie <br/> e a Data de emiss&#227;o.",elementoVazio,buttonOk);
    }

    self.confirmModal.open();
    $("#btnEntendi").focus();
}

Nota.prototype.showHiddenFields = function(superObject,tipoDocumento)
{
    if(tipoDocumento == 3)
    {
        $(".serieESubserie").css("display","none");
        $("#numeroDocumentoFiscalLabel").html("N&uacute;mero do COO");
        $("#numeroDocumentoFiscal").prop("placeholder","Nr. do COO");
    }
    else
    {
        $(".serieESubserie").css("display","block");
        $("#numeroDocumentoFiscalLabel").html("N&uacute;mero Doc.");
        $("#numeroDocumentoFiscal").prop("placeholder","Nr. do Documento");    }
    superObject.carregarParametros(superObject);
};


Nota.prototype.eventoExibirBotoes = function(){
    var self= this;

    $("body").on("blur", ".eventoExibirBotoes", function(e) {
        self.carregarParametros(self);
    });

    $("#dataEmissao").on("change", function(e) {
        self.carregarParametros(self);
    });
};

Nota.prototype.carregarParametros = function(self)
{
    self.apliqueMaskFilter();
    var form = $("#documentoFiscal");
    var formParams = serializeFormToObject(form);

    if(self.validarModelos(formParams) || self.validarEcfAntigo(formParams)){
        self.consultaCarregarParametros(self,formParams);
    }
    else{
        $("#btnAlterarNf").css("display","none");
        $("#btnInserirNf").css("display","inline");

        $(".btn-salvar").prop("disabled", true);
        $(".btn-excluir").prop("disabled", true);
    }
};

Nota.prototype.consultaCarregarParametros = function(self,formParams)
{
    var url = enderecoSite + self.urlBase + "get-layout-params";
    var dataEmissao = self.formatDateToSave(formParams.dataEmissao);
    //var dataEmissao = new Date(formParams.dataEmissao);

    $.ajax({
        url: url,
        data: {
            inscricaoEstadual: self.inscricao,
            tipoDocumentoFiscal: formParams.tipoDocumentoFiscal,
            numeroDocumentoFiscal: formParams.numeroDocumentoFiscal,
            serieNotaFiscal: formParams.serieNotaFiscal,
            subSerieNotaFiscal: formParams.subSerieNotaFiscal,
            dataEmissao: dataEmissao
        },
        type: "GET",
        success: function (resposta)
        {
            if (resposta.salvar)
            {
                $(".btn-salvar").prop("disabled", false);
                $(".btn-excluir").prop("disabled", true);

                if(resposta.valorBotao=="Inserir"){
                    $("#cpf").val("");
                    $("#valorTotal").val("");

                    $("#btnAlterarNf").css("display","none");
                    $("#btnInserirNf").css("display","inline");
                }else{
                    $("#btnInserirNf").css("display","none");
                    $("#btnAlterarNf").css("display","inline");
                }
            }

            if(resposta.excluir)
                $(".btn-excluir").prop("disabled", false);

            if(resposta.nota)
                self.idNota = resposta.valorNota;

            if(resposta.cpf) {
                $("#cpf").val(resposta.valorCpf);
            }
            if(resposta.valorTotal)
            {
                $("#valorTotal").val(self.normalizeValor(resposta.paramValorTotal));
            }

            self.apliqueMaskFilter();

            if(resposta.error){
                nfgMensagens.show(ALERT_TYPES.ERROR, resposta.errorMessage);
            }
        }
    });
};

Nota.prototype.normalizeValor = function(param){
    var value = String(param);
    var index = value.indexOf(".");

    if(index < 0)
        value += ".00";
    else if(index >= 0)
    {
        var digitos = value.substring(index + 1);
        if(digitos.length == 1)
            value += "0";
    }

    return value;
};

Nota.prototype.initDatePicker = function($campo, dataLimite) {
    $campo.datetimepicker({
        formatDate: 'd/m/Y',
        format: 'd/m/Y',
        lang: 'pt',
        timepicker: false,
        maxDate: dataLimite,
        allowBlank: true,
        mask: true,
        closeOnDateSelect: true
    });
    $campo.parents(".input-group").find(".datepickerIcon").click(function() {
        $campo.datetimepicker('show');
    }).css("top","0").css("cursor","pointer");
};

Nota.prototype.eventoConfirmacao = function(){
    var self = this;

    $("body").on("click", ".btn-excluir", function(e) {

        e.preventDefault();
        e.stopImmediatePropagation();

        var title        = "<strong>Tem certezar que deseja excluir esta nota ?</strong>";
        var buttonOk     = "<button type='submit' class='btn btn-default col-sm-6 btn-excluir-ok'>Sim</button>";
        var buttonCancel = "<button class='btn btn-danger col-sm-6 btn-cancelar-acao' type='submit'>N&atilde;o</button>";

        self.confirmModal = new ConfirmModal(title,null,buttonOk,buttonCancel);
        self.confirmModal.open();
    });
};

Nota.prototype.eventoCancelarAcao = function(){
    var self = this;
    $("body").on("click", ".btn-cancelar-acao", function(e) {
        self.confirmModal.close();
    });

};



Nota.prototype.eventoExcluir = function(){
    var self = this;
    $("body").on("click", ".btn-excluir-ok", function(e)
    {
        var idDocumentoFiscalDigital = self.idNota;

        $(this).attr("disabled", true);

        e.preventDefault();
        e.stopImmediatePropagation();

        $.ajax({
            url: enderecoSite + self.urlBase + "excluir-nota",
            data: {
                idDocumentoFiscalDigital: idDocumentoFiscalDigital
            },
            type: "POST",
            success: function (resposta)
            {
                if (resposta.success)
                {
                    nfgMensagens.show(ALERT_TYPES.SUCCESS, resposta.message);
                }
                else
                {
                    nfgMensagens.show(ALERT_TYPES.ERROR, resposta.message);
                }
                self.confirmModal.close();
                self.eventoLimparCampos();
                self.carregarParametros(self);
                self.eventoFiltrar();
                $("#numeroDocumentoFiscal").focus();
            }
        });
    });
};


Nota.prototype.limparCampos = function(){
    var self = this;
    $("body").on("click", ".limparCampos", function(e) {
        self.eventoLimparCampos();
        $("#numeroDocumentoFiscal").focus();
    });
}

Nota.prototype.eventoLimparCampos = function()
{
    this.limparCampo("#numeroDocumentoFiscal");
    this.limparCampo("#subSerieNotaFiscalInput");
    this.limparCampo("#cpf");
    this.limparCampo("#valorTotal");

    $("#selectSerieNotaFiscal").val(1);
    $("#dataEmissao").val("__/__/____");
};

Nota.prototype.limparCampo = function(campoId){
    $(campoId).html("");
    $(campoId).val("");
};

Nota.prototype.isCpfValido = function (cpf){
    var numeros, digitos, soma, i, resultado, digitos_iguais;
    digitos_iguais = 1;
    if (cpf.length < 11)
        return false;
    for (i = 0; i < cpf.length - 1; i++)
        if (cpf.charAt(i) != cpf.charAt(i + 1)){
            digitos_iguais = 0;
            break;
        }
    if (!digitos_iguais){
        numeros = cpf.substring(0,9);
        digitos = cpf.substring(9);
        soma = 0;
        for (i = 10; i > 1; i--)
            soma += numeros.charAt(10 - i) * i;
        resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
        if (resultado != digitos.charAt(0))
            return false;
        numeros = cpf.substring(0,10);
        soma = 0;
        for (i = 11; i > 1; i--)
            soma += numeros.charAt(11 - i) * i;
        resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
        if (resultado != digitos.charAt(1))
            return false;
        return true;
    }
    else
        return false;
};

Nota.prototype.validarModelos = function(formParams)
{
    if ((formParams.tipoDocumentoFiscal == 1 || formParams.tipoDocumentoFiscal == 2) &&
        !_.isEmpty(formParams.numeroDocumentoFiscal) && !_.isEmpty(formParams.serieNotaFiscal)
        && formParams.dataEmissao.indexOf("_") < 0)
        return true;
    return false;
};

Nota.prototype.validarEcfAntigo = function(formParams)
{
    if(formParams.tipoDocumentoFiscal == 3 &&
        !_.isEmpty(formParams.numeroDocumentoFiscal) &&
        formParams.dataEmissao.indexOf("_") < 0)
        return true;
    return false;
};

Nota.prototype.isDataLimiteValida = function()
{
    var data = $("#dataEmissao").val();
    var dataAtual = new Date().toLocaleDateString("pt-BR");

    data = moment(data, "DD/MM/YYYY");
    if(moment(data).isAfter(dataAtual))
        return false;
    return true;
};

Nota.prototype.removeMascaraCpf = function(cpf)
{
    return removeCharacteres(cpf,['_','.','-','.']);
};

Nota.prototype.removeMascaraValorTotal = function(valor)
{
    var novoValor = removeCharacteres(valor,['_','.','.','R','$']);
    return novoValor.replace(",",".").trim();
};

Nota.prototype.isParamsIncluirValidos = function(cpf)
{
    if(this.isCpfValido(cpf))
        return true;
    return false;
};


Nota.prototype.formatDateToSave = function(dateStringParam)
{
    var day = dateStringParam.substring(0,2);
    var month = dateStringParam.substring(3,5) -1;
    var year = dateStringParam.substring(6,10);

    return new Date(year, month, day, 0, 0, 0, 0);
};
