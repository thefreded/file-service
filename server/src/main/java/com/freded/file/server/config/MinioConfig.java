package com.freded.file.server.config;

import io.minio.MinioClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import lombok.Getter;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class MinioConfig {

  @ConfigProperty(name = "minio.url")
  String minioUrl;

  @ConfigProperty(name = "minio.access.key")
  String accessKey;

  @ConfigProperty(name = "minio.secret.key")
  String secretKey;

  @Getter
  @ConfigProperty(name = "minio.bucket.name", defaultValue = "task-files")
  String bucketName;

  @Produces
  @ApplicationScoped
  public MinioClient minioClient() {
    return MinioClient.builder().endpoint(minioUrl).credentials(accessKey, secretKey).build();
  }
}
