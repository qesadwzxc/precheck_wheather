package com.epam.weather.service;

import com.alibaba.fastjson.JSONObject;
import com.epam.weather.entity.WeatherInfoResp;

import java.util.Map;

/**
 * @author Lee
 * service from weather.com
 */
public interface WeatherComService {
    /**
     * Get province list for china
     * @return
     */
    Map<String, String> getProvinceList();

    /**
     * Get city list for specific province
     * @param provinceId
     * @return
     */
    Map<String, String> getCityList(String provinceId);

    /**
     * Get country list for specific city
     * @param cityId
     * @return
     */
    Map<String, String> getCountryList(String cityId);

    /**
     * Get weather info for specific country
     * @param countryId
     * @return
     */
    WeatherInfoResp getWeatherInfo(String countryId);
}
