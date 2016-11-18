package gov.goias.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * @author henrique-rh
 * @since 19/03/15.
 */
@Configuration
public abstract class DataSourceConfig {

    private Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    public abstract DataSource dataSource();

//    @Bean
//    public PlatformTransactionManager transactionManager() {
//        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
//        transactionManager.setDataSource(dataSource());
//        transactionManager.afterPropertiesSet();
//        return transactionManager;
//    }
//
//    @Bean
//    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
//        jdbcTemplate.afterPropertiesSet();
//        return jdbcTemplate;
//    }

    protected DataSource getDefaultDataSource(String jdbcUrl, String user, String password) {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass("oracle.jdbc.OracleDriver");
            dataSource.setJdbcUrl(jdbcUrl);
            dataSource.setUser(user);
            dataSource.setPassword(password);
            dataSource.setInitialPoolSize(1);
            dataSource.setMinPoolSize(1);
            dataSource.setMaxPoolSize(20);
            dataSource.setAcquireIncrement(1);
            dataSource.setAcquireRetryAttempts(3);
            dataSource.setAcquireRetryDelay(60 * 1000); // 1 min
//            dataSource.setTestConnectionOnCheckin(true);
            dataSource.setIdleConnectionTestPeriod(60 * 30); //30 min
//            dataSource.setTestConnectionOnCheckout(true);
            dataSource.setMaxIdleTime(60 * 2); // 2 min
            dataSource.setMaxConnectionAge(60 * 60); // 60 min
//            dataSource.setPreferredTestQuery("select 1 from dual");
            dataSource.setIdleConnectionTestPeriod(60 * 2); // 2 min
            dataSource.setCheckoutTimeout(5 * 1000); // 5 seconds
        } catch (PropertyVetoException e) {
            logger.error(e.getMessage(), e);
        }
        return dataSource;
    }
}
