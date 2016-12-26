package gov.goias.dtos;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bruno-cff on 02/12/2015.
 */
public class DTOContatoGanhadores implements Serializable{

    private Integer idPessoa;
    private String nome;
    private String cpf;
    private String sorteio;
    private Integer numeroBilhete;
    private Integer valor;
    private Integer contaBancaria;
    private List<DTOGENEmailPessoa> listEmails;
    private List<DTOGENTelefonePessoa> listTelefones;

    public Integer getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Integer idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSorteio() {
        return sorteio;
    }

    public void setSorteio(String sorteio) {
        this.sorteio = sorteio;
    }

    public Integer getNumeroBilhete() {
        return numeroBilhete;
    }

    public void setNumeroBilhete(Integer numeroBilhete) {
        this.numeroBilhete = numeroBilhete;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public Integer getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(Integer contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public List<DTOGENEmailPessoa> getListEmails() {
        return listEmails;
    }

    public void setListEmails(List<DTOGENEmailPessoa> listEmails) {
        this.listEmails = listEmails;
    }

    public List<DTOGENTelefonePessoa> getListTelefones() {
        return listTelefones;
    }

    public void setListTelefones(List<DTOGENTelefonePessoa> listTelefones) {
        this.listTelefones = listTelefones;
    }
}
