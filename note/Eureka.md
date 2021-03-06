#SpringCloud
```text
1、eureka是springcloud的核心；
2、配置中心，对各模块配置文件进行集中化管理；
3、消费者怎么去调用提供者，会用到 1个工具 2个组件 ribbon feign；
4、服务的 限流 熔断 机制；
5、网关组件，因为微服务一般不对外的，因此使用网关Zuul，将外部 http请求映射到 服务上；
6、微服务就需要知道事件驱动，目前，系统之间都是通过 http接口 调用的，但是有些情况下，可以通过 rabbitMQ解耦的，springcloud提供了 消息总线组件，它集成了rabbitmq，kafka，来实现事件驱动，因此需要学习springcloud
中事件驱动怎么做；
7、请求到网关 Zuul，映射到 服务1 进行下单，是需要调到很多系统的，如果用户请求失败了，要去跟踪原因，传统的日志只是单系统的，系统之间日志如何关联呢？需要 分布式服务调用追踪组件 sleuth；并使用ELK整理整个集群的信息
```
##Eureka
```text
为什么使用 eureka？
    当 服务启动时，将自己的 服务名 ip port 实例id 注册到 eureka-server，并从eureka-server上拉取所有其他服务的 服务名 ip port 实例id 缓存在本地；
    一个服务 有多个实例，各实例 服务名相同，ip port 实例id不同，同一个服务名 下有多个 ip port 可以调，因此需要负载均衡 从里面选择一个ip port进行调用；
    服务名         ip          port        instanceID
    serviceA      101          8080         101
    serviceA      202          9090         202
eureka结构？
    eureka-server + eureka-instance + eureka-client：eureka-client 向 eureka-server 注册或者拉取 eureka-instance；
eureka原理分析：
    1、服务提供者怎么注册到服务中心的？
    2、注册中心怎么接收注册请求？
    3、注册中心如何存储服务信息？
    4、注册中心高可用机制是什么？
    5、Eureka集群同步机制？
    6、注册中心剔除服务的机制？
    7、服务消费者如何获取服务信息？
```
###Eureka源码分析
#### Eureka源码分析总结
```text
1、Spring框架中，实现 XxxAware接口，要覆写 setXxx(Xxx xx)方法，Spring会自动将Xxx设置进来，例如 ApplicationContextAware{ void setApplicationContext(ApplicationContext applicationContext) throws BeansException;}
2、Spring框架中，实现 SmartLifeCycle接口，覆写 start()方法，当 刷新上下文，所有bean被实例化 初始化以后，会执行 该bean的 start()方法，见 com.dongnaoedu.springcloud.SmartLifecycleDemo
3、web.xml的加载顺序是：Context-Param -> Listener -> Filter -> Servlet，先将Context-Param 即参数封装到 ServletContext，然后通过setServletContext()方法 将ServletContext设置到各个类中；
ContextLoaderListener#contextInitialized(ServletContextEvent event)方法 监听ServletContextEvent事件；
```
```xml
<web-app>
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:applicationContext-*.xml</param-value>
    </context-param>
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
</web-app>
```
####Eureka 服务端 源码分析
2、springcloud中 eureka服务端和客户端是怎么通过注解生效的？
```text
springboot启动时，会扫描spring-cloud-netflix-eureka-server包 META-INF/spring.factories，内容如下：
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  org.springframework.cloud.netflix.eureka.server.EurekaServerAutoConfiguration     # eureka服务端初始化
spring.factories中这样定义目的：springboot启动时会 自动装配 以EnableAutoConfiguration为key的value，因此EurekaServerAutoConfiguration会被 实例化到当前IOC容器
```

