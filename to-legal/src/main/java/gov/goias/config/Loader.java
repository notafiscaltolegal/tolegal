package gov.goias.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * A classe Loader foi criada com o principal intuito de facilitar
 * a execucao de um codigo na inicializacao da aplicacao
 * logo apos a criacao do contexto, ou seja, apos terem sido amarradas as dependencias.
 *
 * Obs.: A execucao de metodos JPA apresentou problemas (testar depois em
 * outros contextos sem ser o Root). O JDBCTemplate funcionou bem.
 *
 * */

@Scope("singleton")
public class Loader implements ApplicationListener<ContextRefreshedEvent> {

//    @Autowired
//    DocumentoFiscalReclamadoToLegal reclamacaoRepository;

    public void executeWhenRootContextStarts() {
//        if (System.getProperty("spring.profiles.active").equals("homolog")) {
//            logger.info("Começando refazBuscasReclamacao()...");
//            Integer[] idsReclamacoes = {402};
//            refazBuscasReclamacao(idsReclamacoes);
//        }
//
//        if (System.getProperty("spring.profiles.active").equals("prod")) {
//            logger.info("Começando refazBuscasReclamacao()...");
//            Integer[] idsReclamacoes =
//                    {
//                    };
//            refazBuscasReclamacao(idsReclamacoes);
//        }
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void refazBuscasReclamacao(Integer[] idsReclamacoes) {
//        try{
//
//            for (Integer idReclamacao: idsReclamacoes){
//                logger.info("refazBuscasReclamacao(" + idReclamacao+")...");
//                reclamacaoRepository.refazerBuscaAutomaticaDeReclamacaoExistente(idReclamacao);
//                Thread.sleep(2000);
//            }
//        }catch (Exception e){
//            logger.error("refazBuscasReclamacao() FALHOU!");
//            e.printStackTrace();
//        }
    }

    private boolean executado;

    public Loader(){
        executado=false;
    }

    private Logger logger = LoggerFactory.getLogger(Loader.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
//        String eventName = event.getApplicationContext().getDisplayName();
//        logger.info("App Context:"+ eventName);
//
//        if (!executado && eventName.contains("Root")){
//            executeWhenRootContextStarts();
//            executado=true;
//        }
    }

}
