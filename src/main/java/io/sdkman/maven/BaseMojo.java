package io.sdkman.maven;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.InputStream;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
public abstract class BaseMojo extends AbstractMojo {

  @Parameter(property = "sdkman.consumer.key", required = true)
  protected String consumerKey;

  @Parameter(property = "sdkman.consumer.token", required = true)
  protected String consumerToken;

  @Parameter(property = "sdkman.candidate")
  protected String candidate;

  @Parameter(property = "sdkman.api.host", defaultValue = "sdkman-vendor-dev.herokuapp.com")
  protected String apiHost;

  protected abstract Map<String, String> getPayload() throws Exception;

  protected abstract HttpEntityEnclosingRequestBase createHttpRequest() throws Exception;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    try {
      ObjectMapper mapper = new ObjectMapper();
      String json = mapper.writeValueAsString(getPayload());

      //
      HttpEntityEnclosingRequestBase req = createHttpRequest();
      req.addHeader("consumer_key", consumerKey);
      req.addHeader("consumer_token", consumerToken);
      req.addHeader("Content-Type", "application/json");
      req.addHeader("Accept", "application/json");
      req.setEntity(new StringEntity(json));

      //
      CloseableHttpClient client = HttpClientBuilder.create().build();
      CloseableHttpResponse resp = client.execute(req);
      int statusCode = resp.getStatusLine().getStatusCode();
      try(InputStream in = resp.getEntity().getContent()) {
        Map<String, ?> sdkmanResp = (Map<String, ?>) mapper.readValue(in, Map.class);
        for (Map.Entry<String, ?> prop : sdkmanResp.entrySet()) {
          System.out.println(prop.getKey() + ":" + prop.getValue());
        }
      }
      if (statusCode < 200 || statusCode >= 300) {
        throw new Exception("Server returned error " + resp.getStatusLine());
      }
    } catch (Exception e) {
      throw new MojoExecutionException("Release failed", e);
    }
  }
}
