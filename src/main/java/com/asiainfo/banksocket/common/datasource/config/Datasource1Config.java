package com.asiainfo.banksocket.common.datasource.config;

import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;

@Configuration
//扫描 Mapper 接口并容器管理
@MapperScan(basePackages = Datasource1Config.PACKAGE, sqlSessionFactoryRef = "ds1SqlSessionFactory")
public class Datasource1Config {
    private Logger logger = Logger.getLogger(this.getClass());	//log4j日志
    // 精确到 master 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.asiainfo.banksocket.mapper.mysqlmapper";
    static final String MAPPER_LOCATION = "classpath:mapper/mysqlmapper/*.xml";

    @Value("${ds1.datasource.url}")
    private String url;
    @Value("${ds1.datasource.username}")
    private String user;
    @Value("${ds1.datasource.password}")
    private String password;
    @Value("${ds1.datasource.driverClassName}")
    private String driverClass;
    @Value("${ds1.datasource.druid.validation-query}")
    private String validationQuery;
    @Value("${ds1.datasource.maxActive}")
    private Integer maxActive;
    @Value("${ds1.datasource.minIdle}")
    private Integer minIdle;
    @Value("${ds1.datasource.initialSize}")
    private Integer initialSize;
    @Value("${ds1.datasource.maxWait}")
    private Long maxWait;
    @Value("${ds1.datasource.timeBetweenEvictionRunsMillis}")
    private Long timeBetweenEvictionRunsMillis;
    @Value("${ds1.datasource.minEvictableIdleTimeMillis}")
    private Long minEvictableIdleTimeMillis;
    @Value("${ds1.datasource.testWhileIdle}")
    private Boolean testWhileIdle;
    @Value("${ds1.datasource.testWhileIdle}")
    private Boolean testOnBorrow;
    @Value("${ds1.datasource.testOnBorrow}")
    private Boolean testOnReturn;
    @Value("${ds1.datasource.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;
    @Value("${ds1.datasource.druid.filters}")
    private String filters;
    @Value("${ds1.datasource.druid.connect-properties}")
    private String connectionProperties;
    @Value("${ds1.datasource.druid.use-global-data-source-stat}")
    private boolean userGlobalDataSourceStat;
    @Value("${ds1.datasource.druid.filter.stat.log-slow-sql}")
    private boolean filterStatLogSlowSql;
    @Value("${ds1.datasource.druid.filter.stat.slow-sql-millis}")
    private long fileterStatSlowSqlMillis;
    private String publicKey;
    @Bean(name = "ds1DataSource")
    @Primary
    public DataSource ds1DataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        //连接池配置
        dataSource.setMaxActive(maxActive);
        dataSource.setMinIdle(minIdle);
        dataSource.setInitialSize(initialSize);
        dataSource.setMaxWait(maxWait);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSource.setTestWhileIdle(testWhileIdle);
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnReturn(testOnReturn);
        dataSource.setValidationQuery(validationQuery);

        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        dataSource.setUseGlobalDataSourceStat(userGlobalDataSourceStat);
        try {
            dataSource.setFilters(filters);
        } catch (SQLException e) {
            //logger.error("druid configuration initialization filter", e);
        }
        dataSource.setConnectionProperties(connectionProperties);
        dataSource.setProxyFilters(Arrays.asList(statDuridFilter(),logFilter()));
        return dataSource;
    }
    @Bean
    @Primary
    public StatFilter statDuridFilter(){
        StatFilter statFilter = new StatFilter();
        statFilter.setSlowSqlMillis(fileterStatSlowSqlMillis);
        statFilter.setLogSlowSql(filterStatLogSlowSql);
        return statFilter;
    }
    @Bean
    public Slf4jLogFilter logFilter(){
        Slf4jLogFilter filter = new Slf4jLogFilter();
        return  filter;
    }
    @Bean(name = "ds1TransactionManager")
    @Primary
    public DataSourceTransactionManager ds1TransactionManager() {
        return new DataSourceTransactionManager(ds1DataSource());
    }

    @Bean(name = "ds1SqlSessionFactory")
    @Primary
    public SqlSessionFactory ds1SqlSessionFactory(@Qualifier("ds1DataSource") DataSource ds1DataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(ds1DataSource);
       // sessionFactory.setTypeAliasesPackage("nc.edu.nuc.Test.entity");
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(Datasource1Config.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
    @Bean(name = "ds1SqlSessionTemplate")
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("ds1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}