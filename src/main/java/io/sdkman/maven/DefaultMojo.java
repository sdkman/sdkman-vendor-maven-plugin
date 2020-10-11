package io.sdkman.maven;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPut;
import org.apache.maven.plugins.annotations.Mojo;

import java.net.URI;
import java.net.URISyntaxException;

import static io.sdkman.maven.infra.ApiEndpoints.DEFAULT_ENDPOINT;

/**
 * Mark a version as default.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Mojo(name = "default")
public class DefaultMojo extends BaseMojo {
  @Override
  protected HttpEntityEnclosingRequestBase createHttpRequest() {
    try {
      return new HttpPut(new URI("https", apiHost, DEFAULT_ENDPOINT, null));
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
