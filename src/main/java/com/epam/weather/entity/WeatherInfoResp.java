package com.epam.weather.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Lee
 * Weather info response entity
 */
@Data
public class WeatherInfoResp {
    @JsonProperty("weatherinfo")
    WeatherInfoBody weatherInfo;

    @Data
    public static class WeatherInfoBody {
        String city;
        @JsonProperty("cityid")
        String cityId;
        String temp;
        @JsonProperty("WD")
        String wd;
        @JsonProperty("WS")
        String ws;
        @JsonProperty("WSE")
        String wse;
        @JsonProperty("SD")
        String sd;
        String isRadar;
        @JsonProperty("Radar")
        String radar;
        String njd;
        String sm;
        String time;
        @JsonProperty("AP")
        String ap;
    }
}
