package com.epam.weather.service.impl;

import com.epam.weather.entity.WeatherInfoResp;
import com.epam.weather.service.WeatherComService;
import com.epam.weather.service.WeatherService;
import com.epam.weather.util.HttpUtils;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lee
 */
@Component
public class WeatherServiceImpl implements WeatherService {
    @Autowired
    WeatherComService weatherComService;

    private final String NUMBER_REGEX = "^(\\-|\\+)?\\d+(\\.\\d+)?$";

    private volatile static Semaphore semaphore;

    public static Semaphore getSemaphoreInstance() {
        if (semaphore == null) {
            synchronized (WeatherServiceImpl.class) {
                if (semaphore == null) {
                    semaphore = new Semaphore(100);
                }
            }
        }
        return semaphore;
    }

    @Override
    public Optional<Integer> getTemperature(String Province, String city, String County) {
        Optional<Integer> resultTemp = Optional.empty();
        if(!getSemaphoreInstance().tryAcquire()){
            return resultTemp;
        }
        try {
            if (StringUtils.isEmpty(Province)) {
                return resultTemp;
            }
            if (StringUtils.isEmpty(city)) {
                return resultTemp;
            }
            if (StringUtils.isEmpty(County)) {
                return resultTemp;
            }

            StringBuilder countryId = new StringBuilder().append(Province).append(city).append(County);
            WeatherInfoResp weatherInfoResp = weatherComService.getWeatherInfo(countryId.toString());
            if (weatherInfoResp != null && weatherInfoResp.getWeatherInfo() != null
                    && !StringUtils.isEmpty(weatherInfoResp.getWeatherInfo().getTemp())) {
                Pattern pattern = Pattern.compile(NUMBER_REGEX);
                Matcher isNum = pattern.matcher(weatherInfoResp.getWeatherInfo().getTemp());
                if(isNum.matches()) {
                    resultTemp = Optional.of(new BigDecimal(weatherInfoResp.getWeatherInfo().getTemp()).intValue());
                }
            }
            return resultTemp;
        } finally {
            getSemaphoreInstance().release();
        }
    }
}
