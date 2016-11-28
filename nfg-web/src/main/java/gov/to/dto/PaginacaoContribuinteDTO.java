package gov.to.dto;

import java.util.List;

import gov.goias.dtos.DTOContribuinte;

public class PaginacaoContribuinteDTO {
	
	private int countPaginacao;
	
	private List<DTOContribuinte> listContribuinteDTO;

	public int getCountPaginacao() {
		return countPaginacao;
	}

	public void setCountPaginacao(int countPaginacao) {
		this.countPaginacao = countPaginacao;
	}

	public List<DTOContribuinte> getListContribuinteDTO() {
		return listContribuinteDTO;
	}

	public void setListContribuinteDTO(List<DTOContribuinte> listContribuinteDTO) {
		this.listContribuinteDTO = listContribuinteDTO;
	}
}