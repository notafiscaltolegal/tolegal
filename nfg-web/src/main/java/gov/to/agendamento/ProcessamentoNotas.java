package gov.to.agendamento;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import gov.to.dominio.SituacaoBilhete;
import gov.to.dominio.SituacaoBonusPontuacao;
import gov.to.dominio.SituacaoPontuacaoNota;
import gov.to.entidade.BilheteToLegal;
import gov.to.entidade.NotaFiscalToLegal;
import gov.to.entidade.PontuacaoBonusToLegal;
import gov.to.entidade.PontuacaoToLegal;
import gov.to.entidade.SorteioToLegal;
import gov.to.filtro.FiltroNotaFiscalToLegal;
import gov.to.filtro.FiltroPontuacaoBonusToLegal;
import gov.to.filtro.FiltroSorteioToLegal;
import gov.to.persistencia.ConsultasDaoJpa;
import gov.to.persistencia.GenericPersistence;
import gov.to.service.NotaFiscalToLegalService;
import gov.to.util.SorteioProperties;

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
	
	private Session session;
    
	private ConsultasDaoJpa<SorteioToLegal> reposirotySorteio;
	
	private GenericPersistence<PontuacaoToLegal, Long> pontuacaoPersistence;
	
	private GenericPersistence<PontuacaoBonusToLegal, Long> genericPontuacaoPersistence;
	
	private ConsultasDaoJpa<PontuacaoBonusToLegal> pontuacaoBonusPersistence;
	
	private GenericPersistence<NotaFiscalToLegal, Long> notaFiscalPersistence;
	
	private GenericPersistence<BilheteToLegal, Long> bilhetePersistence;
	
	private NotaFiscalToLegalService notaFiscalService;
	
	private ConsultasDaoJpa<BilheteToLegal> repositorioBilhete;
	
	@Override
	public void execute(JobExecutionContext jbContext) throws JobExecutionException {
		
		initLookup(jbContext.getJobDetail().getJobDataMap());
		
		this.processamentoNotas();
		this.processaBilhetes();
	}

    @SuppressWarnings("unchecked")
	private void initLookup(JobDataMap jobDataMap) {
    	
    	if (session == null){
    		
    		session = (Session) jobDataMap.get(SESSION);
    		
			if (!session.isOpen()) {
				session = session.getSessionFactory().openSession();
			}
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

	private void processamentoNotas() {
    	
    	StringBuilder sql = new StringBuilder();
    	String dataInicioSorteio = "01/01/2016";
    	String dataFimSorteio = "08/11/2016";
    	String listChaveAcessoJaProcessadas = listarTodasChavesAcessoJaProcessadas(dataInicioSorteio, dataFimSorteio);
    	
    	sql.append(" select n.xnfeid AS id, XNFENNF AS numNota,XNFEEXNOME AS razaoSocial, XNFEDEMI AS dataEmissao, XNFETVNF AS valor, resultp.vlrp AS valorProdCfop, XNFEDCPF AS cpf, XNFEECNPJ AS cnpj from SIATDESV.nfexml n, ");
    	sql.append(" (SELECT sum(pnfevprod) as vlrp, xnfeid FROM SIATDESV.nfexmlpr GROUP BY xnfeid) resultp ");
    	sql.append(" WHERE n.xnfeid = resultp.xnfeid ");
        sql.append(" AND n.XNFEDEMI >= to_date('"+dataInicioSorteio+"','dd/mm/yyyy') AND n.XNFEDEMI <= to_date('"+dataFimSorteio+"','dd/mm/yyyy') ");
        
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
        
        for (NotaFiscalToLegal nota : results){
        	
        	System.out.println("Incluindo notas ainda não processadas no TO LEGAL para pontuação.");
        	
    		nota.setSituacaoPontuacaoNota(SituacaoPontuacaoNota.AGUARDANDO_PROCESSAMENTO);
        	notaFiscalPersistence.salvar(nota);
        }
	}

	private String listarTodasChavesAcessoJaProcessadas(String dataInicioSorteio, String dataFimSorteio) {
		
		Date dataInicio = null;
		Date dataFim = null;
		
		try {
			SimpleDateFormat sd = new SimpleDateFormat("dd/mm/yyyy");
			dataInicio = sd.parse(dataInicioSorteio);
			dataFim = sd.parse(dataFimSorteio);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<String> chavesAcesso = notaFiscalService.chaveAcessoPorDataEmissao(dataInicio, dataFim);
		
		return formataLista(chavesAcesso);
	}

	private String formataLista(List<String> chavesAcesso) {
		
		Set<String> chavesAcessoComAspas = new HashSet<>();
		
		for (String str : chavesAcesso){
			
			chavesAcessoComAspas.add("'"+str+"'");
		}
		
		return StringUtils.join(chavesAcessoComAspas, ",");
	}
	
	public void processaBilhetes() {
		
		FiltroNotaFiscalToLegal filtroNota = new FiltroNotaFiscalToLegal();
		filtroNota.setSituacaoPontuacaoNota(SituacaoPontuacaoNota.AGUARDANDO_PROCESSAMENTO);
		List<NotaFiscalToLegal> listNotaFiscal = notaFiscalService.pesquisar(filtroNota);
		
		FiltroSorteioToLegal filtroSorteio = new FiltroSorteioToLegal();
		filtroSorteio.setNumeroSorteio(SorteioProperties.getValue(SorteioProperties.NUMERO_SORTEIO));
		SorteioToLegal sorteioToLegal = reposirotySorteio.primeiroRegistroPorFiltro(filtroSorteio, SorteioToLegal.class);
		
		for (NotaFiscalToLegal nota : listNotaFiscal){
			
			PontuacaoToLegal pontuacao = new PontuacaoToLegal(nota);
			
			pontuacao.setSorteioToLegal(sorteioToLegal);
			
			pontuacaoPersistence.salvar(pontuacao);
			System.out.println("REALIZA PONTUAÇÂO DA NOTA "+nota.getChaveAcesso());
			
			getSession().flush();
			getSession().clear();
			
			nota.setSituacaoPontuacaoNota(SituacaoPontuacaoNota.PONTUADO);
			notaFiscalPersistence.merge(nota);
			System.out.println("MARCA NOTA COMO PONTUADA");
		}
		
		List<PontuacaoToLegal> listPontuacao = pontuacaoPersistence.listarTodos(PontuacaoToLegal.class);
		
		for (PontuacaoToLegal pontuacao : listPontuacao){
			
			Integer qntBilhetes = pontuacao.getQntPonto() / SorteioProperties.getValue(SorteioProperties.QNT_PONTOS_POR_BILHETE);
			
			for (int i=BigInteger.ZERO.intValue(); i < qntBilhetes; i++){
				
				BilheteToLegal bilheteToLegal = new BilheteToLegal();
				bilheteToLegal.setSorteioToLegal(sorteioToLegal);
				bilheteToLegal.setNumeroSeqBilhete(geraNumeroBilhete());
				bilheteToLegal.setStBilhete(SituacaoBilhete.VALIDO);
				bilheteToLegal.setCpf(pontuacao.getNotaFiscalToLegal().getCpf());
				
				bilhetePersistence.salvar(bilheteToLegal);
			}
		}
		
		FiltroPontuacaoBonusToLegal filtro = new FiltroPontuacaoBonusToLegal();
		
		filtro.setSituacaoBonus(SituacaoBonusPontuacao.ATIVO);
		List<PontuacaoBonusToLegal> listPontuacaoBonus = pontuacaoBonusPersistence.filtrarPesquisa(filtro, PontuacaoBonusToLegal.class);
		
		for (PontuacaoBonusToLegal pontuacaoBonus : listPontuacaoBonus){
			
			if (pontuacaoBonus.getDataLimiteBonus() != null && pontuacaoBonus.getDataLimiteBonus().getTime() < new Date().getTime()){
				
				pontuacaoBonus.setSituacaoBonusPontuacao(SituacaoBonusPontuacao.INATIVO);
				
				genericPontuacaoPersistence.merge(pontuacaoBonus);
				continue;
			}
			
			Integer qntBilhetes = pontuacaoBonus.getQntPonto() / SorteioProperties.getValue(SorteioProperties.QNT_PONTOS_POR_BILHETE);
			
			for (int i=BigInteger.ZERO.intValue(); i < qntBilhetes; i++){
				
				BilheteToLegal bilheteToLegal = new BilheteToLegal();
				bilheteToLegal.setSorteioToLegal(sorteioToLegal);
				bilheteToLegal.setNumeroSeqBilhete(geraNumeroBilhete());
				bilheteToLegal.setStBilhete(SituacaoBilhete.VALIDO);
				bilheteToLegal.setCpf(pontuacaoBonus.getCpf());
				
				bilhetePersistence.salvar(bilheteToLegal);
			}
		}
	}

	private Integer geraNumeroBilhete() {
		
		Criteria criteria = getSession().createCriteria(BilheteToLegal.class);
		
		criteria.setProjection(Projections.max("numeroSeqBilhete"));
		Integer max = (Integer)criteria.uniqueResult();
		
		if (max == null){
			return BigInteger.TEN.intValue();
		}
		
		return ++max;
	}
	
	public Session getSession(){
		
		if (!session.isOpen()) {
			session = session.getSessionFactory().openSession();
		}
		
		return session;
	}
}
