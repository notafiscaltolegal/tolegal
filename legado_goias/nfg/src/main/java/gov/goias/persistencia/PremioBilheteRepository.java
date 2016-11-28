package gov.goias.persistencia;

import gov.goias.dtos.DTOPremiacao;
import gov.goias.entidades.*;
import gov.goias.exceptions.NFGException;
import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by bruno-cff on 16/06/2015.
 */
public abstract class PremioBilheteRepository extends GenericRepository<Integer, PremioBilhete> {
    @Autowired
    private BilhetePessoa bilhetePessoaRepository;

    @Autowired
    GENAgenciaBancaria genAgenciaBancariaRepository;

    @Autowired
    ContaBancariaPremio contaBancariaPremioRepository;

    private static final Logger logger = Logger.getLogger(PremioBilheteRepository.class);

    public List<Map<String,Object>> listarPremiosDoSorteio(Integer idSorteio){
        String sql = "select qtde_premio,valr_premio " +
                "from NFG_PREMIO_SORTEIO " +
                "where id_regra_sorteio= ? order by valr_premio desc";
        try{
            return jdbcTemplate.queryForList(sql, idSorteio);

        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public Integer getIdPremioSorteio(Integer idSorteio, Double valorPremiacao){
        String sql = "select id_premio_sorteio from nfg_premio_sorteio where valr_premio=? and id_regra_sorteio=? ";
        try{
            return jdbcTemplate.queryForObject(sql, Integer.class, valorPremiacao,idSorteio);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public boolean incluiRegistrosDoTxtNaBase(Integer idSorteio, List<Map> bilhetesPremiados){

        for (Map<String,String> bilhetePremiado:bilhetesPremiados){
            String bilhete = bilhetePremiado.get("bilhete");
            String nrPremio =bilhetePremiado.get("premio");
            String valorPremio = bilhetePremiado.get("valor");
            BigDecimal idBilhete;
            Integer idPremioSorteio;

            if (valorPremio== null || valorPremio.length()==0){
                logger.error("Erro: Registro de premiação do arquivo sem valor de premiação!");
                return false;
            }else if(valorPremio.contains("-")) { //se o valor conter um hifen, ele já
                //foi sorteado e portanto nao deve ser incluido no banco.
                continue;
            }

            if (bilhete== null || bilhete.length()==0){
                logger.error("Erro: Registro de premiação do arquivo sem número de bilhete!");
                return false;
            }

            if (nrPremio== null || nrPremio.length()==0){
                logger.error("Erro: Registro de premiação do arquivo sem valor do premio!");
                return false;
            }

            Map mapBilhetePessoa = bilhetePessoaRepository.findBilhete(Long.parseLong(bilhete), idSorteio);
            if (mapBilhetePessoa!=null && mapBilhetePessoa.get("ID_BILHETE")!=null){
                idBilhete = (BigDecimal) mapBilhetePessoa.get("ID_BILHETE");
            }else{
                logger.error("Erro: Número de bilhete "+bilhete+" não foi encontrato na base de dados para a regra sorteio id "+ idSorteio+"!");
                return false;
            }

            idPremioSorteio = getIdPremioSorteio(idSorteio,Double.parseDouble(valorPremio));

            if (idPremioSorteio!=null){
                try{
                    bilhetePessoaRepository.novoBilhete(idBilhete.intValue(),idPremioSorteio,Long.parseLong(nrPremio));
                }catch (Exception cve){
                    logger.error("Erro na inclusao do bilhete "+idBilhete.toString()+" em novo Premio-bilhete: "+cve.getMessage());
                    throw new NFGException("Erro na inclusão, verifique os logs da aplicação!");
                }
            }else {
                logger.error("Erro: Premiacao de valor "+valorPremio+" não foi encontrato na base de dados para a regra sorteio id "+ idSorteio+"!");
                return false;
            }
        }
        return true;
    }

    public List<Map> processaArquivoTxtBilhetesPremiados(MultipartFile file, Integer idSorteio)   {
        List<Map> bilhetesPremiados = new ArrayList<Map>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(),"UTF-8"));

            String line;

            int inicioFaixaBilhetes = 0;
            int finalFaixaBilhetes = 0;
            int nrlinha = 1;
            while ((line = br.readLine()) != null) {
                if (inicioFaixaBilhetes==0){
                    //linha de referencia inicial: Iteração      Premio     Bilhete  Observação
                    if (line.contains("Premio")&&line.contains("Bilhete")&&line.contains("Itera")&&line.contains("Observa")){
                        inicioFaixaBilhetes = nrlinha+2;
                    }
                }

                if (finalFaixaBilhetes==0){
                    //linha de referencia final: AVALIAÇÃO DO SORTEIO
                    if (line.contains("AVALIA") && line.contains(" DO SORTEIO")){
                        finalFaixaBilhetes = nrlinha-2;
                    }
                }

                nrlinha++;
            }
            br.close();


            br = new BufferedReader(new InputStreamReader(file.getInputStream(),"UTF-8"));
            nrlinha = 1;
            while ((line = br.readLine()) != null) {
                if (nrlinha <= finalFaixaBilhetes && nrlinha >= inicioFaixaBilhetes){
                    Map<String,String> bilhetePremiado = new HashMap<String,String>();
                    String[] dados =  line.replaceFirst("(\\s)*","").split("(\\s)+"); //formato:"  xxx   xx   xx   xxxx"
                    bilhetePremiado.put("iteracao", dados[0]);
                    bilhetePremiado.put("premio", dados[1]);
                    bilhetePremiado.put("bilhete", dados[2].replaceAll("(\\W)",""));
                    try{
                        if (dados[3]!=null && dados[3].length()>0){
                            bilhetePremiado.put("possuiObs", "Sim");
                        }else{
                            bilhetePremiado.put("possuiObs", "Nao");
                        }
                    }catch (IndexOutOfBoundsException ie){
                        bilhetePremiado.put("possuiObs", "Nao");
                    }

                    bilhetesPremiados.add(bilhetePremiado);
                }
                nrlinha++;
            }

            br.close();

        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException: "+ e.getMessage());
            return null;
        } catch (IOException e) {
            logger.error("IOException: "+ e.getMessage());
            return null;
        } catch (Exception e){
            logger.error("Exception: "+ e.getStackTrace());//Coloquei o stack pois sao erros nao premeditados..
            return null;
        }

        List<Map<String,Object>> listaPremiacoes = listarPremiosDoSorteio(idSorteio);

        bilhetesPremiados = atribuiValoresAosBilhetesPremiados(bilhetesPremiados, listaPremiacoes);
        bilhetesPremiados = atribuiPessoaAosBilhetesPremiados(bilhetesPremiados,idSorteio);
        return ordenaListaBilhetesPremiados(bilhetesPremiados, "desc");
    }


    public List<Map> atribuiValoresAosBilhetesPremiados(List<Map> bilhetesPremiados,List<Map<String,Object>> listaPremiacoes){
        int aux=0;
        for (Map<String,Object> mapPremiacao:listaPremiacoes){
            for (int i = ((BigDecimal)mapPremiacao.get("qtde_premio")).intValue(); i>0; i--){
                Map bilheteMap;
                try{
                    bilheteMap = (Map)(ordenaListaBilhetesPremiados(bilhetesPremiados, "asc")).get(aux);
                }catch (IndexOutOfBoundsException iob){
                    throw new NFGException("Número total de premiações do sorteio escolhido não bate com o número de linhas de premiação do arquivo importado.",true);
                }

                if (((String)bilheteMap.get("possuiObs")).equals("Nao")){
                    (bilheteMap).put("valor", ((BigDecimal) mapPremiacao.get("valr_premio")).toString());
                }else{
                    (bilheteMap).put("valor", " - ");
                }
                aux++;
            }
        }
        return bilhetesPremiados;
    }

    public List<Map> atribuiPessoaAosBilhetesPremiados(List<Map> bilhetesPremiados,Integer idSorteio){
        String nomePessoa;
        String cpfPessoa;
        String nrBilhete;
        Map mapBilhetePessoa;

        for (Map<String,Object> bilhete:bilhetesPremiados){
            nrBilhete = (String)bilhete.get("bilhete");
            mapBilhetePessoa = bilhetePessoaRepository.findBilhete(Long.parseLong(nrBilhete), idSorteio);
            if (mapBilhetePessoa!=null && mapBilhetePessoa.get("ID_BILHETE")!=null){
                nomePessoa = (String) mapBilhetePessoa.get("NOME_PESSOA");
                cpfPessoa = (String) mapBilhetePessoa.get("CPF");
            }else{
                logger.error("Erro: Número de bilhete "+bilhete+" não foi encontrato na base de dados para a regra sorteio id "+ idSorteio+"!");
                nomePessoa = "Não foi encontrado!";
                cpfPessoa = "Não foi encontrado!";
            }
            (bilhete).put("nomePessoa", nomePessoa);
            (bilhete).put("cpfPessoa", cpfPessoa);
        }

        return bilhetesPremiados;
    }

    public List<Map> ordenaListaBilhetesPremiados(List<Map> bilhetesPremiados, String ordem){
        final String ORDEM_FINAL = ordem;
        Collections.sort(bilhetesPremiados, new Comparator<Map>() {
            @Override
            public int compare(Map arg1, Map arg2) {
                Integer it1=Integer.parseInt ((String)arg1.get("iteracao"));
                Integer it2=Integer.parseInt ((String)arg2.get("iteracao"));
                if (ORDEM_FINAL.equals("desc")){
                    return (it2).compareTo(it1);
                }else{
                    return (it1).compareTo(it2);
                }
            }
        });

        return bilhetesPremiados;
    }


    public void gravaSolicitacaoDeResgate(String tipoResgate, String vlrTributo, String vlrTaxas, Integer selectBanco, String numeroAgencia, Integer selectConta, Integer contaBancaria, String digito, Integer idPremioBilhete) {
        PremioBilhete premioBilhete;

        HashMap<String, Object> dados = new HashMap<>();
        dados.put("id", idPremioBilhete);
        List<PremioBilhete> premioList = list(dados, "id");

        if (premioList.size()>0){
            premioBilhete = premioList.get(0);
        }else{
            premioBilhete = new PremioBilhete();
            premioBilhete.setId(idPremioBilhete);
        }

        premioBilhete.setTipoResgate(tipoResgate.charAt(0));
        premioBilhete.setVlrTributo(Double.parseDouble(vlrTributo.replace(",", ".").replace("R", "").replace("$", "").trim()));
        premioBilhete.setVlrTaxaBancaria(Double.parseDouble(vlrTaxas.replace(",", ".").replace("R", "").replace("$", "").trim()));
        premioBilhete.setDataSolicitacaoResgate(new Date());
        premioBilhete.save();

        if (tipoResgate.equals("T")){

            GENAgenciaBancaria agencia = genAgenciaBancariaRepository.listarAgenciaPorIdAgencia(selectBanco, numeroAgencia);
            ContaBancariaPremio contaBancariaPremio = contaBancariaPremioRepository.consultarContaPorBilhetePremiado(premioBilhete);

            if (contaBancariaPremio == null){
                contaBancariaPremio = new ContaBancariaPremio();
                contaBancariaPremio.setPremioBilhete(premioBilhete);
                contaBancariaPremio.setNumero(contaBancaria);
                contaBancariaPremio.setAgenciaBancaria(agencia);
                contaBancariaPremio.setDigito(digito);
                contaBancariaPremio.setTipo(selectConta);
                contaBancariaPremio.save();
            }else{
                contaBancariaPremio.setNumero(contaBancaria);
                contaBancariaPremio.setAgenciaBancaria(agencia);
                contaBancariaPremio.setDigito(digito);
                contaBancariaPremio.setTipo(selectConta);
                contaBancariaPremio.update();
            }
        }
    }

    public Map findPremiacoesDoSorteio(Integer max, Integer page, Integer sorteio) {
        String sql=" SELECT " +

                "   rs.INFO_SORTEIO, " +
                "   ps.VALR_PREMIO, " +
                "   pf.NUMR_CPF, " +
                "   pf.NOME_PESSOA,  " +
                "   pb.NUMR_PREMIO_PROGRAMA_SORTEIO, " +
                "   pb.DATA_LIMITE_RESGATE, " +
                "   pb.TIPO_RESGATE_PREMIO, " +
                "   pb.DATA_RESGATE_PREMIO, " +
                "   pb.VALR_TAXA_BANCARIA, " +
                "   pb.VALR_TRIBUTO, " +
                "   pb.DATA_SOLICT_RESGATE_PREMIO, " +
                "   pb.INFO_RESGATE, " +
                "   pb.ID_PREMIO_BILHETE, " +
                "   bb.NOME_BANCO, " +
                "   cbp.CODG_BANCO, " +
                "   cbp.TIPO_CONTA_BANCARIA, " +
                "   cbp.CODG_AGENCIA, " +
                "   cbp.NUMR_CONTA_BANCARIA, " +
                "   cbp.NUMR_DIGITO_CONTA_BANCARIA " +

                " FROM NFG_PREMIO_BILHETE pb, NFG_BILHETE_PESSOA bp, NFG_PESSOA_PARTICIPANTE pp, GEN_PESSOA_FISICA pf, NFG_PREMIO_SORTEIO ps, NFG_REGRA_SORTEIO rs,NFG_CONTA_BANCARIA_PREMIO cbp, GEN_BANCO bb, GEN_AGENCIA_BANCARIA gab " +
                " where  pb.ID_BILHETE_PESSOA=bp.ID_BILHETE_PESSOA " +
                "        and pb.ID_PREMIO_SORTEIO = ps.ID_PREMIO_SORTEIO " +
                "        and ps.ID_REGRA_SORTEIO = rs.ID_REGRA_SORTEIO " +
                "        and bp.ID_REGRA_SORTEIO = rs.ID_REGRA_SORTEIO " +
                "        and bp.ID_PESSOA_PARTCT = pp.ID_PESSOA_PARTCT " +
                "        and pp.ID_PESSOA = pf.ID_PESSOA " +
                "        and cbp.ID_PREMIO_BILHETE(+)  = pb.ID_PREMIO_BILHETE " +
                "        and( cbp.CODG_BANCO = gab.CODG_BANCO(+) " +
                "             and cbp.CODG_AGENCIA = gab.CODG_AGENCIA(+) " +
                "             and bb.CODG_BANCO (+) = gab.CODG_BANCO) " +
                " and rs.INFO_SORTEIO = ? ";

        String ordenacao="rs.ID_REGRA_SORTEIO asc, ps.VALR_PREMIO desc, pb.NUMR_PREMIO_PROGRAMA_SORTEIO asc";

        return paginateMappableClassObjects(max,page,sql,ordenacao,DTOPremiacao.class
                ,sorteio);
    }
}
