package gulimall.order.config;

import org.springframework.amqp.core.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * rabbitMq的队列交换机配置
 * @author 孙启新
 * <br>FileName: MyRabbitMqConfig
 * <br>Date: 2020/08/12 11:07:22
 */
@Configuration
public class MyRabbitMqConfig {
    /**
     * 交换机名称
     */
    public static final String ORDER_EVENT_EXCHANGE = "order_event_exchange";
    /**
     * 延时队列名称
     */
    public static final String ORDER_DELAY_QUEUE= "order_delay_queue";
    /**
     * 死信队列名称
     */
    public static final String ORDER_DEAD_QUEUE= "order_dead_queue";
    /**
     * 路由到延时队列使用的路由键
     */
    public static final String ORDER_DELAY_KEY= "order_create_order";
    /**
     * 路由到死信队列使用的路由键
     */
    public static final String ORDER_DEAD_KEY = "order_dead_key";

    /**
     * 创建一个交换机
     * @return 交换机
     */
    @Bean
    public Exchange orderEventExchange(){
        return ExchangeBuilder.topicExchange(ORDER_EVENT_EXCHANGE).durable(true).build();
    }

    /**
     * 创建一个延时队列
     * @return 延时队列
     */
    @Bean
    public Queue orderDelayQueue(){
        return QueueBuilder.durable(ORDER_DELAY_QUEUE).ttl(60000).deadLetterExchange(ORDER_EVENT_EXCHANGE).deadLetterRoutingKey(ORDER_DEAD_KEY).build();
    }

    /**
     * 创建一个死信队列用于接收延时队列过期的消息
     * @return 死信队列
     */
    @Bean
    public Queue orderDeadQueue(){
        return QueueBuilder.durable(ORDER_DEAD_QUEUE).build();
    }

    /**
     * 将交换机与延时队列绑定
     * @return Binding
     */
    @Bean
    public Binding bindingDelayQueue(){
        return new Binding(ORDER_DELAY_QUEUE, Binding.DestinationType.QUEUE,ORDER_EVENT_EXCHANGE,ORDER_DELAY_KEY,null);
    }

    /**
     * 将交换机与死信队列绑定
     * @return Binding
     */
    @Bean
    public Binding bindingDeadQueue(){
        return new Binding(ORDER_DEAD_QUEUE, Binding.DestinationType.QUEUE,ORDER_EVENT_EXCHANGE,ORDER_DEAD_KEY,null);
    }
}
