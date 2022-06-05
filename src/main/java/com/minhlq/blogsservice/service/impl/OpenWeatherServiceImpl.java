package com.minhlq.blogsservice.service.impl;

import static com.minhlq.blogsservice.constant.OpenWeatherConstants.CURRENT_WEATHER_DATA;

import com.minhlq.blogsservice.constant.CacheConstants;
import com.minhlq.blogsservice.dto.OpenWeatherDto;
import com.minhlq.blogsservice.enumdef.WeatherMeasurementUnits;
import com.minhlq.blogsservice.service.OpenWeatherService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Log4j2
@Service
@RequiredArgsConstructor
public class OpenWeatherServiceImpl implements OpenWeatherService {

  @Value("${app.open-weather.base-url}")
  private String baseUrl;

  @Value("${app.open-weather.api-key}")
  private String apiKey;

  private final RestTemplate restTemplate;

  @Override
  @Cacheable(cacheNames = CacheConstants.CURRENT_WEATHER)
  public OpenWeatherDto getCurrentWeather(
      String latitude, String longitude, WeatherMeasurementUnits units, String language) {
    String url = baseUrl + CURRENT_WEATHER_DATA;
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromUriString(url)
            .queryParam("lat", latitude)
            .queryParam("lon", longitude)
            .queryParamIfPresent("units", Optional.of(units.getCode()))
            .queryParamIfPresent("lang", Optional.of(language))
            .queryParam("appid", apiKey);

    return restTemplate.getForObject(builder.toUriString(), OpenWeatherDto.class);
  }
}
