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
            cadastro:true,
            urlDataLimite: '/contador/contribuintes/data-limite',
            urlCadastrar: '/contador/contribuintes/cadastrar'
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

        var totalElements = responseJson.total;
        var numberOfElements = responseJson.list.length;
        currentPage = parseInt(responseJson.start);

        for(i=0; i< numberOfElements; i++){
            var element = responseJson.list[i];
            var elementList = this.buildElementList(element);
            this.attchElementToTheList(elementList);
        }

        this.buildPaginator(totalElements);
        if(settings.callback) settings.callback();
    }

    this.clearList = function(){
        $(settings.listIdentifier).html("");
    }

    this.buildElementList = function(element){
        var elementList= "<tr id='tr-"+element.numeroDeInscricao+"'>";

        elementList+="<td class='maskInscricao'>";
        elementList+=element[0];
        elementList+="</td>";

        elementList+="<td>";
        elementList+=element[1];
        elementList+="</td>";

        elementList+="<td class='maskCnpj'>";
        elementList+=element[2];
        elementList+="</td>";

        elementList += this.getColunaCadastro(element)

        elementList+="</tr>";

        return elementList;
    };

    this.getColunaCadastro = function(element){
        var elementList="";

        if(element[8] == 'S'){
            elementList+="<td>&nbsp;</td>";
            elementList+="<td>Emitente EFD</td>";
        }else if(element[6] == 'S'){
            elementList+="<td>";
            elementList+=element[7];
            elementList+="</td>";
            elementList+="<td>Emitente já cadastrado. (CNAE Obrigatório)</td>";
        }else if(element[3] == 'S' && element[5] == 'S'){
            elementList+="<td>";
            elementList+=element[4];
            elementList+="</td>";
            elementList+="<td>"+ (element[9] == 'C' ? 'Contribuinte cadastrado pelo contador' : 'Empresa já se cadastrou') +"</td>";
        }else if(element[5] == 'S'){
            elementList+="<td>&nbsp;</td>";
            elementList+="<td><button type='button' data-inscricao='"+element[0]+"' data-url-data-limite='"+settings.urlDataLimite+"' data-url-cadastrar='"+settings.urlCadastrar+"' class='btn btn-default btn-sm' data-cadastro='true' onclick='openForm(this);'>Cadastrar</button></form></td><td>&nbsp;</td>";
        } else{
            elementList+="<td>&nbsp;</td>";
            elementList+="<td>Emitente n&#225;o pode participar</td>";
        }

        return elementList;
    }

    this.attchElementToTheList = function(elementList){
        $(settings.listIdentifier).append(elementList);
    }

    this.buildPaginator = function(totalElementos){
        $(settings.pagingElement).html("");

        var context = this;

        var classOfPrevious = (currentPage >0)? "" : " disabled";
        var classOfNext = ((currentPage + parseInt(settings.elementsPerPage)) < (totalElementos))? "" : " disabled";

        var btnPrevious = $("<li class='previous" + classOfPrevious + "'><a href='#'>&larr; Anterior</a></li>").on("click", function(){
            if(currentPage >0) context.reload(currentPage - parseInt(settings.elementsPerPage));
        });

        $(settings.pagingElement).append(btnPrevious);

        var btnNext = $("<li class='next" + classOfNext + "'><a href='#'>Pr&oacute;ximo &rarr;</a></li>").on("click", function(){
            if((currentPage + parseInt(settings.elementsPerPage)) < (totalElementos)) context.reload(currentPage +parseInt(settings.elementsPerPage));
        });

        $(settings.pagingElement).append(btnNext);
    }

    this.reload = function(start){
        var context = this;
        var params = {start: start, max: settings.elementsPerPage};
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
        var context = this;
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
}

var openForm = function(button){
    var termoDeAcordo="Declaro estar ciente de que o credenciamento no programa -To Legal- me obriga a informar ao cidad&#225;o a possibilidade de incluir o CPF no documento fiscal, bem como transmitir as informações à SEFAZ/TO.";
    var data = {termoDeAcordo:termoDeAcordo, isCadastro:$(button).data('cadastro'), inscricao: $(button).data('inscricao'), enderecoCadastro: enderecoSite+$(button).data('url-cadastrar')};
    nfgModal.showModalCadastro(data);

    var $campo = $('.datepicker');
    var value = $campo.val();
    var dataLimite = null;

    $.ajax({
        url: enderecoSite+$(button).data('url-data-limite'),
        type: 'GET',
        dataType: 'text',
        assync:false,
        data: {inscricao: $(button).data('inscricao')},
        success: function(response){
            dataLimite = JSON.parse(response).dataLimite;
            $campo.datetimepicker({
                value: value,
                formatDate: 'd/m/Y',
                format: 'd/m/Y',
                lang: 'pt',
                timepicker: false,
                minDate: 0,
                maxDate: dataLimite,
                allowBlank: true,
                mask: true
            });
        }
    });


    $(".datepickerIcon").click(function() {
        $campo.datetimepicker('show');
    }).css("top","0").css("cursor","pointer");
};