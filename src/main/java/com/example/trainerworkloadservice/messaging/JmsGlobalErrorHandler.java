package com.example.trainerworkloadservice.messaging;

import com.example.trainerworkloadservice.logging.LoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ErrorHandler;

public class JmsGlobalErrorHandler  implements ErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public void handleError(Throwable t) {
        logger.error("Error Message : {}", t.getMessage());
    }

}
