package gov.to.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import gov.goias.service.PaginacaoDTO;
import gov.to.dominio.SerieEnum;
import gov.to.dominio.TipoDocumentoEnum;
import gov.to.entidade.NotaEmpresaToLegal;
import gov.to.filtro.FiltroNotaEmpresaToLegal;
import gov.to.goias.DocumentoFiscalDigitadoToLegal;
import gov.to.persistencia.ConsultasDaoJpa;

@Stateless
public class NotaEmpresaServiceImpl extends ConsultasDaoJpa<NotaEmpresaToLegal> implements NotaEmpresaService {

	// private ConsultasDaoJpa<NotaEmpresaToLegal> reposiroty;
	
	@EJB
	private GenericService<NotaEmpresaToLegal, Long> servicoNotaEmpresa;
	
	@EJB
	private PontuacaoToLegalService pontuacaoToLegalService;


	@Override
	public DocumentoFiscalDigitadoToLegal ultimaNotaValida(Integer inscricaoEstadual, Integer numeroDocumentoFiscal,
			Integer serieNotaFiscal, String subSerieNotaFiscal, Date dataEmissao, Integer tipoDocumentoFiscal) {

		FiltroNotaEmpresaToLegal filtroDocumentoToLegal = new FiltroNotaEmpresaToLegal();

		if (numeroDocumentoFiscal != null) {
			filtroDocumentoToLegal.setNumeroDocumento(numeroDocumentoFiscal.toString());
		}
		
		if (inscricaoEstadual != null) {
			filtroDocumentoToLegal.setInscricaoEstadual(inscricaoEstadual.toString());
		}


		// filtroDocumentoToLegal.setDataEmissao(dataEmissao);
		// filtroDocumentoToLegal.setSerieEnum(SerieEnum.convertCodTipoDocumentoFiscalParaEnum(serieNotaFiscal));

		NotaEmpresaToLegal nota = super.primeiroRegistroPorFiltro(filtroDocumentoToLegal, NotaEmpresaToLegal.class);

		DocumentoFiscalDigitadoToLegal doc = null;
		
		if (nota != null) {
			
			doc = new DocumentoFiscalDigitadoToLegal();

			doc.setNumeroDocumentoFiscal(Integer.parseInt(nota.getNumeroDocumento()));
			doc.setCpf(nota.getCpfDestinatario());
            doc.setSerieNotaFiscal(nota.getSerie().getCodigo());
			doc.setValorTotal(nota.getValor());
			doc.setSubSerieNotaFiscal(nota.getSubSerie());
			doc.setTipoDocumentoFiscal(nota.getTipoDocumento().getCodigo());
			doc.setId(nota.getId().intValue());
		}
		// doc.setCpf("97464104153");
		return doc;
	}

	@Override
	public void cadastrarNota(Integer numeroDocumentoFiscal, Integer serieNotaFiscal, String subSerieNotaFiscal,
			Date dataEmissao, String cpf, Double valorTotal, Integer tipoDocumentoFiscal, Date date, Integer inscricaoEstadual) {
		// TODO Auto-generated method stub
		NotaEmpresaToLegal nota=new NotaEmpresaToLegal();
		
		nota.setNumeroDocumento(numeroDocumentoFiscal.toString());
		nota.setSerie(SerieEnum.convertCodTipoDocumentoFiscalParaEnum(serieNotaFiscal));
		nota.setSubSerie(subSerieNotaFiscal);
		nota.setDataEmissao(dataEmissao);
		nota.setCpfDestinatario(cpf);
		nota.setValor(valorTotal);
		nota.setTipoDocumento(TipoDocumentoEnum.convertCodTipoDocumentoFiscalParaEnum(tipoDocumentoFiscal));
		nota.setInscricaoEstadual(inscricaoEstadual.toString());
		servicoNotaEmpresa.salvar(nota);
		
		
	}

	@Override
	public void atualizarNota(DocumentoFiscalDigitadoToLegal ultimaNotaValida, Integer numeroDocumentoFiscal,
			Integer serieNotaFiscal, String subSerieNotaFiscal, Date dataEmissao, String cpf, Double valorTotal,
			Integer tipoDocumentoFiscal, Integer inscricaoEstadual) {
		
        NotaEmpresaToLegal nota=new NotaEmpresaToLegal();
		
		nota.setNumeroDocumento(numeroDocumentoFiscal.toString());
		nota.setSerie(SerieEnum.convertCodTipoDocumentoFiscalParaEnum(serieNotaFiscal));
		nota.setSubSerie(subSerieNotaFiscal);
		nota.setDataEmissao(dataEmissao);
		nota.setCpfDestinatario(cpf);
		nota.setValor(valorTotal);
		nota.setTipoDocumento(TipoDocumentoEnum.convertCodTipoDocumentoFiscalParaEnum(tipoDocumentoFiscal));
		nota.setInscricaoEstadual(inscricaoEstadual.toString());
		nota.setId(ultimaNotaValida.getId().longValue());
		servicoNotaEmpresa.merge(nota);
		
		
	}

	@Override
	public boolean existePontuacaoParaODocumento(DocumentoFiscalDigitadoToLegal ultimaNotaValida) {
		
		return pontuacaoToLegalService.notaEmpresaPontuada(ultimaNotaValida.getId());
	}

	@Override
	public DocumentoFiscalDigitadoToLegal documentoFiscalPorId(Integer idDocumentoFiscalDigital) {
		return null;
	}

	@Override
	public void alterar(DocumentoFiscalDigitadoToLegal documento) {
		
	}

