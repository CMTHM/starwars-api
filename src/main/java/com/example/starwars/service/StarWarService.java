package com.example.starwars.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.starwars.entity.UserInfo;
import com.example.starwars.exception.ResourceNotFoundException;
import com.example.starwars.repository.UserInfoRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class StarWarService {
    private static final Logger logger = LogManager.getLogger(StarWarService.class);
    private final RestTemplate restTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private UserInfoRepository repository; 
    
    public StarWarService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    public String addUser(UserInfo userInfo) {
        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        repository.save(userInfo);
        return "user added to system ";
    }    

    @Cacheable(value = "starWarsCache", key = "#type + '_' + #name")
    @CircuitBreaker(name = "starWarsCircuitBreaker", fallbackMethod = "fallbackGetEntityDetails")
    public String getEntityDetails(String type, String name) {
        if (!isValidType(type)) {
            throw new IllegalArgumentException("Invalid type provided: " + type);
        }
        try {
            logger.info("Accessing swapi dev endpoints for type: {} and name: {}", type, name);
            String url = UriComponentsBuilder.fromHttpUrl("https://swapi.dev/api/")
                    .pathSegment(type.toLowerCase())
                    .queryParam("search", name)
                    .toUriString();
            String response = restTemplate.getForObject(url, String.class);
            if (response == null || response.contains("Not Found")) {
                throw new ResourceNotFoundException("The " + type + " named " + name + " was not found.");
            }
            return response;
        } catch (HttpClientErrorException e) {
            throw new ResourceNotFoundException("The " + type + " named " + name + " was not found.");
        } catch (Exception e) {
            throw new ServiceException("Error occurred while fetching data from the Star Wars API", e);
        }
    }

    public String fallbackGetEntityDetails(String type, String name, Throwable throwable) {
        logger.error("Fallback method triggered for type: {} and name: {} due to: {}", type, name, throwable.getMessage());
        return "{\"error\": \"Unable to fetch data from the external API. Please try again later.\"}";
    }

    private boolean isValidType(String type) {
        return type.equalsIgnoreCase("planets") ||
               type.equalsIgnoreCase("starships") ||
               type.equalsIgnoreCase("vehicles") ||
               type.equalsIgnoreCase("people") ||
               type.equalsIgnoreCase("films") ||
               type.equalsIgnoreCase("species");
    }
}
