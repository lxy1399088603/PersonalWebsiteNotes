## Rabbit基本使用
----
### 1、生产者和消费者的简单案例

&emsp;&emsp;首先需要一个创建一个公共连接类，连接池可以创建信道，信息的传递是依赖于信道，因此不管是发信息还是接收信息都需依赖于信道,所以该类会为调用者返回一个可用的信道；
```java
public class Common {
    public static final String EXCHANGE_NAME = "交换机名";
    public static final String ROUTING_KEY = "路由键";
    public static final String QUEUE_NAME ="队列名";
    private static final String IP_ADDRESS = "101.34.171.190";
    private static final int PORT= 5672;//RabbitMQ 服务端默认端口号为 5672
    public static connection Common() throws IOException, TimeoutException {
         //创建一个连接工厂，类似一个线程池，其中存放的是信道
         ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        factory.setUsername("rabbitmq");
        factory.setPassword("rabbitmq");
        //创建一个新连接
        Connection connection = factory.newConnection();
    }

}
```

&emsp;&emsp;生产者：通过从控制台的输入来产生消息，通过信道创建一个交换机和一个队列在Broke(RabbitMQ)中，然后将交换机和队列通过给定的路由键进行绑定，接收到用户输入后通过basicPublish()将消息发送到家换季中，然后通过路由键将消息存放到对应的队列中，消息以字节码的信息存放；
```java
public class RabbitProducer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = Common.Common();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(Common.EXCHANGE_NAME,"fanout",true,false,null);
        channel.queueDeclare().getQueue();
        channel.queueBind(Common.QUEUE_NAME,Common.EXCHANGE_NAME,Common.ROUTING_KEY);
        Scanner in = new Scanner(System.in);
        while(in.hasNext()){
            String message = in.next();
            channel.basicPublish(Common.EXCHANGE_NAME,
                    Common.ROUTING_KEY, 
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes());
                    
            System.out.println("消费者发出信息："+message);
        }
        channel.close();
        connection.close();
    }
}
```

&emsp;&emsp;消费者：消费者的话是多个，消费者同样需要拿到一个连接，然后通过连接生成信道，通过队列名和消费的操作进行绑定去接收消息；
```java
public class RabbitConsumer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = Common.Common();
        final Channel channel = connection.createChannel();
        
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException{
                System.out.println ("recv message : "+ new String(body));
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                channel.basicAck(envelope.getDeliveryTag(),false);
            }

        };
        channel.basicConsume(Common.QUEUE_NAME,false,consumer);
        TimeUnit.SECONDS.sleep(5);
        channel.close();
        connection.close();
    }
}
```

### 2、RabbitMQ组件概念
+ 消息：消息一般包含两个部分，消息体和标签（Label），消息体一般是一个带有业务逻辑结构的数据，生产者把消息交给RabbitMQ,RabbitMQ会根据标签把消息发送给感兴趣的消费者；

+ Broker：消息中间件的服务节点，可以看作是一个RabbltMQ服务实例或看作是一台RabbitMQ的服务器，其中包含了交换机，各种队列；

+ 路由键RoutingKey：生产者将消息发送到交换器的时候是使用路由键进行绑定的

+ 绑定键BindingKey：是将交换器与队列进行绑定

### 3、交换器的类型
1. fanout扇出模式：会把发送到交换器上的所有信息发布到所有与交换器绑定的Queue中
2. direct直接模式：消息只会发送到由Routing和Binding对应匹配的Queue中
3. topic主题模式：模糊匹配的直接模式，通过“*”和“#”来进行匹配，前n后1
4. headers：性能差不实用


### 4、AMQP协议
&emsp;&emsp;AMQP的模型架构和RabbitMQ的模型架构是一样的，RabbitMQ中的交换器、队列、绑定、路由键都是遵循AMQP协议中相应的概念，就好比JDK中的方法最底层使用的是C或C++来实现对的；