package net.engineeringdigest.journalApp.controller;


import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.entity.WeatherResponse;
import net.engineeringdigest.journalApp.repository.UserRepository;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")


public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeatherService weatherService;


    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User updatedUser) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User userInDb = userService.findByUsername(username);
        if (userInDb != null) {
            userInDb.setUsername(updatedUser.getUsername());
            userInDb.setPassword(updatedUser.getPassword());
            userService.saveNewUser(userInDb);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @DeleteMapping
    public ResponseEntity<?> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        userRepository.deleteByUsername(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> greeting() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather();
        String weatherInfo = "";
        if (weatherResponse != null) {
            weatherInfo = "Weather Feels like " + weatherResponse.getCurrent().getFeelslike() + " and tempearture is " + weatherResponse.getCurrent().getTemperature();
        }
        return new ResponseEntity<>("Hi " + authentication.getName() + weatherInfo, HttpStatus.OK);
    }

    @PostMapping("/city")
    public ResponseEntity<?> checkWeather(@RequestBody Map<String, String> city){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String cityName = city.get("city");
        WeatherResponse currentWeather = weatherService.getCityWeather(cityName);
        String weatherInfo = "";
        if (currentWeather != null) {
            weatherInfo = "Weather Feels like " + currentWeather.getCurrent().getFeelslike() + " and tempearture is " + currentWeather.getCurrent().getTemperature();
        }
        return new ResponseEntity<>("Hi " + authentication.getName() + weatherInfo, HttpStatus.OK);

    }
}
