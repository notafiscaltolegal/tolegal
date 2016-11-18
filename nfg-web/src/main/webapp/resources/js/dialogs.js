/**
 * Created by Remisson Silva on 26/09/2014.
 */

function ConfirmModal(title,content,buttonOk,buttonCancel)
{
    this.title        = title;
    this.content      = content;
    this.buttonOk     = buttonOk;
    this.buttonCancel = buttonCancel;

    this.buildButtons(this.buttonOk,this.buttonCancel);
    this.build();
}

ConfirmModal.prototype.build = function()
{
    this.genericModal = new GenericModal(this.title,this.content,this.buttons);
};

ConfirmModal.prototype.buildButtons = function(buttonOk,buttonCancel)
{
    this.buttons = "";
    if(this.buttonOk){
        this.buttons += "<div>"+ this.buttonOk +"</div>";
    }
    if(this.buttonCancel){
        this.buttons += "<div>"+ this.buttonCancel +"</div>";
    }
};

ConfirmModal.prototype.open = function()
{
    this.genericModal.open();
};

ConfirmModal.prototype.close = function ()
{
    this.genericModal.close();
};