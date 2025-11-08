package com.example.trainerworkloadservice.integrationTest;

import com.example.trainerworkloadservice.messaging.WorkloadMessageListener;
import com.example.trainerworkloadservice.service.TrainerWorkloadService;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static io.cucumber.junit.platform.engine.Constants.*;


@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME,
        value = "pretty, summary")
@ConfigurationParameter(key = ANSI_COLORS_DISABLED_PROPERTY_NAME, value = "false")
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(
        key = GLUE_PROPERTY_NAME,
        value = "com.example.trainerworkloadservice.integrationTest.steps"
)
public class CucumberIntegrationTest {
    @MockitoBean protected WorkloadMessageListener workloadMessageListener;
    @MockitoBean protected TrainerWorkloadService trainerWorkloadService;
}