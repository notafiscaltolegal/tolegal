var Bilhete = function () {
    this.signAdd();
    this.signRemove();
    this.listaBilhetes = [];
    this.setMasks();
    this.signChangeSorteio();
    this.telaImportarTxt();
    this.botaoImportarTxt();
    this.confirmarImportacao();

    $("#filetxt").filestyle();
    $(":file").filestyle({input: false});
    this.bilhetesPremiados = null;

};

Bilhete.prototype.confirmarImportacao = function () {
    var me = this;
    $("body").on('click', '#btnConfirmaImportacao',function (e) {
        if(me.bilhetesPremiados!=null && me.bilhetesPremiados.length>0){
            var sorteio = $('#numSorteio').val();
            var formData = new FormData($("#formImportaArquivoResultado")[0]);
            $.ajax({
                //url: enderecoSite + '/bilhete/confirmarImportacao/'+sorteio,
                url: enderecoSitePortal + '/bilhete/confirmarImportacao/'+sorteio,
                data: formData,
                type: 'POST',
                cache: false,
                contentType: false,
                processData: false,
                success: function(response) {
                    if (response.success!=null){
                        nfgMensagens.show(ALERT_TYPES.SUCCESS,response.success);
                        me.loadBilhetesPremiados(sorteio);
                    }else{
                        nfgMensagens.show(ALERT_TYPES.ERROR,response.error);
                    }
                }
            });
            $("#modalHome").modal('hide');
        }else{
            nfgMensagens.show(ALERT_TYPES.ERROR,"A importaç&#225;o n&#225;o obteve êxito. Tente importar o arquivo novamente antes de confirmar.",true);
        }
    });
}

Bilhete.prototype.botaoImportarTxt = function () {
    var me = this;

    $("body").on('change', '#filetxt',function (e) {
        //$('#filetxt').change(function(){
        var file = this.files[0];
        var name = file.name;
        var size = file.size;
        var type = file.type;
        //Your validation
    });

    $("body").on('click', '#btnImportarTxtResultado',function (e) {
        var sorteio = $('#numSorteio').val();
        var formData = new FormData($("#formImportaArquivoResultado")[0]);
        $.ajax({
            //url: enderecoSite + '/bilhete/upload/'+sorteio,
            url: enderecoSitePortal + '/bilhete/upload/'+sorteio,
            data: formData,
            type: 'POST',
            cache: false,
            contentType: false,
            processData: false,

            success: function (response) {
                if(response.error!=null){
                    nfgMensagens.show(ALERT_TYPES.ERROR,response.error,true);
                }else{
                    $("#hashmd5").val(response.hashMD5Arquivo);
                    $("#divHash").css("display","block");
                    me.bilhetesPremiados = response.bilhetesPremiados;

                    $(".linhaTxt").remove();
                    _.forEach(response.bilhetesPremiados, function (bilhete) {
                        me.appendBilhetePremiado(bilhete);
                    });

                    nfgMensagens.show(ALERT_TYPES.SUCCESS,response.success,true);
                }
            }
        });
    });
};

Bilhete.prototype.appendBilhetePremiado = function (bilhete) {
    var me = this;
    $("#containerBilhetesPremiadosTxt table tr").first().after(
        "<tr class='linhaTxt' data-id='"+bilhete.iteracao+"'>" +
        "<td>"+bilhete.iteracao+"</td>" +
        "<td>"+bilhete.premio+"</td>" +
        "<td>"+bilhete.bilhete+"</td>" +
        "<td>"+bilhete.possuiObs+"</td>" +
        "<td>"+bilhete.valor+"</td>" +
        "<td>"+bilhete.nomePessoa+"</td>" +
        "<td>"+bilhete.cpfPessoa+"</td>" +
        "</tr>"
    );
};

Bilhete.prototype.telaImportarTxt = function () {
    var me = this;
    $('#importarTxtResultado').click(function (e) {

        $("#messagesContainerModal").html(""); //limpando mensagens anteriores

        var sorteio = $('#numSorteio').val();
        var sorteioText = $('#numSorteio option:selected').text();
        if (sorteio==null || sorteio.length == 0){
            nfgMensagens.show(ALERT_TYPES.ERROR, "Selecione um sorteio!");
        }else{

            $("#modalHomeBody").load('importarTxtResultado'
                ,function (response, status, xhr) {}
            );

            $("#modalHomeTitle").html('Importar arquivo TXT do Resultado do sorteio '+sorteioText);
            $(".modal-dialog").attr("class", "modal-dialog modal-lg");

            setTimeout(function () {
                $("#filetxt").filestyle({buttonText: "Procurar arquivo"});
                $("#modalHome").modal('show');
            },1000)

        }

    });
};

Bilhete.prototype.setMasks = function () {
    $('#numBilhete').mask('9?999999999999');
    $('#numeroPremio').mask('9?999999999999');
};

Bilhete.prototype.loadBilhetesPremiados = function (idSorteio) {
    var me = this;
    $.ajax({
        url: enderecoSitePortal + '/bilhete/lista/' + idSorteio,
        //url: enderecoSite + '/bilhete/lista/' + idSorteio,
        success: function (response) {
            $(".linha").remove();
            _.forEach(response, function (bilhete) {
                me.appendBilhete(bilhete);
            });
        }
    });
};

