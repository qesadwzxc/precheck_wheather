package com.epam.weather.service.impl;

import com.epam.weather.entity.WeatherInfoResp;
import com.epam.weather.service.WeatherComService;
import com.epam.weather.util.HttpUtils;
import okhttp3.OkHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.method;

@RunWith(SpringRunner.class)
public class WeatherComServiceImplTest {

    @InjectMocks
    WeatherComServiceImpl weatherComService;

    @Mock
    HttpUtils httpUtils;

    @Test
    public void getProvinceList() {
    }

    @Test
    public void getCityList() {
    }

    @Test
    public void getCountryList() {
    }

    @Test
    public void getWeatherInfo() throws Exception {
        String resultStr = "{\"weatherinfo\":{\"city\":\"苏州\",\"cityid\":\"101190401\",\"temp\":\"23.9\",\"WD\":\"东北风\",\"WS\":\"小于3级\",\"SD\":\"79%\",\"AP\":\"1004.9hPa\",\"njd\":\"暂无实况\",\"WSE\":\"<3\",\"time\":\"18:00\",\"sm\":\"1.5\",\"isRadar\":\"0\",\"Radar\":\"\"}}";
        when(httpUtils.url(any())).thenReturn(httpUtils);
        when(httpUtils.sync()).thenReturn(resultStr);
        WeatherInfoResp resp = weatherComService.getWeatherInfo("101190401");
        assertEquals("苏州", resp.getWeatherInfo().getCity());

        resultStr = "";
        when(httpUtils.sync()).thenReturn(resultStr);
        WeatherInfoResp resp2 = weatherComService.getWeatherInfo("10119040");
        assertEquals(null, resp2);
    }

}
