/**
 * Created by thiago-mb on 22/07/2014.
 */

$(document).ready(function() {
    $.paginate({
        url: enderecoSite + '/portal/cnae/list-cnaes-autorizados',
        pagingElement: "#paging-cnaes",
        listIdentifier: ".list-paginated",
        cadastro: false,
        searchable: true,
        searchInput: '.input-search-cnae-autorizado',
        searchButton: '.btn-search-cnae-autorizado',
        resetSearchButton: '.btn-reset-search-cnae-autorizado',
        elementsPerPage: 10
    });

    /*INICIALIZADO SECOES*/
    $.ajax({
        url: enderecoSite + "/portal/cnae/list-secao",
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
    });

    $("#secao select").change("change", function() {
        var me = this;
        $.ajax({
            url: enderecoSite + "/portal/cnae/list-divisao",
            data: {idSecao: me.value},
            success: function(resposta) {
                var html = '';
                //necessario limpar os options
                $('#selectDivisao').find('option').remove();
                html = '<option value=\"\">Selecione..</option>';
                $('#selectGrupo').find('option').remove();
                $('#selectGrupo').append(html);
                $('#selectClasse').find('option').remove();
                $('#selectClasse').append(html);
                $('#selectSubClasse').find('option').remove();
                $('#selectSubClasse').append(html);
                //limpando o campo que exibe a ultima subclasse selecionada
                $("#idSubClasseSelecionada").attr('value', '');
                $.each(resposta.listaDivisao, function(index, divisao) {
                    html += '<option value="' + divisao.idDivisaoCnae + '">' + divisao.codDivisaoCnae + ' - '+ divisao.descDivisaoCnae + '</option>';
                });
                $('#selectDivisao').append(html);
                //chamando funcao para exibiç&#225;o da combo seguinte
                subClasseSelecionada("divisao");
            }
        });
    });
    $("#divisao select").change("change", function() {
        var me = this;

        $.ajax({
            url: enderecoSite + "/portal/cnae/list-grupo",
            data: {idDivisao: me.value},
            success: function(resposta) {
                var html = '';
                var htmlTodos = '';
                $('#selectGrupo').find('option').remove();
                html = '<option value=\"\">Selecione..</option>';
                $('#selectGrupo').append(html);
                $('#selectClasse').find('option').remove();
                $('#selectClasse').append(html);
                $('#selectSubClasse').find('option').remove();
                $('#selectSubClasse').append(html);
                $("#idSubClasseSelecionada").attr('value', '');
                html = '';
                $.each(resposta.listaGrupo, function(index, grupo) {
                    html += '<option value="' + grupo.idGrupoCnae + '">' + grupo.codGrupoCnae + ' - ' + grupo.descGrupoCnae+ '</option>';
                });
                htmlTodos = '<option value="todos">Incluir Todos</option>';
                $('#selectGrupo').append(htmlTodos + html);
                //chamando funcao para exibiç&#225;o da combo seguinte
                subClasseSelecionada("grupo");
            }
        });
    });

    $("#grupo select").change("change", function() {
        if(this.value == 'todos') return;

        var me = this;
        $.ajax({
            url: enderecoSite + "/portal/cnae/list-classe",
            data: {idGrupo: me.value},
            success: function(resposta) {
                var html = '';
                var htmlTodos = '';
                $('#selectClasse').html("");
                html = '<option value=\"\">Selecione..</option>';
                $('#selectClasse').append(html);
                $('#selectSubClasse').html("");
                $('#selectSubClasse').append(html);
                $("#idSubClasseSelecionada").attr('value', '');
                html = '';
                $.each(resposta.listaClasse, function(index, classe) {
                    html += '<option value="' + classe.idClasseCnae + '">' + classe.codClasseCnae +  ' - ' + classe.descClasseCnae +'</option>';
                });
                htmlTodos = '<option value="todos">Todos</option>';
                $('#selectClasse').append(htmlTodos + html);
                //chamando funcao para exibiç&#225;o da combo seguinte
                subClasseSelecionada("classe");
            }
        });
    });

    $("#classe select").change("change", function() {
        if(this.value == 'todos') return;

        var me = this;
        $.ajax({
            url: enderecoSite + "/portal/cnae/list-subclasse",
            data: {idClasse: me.value},
            success: function(resposta) {
                var html = '';
                var htmlTodos = '';
                $('#selectSubClasse').find('option').remove();
                html = '<option value=\"\">Selecione..</option>';
                $('#selectSubClasse').append(html);
                html = '';
                $.each(resposta.listaSubClasse, function(index, subclasse) {
                    html += '<option value="' + subclasse.idSubClasseCnae + '">' + subclasse.codSubClasseCnae + ' - ' + subclasse.descSubClasseCnae + '</option>';
                });
                htmlTodos = '<option value="todos">Todos</option>';
                $('#selectSubClasse').append(htmlTodos + html);
                //chamando funcao para exibiç&#225;o da combo seguinte
                subClasseSelecionada("subclasse");
            }
        });
    });
});

$("form[name='cnae']").submit(function(event){
    if($('#selectGrupo').val()!= 'todos' && selectVazio($('#selectClasse'))){
        nfgMensagens.show(ALERT_TYPES.ERROR, "Selecione uma classe.");
        return false;
    } else if($('#selectGrupo').val()!= 'todos' && $('#selectClasse').val()!= 'todos' && selectVazio($('#selectSubClasse'))){
        nfgMensagens.show(ALERT_TYPES.ERROR, "Selecione uma subclasse");
        return false;
    } else if(selectVazio($('#input-data-obrigatoriedade'))){
        nfgMensagens.show(ALERT_TYPES.ERROR, "Informe a data de obrigatoriedade");
        return false;
    }

    return true;
});

function selectVazio(select){
    return select.val() == null || select.val() == "";
}

var subclasse = "";
function subClasseSelecionada(id){
    //verificando o id enviado para que seja exibido ao usuário
    if (id == "lastCombo"){
        subclasse = $("#selectSubClasse :selected").text();
        var subClasseSelecionado = document.getElementById("subClasseSelecionada");
        subClasseSelecionado.innerText = subclasse;
        //setando o hidden para enviar o id da Subclasse selecionada
        var idSubClasseSelecionada = $("#selectSubClasse :selected").val();
        $("#idSubClasseSelecionada").attr('value', idSubClasseSelecionada);
    }else {
        //caso n&#225;o seja a ultima combo(subclasse) o campo subClasseSelecionada será limpada
        var subClasseSelecionado = document.getElementById("subClasseSelecionada");
        subClasseSelecionado.innerText = '';
    }

    return false;
}