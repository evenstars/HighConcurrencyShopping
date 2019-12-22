package com.chang.happyshopping.controller;

import com.chang.happyshopping.access.AccessLimit;
import com.chang.happyshopping.domain.HappyShoppingUser;
import com.chang.happyshopping.domain.OrderInfo;
import com.chang.happyshopping.domain.SeckillOrder;
import com.chang.happyshopping.rabbitmq.MQSender;
import com.chang.happyshopping.rabbitmq.SeckillMessage;
import com.chang.happyshopping.redis.GoodsKey;
import com.chang.happyshopping.redis.RedisService;
import com.chang.happyshopping.result.CodeMsg;
import com.chang.happyshopping.result.Result;
import com.chang.happyshopping.service.GoodsService;
import com.chang.happyshopping.service.HappyShoppingUserService;
import com.chang.happyshopping.service.OrderService;
import com.chang.happyshopping.service.SeckillService;
import com.chang.happyshopping.vo.GoodsVo;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

  @Autowired
  HappyShoppingUserService userService;

  @Autowired
  RedisService redisService;

  @Autowired
  GoodsService goodsService;

  @Autowired
  OrderService orderService;

  @Autowired
  SeckillService seckillService;

  @Autowired
  MQSender sender;

  private Map<Long,Boolean> localOverMap = new HashMap<>();

  @RequestMapping("/do_seckill")
  @ResponseBody
  public String doSeckill(Model model, HappyShoppingUser user,
                          @RequestParam("goodsId")long goodsId){
    if (user==null)
      return "login";
    //determine stock
    GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    int stock = goods.getStockCount();
    if (stock<=0){
      model.addAttribute("errmsg",CodeMsg.STOCK_EMPTY.getMsg());
      return "miaosha_fail";
    }
    //is successful
    SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(),goods.getId());
    if (order!=null){
      model.addAttribute("errmsg",CodeMsg.REPEAT_PURCHASE);
      return "miaosha_fail";
    }

    //1.decrease stock 2. make order 3. write order in a transaction
    OrderInfo orderInfo = seckillService.kill(user,goods);
    model.addAttribute("orderInfo",orderInfo);
    model.addAttribute("goods",goods);
    return "order_detail";
  }

  // get: every time result is equali : post:influence the data of server
  @RequestMapping(value = "/{path}/do_seckill2",method = RequestMethod.POST)
  @ResponseBody
  public Result<Integer> doSeckill(HappyShoppingUser user,
                                   @RequestParam("goodsId")long goodsId,
                                   @PathVariable("path")String path){
    if (user==null)
      return Result.error(CodeMsg.SERVER_ERROR);
    boolean pathCheck = seckillService.checkService(user,goodsId,path);
    if (!pathCheck)
      return Result.error(CodeMsg.REQUEST_ILLEGAL);
    Boolean isOver = localOverMap.get(goodsId);
    if (!isOver)
      return Result.error(CodeMsg.STOCK_EMPTY);
    //decrease stock
    long stock = redisService.decrease(GoodsKey.getSeckillGoodsStock,""+goodsId);
    if (stock< 0 ){
      localOverMap.put(goodsId,true);
      return Result.error(CodeMsg.STOCK_EMPTY);
    }
    SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(),goodsId);
    if (order!=null)
      return Result.error(CodeMsg.REPEAT_PURCHASE);

    //send to queue
    SeckillMessage message = new SeckillMessage();
    message.setUser(user);
    message.setGoodsId(goodsId);
    sender.sendSeckillMessage(message);

    return Result.success(0);
//    //determine stock
//    GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//    int stock = goods.getStockCount();
//    if (stock<=0){
//      return Result.error(CodeMsg.STOCK_EMPTY);
//    }
//    //is successful
//    SeckillOrder order = orderService.getSeckillOrderByUserIdGoodsId(user.getId(),goods.getId());
//    if (order!=null){
//      return Result.error(CodeMsg.REPEAT_PURCHASE);
//    }
//
//    //1.decrease stock 2. make order 3. write order in a transaction
//    OrderInfo orderInfo = seckillService.kill(user,goods);

//    return Result.success(orderInfo);
  }

  //after initialization
  @Override
  public void afterPropertiesSet() throws Exception {
    List<GoodsVo> goodsList= goodsService.listGoodsVo();
    if (goodsList==null )
      return;
    for (GoodsVo good:goodsList){
      redisService.set(GoodsKey.getSeckillGoodsStock,""+good.getId(),good.getGoodsStock());
      localOverMap.put(good.getId(),false);
    }
  }

  // 1:failed 0:still in queue orderId: success
  @RequestMapping(value = "/result",method = RequestMethod.GET)
  @ResponseBody
  public Result<Long> getResult(HappyShoppingUser user,
                          @RequestParam("goodsId")long goodsId){
    if (user == null)
      return Result.error(CodeMsg.MOBILE_NOT_EXIST);
    long result = seckillService.getSeckillResult(user.getId(),goodsId);
    return Result.success(result);
  }

  @AccessLimit(seconds = 5,maxCount=5,needLogin=true)
  @RequestMapping(value = "/path",method = RequestMethod.GET)
  @ResponseBody
  public Result<String> getPath(HttpServletRequest request,
                                HappyShoppingUser user,
                                @RequestParam("goodsId")long goodsId,
                                 @RequestParam("veryfyCode")int veryfyCode) {
    if (user == null)
      return Result.error(CodeMsg.SESSION_ERROR);
    //rate limiter
//    String url = request.getRequestURI();
//    String key = url+"_"+user.getId();
//    Integer count = redisService.get(AccessKey.access,key,Integer.class);
//    if (count == null){
//      redisService.set(AccessKey.access,key,1);
//    }
//    else if (count<5)
//      redisService.increase(AccessKey.access,key);
//    else
//      return Result.error(CodeMsg.ACCESS_LIMIT_MAX);
    boolean checkVerifyCode = seckillService.checkVerifyCode(user,goodsId,veryfyCode);
    if (!checkVerifyCode)
      return Result.error(CodeMsg.REQUEST_ILLEGAL);
    String path = seckillService.creatSeckillPath(user,goodsId);
    return Result.success(path);
  }

  @RequestMapping(value = "verifyCode",method = RequestMethod.GET)
  @ResponseBody
  public Result<String> verifyCode(HttpServletResponse response,@RequestParam("goodsId") long goodsId, HappyShoppingUser user){
    if (user==null)
      return Result.error(CodeMsg.SESSION_ERROR);
    BufferedImage image = seckillService.createVerifyCode(goodsId,user);
    try{
      OutputStream out = response.getOutputStream();
      ImageIO.write(image,"JPEG",out);
      out.flush();
      out.close();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return Result.error(CodeMsg.SERVER_ERROR);
  }
}
