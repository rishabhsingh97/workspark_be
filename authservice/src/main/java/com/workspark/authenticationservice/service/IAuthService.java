package com.workspark.authenticationservice.service;

import com.workspark.authenticationservice.model.AuthenticationResponse;
import com.workspark.authenticationservice.model.ChangePasswordRequest;
import com.workspark.authenticationservice.model.SignInRequest;
import com.workspark.models.request.SignupRequest;

public interface IAuthService {
	
	void signUp(SignupRequest request);
	
	AuthenticationResponse signIn(SignInRequest request);

    AuthenticationResponse refreshToken(String refreshToken);
    
    void resetPassword(String resetToken, String newPassword);

	void changePassword(ChangePasswordRequest changePasswordRequest);

	String validateToken(String token);


}
