package com.df.fne.presenter.request;


public record LoginRequest(String email, String password) {

    boolean isValid() {
        if(email == null || email.isEmpty()) {
            return false;
        }
        return password != null && !password.isEmpty();
    }
}
