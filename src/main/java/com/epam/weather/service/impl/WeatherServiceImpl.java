package com.epam.weather.service.impl;

import com.epam.weather.service.WeatherService;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @author Lee
 */
public class WeatherServiceImpl implements WeatherService {
    @Override
    public Optional<Integer> getTemperature(String Province, String city, String County) {
        return Optional.empty();
    }

    @Override
    public Optional<BigDecimal> getTemperature2(String province, String city, String country) {
        return Optional.empty();
    }
}
