package gov.goias.dtos;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by diogo-rs on 8/4/2014.
 */
public class DTOCnaeCadastro implements Serializable{

    private static final long serialVersionUID = 1908567349267498573L;

    private String secao;
    private String divisao;
    private String grupo;
    private String classe;
    private String subclasse;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date dataObrigatoriedade;

    public String getSecao() {
        return secao;
    }

    public void setSecao(String secao) {
        this.secao = secao;
    }

    public String getDivisao() {
        return divisao;
    }

    public void setDivisao(String divisao) {
        this.divisao = divisao;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getSubclasse() {
        return subclasse;
    }

    public void setSubclasse(String subclasse) {
        this.subclasse = subclasse;
    }

    public Date getDataObrigatoriedade() {
        return dataObrigatoriedade;
    }

    public void setDataObrigatoriedade(Date dataObrigatoriedade) {
        this.dataObrigatoriedade = dataObrigatoriedade;
    }

    public Boolean deveInserirTodosDaDivisao() {
        return "todos".equalsIgnoreCase(grupo);
    }

    public Boolean deveInserirTodosDoGruo(){
        return "todos".equalsIgnoreCase(classe);
    }

    public Boolean deveInserirTodosDaClasse() {
        return "todos".equalsIgnoreCase(subclasse);
    }
}
