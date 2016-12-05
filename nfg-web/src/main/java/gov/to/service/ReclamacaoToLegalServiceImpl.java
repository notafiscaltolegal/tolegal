package gov.to.service;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.springframework.web.multipart.MultipartFile;

import gov.goias.entidades.DocumentoFiscalReclamado;
import gov.goias.entidades.PessoaParticipante;
import gov.goias.exceptions.NFGException;
import gov.goias.service.PaginacaoDTO;
import gov.to.dominio.MotivoReclamacaoEnum;
import gov.to.dominio.ProblemaEmpresaEnum;
import gov.to.dominio.TipoDocumentoFiscalEnum;
import gov.to.entidade.ContribuinteToLegal;
import gov.to.entidade.ReclamacaoToLegal;
import gov.to.entidade.UsuarioToLegal;
import gov.to.filtro.FiltroReclamacaoToLegal;
import gov.to.persistencia.ConsultasDaoJpa;

@Stateless
public class ReclamacaoToLegalServiceImpl extends ConsultasDaoJpa<ReclamacaoToLegal> implements ReclamacaoToLegalService{
	
	@EJB
	private GenericService<ReclamacaoToLegal, Long> servicoReclamacao;
	
	@EJB
	private GenericService<UsuarioToLegal, Long> servicoUsuarioToLegal;
	
	@EJB
	private ContribuinteToLegalService serviceContribuinte;

	@Override
	public PaginacaoDTO<DocumentoFiscalReclamado> findReclamacoesDoCidadao(PessoaParticipante cidadao, Integer page, Integer max) {
		
		PaginacaoDTO<DocumentoFiscalReclamado> paginacaoDTO = new PaginacaoDTO<>();
		
		FiltroReclamacaoToLegal filtro = new FiltroReclamacaoToLegal();
		
		filtro.setIdUsuario(cidadao.getId().longValue());
		
		List<ReclamacaoToLegal> listReclamacoes = super.filtrarPesquisa(filtro, ReclamacaoToLegal.class, "usuarioToLegal");
		
		paginacaoDTO.setCount(listReclamacoes.size());
		
		
		
		return paginacaoDTO;
	}
	
	@Override
	public void cadastraNovaReclamacao(Integer tipoDocFiscalReclamacao, Integer codgMotivo,
			Date dataEmissaoDocFiscal, Integer numeroReclamacao, Integer iEReclamacao, Double valorReclamacao,
			MultipartFile fileReclamacao, PessoaParticipante cidadao, boolean dataDentroDoPrazo, Integer problemaEmpresaReclamacao) throws Exception {
		
		
		
		ContribuinteToLegal contribuinte=serviceContribuinte.findByInscricaoEstadual(iEReclamacao);
		
		if(contribuinte==null){
			throw new NFGException("Inscrição estadual inválida.");
		}
		
		ReclamacaoToLegal reclamacao=new ReclamacaoToLegal();
		reclamacao.setTipoDocFiscal(TipoDocumentoFiscalEnum.convertCodTipoDocumentoFiscalParaEnum(codgMotivo)); 
		reclamacao.setMotivoReclamacao(MotivoReclamacaoEnum.convertCodigoMotivoParaEnum(codgMotivo)); 
		reclamacao.setDataEmissaoDocFiscal(dataEmissaoDocFiscal);
		reclamacao.setNumeroDocFiscal(numeroReclamacao.toString());
		reclamacao.setInscricaoEstadual(iEReclamacao.toString());
		reclamacao.setValorDocFiscal(valorReclamacao);
		reclamacao.setProblemaEmpresa(ProblemaEmpresaEnum.convertCodProblemaEmpresaEnumParaEnum(problemaEmpresaReclamacao));
		reclamacao.setAnexoNota(fileReclamacao.getBytes());
		
		
		reclamacao.setDataCadastro(new Date());
        reclamacao.setUsuarioToLegal(servicoUsuarioToLegal.getById(UsuarioToLegal.class, cidadao.getId().longValue()));		
		servicoReclamacao.salvar(reclamacao);
	}
}