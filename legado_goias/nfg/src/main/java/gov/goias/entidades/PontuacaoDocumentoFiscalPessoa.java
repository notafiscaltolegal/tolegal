package gov.goias.entidades;

import gov.goias.persistencia.GenericRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;

/**
 * Created by Remisson-ss on 14/10/2014.
 */

@Entity
@Table(name = "NFG_PONTUACAO_DOC_FISCAL_PES")
@Repository
public class PontuacaoDocumentoFiscalPessoa extends GenericRepository<Integer, PontuacaoDocumentoFiscalPessoa> implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Column(name = "INFO_PONTUACAO_DOCUMENTO_FISC")
    private String infoPontuacao;

    @Id
    @OneToOne
    @JoinColumn(name = "ID_PONTUACAO_PESSOA")
    PontuacaoPessoa pontuacaoPessoa;

    @ManyToOne
    @JoinColumn(name = "ID_REGRA_PONTUACAO_DOC_FISCAL")
    RegraPontuacaoDocumentoFiscal regraDeConversaoDePontuacao;

    @Id
    @OneToOne
    @JoinColumn(name = "ID_DOCUMENTO_FISCAL_PARTCT")
    DocumentoFiscalParticipante documentoFiscalParticipante;

    public String getInfoPontuacao() {
        return infoPontuacao;
    }

    public void setInfoPontuacao(String infoPontuacao) {
        this.infoPontuacao = infoPontuacao;
    }


    public PontuacaoPessoa getPontuacaoPessoa() {
        return pontuacaoPessoa;
    }

    public void setPontuacaoPessoa(PontuacaoPessoa pontuacaoPessoa) {
        this.pontuacaoPessoa = pontuacaoPessoa;
    }

    public RegraPontuacaoDocumentoFiscal getRegraDeConversaoDePontuacao() {
        return regraDeConversaoDePontuacao;
    }

    public void setRegraDeConversaoDePontuacao(RegraPontuacaoDocumentoFiscal regraDeConversaoDePontuacao) {
        this.regraDeConversaoDePontuacao = regraDeConversaoDePontuacao;
    }

    public DocumentoFiscalParticipante getDocumentoFiscalParticipante() {
        return documentoFiscalParticipante;
    }

    public void setDocumentoFiscalParticipante(DocumentoFiscalParticipante documentoFiscalParticipante) {
        this.documentoFiscalParticipante = documentoFiscalParticipante;
    }


    public boolean existePontuacaoParaODocumento(DocumentoFiscalParticipante docParticipante) {
        String sql = "select count(nfgPontuacao.ID_PONTUACAO_PESSOA) as contador from " +
                " NFG_PONTUACAO_DOC_FISCAL_PES nfgPontuacao where ID_DOCUMENTO_FISCAL_PARTCT = ?";
        try{
            return jdbcTemplate.queryForObject(sql, Boolean.class, docParticipante.getId());
        }catch (EmptyResultDataAccessException e){
            return false;
        }

    }
}
