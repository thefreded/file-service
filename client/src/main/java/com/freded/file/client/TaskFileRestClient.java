package com.freded.file.client;

import com.freded.auth.DynamicAuthHeadersFactory;
import com.freded.file.client.boundary.TaskFile;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "file-api")
@RegisterClientHeaders(DynamicAuthHeadersFactory.class)
public interface TaskFileRestClient extends TaskFile {
}
