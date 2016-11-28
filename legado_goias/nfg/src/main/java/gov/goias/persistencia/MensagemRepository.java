package gov.goias.persistencia;

import gov.goias.dtos.DTOContribuinte;
import gov.goias.entidades.*;
import gov.goias.entidades.enums.TipoPerfilCadastroReclamacao;
import gov.goias.exceptions.NFGException;
import gov.goias.interceptors.CertificateInterceptor;
import gov.goias.services.ContadorLoginService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.sql.*;
import java.util.*;


public class MensagemRepository extends GenericRepository<Integer, Mensagem> {
    private static final Logger logger = Logger.getLogger(MensagemRepository.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    DocumentoFiscalReclamado documentoFiscalReclamadoRepository;

    public List findMensagensCaixaDeEntradaEmpresas(Integer max, Integer page, Integer idPessoa) {
        String sql = "SELECT * FROM ( " +
                "    select * from ( " +
                "        select DESC_MENSAGEM, DESC_TITULO_MENSAGEM, DATA_MENSAGEM, INDI_MENSAGEM_PUBLICA, TIPO_DEST_MENSAGEM_NFG,rownum as rn from ( " +
                "              SELECT DESC_MENSAGEM, DESC_TITULO_MENSAGEM, DATA_MENSAGEM, INDI_MENSAGEM_PUBLICA, TIPO_DEST_MENSAGEM_NFG  FROM NFG_MENSAGEM_NFG where TIPO_DEST_MENSAGEM_NFG = 'E' and INDI_MENSAGEM_PUBLICA ='S'  " +
                "              UNION ALL " +
                "              Select DESC_MENSAGEM, DESC_TITULO_MENSAGEM, DATA_MENSAGEM, INDI_MENSAGEM_PUBLICA, TIPO_DEST_MENSAGEM_NFG FROM NFG_MENSAGEM_NFG where TIPO_DEST_MENSAGEM_NFG = 'E' and INDI_MENSAGEM_PUBLICA ='N' " +
                "              AND ID_MENSAGEM_NFG in (SELECT ID_MENSAGEM_NFG FROM NFG_DESTINATARIO_MENSAGEM_NFG WHERE NFG_DESTINATARIO_MENSAGEM_NFG.ID_PESSOA_DEST_MENSAGEM=? )  " +
                "              ORDER BY DATA_MENSAGEM DESC " +
                "        ) " +
                "    ) WHERE rownum <=?  " +
                ") where rn >  ?";

        try{
            List<Mensagem> resultado = jdbcTemplate.query(
                    sql, new RowMapper<Mensagem>() {
                        @Override
                        public Mensagem mapRow(ResultSet rs, int i) throws SQLException {
                            Mensagem mensagem = new Mensagem();
                            mensagem.setTexto(rs.getString("DESC_MENSAGEM"));
                            mensagem.setTitulo(rs.getString("DESC_TITULO_MENSAGEM"));
                            mensagem.setData(rs.getTimestamp("DATA_MENSAGEM"));
                            mensagem.setMensagemPublica(rs.getString("INDI_MENSAGEM_PUBLICA").charAt(0));
                            mensagem.setTipoDestinatario(rs.getString("TIPO_DEST_MENSAGEM_NFG").charAt(0));
                            return mensagem;
                        }
                    },
                    idPessoa, max*(page+1),max*page);
            return resultado;
        }catch (EmptyResultDataAccessException e){
            return null;
        }

    }

    public Map findMensagensCaixaDeEntrada(Integer max, Integer page, Integer idCidadao) {
        String sql= " select DESC_MENSAGEM, DESC_TITULO_MENSAGEM, DATA_MENSAGEM, INDI_MENSAGEM_PUBLICA, TIPO_DEST_MENSAGEM_NFG from ( " +
                " SELECT DESC_MENSAGEM, DESC_TITULO_MENSAGEM, DATA_MENSAGEM, INDI_MENSAGEM_PUBLICA, TIPO_DEST_MENSAGEM_NFG  FROM NFG_MENSAGEM_NFG where TIPO_DEST_MENSAGEM_NFG = 'C' and INDI_MENSAGEM_PUBLICA ='S'  " +
                " UNION ALL " +
                " Select DESC_MENSAGEM, DESC_TITULO_MENSAGEM, DATA_MENSAGEM, INDI_MENSAGEM_PUBLICA, TIPO_DEST_MENSAGEM_NFG FROM NFG_MENSAGEM_NFG where TIPO_DEST_MENSAGEM_NFG = 'C' and INDI_MENSAGEM_PUBLICA ='N' " +
                " AND ID_MENSAGEM_NFG in (SELECT ID_MENSAGEM_NFG FROM NFG_DESTINATARIO_MENSAGEM_NFG WHERE NFG_DESTINATARIO_MENSAGEM_NFG.ID_PESSOA_DEST_MENSAGEM=? )  " +
                "  ) ";
        String ordenacao="DATA_MENSAGEM DESC";
        return paginateMappableClassObjects(max, page, sql,ordenacao, Mensagem.class, idCidadao);
    }

    public List findMensagensCadastradasPeloUsuario(Integer max, Integer page) {
        String sql = " SELECT * FROM ( " +
                "   SELECT ID_MENSAGEM_NFG,DESC_MENSAGEM,DESC_TITULO_MENSAGEM,DATA_MENSAGEM," +
                "     INDI_MENSAGEM_PUBLICA,TIPO_DEST_MENSAGEM_NFG,rownum as rn  FROM (" +
                "     SELECT * FROM NFG_MENSAGEM_NFG where CODG_TIPO_MENSAGEM = 1" +
                "     order by DATA_MENSAGEM desc " +
                "   ) " +
                "   WHERE rownum <= ?" +
                " ) " +
                " WHERE rn > ?";
        try{
            List<Mensagem> resultado = jdbcTemplate.query(
                    sql, new RowMapper<Mensagem>() {
                        @Override
                        public Mensagem mapRow(ResultSet rs, int i) throws SQLException {
                            Mensagem mensagem = new Mensagem();
                            mensagem.setId(rs.getInt("ID_MENSAGEM_NFG"));
                            mensagem.setTexto(rs.getString("DESC_MENSAGEM"));
                            mensagem.setTitulo(rs.getString("DESC_TITULO_MENSAGEM"));
                            mensagem.setData(rs.getTimestamp("DATA_MENSAGEM"));
                            mensagem.setMensagemPublica(rs.getString("INDI_MENSAGEM_PUBLICA").charAt(0));
                            mensagem.setTipoDestinatario(rs.getString("TIPO_DEST_MENSAGEM_NFG").charAt(0));
                            if (mensagem.getMensagemPublica()=='N'){
                                mensagem.setListaDestinatariosString(listaDestinatariosStringParaMensagem(mensagem.getId(),mensagem.getTipoDestinatario()));
                            }
                            return mensagem;
                        }
                    },
                    //parametros de paginacao:
                    max*(page+1),max*page);
            return resultado;
        }catch (EmptyResultDataAccessException e){
            return null;
        }

    }

    private String listaDestinatariosStringParaMensagem(Integer mensagemId, Character tipoDestinatario) {
        String sql;
        if (tipoDestinatario=='C'){
            sql = "select numr_cpf ||' - '|| nome_pessoa as destinatarios from gen_pessoa_fisica where id_pessoa in ( " +
                    " select id_pessoa_dest_mensagem from NFG_DESTINATARIO_MENSAGEM_NFG where ID_MENSAGEM_NFG=?)";
        }else {
            sql = "select numr_cnpj ||' - '|| nome_fantasia as destinatarios from gen_pessoa_juridica where id_pessoa in ( " +
                    "select id_pessoa_dest_mensagem from NFG_DESTINATARIO_MENSAGEM_NFG where ID_MENSAGEM_NFG=?)";
        }
        List<String> destinatarios = jdbcTemplate.queryForList(sql,String.class, mensagemId);
        String destinatariosStr="";
        for(String destinatario:destinatarios){
            destinatariosStr+=destinatario+" \n";
        }
        return destinatariosStr;
    }

    public Integer countMensagensCadastradasPeloUsuario() {
        String sql = "SELECT count(*) FROM NFG_MENSAGEM_NFG where CODG_TIPO_MENSAGEM = 1";
        try{
            return jdbcTemplate.queryForObject(sql,Integer.class);
        }catch (EmptyResultDataAccessException e){
            return 0;
        }
    }

    public Integer countMensagensEmpresas(Integer idPessoa) {
        String sql = "select count(*) from ( " +
                "SELECT DESC_MENSAGEM, DESC_TITULO_MENSAGEM, DATA_MENSAGEM, INDI_MENSAGEM_PUBLICA, TIPO_DEST_MENSAGEM_NFG, rownum as rn FROM NFG_MENSAGEM_NFG where TIPO_DEST_MENSAGEM_NFG = 'E' and INDI_MENSAGEM_PUBLICA ='S' " +
                "UNION ALL " +
                "Select DESC_MENSAGEM, DESC_TITULO_MENSAGEM, DATA_MENSAGEM, INDI_MENSAGEM_PUBLICA, TIPO_DEST_MENSAGEM_NFG, rownum as rn FROM NFG_MENSAGEM_NFG where TIPO_DEST_MENSAGEM_NFG = 'E' and INDI_MENSAGEM_PUBLICA ='N' " +
                "AND ID_MENSAGEM_NFG in (SELECT ID_MENSAGEM_NFG FROM NFG_DESTINATARIO_MENSAGEM_NFG WHERE NFG_DESTINATARIO_MENSAGEM_NFG.ID_PESSOA_DEST_MENSAGEM=?  ))";
        try{
            return jdbcTemplate.queryForObject(sql,Integer.class,idPessoa);
        }catch (EmptyResultDataAccessException e){
            return 0;
        }
    }

    public boolean cadastrarNovaMensagemUsuario(Character tipoDestinatario, Character mensagemPublica, String titulo, String texto, Map<String,LinkedHashMap> mapDestinatarios) {
        final String sqlInsert = "insert into NFG_MENSAGEM_NFG" +
                " (TIPO_DEST_MENSAGEM_NFG,INDI_MENSAGEM_PUBLICA," +
                " DESC_TITULO_MENSAGEM,DESC_MENSAGEM,DATA_MENSAGEM,CODG_TIPO_MENSAGEM)" +
                "     VALUES " +
                "       (?,?,?,?,sysdate,1)";

        final Character tipoDestinFinal = tipoDestinatario;
        final Character mensagemPublicaFinal = mensagemPublica;
        final String tituloFinal = titulo;
        final String textoFinal = texto;

        boolean retorno = true;
        Integer mensagemId=null;

        try {
            KeyHolder holder = new GeneratedKeyHolder();
            int result = jdbcTemplate.update(new PreparedStatementCreator() {

                @Override
                public PreparedStatement createPreparedStatement(Connection connection)
                        throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[]{"ID_MENSAGEM_NFG"});
                    ps.setString(1, tipoDestinFinal.toString());
                    ps.setString(2, mensagemPublicaFinal.toString());
                    ps.setString(3, tituloFinal);
                    ps.setString(4, textoFinal);
                    return ps;
                }
            }, holder);
            mensagemId = holder.getKey().intValue();
            retorno &= result>0;
        }catch (Exception e){
            throw new NFGException("Erro ao tentar adicionar nova mensagem de usuário: "+e.getMessage());
//            logger.error("Erro ao tentar adicionar nova mensagem de usuário: "+e.getMessage());
//            retorno = false;
        }

