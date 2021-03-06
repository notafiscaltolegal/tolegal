Handlebars.registerHelper('paginate', function(pagination) {
    var total = pagination.total;
    var max = pagination.max;
    var page = pagination.page;
    var prev = page - 1;
    var next = page + 1;
    var numMaxPages = 10;

    var qtdPages = total < max ? 1 : total % max == 0 ? total/max : (total/max) + 1;
    qtdPages = parseInt(qtdPages);
    if(qtdPages>1){
        var html = "<ul class='pagination pagination-sm'>";

        if(prev <= 0) {
            html += "<li class='disabled'><a>" + "&laquo;" + "</a></li>";
        } else {
            html += "<li><a data-page='"+prev+"' href='#"+prev+"' >" + "&laquo;" + "</a></li>";
        }
        var initLeft = 1;
        if (qtdPages >= numMaxPages) {
            initLeft = page - (numMaxPages/2);
            initLeft = initLeft < 1 ? 1 : initLeft;
            if (initLeft > 1) {
                html += "<li><a data-page='"+1+"' href='#"+1+"' >" + 1 + " </a></li>";
                html += "<li class='disabled'><a>...</a></li>";
            }
        }
        var lastPage = 1;
        for (var i = 1; initLeft <= qtdPages && i <= numMaxPages; i++, initLeft++, lastPage = initLeft){
            if(initLeft == page) {
                html += "<li class='active'><a>" + initLeft + " </a></li>";
            } else {
                html += "<li><a data-page='"+initLeft+"' href='#"+initLeft+"' >" + initLeft + " </a></li>";
            }
        }
        if (qtdPages >= numMaxPages) {
            if (qtdPages > lastPage - 1) {
                html += "<li class='disabled'><a>...</a></li>";
                html += "<li><a data-page='"+qtdPages+"' href='#"+qtdPages+"' >" + qtdPages + " </a></li>";
            }
        }

        if(next > qtdPages) {
            html += "<li class='disabled'><a>" + "&raquo;" + "</a></li>";
        } else {
            html += "<li><a data-page='"+next+"' href='#"+next+"' >" + "&raquo;" + "</a></li>";
        }

        html += "</ul>";
    }else{
        html="";
    }
    return new Handlebars.SafeString(html);
});

Handlebars.registerHelper('compare', function(lvalue, rvalue, options) {

    if (arguments.length < 3)
        throw new Error("Handlerbars Helper 'compare' needs 2 parameters");

    operator = options.hash.operator || "==";

    var operators = {
        '==':       function(l,r) { return l == r; },
        '===':      function(l,r) { return l === r; },
        '!=':       function(l,r) { return l != r; },
        '<':        function(l,r) { return l < r; },
        '>':        function(l,r) { return l > r; },
        '<=':       function(l,r) { return l <= r; },
        '>=':       function(l,r) { return l >= r; },
        'typeof':   function(l,r) { return typeof l == r; }
    };

    if (!operators[operator])
        throw new Error("Handlerbars Helper 'compare' doesn't know the operator "+operator);

    var result = operators[operator](lvalue,rvalue);

    if( result ) {
        return options.fn(this);
    } else {
        return options.inverse(this);
    }

});

Handlebars.registerHelper("mesAnoDate", function(i){
    //pega a data em formato int e retorna no padr�o 99/9999
    var mesAno = i.toString();
    var mes = mesAno.substring(4, 6);
    var ano = mesAno.substring(0 , 4);
    mesAno.subs
    var mesAnoFormatado = mes +"/"+ ano;
    return mesAnoFormatado;
});



Handlebars.registerHelper("prettifyDate", function(timestamp) {
    //return new Date(timestamp).toLocaleDateString();
    var prettified = new Date(timestamp);
    var dd = prettified.getDate();
    var mm = prettified.getMonth()+1; //January is 0!
    var yyyy = prettified.getFullYear();

    if(dd<10) {
        dd='0'+dd
    }

    if(mm<10) {
        mm='0'+mm
    }

    prettified = dd+'/'+mm+'/'+yyyy;
    return prettified;
});

Handlebars.registerHelper("prettifyDateIfNotNull", function(timestamp) {

    if (timestamp==null) {
        return "";
    }
    //return new Date(timestamp).toLocaleDateString();
    var prettified = new Date(timestamp);
    var dd = prettified.getDate();
    var mm = prettified.getMonth()+1; //January is 0!
    var yyyy = prettified.getFullYear();

    if(dd<10) {
        dd='0'+dd
    }

    if(mm<10) {
        mm='0'+mm
    }

    prettified = dd+'/'+mm+'/'+yyyy;
    return prettified;
});

Handlebars.registerHelper("intSeparadoPorMilhar", function(i) {
    //funcao nao recomendada para numeros flutuantes
    return i.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
});

Handlebars.registerHelper("maskCpf", function(cpf) {
    return toCpfFormat(cpf);
});

Handlebars.registerHelper("maskCnpj", function(cnpj) {
    return toCnpjFormat(cnpj);
});


Handlebars.registerHelper("maskMoney", function(value) {
    var element = document.createElement("input");
    $(element).css("display", "none");
    document.body.appendChild(element);
    $(element).val(value);
    $(element).maskMoney();
    var masked = $(element).val();
    document.body.removeChild(element);
    return "R$ " + masked;
});


Handlebars.registerHelper("maskMoneyVirgula", function(value) {
    return toMoneyFormat(value==null?value:value.toFixed(2));
});