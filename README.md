# Conduit [![Status](https://github.com/ConduitMC/Conduit/workflows/Gradle%20CI/badge.svg)](https://github.com/ConduitMC/Conduit/actions) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/864ff27f13994fdda6334e439977ef7e)](https://www.codacy.com/gh/ConduitMC/Conduit?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ConduitMC/Conduit&amp;utm_campaign=Badge_Grade) [![Nexus](https://img.shields.io/static/v1?label=Nexus&message=Repository&color=bright-green?style=flat)](https://repo.conduit.systems/)


Conduit is a next-generation Minecraft server built for stability, ease of use, and speed - without breaking Vanilla Minecraft mechanics.

## Contributing

If you would like to contribute to Conduit, please first visit our [issues page](https://github.com/ConduitMC/Conduit/issues) and find one you would like to tackle.

### Guidelines
-   Descriptive commits (don't make them like "did something")
-   Keep commit messages appropriate.
-   Keep pull request descriptions short, and descriptive.
-   Be ready for feedback

### Setting up a development environment

Requirements:

-   Java 8+
-   Git

To get started with developing, you can setup your environment with the following commands:

``` text
git clone https://github.com/ConduitMC/ConduitLauncher
cd ConduitLauncher
./gradlew build install
cd ..

mkdir Server
cd Server
cp ../ConduitLauncher/target/ConduitLauncher-*.jar ConduitLauncher.jar
java -jar ConduitLauncher.jar dev

# Once that finishes, you have a fully ready to go server for development, and all other libraries installed.
# Now, you can clone Conduit and start hacking!
cd ..
git clone https://github.com/ConduitMC/Conduit
cd Conduit
./gradlew build install

# Then copy this build to the .mixins folder in the server.
cp target/Conduit.jar ../Server/.mixins/Conduit.jar
```
