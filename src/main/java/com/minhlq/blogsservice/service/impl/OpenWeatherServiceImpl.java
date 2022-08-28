package com.minhlq.blogsservice.service.impl;

import static com.minhlq.blogsservice.constant.OpenWeatherConstants.CURRENT_WEATHER_DATA;

import com.minhlq.blogsservice.constant.CacheConstants;
import com.minhlq.blogsservice.dto.OpenWeatherDTO;
import com.minhlq.blogsservice.enums.WeatherMeasurementUnits;
import com.minhlq.blogsservice.service.OpenWeatherService;
import com.minhlq.blogsservice.util.HttpClientHelper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * This is implement for the open weather api service operations.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class OpenWeatherServiceImpl implements OpenWeatherService {

  @Value("${application.open-weather.base-url}")
  private String baseUrl;

  @Value("${application.open-weather.api-key}")
  private String apiKey;

  private final HttpClientHelper clientHelper;

  @Override
  @Cacheable(cacheNames = CacheConstants.CURRENT_WEATHER)
  public OpenWeatherDTO getCurrentWeather(
      String latitude, String longitude, WeatherMeasurementUnits units, String language) {
    String url = baseUrl + CURRENT_WEATHER_DATA;
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromUriString(url)
            .queryParam("lat", latitude)
            .queryParam("lon", longitude)
            .queryParamIfPresent("units", Optional.of(units.getCode()))
            .queryParamIfPresent("lang", Optional.of(language))
            .queryParam("appid", apiKey);

    return clientHelper.getForValue(builder.toUriString(), OpenWeatherDTO.class);
  }
}
