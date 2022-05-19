package com.minhlq.blogsservice.enumdef;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WeatherMeasurementUnits {
  STANDARD("standard"),
  METRIC("metric"),
  IMPERIAL("imperial");

  private final String code;
}
