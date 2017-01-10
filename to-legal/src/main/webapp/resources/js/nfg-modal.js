/**
 * @author henrique-rh
 * @since 22/07/2014
 */

function NFGModal() {
    var sourceModalCadastro = $("#modalCadastro").html();
    this.templateModalCadastro = Handlebars.compile(sourceModalCadastro);
}

NFGModal.prototype.showModalCadastro = function(data, cb) {
    var modal = this.templateModalCadastro(data);
    $(".modal").remove();
    $(modal).modal();
    //.modal() sempre adiciona esse padding no body, movendo a página.
    $("body").css("padding-right","0");

    if (cb) cb();
};

var nfgModal = new NFGModal();