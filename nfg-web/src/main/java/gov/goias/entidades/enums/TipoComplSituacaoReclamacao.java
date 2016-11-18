package gov.goias.entidades.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas-mp on 15/10/15.
 */
public enum TipoComplSituacaoReclamacao {

    CIDADAO_CRIOU_RECLAMACAO(1,"Criar Reclamaç&#225;o","Reclamaç&#225;o criada pelo cidad&#225;o e encaminhada ao sistema para busca automática."),
    SISTEMA_ACHOU_AUTOMATICAMENTE(2,"Busca automática achou: finalizar","Documento fiscal encontrado automaticamente pelo sistema."),
    SISTEMA_NAO_ACHOU_AUTOMATICAMENTE(3,"Busca automática n&#225;o achou: encaminhar para empresa","Documento fiscal n&#225;o encontrado automaticamente pelo sistema. Aguardando resposta da empresa."),
    EMPRESA_EVIOU_NOVO_ARQUIVO(4,"Enviar novo arquivo do documento fiscal","Empresa alegou transmissÃ£o de novo arquivo de documento fiscal. Aguardando sistema."),
    EMPRESA_RECUSOU_RECLAMACAO(5,"Recusar Reclamaç&#225;o do cidad&#225;o","Empresa n&#225;o concordou com a Reclamaç&#225;o. Aguardando resposta do cidad&#225;o."),
    EMPRESA_ALEGOU_ERRO_NFG(6,"Alegar erro do sistema","Empresa alegou erro do sistema. Aguardando resposta do usuÃ¡rio gestor do programa."),
    SISTEMA_ACHOU_NOVO_ARQUIVO(7,"Busca de novo arquivo achou: finalizar","Sistema achou novo arquivo enviado pela empresa."),
    SISTEMA_NAO_ACHOU_NOVO_ARQUIVO(8,"Busca de novo arquivo n&#225;o achou: notificar empresa","Sistema n&#225;o achou novo arquivo enviado pela empresa. Aguardando resposta do usuÃ¡rio gestor do programa."),
    CIDADAO_CONCORDOU_COM_EMPRESA(9,"Concordar com resposta da empresa e finalizar Reclamaç&#225;o","CidadÃ£o concordou com a empresa."),
    CIDADAO_NAO_CONCORDOU_COM_EMPRESA(10,"Recusar resposta da empresa","CidadÃ£o n&#225;o concordou com a empresa. Aguardando resposta do usuÃ¡rio gestor do programa."),
    CIDADAO_CANCELOU_RECLAMACAO(11,"Cancelar a Reclamaç&#225;o","Reclamaç&#225;o cancelada pelo cidad&#225;o."),
    USUARIO_NOTIFICOU_ERRO_EMPRESA(12,"Acionar fiscalizaç&#225;o e notificar empresa","UsuÃ¡rio gestor do programa acionou fiscalizaç&#225;o e notificou empresa."),
    USUARIO_NOTIFICOU_ERRO_CIDADAO(13,"Notificar cidad&#225;o que Reclamaç&#225;o n&#225;o procede","UsuÃ¡rio gestor do programa notificou cidad&#225;o."),
    SISTEMA_ACHOU_ARQ_COM_CPF_INCONDIZENTE(14,"Busca de novo arquivo encontrou Documento com CPF de outra pessoa: encaminhar para usuÃ¡rio","Busca de novo arquivo encontrou Documento com CPF de outra pessoa. Aguardando resposta do usuÃ¡rio gestor do programa."),
    SISTEMA_FINALIZOU_RECLAMACAO(15,"Finalizar Reclamaç&#225;o","Reclamaç&#225;o finalizada pelo sistema."),
    EMPRESA_ESTA_DENTRO_DO_PRAZO(16,"Marcar reclamaç&#225;o como dentro do prazo legal para recebimento","A empresa ainda está dentro do prazo legal para envio do documento reclamado.");

    private int value;
    private String descricao;
    private String acao;

    TipoComplSituacaoReclamacao(int value, String acao, String descricao) {
        this.descricao = descricao;
        this.value = value;
        this.acao = acao;
    }

    public static TipoComplSituacaoReclamacao get(int value) {
        for (TipoComplSituacaoReclamacao status : TipoComplSituacaoReclamacao.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }
    public String getDescricao() {
        return descricao;
    }
    public String getAcao() {
        return acao;
    }

    public static List<TipoComplSituacaoReclamacao> getList(){
        List<TipoComplSituacaoReclamacao> lista = new ArrayList<TipoComplSituacaoReclamacao>();
        TipoComplSituacaoReclamacao situacao;

        int aux =0;
        while (true) {
            aux++;
            situacao = get(aux);
            if (situacao==null) break;
            lista.add(situacao);
        };

        return lista;
    }
}
