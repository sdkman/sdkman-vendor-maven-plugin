package io.sdkman.maven;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

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

  /** The hashtag to use (legacy) */
  @Parameter(property = "sdkman.hashtag")
  protected String hashtag;

  /** The URL where the release notes can be found */
  @Parameter(property = "sdkman.release.notes.url")
  protected String releaseNotesUrl;

  @Override
  protected Map<String, String> getPayload() {
    Map<String, String> payload = super.getPayload();
    if (hashtag != null && !hashtag.isEmpty()) payload.put("hashtag", hashtag);
    if (releaseNotesUrl != null && !releaseNotesUrl.isEmpty()) payload.put("url", releaseNotesUrl);
    return payload;
  }

  @Override
  protected HttpEntityEnclosingRequestBase createHttpRequest() {
    try {
      return new HttpPost(createURI(ANNOUNCE_ENDPOINT));
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
