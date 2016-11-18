var Processamento = function () {
    this.signRefresh();
    this.signUpdateAll();
    this.signDetalhes();
    this.signPopover();
    this.loadCountsAnimated();
    this.signApplyTooltip();
    this.signSort();
    this.signUpdateLote();
    this.template = Handlebars.compile($("#tabelaLotes").html());
    this.$containerModal = $('#containerLotes');
    this.fields = {
        EFD :                      {selector: '#tipoEfd',                   attr: {tipoOrigemDocumentoFiscal: 'EFD'},                       params: {tipo: '3'}},
        MFD :                      {selector: '#tipoMfd',                   attr: {tipoOrigemDocumentoFiscal: 'MFD'},                       params: {tipo: '2'}},
        DIGITADA :                 {selector: '#tipoDigitada',              attr: {tipoOrigemDocumentoFiscal: 'DIGITADA'},                  params: {tipo: '4'}},
        NFE :                      {selector: '#tipoNfe',                   attr: {tipoOrigemDocumentoFiscal: 'NFE'},                       params: {tipo: '1'}},
        PROCESSADO :               {selector: '#statusProcessado',          attr: {statusProcessamentoLote: 'PROCESSADO'},                  params: {status: '1'}},
        AGUARDANDO_VALIDACAO :     {selector: '#statusAguardandoValidacao', attr: {statusProcessamentoLote: 'AGUARDANDO_VALIDACAO_NOTAS'},  params: {status: '6'}},
        NAO_PROCESSADO :           {selector: '#statusNaoProcessado',       attr: {statusProcessamentoLote: 'NAO_PROCESSADO'},              params: {status: '3'}},
        NAO_VALIDADO :             {selector: '#statusNaoValidado',         attr: {statusProcessamentoLote: 'NAO_VALIDADO'},                params: {status: '2'}},
        AGUARDANDO_PROCESSAMENTO : {selector: '#statusAguardandoProcess',   attr: {statusProcessamentoLote: 'AGUARDANDO_PROCESSAMENTO'},    params: {status: '4'}},
        EM_PROCESSAMENTO :         {selector: '#statusEmProcess',           attr: {statusProcessamentoLote: 'EM_PROCESSAMENTO'},            params: {status: '5'}}
    };
};

Processamento.prototype.showToast = function (content) {
    var options =  {
        content: content,
        style: "toast",
        timeout: 3000
    };
    $.snackbar(options);
};

Processamento.prototype.signUpdateLote = function () {
    var me = this;
    $('.processamento').on('click', '.btn-update', function () {
        var $tr = $(this).parents('tr');
        var id = $(this).data('id');
        var action = $(this).data('action');
        $.ajax({
            url: enderecoSite + '/admin/processamento/' + action,
            data: {idLote: id},
            success: function (response) {
                $tr.hide(150, function (){
                    me.showToast(response);
                    $(this).remove();
                });
            }
        });
    });
};

Processamento.prototype.signSort = function () {
    var me = this;
    $('.processamento').on('click', '.th-sort', function () {
        var field = $(this).data('field');
        var direction = $(this).find('span').hasClass('mdi-navigation-arrow-drop-down') ? 'asc' : 'desc';
        var lotes = me.modalData.lotes;
        me.modalData.lotes = _.sortByOrder(lotes, field, direction);
        me.refreshTableLotes(me.modalData);
        $('.th-sort span').removeClass('mdi-navigation-arrow-drop-down').removeClass('mdi-navigation-arrow-drop-up');
        $('[data-field="'+field+'"]').find('span').addClass(direction == 'asc' ? 'mdi-navigation-arrow-drop-up' : 'mdi-navigation-arrow-drop-down');
    });
};

Processamento.prototype.signApplyTooltip = function () {
    $("[rel='tooltip']").tooltip();
    $('.modal').on('shown.bs.modal', function () {
        $("[rel='tooltip']").tooltip();
    });
};

Processamento.prototype.signPopover = function () {
    $('.processamento').on('click', '.popoverErro', function () {
        $(this).popover();
    });
};

Processamento.prototype.refreshTableLotes = function (options) {
    var me = this;
    me.modalData = options;
    var html = me.template(options);
    me.$containerModal.html(html);
};

Processamento.prototype.signDetalhes = function() {
    var me = this;
    $('#statusNaoProcessado').click(function () {
        $.ajax({
            url: enderecoSite + '/admin/processamento/lotes',
            data: {status: me.fields.NAO_PROCESSADO.params.status},
            success: function (lotes) {
                $('#detalheProcessamento').modal();
                me.refreshTableLotes({lotes: lotes, showReprocessar: true});
            }
        });
    });
    $('#statusNaoValidado').click(function () {
        $.ajax({
            url: enderecoSite + '/admin/processamento/lotes',
            data: {status: me.fields.NAO_VALIDADO.params.status},
            success: function (lotes) {
                $('#detalheProcessamento').modal();
                me.refreshTableLotes({lotes: lotes, showRevalidar: true});
            }
        });
    });
};

Processamento.prototype.sumCommons = function (lotes, attr) {
    return _.sum(_.filter(lotes, attr), function (lote) {
        return lote.total;
    });
};

Processamento.prototype.loadCounts = function (type, cb) {
    var me = this;
    $.ajax({
        url: enderecoSite + '/admin/processamento/lotes/count',
        data: type ? me.fields[type].params : {},
        success: function (lotes) {
            console.log(lotes);
            var updateField = function (lotes, field) {
                $(field.selector).text(numeral(me.sumCommons(lotes, field.attr)).format('0,0'));
            };
            if (type) {
                var field = me.fields[type];
                updateField(lotes, field);
            } else {
                _.forEach(me.fields, function (field) {
                    updateField(lotes, field);
                });
            }
            if (cb) cb();
        }
    });
};

Processamento.prototype.loadCountsAnimated = function ($btn) {
    var me = this;
    var canStop = false;
    var $icons = $btn || $('.mdi-navigation-refresh');
    $icons.toggleClass('rotating');
    var it = setInterval(function () {
        if (canStop) {
            $icons.toggleClass('rotating');
            clearInterval(it);
        }
    }, 1000);
    me.loadCounts($btn ? $btn.data('type') : undefined, function () {
        canStop = true;
    });
};

Processamento.prototype.signRefresh = function () {
    var me = this;
    $('.mdi-navigation-refresh').click(function (e) {
        me.loadCountsAnimated($(this));
    });
};

Processamento.prototype.signUpdateAll = function () {
    var me = this;
    $('#updateAll').click(function (e) {
       me.loadCountsAnimated();
    });
};


