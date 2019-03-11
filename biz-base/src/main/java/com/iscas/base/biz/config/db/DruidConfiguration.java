package com.iscas.base.biz.config.db;


import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author zhuquanwen
 * @vesion 1.0
 * @date 2018/8/31 11:01
 * @since jdk1.8
 */

@Slf4j
@Configuration
public class DruidConfiguration /*extends MybatisPlusConfig*/{
    @Value("${spring.datasource.druid.mysql.url}")
    private String dbUrl;
    @Value("${spring.datasource.druid.mysql.username}")
    private String username;
    @Value("${spring.datasource.druid.mysql.password}")
    private String password;
    @Value("${spring.datasource.druid.mysql.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.druid.mysql.initial-size}")
    private int initialSize;
    @Value("${spring.datasource.druid.mysql.min-idle}")
    private int minIdle;
    @Value("${spring.datasource.druid.mysql.max-active}")
    private int maxActive;
    @Value("${spring.datasource.druid.mysql.max-wait}")
    private int maxWait;
    @Value("${spring.datasource.druid.mysql.time-between-eviction-runs-millis}")
    private int timeBetweenEvictionRunsMillis;
    @Value("${spring.datasource.druid.mysql.min-evictable-idle-time-millis:60000}")
    private int minEvictableIdleTimeMillis;
    @Value("${spring.datasource.druid.mysql.validation-query}")
    private String validationQuery;
    @Value("${spring.datasource.druid.mysql.test-while-idle}")
    private boolean testWhileIdle;
    @Value("${spring.datasource.druid.mysql.test-on-borrow}")
    private boolean testOnBorrow;
    @Value("${spring.datasource.druid.mysql.test-on-return}")
    private boolean testOnReturn;
    @Value("${spring.datasource.druid.mysql.pool-prepared-statements}")
    private boolean poolPreparedStatements;
    @Value("${spring.datasource.druid.mysql.max-pool-prepared-statement-per-connection-size}")
    private int maxPoolPreparedStatementPerConnectionSize;
    @Value("${spring.datasource.druid.mysql.filters:stat,wall,logback}")
    private String filters;
    @Value("${spring.datasource.druid.mysql.filter.stat.log-slow-sql:true}")
    private boolean logslowSql;
    @Value("${spring.datasource.druid.mysql.filter.stat.merge-sql:true}")
    private boolean mergeSql;
    @Value("${spring.datasource.druid.mysql.filter.stat.slow-sql-millis:200}")
    private long slowSqlMill;

    //sqllite
    @Value("${spring.datasource.druid.sqllite.url}")
    private String dbUrl2;
//    @Value("${spring.datasource.druid.sqllite.username}")
//    private String username2;
//    @Value("${spring.datasource.druid.sqllite.password}")
//    private String password2;
    @Value("${spring.datasource.druid.sqllite.driver-class-name}")
    private String driverClassName2;
    @Value("${spring.datasource.druid.sqllite.initial-size}")
    private int initialSize2;
    @Value("${spring.datasource.druid.sqllite.min-idle}")
    private int minIdle2;
    @Value("${spring.datasource.druid.sqllite.max-active}")
    private int maxActive2;
    @Value("${spring.datasource.druid.sqllite.max-wait}")
    private int maxWait2;
    @Value("${spring.datasource.druid.sqllite.time-between-eviction-runs-millis}")
    private int timeBetweenEvictionRunsMillis2;
    @Value("${spring.datasource.druid.sqllite.min-evictable-idle-time-millis:60000}")
    private int minEvictableIdleTimeMillis2;
    @Value("${spring.datasource.druid.sqllite.validation-query}")
    private String validationQuery2;
    @Value("${spring.datasource.druid.sqllite.test-while-idle}")
    private boolean testWhileIdle2;
    @Value("${spring.datasource.druid.sqllite.test-on-borrow}")
    private boolean testOnBorrow2;
    @Value("${spring.datasource.druid.sqllite.test-on-return}")
    private boolean testOnReturn2;
    @Value("${spring.datasource.druid.sqllite.pool-prepared-statements}")
    private boolean poolPreparedStatements2;
    @Value("${spring.datasource.druid.sqllite.max-pool-prepared-statement-per-connection-size}")
    private int maxPoolPreparedStatementPerConnectionSize2;
//    @Value("${spring.datasource.druid.sqllite.filters:stat,wall,logback}")
//    private String filters2;
//    @Value("${spring.datasource.druid.sqllite.filter.stat.log-slow-sql:true}")
//    private boolean logslowSql2;
//    @Value("${spring.datasource.druid.sqllite.filter.stat.merge-sql:true}")
//    private boolean mergeSql2;
//    @Value("${spring.datasource.druid.sqllite.filter.stat.slow-sql-millis:200}")
//    private long slowSqlMill2;

