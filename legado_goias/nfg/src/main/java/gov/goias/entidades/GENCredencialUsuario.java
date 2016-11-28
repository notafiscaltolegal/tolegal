package gov.goias.entidades;

import gov.goias.persistencia.GENCredencialUsuarioRepository;
import gov.goias.persistencia.GenericRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author henrique-rh
 * @since 15/07/2015
 */
@Entity
@Repository
@Table(name = "GEN_CREDENCIAL_USUARIO_SISTEMA")
public class GENCredencialUsuario extends GENCredencialUsuarioRepository implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "gov.goias.persistencia.util.TriggerAssignedIdentityGenerator")
    @Column(name = "ID_CREDENCIAL_USUARIO_SISTEMA")
    private Integer idCredencialUsuario;

    @Column(name = "NOME_CREDENCIAL_PORTAL")
    private String nomeCredencialPortal;

    @Column(name = "INFO_SENHA")
    private String infoSenha;

    @Column(name = "STAT_CREDENCIAL_PORTAL")
    private Character statusCredencialPortal;

    @Column(name = "DATA_ATIVACAO_CREDENCIAL")
    private Date dataAtivacaoCredencial;

    @Column(name = "INDI_PORTAL")
    private Character indiPortal;

    @Column(name = "NUMR_CPF")
    private String cpf;

    @Column(name = "DATA_ULTIMO_ACESSO")
    private Date dataUltimoAcesso;

    @OneToOne
    @JoinColumn(name = "NOME_CREDENCIAL_PORTAL", referencedColumnName = "NUMR_PESSOA_BASE", insertable = false, updatable = false)
    private GENPessoaFisica pessoaFisica;

    @Transient
    private String rawPassword;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getIdCredencialUsuario() {
        return idCredencialUsuario;
    }

    public void setIdCredencialUsuario(Integer idCredencialUsuario) {
        this.idCredencialUsuario = idCredencialUsuario;
    }

    public String getNomeCredencialPortal() {
        return nomeCredencialPortal;
    }

    public void setNomeCredencialPortal(String nomeCredencialPortal) {
        this.nomeCredencialPortal = nomeCredencialPortal;
    }

    public String getInfoSenha() {
        return infoSenha;
    }

    public void setInfoSenha(String infoSenha) {
        this.infoSenha = infoSenha;
    }

    public Character getStatusCredencialPortal() {
        return statusCredencialPortal;
    }

    public void setStatusCredencialPortal(Character statusCredencialPortal) {
        this.statusCredencialPortal = statusCredencialPortal;
    }

    public Date getDataAtivacaoCredencial() {
        return dataAtivacaoCredencial;
    }

    public void setDataAtivacaoCredencial(Date dataAtivacaoCredencial) {
        this.dataAtivacaoCredencial = dataAtivacaoCredencial;
    }

    public Character getIndiPortal() {
        return indiPortal;
    }

    public void setIndiPortal(Character indiPortal) {
        this.indiPortal = indiPortal;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getDataUltimoAcesso() {
        return dataUltimoAcesso;
    }

    public void setDataUltimoAcesso(Date dataUltimoAcesso) {
        this.dataUltimoAcesso = dataUltimoAcesso;
    }

    public String getRawPassword() {
        return rawPassword;
    }

    public void setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
    }

    public GENPessoaFisica getPessoaFisica() {
        return pessoaFisica;
    }

    public void setPessoaFisica(GENPessoaFisica pessoaFisica) {
        this.pessoaFisica = pessoaFisica;
    }
}