package gov.goias.controllers;

import gov.goias.entidades.BilhetePessoa;
import gov.goias.entidades.PremioBilhete;
import gov.goias.entidades.PremioSorteio;
import gov.goias.entidades.RegraSorteio;
import gov.goias.exceptions.NFGException;
import gov.goias.persistencia.historico.HistoricoNFG;
import gov.goias.util.Encrypter;
import org.hibernate.JDBCException;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.AutoPopulatingList;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

/**
 * @author henrique-rh
 * @since 28/05/15.
 */
@Controller
@RequestMapping("/portal/bilhete")
//@RequestMapping("/bilhete")
public class BilhetesController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RegraSorteio regraSorteioRepository;

    @Autowired
    private PremioSorteio premioSorteioRepository;

    @Autowired
    private BilhetePessoa bilhetePessoaRepository;

    @Autowired
    private PremioBilhete premioBilheteRepository;

    @RequestMapping("/importarTxtResultado")
    public ModelAndView importarTxtResultado() {
        ModelAndView modelAndView = new ModelAndView("bilhete/importarTxt");
        return modelAndView;
    }

    @RequestMapping("/index")
    public ModelAndView formBilhetes() {
        ModelAndView modelAndView = new ModelAndView("bilhete/index");
        List<RegraSorteio> listaSorteios = regraSorteioRepository.list();
        modelAndView.addObject("listaSorteios", listaSorteios);
        return modelAndView;
    }
    @RequestMapping("/premios/{idSorteio}")
    public @ResponseBody List<PremioSorteio> listPremios (@PathVariable(value = "idSorteio") Integer idSorteio) {
        HashMap<String, Object> dados = new HashMap<>();
        dados.put("regraSorteio", idSorteio);
        return premioSorteioRepository.list(dados, "valor");
    }


    @RequestMapping(value = "/confirmarImportacao/{idSorteio}", method = RequestMethod.POST )
    @HistoricoNFG
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody Map confirmarImportacao(@RequestParam("file") MultipartFile file,@PathVariable(value = "idSorteio") Integer idSorteio) {
        HashMap<String, Object> resposta = new HashMap<>();

        if (file.isEmpty()){
            resposta.put("error","Não foi possível localizar o arquivo.");
            return resposta;
        }

        List<Map> bilhetesPremiados =  premioBilheteRepository.processaArquivoTxtBilhetesPremiados(file,idSorteio);

        if (bilhetesPremiados!=null && bilhetesPremiados.size()>0){
            if (premioBilheteRepository.incluiRegistrosDoTxtNaBase(idSorteio,bilhetesPremiados)){
                resposta.put("success","Arquivo devidamente importado!");
            }else {
                resposta.put("error","Arquivo não pode ser importado por divergências com o banco de dados. Verifique os logs da aplicação para mais detalhes!");
            }
        }else{
            resposta.put("error", "Não foi possível encontrar registros para esta importação. Verifique e tente novamente.");
        }

        return resposta;
    }

    @RequestMapping(value = "/upload/{idSorteio}", method = RequestMethod.POST)
    public @ResponseBody Map uploadTxtResultado(@RequestParam("file") MultipartFile file,@PathVariable(value = "idSorteio") Integer idSorteio) {
        HashMap<String, Object> resposta = new HashMap<>();

        if (!file.isEmpty()){
            try {
                List<Map> bilhetesPremiados =  premioBilheteRepository.processaArquivoTxtBilhetesPremiados(file,idSorteio);

                String hashMD5Arquivo  = Encrypter.encryptMD5(file.getInputStream());

                if (bilhetesPremiados!=null && bilhetesPremiados.size()>0){
                    resposta.put("bilhetesPremiados",bilhetesPremiados);
                    resposta.put("success", "Upload de arquivo realizado. Por favor confirme os dados:");
                    resposta.put("hashMD5Arquivo",hashMD5Arquivo);

                }else{
                    resposta.put("error", "Não foi possível encontrar registros para esta importação. O arquivo é este? Hash do arquivo:"+hashMD5Arquivo+" .Verifique e tente novamente.");
                }

            } catch (IOException e) {
                resposta.put("error","Arquivo não pôde ser importado. Tente novamente.");
            }
        }else{
            resposta.put("error","Não foi possível localizar o arquivo.");
        }
        return resposta;
    }

    @RequestMapping("/novoPremio")
    @HistoricoNFG
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody Map getInfoBilhete(Long numeroBilhete, Integer idRegra, Integer idPremio, Long numeroPremio) {
        try {
            Map resposta = bilhetePessoaRepository.findBilhete(numeroBilhete, idRegra);
            BigDecimal idBilhete = (BigDecimal) resposta.get("ID_BILHETE");
            resposta.put("ID_PREMIO_BILHETE", bilhetePessoaRepository.novoBilhete(idBilhete.intValue(), idPremio, numeroPremio));
            return bilhetePessoaRepository.getBilhete(idRegra, numeroBilhete);
        } catch (EmptyResultDataAccessException e) {
            throw new NFGException("Nenhum bilhete encontrado");
        } catch (JDBCException e) {
            throw new NFGException(e.getMessage());
        } catch (PersistenceException e) {
            throw new NFGException("Bilhete já inserido");
        }
    }

    @RequestMapping("/lista/{regra}")
    public @ResponseBody List getListaBilhetes (@PathVariable(value = "regra") Integer idRegra) {
        try {
            return bilhetePessoaRepository.listBilhetes(idRegra);
        } catch (EmptyResultDataAccessException e) {
            throw new NFGException("Nenhum bilhete encontrado");
        }
    }

    @RequestMapping("/remove/{idBilhete}")
    @HistoricoNFG
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody String removeBilhete (@PathVariable(value = "idBilhete") Integer idBilhete) {
        PremioBilhete premioBilhete = premioBilheteRepository.get(idBilhete);
        premioBilhete.delete();
        return "Bilhete removido com sucesso";
    }
}
