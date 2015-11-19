package io.sdkman.maven;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 */
@Mojo(name = "announce")
public class AnnounceMojo extends BaseMojo {

  @Parameter(property = "sdkman.version")
  protected String version;

  @Parameter(property = "sdkman.hashtag")
  protected String hashtag;

  @Parameter(property = "sdkman.text")
  protected String text;

  @Override
  protected Map<String, String> getPayload() throws Exception {
    if (hashtag == null) {
      hashtag = "";
    }
    if (text == null) {
      text = "";
    }
    Map<String, String> payload = new HashMap<>();
    if (text.isEmpty()) {
      if (hashtag.isEmpty()) {
        throw new Exception("Must set one of hashtag or text property");
      }
      if (candidate == null || candidate.isEmpty()) {
        throw new Exception("Missing candidate");
      }
      if (version == null || version.isEmpty()) {
        throw new Exception("Missing version");
      }
      payload.put("version", version);
      payload.put("candidate", candidate);
      payload.put("hashtag", hashtag);
    } else {
      payload.put("text", text);
    }
    return payload;
  }

  @Override
  protected HttpEntityEnclosingRequestBase createHttpRequest() throws Exception {
    String requestURI;
    if (text.isEmpty()) {
      requestURI = "/announce/struct";
    } else {
      requestURI = "/announce/freeform";
    }
    return new HttpPost(new URI("https", apiHost, requestURI, null));
  }
}
