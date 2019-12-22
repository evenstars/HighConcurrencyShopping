package com.chang.happyshopping.service;

import com.chang.happyshopping.dao.GoodsDAO;
import com.chang.happyshopping.domain.Goods;
import com.chang.happyshopping.domain.HappyShoppingUser;
import com.chang.happyshopping.domain.OrderInfo;
import com.chang.happyshopping.domain.SeckillOrder;
import com.chang.happyshopping.redis.RedisService;
import com.chang.happyshopping.redis.SeckillKey;
import com.chang.happyshopping.utils.MD5Util;
import com.chang.happyshopping.utils.UUIDUtil;
import com.chang.happyshopping.vo.GoodsVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

@Service
public class SeckillService {

  @Autowired
  GoodsService goodsService;

  @Autowired
  OrderService orderService;

  @Autowired
  RedisService redisService;

  public BufferedImage createVerifyCode(long goodsId,HappyShoppingUser user) {
    if (goodsId<0 || user == null)
      return null;
    int width = 80;
    int height = 32;
    //create the image
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics g = image.getGraphics();
    // set the background color
    g.setColor(new Color(0xDCDCDC));
    g.fillRect(0, 0, width, height);
    // draw the border
    g.setColor(Color.black);
    g.drawRect(0, 0, width - 1, height - 1);
    // create a random instance to generate the codes
    Random rdm = new Random();
    // make some confusion
    for (int i = 0; i < 50; i++) {
      int x = rdm.nextInt(width);
      int y = rdm.nextInt(height);
      g.drawOval(x, y, 0, 0);
    }
    // generate a random code
    String verifyCode = generateVerifyCode(rdm);
    g.setColor(new Color(0, 100, 0));
    g.setFont(new Font("Candara", Font.BOLD, 24));
    g.drawString(verifyCode, 8, 24);
    g.dispose();
    //把验证码存到redis中
    int rnd = calc(verifyCode);
    redisService.set(SeckillKey.getSeckillVerifyCode, user.getId()+","+goodsId, rnd);
    //输出图片
    return image;
  }
  private static char[] ops = new char[] {'+', '-', '*'};
  /**
   * + - *
   * */
  private String generateVerifyCode(Random rdm) {
    int num1 = rdm.nextInt(10);
    int num2 = rdm.nextInt(10);
    int num3 = rdm.nextInt(10);
    char op1 = ops[rdm.nextInt(3)];
    char op2 = ops[rdm.nextInt(3)];
    String exp = ""+ num1 + op1 + num2 + op2 + num3;
    return exp;
  }

  private static int calc(String exp) {
    try {
      ScriptEngineManager manager = new ScriptEngineManager();
      ScriptEngine engine = manager.getEngineByName("JavaScript");
      return (Integer)engine.eval(exp);
    }catch(Exception e) {
      e.printStackTrace();
      return 0;
    }
  }

  public boolean checkVerifyCode(HappyShoppingUser user, long goodsId, int verifyCode) {
    if(user == null || goodsId <=0) {
      return false;
    }
    Integer codeOld = redisService.get(SeckillKey.getSeckillVerifyCode, user.getId()+","+goodsId, Integer.class);
    if(codeOld == null || codeOld - verifyCode != 0 ) {
      return false;
    }
    redisService.delete(SeckillKey.getSeckillVerifyCode, user.getId()+","+goodsId);
    return true;
  }

  public boolean checkService(HappyShoppingUser user, long goodsId, String path) {
    if (user == null || path==null)
      return false;
    String pathOld = redisService.get(SeckillKey.getSeckillPath,user.getId()+"_"+goodsId,String.class);
    return path.equals(pathOld);
  }

  public long getSeckillResult(Long userId, long goodsId) {
    SeckillOrder order=orderService.getSeckillOrderByUserIdGoodsId(userId,goodsId);
    if (order!=null){
      //success
      return order.getOrderId();
    }
    else if (getGoodsOver(goodsId)){
      return -1;
    }
    else
      return 0;
  }

  private boolean getGoodsOver(long goodsId) {
    return redisService.exists(SeckillKey.isGoodOver,""+goodsId);
  }

  private void setGoodsOver(long goodsId){
    redisService.set(SeckillKey.isGoodOver,""+goodsId,true);
  }

  @Transactional
  public OrderInfo kill(HappyShoppingUser user, GoodsVo goods) {
    //decrease stock
    boolean result = goodsService.reduceStock(goods);
    if (result){
      return orderService.createOrder(user,goods);
    }
    else{
      setGoodsOver(goods.getId());
      return null;
    }
  }

  public String creatSeckillPath(HappyShoppingUser user,long goodsId) {
    String path = MD5Util.md5(UUIDUtil.uuid()+"123456");
    redisService.set(SeckillKey.getSeckillPath,""+user.getId()+"_"+goodsId,path);
    return path;
  }
}
