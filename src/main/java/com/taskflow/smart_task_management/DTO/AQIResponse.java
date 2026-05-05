package com.taskflow.smart_task_management.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AQIResponse {
    @JsonProperty("overall_aqi")
    private int aqi;
}
