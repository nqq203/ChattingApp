package com.main.adapters;

import com.main.entities.User;

public class SignInAdapter {
    public interface SigninCallback {
        void onSuccess(User user);
        void onError(String message);
    }

}
