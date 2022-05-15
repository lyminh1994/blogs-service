package com.minhlq.blogsservice.service.impl;

import static com.minhlq.blogsservice.constant.OpenWeatherConstants.CURRENT_WEATHER_DATA;

import com.minhlq.blogsservice.dto.OpenWeatherDto;
import com.minhlq.blogsservice.enumdef.WeatherMeasurementUnitsEnum;
import com.minhlq.blogsservice.properties.OpenWeatherProperties;
import com.minhlq.blogsservice.service.OpenWeatherService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Log4j2
@Service
@RequiredArgsConstructor
public class OpenWeatherServiceImpl implements OpenWeatherService {

  private final OpenWeatherProperties openWeatherProperties;

  private final RestTemplate restTemplate;

  @Override
  public OpenWeatherDto getCurrentWeather(
      String latitude, String longitude, WeatherMeasurementUnitsEnum units, String language) {
    String url = openWeatherProperties.getBaseUrl() + CURRENT_WEATHER_DATA;
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromUriString(url)
            .queryParam("lat", latitude)
            .queryParam("lon", longitude)
            .queryParamIfPresent("units", Optional.of(units.getCode()))
            .queryParamIfPresent("lang", Optional.of(language))
            .queryParam("appid", openWeatherProperties.getApiKey());

    return restTemplate.getForObject(builder.toUriString(), OpenWeatherDto.class);
  }
}