	@Override
	public PaginacaoDTO<DocumentoFiscalDigitadoToLegal> ultimasNotasInseridas(Integer ieFiltro, Integer nrDocFiltro,
			String dataEmissao, String cpfFiltro, int ig, Integer max) {
		
		PaginacaoDTO<DocumentoFiscalDigitadoToLegal> paginacaoDTO = new PaginacaoDTO<>();
		
		FiltroNotaEmpresaToLegal filtro = new FiltroNotaEmpresaToLegal();
		
		try {
			filtro.setCpfDestinatario(cpfFiltro);
		} catch (Exception e) {
			
		}		
		
		try
		{
		filtro.setNumeroDocumento(nrDocFiltro.toString());
		}catch(Exception ex)
		{			
			
		}
		
		Date date=new Date();
		try		
		{
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		date = (Date)formatter.parse(dataEmissao);
		filtro.setDataEmissao(date);
		}catch(Exception ex)
		{
			
		}
		
		
		try
		{
		filtro.setInscricaoEstadual(ieFiltro.toString());
		}catch(Exception ex)
		{			
			
		}
		
		List<NotaEmpresaToLegal> listNotaEmpresa = super.filtrarPesquisa(filtro, NotaEmpresaToLegal.class);
		
		int inicio = calcInicio(ig, max);
		int fim = calcPagFim(ig, max);

		if (listNotaEmpresa != null) {
			paginacaoDTO.setCount(listNotaEmpresa.size());
		}

		List<DocumentoFiscalDigitadoToLegal> listsDocs = new ArrayList<>();
		;
		
		for (int i = inicio; i < fim; i++) {

			DocumentoFiscalDigitadoToLegal doc = new DocumentoFiscalDigitadoToLegal();

			if (i == listNotaEmpresa.size()) {
				break;
			}

			NotaEmpresaToLegal not = listNotaEmpresa.get(i);
		//	ContribuinteToLegal contribuinte = serviceContribuinte
			//		.findByInscricaoEstadual(Integer.valueOf(rec.getInscricaoEstadual()));
			if (not != null) {

				doc.setCpf(not.getCpfDestinatario());
				doc.setNumeroDocumentoFiscal(Integer.valueOf(not.getNumeroDocumento()));
				doc.setDataEmissao(not.getDataEmissao());
				doc.setValorTotal(not.getValor());
				doc.setSerieNotaFiscal(not.getSerie().getCodigo());
				doc.setSubSerieNotaFiscal(not.getSubSerie());
				doc.setTipoDocumentoFiscal(not.getTipoDocumento().getCodigo());
				listsDocs.add(doc);
			}
		}
		
		paginacaoDTO.setList(listsDocs);
		;
		return paginacaoDTO;
	}
	
	
	private static int calcPagFim(Integer page, Integer max) {

		return (calcInicio(page, max) + max) - 1;
	}

	private static int calcInicio(Integer page, Integer max) {

		return (page * max);
	}

	@Override
	public void excluir(NotaEmpresaToLegal nota) {		
		servicoNotaEmpresa.excluir(nota);		
	}

	@Override
	public NotaEmpresaToLegal buscarNotaEmpresaToLegalPorId(Integer idDocumentoFiscalDigital) {
		
		NotaEmpresaToLegal nota=servicoNotaEmpresa.getById(NotaEmpresaToLegal.class, idDocumentoFiscalDigital.longValue());
		
		
		return nota;
	}

	@Override
	public List<NotaEmpresaToLegal> pesquisar(FiltroNotaEmpresaToLegal filtroNotaEmpresa) {
		
		return super.filtrarPesquisa(filtroNotaEmpresa, NotaEmpresaToLegal.class);
	}

	
	
	/*
	 * @EJB private ConsultasDaoJpa<NotaFiscalToLegal> reposiroty;
	 * 
	 * @Override public List<NotaFiscalToLegal>
	 * pesquisar(FiltroNotaFiscalToLegal filtro, String... hbInitialize) {
	 * return reposiroty.filtrarPesquisa(filtro, NotaFiscalToLegal.class,
	 * hbInitialize); }
	 * 
	 * @Override public NotaFiscalToLegal
	 * primeiroRegistro(FiltroNotaFiscalToLegal filtro, String... hbInitialize)
	 * { return reposiroty.primeiroRegistroPorFiltro(filtro,
	 * NotaFiscalToLegal.class, hbInitialize); }
	 * 
	 * @Override public int totalNotasPorCpf(String cpf) {
	 * 
	 * Criteria criteria =
	 * reposiroty.getSession().createCriteria(NotaFiscalToLegal.class);
	 * 
	 * Long count = (Long) criteria .setProjection(Projections.count("id"))
	 * .add(Restrictions.eq("cpf", cpf)) .uniqueResult();
	 * 
	 * return count.intValue(); }
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public List<String> chaveAcessoPorDataEmissao(Date dataInicio,
	 * Date dataFim) {
	 * 
	 * SimpleDateFormat sp = new SimpleDateFormat("dd/mm/yyyy");
	 * 
	 * String dtInicio = sp.format(dataInicio); String dtFim =
	 * sp.format(dataFim);
	 * 
	 * Query qr = reposiroty.getSession().
	 * createSQLQuery("SELECT chave_acesso FROM TB_NOTA_LEGAL WHERE data_emissao >= to_date('"
	 * +dtInicio+"','dd/mm/yyyy') AND data_emissao <= to_date('"
	 * +dtFim+"','dd/mm/yyyy') ");
	 * 
	 * qr.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
	 * 
	 * List<Map<String, Object>> src = qr.list(); List<String> list = new
	 * ArrayList<String>();
	 * 
	 * for (Map<String, Object> map : src) {
	 * list.add(map.get("CHAVE_ACESSO").toString()); }
	 * 
	 * return list; }
	 */
}