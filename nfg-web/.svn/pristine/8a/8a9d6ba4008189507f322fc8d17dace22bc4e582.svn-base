package gov.goias.cache;

import gov.goias.dtos.DTOContribuinte;
import gov.goias.entidades.GENEmpresa;
import gov.goias.entidades.GENPessoaFisica;
import gov.goias.persistencia.CCEContribuinteRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author henrique-rh
 * @since 29/01/15.
 */
public class CCEContribuinteCache extends CCEContribuinteRepository {

    private volatile static HashMap<String, List<DTOContribuinte>> cacheContribuintes = new HashMap<>();
    private volatile static HashMap<String, Date> ultimasConsultas = new HashMap<>();
    private static Long duasHoras = (long) (1000 * 60 * 60 * 2);
    private ThreadLocal<Long> count = new ThreadLocal<>();


    private String getKeyContador(GENPessoaFisica contador) {
        return "contador:" + contador.getIdPessoa();
    }

    private String getKeyContribuinte(String cnpjBase) {
        return "contribuinte:" + cnpjBase;
    }

    public void clearCacheContribuinte(String cnpjBase) {
        cacheContribuintes.remove(getKeyContribuinte(cnpjBase));
        ultimasConsultas.remove(getKeyContribuinte(cnpjBase));
    }

    public void clearCacheContador(GENPessoaFisica contador) {
        cacheContribuintes.remove(getKeyContador(contador));
        ultimasConsultas.remove(getKeyContador(contador));
    }

    public List<DTOContribuinte> findContribuintesParaContador(Integer page, Integer max, GENPessoaFisica contador, Integer numrInscricao, String cnpj, String nome) {
        return getFromCacheForContador(page, max, contador, numrInscricao, cnpj, nome);
    }

    public Long countContribuintesParaContador(GENPessoaFisica contador, Integer numrInscricao){
        return count.get();
    }

    private List<DTOContribuinte> getFromCacheForContador(Integer page, Integer max, GENPessoaFisica contador, Integer numrInscricao, String cnpj, String nome) {
        Date ultimaConsulta = ultimasConsultas.get(getKeyContador(contador));

        if (ultimaConsulta == null || new Date().getTime() - duasHoras > ultimaConsulta.getTime()) {
            cacheContribuintes.remove(getKeyContador(contador));
        }

        List<DTOContribuinte> list = cacheContribuintes.get(getKeyContador(contador));
        if (list != null) {
            return filter(list, page, max, numrInscricao, cnpj, nome);
        } else {
            cacheContribuintes.put(getKeyContador(contador), getFromDBForContador(contador));
            ultimasConsultas.put(getKeyContador(contador), new Date());

            return getFromCacheForContador(page, max, contador, numrInscricao, cnpj, nome);
        }
    }

    private List<DTOContribuinte> filter(List<DTOContribuinte> list, Integer page, Integer max, Integer numrInscricao, String cnpj, String nome) {
        List<DTOContribuinte> listaRetorno = list;
        List<DTOContribuinte> listaAuxiliar = new ArrayList<>(list);

        for (DTOContribuinte dto : listaRetorno) {
            if (numrInscricao != null) {
                if (!dto.getNumeroInscricao().equals(numrInscricao)) {
                    listaAuxiliar.remove(dto);
                    continue;
                }
            }
            if (cnpj != null) {
                if (!((cnpj.length() == 8 && dto.getNumeroCnpj().substring(0, 8).equals(cnpj)) || dto.getNumeroCnpj().equals(cnpj))) {
                    listaAuxiliar.remove(dto);
                    continue;
                }
            }
            if (nome != null) {
                if (!((dto.getNomeEmpresa() != null && dto.getNomeEmpresa().contains(nome.toUpperCase())))) {
                    listaAuxiliar.remove(dto);
                }
            }
        }

        listaRetorno = new ArrayList<>(listaAuxiliar);

        count.set((long) listaRetorno.size());

        if (page != null && max != null) {
            listaAuxiliar = new ArrayList<>();
            for (int i = page * max, end = i + max, size = listaRetorno.size(); i < end; i++) {
                if (size > i) {
                    listaAuxiliar.add(listaRetorno.get(i));
                }
            }
            listaRetorno = new ArrayList<>(listaAuxiliar);
        }

        return listaRetorno;
    }

    public List<DTOContribuinte> findContribuintesNFG(Integer page, Integer max, String cnpjBase, Integer numrInscricao, String cnpj, String nome) {
        return getFromCacheForContribuinte(page, max, cnpjBase, numrInscricao, cnpj, nome);
    }

    public Long countContribuintesNFG(String cnpjBase, Integer numrInscricao) {
        return count.get();
    }

    private List<DTOContribuinte> getFromCacheForContribuinte(Integer page, Integer max, String cnpjBase, Integer numrInscricao, String cnpj, String nome) {
        Date ultimaConsulta = ultimasConsultas.get(getKeyContribuinte(cnpjBase));

        if (ultimaConsulta == null || new Date().getTime() - duasHoras > ultimaConsulta.getTime()) {
            cacheContribuintes.remove(getKeyContribuinte(cnpjBase));
        }

        List<DTOContribuinte> list = cacheContribuintes.get(getKeyContribuinte(cnpjBase));
        if (list != null) {
            return filter(list, page, max, numrInscricao, cnpj, nome);
        } else {
            cacheContribuintes.put(getKeyContribuinte(cnpjBase), getFromDBForContribuinte(cnpjBase));
            ultimasConsultas.put(getKeyContribuinte(cnpjBase), new Date());

            return getFromCacheForContribuinte(page, max, cnpjBase, numrInscricao, cnpj, nome);
        }
    }

    public boolean inscricaoCompativelContador(GENPessoaFisica contadorLogado, Integer inscricao) {
        Long count = null;

        if (contadorLogado !=null){
            count = countContribuintesParaContador(contadorLogado, inscricao);
        }

        return (count != null && count > 0);
    }

    public boolean inscricaoCompativelContribuinte(GENEmpresa empresaLogada, Integer inscricao) {
        Long count = null;
        if(empresaLogada != null && empresaLogada.getNumeroCnpjBase() != null){
            String numeroCnpjBase = empresaLogada.getNumeroCnpjBase();
            count = countContribuintesNFG(numeroCnpjBase,inscricao);
        }
        return (count != null && count > 0);
    }
}