```text
1、eureka服务端 接收 eureka客户端 注册请求 并存储 实例信息；
2、eureka服务端 高可用；peer 2 peer模式，没有主从节点之分，各节点完全对等，无中心化，相互注册 同步信息，即 实例信息 注册到 eureka1时，eureka2/3都会同步这个实例信息；
3、eureka服务端 服务剔除，默认90s内，没有收到 eureka客户端心跳；
```
```java
/**1、eureka服务端 接收 eureka客户端 注册请求 并存储 实例信息；
* eureka 服务端 受理注册请求，通过 restful接口接收，通过jersey框架实现 restful接口，接口规则 https://github.com/Netflix/eureka/wiki
* 初始化过程，springcloud集成 eureka原生包中的 Jersey REST接口框架*/
@Configuration
public class EurekaServerAutoConfiguration extends WebMvcConfigurerAdapter {
    @Bean   //实例化 一个 JerseyApplication
    public javax.ws.rs.core.Application jerseyApplication(Environment environment, ResourceLoader resourceLoader) {
        //...
        for (String basePackage : EUREKA_PACKAGES) {    //EUREKA_PACKAGES = new String[] { "com.netflix.discovery","com.netflix.eureka" }; 扫描eureka下的包
            Set<BeanDefinition> beans = provider.findCandidateComponents(basePackage);
            for (BeanDefinition bd : beans) {
                Class<?> cls = ClassUtils.resolveClassName(bd.getBeanClassName(),resourceLoader.getClassLoader());
                classes.add(cls);
            }
        }
        //...
        return rc;
    }
    @Bean       //注册过滤器，拦截 /eureka/*请求
    public FilterRegistrationBean jerseyFilterRegistration(javax.ws.rs.core.Application eurekaJerseyApp) {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new ServletContainer(eurekaJerseyApp));
        bean.setOrder(Ordered.LOWEST_PRECEDENCE);
        bean.setUrlPatterns(Collections.singletonList("/eureka/*"));    
        return bean;
    }
}
/**jersey框架 接收 REST接口 注册请求
* */
@Produces({"application/xml", "application/json"})
public class ApplicationResource {
    @POST       //接收 注册请求
    @Consumes({"application/json", "application/xml"})
    public Response addInstance(InstanceInfo info,@HeaderParam(PeerEurekaNode.HEADER_REPLICATION) String isReplication) {
        registry.register(info, "true".equals(isReplication));      //跟register()
        return Response.status(204).build();  
    }
}
public class InstanceRegistry extends PeerAwareInstanceRegistryImpl implements ApplicationContextAware {
    @Override
	public void register(final InstanceInfo info, final boolean isReplication) {
		handleRegistration(info, resolveInstanceLeaseDuration(info), isReplication);        //跟handleRegistration()
		super.register(info, isReplication);    //跟super.register()进入 高可用
	}
	//跟handleRegistration()
	private void handleRegistration(InstanceInfo info, int leaseDuration, boolean isReplication) {
        publishEvent(new EurekaInstanceRegisteredEvent(this, info, leaseDuration, isReplication));  //发送 事件，触发 相应事件 监听器
    }
}
//跟super.register()进入
public abstract class AbstractInstanceRegistry implements InstanceRegistry {
    public void register(InstanceInfo registrant, int leaseDuration, boolean isReplication) {
        try {
            read.lock();
            Map<String, Lease<InstanceInfo>> gMap = registry.get(registrant.getAppName());      //registry 存 注册 实例信息，registry是一个map
            REGISTER.increment(isReplication);
            if (gMap == null) {
                final ConcurrentHashMap<String, Lease<InstanceInfo>> gNewMap = new ConcurrentHashMap<String, Lease<InstanceInfo>>();
                gMap = registry.putIfAbsent(registrant.getAppName(), gNewMap);
                if (gMap == null) {
                    gMap = gNewMap;
                }
            }
            //...
        } finally {
            read.unlock();
        }
    }
}
```

