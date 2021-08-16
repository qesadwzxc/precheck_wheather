package com.epam.weather.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author Lee
 * http request util
 */
@Component
public class HttpUtils {
    private OkHttpClient okHttpClient = null;
    private Map<String, String> headerMap;
    private Map<String, String> paramMap;
    private String url;
    private Request.Builder request;
    public static final String EXCEPTION_MSG = "occur Exception;";
    private static final Integer RETRY_TIMES = 3;

    private HttpUtils() {
                okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS)
                        .hostnameVerifier((hostName, session) -> true)
                        .retryOnConnectionFailure(true)
                        .build();
                addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
    }

    /**
     * set request url
     *
     * @param url
     * @return
     */
    public HttpUtils url(String url) {
        this.url = url;
        return this;
    }

    /**
     * add request param
     *
     * @param key param key
     * @param value param value
     * @return
     */
    public HttpUtils addParam(String key, String value) {
        if (paramMap == null) {
            paramMap = new LinkedHashMap<>(16);
        }
        paramMap.put(key, value);
        return this;
    }

    /**
     * add request header
     *
     * @param key header key
     * @param value header value
     * @return
     */
    public HttpUtils addHeader(String key, String value) {
        if (headerMap == null) {
            headerMap = new LinkedHashMap<>(16);
        }
        headerMap.put(key, value);
        return this;
    }

    /**
     * init get method
     *
     * @return
     */
    public HttpUtils get() {
        request = new Request.Builder().get();
        StringBuilder urlBuilder = new StringBuilder(url);
        if (paramMap != null) {
            urlBuilder.append("?");
            try {
                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    urlBuilder.append(URLEncoder.encode(entry.getKey(), "utf-8")).
                            append("=").
                            append(URLEncoder.encode(entry.getValue(), "utf-8")).
                            append("&");
                }
            } catch (Exception e) {
                //log exception
            }
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        request.url(urlBuilder.toString());
        return this;
    }

    /**
     * sync method, default retry 3 times when http code 5xx
     * @return
     */
    public String sync(){
        String result = "";
        if(request == null){
            get();
        }
        Integer retryTimes = RETRY_TIMES;
        do{
            result = syncWithNoRetry();
            if(!StringUtils.isEmpty(result)){
                return result;
            }
        } while (retryTimes-->0);
        return result;
    }

    /**
     * sync method
     * @return
     */
    public String syncWithNoRetry() {
        StringBuilder buffer = new StringBuilder();
        setHeader(request);
        Response response = null;
        try {
            response = getResponse();
            //log response here

            //http code 2xx
            if(response.code()<HttpStatus.MULTIPLE_CHOICES.value()){
                buffer.append(response.body().string());
            }
            //http code 3xx and 4xx
            else if (response.code()<HttpStatus.INTERNAL_SERVER_ERROR.value()){
                buffer.append(EXCEPTION_MSG);
            }
            //http code 5xx
            else {
                //can do sth.
            }
            return buffer.toString();
        } catch (IOException e) {
            return buffer.toString();
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public Response getResponse() throws IOException {
        return okHttpClient.newCall(request.build()).execute();
    }

    private void setHeader(Request.Builder request) {
        if (headerMap != null) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }
}