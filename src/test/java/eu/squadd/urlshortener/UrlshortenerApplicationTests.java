package eu.squadd.urlshortener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcConfigurerAdapter;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

@AutoConfigureMockMvc
public class UrlshortenerApplicationTests extends MockMvcConfigurerAdapter {

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    protected MockMvc mvc;

    protected void testConfiguration(String beanName) {
        ServletContext servletContext = this.wac.getServletContext();
        Assert.notNull(servletContext, "Servlet context must be initialized");
        Assert.isTrue(servletContext instanceof MockServletContext, "Servlet test context must be of type: MockServletContext");
        Assert.notNull(wac.getBean(beanName), "tested bean must exist");
    }

    protected static String toJson(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void contextLoads() {
    }
}
