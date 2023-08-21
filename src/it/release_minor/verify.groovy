import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

Path path = Paths.get(basedir.toString(), 'build.log')
String buildLog = new String(Files.readAllBytes(path))
assert buildLog.contains('http://localhost:8081/versions')
assert buildLog.contains('"candidate":"it"')
assert buildLog.contains('"version":"1.0.0-SNAPSHOT"')
assert buildLog.contains('"url":"https://host/it-1.0.0-SNAPSHOT.zip"')
assert buildLog.contains('http://localhost:8081/announce/struct')
assert buildLog.contains('Sdk vendor operation successful')
