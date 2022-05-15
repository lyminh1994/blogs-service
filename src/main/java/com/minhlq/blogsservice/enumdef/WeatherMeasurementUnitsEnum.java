package com.minhlq.blogsservice.enumdef;

import lombok.Getter;

@Getter
public enum WeatherMeasurementUnitsEnum {
  STANDARD("standard"),
  METRIC("metric"),
  IMPERIAL("imperial");

  private final String code;

  WeatherMeasurementUnitsEnum(String code) {
    this.code = code;
  }
}
