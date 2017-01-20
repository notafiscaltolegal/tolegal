package gov.to.agendamento;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import gov.to.dominio.SituacaoPontuacaoNota;
import gov.to.entidade.BilheteToLegal;
import gov.to.entidade.NotaEmpresaToLegal;
import gov.to.entidade.NotaFiscalToLegal;
import gov.to.entidade.PontuacaoBonusToLegal;
import gov.to.entidade.PontuacaoToLegal;
import gov.to.entidade.SorteioToLegal;
import gov.to.filtro.FiltroNotaEmpresaToLegal;
import gov.to.filtro.FiltroNotaFiscalToLegal;
import gov.to.filtro.FiltroPontuacaoBonusToLegal;
import gov.to.filtro.FiltroPontuacaoToLegal;
import gov.to.persistencia.ConsultasDaoJpa;
import gov.to.persistencia.GenericPersistence;
import gov.to.service.BilheteToLegalService;
import gov.to.service.BloqueioCpfToLegalService;
import gov.to.service.NotaEmpresaService;
import gov.to.service.NotaFiscalToLegalService;
import gov.to.service.PontuacaoToLegalService;
import gov.to.service.SorteioToLegalService;

public class ProcessamentoNotas implements Job {
	
	public static final String SESSION = "session";
	public static final String REPO_SORTEIO = "reposirotySorteio";
	public static final String PONT_PERSIST = "pontuacaoPersistence";
	public static final String PERSISTENCE_PONTUACAO_BONUS = "genericPontuacaoPersistence";
	public static final String PONT_BONUS_PERSIST = "pontuacaoBonusPersistence";
	public static final String NOTA_FISCAL_PERSIST = "notaFiscalPersistence";
	public static final String BILHETE_PERSIST = "bilhetePersistence";
	public static final String NOTA_FISCAL_SERVICE = "notaFiscalService";
	public static final String REPO_BILHETE = "repositorioBilhete";
	public static final String PONTUACAO_SERVICE = "pontuacaoService";
	public static final String SORTEIO_SERVICE = "sorteioService";
	public static final String BILHETE_SERVICE = "bilheteService";
	public static final String BLOQUEIO_CPF_SERVICE = "bloqueioCpfService";
	public static final String NOTA_EMPRESA_SERVICE = "notaEmpresaService";
	public static final String NOTA_EMPRESA_PERSISTENCE = "notaEmpresaPersistence";
	
	private Session session;
    
	private ConsultasDaoJpa<SorteioToLegal> reposirotySorteio;
	
	private GenericPersistence<PontuacaoToLegal, Long> pontuacaoPersistence;
	
	private GenericPersistence<PontuacaoBonusToLegal, Long> genericPontuacaoPersistence;
	
	private ConsultasDaoJpa<PontuacaoBonusToLegal> pontuacaoBonusPersistence;
	
	private GenericPersistence<NotaFiscalToLegal, Long> notaFiscalPersistence;
	
	private GenericPersistence<BilheteToLegal, Long> bilhetePersistence;
	
	private NotaFiscalToLegalService notaFiscalService;
	
	private ConsultasDaoJpa<BilheteToLegal> repositorioBilhete;
	
	private PontuacaoToLegalService pontuacaoService;
	
	private SorteioToLegalService sorteioToLegalService;
	
	private BilheteToLegalService bilheteToLegalService;
	
	private BloqueioCpfToLegalService bloqueioCpfToLegalService;
	
	private NotaEmpresaService notaEmpresaService;
	
	private GenericPersistence<NotaEmpresaToLegal, Long> notaEmpresaPersistence;
	
	@Override
	public void execute(JobExecutionContext jbContext) throws JobExecutionException {
		
		initLookup(jbContext.getJobDetail().getJobDataMap());
		
		try{
			
			SorteioToLegal sorteioToLegal = sorteioToLegalService.sorteioAtual();
			
			if (sorteioToLegal == null || !sorteioToLegal.getAtivo()){
				return;
			}
			
			this.processamentoNotas(sorteioToLegal);
			this.processaBilhetes(sorteioToLegal);
			
		}catch(Exception xException){
			xException.printStackTrace();
			
		}finally{
			getSession().flush();
			getSession().clear();
			getSession().disconnect();
		}
	}

