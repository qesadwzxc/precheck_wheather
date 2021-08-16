package com.epam.weather.service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Lee
 * Weather service
 */
public interface WeatherService {
    /**
     * Get temperature for specific country
     * @param Province province code(eg. 10119)
     * @param city city code(eg. 04)
     * @param County country code(eg. 01)
     * @return temperature integer value or null
     */
    Optional<Integer> getTemperature(String Province, String city, String County);
}
