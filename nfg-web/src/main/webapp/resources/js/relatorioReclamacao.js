function RelatorioReclamacao(){
    var me = this;
    me.eventoListar();
}


RelatorioReclamacao.prototype.eventoListar = function(){
    var me = this;
    me.pagination = new NFGPagination({
        url: enderecoSite + "/portal/reclamacao/usuario/listarReclamacoesRelatorio",
        //url: enderecoSite + "/reclamacao/usuario/listarReclamacoesRelatorio",
        containerSelector: "#containerReclamacaoRel",
        templateSelector:  "#tabelaReclamacaoRel",
        formFilterSelector: "#formReclamacaoRel",
    });

    var hash = window.location.hash.replace("#","");
    var page = parseInt(hash) ? parseInt(hash) : 1;
    me.pagination.init(page, {
        data: $("#dataRel").val(),
        dataFim:$("#dataRelFim").val(),
        situacao: $("#situacaoRel").val(),
        motivo: $("#motivoReclamacaoRel").val(),
        tipoDocFisc: $("#tipoDocFiscalReclamacaoRel").val(),
        noPrazo: $("#recNoPrazoRel").val()
    });
}