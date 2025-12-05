package com.df.fne.presenter.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@AllArgsConstructor
public class GenericResponse<T> {

    private boolean status;
    private String message;
    private T data;

    public static <T> GenericResponse<T> empty() {
        return success(null,null);
    }

    public static <T> GenericResponse<T> success(T data, String msg) {
        return GenericResponse.<T> builder()
                .message(msg)
                .data(data)
                .status(true)
                .build();
    }

    public static <T> GenericResponse<T> error(T error, String msg) {
        return GenericResponse.<T>builder()
                .message(msg)
                .status(false)
                .data(error)
                .build();
    }
}
