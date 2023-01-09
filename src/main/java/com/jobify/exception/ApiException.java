package com.jobify.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiException extends RuntimeException
{
    /**
     *
     * @param message
     */
    public ApiException(String message)
    {
        super(message);
    }
}
