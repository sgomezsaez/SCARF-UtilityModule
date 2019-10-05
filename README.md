# SCARF-UtilityModule 
SCARF-UtilityModule is the utility service used to store, retrieve, and calculate the utility for reusable cloud application topologies.

# Build Source & Run
1. Build *Pertos* application: `cd Kereta && mvn install && cd ..`
2. Copy the WAR application to the Deployment directory: `cp ./Kereta/target/Kereta-0.0.1-SNAPSHOT.war ./Deployment/kereta_app/bin/Kereta.war`
3. Build the docker container: `docker-compose build`
4. Run: `docker-compose up`

# Running the SCARF toolchain
If you want to run the complete tool chain, please go to [SCARF](https://github.com/sgomezsaez/SCARF)