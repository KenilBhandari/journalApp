package net.engineeringdigest.journalApp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherResponse{

    private  Current current;

    @Getter
    @Setter
    public class Current {
        @JsonProperty("observation_time")
        private String observationTime;
        private int temperature;
        private int weather_code;
        private int wind_speed;
        private int wind_degree;
        private String wind_dir;
        private int feelslike;
        private int uv_index;

    }


}
