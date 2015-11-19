package io.sdkman.maven;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPut;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Mojo(name = "default")
public class DefaultMojo extends BaseMojo {

  @Parameter(property = "sdkman.default", required = true)
  protected String _default;

  public String getDefault() {
    return _default;
  }

  public void setDefault(String _default) {
    this._default = _default;
  }

  @Override
  protected Map<String, String> getPayload() throws Exception {
    if (candidate == null) {
      throw new Exception("Missing candidate");
    }
    Map<String, String> payload = new HashMap<>();
    payload.put("candidate", candidate);
    payload.put("default", _default);
    return payload;
  }

  @Override
  protected HttpEntityEnclosingRequestBase createHttpRequest() throws Exception {
    return new HttpPut(new URI("https", apiHost, "/default", null));
  }
}
