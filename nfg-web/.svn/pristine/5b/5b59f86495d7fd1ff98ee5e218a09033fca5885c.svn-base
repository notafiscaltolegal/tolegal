package gov.goias.entidades;

import gov.goias.entidades.enums.TipoPerfilCadastroReclamacao;
import gov.goias.persistencia.GenericRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by lucas-mp.
 */
@Entity
@Table(name = "NFG_SITUACAO_DOC_FISC_RECLAMA")
@Repository
public class SituacaoDocumentoFiscalReclamado extends GenericRepository implements gov.goias.entidades.JdbcMappable {

    @Id
    @Column(name = "ID_SITUACAO_DOC_FISC_RECLAMA")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "gov.goias.persistencia.util.TriggerAssignedIdentityGenerator")
    private Integer id;

    @Column(name = "DATA_CADASTRO_SITUACAO")
    private Date dataCadastroSituacao;

    @Column(name = "INFO_SITUACAO_DOC_FISC_RECLAMA")
    private String info;

    @Column(name = "TIPO_PERFIL_USUARIO_NFG")
    private Integer tipoPerfil;

    @Transient
    private TipoPerfilCadastroReclamacao perfilCadastro;

    @ManyToOne
    @JoinColumn(name = "ID_DOCUMENTO_FISCAL_RECLAMADO")
    private DocumentoFiscalReclamado documentoFiscalReclamado;

    @ManyToOne
    @JoinColumn(name = "CODG_COMPL_SITUACAO_RECLAMACAO")
    private ComplSituacaoReclamacao complSituacaoReclamacao;

    @ManyToOne
    @JoinColumn(name = "ID_PESSOA_RESPONSAVEL_CADASTRO")
    private GENPessoa pessoaResponsavelCadastro;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataCadastroSituacao() {
        return dataCadastroSituacao;
    }

    public void setDataCadastroSituacao(Date dataCadastroSituacao) {
        this.dataCadastroSituacao = dataCadastroSituacao;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    public DocumentoFiscalReclamado getDocumentoFiscalReclamado() {
        return documentoFiscalReclamado;
    }

    public void setDocumentoFiscalReclamado(DocumentoFiscalReclamado documentoFiscalReclamado) {
        this.documentoFiscalReclamado = documentoFiscalReclamado;
    }

    public ComplSituacaoReclamacao getComplSituacaoReclamacao() {
        return complSituacaoReclamacao;
    }

    public void setComplSituacaoReclamacao(ComplSituacaoReclamacao complSituacaoReclamacao) {
        this.complSituacaoReclamacao = complSituacaoReclamacao;
    }

    public Integer getTipoPerfil() {
        return tipoPerfil;
    }

    public void setTipoPerfil(Integer tipoPerfil) {
        this.tipoPerfil = tipoPerfil;
    }

    public GENPessoa getPessoaResponsavelCadastro() {
        return pessoaResponsavelCadastro;
    }

    public void setPessoaResponsavelCadastro(GENPessoa pessoaResponsavelCadastro) {
        this.pessoaResponsavelCadastro = pessoaResponsavelCadastro;
    }

    public String getPerfilDescricao(){
        return TipoPerfilCadastroReclamacao.get(getTipoPerfil()).getDescricao();
    }

    public TipoPerfilCadastroReclamacao getPerfilCadastro() {
        return perfilCadastro;
    }

    public void setPerfilCadastro(TipoPerfilCadastroReclamacao perfilCadastro) {
        this.perfilCadastro = perfilCadastro;
        this.tipoPerfil=perfilCadastro.getValue();
    }

    @Override
    public void populate(ResultSet resultSet) throws SQLException, ParseException {
        setDataCadastroSituacao(resultSet.getTimestamp("DATA_CADASTRO_SITUACAO"));
        setInfo(resultSet.getString("INFO_SITUACAO_DOC_FISC_RECLAMA"));
        setComplSituacaoReclamacao(new ComplSituacaoReclamacao(resultSet.getInt("CODG_COMPL_SITUACAO_RECLAMACAO")));
        setTipoPerfil(resultSet.getInt("TIPO_PERFIL_USUARIO_NFG"));
    }

}
