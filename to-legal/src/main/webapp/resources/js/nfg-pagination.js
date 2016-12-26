/**
 * @author henrique-rh
 * Para funcionar, o conte&#250;do deve estar em um template handlebars.
 * E deve usar o helper: {{paginate pagination}}
 * em que: (Exemplo) pagination = {
 *     total: 99
 *     page: 1
 *     max: 10
 * }
 * @param options Contendo pelo menos :
 * url (do conteudo paginado retornando json)
 * templateSelector template handlebars
 * @constructor
 */
function NFGPagination(options) {
    if (!options.url) {
        throw "Informe url!";
    }
    if (!options.templateSelector) {
        throw "Informe o id do template handlebars";
    }
    var defaults = {
        containerSelector: "#container",
        btnFilterSelector: "#filter",
        formFilterSelector: "form"
    };
    options = _.defaults(options, defaults);
    
    this.url = options.url;
    this.$container = $(options.containerSelector);
    this.template = Handlebars.compile($(options.templateSelector).html());
    this.btnFilterSelector = options.btnFilterSelector;
    this.formFilterSelector = options.formFilterSelector;

    if (options.beforeLoad && _.isFunction(options.beforeLoad)) {
        this.beforeLoad = options.beforeLoad;
    }

    if (options.afterLoad && _.isFunction(options.afterLoad)) {
        this.afterLoad = options.afterLoad;
    }
    this.signFilter();
}

NFGPagination.prototype.init = function(page, data) {
    this.loadPage(page || 1, data);
};

NFGPagination.prototype.signFilter = function() {
    var me = this;
    // filter on enter
    $(me.formFilterSelector + " input").off("keydown");
    $(me.formFilterSelector + " input").keydown(function(e) {
       if (e.keyCode == 13) {
           e.preventDefault();
           e.stopImmediatePropagation();
           me.filter();
       }
    });

    // filter on click btn filter
    $(me.btnFilterSelector).off("click");
    $(me.btnFilterSelector).click(function(e) {
        e.preventDefault();
        me.filter();
    });

    // filter on reset form
    $("[type='reset']").off("click");
    $("[type='reset']").click(function(e) {
        e.preventDefault();
        $(me.formFilterSelector + ' input').each(function() {
            $(this).val("");
        });
        $(me.formFilterSelector + ' select').each(function() {
            var $options = $(this).find('option[default="default"]');
            if (_.isEmpty($options)) {
                $options = $(this).find('option:first');
            }
            $options.prop('defaultSelected', true);
        });
        me.filter();
    });
};

NFGPagination.prototype.filter = function() {
    var data = serializeFormToObject($(this.formFilterSelector));
    // only not empty values
    data = _.pick(data, function(value, key) {
        return value != '';
    });
    this.loadPage(1, data);
};

NFGPagination.prototype.loadPage = function(page, data) {
    var me = this;
    if (page) {
        page = page > 0 ? page - 1 : 0;
    }
    if (me.beforeLoad) {
        if (!data)
            data = {};
        me.beforeLoad(data);
    }
    $.ajax({
        url: me.url + "/" + page,
        data: data,
        type: 'POST',
        success: function(response) {
            if (page > 0) document.location.hash = page + 1;
            var html = me.template(response);
            me.$container.html(html);
            me.bindPagination(data);
            if(me.afterLoad) me.afterLoad(data);
        }
    });
};

NFGPagination.prototype.bindPagination = function(data) {
    var me = this;
    me.$container.off('click', '.pagination a');
    me.$container.on("click", ".pagination a", function() {
        var page = $(this).data("page");
        if (page) {

            me.loadPage(page, data);
        }
    });
};