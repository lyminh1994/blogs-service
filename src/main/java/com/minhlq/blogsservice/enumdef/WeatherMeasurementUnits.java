package com.minhlq.blogsservice.enumdef;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * WeatherMeasurementUnits defines the unit supported in the weather api request.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Getter
@RequiredArgsConstructor
public enum WeatherMeasurementUnits {
  STANDARD("standard"),
  METRIC("metric"),
  IMPERIAL("imperial");

  private final String code;
}
