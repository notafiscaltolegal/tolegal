/**
 * Created by Remisson Silva on 26/09/2014.
 */
//---------------------------------------------------------------------------->
// Generic Modal
//---------------------------------------------------------------------------->
function GenericModal(title,content,buttons)
{
    this.title   = title;
    this.content = content;
    this.buttons = buttons;

    this.build();
}

GenericModal.prototype.build = function()
{
    var sourceModalGenerico = $("#modal-generico").html();
    this.modalTemplate = Handlebars.compile(sourceModalGenerico);

    this.modal = this.modalTemplate({
        title:this.title,
        content: this.content,
        buttons: this.buttons
    });
};

GenericModal.prototype.open  = function()
{
    $(this.modal).modal();
};

GenericModal.prototype.close = function()
{
    $(".modalGenericoHandlebars").removeClass('modal-backdrop');
    $(".modalGenericoHandlebars").modal('hide');

    //$(".modal-backdrop").remove();
    //$(".modal").remove();
    //$("#modalGenericoHandlebars").modal('hide');
    //    $("#modalGenericoHandlebars").hide();
    //$("#modalGenericoHandlebars").modal('hide');
};