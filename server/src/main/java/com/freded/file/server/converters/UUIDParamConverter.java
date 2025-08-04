package com.freded.file.server.converters;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.Provider;
import java.util.UUID;

@Provider
public class UUIDParamConverter implements ParamConverter<UUID> {
  @Override
  public UUID fromString(String value) {
    try {
      return UUID.fromString(value);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Invalid UUID format: " + value);
    }
  }

  @Override
  public String toString(UUID value) {
    return value.toString();
  }
}
