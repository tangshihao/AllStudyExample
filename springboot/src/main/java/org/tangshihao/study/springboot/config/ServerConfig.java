package org.tangshihao.study.springboot.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.IOException;

@Configuration
public class ServerConfig {
    @Value("${connection.pool.url}")
    private String url;
    @Value("${connection.pool.driverclass}")
    private String driverclass;
    @Value("${connection.pool.username}")
    private String username;
    @Value("${connection.pool.password}")
    private String password;
    @Value("${connection.pool.initialPoolSize}")
    private String initialPoolSize;
    @Value("${connection.pool.maxPoolSize}")
    private String maxPoolSize;
    @Value("${connection.pool.maxIdleTime}")
    private String maxIdleTime;
    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;

    @Bean(name = "c3p0")
    public DataSource c3p0() throws PropertyVetoException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(driverclass);
        dataSource.setJdbcUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setInitialPoolSize(Integer.valueOf(initialPoolSize));
        dataSource.setMaxPoolSize(Integer.valueOf(maxPoolSize));
        dataSource.setMaxIdleTime(Integer.valueOf(maxIdleTime));
        return dataSource;
    }

    @Bean(name = "mybatis")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("c3p0") DataSource dataSource) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
        .getResources(mapperLocations));
        return sessionFactory.getObject();
    }
}