    @SuppressWarnings("unchecked")
	private void initLookup(JobDataMap jobDataMap) {
    	
    	if (session == null){
    		
    		session = (Session) jobDataMap.get(SESSION);
    		
			if (!session.isOpen()) {
				session = session.getSessionFactory().openSession();
			}
    	}
    	
    	if (notaEmpresaPersistence == null){
    		notaEmpresaPersistence = (GenericPersistence<NotaEmpresaToLegal, Long>) jobDataMap.get(NOTA_EMPRESA_PERSISTENCE);
    	}
    	
    	if (notaEmpresaService == null){
    		notaEmpresaService = (NotaEmpresaService) jobDataMap.get(NOTA_EMPRESA_SERVICE);
    	}
    	
    	if (bloqueioCpfToLegalService == null){
    		bloqueioCpfToLegalService = (BloqueioCpfToLegalService) jobDataMap.get(BLOQUEIO_CPF_SERVICE);
    	}
    	
    	if (bilheteToLegalService == null){
    		bilheteToLegalService = (BilheteToLegalService) jobDataMap.get(BILHETE_SERVICE);
    	}
    	
    	if (sorteioToLegalService == null){
    		sorteioToLegalService = (SorteioToLegalService) jobDataMap.get(SORTEIO_SERVICE);
    	}
    	
    	if (pontuacaoService == null){
    		pontuacaoService = (PontuacaoToLegalService) jobDataMap.get(PONTUACAO_SERVICE);
    	}
    	
    	if (bilhetePersistence == null){
    		bilhetePersistence = (GenericPersistence<BilheteToLegal, Long>) jobDataMap.get(BILHETE_PERSIST);
    	}
		
    	if (repositorioBilhete == null){
    		repositorioBilhete = (ConsultasDaoJpa<BilheteToLegal>) jobDataMap.get(REPO_BILHETE);
    	}
    	
    	if (reposirotySorteio == null){
    		reposirotySorteio = (ConsultasDaoJpa<SorteioToLegal>) jobDataMap.get(REPO_SORTEIO);
    	}
    	
    	if (pontuacaoPersistence == null){
    		pontuacaoPersistence = (GenericPersistence<PontuacaoToLegal, Long>) jobDataMap.get(PONT_PERSIST);
    	}
    	
    	if (genericPontuacaoPersistence == null){
    		genericPontuacaoPersistence = (GenericPersistence<PontuacaoBonusToLegal, Long>) jobDataMap.get(PERSISTENCE_PONTUACAO_BONUS);
    	}
    	
    	if (pontuacaoBonusPersistence == null){
    		pontuacaoBonusPersistence = (ConsultasDaoJpa<PontuacaoBonusToLegal>) jobDataMap.get(PONT_BONUS_PERSIST);
    	}
    	
    	if (notaFiscalService == null){
    		notaFiscalService = (NotaFiscalToLegalService) jobDataMap.get(NOTA_FISCAL_SERVICE);
    	}
    	
    	if (notaFiscalPersistence == null){
    		notaFiscalPersistence = (GenericPersistence<NotaFiscalToLegal, Long>) jobDataMap.get(NOTA_FISCAL_PERSIST);
    	}
	}

