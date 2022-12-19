# Steps for building and running with Docker

1. Build the Docker image
    ```bash
    ./mvnw package -Pprod docker:build
    ``` 
    
2. Check the Docker image
    ```bash
    docker images spfskyc
 
    REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
    spfskyc             latest              698774b37bfb        2 hours ago         178MB

    ``` 
3. Run Docker Compose

    ```bash
    docker-compose -f src/main/docker/app.yml up
   
    Creating network "docker_default" with the default driver
    Creating docker_spfskyc-mongodb_1   ... done
    Creating docker_jhipster-registry_1 ... done
    Creating docker_spfskyc-app_1       ... done
    Attaching to docker_jhipster-registry_1, docker_spfskyc-mongodb_1, docker_spfskyc-app_1
    ``` 
    
    This command will start the Docker containers below:
    - JHipster Registry
    - MongoDB
    - SPFS-KYC App
    
    The applications can be access through the advertised URL/port

    ```bash
    # MongoDB
    spfskyc-mongodb_1    | 2018-05-24T10:09:40.688+0000 I NETWORK  [initandlisten] waiting for connections on port 27017

    # JHipster Registry
    jhipster-registry_1  | ----------------------------------------------------------
    jhipster-registry_1  |  Application 'jhipster-registry' is running! Access URLs:
    jhipster-registry_1  |  Local:          http://localhost:8761
    jhipster-registry_1  |  External:       http://172.19.0.3:8761
    jhipster-registry_1  |  Profile(s):     [dev]
    jhipster-registry_1  | ----------------------------------------------------------

    # KYC App
    spfskyc-app_1        | ----------------------------------------------------------
    spfskyc-app_1        |  Application 'spfskyc' is running! Access URLs:
    spfskyc-app_1        |  Local:          http://localhost:8080
    spfskyc-app_1        |  External:       http://172.19.0.4:8080
    spfskyc-app_1        |  Profile(s):     [prod, swagger]
    spfskyc-app_1        | ----------------------------------------------------------
    ```
4. Stop and shutdown all containers
    ```bash
    docker-compose -f src/main/docker/app.yml down
 
    Removing docker_spfskyc-app_1       ... done
    Removing docker_jhipster-registry_1 ... done
    Removing docker_spfskyc-mongodb_1   ... done
    Removing network docker_default
    ``` 
