package org.mifos.connector.common.exception;

import org.mifos.connector.common.channel.dto.PhErrorDTO;

public class ValidationException extends RuntimeException {
    private final PhErrorDTO phErrorDTO;

    public ValidationException(PhErrorDTO phErrorDTO) {
        this.phErrorDTO = phErrorDTO;
    }

    public PhErrorDTO getPhErrorDTO() {
        return phErrorDTO;
    }

}
