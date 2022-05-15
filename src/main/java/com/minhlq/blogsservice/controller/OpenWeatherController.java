package com.minhlq.blogsservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhlq.blogsservice.dto.OpenWeatherDto;
import com.minhlq.blogsservice.enumdef.WeatherMeasurementUnitsEnum;
import com.minhlq.blogsservice.service.OpenWeatherService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weathers")
@RequiredArgsConstructor
public class OpenWeatherController {

  private final OpenWeatherService openWeatherService;

  private final StringRedisTemplate redisTemplate;

  private final ObjectMapper mapper;

  @GetMapping("/current")
  public OpenWeatherDto getCurrentWeather(
      @RequestParam(value = "lat") String latitude,
      @RequestParam(value = "long") String longitude,
      @RequestParam(value = "units", required = false, defaultValue = "IMPERIAL")
          WeatherMeasurementUnitsEnum units,
      @RequestParam(value = "lang", required = false, defaultValue = "en") String language)
      throws JsonProcessingException {
    return openWeatherService.getCurrentWeather(latitude, longitude, units, language);
  }

  @GetMapping("/current-from-cache")
  public OpenWeatherDto getCurrentFromCache() throws JsonProcessingException {
    String value = redisTemplate.opsForValue().get("current-weather");
    if (StringUtils.isNotBlank(value)) {
      return mapper.readValue(value, OpenWeatherDto.class);
    }

    return null;
  }
}
