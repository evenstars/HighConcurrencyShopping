package com.chang.happyshopping.dao;

import com.chang.happyshopping.domain.User;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserDao {

  @Select("select * from user where id = #{id}")
  User getById(@Param("id")int id);

  @Insert("insert into user(id,name) values(#{id},#{name})")
  int insert(User user);
}
