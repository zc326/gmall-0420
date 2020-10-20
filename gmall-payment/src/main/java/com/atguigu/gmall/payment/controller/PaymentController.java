package com.atguigu.gmall.payment.controller;

import com.atguigu.gmall.common.bean.UserInfo;
import com.atguigu.gmall.common.exception.OrderException;
import com.atguigu.gmall.oms.entity.OrderEntity;
import com.atguigu.gmall.payment.interceptor.LoginInterceptor;
import com.atguigu.gmall.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("pay.html")
    public String toPay(@RequestParam("orderToken")String orderToken, Model model){

        OrderEntity orderEntity = this.paymentService.queryOrderByToken(orderToken);
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        Long userId = userInfo.getUserId();
        if (orderEntity == null || orderEntity.getUserId() != userId || orderEntity.getStatus() != 0){
            throw new OrderException("非法参数");
        }

        model.addAttribute("orderEntity", orderEntity);
        return "pay";
    }
}
