/**
 * Created by diogo-rs on 7/15/2014.
 */
$(document).ready(function () {
    $.paginate({
        url: enderecoSite + '/contador/contribuintes/list',
        searchable: true,
        searchInput: '.input-search-contribuinte-nao-participante',
        searchButton: '.btn-search-contribuinte-nao-participante',
        resetSearchButton: '.btn-reset-search-contribuinte-nao-participante',
        callback: function(){
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
        }
    });

/*
    $.paginate({
        url: enderecoSite + '/contador/contribuintes/list-participantes',
        pagingElement: "#paging-participantes",
        listIdentifier: ".list-paginated-participantes",
        cadastro: false,
        searchable: true,
        searchInput: '.input-search-contribuinte-participante',
        searchButton: '.btn-search-contribuinte-participante',
        resetSearchButton: '.btn-reset-search-contribuinte-participante'
    });
*/
});

function cadastrarContribuinte(botao) {
    var inscricao = $(botao).data("inscricao");
    var idParent = $(botao).data("parent");
    var row = $(idParent).clone().attr("id", "");
    $(".list-paginated-participantes").append(row);
    $(idParent).remove();
}
