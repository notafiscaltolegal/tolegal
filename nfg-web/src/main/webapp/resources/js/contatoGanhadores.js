/**
 * Created by bruno-cff on 02/12/2015.
 */
function ContatoGanhadores() {
    var me = this;
    me.eventoListar();
    me.modalContatoEmDetalhe();
}

ContatoGanhadores.prototype.eventoListar = function(){
    var me = this;


    me.pagination = new NFGPagination({
        url: enderecoSite + '/portal/contato-ganhadores/contatoGanhadores/listarContatos',
        //url: enderecoSite + '/contato-ganhadores/contatoGanhadores/listarContatos',
        containerSelector: "#containerContatoGanhadores",
        templateSelector: "#tabelaContatoGanhadores"
    });

    var seletorCombo = "#selectSorteios";
    $(seletorCombo).on("change", function(e) {
        me.pagination.loadPage(page, {idSorteio: this.value});
    });

    var hash = window.location.hash.replace("#","");
    var page = parseInt(hash) ? parseInt(hash) : 1;
    me.pagination.init(page);
}

ContatoGanhadores.prototype.modalContatoEmDetalhe = function() {
    var me = this;
    $("body").on("click", ".btn-contato", function(e){
        var idpessoa = $(this).attr('data-idpessoa');
        $("#modalHomeBody").load(enderecoSite + '/portal/contato-ganhadores/contatoGanhadores/viewContatoDetalhe',
        //$("#modalHomeBody").load(enderecoSite + '/contato-ganhadores/contatoGanhadores/viewContatoDetalhe',
            function( response, status, xhr ) {
                if ( status == "error" ) {
                    window.location.replace(enderecoSite+"/cidadao/login");
                }
            }
        );
        $("#modalHomeTitle").html('Consultar contato em detalhe');
        $(".modal-dialog").attr("class","modal-dialog modal-lg"); //modal largo
        bloqueioDeTela("#divTelaPrincipal");
        setTimeout(function () {
            $("#modalHome").modal('show');
            me.listarContatosDetalhe(idpessoa);
            $("#divTelaPrincipal").unblock();
        }, 1000);
    });
}

ContatoGanhadores.prototype.listarContatosDetalhe = function(idpessoa){
    var me = this;

    me.pagination = new NFGPagination({
        url: enderecoSite + '/portal/contato-ganhadores/contatoGanhadores/listarContatoDetalhe',
        //url: enderecoSite + '/contato-ganhadores/contatoGanhadores/listarContatoDetalhe',
        containerSelector: "#containerContato",
        templateSelector: "#tabelaContatoDetalhe"
    });

    var hash = window.location.hash.replace("#","");
    var page = parseInt(hash) ? parseInt(hash) : 1;
    me.pagination.init(page, {idPessoa : idpessoa});
}
