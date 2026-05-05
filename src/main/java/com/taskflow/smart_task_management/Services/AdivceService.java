package com.taskflow.smart_task_management.Services;

import com.taskflow.smart_task_management.Cache.AppCache;
import com.taskflow.smart_task_management.Constants.ApiConstants;
import com.taskflow.smart_task_management.DTO.AdviceResponse;
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
public class AdivceService {
    @Value("${apininja.api.key}")
    private String API_KEY;


    @Autowired
    AppCache appCache;
    @Autowired
    RedisService redisService;

    @Autowired
    private RestTemplate restTemplate;


    public AdviceResponse getAdviceResponse(){

        HttpHeaders headers = new HttpHeaders();
        headers.set(ApiConstants.API_NINJA_HEADER, API_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<AdviceResponse> exchange = restTemplate.exchange(redisService.get(ApiConstants.ADVICE_API), HttpMethod.GET, entity, AdviceResponse.class);
        return exchange.getBody();

    }

}
