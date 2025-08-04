package com.freded.file.server.converters;

import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import jakarta.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.UUID;

@Provider
public class UUIDParamConverterProvider implements ParamConverterProvider {
  private static final ParamConverter<UUID> UUID_CONVERTER = new UUIDParamConverter();

  @Override
  public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
    if (UUID.class.equals(rawType)) {
      return (ParamConverter<T>) UUID_CONVERTER;
    }
    return null;
  }
}
