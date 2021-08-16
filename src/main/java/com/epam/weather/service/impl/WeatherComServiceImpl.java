package com.epam.weather.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.epam.weather.entity.WeatherInfoResp;
import com.epam.weather.service.WeatherComService;
import com.epam.weather.util.HttpUtils;
import com.epam.weather.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author Lee
 */
@Component
public class WeatherComServiceImpl implements WeatherComService {
    @Value("${weather-com.hosts}")
    String weatherComHosts;

    @Value("${weather-com.province-interface}")
    String weatherComProvinceInterface;

    @Value("${weather-com.city-interface}")
    String weatherComCityInterface;

    @Value("${weather-com.country-interface}")
    String weatherComCountryInterface;

    @Value("${weather-com.weather-interface}")
    String weatherComWeatherInterface;

    final String htmlEnd = ".html";

    @Autowired
    HttpUtils httpUtils;

    @Override
    public Map<String, String> getProvinceList() {
        StringBuilder path = new StringBuilder().append(weatherComHosts).append(weatherComProvinceInterface);
        String resultStr = getHttpResult(path.toString());
        return isSuccessRequest(resultStr) ? JsonUtils.toJavaObject(resultStr, Map.class) : null;
    }

    @Override
    public Map<String, String> getCityList(final String provinceId) {
        StringBuilder path = new StringBuilder().append(weatherComHosts).append(weatherComCityInterface).append(provinceId).append(htmlEnd);
        String resultStr = getHttpResult(path.toString());
        return isSuccessRequest(resultStr) ? JsonUtils.toJavaObject(resultStr, Map.class) : null;
    }

    @Override
    public Map<String, String> getCountryList(final String cityId) {
        StringBuilder path = new StringBuilder().append(weatherComHosts).append(weatherComCountryInterface).append(cityId).append(htmlEnd);
        String resultStr = getHttpResult(path.toString());
        return isSuccessRequest(resultStr) ? JsonUtils.toJavaObject(resultStr, Map.class) : null;
    }

    @Override
    public WeatherInfoResp getWeatherInfo(final String countryId) {
        StringBuilder path = new StringBuilder().append(weatherComHosts).append(weatherComWeatherInterface).append(countryId).append(htmlEnd);
        String resultStr = getHttpResult(path.toString());
        return isSuccessRequest(resultStr) ? JsonUtils.toJavaObject(resultStr, WeatherInfoResp.class) : null;
    }

    private Boolean isSuccessRequest(String resultStr){
        return !StringUtils.isEmpty(resultStr) && !resultStr.equals(HttpUtils.EXCEPTION_MSG);
    }

    private String getHttpResult(String path){
        return httpUtils.url(path).sync();
    }
}
