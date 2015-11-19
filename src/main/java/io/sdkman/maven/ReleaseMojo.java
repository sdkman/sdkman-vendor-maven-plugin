package io.sdkman.maven;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Mojo(name = "release")
public class ReleaseMojo extends BaseMojo {

  @Parameter(property = "sdkman.version", required = true)
  protected String version;

  @Parameter(property = "sdkman.url", required = true)
  protected String url;

  @Override
  protected Map<String, String> getPayload() throws Exception {
    if (candidate == null) {
      throw new Exception("Missing candidate");
    }
    Map<String, String> payload = new HashMap<>();
    payload.put("candidate", candidate);
    payload.put("version", version);
    payload.put("url", url);
    return payload;
  }

  @Override
  protected HttpEntityEnclosingRequestBase createHttpRequest() throws Exception {
    return new HttpPost(new URI("https", apiHost, "/release", null));
  }
}
