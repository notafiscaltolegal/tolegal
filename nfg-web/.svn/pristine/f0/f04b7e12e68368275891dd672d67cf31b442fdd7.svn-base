package gov.goias.services;

import gov.goias.entidades.DocumentoFiscalReclamado;
import gov.goias.entidades.GENPessoaFisica;
import gov.goias.entidades.GENPessoaJuridica;
import gov.goias.entidades.PessoaParticipante;
import gov.goias.entidades.enums.TipoComplSituacaoReclamacao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by lucas-mp on 24/03/15.
 */

@Service
public class NotificacaoService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DocumentoFiscalReclamado documentoFiscalReclamadoRepo;

    private  Logger logger = LoggerFactory.getLogger(NotificacaoService.class);

    private String sqlMensagem;
    private String sqlDestinatario;

    public void notificaEnvolvidosReclamacao(DocumentoFiscalReclamado docReclamado, TipoComplSituacaoReclamacao complSituacao) {
        notificaCidadaoReclamacao(docReclamado, complSituacao);
        notiticaEmpresaReclamacao(docReclamado, complSituacao);
    }

    public void notiticaEmpresaReclamacao(DocumentoFiscalReclamado docReclamado, TipoComplSituacaoReclamacao complSituacao) {
        Integer idPessoa;
        boolean sucesso = false;

        try {
            if (docReclamado == null || docReclamado.getId() == null || docReclamado.getNumero() == null) {
                logger.error("Nao consegui achar a reclamacao a ser notificada, ou ela nao possui numero de documento fiscal.");
                return;
            }

                if (docReclamado.getNumeroCnpjEmpresa()==null){
                    logger.error("Nao consegui achar o cnpj para a reclamacao a ser notificada para empresa. Id doc reclamado:" + docReclamado.getId());
                    return;
                }
                GENPessoaJuridica pessoaJuridica = documentoFiscalReclamadoRepo.findPessoaJuridicaByCnpj(docReclamado.getNumeroCnpjEmpresa());

                if (pessoaJuridica==null || pessoaJuridica.getIdPessoa()==null){
                    logger.error("Nao consegui achar pessoa juridica para a reclamacao a ser notificada para empresa. Id doc reclamado:" + docReclamado.getId());
                    return;
                }else{
                    idPessoa = pessoaJuridica.getIdPessoa();
                }

                sucesso = enviaNotificacaoEmpresa(idPessoa, "Novo andamento na reclamação do Documento Fiscal de número " + docReclamado.getNumero()
                        , complSituacao.getDescricao());

            if (sucesso) {
                logger.info("Notificacao de reclamacao gerada com sucesso para empresa sobre novo status " + complSituacao.getValue() + " da reclamacao:" + docReclamado.getId()+". Id pessoa: "+idPessoa);
            } else {
                logger.error("Algo deu errado na Notificacao de reclamacao para empresa sobre novo status " + complSituacao.getValue() + " da reclamacao:" + docReclamado.getId()+". Id pessoa: "+idPessoa);
            }
        }catch (Exception e){
            logger.error("Algo de errado aconteceu ao tentar notificar novo status de reclamacao para empresa: " + e.getMessage());
        }
    }

    public void notificaCidadaoReclamacao(DocumentoFiscalReclamado docReclamado, TipoComplSituacaoReclamacao complSituacao) {
        Integer idPessoa;
        boolean sucesso = false;

        try {
            if (docReclamado == null || docReclamado.getId() == null || docReclamado.getNumero() == null) {
                logger.error("Nao consegui achar a reclamacao a ser notificada, ou ela nao possui numero de documento fiscal.");
                return;
            }

                PessoaParticipante pessoaParticipante = docReclamado.getPessoaParticipante();
                GENPessoaFisica genPessoaFisica = pessoaParticipante.getGenPessoaFisica();

                if (docReclamado == null || pessoaParticipante == null
                        || genPessoaFisica == null
                        || genPessoaFisica.getIdPessoa() == null) {
                    logger.error("Nao consegui achar o id pessoa para a reclamacao a ser notificada. Id doc reclamado:" + docReclamado.getId());
                    return;
                } else {
                    idPessoa = genPessoaFisica.getIdPessoa();
                }

                sucesso = enviaNotificacaoCidadao(idPessoa, "Novo andamento na reclamação do Documento Fiscal de número " + docReclamado.getNumero()
                        , complSituacao.getDescricao() + " Acesse o painel 'Minhas Reclamações' para mais detalhes.");

            if (sucesso) {
                logger.info("Notificacao de reclamacao gerada com sucesso para cidadao sobre novo status " + complSituacao.getValue() + " da reclamacao:" + docReclamado.getId()+". Id pessoa: "+idPessoa);
            } else {
                logger.error("Algo deu errado na Notificacao de reclamacao para cidadao sobre novo status " + complSituacao.getValue() + " da reclamacao:" + docReclamado.getId()+". Id pessoa: "+idPessoa);
            }
        }catch (Exception e){
            logger.error("Algo de errado aconteceu ao tentar notificar novo status de reclamacao para cidadao: " + e.getMessage());
        }
    }


    public boolean enviaNotificacaoCidadao(Integer idPessoa,String titulo, String texto) {

        Integer idMensagem = gravaMensagemNFG("C","N",titulo,texto,2);
        if (idMensagem==null){
            logger.error("Não foi possivel gravar mensagem no envio de notificacao para cidadao "+idPessoa+". Titulo:"+titulo);
            return false;
        }

        if(gravaDestinatarioDaMensagem(idPessoa,idMensagem)){
            logger.info("Notificação devidamente enviada para cidadao de id pessoa " + idPessoa + ". Titulo:" + titulo);
            return true;
        }else{
            logger.error("Não foi possivel gravar destinatario da mensagem no envio de notificacao para cidadao "+idPessoa+". Titulo:"+titulo);
            return false;

        }
    }


    public boolean enviaNotificacaoEmpresa(Integer idPessoa,String titulo, String texto) {

        Integer idMensagem = gravaMensagemNFG("E","N",titulo,texto,2);
        if (idMensagem==null){
            logger.error("Não foi possivel gravar mensagem no envio de notificacao para empresa "+idPessoa+". Titulo:"+titulo);
            return false;
        }

        if(gravaDestinatarioDaMensagem(idPessoa,idMensagem)){
            logger.info("Notificação devidamente enviada para empresa de id pessoa " + idPessoa + ". Titulo:" + titulo);
            return true;
        }else{
            logger.error("Não foi possivel gravar destinatario da mensagem no envio de notificacao para empresa "+idPessoa+". Titulo:"+titulo);
            return false;

        }
    }

    private Integer gravaMensagemNFG(final String tipoDestin,
                                     final String mensagemPublica,
                                     final String titulo,
                                     final String texto,
                                     final Integer tipoMensagem) {
        sqlMensagem = "insert into NFG_MENSAGEM_NFG" +
                " (TIPO_DEST_MENSAGEM_NFG,INDI_MENSAGEM_PUBLICA," +
                " DESC_TITULO_MENSAGEM,DESC_MENSAGEM,DATA_MENSAGEM,CODG_TIPO_MENSAGEM)" +
                "     VALUES " +
                "       (?,?,?,?,sysdate,?)";

        try {
            //todo retirar esse codigo quando historico for retirado da tabela
            String sqlHist="call GEN.PKGGEN_INICIALIZA_USER.SPGEN_INICIALIZA_USER('10684050')";
            jdbcTemplate.update(sqlHist);

            KeyHolder holder = new GeneratedKeyHolder();
            int result = jdbcTemplate.update(new PreparedStatementCreator() {

                @Override
                public PreparedStatement createPreparedStatement(Connection connection)
                        throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(sqlMensagem, new String[]{"ID_MENSAGEM_NFG"});
                    ps.setString(1, tipoDestin );
                    ps.setString(2, mensagemPublica.toString());
                    ps.setString(3, titulo);
                    ps.setString(4, texto);
                    ps.setString(5, tipoMensagem.toString());
                    return ps;
                }
            }, holder);
            if (result>0)
                return holder.getKey().intValue();
            else
                return null;
        }catch (Exception e) {
            logger.error("Erro ao tentar adicionar nova mensagem de usuário: "+e.getMessage());
            return null;
        }
    }


    private boolean gravaDestinatarioDaMensagem(Integer idPessoa,Integer mensagemId) {
        sqlDestinatario = "insert into NFG_DESTINATARIO_MENSAGEM_NFG" +
                " (ID_PESSOA_DEST_MENSAGEM,ID_MENSAGEM_NFG) " +
                "     VALUES " +
                "       (?,?)";

        int result;

        try {
            //todo retirar esse codigo quando historico for retirado da tabela
            String sqlHist="call GEN.PKGGEN_INICIALIZA_USER.SPGEN_INICIALIZA_USER('10684050')";
            jdbcTemplate.update(sqlHist);

            result = jdbcTemplate.update(sqlDestinatario,idPessoa,mensagemId);
        }catch (Exception e){
            logger.error("Erro ao tentar adicionar novo destinatario de idpessoa "+idPessoa+" na mensagem de usuário: "+e.getMessage());
            return false;
        }

        return result > 0;
    }

}
