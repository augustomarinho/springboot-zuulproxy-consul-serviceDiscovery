# Goal
Project created for studying the follow requirements:
 - a way to integrate ZuulProxy with Consul instead of Eureka
 - Updating ServiceIds with Consult without ConsulTemplate strategies
 - Creating a Weight Roud Roubin Filter to implement Blue/Green Deployment

# Initial Setup

  - Run Consul inside Docker (For Development) :
    ```sh
        docker run -d --net=host --name=dev-consul consul:latest
    ```

  - Run Consul in Server Mode (For Development):
      ```sh
          docker run -d --name=dev-consul --net=host -e 'CONSUL_LOCAL_CONFIG={"skip_leave_on_interrupt": true}' consul agent -server -bind=<external ip> -retry-join=<root agent ip> -bootstrap-expect=<number of server agents> -ui
      ```

  - Run Consul in Client Mode (For Development using consult server together):
      ```sh
        docker run -d --name=dev-consul-client --net=host -e 'CONSUL_LOCAL_CONFIG={"leave_on_terminate": true}' consul agent -bind=<external ip> -retry-join=<root agent ip>
      ```

# Run Configurations to simulate Blue/Green env for RestServiceDiscoveryApp
Blue (Default in application.yml)
   ```sh
   -Dserver.port=8080 -Dspring.cloud.consul.discovery.tags="color=blue" -Dapp.color=blue
   ```

Green
   ```sh
   -Dserver.port=8081 -Dspring.cloud.consul.discovery.tags="color=green" -Dapp.color=green
   ```


# How to configure manually Key-value in Consul Web Console

![Consul KV Config](https://github.com/augustomarinho/springboot-zuulproxy-consul-serviceDiscovery/blob/master/amzuulproxy/docs/Consul-KV.png)