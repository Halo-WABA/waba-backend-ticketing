package com.festimap.tiketing.global.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode {

    // Common Error
    INTERNAL_SERVER_ERROR("C001", "Server Error",500),
    INVALID_INPUT_VALUE("C002", "Invalid Input Value",400),
    METHOD_NOT_ALLOWED("C003", "Invalid HTTP Method",405),
    ENTITY_NOT_FOUND("C004", "Entity Not Found",400),
    INVALID_JSON_FORMAT("C008", "Invalid JSON Format",400),
    MISSING_PARAMETER("C009", "Missing Parameter",400),
    INVALID_PARAMETER_TYPE("C010", "Invalid Parameter Type",400),

    //DataBase Error
    ERR_DATA_INTEGRITY_VIOLATION("E001", "Data integrity violation",409),

    // Auth
    UN_AUTHENTICATED("A002", "Unauthenticated",401),
    UN_AUTHORIZED("A003", "Unauthorized",403),

    // Infra
    SMS_SEND_FAILED("I001", "SMS Send Failed",500),
    THREAD_POOL_REJECTED("I003", "Async thread pool rejected execution", 503),

    // Verification
    INVALID_VERIFICATION_CODE("V001", "Invalid Verification Code",400),
    EXPIRED_VERIFICATION_CODE("V002", "Verification code has expired", 400),
    VERIFICATION_REQUIRED("V003", "Verification has not been completed", 400),

    // Ticketing
    TICKET_RESERVATION_CLOSED("T001", "Ticket Reservation Closed",429),
    TICKET_EXIST_BY_PHONENUM("T002", "Ticket Exist By Phone Number",400),
    TICKET_SOLD_OUT("T003", "Not enough tickets remaining", 400),
    TICKET_SERVER_NOT_OPEN("T004", "Ticket Server Not Open", 400),
    TICKET_SERVICE_FINISHED("T005", "Ticket Service is Finished", 400),

    NOT_SUPPORTED_STRATEGY("T006","Not Supported Strategy",400),
    TICKET_SERVICE_CONGESTED("T007", "Ticketing Service Congested",500),
    TICKET_CANCELLATION_WINDOW_EXPIRED("T007", "Ticket cancellation window expired", 400),

    // Event
    EVENT_ALREADY_OPEN("EVT001", "Cannot modify event after open time", 400),
    NO_REMAINING_TICKETS("EVT002", "Cannot reopen event: no remaining tickets", 400),

    // Guest
    INVALID_GUEST_TOKEN("G001", "Invalid guest token or Expired guest token", 401),
    FORBIDDEN_GUEST_TOKEN("G003", "Token does not have GUEST role", 403),
    ;

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final String code, final String message, final int status) {
        this.message = message;
        this.code = code;
        this.status = status;
    }
}
