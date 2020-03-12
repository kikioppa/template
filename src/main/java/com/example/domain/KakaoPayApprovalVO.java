package com.example.domain;

import java.util.Date;

import lombok.Data;
 
@Data
public class KakaoPayApprovalVO {
    
    //response
    private String aid, tid, cid, sid;
    private String partner_order_id, partner_user_id, payment_method_type;
   // private AmountVO amount;
    private CardVO card_info;
    private String item_name, item_code, payload;
    private Integer tax_free_amount, vat_amount;
    private Date created_at, approved_at;
    private String bookingDate;
    private String bookingTime;
    private String myNumber;
    //private Integer total, tax_free, vat, point, discount;
    private Integer  tax_free, vat, point, discount;
    private String total_amount;
    private String quantity;
}