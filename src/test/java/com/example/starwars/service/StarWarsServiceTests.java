package com.example.starwars.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

class StarWarsServiceTests {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private StarWarService starWarService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEntityDetails_Success() {
        // Arrange
        String type = "people";
        String name = "Luke Skywalker";
        String expectedResponse = "{\"name\": \"Luke Skywalker\"}";
        
        when(restTemplate.getForObject(anyString(), eq(String.class)))
            .thenReturn(expectedResponse);

        // Act
        String response = starWarService.getEntityDetails(type, name);

        // Assert
        assertEquals(expectedResponse, response);
        verify(restTemplate, times(1)).getForObject(anyString(), eq(String.class));
    }


	@Test
    public void testGetEntityNotFound() {
        when(restTemplate.getForObject("https://swapi.dev/api/vehicles/?search=Nonexistent", String.class))
            .thenReturn("{\"results\":[]}");

        assertThrows(RuntimeException.class, () -> starWarService.getEntityDetails("vehicles", "Nonexistent"));
    }

    @Test
    void testGetEntityDetails_InvalidType() {
        // Act & Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            starWarService.getEntityDetails("invalidType", "Some Name");
        });

        assertEquals("Invalid type provided: invalidType", thrown.getMessage());
    }

    @Test
    void testFallbackGetEntityDetails() {
        // Arrange
        String type = "people";
        String name = "Some Name";
        Throwable throwable = new RuntimeException("Simulated exception");

        // Act
        String response = starWarService.fallbackGetEntityDetails(type, name, throwable);

        // Assert
        assertEquals("{\"error\": \"Unable to fetch data from the external API. Please try again later.\"}", response);
    }
}

