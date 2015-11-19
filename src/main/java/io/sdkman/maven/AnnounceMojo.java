package io.sdkman.maven;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.net.URI;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Mojo(name = "announce")
public class AnnounceMojo extends BaseMojo {

  @Parameter(property = "sdkman.version", required = true)
  protected String version;

  @Parameter(property = "sdkman.hashtag", required = true)
  protected String hashtag;

  @Override
  protected Map<String, String> getPayload() {
    Map<String, String> payload = super.getPayload();
    payload.put("version", version);
    payload.put("hashtag", hashtag);
    return payload;
  }

  @Override
  protected HttpEntityEnclosingRequestBase createHttpRequest() throws Exception {
    return new HttpPost(new URI("https", apiHost, "/announce/struct", null));
  }
}
