package gov.goias.controllers;

import gov.goias.dtos.DTOTotaisCadastrados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @since 25/06/15.
 */
@Controller
public class TotaisCadastradosController {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;

    @RequestMapping("/portal/totaisCadastrados")
    public ModelAndView relatorioTotaisCadastrados() throws ParseException {
        ModelAndView modelAndView = new ModelAndView("totais-cadastrados/index");
        DTOTotaisCadastrados dtoTotaisCadastrados = consultarDadosTotaisCadastrados();
        modelAndView.addObject("dadosTotaisCadastrados", dtoTotaisCadastrados);
        return modelAndView;
    }

    @RequestMapping("/portal/totaisCadastrados/totaisCadastradosPorDia")
    public @ResponseBody List consultarTotaisCadastradosPorDia(
            @RequestParam(required = false) String dataInicio,
            @RequestParam(required = false) String dataFim) throws ParseException {
//        SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
//        String clausulaIntervaloData;
//        String queryTotaisPorDia =
//                "SELECT TO_CHAR(DATA_CADASTRO, 'DD/MM/YYYY') AS DATA_CADASTRO, COUNT(1) AS CADASTRADOS " +
//                " FROM NFG_PESSOA_PARTICIPANTE NP " +
//                    "$clausulaIntervaloDatas" +
//                " GROUP BY TO_CHAR(DATA_CADASTRO, 'DD/MM/YYYY') " +
//                " ORDER BY TO_DATE(DATA_CADASTRO, 'DD/MM/YYYY') DESC";
//
//        List<Map<String, Object>> retorno = null;
//        Date dataInicioConvertida;
//        Date dataFimConvertida;
//        if (dataInicio != null && !dataInicio.isEmpty()) {
//            dataInicioConvertida = this.getPrimeiroMinutoData(formatoData.parse(dataInicio));
//            clausulaIntervaloData = " WHERE DATA_CADASTRO >= ? ";
//            if (dataFim != null && !dataFim.isEmpty()) {
//                dataFimConvertida = this.getUltimoMinutoData(formatoData.parse(dataFim));
//                clausulaIntervaloData += " AND DATA_CADASTRO <= ? ";
//                retorno = jdbcTemplate.queryForList(queryTotaisPorDia.replace("$clausulaIntervaloDatas", clausulaIntervaloData), dataInicioConvertida, dataFimConvertida);
//            } else {
//                retorno = jdbcTemplate.queryForList(queryTotaisPorDia.replace("$clausulaIntervaloDatas", clausulaIntervaloData), dataInicioConvertida);
//            }
//        } else if (dataFim != null && !dataFim.isEmpty()) {
//            dataFimConvertida = this.getUltimoMinutoData(formatoData.parse(dataFim));
//            clausulaIntervaloData = " WHERE DATA_CADASTRO <= ? ";
//            retorno = jdbcTemplate.queryForList(queryTotaisPorDia.replace("$clausulaIntervaloDatas", clausulaIntervaloData), dataFimConvertida);
//        }

        return new ArrayList<>();
    }

    private DTOTotaisCadastrados consultarDadosTotaisCadastrados() {

        return new DTOTotaisCadastrados();
    }

    /**
     * Calcula e retorna uma data correspondente à data informada, porém usando o último minuto da última hora.
     *
     * @param data  a data a ser processada
     * @return      uma data correspondente à informada, porém no último minuto da última hora
     */
    private Date getUltimoMinutoData(Date data) {
        Calendar tempCalendar = new GregorianCalendar();
        tempCalendar.setTime(data);
        tempCalendar.set(Calendar.HOUR_OF_DAY, 23);
        tempCalendar.set(Calendar.MINUTE, 59);
        tempCalendar.set(Calendar.SECOND, 59);
        return tempCalendar.getTime();
    }

    /**
     * Calcula e retorna uma data correspondente à data informada, porém usando o primeiro minuto da primeira hora.
     *
     * @param data  a data a ser processada
     * @return      uma data correspondente à informada, porém no último minuto da última hora
     */
    private Date getPrimeiroMinutoData(Date data) {
        Calendar tempCalendar = new GregorianCalendar();
        tempCalendar.setTime(data);
        tempCalendar.set(Calendar.HOUR_OF_DAY, 0);
        tempCalendar.set(Calendar.MINUTE, 0);
        tempCalendar.set(Calendar.SECOND, 0);
        return tempCalendar.getTime();
    }

}