```java
/**
*2、eureka服务端 高可用
*/
public class EurekaServerAutoConfiguration extends WebMvcConfigurerAdapter {
    @Bean       //eurekaServer上下文初始化
	public EurekaServerContext eurekaServerContext(ServerCodecs serverCodecs, PeerAwareInstanceRegistry registry, PeerEurekaNodes peerEurekaNodes) {
		//跟 new DefaultEurekaServerContext()
        return new DefaultEurekaServerContext(this.eurekaServerConfig, serverCodecs, registry, peerEurekaNodes, this.applicationInfoManager);
	}
}
@Singleton
public class DefaultEurekaServerContext implements EurekaServerContext {
    @Inject
    public DefaultEurekaServerContext(EurekaServerConfig serverConfig,ServerCodecs serverCodecs,PeerAwareInstanceRegistry registry,PeerEurekaNodes peerEurekaNodes,ApplicationInfoManager applicationInfoManager) {
        this.serverConfig = serverConfig;
        this.serverCodecs = serverCodecs;
        this.registry = registry;
        this.peerEurekaNodes = peerEurekaNodes;
        this.applicationInfoManager = applicationInfoManager;
    }
    @PostConstruct      //@PostConstruct 加载Servlet 后运行，且只运行一次
    @Override
    public void initialize() throws Exception {
        peerEurekaNodes.start();                //跟；刷新 peer列表，并启动更新任务
        registry.init(peerEurekaNodes);
    }
}
@Singleton
public class PeerEurekaNodes {
    public void start() {
        updatePeerEurekaNodes(resolvePeerUrls());       //跟 updatePeerEurekaNodes()
    }
    protected void updatePeerEurekaNodes(List<String> newPeerUrls) {
        if (newPeerUrls.isEmpty()) {return;}
        Set<String> toShutdown = new HashSet<>(peerEurekaNodeUrls);     //eureka服务端初始化时，维护了一个 peerEurekaNodeUrls
        toShutdown.removeAll(newPeerUrls);
        Set<String> toAdd = new HashSet<>(newPeerUrls);
        toAdd.removeAll(peerEurekaNodeUrls);

        if (toShutdown.isEmpty() && toAdd.isEmpty()) { // No change
            return;
        }

        // Remove peers no long available
        List<PeerEurekaNode> newNodeList = new ArrayList<>(peerEurekaNodes);

        // Add new peers
        if (!toAdd.isEmpty()) {
            for (String peerUrl : toAdd) {
                newNodeList.add(createPeerEurekaNode(peerUrl));
            }
        }
    }
}
/** PeerAwareInstanceRegistryImpl用来同步 renew/register/cancel*/
@Singleton
public class PeerAwareInstanceRegistryImpl extends AbstractInstanceRegistry implements PeerAwareInstanceRegistry {
    @Override
    public void register(final InstanceInfo info, final boolean isReplication) {
        int leaseDuration = Lease.DEFAULT_DURATION_IN_SECS; //默认 90
        if (info.getLeaseInfo() != null && info.getLeaseInfo().getDurationInSecs() > 0) {
            leaseDuration = info.getLeaseInfo().getDurationInSecs();
        }
        super.register(info, leaseDuration, isReplication);
        replicateToPeers(Action.Register, info.getAppName(), info.getId(), info, null, isReplication);  //跟
    }
    private void replicateToPeers(Action action, String appName, String id,InstanceInfo info /* optional */,InstanceStatus newStatus /* optional */, boolean isReplication) {
        try {
            if (isReplication) {     //防止循环传播   
                numberOfReplicationsLastMin.increment();
            }
            // If it is a replication already, do not replicate again as this will create a poison replication
            if (peerEurekaNodes == Collections.EMPTY_LIST || isReplication) {
                return;
            }

            for (final PeerEurekaNode node : peerEurekaNodes.getPeerEurekaNodes()) {    //循环当前所有节点
                // If the url represents this host, do not replicate to yourself.
                if (peerEurekaNodes.isThisMyUrl(node.getServiceUrl())) {
                    continue;
                }
                replicateInstanceActionsToPeers(action, appName, id, info, newStatus, node);    //将实例同步到 其他节点
            }
        } finally {
            tracer.stop();
        }
    }
}
```
```java
/**3、eureka服务端 剔除 服务*/
@Configuration
public class EurekaServerInitializerConfiguration implements ServletContextAware, SmartLifecycle, Ordered {
    @Override
    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eurekaServerBootstrap.contextInitialized(EurekaServerInitializerConfiguration.this.servletContext); //初始化上下文
                    publish(new EurekaRegistryAvailableEvent(getEurekaServerConfig()));
                    EurekaServerInitializerConfiguration.this.running = true;
                    publish(new EurekaServerStartedEvent(getEurekaServerConfig()));
                }
                catch (Exception ex) {}
            }
        }).start();
    }
    public void contextInitialized(ServletContext context) {
        try {
            initEurekaEnvironment();
            initEurekaServerContext();
            context.setAttribute(EurekaServerContext.class.getName(), this.serverContext);
        }
        catch (Throwable e) {}
    }
}
public class EurekaServerBootstrap {
    protected void initEurekaServerContext() throws Exception {
		//...
		this.registry.openForTraffic(this.applicationInfoManager, registryCount); //跟
	}
}
public abstract class AbstractInstanceRegistry implements InstanceRegistry {
    protected void postInit() {
        renewsLastMin.start();
        if (evictionTaskRef.get() != null) {
            evictionTaskRef.get().cancel();
        }
        evictionTaskRef.set(new EvictionTask());    //创建剔除任务线程，封装到定时任务里
        evictionTimer.schedule(evictionTaskRef.get(),serverConfig.getEvictionIntervalTimerInMs(),serverConfig.getEvictionIntervalTimerInMs());
    }
    class EvictionTask extends TimerTask {
        private final AtomicLong lastExecutionNanosRef = new AtomicLong(0l);
        @Override
        public void run() {
            try {
                long compensationTimeMs = getCompensationTimeMs();
                evict(compensationTimeMs);  //跟
            } catch (Throwable e) {}
        }
    }
    public void evict(long additionalLeaseMs) {
        if (!isLeaseExpirationEnabled()) {return;}      //开启自我保护模式，直接返回，不会执行后面的 剔除
        List<Lease<InstanceInfo>> expiredLeases = new ArrayList<>();
        //..
    }
}
public class PeerAwareInstanceRegistryImpl extends AbstractInstanceRegistry implements PeerAwareInstanceRegistry {
    @Override
    public boolean isLeaseExpirationEnabled() {
        if (!isSelfPreservationModeEnabled()) {     //开启自我保护模式，不会剔除 实例信息，如果关闭自我保护模式，判断最后一分钟接收到心跳数是否达到阈值，达到阈值，会进行剔除，达不到阈值，不会剔除
            return true;
        }
        //当 最后一分钟收到实例心跳数getNumOfRenewsInLastMin() > 每分钟收到实例心跳数numberOfRenewsPerMinThreshold时，证明网络没问题，eureka不会关闭剔除
        return numberOfRenewsPerMinThreshold > 0 && getNumOfRenewsInLastMin() > numberOfRenewsPerMinThreshold;  
    }
}
```
####Eureka 客户端 源码分析
```text
springboot启动时，会扫描 spring-cloud-netflix-eureka-client包META-INF目录下的spring.factories，内容如下：
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration,\    # eureka客户端初始化
  org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration    # eureka客户端初始化
spring.factories中这样定义目的：springboot启动时会 自动装配 以EnableAutoConfiguration为key的value，因此EurekaClientAutoConfiguration,EurekaDiscoveryClientConfiguration会被 实例化到当前IOC容器
```
```text
注册：eureka-client 怎么把 instance(appName,ip,port,instanceId) 注册到 eureka-server？
```

