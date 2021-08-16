package com.epam.weather.service.impl;

import com.epam.weather.entity.WeatherInfoResp;
import com.epam.weather.service.WeatherComService;
import com.epam.weather.service.WeatherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;
import java.util.concurrent.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({WeatherServiceImpl.class})
public class WeatherServiceImplTest {
    @InjectMocks
    WeatherServiceImpl weatherService;

    @Mock
    WeatherComService weatherComService;

    @Test
    public void getTemperatureExecuteLimit() throws ExecutionException, InterruptedException {
        PowerMockito.mockStatic(WeatherServiceImpl.class);
        PowerMockito.when(WeatherServiceImpl.getSemaphoreInstance()).thenReturn(new Semaphore(1));
        WeatherInfoResp resp = new WeatherInfoResp();
        resp.setWeatherInfo(new WeatherInfoResp.WeatherInfoBody());
        resp.getWeatherInfo().setTemp("10");
        when(weatherComService.getWeatherInfo(any())).thenReturn(resp);

        WeatherThread weatherThread = new WeatherThread();
        FutureTask futureTask1 = new FutureTask(weatherThread);
        FutureTask futureTask2 = new FutureTask(weatherThread);
        Thread thread1 = new Thread(futureTask1);
        Thread thread2 = new Thread(futureTask2);
        thread1.start();
        thread2.start();

        Object o1 = futureTask1.get();
        Object o2 = futureTask2.get();
        assertNotEquals(o1, o2);
    }

    @Test
    public void getTemperature(){
        WeatherInfoResp resp = null;
        when(weatherComService.getWeatherInfo(any())).thenReturn(resp);
        assertEquals(Optional.empty(), weatherService.getTemperature("1", "1", "1"));

        resp = new WeatherInfoResp();
        resp.setWeatherInfo(new WeatherInfoResp.WeatherInfoBody());
        resp.getWeatherInfo().setTemp("10");
        when(weatherComService.getWeatherInfo(any())).thenReturn(resp);
        assertEquals(Optional.of(10), weatherService.getTemperature("1", "1", "1"));
        resp.getWeatherInfo().setTemp("-1");
        assertEquals(Optional.of(-1), weatherService.getTemperature("1", "1", "1"));
        resp.getWeatherInfo().setTemp("11.1");
        assertEquals(Optional.of(11), weatherService.getTemperature("1", "1", "1"));
        resp.getWeatherInfo().setTemp("a");
        assertEquals(Optional.empty(), weatherService.getTemperature("1", "1", "1"));
        resp.getWeatherInfo().setTemp("0.1");
        assertEquals(Optional.of(0), weatherService.getTemperature("1", "1", "1"));
        resp.getWeatherInfo().setTemp("0");
        assertEquals(Optional.of(0), weatherService.getTemperature("1", "1", "1"));

        assertEquals(Optional.empty(), weatherService.getTemperature("", "1", "1"));
        assertEquals(Optional.empty(), weatherService.getTemperature(null, "1", "1"));
        assertEquals(Optional.empty(), weatherService.getTemperature("1", "", "1"));
        assertEquals(Optional.empty(), weatherService.getTemperature("1",  null, "1"));
        assertEquals(Optional.empty(), weatherService.getTemperature("1",  "1", ""));
        assertEquals(Optional.empty(), weatherService.getTemperature("1",  "1", null));
    }

    class WeatherThread implements Callable {
        @Override
        public Object call() throws Exception {
            return weatherService.getTemperature("1", "1", "1");
        }
    }
}
