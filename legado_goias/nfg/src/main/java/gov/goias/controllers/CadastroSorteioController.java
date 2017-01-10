package gov.goias.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import gov.goias.entidades.RegraSorteio;
import gov.goias.entidades.enums.StatusProcessamentoSorteio;
import gov.goias.exceptions.NFGException;
import gov.goias.persistencia.historico.HistoricoAdmin;

/**
 * @author henrique-rh
 * @since 17/07/15.
 */    
@Controller
public class CadastroSorteioController extends BaseController {

    @Autowired
    private RegraSorteio regraSorteioRepository;

    @RequestMapping(value = "/admin/cadastro/sorteio", method = RequestMethod.GET)
    public ModelAndView viewCadastroSorteio() {
        ModelAndView mav = new ModelAndView("admin/cadastroSorteio");
        mav.addObject("sorteios", regraSorteioRepository.listSorteios());
        return mav;
    }

    @Autowired
    private SimpleDateFormat simpleDateFormat;

    @Autowired
    private HttpServletRequest request;

    @Transactional
    @HistoricoAdmin
    @RequestMapping(value = "/admin/cadastro/sorteio", method = RequestMethod.POST)
    public @ResponseBody String cadastrarSorteio(String password) throws ParseException {
        if (!password.toUpperCase().equals(adminLogado.getRawPassword())) {
            throw new NFGException("Senha não confere");
        }
        Integer idSorteio = Integer.parseInt(request.getParameter("idSorteio"));

        RegraSorteio regraSorteio;
        if (idSorteio > 0) {
            regraSorteio = regraSorteioRepository.buscaSorteioPorId(Integer.parseInt(request.getParameter("idSorteio")));
            if (!regraSorteio.getStatus().equals(StatusProcessamentoSorteio.AGUARDANDO_GERACAO_BILHETES.getValor())) {
                throw new NFGException("Esse sorteio não pode ser alterado. Apenas sorteio com status: " + StatusProcessamentoSorteio.AGUARDANDO_GERACAO_BILHETES.getDescricao() + " pode ser alterado.");
            }
        } else {
            regraSorteio = new RegraSorteio();
            regraSorteio.setDataCadastro(new Date());
        }

        regraSorteio.setNumeroConversao(Double.valueOf(request.getParameter("numeroConversao")));
        regraSorteio.setDataExtracaoLoteria(simpleDateFormat.parse(request.getParameter("dataExtracaoLoteria")));
        regraSorteio.setDataRealizacao(simpleDateFormat.parse(request.getParameter("dataRealizacao")));
        regraSorteio.setDataLimiteCadastroPessoa(simpleDateFormat.parse(request.getParameter("dataLimiteCadastroPessoa")));
        regraSorteio.setInformacao(request.getParameter("informacao"));
        regraSorteio.setNumeroMaxDocFisc(Integer.parseInt(request.getParameter("numeroMaxDocFisc")));
        regraSorteio.setNumeroLoteria(Integer.parseInt(request.getParameter("numeroLoteria")));
        regraSorteio.setStatus(StatusProcessamentoSorteio.AGUARDANDO_GERACAO_BILHETES.getValor());
        regraSorteio.setTipo(1);
        regraSorteio.setDivulgaSorteio("S");
        regraSorteio.setRealizado('N');
        regraSorteio.save();
        return "Sorteio " + regraSorteio.getInformacao() + " salvo com sucesso!";
    }

    @RequestMapping(value = "/admin/sorteio/{idSorteio}", method = RequestMethod.GET)
    public @ResponseBody RegraSorteio carregaSorteio(@PathVariable(value = "idSorteio") String idSorteio) {
        return regraSorteioRepository.buscaSorteioPorId(Integer.parseInt(idSorteio));
    }
}
