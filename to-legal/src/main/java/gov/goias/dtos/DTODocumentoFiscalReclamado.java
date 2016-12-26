package gov.goias.dtos;

import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;

/**
 * Created by bruno-cff on 15/10/2015.
 */
public class DTODocumentoFiscalReclamado implements Serializable {

    private Integer id;
    private Date dataRegistro;
    private String numero;
    private String nomeReclamante;
    private Date dataEmissao;
    private Long valor;
    private String status;
    private String motivo;
    private Date ultimaAtualizacao;
    private String info;
    private Blob imagem;
    private Integer tipoExtensaoDocumento;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getNomeReclamante() {
        return nomeReclamante;
    }

    public void setNomeReclamante(String nomeReclamante) {
        this.nomeReclamante = nomeReclamante;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Date getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(Date ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Blob getImagem() {
        return imagem;
    }

    public void setImagem(Blob imagem) {
        this.imagem = imagem;
    }

    public Integer getTipoExtensaoDocumento() {
        return tipoExtensaoDocumento;
    }

    public void setTipoExtensaoDocumento(Integer tipoExtensaoDocumento) {
        this.tipoExtensaoDocumento = tipoExtensaoDocumento;
    }
}
