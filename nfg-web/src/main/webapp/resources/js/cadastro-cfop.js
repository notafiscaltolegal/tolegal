/**
 * Created by thiago-mb on 22/07/2014.
 */

$(document).ready(function() {
    var pagination = new NFGPagination({
        url: enderecoSite + "/cadastro-cfop/list-cfops-autorizados/",
        containerSelector: "#containerCfopsAutorizados",
        templateSelector: "#tabelaCFOPs",
        formFilterSelector: "#filtro",
        btnFilterSelector: ".btn-search-cfop-autorizado"

    });
    pagination.init(1);
    /*INICIALIZADO CFOPS
    $.ajax({
        url: enderecoSite + "/cadastro-cfop/list-cfop",
        success: function(resposta) {
            var html = '';
            //necessario limpar os options
            $('#selectSecao').find('option').remove();
            $('#selectSecao').append("<option value=''>Selecione..</option>");

            $.each(resposta.listaSecao, function(index, secao) {
                html += '<option value="' + secao.idSecaoCnae + '">' + secao.codSecaoCnae + ' - '+ secao.descSecaoCnae + '</option>';
            });
            $('#selectSecao').append(html);
        }
    });*/



});

