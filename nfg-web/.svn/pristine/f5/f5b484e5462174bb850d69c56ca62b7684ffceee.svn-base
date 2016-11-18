package gov.goias.persistencia;

import entidade.Credencial;
import gov.goias.entidades.GENPessoaFisica;
import gov.goias.entidades.PessoaParticipante;
import gov.goias.entidades.RFEPessoaFisica;
import gov.goias.services.IGenPessoaFisicaService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author lucas-mp
 * @since 14/10/2014
 */
@Repository
public class MonitorRepository extends GenericRepository<Integer, PessoaParticipante> {

    private static final Logger logger = Logger.getLogger(MonitorRepository.class);

    @Autowired
    GENPessoaFisica genPessoaFisicaRepository;

    @Autowired
    RFEPessoaFisica rfePessoaFisicaRepository;

    @Autowired
    IGenPessoaFisicaService genService;

    @Autowired
    PessoaParticipante pessoaParticipanteRepository;


     public int efetuarMonitoramentoNFGWeb() {
        try{

            String cpf = "03794778154";

            GENPessoaFisica genPessoaFisica = genPessoaFisicaRepository.findByCpf(cpf);
            PessoaParticipante pessoaParticipante = pessoaParticipanteRepository.findByGenPessoaCPF(genPessoaFisica);

            if (pessoaParticipante==null) return 1;//ERRO NO NFG


            Credencial credencial = genService.getSenhaPessoaFisicaWs(cpf);
            String hashSenhaServidor = credencial!=null? credencial.getInfoSenha():null;


            if(pessoaParticipante!=null && hashSenhaServidor==null){
                return 2; //ERRO NO GEN
            }

            return 0; //SUCESSO
        }catch (Exception e){
            logger.error("Erro ocorreu no monitoramento: "+e.getMessage());
            e.printStackTrace();
            return 1; //ERRO no NFG
        }

    }

}

