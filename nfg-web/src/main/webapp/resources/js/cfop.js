/**
 * Created by Remisson Silva on 24/09/2014.
 */
function CFOP(options) {
    this.codigoCFOP    = options.codigoCFOP;
    this.descricaoCFOP = options.descricaoCFOP;

    var me = this;
    me.pagination = new NFGPagination({
        url: enderecoSite + "/portal/cfop/list-cfops-autorizados/",
        containerSelector: "#containerCfopsAutorizados",
        templateSelector: "#tabelaCFOPs",
        formFilterSelector: "#filtro",
        btnFilterSelector: ".btn-search-cfop-autorizado"
    });

    var page = 1;
    me.pagination.init(page);
    me.eventoExcluir();
    me.eventoConfirmacao();
    me.eventoCancelarExclusao();
    me.eventoSalvar();
    me.campoNumerico();
    me.eventoExibirBotaoSalvar();
    me.exibrBotaoSalvar($('#selectCfop'));
}

CFOP.prototype.eventoConfirmacao = function(){
    var self = this;

    $("#containerCfopsAutorizados").on("click", ".btn-cfop-confirm", function(e) {

        e.preventDefault();
        e.stopImmediatePropagation();

        var title        = "<strong>Tem certezar que deseja excluir este CFOP ?</strong>";
        var buttonOk     = "<button type='submit' class='btn btn-default col-md-6 btn-excluir-ok'>Sim</button>";
        var buttonCancel = "<button class='btn btn-danger col-md-6 btn-excluir-cancelar' " +
            "data-id-cfop='{{idCfopeAutorizado}}' type='submit'>N&atilde;o</button>";

        self.idCfopAutorizado = $(this).data("id-cfop");

        self.confirmModal = new ConfirmModal(title,null,buttonOk,buttonCancel);
        self.confirmModal.open();
    });
};

CFOP.prototype.eventoCancelarExclusao = function(){
    var self = this;
    $("body").on("click", ".btn-excluir-cancelar", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        self.confirmModal.close();
    });
};

CFOP.prototype.eventoExcluir = function(){
    var self = this;
    $("body").on("click", ".btn-excluir-ok", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();

        var idCfopAutorizado = self.idCfopAutorizado;
        var btn = this;
        $(this).attr("disabled", true);

        e.preventDefault();
        e.stopImmediatePropagation();

        $.ajax({
            url: enderecoSite + "/portal/cfop/excluir-cfop",
            data: {idCfopAutorizado: idCfopAutorizado},
            type: "POST",
            success: function (resposta) {
                self.pagination.loadPage(1);
                if (resposta.success)
                    nfgMensagens.show(ALERT_TYPES.SUCCESS, resposta.message);
                else if(!resposta.success)
                    nfgMensagens.show(ALERT_TYPES.ERROR, resposta.message);
                self.confirmModal.close();
            }
        });
    });
};

CFOP.prototype.eventoSalvar = function(){
    var self = this;

    $("#cfop-form").on("click", ".btn-salvar", function(e)
    {
        e.preventDefault();
        e.stopImmediatePropagation();

        $("#cfop-form .btn-salvar").prop("disabled", true);
        var form = $("#cfop-form");
        var formParams = serializeFormToObject(form);
        var url = enderecoSite + "/portal/cfop/cadastrar-cfop";

        $.ajax({
            url: url,
            data: { codigo: formParams.codigo },
            type: "POST",
            success: function (resposta) {
                self.pagination.loadPage(1);
                if (resposta.success)
                {
                    nfgMensagens.show(ALERT_TYPES.SUCCESS, resposta.message);
                    self.limparCamposCadastro();
                }
                else if(!resposta.success)
                    nfgMensagens.show(ALERT_TYPES.ERROR, resposta.message);
            }
        });
        $("#cfop-form .btn-salvar").prop("disabled", false);
    });
};

CFOP.prototype.limparCamposCadastro = function(){
    $("#cfop-form")[0].reset();
    this.exibirBotaoSalvar($('#selectCfop'));
};

CFOP.prototype.campoNumerico = function(){
	$(".numerico").keypress(function (e) {
	    if (String.fromCharCode(e.keyCode).match(/[^0-9]/g)) 
	    	return false;
	});
};

CFOP.prototype.eventoExibirBotaoSalvar = function(){
	var self = this;
	$("body").on("click keyup","#selectCfop",function(){
		self.exibirBotaoSalvar(this);
	});
};

CFOP.prototype.exibirBotaoSalvar = function(combo){
	var value = false;
	if($(combo).val() == "")value = true;
	$(".btn-salvar").prop("disabled", value);
};