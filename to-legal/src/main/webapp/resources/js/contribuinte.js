/**
 * @author henrique-rh
 * @since 23/07/2014
 */
function Contribuinte(options) {
    this.urlCadastrar = options.urlCadastrar;
    this.urlAlterar = options.urlAlterar;
    this.urlListar = options.urlListar;
    this.fecharModal();
    
    var me = this;
    me.pagination = new NFGPagination({
        url: enderecoSite + "/contribuinte/list",
        containerSelector: "#containerContribuintes",
        templateSelector: "#tabelaContribuintes",
        formFilterSelector: "#formEmpresas",
        beforeLoad: function(data) {
        },
        afterLoad: function(data) {
        }
    });
    me.pagination.init(0);

    $.ajax({
        url: enderecoSite + "/mensagens/mensagensNaoLidasEmpresa",
        type: 'POST',
        success:function(response){
            if(response.nrMensagensNovas!=null){
                $("#nrMensagensNovasLink").css("display","inline");
                $("#nrMensagensNovas").html(response.nrMensagensNovas);
            }else{
                $("#nrMensagensNovasLink").css("display","none");
            }
        }
    });
}

Contribuinte.prototype.initListar = function() {
    var me = this;
//    me.pagination = new NFGPagination({
//        url: enderecoSite + me.urlListar,
//        containerSelector: "#containerContribuintes",
//        templateSelector: "#tabelaContribuintes",
//        formFilterSelector: "#formFiltroContribuinte",
//        btnFilterSelector: "#filtrarContribuinte",
//        afterLoad: function() {
//            me.afterLoad();
//        }
//    });
//    me.assineEventos();
//    var hash = window.location.hash.replace("#","");
//    var page = parseInt(hash) ? parseInt(hash) : 1;
//    me.pagination.init(page);
//    me.apliqueMaskFilter();
//    me.modalMensagensEmpresas();
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
        var termoDeAcordo="Declaro estar ciente de que o credenciamento no programa To Legal me obriga: <br><br>";
        termoDeAcordo += "1. Informar ao consumidor, no momento da emiss&#227;o do documento fiscal, a possibilidade de inclus&#227;o do CPF no referido documento; <br><br>";
        termoDeAcordo += "2. Transmitir ao sistema de informa&#231;&#227;o do Programa os dados das opera&#231;&#245;es e presta&#231;&#245;es correspondentes, na forma e nos prazos estabelecidos em ato da Secretaria da Fazenda. <br><br>";
        termoDeAcordo += "Notas:  <br><br>";
        termoDeAcordo += "- A inclus&#227;o do n&#250;mero do CPF no documento fiscal n&#227;o pode ser condicionada a nenhuma esp&#233;cie de cadastro pr&#233;vio do consumidor na empresa;  <br><br>";
        termoDeAcordo += "- &#201; necess&#225;rio manter atualizados os dados cadastrais de todos os seus estabelecimentos, especialmente seus nomes de fantasia e os seus endere&#231;os comerciais, os quais ser&#227;o disponibilizados aos consumidores a fim de que identifiquem corretamente as empresas participantes do Programa.";
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
        var termoDeAcordo="Declaro estar ciente de que o credenciamento no programa To Legal me obriga a informar ao cidad&#225;o a possibilidade de incluir o CPF no documento fiscal, bem como transmitir as informações à SEFAZ/TO.";
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

Contribuinte.prototype.apliqueMaskFilter = function() {
    $(".inputInscricao").mask("99.999.999-9");
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