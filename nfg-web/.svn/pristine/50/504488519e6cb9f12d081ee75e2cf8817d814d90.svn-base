/**
 * @author henrique-rh
 * @since 19/08/2014.
 */
function EmpresaParticipante() {

}

EmpresaParticipante.prototype.initDetalheEmpresa = function() {

};

EmpresaParticipante.prototype.initListar = function(data) {
    var me = this;
    me.pagination = new NFGPagination({
        url: enderecoSite + "/busca/empresas/",
        containerSelector: "#containerEmpresas",
        templateSelector: "#tabelaEmpresasParticipantes",
        formFilterSelector: "#formFiltroEmprParticipante",
        btnFilterSelector: "#filtrar",
        beforeLoad: function(data) {
            if(data && data.cnpj) {
                data.cnpj = onlyNumbers(data.cnpj);
            }
            $("#containerEmpresas").html("");
            appendingLoading($("#containerEmpresas"));
        },
        afterLoad: function() {
            me.apliqueMask();
            removeLoading($("#containerEmpresas"));
        }
    });
    var hash = window.location.hash.replace("#","");
    var page = parseInt(hash) ? parseInt(hash) : 1;
    me.pagination.init(page, data);
};

//TODO Refatorar, mudar para globals
EmpresaParticipante.prototype.apliqueMask = function() {
    $(".inputCnpj").mask("99.999.999?/9999-99");
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