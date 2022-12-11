package com.thoughtworks.travel.basetest;

import com.thoughtworks.travel.Application;
import com.thoughtworks.travel.configuration.MariaDB4jExtension;
import com.thoughtworks.travel.configuration.TruncateDatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.reset;

@Slf4j
@ExtendWith({SpringExtension.class, MariaDB4jExtension.class})
//@SpringBootTest
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class ApplicationTestBase {
    @Autowired
    private TruncateDatabaseService truncateDatabaseService;

    @Autowired
    @Qualifier("mock")
    private Object[] mockObjects;

    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    void setUp() {
        reset(mockObjects);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        truncateDatabaseService.restartIdWith(1);
    }

}
