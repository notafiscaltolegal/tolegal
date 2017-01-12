/**
 * Created by lucas-mp on 10/10/14.
 */

function Mensagens(options) {
    var me = this;

    me.build(options);
    me.cadastrarNovaMensagemUsuario();
    me.alteraTelaSeMensagemPublica();
    me.adicionarDestinatario();
    me.removerDestinatario();
    me.filtrarDestinatario();
    me.filtroDestinatario();
    me.alterarMensagemUsuario();
    me.excluirMensagemUsuario();
    me.efetuarAlteracaoMensagem();

    this.mapaDestinatariosAdicionados = {};
}



Mensagens.prototype.build = function(options){
    var me = this;
    if(options != null && options.tela=='usuarioCadastrar' ){
        me.carregaListaDeDestinatariosAdicionados();
    }
    if(options != null && options.tela=='usuarioIndex' ){
        me.gridMensagensUsuario();
    }
    if(options != null && options.tela=='site2' ){
        me.modalMensagens();
        me.fecharModal();
        me.carregaMensagensNaoLidas();
    }

}

Mensagens.prototype.carregaMensagensNaoLidas = function(){
    var me = this;
    $.ajax({
        url: enderecoSite + "/mensagens/mensagensNaoLidasCidadao",
        type: 'POST',
        success:function(response){
            if(response.nrMensagensNovas!=null){
                $("#nrMensagensNovasLink").css("display","inline");
                $("#nrMensagensNovas").html(response.nrMensagensNovas);
            }else{
                $("#nrMensagensNovasLink").css("display","none");
            }
        }
    });
}

Mensagens.prototype.gridMensagensUsuario = function(){
    var me = this;
    me.pagination = new NFGPagination({
        url: enderecoSite + "/portal/mensagens/usuario/listarMensagensCadastradasPeloUsuario",
        containerSelector: "#containerMensagensUsuario",
        templateSelector: "#tabelaMensagensUsuario",
        formFilterSelector: "#formMensagensUsuario",
        btnFilterSelector: "#filtrarMensagensUsuario",
        beforeLoad: function(data) {
            bloqueioDeTela("#dadosMensagensCadastradas");
        },
        afterLoad: function(data) {
            $("#dadosMensagensCadastradas").unblock();
        }

    });
    me.pagination.init(0);
}


Mensagens.prototype.modalMensagens = function() {
    var me = this;
    $("#botaoMensagem").off('click');
    $("#botaoMensagem").click(function(e) {
        $("#modalHomeBody").load( enderecoSite + "/mensagens/viewMensagens",
            function( response, status, xhr ) {}
        );
        $("#modalHomeTitle").html('Minhas Mensagens');
        $(".modal-dialog").attr("class","modal-dialog modal-lg");
        bloqueioDeTela("#divPaineisTelaInicial");
        setTimeout(function () {
            $("#modalHome").modal('show');
            me.gravarLeituraDasMensagens();
            me.listarMensagens();
            $("#divPaineisTelaInicial").unblock();
        }, 1000);
    });

}

Mensagens.prototype.fecharModal = function () {
    var me = this;
    $('#modalHome').on('hidden.bs.modal', function () { //esse modalHome fica em telaInicialSite
        me.carregaMensagensNaoLidas();
    });
}

Mensagens.prototype.gravarLeituraDasMensagens = function() {
    var me = this;
    
    $.ajax({
        url: enderecoSite + "/mensagens/gravarLeituraDasMensagens",
        type: 'POST',
        success:function(response){
        	me.listarMensagens();
        }
    });
}

Mensagens.prototype.listarMensagens = function() {
    var me = this;
    me.pagination = new NFGPagination({
        url: enderecoSite + "/mensagens/listarMensagens",
        containerSelector: "#containerMensagens",
        templateSelector: "#tabelaMensagens",
        beforeLoad: function(data) {
            $("#modalHomeBody").block({
                css: {
                    backgroundColor: 'transparent', color: 'transparent', border: '1'
                },
                overlayCSS:  {
                    backgroundColor: '#000',
                    opacity:         0.2,
                    cursor:          'wait'
                }
            });
        },
        afterLoad: function(data) {
            $("#modalHomeBody").unblock();
        }
    });

    var hash = window.location.hash.replace("#","");
    var page = parseInt(hash) ? parseInt(hash) : 1;
    me.pagination.init(page);

}

