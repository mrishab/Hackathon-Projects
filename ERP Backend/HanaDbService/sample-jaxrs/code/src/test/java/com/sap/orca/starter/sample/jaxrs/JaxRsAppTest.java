package com.sap.orca.starter.sample.jaxrs;

import com.sap.orca.starter.sample.jaxrs.model.Model;
import com.sap.orca.test.container.config.TestContainer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestContainer(tenant = "google", user = "david")
public class JaxRsAppTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void exampleTest() {
        @SuppressWarnings("unchecked")
        List<Model> models = this.restTemplate.getForObject("/api/v1/model", List.class);
        assertThat(models.size()).isEqualTo(1);
    }
}
