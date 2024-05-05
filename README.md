# TScript34-P0018

Helps with the last couple of steps of preparing
a package to publish to Maven central

## How do I publish something to Maven Central?

One time:
- Set up an account on https://central.sonatype.com/publishing ,
  get your namespace verified, and all that
  - See [the TScript34-P0012-Lib01 README](https://github.com/TOGoS/tscript34-p0012-lib01)
    for a long story and an example pom.xml
- Set up a GPG signing key

For each project:
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
    will still result in the library being a transitive dependency
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

To publish:
- Run `mvn verify`.
  - If all goes well you should be prompted to unlock your GPG signing
    key, which will be used to generate some `.asc` files, and then
    P0018 will generate `.md5` and `.sha1` files and cram everything
    needed--the POM file, the executable, sources, and javadoc
    JARs, `.asc` signature files for each of those, and `.md5`
    and `.sha1` files for everything--into a zip file named
    named "target/" + artifact ID + "-" + version + "-package.zip".
- Go to https://central.sonatype.com/publishing ,
  - Click 'Publish Component'
  - Enter a 'deployment name'
    (not super important, but should probably be unique
    and indicate the artifact name and version),
  - Pick the generated ...-package.zip and upload it
  - Reload the publishing page once in a while to see
    your deployment transition to 'Validating' and,
    hopefully, 'Validated' states.
  - If errored, look at the error messages, try to fix them,
    rebuild and try uploading a new package.
  - Once it has reached 'Validated', click 'Publish',
    and after a little while your package should be available
    from Maven Central and you can depend on it from other projects.
    - Don't forget to blow away any `.lastUpdated` files under
      `~/.m2/repository/..../ARTIFACT-NAME/VERSION/` corresponding
      to previous, failed attempts to install it.

The last few steps after generating the package--uploading, waiting
for verification, and publishing a package--can be automated using
some web service APIs.
But I find the manual process of using the web interface not too bad
compared to all the wrangling of `pom.xml`, so haven't bothered.

This tool can also be run from the command-line,
in case you don't want to list it as a Maven dependency.

Perhaps it should also be a Maven plugin, to avoid
it being listed as a dependency of projects that use it.
And/or we could have two pom.xmls; the one that
is used to get the build to happen, and the one
that we publish, which doesn't need all the `<build>` crap in it.
