package gov.goias.persistencia;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ParametrosRepository extends GenericRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public String get(String key){
		String sql = "select VALR_PARAMETRO from NFG_PARAMETRO_CONFIGURACAO_NFG" +
				" where NOME_PARAMETRO = ? ";

		String result = jdbcTemplate.queryForObject(sql,String.class,key);
		return result;
	}

	public boolean put(String key, String value){
		return false;
	}

}
