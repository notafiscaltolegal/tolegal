//package gov.goias.controllers
//
//import gov.sefaz.controle.gerenciador.Portal
//import groovy.sql.Sql
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.stereotype.Controller
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RequestMethod
//import org.springframework.web.bind.annotation.ResponseBody
//import org.springframework.web.servlet.ModelAndView
//
//import javax.servlet.http.HttpServletRequest
//import javax.servlet.http.HttpServletResponse
//
///**
// * @author henrique-rh
// * @since 26/11/2014.
// */
//@Controller
//@RequestMapping("/portal/admin")
//class AdminController {
//
//    @Autowired
//    private Sql sql
//
//    @Autowired
//    private HttpServletRequest request
//
//    private Boolean isAdmin() {
//        Portal portal = (Portal) request.getSession().getAttribute("portal");
//        return ['8622914', '6918751', '6905307', '10684050'].contains(portal.usuario)
//    }
//
//    @RequestMapping(value = "/consulta", method = RequestMethod.GET)
//    ModelAndView viewConsulta(HttpServletResponse response) {
//        if (!isAdmin()) {
//            response.sendError(403, "Usuário não possui privilégio para acessar esta página")
//            return null
//        }
//
//        def suggestions = request.session.getAttribute('suggestions')
//        if (!suggestions) {
//            suggestions = []
//            sql.eachRow("""
//            SELECT TABLE_NAME FROM ALL_TABLES WHERE
//              TABLE_NAME LIKE 'NFG%' OR
//              TABLE_NAME LIKE 'GEN%' OR
//              TABLE_NAME LIKE 'CCE%' OR
//              TABLE_NAME LIKE 'RFE%' OR
//              TABLE_NAME LIKE 'MFD%' OR
//              TABLE_NAME LIKE 'EFD%'""") { row ->
//                suggestions += [name: row.table_name, value: row.table_name, score: 2, meta: 'TABLE', syllables: '1']
//            }
//
//            sql.eachRow("""
//            SELECT VIEW_NAME FROM ALL_VIEWS WHERE
//              VIEW_NAME LIKE 'NFG%' OR
//              VIEW_NAME LIKE 'GEN%' OR
//              VIEW_NAME LIKE 'CCE%' OR
//              VIEW_NAME LIKE 'RFE%' OR
//              VIEW_NAME LIKE 'MFD%' OR
//              VIEW_NAME LIKE 'EFD%'
//              """) { row ->
//                suggestions += [name: row.view_name, value: row.view_name, score: 2, meta: 'VIEW', syllables: '1']
//            }
//
//            sql.eachRow("""
//            SELECT DISTINCT COLUMN_NAME FROM ALL_TAB_COLUMNS WHERE
//              TABLE_NAME LIKE 'NFG%' OR
//              TABLE_NAME LIKE 'GEN%' OR
//              TABLE_NAME LIKE 'CCE%' OR
//              TABLE_NAME LIKE 'RFE%' OR
//              TABLE_NAME LIKE 'MFD%' OR
//              TABLE_NAME LIKE 'EFD%'""") { row ->
//                suggestions += [name: row.column_name, value: row.column_name, score: 1, meta: 'COLUMN', syllables: '1']
//            }
//            suggestions += [name: 'rownum', value: 'rownum', score: 1, meta: 'keyword', syllables: '1']
//            request.session.setAttribute("suggestions", suggestions)
//        }
//        ModelAndView mav = new ModelAndView('admin/consulta')
//        mav.addObject("suggestions", suggestions)
//        return mav
//    }
//
//
//    @RequestMapping(value = "/executar", method = RequestMethod.POST)
//    @ResponseBody def consulta(HttpServletResponse response, HttpServletRequest request) {
//        Portal portal = (Portal) request.getSession().getAttribute("portal");
//
//        if (!isAdmin()) {
//            response.sendError(403, "Usuário não possui privilégio para acessar esta página")
//            return null
//        }
//
//        String table
//        try {
//            sql.call("call GEN.PKGGEN_INICIALIZA_USER.SPGEN_INICIALIZA_USER(?)", [portal.usuario])
//            def params = request.getParameterMap()
//            if (params.q[0].toString().toLowerCase().trim().startsWith("select")
//                    && !params.q[0].toString().contains("delete")
//                    && !params.q[0].toString().contains("insert")
//                    && !params.q[0].toString().contains("update")
//            ) {
//                def result = sql.rows(params.q[0])
//                def columns = ''
//                def rows = ''
//                if (!result.isEmpty()) {
//                    result?.first()?.keySet()?.each { it ->
//                        columns += "<th>$it</th>"
//                    }
//                    result.each { it ->
//                        rows += "<tr>"
//                        it.values().each{ cel ->
//                            rows += "<td>$cel</td>"
//                        }
//                        rows += "</tr>"
//                    }
//                } else {
//                    columns = '<th>Nenhum registro encontrado</th>'
//                }
//
//                table = """
//                <table border='1' class='table table-striped table-hover table-condensed'>
//                    <thead>
//                        <tr>$columns</tr>
//                    </thead>
//                    <tbody>
//                        $rows
//                    </tbody>
//                    <tfoot>
//                        <tr>
//                            <td colspan='${result.isEmpty() ? '1' : result?.first()?.keySet()?.size()}'>Total: <strong>${result.isEmpty() ? '0' : result?.size()}</strong></td>
//                        </tr>
//                    </tfoot>
//                </table>
//                """
//            } else {
//                if (!request.getRequestURL().contains("nfgoiana")) {
//                    sql.execute(params.q[0])
//                    table = "Executado: ${params.q[0]}"
//                }
//            }
//        } catch (Exception e){
//            table = e.getMessage()
//        }
//        response.getWriter().write(table)
//        response.setCharacterEncoding("utf-8")
//        return null
//    }
//}
