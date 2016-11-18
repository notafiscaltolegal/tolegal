/**
 * Responsável por fornecer uma instância do módulo associado à view de Totais Cadastrados.
 */
var TotaisCadastrados = function () {
    var me = this;
    me.initBotaoPesquisar();
    $(".datepicker").each(function() {
        me.initDatePicker($(this));
    });
};

/**
 * Função responsável por inicializar os campos de seleção de data.
 *
 * @param $campo o campo a ser inicializado
 */
TotaisCadastrados.prototype.initDatePicker = function($campo) {
    if (isIE()) {
        $campo.on('keydown', function() {
            return false;
        });
    }
    $campo.datetimepicker({
        formatDate: 'd/m/Y',
        format: 'd/m/Y',
        lang: 'pt',
        timepicker: false,
        allowBlank: true,
        maxDate: 0,
        mask: true,
        closeOnDateSelect: true,
        yearStart:1900,
        validateOnBlur: false
    });

    $campo.next(".datepickerIcon").click(function() {
        $campo.datetimepicker('show');
    }).css("top","0").css("cursor","pointer");
};

/**
 * Função responsável por inicializar o botão de pesquisa de totais cadastrados por dia.
 */
TotaisCadastrados.prototype.initBotaoPesquisar = function () {
    var me = this;
    $("#botaoPesquisarTotaisPorDia").on("click", function () {
        var regexData = /^\d{2}([./-])\d{2}\1\d{4}$/;
        var valorInputDataInicio = regexData.test(me.getDataInicio()) ? me.getDataInicio() : null;
        var valorInputDataFim = regexData.test(me.getDataFim()) ? me.getDataFim() : null;
        var parametros = {
            dataInicio: valorInputDataInicio,
            dataFim: valorInputDataFim
        };

        var url = enderecoSite + "/portal/totaisCadastrados/totaisCadastradosPorDia";
        ajaxBloqueioDeTela(".totais-cadastrados-dia", url, parametros, "get", function (resposta) {
            $(".lista-totais-dia tbody tr:not(.modelo-row-total-dia, .row-tabela-vazia)").remove();
            if (null !== resposta && !_.isEmpty(resposta)) {
                me.getLinhaNenhumResultado().css("display", "none");
                $.each(resposta, function (idx, itemDia) {
                    var novaLinha = $(".modelo-row-total-dia").clone(true);
                    novaLinha.removeAttr("style").removeClass("modelo-row-total-dia");
                    novaLinha.find(".totais-cadastrados-dia").text(itemDia.DATA_CADASTRO);
                    novaLinha.find(".totais-cadastrados-valor").text(itemDia.CADASTRADOS);
                    $(".lista-totais-dia").find("tbody").append(novaLinha);
                });
            } else {
                me.getLinhaNenhumResultado().removeAttr("style");
            }
        });

        return false;
    });
};

/**
 * Função responsável por obter a data inicial do período a ser pesquisado na tela de totais cadastrados.
 */
TotaisCadastrados.prototype.getDataInicio = function () {
    return $(".datepicker[name=dataInicio]").val();
};

/**
 * Função responsável por obter a data final do período a ser pesquisado na tela de totais cadastrados.
 */
TotaisCadastrados.prototype.getDataFim = function () {
    return $(".datepicker[name=dataFim]").val();
};

/**
 * Função responsável por obter, na tabela de totais por dia, a linha "Nenhum resultado".
 */
TotaisCadastrados.prototype.getLinhaNenhumResultado = function () {
    return $(".lista-totais-dia").find(".row-tabela-vazia");
};