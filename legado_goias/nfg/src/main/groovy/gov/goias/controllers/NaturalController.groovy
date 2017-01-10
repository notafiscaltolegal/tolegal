//package gov.goias.controllers
//
//import com.softwareag.entirex.aci.Broker
//import gov.sefaz.io.RPCNatural
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.http.MediaType
//import org.springframework.web.bind.annotation.PathVariable
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.ResponseBody
//import org.springframework.web.bind.annotation.RestController
//
//import javax.servlet.http.HttpServletRequest
//import javax.servlet.http.HttpServletResponse
//
///**
// * Created by henrique-rh on 03/12/14.
// */
//@RestController
//@RequestMapping("/mf")
//class NaturalController {
//
//    @Autowired
//    HttpServletRequest request
//
//    @RequestMapping("/{operacao}")
//    public @ResponseBody Map listWithoutParam(@PathVariable(value="operacao") String operacao, HttpServletResponse response) {
//        return list(operacao, "", response)
//    }
//
//    /*
//        #Desenvolvimento
//        SEFAZ_BROKERID=pfaz.intra.goias.gov.br:18026
//        SEFAZ_SERVER=RPC/SRVDESV/CALLNAT
//        SEFAZ_LIB=FAZDFGI
//
//        #Homologação
//        SEFAZ_BROKERID=pfaz.intra.goias.gov.br:18026
//        SEFAZ_SERVER=RPC/SRVHMP/CALLNAT
//        SEFAZ_LIB=FAZPFGI
//
//        #Produção
//        SEFAZ_BROKERID=pfaz.intra.goias.gov.br:18024
//        SEFAZ_SERVER=RPC/JAVPROD/CALLNAT
//        SEFAZ_LIB=FAZPFGI
//    */
//    @RequestMapping("/{operacao}/{parametros}")
//    public @ResponseBody Map list(@PathVariable(value="operacao") String operacao, @PathVariable(value="parametros") String parametros, HttpServletResponse response) {
//
//        String brokerid = "pfaz.intra.goias.gov.br:18026?poolsize=640&pooltimeout=60";
//        Broker broker = new Broker(brokerid, "RPC/SRVHMP/CALLNAT")
//        RPCNatural rpc = new RPCNatural(broker)
//        rpc.setNomePrograma("NWEB0000")
//
//        try {
//            rpc.addParametro(RPCNatural.IN_OUT, RPCNatural.TIPO_AV, "AV", getXMLRequestHeader(operacao, parametros))
//            rpc.executar()
//            String xmlRetorno = rpc.getAlfaVariavel()
//            response.getWriter().write(xmlRetorno)
//        } catch (Exception ex) {
//            response.getWriter().write("Erro de comunicação com o broker" + ex.getMessage())
//        }
//        response.setCharacterEncoding("utf-8")
//        response.setHeader("Content-Type", MediaType.APPLICATION_XML_VALUE)
//        return null
//    }
//
//    private static String getXMLRequestHeader(servico, String parametros) {
//        return """
//        <?xml version="1.0" encoding="ISO-8859-1" standalone="no"?>
//        <NWS>
//            <SERVICO>$servico</SERVICO>
//            <PARAMETRO>$parametros</PARAMETRO>
//        </NWS>
//        """.replace(" ", "")
//    }
//
//}
