//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package eu.unicredit.qrh.admin.lib.configuration;

import jakarta.persistence.EntityManagerFactory;
import java.util.Objects;
import javax.sql.DataSource;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = {"eu.unicredit.qrh.admin.lib.repository"},
    entityManagerFactoryRef = "qrhadmindbEntityManagerFactory",
    transactionManagerRef = "qrhadmindbTransactionManager"
)
@EntityScan({"eu.unicredit.qrh.admin.lib"})
public class QrhJpaConfig {
    public QrhJpaConfig() {
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean qrhadmindbEntityManagerFactory(@Autowired @Qualifier("qrhadmindbDataSource") DataSource dataSource, @Autowired @Value("${spring.datasource.qrhadmindb.jpa.show-sql}") String showSql, @Autowired @Value("${spring.datasource.qrhadmindb.jpa.open-in-view}") String openInView, @Autowired @Value("${spring.datasource.qrhadmindb.jpa.generate-ddl}") String generateDdl, @Autowired EntityManagerFactoryBuilder builder) {
        JpaProperties jpaProperties = new JpaProperties();
        jpaProperties.setShowSql(BooleanUtils.toBoolean(showSql));
        jpaProperties.setOpenInView(BooleanUtils.toBoolean(openInView));
        jpaProperties.setGenerateDdl(BooleanUtils.toBoolean(generateDdl));
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = builder.dataSource(dataSource).packages(new String[]{"eu.unicredit.qrh.admin.lib"}).build();
        localContainerEntityManagerFactoryBean.setJpaPropertyMap(jpaProperties.getProperties());
        return localContainerEntityManagerFactoryBean;
    }

    @Bean
    @Primary
    public PlatformTransactionManager qrhadmindbTransactionManager(@Autowired @Qualifier("qrhadmindbEntityManagerFactory") LocalContainerEntityManagerFactoryBean qrhadmindbEntityManagerFactory) {
        return new JpaTransactionManager((EntityManagerFactory)Objects.requireNonNull(qrhadmindbEntityManagerFactory.getObject()));
    }
}
