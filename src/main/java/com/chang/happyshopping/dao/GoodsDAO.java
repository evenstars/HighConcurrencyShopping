package com.chang.happyshopping.dao;

import com.chang.happyshopping.vo.GoodsVo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface GoodsDAO {

  @Select("select g.*, sg.stock_count,sg.start_date,sg.end_date,sg.miaosha_price from seckill_goods sg left join goods g on sg.goods_id = g.id")
  List<GoodsVo> listGoodsVo();

  @Select("select g.*, sg.stock_count,sg.start_date,sg.end_date,sg.miaosha_price from seckill_goods sg left join goods g on sg.goods_id = g.id where g.id =#{goodsId}")
  GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);
}
