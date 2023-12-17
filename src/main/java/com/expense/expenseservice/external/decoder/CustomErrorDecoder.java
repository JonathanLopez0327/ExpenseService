package com.expense.expenseservice.external.decoder;

import com.expense.expenseservice.exception.ExpenseServiceCustomException;
import com.expense.expenseservice.model.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        ObjectMapper objectMapper
                = new ObjectMapper();

        log.info("::{}", response.request().url());
        log.info("::{}", response.request().headers());

        try {
            ErrorResponse errorResponse
                    = objectMapper.readValue(response.body().asInputStream(),
                    ErrorResponse.class);

            return new ExpenseServiceCustomException(
                    errorResponse.getErrorMessage(),
                    errorResponse.getErrorCode(),
                    response.status()
            );
        } catch (IOException e) {
            throw new ExpenseServiceCustomException(
                    "Internal Server Error",
                    "INTERNAL_SERVER_ERROR",
                    500);
        }
    }
}
