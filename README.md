## Maven Plugin for SDKMAN!

The [SDKMAN!](http://sdkman.io) Maven Plugin.

### Release a new Candidate Version

Usage in a `pom.xml`:

```
<plugin>
  <groupId>io.sdkman</groupId>
  <artifactId>sdkman-maven-plugin</artifactId>
  <version>1.0-SNAPSHOT</version>
  <execution>
    <goals>
      <goal>release</goal>
    </goals>
    <phase>deploy</phase>
    <configuration>
      <apiHost>the-api-host</apiHost>
      <candidate>the-candidate</candidate>
      <version>the-version</version>
      <url>the-url</url>
    </configuration>
  </execution>
</plugin>
```

The `consumerKey` and `consumerToken` could also be specified in the `configuration` tag but these should not be there. These
values have corresponding properties that should be used from the command line or configured in the `settings.xml`.

The `apiHost` specifies the SDKMAN! server to use, the default value is the SDKMAN! dev server _sdkman-vendor-dev.herokuapp.com_.
For real announce, this should be replaced by the SDKMAN! prod server.

Usage from command line:

```
mvn -e io.sdkman:sdkman-maven-plugin:1.0-SNAPSHOT:release \ 
    -Dsdkman.api.host=${api_host} \
    -Dsdkman.consumer.key=${my_key} \
    -Dsdkman.consumer.token=${my_token} \
    -Dsdkman.candidate=${my_candidate} \
    -Dsdkman.version=${my_version} \
    -Dsdkman.url=${my_url}
```

### Set existing Version as Default for Candidate

Usage in a `pom.xml`:

```
<plugin>
  <groupId>io.sdkman</groupId>
  <artifactId>sdkman-maven-plugin</artifactId>
  <version>1.0-SNAPSHOT</version>
  <execution>
    <goals>
      <goal>default</goal>
    </goals>
    <phase>deploy</phase>
    <configuration>
      <apiHost>the-api-host</apiHost>
      <candidate>the-candidate</candidate>
      <default>the-version</default>
    </configuration>
  </execution>
</plugin>
```

Usage from command line:

```
mvn -e io.sdkman:sdkman-maven-plugin:1.0-SNAPSHOT:default \ 
    -Dsdkman.api.host=${api_host} \
    -Dsdkman.consumer.key=${my_key} \
    -Dsdkman.consumer.token=${my_token} \
    -Dsdkman.candidate=${my_candidate} \
    -Dsdkman.default=${my_version}
```

### Broadcast a Structured Message

Usage in a `pom.xml`:

```
<plugin>
  <groupId>io.sdkman</groupId>
  <artifactId>sdkman-maven-plugin</artifactId>
  <version>1.0-SNAPSHOT</version>
  <execution>
    <goals>
      <goal>announce</goal>
    </goals>
    <phase>deploy</phase>
    <configuration>
      <apiHost>the-api-host</apiHost>
      <candidate>the-candidate</candidate>
      <version>the-version</version>
      <hashtag>the-hashtag</hashtag>
    </configuration>
  </execution>
</plugin>
```

Usage from command line:

```
mvn -e io.sdkman:sdkman-maven-plugin:1.0-SNAPSHOT:announce \ 
    -Dsdkman.api.host=${api_host} \
    -Dsdkman.consumer.key=${my_key} \
    -Dsdkman.consumer.token=${my_token} \
    -Dsdkman.candidate=${my_candidate} \
    -Dsdkman.version=${my_version} \
    -Dsdkman.hashtag=${my_hashtag}
```

### Broadcast a Freeform Message

Usage in a `pom.xml`:

```
<plugin>
  <groupId>io.sdkman</groupId>
  <artifactId>sdkman-maven-plugin</artifactId>
  <version>1.0-SNAPSHOT</version>
  <execution>
    <goals>
      <goal>announce</goal>
    </goals>
    <phase>deploy</phase>
    <configuration>
      <apiHost>the-api-host</apiHost>
      <text>the-text</text>
    </configuration>
  </execution>
</plugin>
```

Usage from command line:

```
mvn -e io.sdkman:sdkman-maven-plugin:1.0-SNAPSHOT:announce \ 
    -Dsdkman.api.host=${api_host} \
    -Dsdkman.consumer.key=${my_key} \
    -Dsdkman.consumer.token=${my_token} \
    -Dsdkman.text=${my_text}
```

## External configuration

The consumer key/token and the api host can be specified in the _settings.xml_ Maven configuration, most likely with
a profile to activate when necessary:

```
<profiles>
  <id>sdkman</id>
  <profile>
    <sdkman.api.host>the-api-host</sdkman.api.host>
    <sdkman.consumer.key>my-key</sdkman.consumer.key>
    <sdkman.consumer.token>my-token</sdkman.consumer.token>
  </profile>
</profiles>
```

It can be used activating the _sdkman_ profile.
