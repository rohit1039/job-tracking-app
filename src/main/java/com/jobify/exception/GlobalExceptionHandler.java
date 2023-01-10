package com.jobify.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler
{
    private static final Logger LOGGER = LogManager.getLogger(GlobalExceptionHandler.class.getName());

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        final Map<String, String> errors = new ConcurrentHashMap<>();

        exception.getBindingResult()
                 .getFieldErrors()
                 .forEach((error) ->
                          {
                              errors.put(error.getField(), error.getDefaultMessage());
                          });
        exception.getBindingResult()
                 .getGlobalErrors()
                 .forEach((errorGlobal) ->
                          {
                              errors.put(((FieldError) errorGlobal).getField(), errorGlobal.getDefaultMessage());
                          });
        LOGGER.error("{}", errors);
        return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionInApiResponse> handleGlobalErrorException(final Exception exception,
                                                                             final WebRequest request)
    {
        final ExceptionInApiResponse response = new ExceptionInApiResponse(LocalDateTime.now(), exception.getMessage(),
                                                                           request.getDescription(false));
        LOGGER.error("{}", response.getMessage());
        return new ResponseEntity<ExceptionInApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionInApiResponse> handleUserNotFoundException(final UsernameNotFoundException exception,
                                                                              final WebRequest request)
    {
        final ExceptionInApiResponse response = new ExceptionInApiResponse(LocalDateTime.now(), exception.getMessage(),
                                                                           request.getDescription(false));
        LOGGER.error("{}", response.getMessage());
        return new ResponseEntity<ExceptionInApiResponse>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionInApiResponse> handleDAOErrorException(final DataIntegrityViolationException exception,
                                                                          final WebRequest request)
    {
        final ExceptionInApiResponse response = new ExceptionInApiResponse(LocalDateTime.now(), exception.getMessage(),
                                                                           request.getDescription(false));
        LOGGER.error("{}", response.getMessage());
        return new ResponseEntity<ExceptionInApiResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionInApiResponse> handleUnAuthorizedErrorException(final BadCredentialsException exception,
                                                                                   final WebRequest request)
    {
        final ExceptionInApiResponse
            response =
            new ExceptionInApiResponse(LocalDateTime.now(), exception.getLocalizedMessage(),
                                       request.getDescription(false));
        LOGGER.error("{}", response.getMessage());
        return new ResponseEntity<ExceptionInApiResponse>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ExceptionInApiResponse> handleServerErrorException(final ApiException exception,
                                                                             final WebRequest request)
    {
        final ExceptionInApiResponse response = new ExceptionInApiResponse(LocalDateTime.now(), exception.getMessage(),
                                                                           request.getDescription(false));
        LOGGER.error("{}", response.getMessage());
        return new ResponseEntity<ExceptionInApiResponse>(response, HttpStatus.NOT_FOUND);
    }
}
