package gov.goias.dtos;

import java.util.Date;

/**
 * @author lucas-mp
 * @since 26/01/2015.
 */
public class DTOMinhasNotas {

    private String numero;
    private String cnpj;
    private String estabelecimento;
    private String fantasia;
    private Date emissao;
    private Date registro;
    private Double valor;
    private Character empresaCadastrada;
    private String infoSorteioParticipado;
    private String detalhe;
    private Integer qtdePontos;
    private Integer statusPontuacao;

    public DTOMinhasNotas() {

    }

    public DTOMinhasNotas(String cnpj, String estabelecimento,
                          String fantasia,String numero, Date emissao, Date registro,
                          Double valor, Character empresaCadastrada, String infoSorteioParticipado,
                          Integer qtdePontos,Integer statusPontuacao,String detalhe
    ) {
        this.numero = numero;
        this.cnpj = cnpj;
        this.estabelecimento = estabelecimento;
        this.fantasia = fantasia;
        this.emissao = emissao;
        this.registro = registro;
        this.valor = valor;
        this.empresaCadastrada = empresaCadastrada;
        this.infoSorteioParticipado=infoSorteioParticipado;
        this.qtdePontos=qtdePontos;
        this.statusPontuacao=statusPontuacao;
        this.detalhe=detalhe;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(String estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Date getEmissao() {
        return emissao;
    }

    public void setEmissao(Date emissao) {
        this.emissao = emissao;
    }

    public Date getRegistro() {
        return registro;
    }

    public void setRegistro(Date registro) {
        this.registro = registro;
    }


    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public Character getEmpresaCadastrada() {
        return empresaCadastrada;
    }

    public void setEmpresaCadastrada(Character empresaCadastrada) {
        this.empresaCadastrada = empresaCadastrada;
    }

    public String getInfoSorteioParticipado() {
        return infoSorteioParticipado;
    }

    public void setInfoSorteioParticipado(String infoSorteioParticipado) {
        this.infoSorteioParticipado = infoSorteioParticipado;
    }

    public Integer getQtdePontos() {
        return qtdePontos;
    }

    public void setQtdePontos(Integer qtdePontos) {
        this.qtdePontos = qtdePontos;
    }

    public Integer getStatusPontuacao() {
        return statusPontuacao;
    }

    public void setStatusPontuacao(Integer statusPontuacao) {
        this.statusPontuacao = statusPontuacao;
    }

    public String getDetalhe() {
        return detalhe;
    }

    public void setDetalhe(String detalhe) {
        this.detalhe = detalhe;
    }
}
