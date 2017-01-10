package gov.goias.entidades.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas-mp on 15/10/15.
 */
public enum TipoComplSituacaoReclamacao {

    CIDADAO_CRIOU_RECLAMACAO(1,"Criar Reclamação","Reclamação criada pelo cidadão e encaminhada ao sistema para busca automática."),
    SISTEMA_ACHOU_AUTOMATICAMENTE(2,"Busca automática achou: finalizar","Documento fiscal encontrado automaticamente pelo sistema."),
    SISTEMA_NAO_ACHOU_AUTOMATICAMENTE(3,"Busca automática não achou: encaminhar para empresa","Documento fiscal não encontrado automaticamente pelo sistema. Aguardando resposta da empresa."),
    EMPRESA_EVIOU_NOVO_ARQUIVO(4,"Enviar novo arquivo do documento fiscal","Empresa alegou transmissÃ£o de novo arquivo de documento fiscal. Aguardando sistema."),
    EMPRESA_RECUSOU_RECLAMACAO(5,"Recusar Reclamação do cidadão","Empresa não concordou com a Reclamação. Aguardando resposta do cidadão."),
    EMPRESA_ALEGOU_ERRO_NFG(6,"Alegar erro do sistema","Empresa alegou erro do sistema. Aguardando resposta do usuÃ¡rio gestor do programa."),
    SISTEMA_ACHOU_NOVO_ARQUIVO(7,"Busca de novo arquivo achou: finalizar","Sistema achou novo arquivo enviado pela empresa."),
    SISTEMA_NAO_ACHOU_NOVO_ARQUIVO(8,"Busca de novo arquivo não achou: notificar empresa","Sistema não achou novo arquivo enviado pela empresa. Aguardando resposta do usuÃ¡rio gestor do programa."),
    CIDADAO_CONCORDOU_COM_EMPRESA(9,"Concordar com resposta da empresa e finalizar Reclamação","CidadÃ£o concordou com a empresa."),
    CIDADAO_NAO_CONCORDOU_COM_EMPRESA(10,"Recusar resposta da empresa","CidadÃ£o não concordou com a empresa. Aguardando resposta do usuÃ¡rio gestor do programa."),
    CIDADAO_CANCELOU_RECLAMACAO(11,"Cancelar a Reclamação","Reclamação cancelada pelo cidadão."),
    USUARIO_NOTIFICOU_ERRO_EMPRESA(12,"Acionar fiscalização e notificar empresa","UsuÃ¡rio gestor do programa acionou fiscalização e notificou empresa."),
    USUARIO_NOTIFICOU_ERRO_CIDADAO(13,"Notificar cidadão que Reclamação não procede","UsuÃ¡rio gestor do programa notificou cidadão."),
    SISTEMA_ACHOU_ARQ_COM_CPF_INCONDIZENTE(14,"Busca de novo arquivo encontrou Documento com CPF de outra pessoa: encaminhar para usuÃ¡rio","Busca de novo arquivo encontrou Documento com CPF de outra pessoa. Aguardando resposta do usuÃ¡rio gestor do programa."),
    SISTEMA_FINALIZOU_RECLAMACAO(15,"Finalizar Reclamação","Reclamação finalizada pelo sistema."),
    EMPRESA_ESTA_DENTRO_DO_PRAZO(16,"Marcar reclamação como dentro do prazo legal para recebimento","A empresa ainda está dentro do prazo legal para envio do documento reclamado.");

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
