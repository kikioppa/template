package com.example.controller;

import com.example.domain.KakaoPayReadyVO;
import com.example.service.KakaoPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.Setter;
import lombok.extern.java.Log;
 
@Log
@Controller
public class SampleController {

	
    @Setter(onMethod_ = @Autowired)
    private KakaoPay kakaopay;

    @GetMapping("/kakaoPay")
    public void kakaoPayGet() {
        //kakaoPay.html이 없어서 에러 날 것
    }
    
    @PostMapping("/kakaoPay")
    public String kakaoPay( KakaoPayReadyVO  kakaoPayReadyVO) {
        log.info("kakaoPay post............................................"+kakaoPayReadyVO);
       String nextpage= kakaopay.kakaoPayReady(kakaoPayReadyVO);
        log.info("nextpage=====>"+nextpage);
        return "redirect:" + nextpage;
 
    }
    
    @GetMapping("/kakaoPaySuccess")
    public void kakaoPaySuccess(@RequestParam("pg_token") String pg_token, String total_amount, Model model) {
        log.info("kakaoPaySuccess get............................................");
        log.info("kakaoPaySuccess pg_token : " + pg_token+"  :  "+total_amount);
        
        model.addAttribute("info", kakaopay.kakaoPayInfo(pg_token,total_amount));
        
    }
    
    @RequestMapping("/")
    public String index(Model model){
    	
        return "/fragments/header";
    }
    
    
    

    
    @RequestMapping("/datepicker")
    public String viewTest(Model model){
    	KakaoPayReadyVO  kakaoPayReadyVO =new KakaoPayReadyVO();
    	model.addAttribute("name2", kakaoPayReadyVO);
    	model.addAttribute("name", "dfdfdsf");
    	
        return "datepicker";
    }
    
    

    @GetMapping("/bookingDetail")
    public void bookingGet() {
        
    }
    
    @PostMapping("/bookingDetail")
    public String bookingDetail(Model model) {
    
        return "bookingDetail";
 
    }
    
}