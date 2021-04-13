package web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement

@PropertySource("classpath:db.properties")

@EnableJpaRepositories("web")
public class DbConfig {
    private static final String Prop_Database_Driver = "db.driver";
    private static final String Prop_Database_Password = "db.password";
    private static final String Prop_Database_URL = "db.url";
    private static final String Prop_Database_UserName = "db.username";
    private static final String Prop_Hibernate_Dialect = "hibernate.dialect";
    private static final String Prop_Hibernate_Show_SQL = "hibernate.show_sql";
    private static final String Prop_Hibernate_Format_SQL = "hibernate.format_sql";
    private static final String Prop_Entitymanager_Packages_to_Scan = "entitymanager.packages.to.scan";
    private static final String Prop_Hibernate_hbm2ddl_AUTO = "hibernate.hbm2ddl.auto";

    private Environment env;

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;   }



    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean getEntityManagerFactoryBean() {
        LocalContainerEntityManagerFactoryBean factoryBean =
                new LocalContainerEntityManagerFactoryBean();
        factoryBean.setJpaVendorAdapter(getJpaVendorAdapter());
        factoryBean.setDataSource(getDataSource());
        factoryBean.setPersistenceUnitName("myJpaPersistenceUnit");
        factoryBean.setPackagesToScan(env.getRequiredProperty(Prop_Entitymanager_Packages_to_Scan));
        factoryBean.setJpaProperties(getHibernateProperties());
        return factoryBean;
    }

    @Bean
    public JpaVendorAdapter getJpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager getTransactionManager() {
        return new JpaTransactionManager(getEntityManagerFactoryBean().getObject());
    }

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty(Prop_Database_Driver));
        dataSource.setUrl(env.getRequiredProperty(Prop_Database_URL));
        dataSource.setUsername(env.getRequiredProperty(Prop_Database_UserName));
        dataSource.setPassword(env.getRequiredProperty(Prop_Database_Password));
        return dataSource;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put(Prop_Hibernate_Show_SQL, env.getRequiredProperty(Prop_Hibernate_Show_SQL));
        properties.put(Prop_Hibernate_Format_SQL, env.getRequiredProperty(Prop_Hibernate_Format_SQL));
        properties.put(Prop_Hibernate_Dialect, env.getRequiredProperty(Prop_Hibernate_Dialect));
        properties.put(Prop_Hibernate_hbm2ddl_AUTO, env.getRequiredProperty(Prop_Hibernate_hbm2ddl_AUTO));

        return properties;
    }
}


