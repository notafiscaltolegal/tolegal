package gov.to.service;

import javax.ejb.Local;

import gov.to.dto.PaginacaoContribuinteDTO;
import gov.to.entidade.ContribuinteToLegal;

/**
 * 
 * @author pedro.oliveira
 * 
 *
 */
@Local
public interface ContribuinteToLegalService {

	PaginacaoContribuinteDTO findContribuintes(Integer page, Integer max, String cnpjBase, Integer numrInscricao, String cnpj, String nome);
	
    ContribuinteToLegal findByInscricaoEstadual(Integer inscricaoEstadual);
}
