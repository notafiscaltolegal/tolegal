function Premiacao(options){
    var me = this;
    me.build(options);

}


Premiacao.prototype.build = function(options){
    var me = this;

    me.selecionaSorteio();
    me.editaDataResgate();
    me.editaInfoResgate();


    $(".datepicker").each(function() {
        me.initDatePicker($(this));
    });

    //if($("#selectSorteios").val().length<=0){
    //    $("#selectSorteios").val(1);
    //    me.gridPremiacaoUsuario(1);
    //}

}



Premiacao.prototype.editaDataResgate = function(){
    var me = this;
    $("body").on('change', '.dataResgateEdit',function (e) {
        var elementoDataResgate = $(this).parent().find('.dataResgateEdit').find('input');
        var dataResgate = elementoDataResgate.val().trim();

        var idPremioBilhete = $(this).parent().find('.idPremioBilheteEdit').html().trim();

        if (dataResgate==null || dataResgate.length==0){
            me.gridPremiacaoUsuario($("#selectSorteios").val());
            return;
        }else{

            ajaxBloqueioDeTela(".dataResgateEdit",
                enderecoSite + "/portal/premiacao/usuario/editarPremiacao",
                //enderecoSite + "/premiacao/usuario/editarPremiacao",
                {dataResgate:dataResgate,
                    infoResgate:null,
                    idPremioBilhete:idPremioBilhete},
                'POST',
                function(response) {
                    if(response.sucesso){
                        nfgMensagens.show(ALERT_TYPES.SUCCESS, "Data de resgate alterada!");
                        me.gridPremiacaoUsuario($("#selectSorteios").val());
                    }else{
                        nfgMensagens.show(ALERT_TYPES.ERROR, response.erro);
                    }
                }
            );

        }
    });

}


Premiacao.prototype.editaInfoResgate = function(){
    var me = this;
    $("body").on('change', '.infoResgateEdit',function (e) {
        var elemento = $(this).parent().find('.infoResgateEdit').find('input');
        var infoResgate = elemento.val().trim();

        var idPremioBilhete = $(this).parent().find('.idPremioBilheteEdit').html().trim();

        if (infoResgate==null){
            me.gridPremiacaoUsuario($("#selectSorteios").val());
            return;
        }else{

            ajaxBloqueioDeTela(".infoResgateEdit",
                enderecoSite + "/portal/premiacao/usuario/editarPremiacao",
                //enderecoSite + "/premiacao/usuario/editarPremiacao",
                {dataResgate:null,
                    infoResgate:infoResgate,
                    idPremioBilhete:idPremioBilhete},
                'POST',
                function(response) {
                    if(response.sucesso){
                        nfgMensagens.show(ALERT_TYPES.SUCCESS, "Info de resgate alterada!");
                        me.gridPremiacaoUsuario($("#selectSorteios").val());
                    }else{
                        nfgMensagens.show(ALERT_TYPES.ERROR, response.erro);
                    }
                }
            );

        }
    });

}

Premiacao.prototype.selecionaSorteio = function(){
    var me = this;
    $("#selectSorteios").on("change", function(e) {
        me.gridPremiacaoUsuario(this.value);
    });

}

Premiacao.prototype.gridPremiacaoUsuario = function(sorteio){
    var me = this;
    me.pagination = new NFGPagination({
        url: enderecoSite + "/portal/premiacao/usuario/listarPremiacoes",
        //url: enderecoSite + "/premiacao/usuario/listarPremiacoes",
        containerSelector: "#containerPremiacoesUsuario",
        templateSelector: "#tabelaPremiacoesUsuario",
        formFilterSelector: "#formPremiacoesUsuario",

        beforeLoad: function() {
            bloqueioDeTela("#dadosPremiacao");
        },
        afterLoad: function() {

            $(".inputDate").mask("99/99/9999");
            $("#dadosPremiacao").unblock();
        }
    });


    var hash = window.location.hash.replace("#","");
    var page = parseInt(hash) ? parseInt(hash) : 1;

    me.pagination.loadPage(page, {sorteio: sorteio});
    //me.pagination.init(page);

}

Premiacao.prototype.initDatePicker = function($campo) {
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
        maxDate: 0,
        mask: true,
        closeOnDateSelect: true,
        yearStart:1900,
        validateOnBlur: false
    });

    $(".datepickerIcon").click(function() {
        $campo.datetimepicker('show');
    }).css("top","0").css("cursor","pointer");
};