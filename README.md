# springboot-zuulproxy-consul-serviceDiscovery
Project created for studying a way to integrate ZuulProxy with Consul instead of Eureka.

# Initial Setup

  - Run Consul inside Docker :
    ```sh
        docker run -d --net=host --name=dev-consul consul:latest