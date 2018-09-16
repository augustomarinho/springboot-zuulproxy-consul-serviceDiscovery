# Goal
Project created for studying the follow requirements:
 - a way to integrate ZuulProxy with Consul instead of Eureka
 - Updating ServiceIds with Consult without ConsulTemplate strategies
 - Creating a Weight Roud Roubin Filter to implement Blue/Green Deployment

# Initial Setup

  - Run Consul inside Docker :
    ```sh
        docker run -d --net=host --name=dev-consul consul:latest
    ```