	private void processamentoNotas(SorteioToLegal sorteioToLegal) {
    	
    	StringBuilder sql = new StringBuilder();
    	String dataInicioSorteio = null;
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
		dataInicioSorteio = sdf.format(sorteioToLegal.getDataInicioSorteio());
    	
    	String listChaveAcessoJaProcessadas = listarTodasChavesAcessoJaProcessadas(dataInicioSorteio);
    	
    	sql.append(" select n.xnfeid AS id, XNFENNF AS numNota,XNFEEXNOME AS razaoSocial, XNFEDEMI AS dataEmissao, XNFETVNF AS valor, resultp.vlrp AS valorProdCfop, XNFEDCPF AS cpf, XNFEECNPJ AS cnpj from SIATDESV.nfexml n, ");
    	sql.append(" (SELECT sum(pnfevprod) as vlrp, xnfeid FROM SIATDESV.nfexmlpr GROUP BY xnfeid) resultp ");
    	sql.append(" WHERE n.xnfeid = resultp.xnfeid ");
        sql.append(" AND n.XNFEDEMI >= to_date('"+dataInicioSorteio+"','dd/mm/yyyy') ");
        
        if (!listChaveAcessoJaProcessadas.isEmpty()){
        	 sql.append(" AND n.xnfeid not in ("+listChaveAcessoJaProcessadas+") ");
        }
        
        org.hibernate.Query query = getSession().createSQLQuery(sql.toString());
        
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> src = query.list();
		List<NotaFiscalToLegal> results = new ArrayList<NotaFiscalToLegal>(src.size());
		
		String propId ="ID";
		String propNumNota ="NUMNOTA";
		String propDataEmissao ="DATAEMISSAO";
		String propRazaoSocial = "RAZAOSOCIAL";
		String propValor = "VALOR";
		String propValorProdCfop = "VALORPRODCFOP";
		String propCpf = "CPF";
		String propCnpj = "CNPJ";
		
		for (Map<String, Object> map : src) {
			
			NotaFiscalToLegal nota = new NotaFiscalToLegal();
			
			nota.setChaveAcesso(map.get(propId).toString());
			nota.setCpf(map.get(propCpf).toString());
			nota.setCnpj(map.get(propCnpj).toString());
			
			try {
				SimpleDateFormat sd = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
				nota.setDataEmissao(sd.parse(map.get(propDataEmissao).toString()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			nota.setNumNota(map.get(propNumNota).toString());
			nota.setRazaoSocial(map.get(propRazaoSocial).toString());
			nota.setValor(Double.parseDouble(map.get(propValor).toString()));
			nota.setValorProdCfop(Double.parseDouble(map.get(propValorProdCfop).toString()));
			
			results.add(nota);
		}
        
		getSession().flush();
		getSession().clear();
		
		int i = 1;
		
		List<String> cpfsBloqueados = bloqueioCpfToLegalService.cpfsBloqueados();
        
        for (NotaFiscalToLegal nota : results){
        	
        	if (cpfsBloqueados != null && cpfsBloqueados.contains(nota.getCpf())){
        		System.out.println("#CPF BLOQUEADO# CPF:"+nota.getCpf()+" Data:"+new Date());
        		continue;
        	}
        	
    		nota.setSituacaoPontuacaoNota(SituacaoPontuacaoNota.AGUARDANDO_PROCESSAMENTO);
        	notaFiscalPersistence.salvar(nota);
        	
        	if (i % 50 == 0){
        		getSession().flush();
        	}
        	
        	++i;
        }
	}

	private String listarTodasChavesAcessoJaProcessadas(String dataInicioSorteio) {
		
		Date dataInicio = null;
		
		try {
			SimpleDateFormat sd = new SimpleDateFormat("dd/mm/yyyy");
			dataInicio = sd.parse(dataInicioSorteio);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<String> chavesAcesso = notaFiscalService.chaveAcessoPorDataEmissao(dataInicio);
		
		return formataLista(chavesAcesso);
	}
	
	private String formataLista(List<String> chavesAcesso) {
		
		Set<String> chavesAcessoComAspas = new HashSet<>();
		
		for (String str : chavesAcesso){
			
			chavesAcessoComAspas.add("'"+str+"'");
		}
		
		return StringUtils.join(chavesAcessoComAspas, ",");
	}
	
	public void processaBilhetes(SorteioToLegal sorteioToLegal) {
		
		processaNotaLegalParaGerarPontuacao(sorteioToLegal);
		
		getSession().flush();
		
		processaNotaEmpresaParaGerarPontuacao(sorteioToLegal);
		
		getSession().flush();
		
		processaPontuacaoParaGerarBilhete(sorteioToLegal);
		
		getSession().flush();
		
		processaPontuacaoBonusParaGerarBilhete(sorteioToLegal);
		
		getSession().flush();
	}

	private void processaNotaEmpresaParaGerarPontuacao(SorteioToLegal sorteioToLegal) {
		
		FiltroNotaEmpresaToLegal filtroNotaEmpresa = new FiltroNotaEmpresaToLegal();
		filtroNotaEmpresa.setSituacaoPontuacaoNota(SituacaoPontuacaoNota.AGUARDANDO_PROCESSAMENTO);
		List<NotaEmpresaToLegal> listNotaEmpresa = notaEmpresaService.pesquisar(filtroNotaEmpresa);
		
		List<String> cpfsBloqueados = bloqueioCpfToLegalService.cpfsBloqueados();
		
		for (NotaEmpresaToLegal nota : listNotaEmpresa){
			
			if (cpfsBloqueados != null && cpfsBloqueados.contains(nota.getCpfDestinatario())){
				continue;
			}
			
			PontuacaoToLegal pontuacao = new PontuacaoToLegal(nota);
			pontuacao.setSorteioToLegal(sorteioToLegal);
			pontuacaoPersistence.salvar(pontuacao);
			
			getSession().flush();
			
			nota.setSituacaoPontuacaoNota(SituacaoPontuacaoNota.PONTUADO);
			notaEmpresaPersistence.merge(nota);
		}
	}

	private void processaNotaLegalParaGerarPontuacao(SorteioToLegal sorteioToLegal) {
		
		FiltroNotaFiscalToLegal filtroNota = new FiltroNotaFiscalToLegal();
		filtroNota.setSituacaoPontuacaoNota(SituacaoPontuacaoNota.AGUARDANDO_PROCESSAMENTO);
		List<NotaFiscalToLegal> listNotaFiscal = notaFiscalService.pesquisar(filtroNota);
		
		for (NotaFiscalToLegal nota : listNotaFiscal){
			
			PontuacaoToLegal pontuacao = new PontuacaoToLegal(nota);
			pontuacao.setSorteioToLegal(sorteioToLegal);
			pontuacaoPersistence.salvar(pontuacao);
			
			getSession().flush();
			
			nota.setSituacaoPontuacaoNota(SituacaoPontuacaoNota.PONTUADO);
			notaFiscalPersistence.merge(nota);
		}
	}

	private void processaPontuacaoBonusParaGerarBilhete(SorteioToLegal sorteioToLegal) {
		
		FiltroPontuacaoBonusToLegal filtro = new FiltroPontuacaoBonusToLegal();
		filtro.setSituacaoPontuacaoNota(SituacaoPontuacaoNota.AGUARDANDO_PROCESSAMENTO);
		filtro.setIdSorteio(sorteioToLegal.getId());
		List<PontuacaoBonusToLegal> listPontuacaoBonus = pontuacaoBonusPersistence.filtrarPesquisa(filtro, PontuacaoBonusToLegal.class);
		
		for (PontuacaoBonusToLegal pontuacaoBonus : listPontuacaoBonus){
			
			bilheteToLegalService.processaBilhetePorPontuacaoBonus(sorteioToLegal, pontuacaoBonus);
			getSession().flush();
		}
	}

	private void processaPontuacaoParaGerarBilhete(SorteioToLegal sorteioToLegal) {
		
		FiltroPontuacaoToLegal filtroPont = new FiltroPontuacaoToLegal();
		filtroPont.setSituacaoPontuacaoNota(SituacaoPontuacaoNota.AGUARDANDO_PROCESSAMENTO);
		List<PontuacaoToLegal> listPontuacao = pontuacaoService.pesquisar(filtroPont);
		
		Map<String, Integer> mapPontuacao = new HashMap<>();
		
		for (PontuacaoToLegal pontuacao : listPontuacao){
			
			String cpf = null;
			
			if (pontuacao.getNotaFiscalToLegal() == null){
				
				cpf = pontuacao.getNotaFiscalEmpresaToLegal().getCpfDestinatario();
				
			}else if (pontuacao.getNotaFiscalEmpresaToLegal() == null){
				
				cpf = pontuacao.getNotaFiscalToLegal().getCpf();
			}
			
			if (mapPontuacao.get(cpf) == null){
				mapPontuacao.put(cpf, pontuacao.getQntPonto());
			}else{
				mapPontuacao.put(cpf, mapPontuacao.get(cpf) + pontuacao.getQntPonto());
			}
		}
		
		for (Entry<String, Integer> entry : mapPontuacao.entrySet()){
			
			String cpf = entry.getKey();
			Integer totalPonto = entry.getValue();
			
			bilheteToLegalService.processaBilhetePorPontuacao(sorteioToLegal, cpf, totalPonto);
			getSession().flush();
		}
		
		for (PontuacaoToLegal pontuacao : listPontuacao){
			
			pontuacao.setSituacaoPontuacao(SituacaoPontuacaoNota.PONTUADO);
			pontuacaoPersistence.merge(pontuacao);
		}
	}

	public Session getSession(){
		
		if (!session.isOpen()) {
			session = session.getSessionFactory().openSession();
		}
		
		return session;
	}
}