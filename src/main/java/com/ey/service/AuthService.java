package com.ey.service;

import com.ey.dto.request.LoginRequest;
import com.ey.dto.response.LoginResponse;

public interface AuthService {
	LoginResponse login(LoginRequest request);

}
