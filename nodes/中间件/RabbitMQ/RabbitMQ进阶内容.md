## RabbitMQ进阶内容
----
### 1、过期时间（TTL）Time ti Live
在rabbitMQ中可以对消息或者队列设置TTL；
+ 对消息设置TTL（key = x-message-ttl）

&emsp;&emsp;如果不设置TTL表示消息不会过期，如果为0表示如果没有直接被使用则会过期；
```java
//通过参数
Map<String,Object> argss =new HashMap<String,Object>();
argss.put (”x-message-ttl”,6000);
channel.queueDeclare(queueName,durable,exclusive,autoDelete,argss);
```

+ 对队列设置TTL（key = x-expires）
+ 
&emsp;&emsp;如果不设置TTL表示队列不会过期，队列的过期时间不能设置为0,只能设置为比0大；
```java
Map<String,Object> args ＝new HashMap<String, Object>();
args.put (”x expires”,1800000);
channel.queueDeclare (”myqueue”,false,false,false,args);
```


### 2、死信队列（DLX) Dead-Letter-Exchange
&emsp;&emsp;当一条消息在一个队列中变成死信后会被发送到另一个交换器中，这个交换器就是死信交换器，与之对应的队列称为死信队列；

&emsp;&emsp;当消息被拒绝（Basic.Reject/Basic.Nack）、消息过期、队列达到最大长度时消息会变成死信；
x-dead-letter-exchange、x-dead-letter-routing-key
```java
channel exchangeDeclare(”dlx_exchange”,”direct”); 
Map<String,Object> args = new HashMap<String,Object>();
//指定死信交换器
args.put(”x-dead-letter-exchange”,”dlxexchange”);
//死信路由键，路由到的队列就是死信队列
args.put(”x-dead-letter-routing-key”,”dlx-routing-key”);
channel.queueDeclare(”myqueue”,false,false,false,args);
```



### 3、延迟队列
&emsp;&emsp;所谓延迟队列就是指当新消息被发送后并不想消费者能够立刻拿到消息，而是等待特定的时间后，消费者才能拿到这个信息进行消费

可以通过TTL+DLX模拟出延迟队列的功能：生产者在发送信息的时候通过设置不同的路由键，以此将消息发送到与交换器绑定的队列中，这些队列都设置了过期时间，同时也会配置死信交换器和死信队列。当相应丢弃消息过期是就会转存到相应的死信队列中，这样消费者根据业务需求，分别选择不同延迟等级的延迟队列（死信队列）进行消费。

### 4、优先级队列
&emsp;&emsp;优先级高的消息具备优先被消费的特权，通过队列的参数实现

&emsp;&emsp;key = x-max-priority
```java
Map<String,Object> args ＝new HashMap<String,Object>();
args.put (”x-ax-priority”,10);
channel.queueDeclare (”queue.priority”,true,false,false,args);
```
&emsp;&emsp;对于消费者的消费速度大于生产者的生产速度且Broker中午消息堆积情况的时候，对发送的消息设置优先级就没有意义了；

### 5、远程过程调用（RPC）Remote Procedure Call
&emsp;&emsp;它是一种通过网络从远程计算机上请求服务而不需要了解底层网络技术的手段；主要功能是让构建分布式更容易；通俗说就是假设有A.B两台服务器，一个应用部署在A上，另一个部署在B上，A想要调用B应用的一些方法，由于不在同一内存空间，不能直接调用，需要通过网络来表达调用的语义和传达调用的数据；

###### RabbitMQ中RPC的处理流程：
+ 当客户端启动时会创建一个匿名的回调队列，客户端会为RPC请求设置两个属性，分别用来告知RPC服务端回复请求时的目的队列（回调队列），并标记一个请求；
+ 之后请求会被发送到rpc_queue队列中
+ 当RPC度武器端监听到rpc_queue队列中有请求的时候，服务器会处理好并且返回结果消息到客户端的回调队列中
+ 客户端监听回调队列，当有消息时检查标记是否匹配，如果匹配则接收信息

### 6、持久化
&emsp;&emsp;持久化可以提高RabbitMQ的可靠性，防止在异常情况下数据丢失，例如重启，关闭，宕机等。
###### RabbitMQ的持久化分为三个部分
1. 交换器的持久化

&emsp;&emsp;在声明交换器时将durable参数设置为true，如果交换器不设置持久化那么在RabbitMQ服务重启之后，相关的交换器元数据会丢失，不过消息不会丢失，知识不能即将消息发送到这个交换器中了。
2. 队列的持久化

&emsp;&emsp;在声明队列时将durable参数设置为true，不持久化那么在服务重启之后消息肯定会丢失；
3. 消息的持久化

