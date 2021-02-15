package com.example.bestfood;

import org.json.JSONArray;
import org.json.JSONObject;

public class KakaoPayApproveVO {
    private String aid, tid, cid, sid;
    private String partner_order_id, partner_user_id;
    private String payment_method_type;
    private int total;
    private int vat;
    private String purchase_corp;
    private String card_type;
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
    public void setTotal(int total) {
        this.total = total;
    }
    public void setVat(int vat) {
        this.vat = vat;
    }
    public void setPurchase_corp(String purchase_corp) {
        this.purchase_corp = purchase_corp;
    }
    public void setCard_type(String card_type) {
        this.card_type = card_type;
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
    public int getTotal() {
        return total;
    }
    public int getVat() {
        return vat;
    }
    public String getPurchase_corp() {
        return purchase_corp;
    }
    public String getCard_type() {
        return card_type;
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
                ", total=" + total +
                ", vat=" + vat +
                ", purchase_corp='" + purchase_corp + '\'' +
                ", card_type='" + card_type + '\'' +
                ", install_month='" + install_month + '\'' +
                ", interest_free_install='" + interest_free_install + '\'' +
                ", item_name='" + item_name + '\'' +
                ", approved_at'" + approved_at + '\'' +
                '}';
    }
}

