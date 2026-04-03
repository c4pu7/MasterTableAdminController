
import eu.unicredit.qrh.core.env.service.EnvService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = QrhAdminApplication.class)
@Import(CoreEngineMockConfig.class)
public class BaseContextTest extends CommonEnvironmentConfig {

    @MockBean
    protected EnvService envService;
}
