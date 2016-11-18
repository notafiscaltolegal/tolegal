package gov.to.agendamento;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import gov.to.entidade.BilheteToLegal;
import gov.to.entidade.NotaFiscalToLegal;
import gov.to.entidade.PontuacaoBonusToLegal;
import gov.to.entidade.PontuacaoToLegal;
import gov.to.entidade.SorteioToLegal;
import gov.to.persistencia.AbstractModel;
import gov.to.persistencia.ConsultasDaoJpa;
import gov.to.persistencia.GenericPersistence;
import gov.to.service.NotaFiscalToLegalService;

@Singleton
@Startup
public class SchedulerProcessamentoNotas extends AbstractModel {
	
	@EJB
	private ConsultasDaoJpa<SorteioToLegal> reposirotySorteio;
	
	@EJB
	private ConsultasDaoJpa<BilheteToLegal> repositorioBilhete;
	
	@EJB
	private GenericPersistence<PontuacaoToLegal, Long> pontuacaoPersistence;
	
	@EJB
	private GenericPersistence<PontuacaoBonusToLegal, Long> genericPontuacaoPersistence;
	
	@EJB
	private ConsultasDaoJpa<PontuacaoBonusToLegal> pontuacaoBonusPersistence;
	
	@EJB
	private GenericPersistence<NotaFiscalToLegal, Long> notaFiscalPersistence;
	
	@EJB
	private GenericPersistence<BilheteToLegal, Long> bilhetePersistence;
	
	@EJB
	private NotaFiscalToLegalService notaFiscalService;

	@PostConstruct
	public void inicio(){
		
		JobDetail job = JobBuilder.newJob(ProcessamentoNotas.class).build();
		
		JobDataMap jobDataMap = job.getJobDataMap();
		
		jobDataMap.put(ProcessamentoNotas.SESSION, getSession());
		jobDataMap.put(ProcessamentoNotas.REPO_BILHETE, repositorioBilhete);
		jobDataMap.put(ProcessamentoNotas.PONT_BONUS_PERSIST, pontuacaoBonusPersistence);
		jobDataMap.put(ProcessamentoNotas.REPO_SORTEIO, reposirotySorteio);
		jobDataMap.put(ProcessamentoNotas.BILHETE_PERSIST, bilhetePersistence);
		jobDataMap.put(ProcessamentoNotas.NOTA_FISCAL_PERSIST, notaFiscalPersistence);
		jobDataMap.put(ProcessamentoNotas.PONT_PERSIST, pontuacaoPersistence);
		jobDataMap.put(ProcessamentoNotas.NOTA_FISCAL_SERVICE, notaFiscalService);
		
		Trigger trigger = TriggerBuilder
			.newTrigger()
			.withSchedule(
				SimpleScheduleBuilder.simpleSchedule()
					.withIntervalInSeconds(60).repeatForever())
			.build();
		
		Scheduler scheduler;
		
		try {
			
			scheduler = new StdSchedulerFactory().getScheduler();
			
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
			
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
