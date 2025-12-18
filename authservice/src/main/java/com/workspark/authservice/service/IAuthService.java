package com.workspark.authservice.service;

import com.workspark.authservice.model.AuthenticationResponse;
import com.workspark.authservice.model.ChangePasswordRequest;
import com.workspark.authservice.model.SignInRequest;
import com.workspark.models.request.SignupRequest;

public interface IAuthService {
	
	void signUp(SignupRequest request);
	
	AuthenticationResponse signIn(SignInRequest request);

    AuthenticationResponse refreshToken(String refreshToken);
    
    void resetPassword(String resetToken, String newPassword);

	void changePassword(ChangePasswordRequest changePasswordRequest);

	String validateToken(String token);


}
