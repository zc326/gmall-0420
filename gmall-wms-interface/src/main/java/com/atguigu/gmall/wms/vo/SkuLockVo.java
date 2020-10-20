package com.atguigu.gmall.wms.vo;

import lombok.Data;

@Data
public class SkuLockVo {
    private Long skuId;
    private Integer count;
    private Boolean lock;
    private Long wareSkuId;
}
