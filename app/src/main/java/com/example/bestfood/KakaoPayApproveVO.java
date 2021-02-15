package com.example.bestfood;

import org.json.JSONArray;
import org.json.JSONObject;

public class KakaoPayApproveVO {
    private String aid, tid, cid, sid;
    private String partner_order_id, partner_user_id;
    private String payment_method_type;
    private JSONObject amount;
    private String total;
    private JSONArray card_info;
    private String purchase_corp;
    private String install_month;
    private String interest_free_install;
    private String item_name, item_code, quantity;
    private String created_at, approved_at;

    public void setTid(String tid) {
        this.tid = tid;
    }
    public void setPayment_method_type(String payment_method_type) {
        this.payment_method_type = payment_method_type;
    }
    public void setAmount(JSONObject amount) {
        this.amount = amount;
    }
    public void setTotal(String total) {
        this.total = total;
    }
    public void setCard_info(JSONArray card_info) {
        this.card_info = card_info;
    }
    public void setPurchase_corp(String purchase_corp) {
        this.purchase_corp = purchase_corp;
    }
    public void setInstall_month(String install_month) {
        this.install_month = install_month;
    }
    public void setInterest_free_install(String interest_free_install) {
        this.interest_free_install = interest_free_install;
    }
    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
    public void setApproved_at(String approved_at) {
        this.approved_at = approved_at;
    }


    public String getTid() {
        return tid;
    }
    public String getPayment_method_type() {
        return payment_method_type;
    }
    public JSONObject getAmount() {
        return amount;
    }
    public String getTotal() {
        return total;
    }
    public JSONArray getCard_info() {
        return card_info;
    }
    public String getPurchase_corp() {
        return purchase_corp;
    }
    public String getInstall_month() {
        return install_month;
    }
    public String getInterest_free_install() {
        return interest_free_install;
    }
    public String getItem_name() {
        return item_name;
    }
    public String getApproved_at() {
        return approved_at;
    }


    @Override
    public String toString() {
        return "KakaoPayApproveVO {" +
                "tid='" + tid + '\'' +
                ", payment_method_type='" + payment_method_type + '\'' +
                ", amount='" + amount + '\'' +
                ", total='" + total + '\'' +
                ", card_info='" + card_info + '\'' +
                ", purchase_corp='" + purchase_corp + '\'' +
                ", install_month='" + install_month + '\'' +
                ", interest_free_install='" + interest_free_install + '\'' +
                ", item_name='" + item_name + '\'' +
                ", approved_at'" + approved_at + '\'' +
                '}';
    }
}