```java
/**将 eureka.instance为前缀的配置信息 封装到 InstanceInfo里，并一起封装到 ApplicationInfoManager*/
@Configuration
public class EurekaClientAutoConfiguration {
    @Configuration
    protected static class EurekaClientConfiguration {
        @Bean(destroyMethod = "shutdown")
        @ConditionalOnMissingBean(value = EurekaClient.class, search = SearchStrategy.CURRENT)
        public EurekaClient eurekaClient(ApplicationInfoManager manager, EurekaClientConfig config) {
            return new CloudEurekaClient(manager, config, this.optionalArgs,
                    this.context);
        }
        @Bean
        @ConditionalOnMissingBean(value = {ApplicationInfoManager.class},search = SearchStrategy.CURRENT)
        public ApplicationInfoManager eurekaApplicationInfoManager(EurekaInstanceConfig config) {
            InstanceInfo instanceInfo = (new InstanceInfoFactory()).create(config);
            return new ApplicationInfoManager(config, instanceInfo);
        }
    }
}
/**跟 InstanceInfoFactory#create()*/
public class InstanceInfoFactory {

    public InstanceInfo create(EurekaInstanceConfig config) {/**EurekaInstanceConfig 实现类  EurekaInstanceConfigBean，将 以eureka.instance为前缀的配置 封装到该实现类*/
        LeaseInfo.Builder leaseInfoBuilder = LeaseInfo.Builder.newBuilder().setRenewalIntervalInSecs(config.getLeaseRenewalIntervalInSeconds())
            .setDurationInSecs(config.getLeaseExpirationDurationInSeconds());

        InstanceInfo.Builder builder = InstanceInfo.Builder.newBuilder();
        
        String namespace = config.getNamespace();
        if (!namespace.endsWith(".")) {
            namespace = namespace + ".";
        }
        builder.setNamespace(namespace).setAppName(config.getAppname())
                .setInstanceId(config.getInstanceId())
                .setAppGroupName(config.getAppGroupName())
                .setDataCenterInfo(config.getDataCenterInfo())
                .setIPAddr(config.getIpAddress()).setHostName(config.getHostName(false))
                .setPort(config.getNonSecurePort())
                .enablePort(InstanceInfo.PortType.UNSECURE,
                        config.isNonSecurePortEnabled())
                .setSecurePort(config.getSecurePort())
                .enablePort(InstanceInfo.PortType.SECURE, config.getSecurePortEnabled())
                .setVIPAddress(config.getVirtualHostName())
                .setSecureVIPAddress(config.getSecureVirtualHostName())
                .setHomePageUrl(config.getHomePageUrlPath(), config.getHomePageUrl())
                .setStatusPageUrl(config.getStatusPageUrlPath(),
                        config.getStatusPageUrl())
                .setHealthCheckUrls(config.getHealthCheckUrlPath(),
                        config.getHealthCheckUrl(), config.getSecureHealthCheckUrl())
                .setASGName(config.getASGName());
        //...
	}
}
/**实例化DiscoveryClient的一个子类 CloudEurekaClient*/
@Configuration
public class EurekaClientAutoConfiguration {
    @Configuration
    protected static class EurekaClientConfiguration {
        @Bean(destroyMethod = "shutdown")
        @ConditionalOnMissingBean(value = EurekaClient.class, search = SearchStrategy.CURRENT)
        public EurekaClient eurekaClient(ApplicationInfoManager manager, EurekaClientConfig config) {
            return new CloudEurekaClient(manager, config, this.optionalArgs,this.context);  //跟
        }
    }
}
/**跟 new CloudEurekaClient() 到 DiscoveryClient构造方法中，里面调用 initScheduledTasks()初始化定时任务*/
@Singleton
public class DiscoveryClient implements EurekaClient {
    @Inject
    DiscoveryClient(ApplicationInfoManager applicationInfoManager, EurekaClientConfig config, AbstractDiscoveryClientOptionalArgs args, Provider<BackupRegistry> backupRegistryProvider) {
        initScheduledTasks();
    }
    /**启动前 初始化*/
    private void initScheduledTasks() {
        if (clientConfig.shouldRegisterWithEureka()) {
            int renewalIntervalInSecs = instanceInfo.getLeaseInfo().getRenewalIntervalInSecs();
            int expBackOffBound = clientConfig.getHeartbeatExecutorExponentialBackOffBound();
            //心跳检测；开启 ScheduledExecutorService 定时任务
            scheduler.schedule(
                    new TimedSupervisorTask(
                            "heartbeat",
                            scheduler,
                            heartbeatExecutor,
                            renewalIntervalInSecs,
                            TimeUnit.SECONDS,
                            expBackOffBound,
                            new HeartbeatThread()       //跟 new HeartbeatThread()
                    ),
                    renewalIntervalInSecs, TimeUnit.SECONDS);
                //...
        }
    }
    private class HeartbeatThread implements Runnable {
        public void run() {
            if (renew()) {      //renew()表示刷新 跟
                lastSuccessfulHeartbeatTimestamp = System.currentTimeMillis();
            }
        }
    }
    boolean renew() {
        EurekaHttpResponse<InstanceInfo> httpResponse;
        try {
            httpResponse = eurekaTransport.registrationClient.sendHeartBeat(instanceInfo.getAppName(), instanceInfo.getId(), instanceInfo, null);
            if (httpResponse.getStatusCode() == 404) {      //向服务端获取 当前服务实例信息，如果返回404 表示服务端没有当前实例信息，则进入注册流程
                REREGISTER_COUNTER.increment();
                return register();      //register() 表示 eureka客户端 instance信息 注册到 eureka服务端
            }
            return httpResponse.getStatusCode() == 200;     //在eureka服务端获取到 当前实例信息，则返回200
        }catch (Exception e){}
    }
}
```


