package gov.goias.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author henrique-rh
 * @since 28/05/15.
 */
@Controller
@RequestMapping("/portal/bilhete")
public class BilhetesController {

    @RequestMapping("/importarTxtResultado")
    public ModelAndView importarTxtResultado() {
        ModelAndView modelAndView = new ModelAndView("bilhete/importarTxt");
        return modelAndView;
    }

    @RequestMapping("/index")
    public ModelAndView formBilhetes() {
        ModelAndView modelAndView = new ModelAndView("bilhete/index");
        return modelAndView;
    }


    @RequestMapping(value = "/confirmarImportacao/{idSorteio}", method = RequestMethod.POST )
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody Map confirmarImportacao(@RequestParam("file") MultipartFile file,@PathVariable(value = "idSorteio") Integer idSorteio) {

        return new HashMap();
    }

    @RequestMapping(value = "/upload/{idSorteio}", method = RequestMethod.POST)
    public @ResponseBody Map uploadTxtResultado(@RequestParam("file") MultipartFile file,@PathVariable(value = "idSorteio") Integer idSorteio) {
        return new HashMap();
    }

    @RequestMapping("/novoPremio")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody Map getInfoBilhete(Long numeroBilhete, Integer idRegra, Integer idPremio, Long numeroPremio) {
    	return new HashMap();
    }

    @RequestMapping("/lista/{regra}")
    public @ResponseBody List getListaBilhetes (@PathVariable(value = "regra") Integer idRegra) {
    	return new ArrayList<>();
    }

    @RequestMapping("/remove/{idBilhete}")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public @ResponseBody String removeBilhete (@PathVariable(value = "idBilhete") Integer idBilhete) {
        return "Bilhete removido com sucesso";
    }
}
