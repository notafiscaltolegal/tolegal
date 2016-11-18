package gov.goias.services;

import gov.goias.entidades.*;
import gov.goias.exceptions.NFGException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author henrique-rh
 * @since 02/09/2014
 */
@Service
@Configurable
public class ContribuinteService {

    @Autowired
    EmpresaParticipante empresaParticipanteRepository;

    @Autowired
    private CCEContribuinte cceContribuinteRepository;

    @Autowired
    private CnaeAutorizado cnaeAutorizadoRepository;

    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    private void cadastrarContribuinteAoNFG(CCEContribuinte contribuinte, String dataInicio, Character indiResponsavel, Integer idPessoaAdesao, String termoDeAcordo, String redirectUrl) throws NFGException {
        if(!contribuinte.podeParticiparDoNFG()) {
            throw new NFGException("Contribuinte não possui CNAE autorizado", redirectUrl);
        }

        Date dataEfetiva;
        try {
            dataEfetiva = format.parse(dataInicio);
        } catch (ParseException e) {
            throw new NFGException("Data de início vigência inválida", redirectUrl);
        }

        Boolean dataValida = validaData(dataEfetiva, contribuinte);
        if(!dataValida) {
            throw new NFGException("Data de início vigência inválida", redirectUrl);
        }

        Boolean isParticipante = cceContribuinteRepository.isParticipante(contribuinte.getNumeroInscricao());
        if(!isParticipante) {
            EmpresaParticipante empresaParticipante = new EmpresaParticipante();
            empresaParticipante.setTermoDeAcordo(termoDeAcordo.getBytes());
            empresaParticipante.setContribuinte(contribuinte);
            empresaParticipante.setDataCredenciamento(new Date());
            empresaParticipante.setDataEfetivaParticipacao(dataEfetiva);
            empresaParticipante.setIndiResponsavel(indiResponsavel);
            empresaParticipante.setIdPessoaAdesao(idPessoaAdesao);
            empresaParticipante.save();
        }
    }

    public Date getDataLimite(Date dataObrigatoriedadeCnae, Date dataCredenciamento) {
        Date dataLimite = getDataLimite(dataObrigatoriedadeCnae);
        Calendar cadastramentoMaisNoventa = Calendar.getInstance();
        cadastramentoMaisNoventa.setTime(dataCredenciamento);
        cadastramentoMaisNoventa.add(Calendar.DAY_OF_MONTH, 90);

        if (cadastramentoMaisNoventa.getTime().before(dataLimite)) {
            return cadastramentoMaisNoventa.getTime();
        } else {
            return dataLimite;
        }
    }

    public Date getDataLimite(Date dataObrigatoriedadeCnae) {
        Calendar hojeMaisNoventa = Calendar.getInstance();
        hojeMaisNoventa.set(Calendar.HOUR_OF_DAY, 23);
        hojeMaisNoventa.set(Calendar.MINUTE, 59);
        hojeMaisNoventa.add(Calendar.DAY_OF_MONTH, 90);

        Calendar dataObrigatoriedadeCalendar = Calendar.getInstance();
        dataObrigatoriedadeCalendar.setTime(dataObrigatoriedadeCnae);
        dataObrigatoriedadeCalendar.set(Calendar.HOUR_OF_DAY, 23);
        dataObrigatoriedadeCalendar.set(Calendar.MINUTE, 59);

        if (dataObrigatoriedadeCalendar.before(hojeMaisNoventa)) {
            return dataObrigatoriedadeCalendar.getTime();
        } else {
            return hojeMaisNoventa.getTime();
        }
    }

    public Boolean validaData(Date dataEfetiva, CCEContribuinte contribuinte) {
        CnaeAutorizado cnaeAutorizado = cnaeAutorizadoRepository.findForContribuinte(contribuinte);
        Date dataLimite = getDataLimite(cnaeAutorizado.getDataObrigatoriedade());
        return dataEfetiva.before(dataLimite);
    }

    public Boolean validaData(Date dataEfetiva, CCEContribuinte contribuinte, EmpresaParticipante empresaParticipante) {
        CnaeAutorizado cnaeAutorizado = cnaeAutorizadoRepository.findForContribuinte(contribuinte);
        Date dataLimite = getDataLimite(cnaeAutorizado.getDataObrigatoriedade(), empresaParticipante.getDataCredenciamento());
        return dataEfetiva.before(dataLimite);
    }

    public Boolean obrigadoEnviarMFD(CCEContribuinte contribuinte) {
        return false;
    }

    public Boolean podeAlterarData(Date dataLimite, Date dataEfetivaParticipacao) {
        if (dataLimite == null) {
            return false;
        } else if (dataEfetivaParticipacao == null) {
            return new Date().before(dataLimite);
        } else {
            return new Date().before(dataLimite) && new Date().before(dataEfetivaParticipacao);
        }
    }

