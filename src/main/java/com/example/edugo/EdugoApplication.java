package com.example.edugo;

import com.example.edugo.dto.LoginRequest;
import com.example.edugo.entity.User;
import com.example.edugo.security.JwtUtil;
import com.example.edugo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class EdugoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdugoApplication.class, args);
	}

	@Component
	@RequiredArgsConstructor
	public static class TestLogin implements CommandLineRunner {

		private final AuthService authService;
		private final JwtUtil jwtUtil;

		@Override
		public void run(String... args) throws Exception {
			LoginRequest loginRequest = new LoginRequest("admin@edugo.com", "admin123");
			User user = authService.authenticate(loginRequest);
			String token = jwtUtil.generateToken(user);
			System.out.println("Token généré : " + token);
		}
	}
}
