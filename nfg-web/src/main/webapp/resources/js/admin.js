function Admin(suggestions) {
    this.initEditor(suggestions);
    this.signExecute();
}

Admin.prototype.initEditor = function(suggestions) {
    this.editor = ace.edit('editor');
    var langTools = ace.require("ace/ext/language_tools");
    this.editor.getSession().setMode("ace/mode/sql");
    this.editor.setShowPrintMargin(false);
    this.editor.setOptions({
        enableBasicAutocompletion: true
    });
    var database = {
        getCompletions: function(editor, session, pos, prefix, callback) {
            callback(null, suggestions);
        }
    };
    langTools.addCompleter(database);
};

Admin.prototype.signExecute = function() {
    var me = this;
    $("#executar").click(function() {
        if (
            _.contains(me.editor.getValue().toLowerCase(), 'delete') ||
            _.contains(me.editor.getValue().toLowerCase(), 'update') ||
            _.contains(me.editor.getValue().toLowerCase(), 'insert')
        ) {
            var c = confirm("Vc irá executar update/insert/delete, tem certeza?");
            if (c == true) {
                me.executeQuery();
            }
        } else {
            me.executeQuery();
        }
    });
};

Admin.prototype.executeQuery = function() {
    var me = this;
    $("#executar").attr('disabled', 'disabled');
    $.post(enderecoSite + '/portal/admin/executar', {q: me.editor.getValue()}, function(response) {
        $('.response-container').html(response);
        $("#executar").removeAttr('disabled');
    });
};
