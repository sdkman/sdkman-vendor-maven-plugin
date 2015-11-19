package io.sdkman.maven;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Mojo(name = "release")
public class ReleaseMojo extends AbstractMojo {

  @Parameter(property = "sdkman.consumer.key", required = true)
  private String consumerKey;

  @Parameter(property = "sdkman.consumer.token", required = true)
  private String consumerToken;

  @Parameter(property = "sdkman.candidate", required = true)
  private String candidate;

  @Parameter(property = "sdkman.version", required = true)
  private String version;

  @Parameter(property = "sdkman.url", required = true)
  private String url;

  @Parameter(defaultValue = "sdkman-vendor-dev.herokuapp.com")
  private String apiHost;

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {

    try {
      ObjectMapper mapper = new ObjectMapper();
      HashMap<String, String> json = new LinkedHashMap<>();
      json.put("candidate", candidate);
      json.put("version", version);
      json.put("url", url);
      String jsonString = mapper.writeValueAsString(json);


      //
      HttpPost post = new HttpPost(new URI("https", apiHost, "/release", null));
      post.addHeader("consumer_key", consumerKey);
      post.addHeader("consumer_token", consumerToken);
      post.addHeader("Content-Type", "application/json");
      post.addHeader("Accept", "application/json");
      post.setEntity(new StringEntity(jsonString));

      //
      CloseableHttpClient client = HttpClientBuilder.create().build();
      CloseableHttpResponse resp = client.execute(post);
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
