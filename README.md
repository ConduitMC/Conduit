# Conduit

Conduit is a next-generation Minecraft server built for stability, ease of use, and speed - without breaking Vanilla Minecraft mechanics.

# Contributing

If you would like to contribute to Conduit, please first visit our [issues page](https://github.com/ConduitMC/Conduit/issues) and find one you would like to tackle.

### Guidelines
 - Descriptive commits (don't make them like "did something")
 - Keep commit messages appropriate.
 - Keep pull request descriptions short, and descriptive.
 - Be ready for feedback

### Setting up a development environment

Requirements:

 - Java 8+
 - Maven
 - Git

To get started with developing, you can setup your environment with the following commands:

```
git clone https://github.com/ConduitMC/LegacyLauncher
cd LegacyLauncher
./gradlew build
mvn install:install-file "-Dpackaging=jar" "-DgeneratePom=true" "-Dfile=./build/libs/launchwrapper-1.0.jar" "-DgroupId=io.github.lightwayup" "-DartifactId=launchwrapper" "-Dversion=1.13"
cd ..

git clone https://github.com/ConduitMC/ConduitLauncher
cd ConduitLauncher
mvn clean package

cd ..
mkdir Server
cd Server
cp ../ConduitLauncher/target/ConduitLauncher-*.jar ConduitLauncher.jar
java -jar ConduitLauncher.jar dev

# Once that finsihes, you have a fully ready to go server for development, and all other libraries installed.
# Now, you can clone Conduit and start hacking!
cd ..
git clone https://github.com/ConduitMC/Conduit
cd Conduit
mvn clean package

# Then copy this build to the .mixins folder in the server.
cp target/Conduit.jar ../Server/.mixins/Conduit.jar
```
