
import eu.unicredit.qrh.admin.lib.configuration.QrhJpaConfig;
import eu.unicredit.qrh.core.env.service.EnvService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = QrhAdminApplication.class)
@Import(CoreEngineMockConfig.class)
@ComponentScan(
        basePackages = "eu.unicredit",
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = QrhJpaConfig.class
        )
)
public class BaseContextTest extends CommonEnvironmentConfig {

    @MockBean
    protected EnvService envService;
