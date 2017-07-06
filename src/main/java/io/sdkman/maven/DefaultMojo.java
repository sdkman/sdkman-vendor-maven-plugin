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

  @Parameter(property = "sdkman.version", required = true)
  protected String _version;

  public String getVersion() {
    return _version;
  }

  public void setVersion(String _version) {
    this._version  = _version;
  }

  @Override
  protected Map<String, String> getPayload() throws Exception {
    if (candidate == null) {
      throw new Exception("Missing candidate");
    }
    Map<String, String> payload = new HashMap<>();
    payload.put("candidate", candidate);
    payload.put("version", _version);
    return payload;
  }

  @Override
  protected HttpEntityEnclosingRequestBase createHttpRequest() throws Exception {
    return new HttpPut(new URI("https", apiHost, "/default", null));
  }
}
