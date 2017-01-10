//package gov.goias.controllers
//
//import groovy.json.JsonOutput
//import groovy.json.JsonSlurper
//import groovy.json.StringEscapeUtils
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.stereotype.Controller
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RequestMethod
//import org.springframework.web.servlet.ModelAndView
//import org.springframework.web.servlet.view.RedirectView
//
//import javax.servlet.http.HttpServletRequest
//import javax.servlet.http.HttpServletResponse
//import java.text.SimpleDateFormat
//
///**
// * Created by henrique-rh on 30/01/15.
// */
////@RequestMapping("/precidadao")
//@Controller
//class PreCidadaoController {
//
//    public volatile static List cidadaos = [];
//    public static String diretorio = "/mnt/nfg"
//
//    public PreCidadaoController() {
////        fixCadastro()
////        File arquivo = new File("$diretorio/cidadao_teste.json")
////        if (!arquivo.exists()) {
////            arquivo.createNewFile()
////        } else {
////            arquivo.eachLine { line ->
////                try {
////                    def cidadao = new JsonSlurper().parseText(line)
////                    if(!jaCadastrado(cidadao)){
////                        cidadaos += cidadao
////                    }
////                } catch (Exception e) {
////                    println "ERRO AO CARREGAR $line -- ${e.getMessage()}"
////                }
////            }
////        }
//    }
//
//    @Autowired
//    private HttpServletRequest request
//
////    @RequestMapping(value = "/cadastro", method = RequestMethod.GET)
//    ModelAndView redirectToIndex() {
//        return new ModelAndView(new RedirectView("/", true))
//    }
//
//
//    static void fixCadastro() {
//        File arquivo = new File("$diretorio/cidadao_teste.json")
//        File newArquivo = new File("$diretorio/newcidadao_teste.json")
//        newArquivo.createNewFile()
//        arquivo.eachLine { line ->
//            try {
//                println(line)
//                def cidadao = new JsonSlurper().parseText(line)
//                cidadao = cidadaoFormatado(cidadao)
//                cidadao.dataCadastro = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date())
//                def json = new JsonOutput().toJson(cidadao);
//                newArquivo << "${StringEscapeUtils.unescapeJavaScript(json)}\n"
//            } catch (Exception e) {
//                println "ERRO AO CARREGAR $line -- ${e.getMessage()}"
//            }
//        }
//        new File("$diretorio/cidadao_teste.json").renameTo("$diretorio/cidadao_teste.json.back")
//        new File("$diretorio/newcidadao_teste.json").renameTo("$diretorio/cidadao_teste.json")
//
//    }
//
////    @RequestMapping(value = "/cadastro", method = RequestMethod.POST)
//    ModelAndView cadastrarCidadao(HttpServletResponse response) {
//        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy")
//        def cidadao = request.getParameterMap()
//        cidadao.entrySet().each {field ->
//            field.value = field.value[0]
//        }
//        if (!jaCadastrado(cidadao)) {
//            if (format.parse(cidadao.dataNascimento) > new Date() - 1) {
//                ModelAndView mav = new ModelAndView("index")
//                mav.addObject("errorMessage", "Data de nascimento inválida")
//                return mav
//            } else {
//                appendToFile(cidadao);
//            }
//        } else {
//            ModelAndView mav = new ModelAndView("index")
//            mav.addObject("errorMessage", "Esse cpf já foi cadastrado em nossa base.")
//            return mav
//        }
//        return new ModelAndView("cidadao/aguarde")
//    }
//
//    static synchronized Boolean jaCadastrado(cidadao) {
//        for (def c : cidadaos) {
//            if (c?.cpf?.trim()?.equals(cidadao?.cpf?.replaceAll("\\D+","")?.trim())) {
//                return true
//            }
//        }
//        return false
//    }
//
//    static Map cidadaoFormatado(Map cidadao) {
//        def newCidadao = new HashMap<>(cidadao)
//        newCidadao.dataCadastro = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date())
//        newCidadao.nome = newCidadao?.nome?.toUpperCase()?.trim()
//        newCidadao.nomeMae = newCidadao?.nomeMae?.toUpperCase()?.trim()
//        newCidadao.cpf = newCidadao.cpf.replaceAll("\\D+","")
//        return newCidadao
//    }
//    static synchronized appendToFile(Map cidadao) {
//        def newCidadao = cidadaoFormatado(cidadao)
//
//        File arquivo = new File("$diretorio/cidadao_teste.json")
//        def json = new JsonOutput().toJson(newCidadao);
//        if (!arquivo.exists()) {
//            arquivo.createNewFile()
//        }
//
//        arquivo << "${StringEscapeUtils.unescapeJavaScript(json)}\n"
//        cidadaos += new JsonSlurper().parseText(json)
//    }
//}
