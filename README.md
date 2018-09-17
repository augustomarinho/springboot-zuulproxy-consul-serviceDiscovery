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

# Run Configurations to simulate Blue/Green env for RestServiceDiscoveryApp

Blue (Default in application.yml)

 -Dserver.port=8080 -Dspring.cloud.consul.discovery.tags="color=green" -Dapp.color=green

Green

 -Dserver.port=8081 -Dspring.cloud.consul.discovery.tags="color=green" -Dapp.color=green


# How to configure manually Key-value in Consul Web Console

![Consul KV Config](https://github.com/augustomarinho/springboot-zuulproxy-consul-serviceDiscovery/blob/master/amzuulproxy/docs/Consul-KV.png)
