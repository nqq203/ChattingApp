package com.main.adapters;

import com.main.entities.User;

public class SignUpAdapter {
    public interface SignupCallback {
        void onSuccess(User user);
        void onError(String message);
    }

    public void signupUser(String fullname, String gender, String birthDate, String phoneNumber, String password, SignupCallback callback) {
        if (fullname.isEmpty() || gender.isEmpty() || birthDate.isEmpty() || phoneNumber.isEmpty() || password.isEmpty()) {
            callback.onError("All fields are required.");
        }
        else {
            User newUser = new User(fullname, gender, birthDate, phoneNumber, password, null);
            callback.onSuccess(newUser);
        }
    }
}
