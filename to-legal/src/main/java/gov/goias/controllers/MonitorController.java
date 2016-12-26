package gov.goias.controllers;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by lucas-mp on 10/10/14.
 */
@Controller
public class MonitorController extends AdminBaseController {
    private static final Logger logger = Logger.getLogger(MonitorController.class);

    @RequestMapping(value = "/monitor", method = RequestMethod.GET)
    public @ResponseBody String monitorarNFG(HttpServletResponse response) {
        return "MonitorController 34";

    }

    @RequestMapping("/estatistica/jpa")
    public @ResponseBody String gerarEstatisticaJpa(HttpServletResponse response) {
        return "MonitorController 40";
    }
}