Bilhete.prototype.loadPremios = function (idSorteio) {
    var $premio = $('#premio');
    if (_.isUndefined(idSorteio) || idSorteio == '') {
        $premio.attr('disabled', true);
        $premio.html('<option value="" >Selecione um Sorteio</option>');
        return;
    } else {
        $premio.removeAttr('disabled');
    }
    $.ajax({
        url: enderecoSitePortal + '/bilhete/premios/' + idSorteio,
        //url: enderecoSite + '/bilhete/premios/' + idSorteio,
        success: function (response) {
            var html = "<option value=''>Selecione um prêmio</option>";
            _.forEach(response, function (premio) {
                html += "<option value='"+premio.id+"'>"+premio.valorFormatado+"</option>"
            });
            $premio.html(html);
        }
    })
};

Bilhete.prototype.signChangeSorteio = function () {
    var me = this;
    $('#numSorteio').on('change', function () {
        me.loadPremios(this.value);
        me.loadBilhetesPremiados(this.value);
    });
};

Bilhete.prototype.signAdd = function () {
    var me = this;
    $('#bilhete-form').submit(function (e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        me.addBilhete();
    });

    $('.btn-salvar').click(function (e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        me.addBilhete();
    });
};

Bilhete.prototype.addBilhete = function () {
    var me = this;
    var formSelector = "#bilhete-form";
    var form = $(formSelector);
    var formParams = serializeFormToObject(form);
    if (_.isUndefined(formParams.numSorteio) || _.isEqual(formParams.numSorteio, '')) {
        nfgMensagens.show(ALERT_TYPES.WARN, 'Selecione um sorteio');
        return;
    }
    if (_.isUndefined(formParams.numBilhete) || _.isEqual(formParams.numBilhete, '')) {
        nfgMensagens.show(ALERT_TYPES.WARN, 'Preencha o campo n&#250;mero bilhete');
        return;
    }
    if (_.isUndefined(formParams.numeroPremio) || _.isEqual(formParams.numeroPremio, '')) {
        nfgMensagens.show(ALERT_TYPES.WARN, 'Preencha o campo n&#250;mero prêmio');
        return;
    }
    $.ajax({
        url: enderecoSitePortal + '/bilhete/novoPremio/',
        //url: enderecoSite + '/bilhete/novoPremio/',
        data: {numeroBilhete: formParams.numBilhete, idRegra: formParams.numSorteio, numeroPremio: formParams.numeroPremio, idPremio: formParams.premio},
        success: function (response) {
            if (_.contains(me.listaBilhetes, response.ID_PREMIO_BILHETE)) {
                nfgMensagens.show(ALERT_TYPES.ERROR, "Bilhete já inserido");
            } else {
                me.appendBilhete(response);
            }
        }
    });
    $(formSelector + ' input').each(function() {
        $(this).val("");
    });
    $('#premio').each(function() {
        var $options = $(this).find('option[default="default"]');
        if (_.isEmpty($options)) {
            $options = $(this).find('option:first');
        }
        $options.prop('defaultSelected', true);
    });
    $('#numBilhete').val('');
    $('#numeroPremio').val('');
};
//TODO numeroPremio e premio from sql ou dto
Bilhete.prototype.appendBilhete = function (bilhete) {
    var me = this;
    me.listaBilhetes.push(bilhete.ID_PREMIO_BILHETE);
    $("#containerBilhetesPremiados table tr").first().after("<tr class='linha' data-id='"+bilhete.ID_PREMIO_BILHETE+"'>" +
    "<td>"+bilhete.NUM_SORTEIO+"</td>" +
    "<td>"+bilhete.NUM_BILHETE+"</td>" +
    "<td>"+bilhete.NUMERO_PREMIO+"</td>" +
    "<td>"+bilhete.VALOR_PREMIO+"</td>" +
    "<td>"+toCpfFormat(bilhete.CPF)+"</td>" +
    "<td>"+bilhete.NOME_PESSOA+"</td>" +
    "<td>"+bilhete.MUNICIPIO+"</td>" +
    "<td>"+bilhete.UF+"</td>" +
    "<td>"+bilhete.BAIRRO+"</td>" +
    "<td style='width:10px'><button type='button' class='close excluir' aria-label='Remover' style='color: red'><span aria-hidden='true'>&times;</span></button></td>" +
    "</tr>");
};




Bilhete.prototype.signRemove = function () {
    var me = this;
    $('#containerBilhetesPremiados table').on('click', '.linha .excluir', function () {
        var id = $(this).parents('tr').data('id');
        $(this).parents('tr').remove();
        debugger;
        _.pull(me.listaBilhetes, id);
        //me.reorganize();
        $.ajax({
            url: enderecoSitePortal + '/bilhete/remove/' + id,
            //url: enderecoSite + '/bilhete/remove/' + id,
            success: function (response) {
                console.log(response);
            }
        });
    });
};

Bilhete.prototype.reorganize = function () {
    var i = 1;
    $(".indexBilhete").each(function () {
        $(this).text(i++);
    });
};
