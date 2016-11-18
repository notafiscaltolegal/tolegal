/**
 * Created by Letícia Álvares on 31/05/2016.
 */

function Ecf(options) {
    var self = this;
    this.inscricao = options.inscricaoEstadual;
    this.urlBase = options.urlBase;
    this.retificadora = options.retificadora;
    $(":file").filestyle({buttonText: "Procurar arquivo"});

    self.btnImportarECF();

    document.onkeydown=function(e){
        var evt = e || window.event;
        if(evt.keyCode=='13'){
            $(".btn-submitform").click();
        }
    }

    this.mensagensErro = null;
    this.mensagensAlerta = null;

}

Ecf.prototype.btnImportarECF = function(){
    var self= this;

     $("body").on("click", "#btnImportarECF", function(e) {

         $(".linhaTxt").remove();
         $(".linhaTxtAlertas").remove();

         if($('#fileECF').val()!=null && $('#fileECF').val().length>0){
             var ext = $('#fileECF').val().split('.').pop().toLowerCase();
             if($.inArray(ext, ['zip']) == -1) {
                 alert("Extens&#225;o inválida! O arquivo deve estar compactado com extens&#225;o .zip");
                 //Pq o codigo abaixo nao tá funcionando? Depois eu vejo
                 //nfg-webMensagens.show(ALERT_TYPES.ERROR, "Extens&#225;o inválida! O arquivo deve estar compactado com extens&#225;o .zip",true);
                 return;
             }
         }

        var formData = new FormData($("#frmImportaECF")[0]);
        $.ajax({
            url: enderecoSite + '/contribuinte/ecf/upload/'+self.inscricao,
            data: formData,
            type: 'POST',
            cache: false,
            contentType: false,
            processData: false,

            success: function (response) {
                if(response.error!=null){
                    nfgMensagens.show(ALERT_TYPES.ERROR,response.error,true);
                }else{
                    self.mensagensErro = response.mensagensErro;
                    self.mensagensAlerta = response.mensagensAlerta;
                    self.retificadora =  response.retificadora;

                    if (self.mensagensErro!= null){
                        if (self.mensagensErro.length > 0)
                        {
                            $("#containerMensagensErro").css("display","block");
                        }
                    }
                    else  $("#confirmarImportacao").css("display","block");

                    if (self.mensagensAlerta!= null){
                        if (self.mensagensAlerta.length > 0)
                        {
                            $("#containerMensagensAlerta").css("display","block");
                        }
                    }

                    $(".linhaTxt").remove();
                    _.forEach(response.mensagensErro, function (mensagensErro) {
                        self.appendMensagemErro(mensagensErro);
                    });

                    $(".linhaTxtAlertas").remove();
                    _.forEach(response.mensagensAlerta, function (mensagensAlerta) {
                        self.appendMensagemAlerta(mensagensAlerta);
                    });
                }
            }

        });

    });
}

Ecf.prototype.btnConfirmaImportacao = function(){
    var self= this;

    $("body").on("click", "#btnConfirmaImportacao", function(e) {
        var formData = new FormData($("#frmImportaECF")[0]);
        $.ajax({
            url: enderecoSite + '/contribuinte/ecf/finalizaUpload/' + self.inscricao,
            data: formData,
            type: 'POST',
            cache: false,
            contentType: false,
            processData: false,

            success: function (response) {
                if(response.error!=null){
                    nfgMensagens.show(ALERT_TYPES.ERROR,response.error,true);
                }else{
                    nfgMensagens.show(ALERT_TYPES.ERROR,"Importaç&#225;o finalizada com sucesso",true);
                }
            }
        });
    });
}

Ecf.prototype.appendMensagemErro = function (mensagensErro) {
    var me = this;
    $("#containerMensagensErro table tr").first().after(
        "<tr class='linhaTxt' style:'padding:10' data-id='"+mensagensErro.mensagem+"'>" +
        "<td>"+mensagensErro.mensagem+"</td>" +
        "</tr>"
    );
};

Ecf.prototype.appendMensagemAlerta = function (mensagensAlerta) {
    var me = this;
    $("#containerMensagensAlerta table tr").first().after(
        "<tr class='linhaTxtAlertas' style:'padding:10' data-id='"+mensagensAlerta.mensagem+"'>" +
        "<td>"+mensagensAlerta.mensagem+"</td>" +
        "</tr>"
    );
};













