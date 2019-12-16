package com.chang.happyshopping.dao;

import com.chang.happyshopping.domain.HappyShoppingUser;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface HappyShoppingUserDAO {

  @Select("select * from happyshopping_user where id = #{id}")
  public HappyShoppingUser getById(@Param("id") long id);
}
