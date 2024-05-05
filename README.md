# TScript34-P0018

Helps with the last couple of steps of preparing
a package to publish to Maven central

## How do I publish something to Maven Central?

- Set up an account on https://central.sonatype.com/publishing,
  get your namespace verified, and all that
  - See [the TScript34-P0012-Lib01 README](https://github.com/TOGoS/tscript34-p0012-lib01)
    for a long story and an example pom.xml
- Add a bunch of crap to your pom.xml to generate
  your sources and javadoc JAR files.
  See this project's [pom.xml](./pom.xml) for example.
- List this library as a dependency in your pom.xml
  - ```xml
    <dependency>
    	<groupId>net.nuke24.tscript34.p0018</groupId>
    	<artifactId>tscript34-p0018</artifactId>
    	<version>0.0.1</version>
    </dependency>
    ```
  - It's of course not needed at runtime
    but as far as I know there is no way to tell Maven that
    a dependency is only needed at built time; `<scope>compile</scope>`
    will still result in the library being a transitive dependency)
- Within project > build > plugins,
  set up a final build step in the 'verify' phase to call it:

```xml
			<plugin>
				<groupId>org.codehaus.mojo</groupId>
				<artifactId>exec-maven-plugin</artifactId>
				<version>3.0.0</version>
				<executions>
					<execution>
						<id>hash-the-files</id>
						<phase>verify</phase>
						<goals>
							<goal>java</goal>
						</goals>
						<configuration>
							<mainClass>net.nuke24.tscript34.p0018.Packager</mainClass>
						</configuration>
					</execution>
				</executions>
			</plugin>
```

It can also be run from the command-line.

Perhaps this should be a Gradle plugin.
