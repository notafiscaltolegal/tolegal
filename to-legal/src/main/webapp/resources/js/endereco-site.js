/**
 * Created by diogo-rs on 7/17/2014.
 */
function EnderecoSite(){

    this.enderecoSite = function(){
        var protocol = location.protocol;
        var host = location.host;
        if(/localhost:8080$/.test(host) || /localhost:8443$/.test(host) || /.*sefazhomolog\.\intra\.goias\.gov\.br.*/.test(host) || /.*portalsefaz.*/.test(host)){
            return protocol+"//"+host+"/to-legal";
        }

        return protocol+"//"+host;
    }

    this.paginaAtual = function(){
        var protocol = location.protocol;
        return protocol+"//"+location.host+location.pathname;
    }
}