package com.freded.file.server.controller;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import org.jboss.logging.Logger;

@ApplicationScoped
public class MinioUploadService {

  private static final Logger LOG = Logger.getLogger(MinioUploadService.class);

  @Inject MinioClient minioClient;
  @Inject com.freded.file.server.config.MinioConfig minioConfig;

  // TODO: based on error, tell if file exist or not
  public void uploadFile(String objectName, InputStream inputStream, long size, String contentType) throws Exception {
    minioClient.putObject(
        PutObjectArgs.builder().bucket(minioConfig.getBucketName()).object(objectName).stream(inputStream, size, -1)
            .contentType(contentType)
            .build());
    LOG.info("Uploaded file to MinIO: " + objectName);
  }

  /**
   * Get a temporary URL for downloading a file from MinIO
   *
   * @param objectName the object name in MinIO
   * @param expiryHours expiry time in hours (default 1 hour if not specified)
   * @return temporary URL string
   */
  public String getTempUrl(String objectName, int expiryHours) throws Exception {
    String url =
        minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(minioConfig.getBucketName())
                .object(objectName)
                .expiry(expiryHours, TimeUnit.HOURS)
                .build());

    LOG.info("Generated temporary URL for object: " + objectName + " (expires in " + expiryHours + " hours)");
    return url;
  }

  /**
   * Get a temporary URL for downloading a file from MinIO with default 1 hour expiry
   *
   * @param objectName the object name in MinIO
   * @return temporary URL string
   */
  public String getTempUrl(String objectName) throws Exception {
    return getTempUrl(objectName, 1);
  }
}
