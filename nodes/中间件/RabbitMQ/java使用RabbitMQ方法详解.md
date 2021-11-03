## java使用RabbitMQ方法详解
----
### 1、Connection
&emsp;&emsp;Connection可以用来创建多个Channel实例，但是Channel是属于线程本地变量，不能再线程间共享，应用程序应该为每个线程开辟一个Channel；

### 2、交换器和队列
&emsp;&emsp;交换器和队列是AMQP的高级层面的构建模块，在使用前需要先声明他们（Declare）
```java
//非持久化队列，有RabbitMQ自动生成 channel.queueDeclare().getQueue();
channel.exchangeDeclare(exchangeName, ” direct”, true);
String queueName = channel.queueDeclare().getQueue();
channel . queueBind(queueName,exchangeName, routingKey);

//持久化队列
channel.exchangeDeclare(exchangeName, ” direct”, true) ;
channel.queueDeclare(queueName, true, false, false, null);
channel.queueBind(queueName, exchangeName, routingKey) ;
```

#### 2-1、exchangeDeclare()方法
```java
DeclareOk exchangeDeclare(
        String exchangeName, //交换器名称
        BuiltinExchangeType type, //交换器类型
        boolean durable, //是否持久化，持久化可以存盘，重启不丢失信息
        boolean autoDelete, //是否自动删除，自动解除绑定
        boolean internal, //是否是内置交换器，客户端程序无法直接发送信息到内置交换器中，只能通过家换气路由到交换器这种方式
        Map<String, Object> arguments//其他的一些结构化参数
) throws IOException;
```

#### 2-2、queueDeclare()方法
```java
com.rabbitmq.client.AMQP.Queue.DeclareOk queueDeclare
        (String queue, //队列名
        boolean durable, //是否持久化，持久化会存盘
        boolean exclusive, //是否排他，排他是针对线程，只对首次声明的线程可见，在连接断开后会自动删除
        boolean autoDelete, //是否自动删除
        Map<String, Object> arguments //其他参数，以map的格式存储，KEY为定义好的，查阅文档可知
) throws IOException;
```

#### 2-3、queueBind()方法
```java
com.rabbitmq.client.AMQP.Queue.BindOk queueBind(
        String queue, //队列名
        String exchange, //交换器名
        String routingKey, //路由键
        Map<String, Object> argument
) throws IOException;
```

#### 2-4、exchangeBind()方法
```java
BindOk exchangeBind(
        String exchange1, 
        String exchang2, 
        String routingKey, 
        Map<String, Object> arguments
) throws IOException;
```


#### 2-5、basicPublish()方法：用于消息的发布
```java
void basicPublish(
        String exchange, //交换器名
        String routingKey, //路由键
        /* mandatory
        * 为true时，若无法匹配到对应的队列那么会调用return()将消息返回给生产者
        * 为false时，会直接丢弃消息
        */
        boolean mandatory, 
        /* immediate
        * 这个属性在高版本的RabbitMQ中已经被去除
        */
        boolean immediate, 
        BasicProperties props, //消息的基本属性集，有14个属性成员
        byte[] body //消息体，正在需要发送的消息，字节码形式 message.getBytes()
) throws IOException;
```


#### 2-6、basicAck()方法
&emsp;&emsp;用于消息的确认与拒绝，true表示未删除重入队列，false表示可以删除，该方法只能针对一条消息，多条使用basicNack；
```java
void basicAck(
        long deliveryTag, //消息的编号
        boolean requeue //若为true则RabbitMQ会重新将这条消息存入队列发送给下一个订阅的消费者，若为false则证明已经处理完成可以删除
) throws IOException;
```

### 3、关闭连接
&emsp;&emsp;关闭连接是针对Connection和Channel的，在AMQP协议中他们采用了同样的方式起来管理网络失败、内部错误和显示关闭连接，生命周期相同；

### 4、生命周期：
+ open：开启
+ closing：正在关闭，显式的调用了close方法来关闭Connection或者Channel
+ closed：已经关闭