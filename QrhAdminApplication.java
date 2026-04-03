
package eu.unicredit.xframe.qrh.admin.rs;

import eu.unicredit.qrh.core.utils.persistence.repository.CustomRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.task.TaskDecorator;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;

@SpringBootApplication(exclude = {
        UserDetailsServiceAutoConfiguration.class})
@EnableAspectJAutoProxy
@Slf4j
@EnableTransactionManagement
@EnableScheduling
@EnableAsync(proxyTargetClass = true)
@ComponentScan(value = {"eu.unicredit.xframe.qrh.admin.rs", "eu.unicredit.qrh.core", "eu.unicredit.qrh.admin", "eu.unicredit.qrh"})
@EntityScan(basePackages = {"eu.unicredit.xframe.qrh.admin.rs", "eu.unicredit.qrh.core", "eu.unicredit.qrh.admin"})
@EnableJpaRepositories(basePackages = {"eu.unicredit.qrh.core","eu.unicredit.xframe.qrh.admin.rs.repository"}, repositoryBaseClass = CustomRepositoryImpl.class)
public class QrhAdminApplication implements AsyncConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(QrhAdminApplication.class, args);
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setTaskDecorator(new MdcTaskDecorator());
        executor.initialize();
        return executor;
    }

    static class MdcTaskDecorator implements TaskDecorator {

        @Override
        public Runnable decorate(Runnable runnable) {
            // Right now: Web thread context !
            // (Grab the current thread MDC data)
            Map<String, String> contextMap = Optional.ofNullable(MDC.getCopyOfContextMap()).orElse(Collections.emptyMap());
            return () -> {
                try {
                    // Right now: @Async thread context !
                    // (Restore the Web thread context's MDC data)
                    MDC.setContextMap(contextMap);
                    runnable.run();
                } finally {
                    MDC.clear();
                }
            };
        }
    }
}
