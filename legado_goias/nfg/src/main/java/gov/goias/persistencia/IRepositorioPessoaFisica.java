package gov.goias.persistencia;

import gov.goias.services.IGenPessoaFisicaService;

import java.util.Date;

public interface IRepositorioPessoaFisica {


    public String getNome();
    public String getNomeDaMae();
    public String getCpf();
    public Date getDataDeNascimento();
    public Integer getIdPessoa();

}