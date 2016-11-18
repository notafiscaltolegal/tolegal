package gov.goias.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import gov.goias.util.FileManagement;
import gov.goias.util.TextUtils;

/**
 */
@Controller
@RequestMapping(value = {"/contribuinte/ecf"})
//todo retirar mock
//@RequestMapping("/nota")
public class EcfController extends BaseController
{

    private static final Logger logger = Logger.getLogger(EcfController.class);

    @RequestMapping("/importar/{inscricao}")
    public ModelAndView cadastro(@PathVariable(value="inscricao")Integer inscricao)
    {

        //todo retirar mock
//        inscricao = 100038280;

        ModelAndView modelAndView = new ModelAndView("/ecf/importar");

        return modelAndView;
    }

    @RequestMapping(value = "/upload/{inscricao}", method = RequestMethod.POST)
    public @ResponseBody Map uploadTxtResultado(@RequestParam("file") MultipartFile file,@PathVariable(value = "inscricao") Integer inscricao) {
        HashMap<String, Object> resposta = new HashMap<>();
        return resposta;
    }

    @RequestMapping(value = "/finalizaUpload/{inscricao}", method = RequestMethod.POST)
    public @ResponseBody Map finalizaUpload(@RequestParam("file") MultipartFile file,@PathVariable(value = "inscricao") Integer inscricao) {
        HashMap<String, Object> resposta = new HashMap<>();
        return resposta;
    }
}