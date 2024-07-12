package org.mifos.connector.common.exception.mapper;

import java.util.HashMap;
import java.util.Map;

import org.mifos.connector.common.channel.dto.PhErrorDTO;
import org.mifos.connector.common.exception.PaymentHubError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.mifos.connector.common.exception.ValidationException;

/**
 * Default implementation of mapper. This class can be used in ams or payment schema connector for creating there
 * respective mappings of external error codes.
 */
public abstract class ErrorMapper implements Mapper {

    private Map<String, PaymentHubError> errorMap = new HashMap<>();

    public ErrorMapper() {
        configure();
    }

    @Override
    public void add(String externalErrorCode, PaymentHubError internalError) {
        errorMap.put(externalErrorCode, internalError);
    }

    @Override
    public void add(String externalErrorCode, String internalError) {
        errorMap.put(externalErrorCode, PaymentHubError.fromCode(internalError));
    }

    @Override
    public PaymentHubError getInternalError(String externalErrorCode) {
        return errorMap.get(externalErrorCode);
    }

    @Override
    public String getExternalError(String internalErrorCode) {
        PaymentHubError paymentHubErrors = PaymentHubError.fromCode(internalErrorCode);
        PaymentHubError filterResult = errorMap.values().stream().filter(paymentHubErrors::equals).findFirst()
                .orElseThrow(() -> new RuntimeException("Can not get external error code for internal error code: " + internalErrorCode));
        return filterResult.getErrorCode();
    }

    @Override
    public String getExternalError(PaymentHubError internalErrorCode) {
        PaymentHubError filterResult = errorMap.values().stream().filter(internalErrorCode::equals).findFirst()
                .orElseThrow(() -> new RuntimeException("Can not get external error code for internal error code: " + internalErrorCode));
        return filterResult.getErrorCode();
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<PhErrorDTO> handleValidationException(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getPhErrorDTO());
    }

}
