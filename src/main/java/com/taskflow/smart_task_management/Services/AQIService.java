package com.taskflow.smart_task_management.Services;

import com.taskflow.smart_task_management.Cache.AppCache;
import com.taskflow.smart_task_management.Constants.ApiConstants;
import com.taskflow.smart_task_management.DTO.AQIResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AQIService {

    @Value("${apininja.api.key}")
    private String API_KEY;

    @Autowired
    AppCache appCache;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    RedisService redisService;




    public AQIResponse getAQI(String city){
        AQIResponse aqiResponse = redisService.get("AQI of " + city, AQIResponse.class);
        if(aqiResponse != null){
            return aqiResponse;
        }else{
            HttpHeaders headers = new HttpHeaders();
            headers.set(ApiConstants.API_NINJA_HEADER, API_KEY);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<AQIResponse> exchange = restTemplate.exchange(redisService.get(ApiConstants.AQI_API), HttpMethod.GET, entity, AQIResponse.class,city);
            AQIResponse body = exchange.getBody();
            if(body != null){
                redisService.set("AQI of "+city,body,300);
            }
            return body;

        }

    }
}