eureka配置类如下：
InstanceRegistryProperties，EurekaServerConfigBean，EurekaClientConfigBean，EurekaInstanceConfigBean，EurekaDashboardProperties；





@Deprecated
public DiscoveryClient(ApplicationInfoManager applicationInfoManager, final EurekaClientConfig config, DiscoveryClientOptionalArgs args) {
    //跟this()，代码如下：
    this(applicationInfoManager, config, (AbstractDiscoveryClientOptionalArgs) args);
}
this()代码：
@Inject
DiscoveryClient(ApplicationInfoManager applicationInfoManager, EurekaClientConfig config, AbstractDiscoveryClientOptionalArgs args, Provider<BackupRegistry> backupRegistryProvider) {
    if (args != null) {
        this.healthCheckHandlerProvider = args.healthCheckHandlerProvider;
        this.healthCheckCallbackProvider = args.healthCheckCallbackProvider;
        this.eventListeners.addAll(args.getEventListeners());
    } else {
        this.healthCheckCallbackProvider = null;
        this.healthCheckHandlerProvider = null;
    }

    this.applicationInfoManager = applicationInfoManager;
    InstanceInfo myInfo = applicationInfoManager.getInfo();

    clientConfig = config;
    staticClientConfig = clientConfig;
    transportConfig = config.getTransportConfig();
    instanceInfo = myInfo;
    if (myInfo != null) {
        appPathIdentifier = instanceInfo.getAppName() + "/" + instanceInfo.getId();
    } else {
        logger.warn("Setting instanceInfo to a passed in null value");
    }

    this.backupRegistryProvider = backupRegistryProvider;

    this.urlRandomizer = new EndpointUtils.InstanceInfoBasedUrlRandomizer(instanceInfo);
    localRegionApps.set(new Applications());

    fetchRegistryGeneration = new AtomicLong(0);

    remoteRegionsToFetch = new AtomicReference<String>(clientConfig.fetchRegistryForRemoteRegions());
    remoteRegionsRef = new AtomicReference<>(remoteRegionsToFetch.get() == null ? null : remoteRegionsToFetch.get().split(","));

    if (config.shouldFetchRegistry()) {
        this.registryStalenessMonitor = new ThresholdLevelsMetric(this, METRIC_REGISTRY_PREFIX + "lastUpdateSec_", new long[]{15L, 30L, 60L, 120L, 240L, 480L});
    } else {
        this.registryStalenessMonitor = ThresholdLevelsMetric.NO_OP_METRIC;
    }

    if (config.shouldRegisterWithEureka()) {
        this.heartbeatStalenessMonitor = new ThresholdLevelsMetric(this, METRIC_REGISTRATION_PREFIX + "lastHeartbeatSec_", new long[]{15L, 30L, 60L, 120L, 240L, 480L});
    } else {
        this.heartbeatStalenessMonitor = ThresholdLevelsMetric.NO_OP_METRIC;
    }

    logger.info("Initializing Eureka in region {}", clientConfig.getRegion());

    if (!config.shouldRegisterWithEureka() && !config.shouldFetchRegistry()) {
        logger.info("Client configured to neither register nor query for data.");
        scheduler = null;
        heartbeatExecutor = null;
        cacheRefreshExecutor = null;
        eurekaTransport = null;
        instanceRegionChecker = new InstanceRegionChecker(new PropertyBasedAzToRegionMapper(config), clientConfig.getRegion());

        // This is a bit of hack to allow for existing code using DiscoveryManager.getInstance()
        // to work with DI'd DiscoveryClient
        DiscoveryManager.getInstance().setDiscoveryClient(this);
        DiscoveryManager.getInstance().setEurekaClientConfig(config);

        initTimestampMs = System.currentTimeMillis();

        logger.info("Discovery Client initialized at timestamp {} with initial instances count: {}", initTimestampMs, this.getApplications().size());
        return;  // no need to setup up an network tasks and we are done
    }

    try {
        scheduler = Executors.newScheduledThreadPool(3,
                new ThreadFactoryBuilder()
                        .setNameFormat("DiscoveryClient-%d")
                        .setDaemon(true)
                        .build());

        heartbeatExecutor = new ThreadPoolExecutor(
                1, clientConfig.getHeartbeatExecutorThreadPoolSize(), 0, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new ThreadFactoryBuilder()
                        .setNameFormat("DiscoveryClient-HeartbeatExecutor-%d")
                        .setDaemon(true)
                        .build()
        );  // use direct handoff

        cacheRefreshExecutor = new ThreadPoolExecutor(
                1, clientConfig.getCacheRefreshExecutorThreadPoolSize(), 0, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new ThreadFactoryBuilder()
                        .setNameFormat("DiscoveryClient-CacheRefreshExecutor-%d")
                        .setDaemon(true)
                        .build()
        );  // use direct handoff

        eurekaTransport = new EurekaTransport();
        scheduleServerEndpointTask(eurekaTransport, args);

        AzToRegionMapper azToRegionMapper;
        if (clientConfig.shouldUseDnsForFetchingServiceUrls()) {
            azToRegionMapper = new DNSBasedAzToRegionMapper(clientConfig);
        } else {
            azToRegionMapper = new PropertyBasedAzToRegionMapper(clientConfig);
        }
        if (null != remoteRegionsToFetch.get()) {
            azToRegionMapper.setRegionsToFetch(remoteRegionsToFetch.get().split(","));
        }
        instanceRegionChecker = new InstanceRegionChecker(azToRegionMapper, clientConfig.getRegion());
    } catch (Throwable e) {
        throw new RuntimeException("Failed to initialize DiscoveryClient!", e);
    }

    if (clientConfig.shouldFetchRegistry() && !fetchRegistry(false)) {
        fetchRegistryFromBackup();
    }
    //初始化 定时任务；跟initScheduledTasks()
    initScheduledTasks();
    try {
        Monitors.registerObject(this);
    } catch (Throwable e) {
        logger.warn("Cannot register timers", e);
    }

    // This is a bit of hack to allow for existing code using DiscoveryManager.getInstance()
    // to work with DI'd DiscoveryClient
    DiscoveryManager.getInstance().setDiscoveryClient(this);
    DiscoveryManager.getInstance().setEurekaClientConfig(config);

    initTimestampMs = System.currentTimeMillis();
    logger.info("Discovery Client initialized at timestamp {} with initial instances count: {}",
            initTimestampMs, this.getApplications().size());
}
DiscoveryClient类在实例化后，开启了定时任务renew，DiscoveryClient#initScheduledTasks