Mensagens.prototype.alteraTelaSeMensagemPublica = function(){
    var me = this;
    $("#checkMensagemPublica").click(function(e) {
        if($("#checkMensagemPublica").is(":checked")){
            $("#dadosListaDeDestinatarios").css("display","none");
        }else{
            $("#dadosListaDeDestinatarios").css("display","inline");
        }
    });
}

Mensagens.prototype.cadastrarNovaMensagemUsuario = function(){
    var me = this;
    $("#btnMensagemConcluir").click(function(e) {
        var tipoDestinatario = $("#selectTipoDestinatario").val();
        var mensagemPublica = $("#checkMensagemPublica").is(":checked")? 'S':'N';
        var titulo = $("#tituloMensagem").val();
        var texto = $("#textoMensagem").val()

        if(!(titulo.length > 0)){
            alert("Preencha o campo Título!!");
            return;
        }
        if(!(texto.length > 0)){
            alert("Preencha o campo Texto!!");
            return;
        }

        var mapa = me.mapaDestinatariosAdicionados;

        ajaxBloqueioDeTela("#btnMensagemConcluir",
            enderecoSite+"/portal/mensagens/usuario/efetuarcadastro",
            {
                tipoDestinatario:tipoDestinatario,
                mensagemPublica:mensagemPublica,
                mapDestinatariosString: mensagemPublica=='S'?'':JSON.stringify(mapa),
                titulo:titulo,
                texto:texto
            },
            'POST',
            function(response){
                alert(response.mensagem);
                if(response.success){
                    window.location.replace(enderecoSite+"/portal/mensagens/usuario/index");
                }
            }
        )

    });
}

Mensagens.prototype.filtroDestinatario = function() {
    var me = this;
    $("body").on("keyup", ".campoFiltroDestinatario", function(e) {

        var me = this;
        me.pagination = new NFGPagination({
            url: enderecoSite + "/portal/mensagens/usuario/filtrarDestinatarioMensagemUsuario",
            containerSelector: "#containerFiltroDestinatario",
            templateSelector: "#tabelaFiltroDestinatario",
            formFilterSelector: "#formFiltroDestinatario",
            beforeLoad: function(data) {
                if(data){
                    data.tipoDestinatario = $("#selectTipoDestinatario").val();
                    data.cpfDestinatario = $("#cpfDestinatario").val();
                    data.nomeDestinatario = $("#nomeDestinatario").val();
                    data.cnpjDestinatario = $("#cnpjDestinatario").val();
                    data.nomeFantasiaDestinatario = $("#nomeFantasiaDestinatario").val();
                }
            }

        });
        me.pagination.init(0);

    });
}



Mensagens.prototype.efetuarAlteracaoMensagem = function() {
    var me = this;

    $("body").on("click", "#btnAlterarMensagem", function(e) {
        var titulo = $("#tituloMensagemAlterar").val();
        var texto = $("#textoMensagemAlterar").val();

        if(!(titulo.length > 0)){
            alert("Preencha o campo Título!!");
            return;
        }

        if(!(texto.length > 0)){
            alert("Preencha o campo Texto!!");
            return;
        }

        ajaxBloqueioDeTela("#dadosMensagensCadastradas",
            enderecoSite+"/portal/mensagens/usuario/efetuarAlteracaoMensagem",
            {
                texto:texto,
                titulo:titulo,
                id:$("#idMensagemAlterar").val()
            },
            'POST',
            function(response){
                alert(response.mensagem);
                if(response.success){
                    $("#modalhome").modal('hide');
                    $("#filtrarMensagensUsuario").click();
                }
            }
        );

    });
}

Mensagens.prototype.alterarMensagemUsuario = function() {
    var me = this;
    $("body").on("click", ".alterarMensagemUsuario", function(e) {
        var id = $(this).parent().parent().find('.idMensagemLista').html();
        var texto = $(this).parent().parent().find('.textoMensagemLista').html();
        var titulo = $(this).parent().parent().find('.tituloMensagemLista').html();

        $("#modalHomeBody").load( enderecoSite + "/portal/mensagens/usuario/viewAlterarMensagem",
            function( response, status, xhr ) {}
        );


        $("#modalHomeTitle").html('Alterar Mensagem');
        $(".modal-dialog").attr("class","modal-dialog modal-md");
        setTimeout(function () {
            $("#modalhome").modal('show');
            $("#tituloMensagemAlterar").val(titulo);
            $("#textoMensagemAlterar").val(texto);
            $("#idMensagemAlterar").val(id);
        }, 1000);

    });
}


