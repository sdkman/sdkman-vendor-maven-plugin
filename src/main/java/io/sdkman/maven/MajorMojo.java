package io.sdkman.maven;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.sdkman.maven.infra.ApiEndpoints.ANNOUNCE_ENDPOINT;
import static io.sdkman.maven.infra.ApiEndpoints.DEFAULT_ENDPOINT;
import static io.sdkman.maven.infra.ApiEndpoints.RELEASE_ENDPOINT;

/**
 * Release, announce, and make default.
 *
 * @author Andres Almiray
 */
@Mojo(name = "major-release")
public class MajorMojo extends BaseMojo {

  /** The hashtag to use (legacy) */
  @Parameter(property = "sdkman.hashtag")
  protected String hashtag;

  /** The URL from where the candidate version can be downloaded */
  @Parameter(property = "sdkman.url")
  protected String url;

  /**
   * Platform to downloable URL mappings.
   * Supported platforms are:
   * <ul>
   * <li>MAC_OSX</li>
   * <li>MAC_ARM64</li>
   * <li>WINDOWS_64</li>
   * <li>LINUX_32</li>
   * <li>LINUX_64</li>
   * <li>LINUX_ARM32SF</li>
   * <li>LINUX_ARM32HF</li>
   * <li>LINUX_ARM64</li>
   * </ul>
   * Example:
   * <pre>
   *     "MAC_OSX"   :"https://host/micronaut-x.y.z-macosx.zip"
   *     "LINUX_64"  :"https://host/micronaut-x.y.z-linux64.zip"
   *     "WINDOWS_64":"https://host/micronaut-x.y.z-win.zip"
   * </pre>
   */
  @Parameter(property = "sdkman.platforms")
  protected Map<String, String> platforms;

  /** The URL where the release notes can be found */
  @Parameter(property = "sdkman.release.notes.url")
  protected String releaseNotesUrl;

  @Override
  protected HttpEntityEnclosingRequestBase createHttpRequest() {
    try {
      return new HttpPost(createURI(RELEASE_ENDPOINT));
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
      throw new MojoExecutionException("Sdk major release failed", e);
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
    responses.add(execCall(getPayload(), createDefaultHttpRequest()));

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
    if (hashtag != null && !hashtag.isEmpty()) payload.put("hashtag", hashtag);
    if (releaseNotesUrl != null && !releaseNotesUrl.isEmpty()) payload.put("url", releaseNotesUrl);
    return payload;
  }

  protected Map<String, String> getReleasePayload() {
    Map<String, String> payload = super.getPayload();
    payload.put("platform", "UNIVERSAL");
    payload.put("url", url);
    return payload;
  }

  protected HttpEntityEnclosingRequestBase createAnnounceHttpRequest() {
    try {
      return new HttpPost(createURI(ANNOUNCE_ENDPOINT));
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }

  protected HttpEntityEnclosingRequestBase createDefaultHttpRequest() {
    try {
      return new HttpPut(createURI(DEFAULT_ENDPOINT));
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
