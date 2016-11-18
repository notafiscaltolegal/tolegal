package gov.goias.cache;

import gov.goias.dtos.DTOEmpresaParticipante;
import gov.goias.dtos.DTOSubclasseCnae;
import gov.goias.persistencia.EmpresaParticipanteRepository;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author henrique-rh
 * @since 29/01/15.
 */
public class EmpresaParticipanteCache extends EmpresaParticipanteRepository {
    private volatile static HashMap<String, List<DTOEmpresaParticipante>> cacheEmpresasParticipantes = new HashMap<>();
    private volatile static HashMap<String, Date> ultimasConsultas = new HashMap<>();
    private ThreadLocal<Long> count = new ThreadLocal<>();
    private static final String CHAVE_BUSCA_EMPRESAS = "buscaEmpresas";

    public Long countEmpresasParticipantes(Long idSubClasseCnae, String nome, String cnpj, String codgMunicipio) {
        return count.get();
    }

    public List<DTOEmpresaParticipante> listEmpresasParticipantes(Integer page, Integer max, Long idSubClasseCnae, String nome, String cnpj, String codgMunicipio, String nomeBairro) {
        return getFromCache(page, max, idSubClasseCnae, nome, cnpj, codgMunicipio, nomeBairro);
    }

    private List<DTOEmpresaParticipante> getFromCache(Integer page, Integer max, Long idSubClasseCnae, String nome, String cnpj, String codgMunicipio, String nomeBairro) {
//        Long vinteQuatroHoras = (long) (1000 * 60 * 60 * 24);
//        Date ultimaConsulta = ultimasConsultas.get(CHAVE_BUSCA_EMPRESAS);
//
//        if (ultimaConsulta == null || new Date().getTime() - vinteQuatroHoras > ultimaConsulta.getTime()) {
//            cacheEmpresasParticipantes.remove(CHAVE_BUSCA_EMPRESAS);
//        }

        List<DTOEmpresaParticipante> list = cacheEmpresasParticipantes.get(CHAVE_BUSCA_EMPRESAS);
        if (list != null) {
            return filter(new ArrayList<>(list), page, max, idSubClasseCnae, nome, cnpj, codgMunicipio, nomeBairro);
        } else {
            updateCacheEmpresasParticipantesSync();
            return getFromCache(page, max, idSubClasseCnae, nome, cnpj, codgMunicipio, nomeBairro);
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    private synchronized void updateCacheEmpresasParticipantesSync() {
        Long cincoMinutos = (long) (1000 * 60 * 5);
        Date ultimaConsulta = ultimasConsultas.get(CHAVE_BUSCA_EMPRESAS);

        if (ultimaConsulta == null || new Date().getTime() - cincoMinutos > ultimaConsulta.getTime()) {
            cacheEmpresasParticipantes.put(CHAVE_BUSCA_EMPRESAS, getEmpresasParticipantesFromDB());
            ultimasConsultas.put(CHAVE_BUSCA_EMPRESAS, new Date());
        }
    }

    private List<DTOEmpresaParticipante> filter(List<DTOEmpresaParticipante> list, Integer page, Integer max, Long idSubClasseCnae, String nome, String cnpj, String codgMunicipio, String nomeBairro) {
        List<DTOEmpresaParticipante> listaRetorno = list;
        List<DTOEmpresaParticipante> listaAuxiliar = new ArrayList<>(list);

        if (codgMunicipio != null || cnpj != null || idSubClasseCnae != null || nome != null || nomeBairro != null) {
            for (DTOEmpresaParticipante dto : listaRetorno) {
                if (codgMunicipio != null) {
                    if (!dto.getCodgMunicipio().trim().equals(codgMunicipio.trim())) {
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
                if (idSubClasseCnae != null) {
                    if (!dto.getListaSubclasseCnae().contains(new DTOSubclasseCnae(idSubClasseCnae))) {
                        listaAuxiliar.remove(dto);
                        continue;
                    }
                }
                if (nome != null) {
                    if (!((dto.getNomeEmpresa() != null && dto.getNomeEmpresa().contains(nome.toUpperCase())) ||
                            (dto.getNomeFantasia() != null && dto.getNomeFantasia().contains(nome.toUpperCase())))) {
                        listaAuxiliar.remove(dto);
                        continue;
                    }
                }

                if (nomeBairro != null) {
                    if (!(dto.getNomeBairro() != null && dto.getNomeBairro().contains(nomeBairro.toUpperCase()))) {
                        listaAuxiliar.remove(dto);
                    }
                }
            }
        }
        listaRetorno = new ArrayList<>(listaAuxiliar);

        this.count.set((long) listaAuxiliar.size());

        if (page != null && max != null) {
            listaAuxiliar = new ArrayList<>();
            int init = page*max;
            int end = init + max;
            int size = listaRetorno.size();
            for (int i = init; i < end; i++) {
                if (size > i) {
                    listaAuxiliar.add(listaRetorno.get(i));
                }
            }
            listaRetorno = new ArrayList<>(listaAuxiliar);
        }
        return listaRetorno;
    }
}
