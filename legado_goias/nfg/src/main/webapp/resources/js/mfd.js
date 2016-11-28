function Mfd(options){
    var me = this;
    me.urlBase = options.urlBase;
    me.eventoListar();
}


Mfd.prototype.eventoListar = function(){
    var me = this;
    me.pagination = new NFGPagination({
        url: enderecoSite + me.urlBase + "listar",
        containerSelector: "#containerMfd",
        templateSelector:  "#tabelaMfd",
        formFilterSelector: "#formReferencia",
        btnFilterSelector: "#btnGerar"

    });

    var hash = window.location.hash.replace("#","");
    var page = parseInt(hash) ? parseInt(hash) : 1;
    me.pagination.init(page, {inscricaoEstadual: $(inscricaoEstadual).val()});
}