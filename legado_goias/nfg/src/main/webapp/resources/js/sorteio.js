var Sorteio = function () {
    var me = this;
    me.initDatePicker();
    me.masks();
    me.signSubmit();
    me.signChangeSorteio();
    me.signNovoSorteio();
};
Sorteio.prototype.masks = function () {
    $('#numeroConversao').mask('9?999');
    $('#numeroMaxDocFisc').mask('9?9999');
    $('#numeroLoteria').mask('9?9999');
};

Sorteio.prototype.signNovoSorteio = function () {
    var fieldsSelector = '#fieldsSorteio';
  $('#btnNovoSorteio').click(function () {
      $(fieldsSelector).show();
      $(fieldsSelector).find('input').val('');
      $('#idSorteio').val('0');
      $('#btnCadastrarSorteio').text('CADASTRAR SORTEIO');
      $('#divStatus').hide();
  });
};

Sorteio.prototype.signChangeSorteio = function() {
    $('#idSorteio').change(function (e) {
        if (this.value == '0') {
            $('#fieldsSorteio').hide();
        } else {
            $.ajax({
                url: enderecoSite + "/admin/sorteio/" + this.value,
                success: function (regraSorteio) {
                    $('#idSorteio').val(regraSorteio.id);
                    $('#informacao').val(regraSorteio.informacao);
                    $('#dataExtracaoLoteria').val(regraSorteio.dataExtracaoLoteriaStr);
                    $('#numeroLoteria').val(regraSorteio.numeroLoteria);
                    $('#dataRealizacao').val(regraSorteio.dataRealizacaoStr);
                    $('#numeroConversao').val(regraSorteio.numeroConversao);
                    $('#numeroMaxDocFisc').val(regraSorteio.numeroMaxDocFisc);
                    $('#dataLimiteCadastroPessoa').val(regraSorteio.dataLimiteCadastroPessoaStr);
                    $('#status').val(regraSorteio.statusStr);
                    $('#btnCadastrarSorteio').text('ATUALIZAR SORTEIO');
                    $('#divStatus').show();
                    $('#fieldsSorteio').show();
                }
            });
        }
    });
};

Sorteio.prototype.signSubmit = function () {
    $('#btnCadastrarSorteio').on('click', function (e) {
        e.preventDefault();
        var data = serializeFormToObject($('#formCadastroSorteio'));
        $.ajax({
            url: enderecoSite + '/admin/cadastro/sorteio',
            type: 'POST',
            data: data,
            success: function(response) {
                nfgMensagens.show(ALERT_TYPES.SUCCESS, response);
            }
        });
    });
};

Sorteio.prototype.initDatePicker = function() {
    $(".datepicker").each(function() {
        var $campo = $(this);
        if (isIE()) {
            $campo.on('keydown', function() {
                return false;
            });
        }
        $campo.datetimepicker({
            formatDate: 'd-m-Y',
            format: 'd/m/Y',
            lang: 'pt',
            timepicker: false,
            allowBlank: true,
            mask: false,
            closeOnDateSelect: true,
            yearStart:1900,
            validateOnBlur: true
        });

        $(".datepickerIcon").click(function() {
            $campo.datetimepicker('show');
        }).css("top","0").css("cursor","pointer");
    });
};