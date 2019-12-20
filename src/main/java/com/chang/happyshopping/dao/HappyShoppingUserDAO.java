package com.chang.happyshopping.dao;

import com.chang.happyshopping.domain.HappyShoppingUser;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface HappyShoppingUserDAO {

  @Select("select * from happyshopping_user where id = #{id}")
  HappyShoppingUser getById(@Param("id") long id);

  @Update("update happyshopping_user set password = #{pawword} where id = #{id}")
  void update(HappyShoppingUser user);
}
