/**
 * Created by diogo-rs on 7/15/2014.
 */
(function($){
    $.paginate = function(options){
        var context = this;

        var defaults = {
            url: "/",
            params: {},
            pagingElement: '#paging',
            cadastro:true
        };

        var settings = $.extend(defaults, options);
        settings.params['max'] = settings.elementsPerPage || 5;
        $.ajax({
            url: settings.url,
            type: 'GET',
            dataType: 'text',
            data: settings.params,
            context: context,
            success: function(response){
                new ResponseProcessor(settings).proccess(response);
            }
        });
    }
})(jQuery);

function ResponseProcessor(options){
    var currentPage = 0;
    var currentPageTotalOfElements=0;
    var currentTotalOfElements=0;
    var context = this;
    var currentSearch="";

    var defaults = {
        elementsPerPage: 5,
        listIdentifier: ".list-paginated"
    };

    var settings = $.extend(defaults, options);

    if(settings.searchable){
        $(settings.searchInput).on('keydown', function (e) {
            // Allow: backspace, delete, tab, escape, enter and .
            if ($.inArray(e.keyCode, [46, 8, 9, 27, 13]) !== -1 ||
                // Allow: Ctrl+A, Ctrl+C, Ctrl+v
                ((e.keyCode == 65 || e.keyCode == 67 || e.keyCode == 86) && e.ctrlKey === true) ||
                // Allow: home, end, left, right
                (e.keyCode >= 35 && e.keyCode <= 39)) {
                // let it happen, don't do anything
                return;
            }
            // Ensure that it is a number and stop the keypress
            if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
                e.preventDefault();
            }
        });

        var context = this;
        $(settings.searchButton).on("click", function(){
            context.reload(0, $(settings.searchInput).val());
        });

        $(settings.resetSearchButton).on("click", function(){
            context.reload(0);
            $(settings.searchInput).val('');
        })
    }

    this.proccess = function(response){
        var responseJson = JSON.parse(response);
        this.buildPaginetedList(responseJson);
    }

    this.buildPaginetedList = function(responseJson){
        this.clearList();

        currentTotalOfElements = responseJson.total;
        currentPage = parseInt(responseJson.start);
        currentPageTotalOfElements = responseJson.list.length;

        for(i=0; i< currentPageTotalOfElements; i++){
            var element = responseJson.list[i];
            var elementList = this.buildElementList(element);
            this.attchElementToTheList(elementList);
        }

        this.buildPaginator(currentTotalOfElements);
    }

    this.clearList = function(){
        $(settings.listIdentifier).html("");
    }

    this.buildElementList = function(element){
        var elementList= "<tr id='tr-"+element.idCnaeAutorizado+"' data-cnae='"+element.idCnaeAutorizado+"'>";

        elementList+="<td>";
        elementList+=element.subClasseCnae.codSubClasseCnae;
        elementList+="</td>";

        elementList+="<td>";
        elementList+=element.subClasseCnae.descSubClasseCnae;
        elementList+="</td>";

        elementList+="<td id='data-obrigatoriedade-"+element.idCnaeAutorizado+"'>";
        elementList+=element.dataObrigatoriedadeFormatada;
        elementList+="</td>";

        elementList+="</tr>";

        var tdEdit = $(this.buildTdEdit(element));
        var tdExcluir = $(this.buildTdExcluir(element));

        var newElement = $(elementList);
        newElement.append(tdEdit);
        newElement.append(tdExcluir);

        return newElement;
    }

    this.buildTdEdit = function(element){
        var idTdData = "#data-obrigatoriedade-"+element.idCnaeAutorizado;
        var tdEdit="<td class='center'></td>";
        var input;

        var htmlBtnEditar = "<button class='btn btn-default' type='button'>Editar</button>";

        var clickSalvar = function(event){
            event.preventDefault();
            var dataAtual = input.val();

            $.ajax({
                url:enderecoSite+'/portal/cnae/update',
                type: 'POST',
                data: {cnae: element.idCnaeAutorizado, data: dataAtual},
                context: this,
                success: function(response){
                    $(idTdData).html(dataAtual);
                    $(this).text("Editar");
                    $(this).unbind('click', clickSalvar);
                    $(this).click(clickEditar);
                }
            });


        };

        var clickEditar = function(event){
            event.preventDefault();
            var dataAtual = $(idTdData).text();
            $.ajax({
                url:enderecoSite+'/portal/cnae/ha-contribuintes-cadastrados',
                type: 'POST',
                data: {cnae: element.subClasseCnae.idSubClasseCnae},
                context: this,
                success: function(response){
                    if(response.success) {
                        if(!response.possuiContribuintes || (response.possuiContribuintes && confirm("Existema contribuintes cadastrados para esse cnae, deseja alterar mesmo assim?"))) {

                            $(idTdData).html('');
                            input = $("<input value='" + dataAtual + "'>");
                            $(input).mask("99/99/9999");
                            $(idTdData).append(input);
                            $(this).text("Salvar");
                            $(this).unbind('click', clickEditar);
                            $(this).click(clickSalvar);
                        }
                    }
                }
            });
        };

        var btnEdit = $(htmlBtnEditar).on("click", clickEditar);

        var newTd = $(tdEdit);
        newTd.append(btnEdit);

        return newTd;
    }

    this.buildTdExcluir = function(element){
        var tdExcluir="<td class='center'></td>";

        var formExcluir = "<form action='/to-legal/portal/cnae/excluir-cnae' method='post'>";
        formExcluir+="<input type='hidden' name='idCnaeAutorizadoDel' value='"+element.idCnaeAutorizado+"'>";
        formExcluir+="<button class='btn btn-danger' type='submit'>Excluir</button>";
        formExcluir+="</form>";

        var newForm = $(formExcluir).on("submit", function(event){
            event.preventDefault();
            $.ajax({
                url:enderecoSite+'/portal/cnae/ha-contribuintes-cadastrados',
                type: 'POST',
                data: {cnae: element.subClasseCnae.idSubClasseCnae},
                context: this,
                success: function(response){
                    if(response.success) {
                        if(!response.possuiContribuintes || (response.possuiContribuintes && confirm("Existema contribuintes cadastrados para esse cnae, deseja excluir mesmo assim?"))) {
                            context.deleteElement("#tr-"+element.idCnaeAutorizado);
                        }
                    }
                }
            });
        });

        var newTd = $(tdExcluir);
        newTd.append(newForm);

        return newTd;
    }

    this.attchElementToTheList = function(elementList){
        $(settings.listIdentifier).append(elementList);
    }

    this.buildPaginator = function(totalElementos){
        $(settings.pagingElement).html("");

        var classOfPrevious = (currentPage >0)? "" : " disabled";
        var classOfNext = ((currentPage + parseInt(settings.elementsPerPage)) < (totalElementos))? "" : " disabled";

        var btnPrevious = $("<li class='previous" + classOfPrevious + "'><a href='#'>&larr; Anterior</a></li>").on("click", function(){
            if(currentPage >0) context.reload(currentPage - parseInt(settings.elementsPerPage), currentSearch);
        });

        $(settings.pagingElement).append(btnPrevious);

        var btnNext = $("<li class='next" + classOfNext + "'><a href='#'>Pr&oacute;ximo &rarr;</a></li>").on("click", function(){
            if((currentPage + parseInt(settings.elementsPerPage)) < (totalElementos)) context.reload(currentPage +parseInt(settings.elementsPerPage), currentSearch);
        });

        $(settings.pagingElement).append(btnNext);
    }

    this.reload = function(start){
        var params = {start: start, max: settings.elementsPerPage, query: currentSearch};
        $.ajax({
            url: settings.url,
            type: 'GET',
            dataType: 'text',
            data: params,
            context: context,
            success: function(response){
                this.proccess(response);
            }
        });
    }

    this.reload = function(start, searchFor){
        currentSearch = searchFor;
        var params = {start: start, max: settings.elementsPerPage, query: searchFor};
        $.ajax({
            url: settings.url,
            type: 'GET',
            dataType: 'text',
            data: params,
            context: context,
            success: function(response){
                this.proccess(response);
            }
        });
    }

    this.deleteElement = function(element){
        $.ajax({
            url:enderecoSite+'/portal/cnae/excluir-cnae',
            type: 'POST',
            data: {idCnaeAutorizadoDel: $(element).data('cnae')},
            success: function(response){
                if(response.success){
                    $(element).remove();
                    var deveRecarregarNovaPagina = $(settings.listIdentifier).children("tr").length <= 0 && currentPage > 0;

                    if(deveRecarregarNovaPagina){
                        context.reload(currentPage - parseInt(settings.elementsPerPage));
                    } else{
                        context.reload(currentPage)
                    }
                }
            }
        });

    }
}