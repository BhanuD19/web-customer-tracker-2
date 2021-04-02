package com.vaibsD.springdemo.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;
import java.util.logging.Logger;

@Configuration
@PropertySource("classpath:persistence-mysql.properties")
@EnableTransactionManagement
@EnableWebSecurity
public class AppDataSourceConfig implements WebMvcConfigurer {

    private final Logger logger;
    private final Environment env;

    public AppDataSourceConfig(Environment env) {
        this.env = env;
        this.logger = Logger.getLogger("AppDataSourceConfig.class");
    }

    @Bean
    public LocalSessionFactoryBean sessionFactoryBean() {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource());
        sessionFactoryBean.setPackagesToScan("com.vaibsD.springdemo.entity");
        sessionFactoryBean.setHibernateProperties(getHibernateProperties());

        return sessionFactoryBean;
    }

    @Bean
    public DataSource dataSource() {
        logger.info(">>>>>jdbc url: " + env.getProperty("jdbc.url"));
        logger.info(">>>>>jdbc driver: " + env.getProperty("jdbc.driver"));
        logger.info(">>>>>jdbc user: " + env.getProperty("jdbc.user"));
        logger.info(">>>>>jdbc password: " + env.getProperty("jdbc.password"));
        logger.info(">>>>>jdbc initpoolsize: " + getIntProperty("connection.pool.initialPoolSize"));
        logger.info(">>>>>jdbc minpoolsize: " + getIntProperty("connection.pool.minPoolSize"));
        logger.info(">>>>>jdbc maxpoolsize: " + getIntProperty("connection.pool.maxPoolSize"));
        logger.info(">>>>>jdbc maxidletime: " + getIntProperty("connection.pool.maxIdleTime"));


        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass(env.getProperty("jdbc.driver"));
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
        dataSource.setJdbcUrl(env.getProperty("jdbc.url"));
        dataSource.setUser(env.getProperty("jdbc.user"));
        dataSource.setPassword(env.getProperty("jdbc.password"));

        dataSource.setInitialPoolSize(getIntProperty("connection.pool.initialPoolSize"));
        dataSource.setMinPoolSize(getIntProperty("connection.pool.minPoolSize"));
        dataSource.setMaxPoolSize(getIntProperty("connection.pool.maxPoolSize"));
        dataSource.setMaxIdleTime(getIntProperty("connection.pool.maxIdleTime"));

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        HibernateTransactionManager manager = new HibernateTransactionManager();
        manager.setSessionFactory(sessionFactoryBean().getObject());
        return manager;
    }


    private Properties getHibernateProperties() {
        Properties hibernateProp = new Properties();
        hibernateProp.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialect");
        hibernateProp.setProperty("hibernate.show_sql", "true");

        return hibernateProp;
    }


    private int getIntProperty(String s) {
        return Integer.parseInt(env.getProperty(s));
    }

}
