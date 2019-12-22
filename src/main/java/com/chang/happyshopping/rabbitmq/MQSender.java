package com.chang.happyshopping.rabbitmq;

import com.chang.happyshopping.redis.RedisService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {
  @Autowired
  AmqpTemplate amqpTemplate;

  private static Logger log = LoggerFactory.getLogger(MQSender.class);

//  public void send(Object message){
//    String msg = RedisService.beanToString(message);
//    amqpTemplate.convertAndSend(MQConfig.QUEUE,msg);
//  }
//
//  public void sendTopic(Object message){
//    String msg = RedisService.beanToString(message);
//    log.info("send topic "+ msg);
//    amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,MQConfig.ROUTIN_KEY1,msg+'1');
//    amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,MQConfig.ROUTIN_KEY2,msg+'2');
//  }
//
//  public void sendFanout(Object message){
//    String msg = RedisService.beanToString(message);
//    log.info("send topic "+ msg);
//    amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",msg+'1');
//  }
//
//  public void sendHeader(Object message){
//    String msg = RedisService.beanToString(message);
//    log.info("send head"+msg);
//    MessageProperties properties = new MessageProperties();
//    properties.setHeader("header1","value1");
//    properties.setHeader("header2","value2");
//    Message obj = new Message(msg.getBytes(),properties);
//    amqpTemplate.convertAndSend(MQConfig.HEADER_EXCHANGE,"",obj);
//  }

  public void sendSeckillMessage(SeckillMessage message) {
    String msg = RedisService.beanToString(message);
    log.info("send message+"+msg);
    amqpTemplate.convertAndSend(MQConfig.SECKILL_QUEUE,msg);
  }
}
