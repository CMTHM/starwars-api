package com.example.starwars.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.starwars.dto.AuthRequest;
import com.example.starwars.service.JwtService;
import com.example.starwars.service.StarWarService;

@RestController
public class StarWarsController {
	private static final Logger logger = LogManager.getLogger(StarWarsController.class);
	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;
	private final StarWarService starWarsService;

	public StarWarsController(StarWarService starWarsService) {
		this.starWarsService = starWarsService;
	}

	@GetMapping("/search")
	public String search(@RequestParam String type, @RequestParam String name) {
		logger.info("Accessing the endpoint for swapi dev");
		return starWarsService.getEntityDetails(type, name);
	}

	@PostMapping("/authenticate")
	public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		if (authentication.isAuthenticated()) {
			return jwtService.generateToken(authRequest.getUsername());
		} else {
			throw new UsernameNotFoundException("invalid user request !");
		}

	}
}
