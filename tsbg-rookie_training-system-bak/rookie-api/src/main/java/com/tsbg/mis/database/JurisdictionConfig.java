package com.tsbg.mis.database;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.tsbg.mis.dao.jurisdiction"}, sqlSessionTemplateRef = "jurisdictionSqlSessionTemplate")
public class JurisdictionConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.jurisdiction.datasource")
    public DataSource jurisdictionDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public SqlSessionTemplate jurisdictionSqlSessionTemplate(@Qualifier("jurisdictionSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        SqlSessionTemplate template = new SqlSessionTemplate(sqlSessionFactory);
        return template;
    }

    @Bean
    public SqlSessionFactory jurisdictionSqlSessionFactory(@Qualifier("jurisdictionDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);

        //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources("classpath*:sqlMapper/jurisdiction/*.xml"));
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
