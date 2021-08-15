package com.epam.weather.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.epam.weather.entity.WeatherInfoResp;
import com.epam.weather.service.WeatherComService;

/**
 * @author Lee
 */
public class WeatherComServiceImpl implements WeatherComService {
    @Override
    public JSONObject getProvinceList() {
        return null;
    }

    @Override
    public JSONObject getCityList(String provinceId) {
        return null;
    }

    @Override
    public JSONObject getCountryList(String cityId) {
        return null;
    }

    @Override
    public WeatherInfoResp getWeatherInfo(String countryId) {
        return null;
    }
}
