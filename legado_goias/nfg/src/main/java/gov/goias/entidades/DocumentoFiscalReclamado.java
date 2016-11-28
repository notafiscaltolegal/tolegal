package gov.goias.entidades;

import gov.goias.entidades.enums.TipoComplSituacaoReclamacao;
import gov.goias.persistencia.DocumentoFiscalReclamadoRepository;
import gov.goias.util.ResultSetOpcional;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bruno-cff on 30/09/2015.
 */
@Entity
@Table(name = "NFG_DOCUMENTO_FISCAL_RECLAMADO")
@Repository
public class DocumentoFiscalReclamado extends DocumentoFiscalReclamadoRepository implements Serializable, gov.goias.entidades.JdbcMappable {

    @Autowired
    @Transient
    SimpleDateFormat simpleDateFormat;

    @Id
    @Column(name = "ID_DOCUMENTO_FISCAL_RECLAMADO")
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "gov.goias.persistencia.util.TriggerAssignedIdentityGenerator")
    private Integer id;

    @Column(name = "DATA_DOCUMENTO_FISCAL")
    private Date dataDocumentoFiscal;

    @Column(name = "NUMR_DOCUMENTO_FISCAL")
    private Integer numero;

    @Column(name = "TIPO_DOCUMENTO_FISCAL_RECLAMA")
    private Integer tipoDocumentoFiscal;

    @Column(name = "VALR_DOCUMENTO_FISCAL")
    private Double valor;

    @Column(name = "DATA_RECLAMACAO")
    private Date dataReclamacao;

    @Column(name = "INDI_RECLAMACAO_RESOLVIDA")
    private String reclamacaoResolvida;

    @Column(name = "IMAG_DOCUMENTO_FISCAL_RECLAMA")
    private Blob imgDocumentoFiscal;

    @Column(name = "TIPO_EXTENSAO_ARQUIVO_DOC_FISC")
    private Integer tipoExtensao;

    @ManyToOne
    @JoinColumn(name = "CODG_MOTIVO_RECLAMACAO")
    private MotivoReclamacao motivoReclamacao;

    @ManyToOne
    @JoinColumn(name = "ID_PESSOA_PARTCT_RECLAMANTE")
    private PessoaParticipante pessoaParticipante;

    @Column(name = "NUMR_CNPJ_RECLAMADO")
    private String numeroCnpjEmpresa;

    @Transient
    private String nomeFantasiaEmpresa;

    @Transient
    private String listaAndamentoStr;

    @Transient
    private String statusAndamentoStr;

    @Transient
    private String disableRadioBtn;

    @Transient
    private String razaoSocial;

    @Transient
    private String inscricaoEmpresa;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataDocumentoFiscal() {
        return dataDocumentoFiscal;
    }


    public String getDataDocumentoFiscalStr() {
        if (dataDocumentoFiscal != null) {
            simpleDateFormat= (simpleDateFormat==null)? new SimpleDateFormat("dd/MM/yyyy"): simpleDateFormat;
            return simpleDateFormat.format(dataDocumentoFiscal);
        } else {
            return new String();
        }

    }

    public String getInscricaoEmpresa() {
        return inscricaoEmpresa;
    }

    public void setInscricaoEmpresa(String inscricaoEmpresa) {
        this.inscricaoEmpresa = inscricaoEmpresa;
    }

    public void setDataDocumentoFiscal(Date dataDocumentoFiscal) {
        this.dataDocumentoFiscal = dataDocumentoFiscal;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getTipoDocumentoFiscal() {
        return tipoDocumentoFiscal;
    }

    public void setTipoDocumentoFiscal(Integer tipoDocumentoFiscal) {
        this.tipoDocumentoFiscal = tipoDocumentoFiscal;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getDataReclamacao() {
        return dataReclamacao;
    }

    public void setDataReclamacao(Date dataReclamacao) {
        this.dataReclamacao = dataReclamacao;
    }

    public String getReclamacaoResolvida() {
        return reclamacaoResolvida;
    }

    public void setReclamacaoResolvida(String reclamacaoResolvida) {
        this.reclamacaoResolvida = reclamacaoResolvida;
    }

    public Blob getImgDocumentoFiscal() {
        return imgDocumentoFiscal;
    }

    public void setImgDocumentoFiscal(Blob imgDocumentoFiscal) {
        this.imgDocumentoFiscal = imgDocumentoFiscal;
    }

    public Integer getTipoExtensao() {
        return tipoExtensao;
    }

    public void setTipoExtensao(Integer tipoExtensao) {
        this.tipoExtensao = tipoExtensao;
    }

    public MotivoReclamacao getMotivoReclamacao() {
        return motivoReclamacao;
    }

    public void setMotivoReclamacao(MotivoReclamacao motivoReclamacao) {
        this.motivoReclamacao = motivoReclamacao;
    }

    public PessoaParticipante getPessoaParticipante() {
        return pessoaParticipante;
    }

    public void setPessoaParticipante(PessoaParticipante pessoaParticipante) {
        this.pessoaParticipante = pessoaParticipante;
    }

    public String getNumeroCnpjEmpresa() {
        return numeroCnpjEmpresa;
    }

    public void setNumeroCnpjEmpresa(String numeroCnpjEmpresa) {
        this.numeroCnpjEmpresa = numeroCnpjEmpresa;
    }

    public String getNomeFantasiaEmpresa() {
        return nomeFantasiaEmpresa;
    }

    public void setNomeFantasiaEmpresa(String nomeFantasiaEmpresa) {
        this.nomeFantasiaEmpresa = nomeFantasiaEmpresa;
    }

    public String getDescUltimaSituacao() {
        Integer codgUltimaSituacao = getCodgUltimaSituacao();
        return codgUltimaSituacao == null ? "" : TipoComplSituacaoReclamacao.get(codgUltimaSituacao).getDescricao();
    }

    public TipoComplSituacaoReclamacao getSituacaoAtual() {
        if(getCodgUltimaSituacao() == null) return null;
        return TipoComplSituacaoReclamacao.get(getCodgUltimaSituacao());
    }

    public Integer getCodgUltimaSituacao() {
        if (getId() == null) return null;

        String sql = "select CODG_COMPL_SITUACAO_RECLAMACAO from" +
                " (SELECT CODG_COMPL_SITUACAO_RECLAMACAO FROM " +
                " NFG_SITUACAO_DOC_FISC_RECLAMA " +
                " where ID_DOCUMENTO_FISCAL_RECLAMADO = ? " +
                " ORDER BY DATA_CADASTRO_SITUACAO DESC, ID_SITUACAO_DOC_FISC_RECLAMA DESC) " +
                " where rownum < 2";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, getId());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public String getListaAndamentoStr() {
        Integer id = getId();
        if (id == null) return null;
        return listAndamentoReclamacaoString(id);
    }

    public void setListaAndamentoStr(String listaAndamentoStr) {
        this.listaAndamentoStr = listaAndamentoStr;
    }

    public String getStatusAndamentoStr() {
        Integer id = getId();
        if (id == null) return null;
        return retornaStatusAndamentoString(id);
    }

    public String getDisableRadioBtn() {
        return disableRadioBtn;
    }

    public void setDisableRadioBtn(String disableRadioBtn) {
        this.disableRadioBtn = disableRadioBtn;
    }

    public void setStatusAndamentoStr(String statusAndamentoStr) {
        this.statusAndamentoStr = statusAndamentoStr;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    @Override
    public void populate(ResultSet resultSet) throws SQLException, ParseException {
        ResultSetOpcional rs = new ResultSetOpcional(resultSet);


        setId(rs.getInt("ID_DOCUMENTO_FISCAL_RECLAMADO"));
        setNumero(rs.getInt("NUMR_DOCUMENTO_FISCAL"));
        setValor(rs.getDouble("VALR_DOCUMENTO_FISCAL"));
        setDataReclamacao(rs.getTimestamp("DATA_RECLAMACAO"));
        setDataDocumentoFiscal(rs.getTimestamp("DATA_DOCUMENTO_FISCAL"));


        setNumeroCnpjEmpresa(rs.getString("NUMR_CNPJ_RECLAMADO"));

        setInscricaoEmpresa(rs.getString("NUMR_INSCRICAO"));


        setRazaoSocial(rs.getString("NOME_EMPRESAR"));
        setNomeFantasiaEmpresa(rs.getString("NOME_FANTASIA"));
        setTipoExtensao(rs.getInt("TIPO_EXTENSAO_ARQUIVO_DOC_FISC"));

        PessoaParticipante pessoaParticipante = new PessoaParticipante();
        GENPessoaFisica genPessoaFisica = new GENPessoaFisica();

        genPessoaFisica.setNome(rs.getString("NOME_PESSOA"));

        pessoaParticipante.setGenPessoaFisica(genPessoaFisica);

        MotivoReclamacao motivoReclamacao = new MotivoReclamacao();
        motivoReclamacao.setDescricao(rs.getString("DESC_MOTIVO_RECLAMACAO"));

        setMotivoReclamacao(motivoReclamacao);
        setPessoaParticipante(pessoaParticipante);
    }

}