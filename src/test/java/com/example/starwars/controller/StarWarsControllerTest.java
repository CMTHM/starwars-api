package com.example.starwars.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.starwars.dto.AuthRequest;
import com.example.starwars.service.JwtService;
import com.example.starwars.service.StarWarService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class StarWarsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private StarWarService starWarService;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private StarWarsController starWarsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(starWarsController).build();
    }

    @Test
    void search_ReturnsEntityDetails() throws Exception {
        // Arrange
        String type = "Species";
        String name = "Anything";
        String expectedResponse="" ;
        
        when(starWarService.getEntityDetails(type, name)).thenReturn(expectedResponse);

        // Act & Assert
        mockMvc.perform(get("/search")
                .param("type", type)
                .param("name", name)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    public void authenticateAndGetToken_ValidCredentials_ReturnsToken() throws Exception {
        // Arrange
       // AuthRequest authRequest = new AuthRequest("username", "password");
        UserDetails userDetails = mock(UserDetails.class);
        String expectedToken = "token";
        
        // Mocking
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(mock(Authentication.class));
        when(jwtService.generateToken(userDetails)).thenReturn(expectedToken);

     }

	/*
	 * @Test void authenticateAndGetToken_ReturnsUnauthorized() throws Exception {
	 * // Arrange String username = "user"; String password = "wrong-pass";
	 * AuthRequest authRequest = new AuthRequest(username, password);
	 * 
	 * when(authenticationManager.authenticate(any(
	 * UsernamePasswordAuthenticationToken.class))) .thenThrow(new
	 * UsernameNotFoundException("org.springframework.security.core.userdetails.UsernameNotFoundException: invalid user request ! !"
	 * ));
	 * 
	 * // Act & Assert mockMvc.perform(post("/authenticate")
	 * .contentType(MediaType.APPLICATION_JSON) .content("{\"username\":\"" +
	 * username + "\", \"password\":\"" + password + "\"}")) .andExpect(content().
	 * string("org.springframework.security.core.userdetails.UsernameNotFoundException: invalid user request ! !"
	 * )); }
	 */
}
