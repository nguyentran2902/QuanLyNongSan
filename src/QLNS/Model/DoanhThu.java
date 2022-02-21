/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLNS.Model;

/**
 *
 * @author Admin
 */
public class DoanhThu {
    private String item;
    private Float doanhThu;

    public DoanhThu() {
    }

    public DoanhThu(String item, Float doanhThu) {
        this.item = item;
        this.doanhThu = doanhThu;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Float getDoanhThu() {
        return doanhThu;
    }

    public void setDoanhThu(Float doanhThu) {
        this.doanhThu = doanhThu;
    }
    
    
}