        if (mapDestinatarios!=null && mensagemId!=null){
            retorno &= cadastrarDestinatariosDaMensagemUsuario(mapDestinatarios,tipoDestinatario.toString(),mensagemId);
        }

        return retorno;
    }

    private boolean cadastrarDestinatariosDaMensagemUsuario(Map<String, LinkedHashMap> mapDestinatarios,String tipoDestinatario,Integer mensagemId) {
        boolean retorno = true;

        for (Map.Entry<String, LinkedHashMap> entry : mapDestinatarios.entrySet())
        {
            LinkedHashMap<String,String> mapaDestinatarios = ((LinkedHashMap<String,String>)entry.getValue());

            String id = mapaDestinatarios.get("id");

            String sqlInsert = "insert into NFG_DESTINATARIO_MENSAGEM_NFG" +
                    " (ID_PESSOA_DEST_MENSAGEM,ID_MENSAGEM_NFG) " +
                    "     VALUES " +
                    "       (?,?)";

            try {

                int result = jdbcTemplate.update(sqlInsert,id,mensagemId);
                retorno &= result>0;
            }catch (Exception e){
                throw new NFGException("Erro ao tentar adicionar novo destinatario de idpessoa "+id+" na mensagem de usuário: "+e.getMessage());
            }
        }
        return retorno;
    }

    public List findDestinatarioCpf(String cpfDestinatario, String nomeDestinatario) {
        if(cpfDestinatario==null)
            throw new NFGException("Escolha cpf para fazer o filtro!",true);

        List<Map> retorno = new ArrayList();

        String sql = "select NUMR_CPF,NOME_PESSOA,ID_PESSOA from GEN_PESSOA_FISICA " +
                "WHERE 1=1 " +
                "and NUMR_CPF like ? " +
                "and upper(NOME_PESSOA) like upper(?)"+
                " and rownum <= 5"+
                " order by NOME_PESSOA"; //limite de 5 registros

        try{
            retorno = jdbcTemplate.query(
                    sql, new RowMapper<Map>() {
                        @Override
                        public Map mapRow(ResultSet rs, int i) throws SQLException {
                            Map resultMap = new HashMap();
                            resultMap.put("cpf", rs.getString("NUMR_CPF"));
                            resultMap.put("nome", rs.getString("NOME_PESSOA"));
                            resultMap.put("idpessoa", rs.getString("ID_PESSOA"));
                            return resultMap;
                        }
                    }
                    //parametros:
                    ,"%"+cpfDestinatario+"%","%"+nomeDestinatario+"%"
            );
            return retorno;
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }


    public List findDestinatarioCnpj(String cnpjDestinatario, String nomeFantasiaDestinatario) {
        if(cnpjDestinatario==null)
            throw new NFGException("Escolha cnpj para fazer o filtro!",true);
        if(nomeFantasiaDestinatario==null)
            throw new NFGException("Erro: valor nao esperado!",true);

        List<Map> retorno = new ArrayList();

        String sql = "SELECT NUMR_CNPJ,NOME_FANTASIA,ID_PESSOA from  GEN_PESSOA_JURIDICA " +
                " WHERE 1=1 " +
                " AND NUMR_CNPJ like ? " +
                " and upper(NOME_FANTASIA) like upper(?) " +
                " and rownum <=5 " +
                " order by NOME_FANTASIA"; //limite de 5 registros

        try{
            retorno = jdbcTemplate.query(
                    sql, new RowMapper<Map>() {
                        @Override
                        public Map mapRow(ResultSet rs, int i) throws SQLException {
                            Map resultMap = new HashMap();
                            resultMap.put("cnpj", rs.getString("NUMR_CNPJ"));
                            resultMap.put("fantasia", rs.getString("NOME_FANTASIA"));
                            resultMap.put("idpessoa", rs.getString("ID_PESSOA"));
                            return resultMap;
                        }
                    }
                    //parametros:
                    , "%" + cnpjDestinatario + "%", "%" + nomeFantasiaDestinatario + "%"
            );
            return retorno;
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public boolean alterarMensagemUsuario(String titulo, String texto, Integer id) {
        String sqlInsert = "update NFG_MENSAGEM_NFG" +
                " set DESC_TITULO_MENSAGEM=? " +
                " , DESC_MENSAGEM=? " +
                " WHERE ID_MENSAGEM_NFG=?  ";

        try {
            int result = jdbcTemplate.update(sqlInsert,titulo,texto,id);
            return result>0;
        }catch (Exception e){
            throw new NFGException("Erro ao tentar atualizar mensagem de id "+id+": "+e.getMessage(),true);
        }

    }

    public boolean excluirMensagemUsuario(Integer id, Character publica) {
        boolean retorno = true;

        if (publica=='N'){
            retorno &= excluirDestinatariosMensagemUsuario(id);
        }

        String sqlInsert = "delete from NFG_MENSAGEM_NFG" +
                "  WHERE ID_MENSAGEM_NFG=?  ";

        try {
            int result = jdbcTemplate.update(sqlInsert,id);
            retorno &=  result>0;
        }catch (Exception e){
            throw new NFGException("Erro ao tentar excluir mensagem de id "+id+": "+e.getMessage());
        }

        return retorno;
    }

    private boolean excluirDestinatariosMensagemUsuario(Integer id) {
        String sqlInsert = "delete from NFG_DESTINATARIO_MENSAGEM_NFG " +
                "  WHERE ID_MENSAGEM_NFG=?  ";

        try {
            int result = jdbcTemplate.update(sqlInsert,id);
            return result>0;
        }catch (Exception e){
            throw new NFGException("Erro ao tentar excluir destinatarios da mensagem de id "+id+": "+e.getMessage());
        }
    }

    public List<DTOContribuinte> retornaQtdMensagensEmpresas(List<DTOContribuinte> contribuintes){
        List<DTOContribuinte> dtoContribuintes = new ArrayList<>();

        for(DTOContribuinte dtoContribuinte : contribuintes){
            if(dtoContribuinte.getIdPessoa() != null){
                Integer qtdMensagens = listarQtdMensagensNaoLidas(dtoContribuinte.getIdPessoa());
                Integer qtdReclamacoes = retornaNumeroDeReclamacoesNaoRespondidas(dtoContribuinte.getNumeroCnpj());
                dtoContribuinte.setQtdReclamacoes(qtdReclamacoes);
                dtoContribuinte.setQtdMensagens(qtdMensagens);
                dtoContribuintes.add(dtoContribuinte);
            }
        }
        return dtoContribuintes;
    }

    private Integer retornaNumeroDeReclamacoesNaoRespondidas(String cnpj){
        Integer numeroReclamacoes = 0;
        List<DocumentoFiscalReclamado> documentosReclamados = documentoFiscalReclamadoRepository.findReclamacaoPorCnpj(cnpj);


        if(documentosReclamados==null ) return 0;
        if(documentosReclamados.isEmpty()) return 0;

        for(DocumentoFiscalReclamado reclamacao : documentosReclamados){
            List<ComplSituacaoReclamacao> statusDisponiveis = documentoFiscalReclamadoRepository.acoesDisponiveisDeReclamacaoParaOPerfil(TipoPerfilCadastroReclamacao.CONTRIBUINTE, reclamacao);

            if(statusDisponiveis != null && statusDisponiveis.size() > 0){
                numeroReclamacoes ++;
            }
        }

        return numeroReclamacoes;
    }

    private Integer listarQtdMensagensNaoLidas(Integer idPessoa){
        String sql = "select count(*) from " +
                "Nfg_Destinatario_Mensagem_Nfg where Id_Pessoa_Dest_Mensagem = ? " +
                "and Data_Primeira_Leitura_Mensagem is null " +
                "and Indi_Leitura_Contador is null";
        try{
            return jdbcTemplate.queryForObject(sql, Integer.class,idPessoa);
        }catch (Exception e){
            return null;
        }
    }

    public Integer findNumeroDeMensagensNaoLidasPeloCidadao(PessoaParticipante cidadao) {
        if (cidadao==null || cidadao.getGenPessoaFisica()==null)
            return 0;

        String sql = "select count(*) from  " +
                "nfg_destinatario_mensagem_nfg dest " +
                "where Dest.DATA_PRIMEIRA_LEITURA_MENSAGEM is null  " +
                "and Dest.Id_Pessoa_Dest_Mensagem = ?";
        try{
            return jdbcTemplate.queryForObject(sql,Integer.class,cidadao.getGenPessoaFisica().getIdPessoa());
        }catch (EmptyResultDataAccessException e){
            return 0;
        }
    }

    public boolean gravarLeituraDasMensagens(PessoaParticipante cidadao) {
        if (cidadao==null || cidadao.getGenPessoaFisica()==null)
            return false;

        String sqlInsert = "update nfg_destinatario_mensagem_nfg set DATA_PRIMEIRA_LEITURA_MENSAGEM = sysdate , " +
                " Indi_Leitura_Contador = 'N'  " +
                " where Id_Pessoa_Dest_Mensagem = ?";

        try {
            int result = jdbcTemplate.update(sqlInsert,cidadao.getGenPessoaFisica().getIdPessoa());
            return result>0;
        }catch (Exception e){
            return false;
        }
    }

    private List<DestinatarioMensagem> listarMensagensNaoLidas(Integer idPessoa){
        List<DestinatarioMensagem> mensagensNaoLidas;

        try{
            String sql = "select dest from " +
                    "DestinatarioMensagem dest where dest.genPessoa.idPessoa = :idPessoa " +
                    "and dest.dataLeitura is null " +
                    "and dest.leituraContador is null";

            Query query = this.entityManager().createQuery(sql);
            query.setParameter("idPessoa", idPessoa);
            mensagensNaoLidas =  query.getResultList();

        }catch (Exception e){
            return null;
        }
        return mensagensNaoLidas;
    }

    public boolean gravarLeituraDasMensagensEmpresas(Integer idPessoa) {
        GENPessoaFisica contador = (GENPessoaFisica) request.getSession().getAttribute(ContadorLoginService.SESSION_CONTADOR_LOGADO);
        GENEmpresa empresa = (GENEmpresa) request.getSession().getAttribute(CertificateInterceptor.SESSION_EMPRESA_LOGADA);

        List<DestinatarioMensagem> mensagensNaoLidas = listarMensagensNaoLidas(idPessoa);

        if(mensagensNaoLidas != null && mensagensNaoLidas.size() > 0){
            int result = 0;
            for(DestinatarioMensagem dest : mensagensNaoLidas){
                Integer idMensagem = dest.getMensagem().getId();
                if (contador != null){
                    String sqlInsert = "update nfg_destinatario_mensagem_nfg set DATA_PRIMEIRA_LEITURA_MENSAGEM = sysdate , " +
                            " Indi_Leitura_Contador = 'S'  " +
                            " where Id_Pessoa_Dest_Mensagem = ?" +
                            " and id_mensagem_nfg = ?";

                    try {
                        result = jdbcTemplate.update(sqlInsert, idPessoa, idMensagem);
                    }catch (Exception e){
                        return false;
                    }

                }else if(empresa != null){
                    String sqlInsert = "update nfg_destinatario_mensagem_nfg set DATA_PRIMEIRA_LEITURA_MENSAGEM = sysdate , " +
                            " Indi_Leitura_Contador = 'N'  " +
                            " where Id_Pessoa_Dest_Mensagem = ?" +
                            " and id_mensagem_nfg = ?";

                    try {
                        result = jdbcTemplate.update(sqlInsert, idPessoa, idMensagem);
                    }catch (Exception e){
                        return false;
                    }
                }
            }
            return result>0;
        }
        return false;
    }
}
