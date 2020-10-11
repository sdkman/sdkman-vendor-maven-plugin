package io.sdkman.maven;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import static io.sdkman.maven.infra.ApiEndpoints.ANNOUNCE_ENDPOINT;

/**
 * Posts an announcment
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Mojo(name = "announce")
public class AnnounceMojo extends BaseMojo {

  /** the hashtag to use */
  @Parameter(property = "sdkman.hashtag", required = true)
  protected String hashtag;

  @Override
  protected Map<String, String> getPayload() {
    Map<String, String> payload = super.getPayload();
    payload.put("hashtag", hashtag);
    return payload;
  }

  @Override
  protected HttpEntityEnclosingRequestBase createHttpRequest() {
    try {
      return new HttpPost(new URI("https", apiHost, ANNOUNCE_ENDPOINT, null));
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
