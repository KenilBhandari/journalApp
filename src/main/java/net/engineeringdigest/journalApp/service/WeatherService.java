package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.cache.AppCache;
import net.engineeringdigest.journalApp.entity.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class WeatherService {

    private static final String API_URL = "https://api.weatherstack.com/current?access_key=API_KEY&query=CITY";
    @Value("${weather.api.key}")
    private String apiKey;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    @Autowired
    private RedisService redisService;

    public WeatherResponse getWeather() {
        String apiURL = appCache.APP_CACHE.get("weather_api").replace("<apiKey>", apiKey).replace("<city>", "Vadodara");
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(apiURL, HttpMethod.GET, null, WeatherResponse.class);

        WeatherResponse body = response.getBody();
        return body;
    }

    public WeatherResponse getCityWeather(String city) {
        WeatherResponse weatherCachedResponse = redisService.get("weather_of_" + city, WeatherResponse.class);
        if (weatherCachedResponse != null) {
            return weatherCachedResponse;
        } else {
            String apiURL = API_URL.replace("API_KEY", apiKey).replace("CITY", city);
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(apiURL, HttpMethod.GET, null, WeatherResponse.class);
            WeatherResponse body = response.getBody();
            if (body != null) {
                redisService.set("weather_of_" + city, body, 300l);
            }
            return body;
        }
    }

}
