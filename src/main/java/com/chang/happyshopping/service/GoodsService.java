package com.chang.happyshopping.service;

import com.chang.happyshopping.dao.GoodsDAO;
import com.chang.happyshopping.vo.GoodsVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

  @Autowired
  GoodsDAO goodsDAO;

  public List<GoodsVo> listGoodsVo(){
    return goodsDAO.listGoodsVo();
  }

  public GoodsVo getGoodsVoByGoodsId(long goodsId) {
    return goodsDAO.getGoodsVoByGoodsId(goodsId);
  }
}
