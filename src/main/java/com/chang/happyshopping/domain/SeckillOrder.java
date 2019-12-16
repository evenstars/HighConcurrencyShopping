package com.chang.happyshopping.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillOrder {
  private Long id;
  private Long userId;
  private Long orderId;
  private Long goodsId;
}
