 # maven打包
 mvn clean package
 

# 高可用p2p集群运行

# 正常情况下，统一命令运行，这里因为一个机器上部署多个eurekaserver，所以需要通过profile来指定运行端口

## 修改host，加上 127.0.0.1 eureka1  eureka2  eureka3

java -jar .\target\lesson-1-eureka-0.0.1-SNAPSHOT.jar --spring.profiles.active=eureka1

java -jar .\target\lesson-1-eureka-0.0.1-SNAPSHOT.jar --spring.profiles.active=eureka2

java -jar .\target\lesson-1-eureka-0.0.1-SNAPSHOT.jar --spring.profiles.active=eureka3