Mensagens.prototype.excluirMensagemUsuario = function() {
    var me = this;
    $("body").on("click", ".excluirMensagemUsuario", function(e) {

        var id = $(this).parent().parent().find('.idMensagemLista').html();
        var titulo = $(this).parent().parent().find('.tituloMensagemLista').html();
        var publica = $(this).parent().parent().find('.publicaLista').html();
        var confirmado = confirm("Tem certeza que deseja excluir a mensagem "+titulo+" ?");
        if(confirmado){
            ajaxBloqueioDeTela(".excluirMensagemUsuario",
                enderecoSite+"/portal/mensagens/usuario/efetuarExclusaoMensagem",
                {
                    id:id,
                    publica:publica
                },
                'POST',
                function(response){
                    alert(response.mensagem);
                    $("#filtrarMensagensUsuario").click();
                }
            );
        }
    });
}

Mensagens.prototype.adicionarDestinatario = function() {
    var me = this;
    $("body").on("click", ".adicionarDestinatarioLink", function(e) {
        var idPessoaCidadao = $(this).parent().find('.pessoaCidadaoFiltroAdicionar').html();
        var nomeCidadao = $(this).parent().find('.nomeFiltroAdicionar').html();
        var cpfCidadao = $(this).parent().find('.cpfFiltroAdicionar').html();

        var idPessoaEmpresa = $(this).parent().find('.pessoaEmpresaFiltroAdicionar').html();
        var nomeEmpresa = $(this).parent().find('.fantasiaFiltroAdicionar').html();
        var cnpjEmpresa = $(this).parent().find('.cnpjFiltroAdicionar').html();

        if(idPessoaCidadao != null){
            idPessoaCidadao=idPessoaCidadao.trim();
            nomeCidadao=nomeCidadao.trim();
            cpfCidadao=cpfCidadao.trim();
            me.mapaDestinatariosAdicionados[idPessoaCidadao] = {id:idPessoaCidadao,nome:nomeCidadao,cadastro:cpfCidadao};
        }else{
            idPessoaEmpresa=idPessoaEmpresa.trim();
            nomeEmpresa=nomeEmpresa.trim();
            cnpjEmpresa=cnpjEmpresa.trim();
            me.mapaDestinatariosAdicionados[idPessoaEmpresa] = {id:idPessoaEmpresa,nome:nomeEmpresa,cadastro:cnpjEmpresa};
        }

        me.carregaListaDeDestinatariosAdicionados();

    });
}

Mensagens.prototype.removerDestinatario = function() {
    var me = this;
    $("body").on("click", ".removerDestinatario", function(e) {
        var idAdicionado = $(this).parent().parent().find(".idPessoaAdicionado").html().trim();
        delete me.mapaDestinatariosAdicionados[idAdicionado];
        me.carregaListaDeDestinatariosAdicionados();
    });
}


Mensagens.prototype.carregaListaDeDestinatariosAdicionados = function() {
    var me = this;
    var template = Handlebars.compile($("#listaDestinatariosAdicionados").html());
    var html = template({destinatarios:me.mapaDestinatariosAdicionados});
    var $container = $("#containerListaDeDestinatarios");
    $container.html(html);
}

Mensagens.prototype.filtrarDestinatario = function() {
    var me = this;
    $("#btnFiltrarDestinatario").off('click');
    $("#btnFiltrarDestinatario").click(function(e) {
        $("#modalHomeBody").load( enderecoSite + "/portal/mensagens/usuario/adicionarDestinatario",
            function( response, status, xhr ) {}
        );

        $("#modalHomeTitle").html('Adicionar Destinatário');
        $(".modal-dialog").attr("class","modal-dialog modal-sm");
        setTimeout(function () {

            if($("#selectTipoDestinatario").val()=='C'){
                $("#adicionarDestCidadao").css("display","inline");
                $("#adicionarDestEmpresa").css("display","none");
            }else{
                $("#adicionarDestEmpresa").css("display","inline");
                $("#adicionarDestCidadao").css("display","none");
            }


            $("#modalhome").modal('show');
            $('.modal-backdrop').removeClass("modal-backdrop");



        }, 1000);
    });

}