    @Bean(name = "db_mysql")
    @ConfigurationProperties(prefix = "spring.datasource.druid.mysql")
    public DataSource db1(){
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(this.dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);

        //configuration
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);

        //        datasource.setUseGlobalDataSourceStat(useGlobalDataSourceStat);
        try {
            datasource.setFilters(filters);
        } catch (SQLException e) {
            log.error("druid configuration initialization filter: "+ e);
        }
        datasource.setProxyFilters(Arrays.asList(statFilter(),logFilter()));
//        datasource.setConnectionProperties(connectionProperties);
        return datasource;
    }

//    @Bean(name = "db_sqllite")
//    @ConfigurationProperties(prefix = "spring.datasource.sqllite.druid" )
//    public DataSource db2 () {
//        return DruidDataSourceBuilder.create().build();
//    }

    @Bean(name = "db_sqlite")
    public DataSource db2(){
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(this.dbUrl2);
//        datasource.setUsername(username2);
//        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName2);

        //configuration
        datasource.setInitialSize(initialSize2);
        datasource.setMinIdle(minIdle2);
        datasource.setMaxActive(maxActive2);
        datasource.setMaxWait(maxWait2);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis2);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis2);
        datasource.setValidationQuery(validationQuery2);
        datasource.setTestWhileIdle(testWhileIdle2);
        datasource.setTestOnBorrow(testOnBorrow2);
        datasource.setTestOnReturn(testOnReturn2);
        datasource.setPoolPreparedStatements(poolPreparedStatements2);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize2);
        datasource.setDbType(DbType.SQLITE.getDb());
        return datasource;
    }


    /**
     * 动态数据源配置
     * @return
     */
    @Bean
    @Primary
    public DataSource multipleDataSource (@Qualifier("db_mysql") DataSource db1,
                                          @Qualifier("db_sqlite") DataSource db2) {

        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map< Object, Object > targetDataSources = new HashMap<>();
        targetDataSources.put(DBTypeEnum.db1.getValue(), db1 );
        targetDataSources.put(DBTypeEnum.db2.getValue(), db2);
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(db1);
        return dynamicDataSource;
    }

    @Bean
    @Primary
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(multipleDataSource(db1(),db2()));
//        sqlSessionFactory.setDataSource(multipleDataSource);
        //sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/*/*Mapper.xml"));

        MybatisConfiguration configuration = new MybatisConfiguration();
        //configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        sqlSessionFactory.setConfiguration(configuration);
//        sqlSessionFactory.setPlugins(new Interceptor[]{ //PerformanceInterceptor(),OptimisticLockerInterceptor()
//                paginationInterceptor() //添加分页功能
//        });
//        sqlSessionFactory.setGlobalConfig(globalConfiguration());
        return sqlSessionFactory.getObject();
    }




    @Bean
    @Primary
    public StatFilter statFilter(){
        StatFilter statFilter = new StatFilter();
        statFilter.setSlowSqlMillis(slowSqlMill);
        statFilter.setLogSlowSql(logslowSql);
        statFilter.setMergeSql(mergeSql);
        return statFilter;
    }

    @Bean
    public Slf4jLogFilter logFilter(){
        Slf4jLogFilter filter = new Slf4jLogFilter();
//        filter.setResultSetLogEnabled(false);
//        filter.setConnectionLogEnabled(false);
//        filter.setStatementParameterClearLogEnable(false);
//        filter.setStatementCreateAfterLogEnabled(false);
//        filter.setStatementCloseAfterLogEnabled(false);
//        filter.setStatementParameterSetLogEnabled(false);
//        filter.setStatementPrepareAfterLogEnabled(false);
        return  filter;
    }
}
