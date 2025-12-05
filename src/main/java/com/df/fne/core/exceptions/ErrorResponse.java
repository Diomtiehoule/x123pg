package com.df.fne.core.exceptions;


public record ErrorResponse(boolean status, String message, int code) {
}
