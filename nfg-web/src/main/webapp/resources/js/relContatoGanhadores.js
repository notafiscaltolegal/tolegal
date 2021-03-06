/**
 * Created by bruno-cff on 02/12/2015.
 */
function RelContatoGanhadores(){
    var me = this;
    me.eventoListarRelatorio();
}

RelContatoGanhadores.prototype.eventoListarRelatorio = function(){
    var me = this;
    var idSorteio =  $("#idSorteio").val();
    me.pagination = new NFGPagination({
        //url: enderecoSite + '/contato-ganhadores/contatoGanhadores/listarContatosRelatorio',
        url: enderecoSite + '/portal/contato-ganhadores/contatoGanhadores/listarContatosRelatorio',
        containerSelector: "#containerContatoGanhadoresRel",
        templateSelector: "#tabelaContatoGanhadoresRel"
    });

    var hash = window.location.hash.replace("#","");
    var page = parseInt(hash) ? parseInt(hash) : 1;
    me.pagination.init(page, {idSorteio: idSorteio});
}