/**Initializes all scheduled tasks.*/
private void initScheduledTasks() {
    ...
    if (clientConfig.shouldRegisterWithEureka()) {
        int renewalIntervalInSecs = instanceInfo.getLeaseInfo().getRenewalIntervalInSecs();
        int expBackOffBound = clientConfig.getHeartbeatExecutorExponentialBackOffBound();
        logger.info("Starting heartbeat executor: " + "renew interval is: " + renewalIntervalInSecs);
        // Heartbeat timer 心跳检测：每隔几秒，执行一次new HeartbeatThread()线程；
        scheduler.schedule(
                new TimedSupervisorTask(
                        "heartbeat",
                        scheduler,
                        heartbeatExecutor,
                        renewalIntervalInSecs,
                        TimeUnit.SECONDS,
                        expBackOffBound,
                        //跟 HeartbeatThread()构造方法
                        new HeartbeatThread()
                ),
                renewalIntervalInSecs, TimeUnit.SECONDS);
    ...
}
跟 new HeartbeatThread()；renew()方法发送心跳，返回404，代表没有注册，则进入注册流程，
private class HeartbeatThread implements Runnable {
    public void run() {
        //跟 renew()
        if (renew()) {
            lastSuccessfulHeartbeatTimestamp = System.currentTimeMillis();
        }
    }
}
DiscoveryClient#renew()：
boolean renew() {
    EurekaHttpResponse<InstanceInfo> httpResponse;
    try {
        httpResponse = eurekaTransport.registrationClient.sendHeartBeat(instanceInfo.getAppName(), instanceInfo.getId(), instanceInfo, null);
        //如果返回404，则eureka客户端 重新向eureka服务端注册；
        if (httpResponse.getStatusCode() == 404) {
            REREGISTER_COUNTER.increment();
            #跟 DiscoveryClient#register()方法
            return register();
        }
        return httpResponse.getStatusCode() == 200;
    } catch (Throwable e) {
        return false;
    }
}
至此，服务提供者已注册到服务中心；

4、服务中心怎么接收注册请求？
通过rest接口接收，通过jersey框架实现这些rest接口；通过@EnableEurekaServer这个注解开启 eureka server服务端；
初始化的过程：spring集成 eureka server原生包中 Jersey RESTful接口，Jersey中的Path就像spring中的 requestMapping；
；在eureka server初始化过程中，eureka server什么时候把 rest接口注册上去的？
Eureka托管github地址：https://github.com/Netflix/eureka；→ wiki → Eureka REST operations选项：
通过EurekaServerAutoConfiguration#jerseyApplication() 处理 eureka客户端注册请求：
@Bean
public javax.ws.rs.core.Application jerseyApplication(Environment environment, ResourceLoader resourceLoader) {
    ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false, environment);
    // Filter to include only classes that have a particular annotation.
    provider.addIncludeFilter(new AnnotationTypeFilter(Path.class));
    provider.addIncludeFilter(new AnnotationTypeFilter(Provider.class));
    // Find classes in Eureka packages (or subpackages)
    Set<Class<?>> classes = new HashSet<Class<?>>();
    //扫描 eureka下所有的包，包括com.netflix.discovery，com.netflix.eureka，扫描包中所有的类；
    for (String basePackage : EUREKA_PACKAGES) {
        Set<BeanDefinition> beans = provider.findCandidateComponents(basePackage);
        for (BeanDefinition bd : beans) {
            Class<?> cls = ClassUtils.resolveClassName(bd.getBeanClassName(),resourceLoader.getClassLoader());
            classes.add(cls);
        }
    }

    // Construct the Jersey ResourceConfig
    Map<String, Object> propsAndFeatures = new HashMap<String, Object>();
    propsAndFeatures.put(
            // Skip static content used by the webapp
            ServletContainer.PROPERTY_WEB_PAGE_CONTENT_REGEX,
            EurekaConstants.DEFAULT_PREFIX + "/(fonts|images|css|js)/.*");
    DefaultResourceConfig rc = new DefaultResourceConfig(classes);
    rc.setPropertiesAndFeatures(propsAndFeatures);
    return rc;
}
springcloud将 注册信息扫描出来，创建一个实例 javax.ws.rs.core.Application，该Application实际是 FilterRegistrationBean，EurekaServerAutoConfiguration#jerseyFilterRegistration()的返回值；
@Bean
public FilterRegistrationBean jerseyFilterRegistration(javax.ws.rs.core.Application eurekaJerseyApp) {
    //动态加载 filter，不需要xml配置filter了
    FilterRegistrationBean bean = new FilterRegistrationBean();
    bean.setFilter(new ServletContainer(eurekaJerseyApp));
    bean.setOrder(Ordered.LOWEST_PRECEDENCE);
    bean.setUrlPatterns(Collections.singletonList(EurekaConstants.DEFAULT_PREFIX + "/*"));
    return bean;
}

接收eureka 客户端请求：
注册时调用该方法：ApplicationResource#addInstance()，发送post请求时调用该 addInstance()方法，因为springcloud集成了 eureka中的 jersey；
@POST
@Consumes({"application/json", "application/xml"})
public Response addInstance(InstanceInfo info,@HeaderParam(PeerEurekaNode.HEADER_REPLICATION) String isReplication) {
    ......
}
受理请求：InstanceRegistry#register() 发布 EurekaInstanceRegisteredEvent事件，并调用父类方法 PeerAwareInstanceRegistryImpl#register()
集群同步：replicateToPeers；
最后调用 AbstractInstanceRegistry.registry()，将eureka客户端 发送过来的实例信息存在 ConcurrentHashMap中；

6、服务中心自身是怎么实现高可用的？ peer2peer 对等eureka server实例，各eureka server相互注册，共享实例信息；

7、服务集群之间怎么同步信息？如何去重？
eureka server初始化时，维护了一个 PeerEurekaNodes.peerEurekaNodeUrls对象，