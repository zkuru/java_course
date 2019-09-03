package endpoint;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.lang.annotation.*;

@WebAppConfiguration
@ActiveProfiles("dev")
@ContextConfiguration("classpath:spring-web-servlet.xml")

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentTest {
}