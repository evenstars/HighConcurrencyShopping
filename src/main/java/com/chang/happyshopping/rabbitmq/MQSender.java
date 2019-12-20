package com.chang.happyshopping.rabbitmq;

import com.chang.happyshopping.redis.RedisService;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {
  @Autowired
  AmqpTemplate amqpTemplate;


  public void send(Object message){
    String msg = RedisService.beanToString(message);
    amqpTemplate.convertAndSend(MQConfig.QUEUE,msg);
  }
}
