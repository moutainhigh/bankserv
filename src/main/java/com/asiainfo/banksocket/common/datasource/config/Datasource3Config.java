package com.asiainfo.banksocket.common.datasource.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author WangBaoQiang
 * @ClassName: Datasource3Config
 * @description: TODO
 * @date 2019/8/69:11
 * @Version 1.0
 */
@Deprecated
//@Configuration
//扫描 Mapper 接口并容器管理
@MapperScan(basePackages = Datasource3Config.PACKAGE, sqlSessionFactoryRef = "ds3SqlSessionFactory")

public class Datasource3Config {
    private Logger logger = Logger.getLogger(this.getClass());	//log4j日志
    // 精确到 master 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.asia.mapper.orclmapper";
    static final String MAPPER_LOCATION = "classpath:mapper/orclmapper/*.xml";

    @Value("${ds3.datasource.url}")
    private String url;
    @Value("${ds3.datasource.username}")
    private String user;
    @Value("${ds3.datasource.password}")
    private String password;
    @Value("${ds3.datasource.driverClassName}")
    private String driverClass;
    @Value("${ds3.datasource.druid.validation-query}")
    private String validationQuery;
    @Value("${ds3.datasource.maxActive}")
    private Integer maxActive;
    @Value("${ds3.datasource.minIdle}")
    private Integer minIdle;
    @Value("${ds3.datasource.initialSize}")
    private Integer initialSize;
    @Value("${ds3.datasource.maxWait}")
    private Long maxWait;
    @Value("${ds3.datasource.timeBetweenEvictionRunsMillis}")
    private Long timeBetweenEvictionRunsMillis;
    @Value("${ds3.datasource.minEvictableIdleTimeMillis}")
    private Long minEvictableIdleTimeMillis;
    @Value("${ds3.datasource.testWhileIdle}")
    private Boolean testWhileIdle;
    @Value("${ds3.datasource.testWhileIdle}")
    private Boolean testOnBorrow;
    @Value("${ds3.datasource.testOnBorrow}")
    private Boolean testOnReturn;
    @Value("${ds3.datasource.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;
    @Value("${ds3.datasource.druid.filters}")
    private String filters;
    @Value("${ds3.datasource.druid.connect-properties}")
    private String connectionProperties;
    @Value("${ds3.datasource.druid.use-global-data-source-stat}")
    private boolean userGlobalDataSourceStat;
    @Value("${ds3.datasource.druid.filter.stat.log-slow-sql}")
    private boolean filterStatLogSlowSql;
    @Value("${ds3.datasource.druid.filter.stat.slow-sql-millis}")
    private long fileterStatSlowSqlMillis;
    @Bean(name = "ds3DataSource")
    public DataSource ds3DataSource() {
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
            logger.error("druid configuration initialization filter", e);
        }
        dataSource.setConnectionProperties(connectionProperties);
        //dataSource.setProxyFilters(Arrays.asList(statDuridFilter(),logFilter()));
        return dataSource;
    }

    @Bean(name = "ds3TransactionManager")
    public DataSourceTransactionManager ds3TransactionManager() {
        return new DataSourceTransactionManager(ds3DataSource());
    }

    @Bean(name = "ds3SqlSessionFactory")
    public SqlSessionFactory ds2SqlSessionFactory(@Qualifier("ds3DataSource") DataSource ds3DataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(ds3DataSource);
        //给类起别名，方便mybatis在Mapper配置文件里的引用
        //sessionFactory.setTypeAliasesPackage("nc.edu.nuc.Test.entity");
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(Datasource3Config.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
    @Bean(name = "ds3SqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("ds3SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
