package com.atguigu.gmall.cart.controller;

import com.atguigu.gmall.cart.interceptor.LoginInterceptor;
import com.atguigu.gmall.cart.pojo.Cart;
import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.bean.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public String addCart(Cart cart){
        this.cartService.addCart(cart);
        return "redircet://cart.gmall.com/addCart.html?skuId=" + cart.getSkuId();
    }

    @GetMapping("addCart.html")
    public String queryCartBySkuId(@RequestParam("skuId") Long skuId, Model model){
        Cart cart = this.cartService.queryCartBySkuId(skuId);
        model.addAttribute("cart",cart);
        return "addCart";
    }

    @GetMapping("cart.html")
    public String queryCarts(Model model){
        List<Cart> carts = this.cartService.queryCarts();
        model.addAttribute("carts",carts);
        return "carts";
    }

    @PostMapping("updateNum")
    public ResponseVo updateNum(@RequestBody Cart cart){
        this.cartService.updateNum(cart);
        return ResponseVo.ok(cart);
    }

    @PostMapping("deleteCart")
    @ResponseBody
    public ResponseVo deleteCartBySkuId(@RequestParam("skuId") Long skuId){
        this.cartService.deleteCartBySkuId(skuId);
        return ResponseVo.ok();
    }

    @GetMapping("test")
    @ResponseBody
    public String test(HttpServletRequest request) throws ExecutionException, InterruptedException {
        //System.out.println(LoginInterceptor.getUserInfo());
        long now = System.currentTimeMillis();
        this.cartService.executor1();
        //ListenableFuture<String> future2 = this.cartService.executor2();
//        future1.addCallback(value -> System.out.println("调用成功：" + value),
//                ex -> System.out.println("调用失败：" + ex.getMessage()));
//        future2.addCallback(value -> System.out.println("调用成功：" + value),
//                ex -> System.out.println("调用失败：" + ex.getMessage()));
        System.out.println(System.currentTimeMillis() - now);
        return "hello interceptor!";
    }

    @GetMapping("checked/{userId}")
    @ResponseBody
    public ResponseVo<List<Cart>> queryCheckedCartsByUserId(@PathVariable("userId")Long userId){
        List<Cart> carts = this.cartService.queryCheckedCartsByUserId(userId);
        return ResponseVo.ok(carts);
    }
}
