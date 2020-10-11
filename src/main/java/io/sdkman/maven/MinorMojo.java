package io.sdkman.maven;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.sdkman.maven.infra.ApiEndpoints.ANNOUNCE_ENDPOINT;
import static io.sdkman.maven.infra.ApiEndpoints.RELEASE_ENDPOINT;

/**
 * Release and announce.
 *
 * @author Andres Almiray
 */
@Mojo(name = "minor-release")
public class MinorMojo extends BaseMojo {

  @Parameter(property = "sdkman.hashtag", required = true)
  protected String hashtag;

  @Parameter(property = "sdkman.url")
  protected String url;

  @Parameter(property = "sdkman.platforms")
  protected Map<String, String> platforms;

  @Override
  protected HttpEntityEnclosingRequestBase createHttpRequest() {
    try {
      return new HttpPost(new URI("https", apiHost, RELEASE_ENDPOINT, null));
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  protected void doExecute() throws MojoExecutionException {
    try {
      HttpResponse resp = executeMajorRelease();
      int statusCode = resp.getStatusLine().getStatusCode();
      if (statusCode < 200 || statusCode >= 300) {
        throw new IllegalStateException("Server returned error " + resp.getStatusLine());
      }
    } catch (Exception e) {
      throw new MojoExecutionException("Sdk minor release failed", e);
    }
  }

  protected HttpResponse executeMajorRelease() throws IOException {
    List<HttpResponse> responses = new ArrayList<>();

    if (platforms == null || platforms.isEmpty()) {
       responses.add(execCall(getReleasePayload(), createHttpRequest()));
    } else {
      for (Map.Entry<String, String> platform : platforms.entrySet()) {
        Map<String, String> payload = super.getPayload();
        payload.put("platform", platform.getKey());
        payload.put("url", platform.getValue());
        responses.add(execCall(payload, createHttpRequest()));
      }
    }

    responses.add(execCall(getAnnouncePayload(), createAnnounceHttpRequest()));

    return responses.stream()
        .filter(resp -> {
          int statusCode = resp.getStatusLine().getStatusCode();
          return statusCode < 200 || statusCode >= 300;
        })
        .findFirst()
        .orElse(responses.get(responses.size() - 1));
  }

  protected Map<String, String> getAnnouncePayload() {
    Map<String, String> payload = super.getPayload();
    payload.put("hashtag", hashtag);
    return payload;
  }

  protected Map<String, String> getReleasePayload() {
    Map<String, String> payload = super.getPayload();
    payload.put("url", url);
    return payload;
  }

  protected HttpEntityEnclosingRequestBase createAnnounceHttpRequest() {
    try {
      return new HttpPost(new URI("https", apiHost, ANNOUNCE_ENDPOINT, null));
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
