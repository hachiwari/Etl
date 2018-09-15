package com.tkurek.wat.Etl.config;

import com.tkurek.wat.Etl.mapper.TestMapper;
import com.tkurek.wat.Etl.mapper.TwoMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.inject.Named;
import javax.sql.DataSource;

@Configuration
public class MyBatisConfiguration {
    private static final String ONE_SESSION_FACTORY = "oneSessionFactory";
    private static final String ANOTHER_SESSION_FACTORY = "anotherSessionFactory";

    @Bean(name = ONE_SESSION_FACTORY, destroyMethod = "")
    @Primary
    public SqlSessionFactoryBean sqlSessionFactory(@Named(DatabaseConfiguration.DATABASE_SOURCE_ONE) final DataSource oneDataSource) throws Exception {
        final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(oneDataSource);
        SqlSessionFactory sqlSessionFactory;
        sqlSessionFactory = sqlSessionFactoryBean.getObject();
        sqlSessionFactory.getConfiguration().addMapper(TestMapper.class);
        // Various other SqlSessionFactory settings
        return sqlSessionFactoryBean;
    }

    @Bean
    public MapperFactoryBean<TestMapper> etrMapper(@Named(ONE_SESSION_FACTORY) final SqlSessionFactoryBean sqlSessionFactoryBean) throws Exception {
        MapperFactoryBean<TestMapper> factoryBean = new MapperFactoryBean<>(TestMapper.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactoryBean.getObject());
        return factoryBean;
    }

    @Bean(name = ANOTHER_SESSION_FACTORY, destroyMethod = "")
    public SqlSessionFactoryBean censoSqlSessionFactory(@Named(DatabaseConfiguration.DATABASE_SOURCE_TWO) final DataSource anotherDataSource) throws Exception {
        final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(anotherDataSource);
//        sqlSessionFactoryBean.setMapperLocations("classpath*:db/mapper/*.xml");
        final SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBean.getObject();
        sqlSessionFactory.getConfiguration().addMapper(TwoMapper.class);
        // Various other SqlSessionFactory settings
        return sqlSessionFactoryBean;
    }

    @Bean
    public MapperFactoryBean<TwoMapper> dbMapper(@Named(ANOTHER_SESSION_FACTORY) final SqlSessionFactoryBean sqlSessionFactoryBean) throws Exception {
        MapperFactoryBean<TwoMapper> factoryBean = new MapperFactoryBean<>(TwoMapper.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactoryBean.getObject());
        return factoryBean;
    }
}
