/**
 * @author henrique-rh
 * @since 23/07/2014
 */
function Contribuinte(options) {
    this.urlCadastrar = options.urlCadastrar;
    this.urlAlterar = options.urlAlterar;
    this.urlListar = options.urlListar;
    this.fecharModal();
}

Contribuinte.prototype.initListar = function() {
    var me = this;
    me.pagination = new NFGPagination({
        url: enderecoSite + me.urlListar,
        containerSelector: "#containerContribuintes",
        templateSelector: "#tabelaContribuintes",
        formFilterSelector: "#formFiltroContribuinte",
        btnFilterSelector: "#filtrarContribuinte",
        afterLoad: function() {
            me.afterLoad();
        }
    });
    me.assineEventos();
    var hash = window.location.hash.replace("#","");
    var page = parseInt(hash) ? parseInt(hash) : 1;
    me.pagination.init(page);
    me.apliqueMaskFilter();
    me.modalMensagensEmpresas();
};


Contribuinte.prototype.fecharModal = function () {
    var me = this;
    $('#modalHome').on('hidden.bs.modal', function () { //esse modalHome fica na view contribuinte/list
       me.initListar();
        //$("#filtrarContribuinte").click();
    });
}

Contribuinte.prototype.modalMensagensEmpresas = function() {
    var me = this;

    $("#containerContribuintes").on('click', '.btn-mensagem', function(e) {
        var idpessoa = $(this).attr('data-idpessoa');
        $("#modalHomeBody").load( enderecoSite + "/mensagens/viewMensagens",
            function( response, status, xhr ) {}
        );
        $("#modalHomeTitle").html('Minhas Mensagens');
        $(".modal-dialog").attr("class","modal-dialog modal-lg");
        bloqueioDeTela("#divContribuinteList");
        setTimeout(function () {
            $("#modalHome").modal('show');
            me.listarMensagensEmpresas(idpessoa);
            me.gravarLeituraDasMensagensEmpresas(idpessoa);
            $("#divContribuinteList").unblock();
        }, 1000);
    });
}

Contribuinte.prototype.gravarLeituraDasMensagensEmpresas = function(idpessoa) {
    $.ajax({
        url: enderecoSite + "/mensagens/gravarLeituraDasMensagensEmpresas",
        type: 'POST',
        data: {idPessoa: idpessoa}
    });
}

Contribuinte.prototype.listarMensagensEmpresas = function(idpessoa) {
    var me = this;
    me.pagination = new NFGPagination({
        url: enderecoSite + "/mensagens/listarMensagensEmpresas" ,
        containerSelector: "#containerMensagens",
        templateSelector: "#tabelaMensagens",
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
        },
        afterLoad: function(data) {
            $("#modalHomeBody").unblock();
        }
    });

    me.pagination.init(0, {idPessoa: idpessoa});
}

Contribuinte.prototype.assineEventos = function() {
    var me = this;
    $("#containerContribuintes").on("click", ".btn-cadastrar", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        var dataInicioVigencia = $(this).parents("tr").find(".datepicker").val();
        if (!dataInicioVigencia || dataInicioVigencia == "__/__/____") {
            nfgMensagens.show(ALERT_TYPES.ERROR, "Favor selecionar uma data");
            return;
        }
        var termoDeAcordo="Declaro estar ciente de que o credenciamento no programa Nota Fiscal Goiana – NFGoiana me obriga: <br><br>";
        termoDeAcordo += "1. Informar ao consumidor, no momento da emissão do documento fiscal, a possibilidade de inclusão do CPF no referido documento; <br><br>";
        termoDeAcordo += "2. Transmitir ao sistema de informação do Programa os dados das operações e prestações correspondentes, na forma e nos prazos estabelecidos em ato da Secretaria da Fazenda. <br><br>";
        termoDeAcordo += "Notas:  <br><br>";
        termoDeAcordo += "- A inclusão do número do CPF no documento fiscal não pode ser condicionada a nenhuma espécie de cadastro prévio do consumidor na empresa;  <br><br>";
        termoDeAcordo += "- É necessário manter atualizados os dados cadastrais de todos os seus estabelecimentos, especialmente seus nomes de fantasia e os seus endereços comerciais, os quais serão disponibilizados aos consumidores a fim de que identifiquem corretamente as empresas participantes do Programa.";
        var data = $(this).data();
        data.urlCadastro = enderecoSite + me.urlCadastrar;
        data.termoDeAcordo = termoDeAcordo;
        data.dataInicioVigencia = dataInicioVigencia;

        nfgModal.showModalCadastro(data);
    }).on("click", ".btn-alterar", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        var novoInicioVigencia = $(this).parents("tr").find(".datepicker").val();
        if (!novoInicioVigencia || novoInicioVigencia == "__/__/____") {
            nfgMensagens.show(ALERT_TYPES.ERROR, "Favor selecionar uma data");
            return;
        }
        var termoDeAcordo="Declaro estar ciente de que o credenciamento no programa Nota Fiscal Goiana – NFGoiana me obriga a informar ao cidadão a possibilidade de incluir o CPF no documento fiscal, bem como transmitir as informações à SEFAZ/GO.";
        var data = $(this).data();
        data.urlCadastro = enderecoSite + me.urlAlterar;
        data.termoDeAcordo = termoDeAcordo;
        data.dataInicioVigencia = novoInicioVigencia;

        nfgModal.showModalCadastro(data);
    });
};
Contribuinte.prototype.afterLoad = function() {
    var me = this;
    me.apliqueMask();
    $(".datepicker").each(function() {
        var data = $(this).data();
        if (data.podeAlterar) {
            me.initDatePicker($(this), data.limite);
        } else {
            $(this).prop("disabled", true)
        }
    });
};

Contribuinte.prototype.apliqueMaskFilter = function() {
    $(".inputInscricao").mask("99.999.999-9");
};

Contribuinte.prototype.initDatePicker = function($campo, dataLimite) {
    $campo.datetimepicker({
        formatDate: 'd/m/Y',
        format: 'd/m/Y',
        lang: 'pt',
        timepicker: false,
        minDate: 0,
        maxDate: dataLimite,
        allowBlank: true,
        mask: true,
        closeOnDateSelect: true
    });
    $campo.parents(".input-group").find(".datepickerIcon").click(function() {
        $campo.datetimepicker('show');
    }).css("top","0").css("cursor","pointer");
};

Contribuinte.prototype.apliqueMask = function() {
    //mask cnpj
    $(".maskCnpj").each(function() {
        var $campo = $(this);
        var texto = $campo.text();
        if(texto.length == 14) {
            var cnpj_original = texto;
            var cnpj = cnpj_original.substr(0,2) + "." +
                cnpj_original.substr(2,3) + "." +
                cnpj_original.substr(5,3) + "/" +
                cnpj_original.substr(8,4) + "-"+
                cnpj_original.substr(12,2);
            texto = cnpj;
        } else if(texto.length == 15) {
            texto +="-" ;
        }
        $campo.text(texto);
    });
    //mask inscricao
    $(".maskInscricao").each(function() {
        var $campo = $(this);
        var texto = $campo.text();
        if(texto.length == 9) {
            var inscricao_original = texto;
            var inscricao = inscricao_original.substr(0,2) + "." +
                inscricao_original.substr(2,3) + "." +
                inscricao_original.substr(5,3) + "-" +
                inscricao_original.substr(8,2);
            texto = inscricao;
        }
        $campo.text(texto);
    });
};