&emsp;&emsp;MessageProperties.PERSISTENT_TEXT_PLAIN这个属性封装了消息的持久化，将全部消息设置为持久化会严重影响RabbitMQ的性能，写入磁盘的速度比写入内存的速度会慢很多，对于可靠性不是那么高的消息可以不采用持久化，这样可以提高成体的吞吐量；

### 7、RabbitMQ的镜像队列机制
###### 镜像队列机制的作用是：
&emsp;&emsp;消息持久化的原理时将消息刷到磁盘中，首先会先写到缓存中，然后再写入磁盘，这个过程不是立即完成的，当在这段时间内RabbitMQ的服务节点发生问题的是后，消息还没有正在的落盘，那么这些消息将会丢失；

&emsp;&emsp;镜像队列就是用来解决这个问题的，其相当于设置了副本，如果主节点在某时间内挂了，可以自动切换到从节点，这样可以有效的保证高可用性，除非整个集群都挂；

&emsp;&emsp;除了使用镜像队列外还可以引入事务机制或者发送方确认机制来保证信息已经正确的存储到RabbitMQ中；

### 8、保证消息的存储机制
&emsp;&emsp;在RabbitMQ中消息的持久化可以解决服务器出现故障导致的消息丢失，但是消息持久化是一个宏观上的概念，其中在消息的传递过程中如果发生了故障，消息还未到达Broker中就丢失了；针对这个问题第一我们可以使用镜像队列，其次就是使用事务机制或者发送方确认机制；

##### &emsp;&emsp;1.事务机制

&emsp;&emsp;只有消息被Broker接收，事务才算提交成功，否则就会进行一个回滚操作榆次同时可以进行消息的重发；但是事务机制会带来严重的性能问题；
```java
channel.txSelect //将当前信道设置为事务模式
channel.txCommit //用于提交事务
channel.txRollback //用于回滚事务

channel.txSelect();
channel.basicPublish(EXCHANGE_NAME,ROUTING_KEY,
MessageProperties.PERSISTENT_TEXT_PLAIN,
”transaction messages”.getBytes());
channel.txCommit();
```

##### &emsp;&emsp;2. 发送方去确认机制
&emsp;&emsp;发送方确认机制是为了解决事务机制的性能问题的，因为事务机制会在客户端的RPC操作中在多加结果操作步骤，并且事务机制在发送一条消息之后会使发送方阻塞，等到接收到Broker的回应后才会释放阻塞，进行下一条消息的发送，这样当消息多的时候会降低消息的吞吐量；发送方确认机制是一种轻量级的方式，并且它是异步执行的；

&emsp;&emsp;生产者将信道设置支撑confirm（确认）模式，一旦信道变成了confirm模式所有在此信道上发布的消息都会被指派一个唯一ID（默认从1开始），一旦消息被发送到匹配的队列之后RabbitMQ就会发送一个确认（Basic.Ack）给生产者（其中包含消息的唯一ID），如果消息和队列是持久化的，那么确认消息会在消息写入磁盘之后发出；保证了消息能正常的落盘。

### 9、消息的发送确认机制对比
###### 对比包括
1. 事务

2. 多条消息使用循环

3. 普通confirm（信道变为confirm模式）

4. 批量confirm（多条信息版的普通版本，多信息一起回复确认信息）

5. 异步confirm（通过一个SortedSet集合实现）

6. 批量和异步的吞吐量好，不过需要在客户端维护状态；

### 10、消费者接收消息是的注意点
###### 消息分发
&emsp;&emsp;当队列有多个消费者时，队列默认以轮询的分发方式发送个给消费者，每条消息只会发送给订阅列表里的一个消费者；在某些时候消费者的性能不高那么可以设置一个分发的权重，例如有些消费者的处理消息的能力比较弱，那么Broker就会给他在轮询的基础上少分配消息
```java
channel.basicQos(
    int prefetchSize, //消费者所能接收的未确认消息的总体上限大小，设置为0时表示无上限
    int prefetchCount, //
    boolean global//最好别用，会产生负载
)
```

###### 消息顺序性在一些情况下消息的顺序性会被打破
1. 事务回滚
2. 服务器故障，消息为落盘会发生消息的补偿发送（异步执行：其他线程发送）
3. 设置了优先级
4. 消费者端的轮询调度，处理速度不同，接收速度相同

### 11、消息传输保障
###### 有三个等级
1. 最多一次，消息只会传输一次，不重复传输   
2. 最少一次，消息绝不会丢失，但可能会重复传输
3. 恰好一次，每条消息肯定会传输一次
