package gov.goias.services;

import entidade.Credencial;
import entidade.endereco.Endereco;
import entidade.endereco.Logradouro;
import entidade.endereco.Municipio;
import gov.goias.entidades.GENPessoa;
import gov.goias.entidades.GENPessoaFisica;
import org.codehaus.jackson.JsonParseException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface IGenPessoaFisicaService {
    //////// GETS /////////
    public String getEmailPessoaFisicaWs(String idpessoa);

    public List<Integer> idPessoaDeEmailExistente(String email);

    public String getTelefonePessoaFisicaWs(String idpessoa);

    public GENPessoaFisica getPessoaFisicaWs(String cpf);

    public Credencial getSenhaPessoaFisicaWs(String cpf);

    public Endereco getEnderecoPessoaFisicaWs(String idpessoa);

    public Logradouro getLogradouroWs(Integer cep);

    public List<Municipio> getMunicipiosPorEstadoWs(String uf);
    //////// SETS /////////
    public String setPessoaFisicaWs(GENPessoaFisica pessoaFisica);

    public String setEmailPessoaFisicaWs(String email, Integer idPessoa, String matricula);

    public String setTelefonePessoaFisicaWs(Integer dddTelefone, Integer nrTelefon, Integer idPessoa,String matricula);

    public String setSenhaPessoaFisicaWs(String cpf, String senha,String matricula);

    public String setEnderecoPessoaFisicaWs(Endereco endereco,String matricula,Integer idPessoa);

    //////// OUTROS /////////
    public boolean enviaEmailRecuperacaoSenhaPessoaFisicaWs(Integer idPessoa, String path);

    public boolean verificaTokenSenhaPessoaFisicaWs(String cpf, String token);
}