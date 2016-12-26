/**
 * Created by bruno-cff on 23/06/2015.
 */

function PremiacaoPortal(){
    var me = this;
    me.eventoListar();
}


PremiacaoPortal.prototype.eventoListar = function(){
    $(document).ready(function() {
        var me = this;
        me.pagination = new NFGPagination({
            url: enderecoSite + '/portal/premiacao/listarPremiacao',
            containerSelector: "#containerPremiacao",
            templateSelector: "#tabelaPremiacaoPortal"
        });

        var seletorCombo = "#selectSorteios";
        $(seletorCombo).on("change", function(e) {
            me.pagination.loadPage(page, {idSorteio: this.value});
        });

        var hash = window.location.hash.replace("#","");
        var page = parseInt(hash) ? parseInt(hash) : 1;
        me.pagination.init(page);
    });
}
