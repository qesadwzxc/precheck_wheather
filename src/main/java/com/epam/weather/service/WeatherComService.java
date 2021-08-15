package com.epam.weather.service;

import com.alibaba.fastjson.JSONObject;
import com.epam.weather.entity.WeatherInfoResp;

/**
 * @author Lee
 * service from weather.com
 */
public interface WeatherComService {
    /**
     * Get province list for china
     * @return
     */
    public JSONObject getProvinceList();

    /**
     * Get city list for specific province
     * @param provinceId
     * @return
     */
    public JSONObject getCityList(String provinceId);

    /**
     * Get country list for specific city
     * @param cityId
     * @return
     */
    public JSONObject getCountryList(String cityId);

    /**
     * Get weather info for specific country
     * @param countryId
     * @return
     */
    public WeatherInfoResp getWeatherInfo(String countryId);
}