    public Boolean cadastroCompulsorio(Date dataObrigatoriedadeCnae) {
        Calendar dataObrigatoriedadeCalendar = Calendar.getInstance();
        dataObrigatoriedadeCalendar.setTime(dataObrigatoriedadeCnae);
        dataObrigatoriedadeCalendar.set(Calendar.HOUR_OF_DAY, 23);
        dataObrigatoriedadeCalendar.set(Calendar.MINUTE, 59);

        return dataObrigatoriedadeCalendar.getTime().before(new Date());
    }

    public void cadastrarPelaEmpresa(GENEmpresa empresaLogada, Integer inscricao, String dataInicio, String termoDeAcordo, String redirectUrl) throws NFGException {
        CCEContribuinte contribuinte = cceContribuinteRepository.get(inscricao);

        if (!((GENPessoaJuridica) contribuinte.getPessoa()).getEmpresa().getNumeroCnpjBase().equals(empresaLogada.getNumeroCnpjBase())){
            throw new NFGException("Contribuinte não autorizado a realizar esse cadastro", redirectUrl);
        }

        cadastrarContribuinteAoNFG(contribuinte, dataInicio, EmpresaParticipante.INDI_EMPRESA, contribuinte.getPessoa().getIdPessoa(), termoDeAcordo, redirectUrl);
        cceContribuinteRepository.clearCacheContribuinte(empresaLogada.getNumeroCnpjBase());
    }

    public void alterarPelaEmpresa(GENEmpresa empresaLogada, Integer inscricao, String dataInicio, String redirectUrl) throws NFGException {
        CCEContribuinte contribuinte = cceContribuinteRepository.get(inscricao);

        if (!((GENPessoaJuridica) contribuinte.getPessoa()).getEmpresa().getNumeroCnpjBase().equals(empresaLogada.getNumeroCnpjBase())){
            throw new NFGException("Contribuinte não autorizado a realizar esse cadastro", redirectUrl);
        }

        alterarContribuinteNFG(contribuinte, dataInicio, redirectUrl);
        cceContribuinteRepository.clearCacheContribuinte(empresaLogada.getNumeroCnpjBase());

    }

    public void alterarPeloContador(GENPessoaFisica contadorLogado, Integer inscricao, String dataInicio, String redirectUrl) throws NFGException {
        CCEContribuinte contribuinte = cceContribuinteRepository.get(inscricao);

        if (!cceContribuinteRepository.contribuinteAssociadoAoContador(inscricao, contadorLogado)) {
            throw new NFGException("Contribuinte não associado ao contador", redirectUrl);
        }

        alterarContribuinteNFG(contribuinte, dataInicio, redirectUrl);
        cceContribuinteRepository.clearCacheContador(contadorLogado);
    }

    private void alterarContribuinteNFG(CCEContribuinte contribuinte, String dataInicio, String onException) throws NFGException {
        if(!contribuinte.podeParticiparDoNFG()) {
            throw new NFGException("Contribuinte não possui CNAE autorizado", onException);
        }

        Date dataEfetiva;
        try {
            dataEfetiva = format.parse(dataInicio);
        } catch (ParseException e) {
            throw new NFGException("Data de início vigência inválida", onException);
        }

        EmpresaParticipante empresaParticipante = empresaParticipanteRepository.findEmpresaComParticipacaoAtivaPorInscricao(contribuinte.getNumeroInscricao());
        Boolean dataValida = validaData(dataEfetiva, contribuinte, empresaParticipante);
        if(dataValida) {
            empresaParticipante.setDataEfetivaParticipacao(dataEfetiva);
            empresaParticipante.save();
        } else {
            throw new NFGException("Data de início vigência inválida", onException);
        }
    }

    public void cadastrarPeloContador(GENPessoaFisica contadorLogado, Integer inscricao, String dataInicio, String termoDeAcordo, String redirectUrl) throws NFGException {
        if (!cceContribuinteRepository.contribuinteAssociadoAoContador(inscricao, contadorLogado)) {
            throw new NFGException("Contribuinte não associado ao contador", redirectUrl);
        }

        CCEContribuinte contribuinte = cceContribuinteRepository.get(inscricao);
        cadastrarContribuinteAoNFG(contribuinte, dataInicio, EmpresaParticipante.INDI_CONTADOR, contadorLogado.getIdPessoa(), termoDeAcordo, redirectUrl);
        cceContribuinteRepository.clearCacheContador(contadorLogado);
    }

    public void descadastrarContribuinteDoNFG(CCEContribuinte contribuinte, String justificativa) {
        EmpresaParticipante empresaParticipante = this.empresaParticipanteRepository.findEmpresaComParticipacaoAtivaPorInscricao(contribuinte.getNumeroInscricao());
        empresaParticipante.setDataDescredenciamento(new Date());
        empresaParticipante.setMotivoDescredenciamento(justificativa);
        empresaParticipante.update();
    }
}