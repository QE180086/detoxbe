package com.parttime.job.Application.projectmanagementservice.configuration.handler;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.constant.MessageConstant;
import com.parttime.job.Application.common.dto.ErrorDTO;
import com.parttime.job.Application.common.dto.GenericResponse;
import com.parttime.job.Application.common.dto.MessageDTO;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.common.exception.ExportException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.PropertyValueException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomerExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String MESSAGE_ERROR = "Eception: \n MessageCode = %s \n Message = %s";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();

        if (!errors.isEmpty()) {
            FieldError firstError = errors.get(0);

            ErrorDTO errorDTO = new ErrorDTO(
                    firstError.getField(),
                    firstError.getDefaultMessage()
            );


            GenericResponse<Object> response = GenericResponse.builder()
                    .isSuccess(false)
                    .message(MessageDTO.builder()
                            .messageCode(MessageCodeConstant.M016_VALIDATION_FAILED)
                            .messageDetail(errorDTO.getMessage())
                            .build())
                    .build();

            logger.error(String.format("%s : %s", MessageCodeConstant.M016_VALIDATION_FAILED, errors));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        GenericResponse<Object> defaultResponse = GenericResponse.builder()
                .isSuccess(false)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M016_VALIDATION_FAILED)
                        .messageDetail("Validation failed")
                        .build())
                .build();

        return new ResponseEntity<>(defaultResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GenericResponse<Object> handleGlobalException(Exception ex, HttpServletRequest request) {
        GenericResponse<Object> response = GenericResponse.builder()
                .isSuccess(false)
                .message(MessageDTO.builder()
                        .messageCode(MessageCodeConstant.M026_FAIL)
                        .messageDetail(ex.getMessage())
                        .build())
                .build();

        log.error("Global Exception: {}", ex.getMessage(), ex);
        return response;
    }

        /**
         * App exception
         *
         * @param ex exception
         * @param request request
         * @return GenericResponse<String>
         */

        @ExceptionHandler(AppException.class)
        @ResponseStatus(value = HttpStatus.BAD_REQUEST)
        public GenericResponse<Object> appException (AppException ex, HttpServletRequest request){
            GenericResponse<Object> response = GenericResponse.builder()
                    .isSuccess(false)
                    .message(MessageDTO.builder()
                            .messageCode(ex.getMessageCode())
                            .messageDetail(ex.getMessage())
                            .build())
                    .build();

            logger.error(String.format(MESSAGE_ERROR, ex.getMessageCode(), ex.getCause()));
            return response;
        }

        /**
         * Export exception
         *
         * @param ex exception
         * @param request request
         * @return GenericResponse<String>
         */

        @ExceptionHandler(ExportException.class)
        @ResponseStatus(value = HttpStatus.BAD_REQUEST)
        public GenericResponse<Object> exportException (ExportException ex, HttpServletRequest request){
            GenericResponse<Object> response = GenericResponse.builder()
                    .isSuccess(false)
                    .message(MessageDTO.builder()
                            .messageCode(ex.getMessageCode())
                            .messageDetail(ex.getMessage())
                            .build())
                    .build();

            logger.error(String.format(MESSAGE_ERROR, ex.getMessageCode(), ex.getCause()));
            return response;
        }

        @ExceptionHandler(NullPointerException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public GenericResponse<Object> handleNullPointerException (NullPointerException ex, HttpServletRequest request){
            GenericResponse<Object> response = GenericResponse.builder()
                    .isSuccess(false)
                    .message(MessageDTO.builder()
                            .messageCode("Null Pointer Exception")
                            .messageDetail(ex.getMessage())
                            .build())
                    .build();

            logger.error("NullPointerException: " + ex.getMessage(), ex);
            return response;
        }

        @ExceptionHandler(PropertyValueException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public GenericResponse<Object> handlePropertyValueException (PropertyValueException ex, HttpServletRequest
        request){
            GenericResponse<Object> response = GenericResponse.builder()
                    .isSuccess(false)
                    .message(MessageDTO.builder()
                            .messageCode("Property Value Exception")
                            .messageDetail(ex.getMessage())
                            .build())
                    .build();

            logger.error("Property Value Exception: " + ex.getMessage(), ex);
            return response;
        }

    }
