package gov.goias.controllers;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/regras") 
public class RegrasPontuacaoESorteioController extends BaseController {

	private Logger logger = Logger.getLogger(RegrasPontuacaoESorteioController.class);

	@RequestMapping(value = "/pontuacao/bonus/put")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public @ResponseBody String putRegraPontuacaoBonus(@RequestBody Map<String, String> regraPontuacaoExtraMap) {

		return "RegrasPontuacaoESorteioController 24";
	}

	@RequestMapping(value = "/pontuacao/notas/put")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public @ResponseBody String putRegraPontuacaoNotas(@RequestBody Map<String, String> regraPontuacaoNotasMap) {

		return "RegrasPontuacaoESorteioController 31";
	}

	@RequestMapping(value = "/sorteio/put")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public @ResponseBody String putRegraSorteio(@RequestBody Map<String, String> regraSorteioMap) {
		return "RegrasPontuacaoESorteioController 37";
	}

}
