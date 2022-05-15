package com.minhlq.blogsservice.service.impl;

import static com.minhlq.blogsservice.constant.OpenWeatherConstants.CURRENT_WEATHER_DATA;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhlq.blogsservice.dto.OpenWeatherDto;
import com.minhlq.blogsservice.enumdef.WeatherMeasurementUnitsEnum;
import com.minhlq.blogsservice.properties.OpenWeatherProperties;
import com.minhlq.blogsservice.service.OpenWeatherService;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Log4j2
@Service
@RequiredArgsConstructor
public class OpenWeatherServiceImpl implements OpenWeatherService {

  private final OpenWeatherProperties openWeatherProperties;

  private final RestTemplate restTemplate;

  private final StringRedisTemplate redisTemplate;

  private final ObjectMapper mapper;

  @Cacheable(cacheNames = "current-weather-cache")
  @Override
  public OpenWeatherDto getCurrentWeather(
      String latitude, String longitude, WeatherMeasurementUnitsEnum units, String language)
      throws JsonProcessingException {
    String url = openWeatherProperties.getBaseUrl() + CURRENT_WEATHER_DATA;
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromUriString(url)
            .queryParam("lat", latitude)
            .queryParam("lon", longitude)
            .queryParamIfPresent("units", Optional.of(units.getCode()))
            .queryParamIfPresent("lang", Optional.of(language))
            .queryParam("appid", openWeatherProperties.getApiKey());

    OpenWeatherDto response =
        restTemplate.getForObject(builder.toUriString(), OpenWeatherDto.class);
    if (response != null) {
      redisTemplate.opsForValue().set("current-weather", mapper.writeValueAsString(response));
      redisTemplate.expire("current-weather", 60000, TimeUnit.MILLISECONDS);
    }

    return response;
  }
}
