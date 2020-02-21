# systems.conduit.core.Conduit [![Status](https://github.com/ConduitMC/systems.conduit.core.Conduit/workflows/Gradle%20CI/badge.svg)](https://github.com/ConduitMC/systems.conduit.core.Conduit/actions) [![Codacy Badge](https://systems.conduit.api.codacy.com/project/badge/Grade/864ff27f13994fdda6334e439977ef7e)](https://www.codacy.com/gh/ConduitMC/systems.conduit.core.Conduit?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ConduitMC/systems.conduit.core.Conduit&amp;utm_campaign=Badge_Grade) [![Nexus](https://img.shields.io/static/v1?label=Nexus&message=Repository&color=bright-green?style=flat)](https://repo.conduit.systems/)

systems.conduit.core.Conduit is a next-generation Minecraft server built for stability, ease of use, and speed - without breaking Vanilla Minecraft mechanics.

## Contributing

If you would like to contribute to systems.conduit.core.Conduit, please first visit our [issues page](https://github.com/ConduitMC/systems.conduit.core.Conduit/issues) and find one you would like to tackle.

### Guidelines
-   Descriptive commits (don't make them like "did something")
-   Keep commit messages appropriate.
-   Keep pull request descriptions short, and descriptive.
-   Be ready for feedback

### Setting up a development environment

Requirements:

-   Java 8+
-   Git

To get started with developing, you can setup your environment with the following systems.conduit.core.commands:

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
# Now, you can clone systems.conduit.core.Conduit and start hacking!
cd ..
git clone https://github.com/ConduitMC/systems.conduit.core.Conduit
cd systems.conduit.core.Conduit
./gradlew build install

# Then copy this build to the .systems.conduit.core.mixins folder in the server.
cp target/systems.conduit.core.Conduit.jar ../Server/.systems.conduit.core.mixins/systems.conduit.core.Conduit.jar
```
