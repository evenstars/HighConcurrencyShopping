package com.chang.happyshopping.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class MQConfig {
  public static final String SECKILL_QUEUE = "queue";
  public static final String QUEUE = "queue";
  public static final String TOPIC_QUEUE1 = "topic_queue1";
  public static final String TOPIC_QUEUE2 = "topic_queue2";
  public static final String HEADER_QUEUE = "header_queue";
  public static final String TOPIC_EXCHANGE = "topic_exchange";
  public static final String ROUTIN_KEY1 = "topic_KEY1";
  public static final String ROUTIN_KEY2 = "topic_#";
  public static final String FANOUT_EXCHANGE = "FANOUT_EXCHANGE";
  public static final String HEADER_EXCHANGE = "HEADER_EXCHANGE";

  // direct model
  @Bean
  public Queue queue(){
    return new Queue("queue",true);
  }

  // switch model
  @Bean
  public Queue topicQueue1(){
    return new Queue(TOPIC_QUEUE1,true);
  }

  @Bean
  public Queue topicQueue2(){
    return new Queue(TOPIC_QUEUE2,true);
  }

  @Bean
  public TopicExchange topicExchange(){
    return new TopicExchange(TOPIC_EXCHANGE);
  }

  @Bean
  public Binding topicBinding(){
    return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with(ROUTIN_KEY1);
  }

  @Bean
  public Binding topicBinding2(){
    return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with(ROUTIN_KEY2);
  }

  //fanout
  @Bean
  public FanoutExchange fanoutExchange(){
    return new FanoutExchange(FANOUT_EXCHANGE);
  }

  @Bean
  public Binding fanoutBinding1(){
    return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
  }

  @Bean
  public Binding fanoutBinding2(){
    return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
  }

  @Bean
  public HeadersExchange headerExchange(){
    return new HeadersExchange(HEADER_QUEUE);
  }

  @Bean
  public Queue headerQueue(){
    return new Queue(HEADER_QUEUE,true);
  }

  @Bean
  public Binding headerBinding(){
    Map<String,Object> map = new HashMap<>();
    map.put("header1","value1");
    map.put("header2","value2");
    return BindingBuilder.bind(headerQueue()).to(headerExchange()).whereAll(map).match();
  }
}
