package io.sdkman.maven;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.net.URI;
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
    Map<String, String> payload = super.getPayload();
    payload.put("version", version);
    payload.put("url", url);
    return payload;
  }

  @Override
  protected HttpEntityEnclosingRequestBase createHttpRequest() throws Exception {
    return new HttpPost(new URI("https", apiHost, "/release", null));
  }
}
