/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLNS.Model;

/**
 *
 * @author Admin
 */
public class HoaDonDeTail {
    private String idHD;
    private int idNS;
    private float soluongBan;
    private float dongiaMua;
    private float thanhTien;

    public HoaDonDeTail() {
    }

    public HoaDonDeTail(String idHD, int idNS, float soluongBan, float dongiaMua, float thanhTien) {
        this.idHD = idHD;
        this.idNS = idNS;
        this.soluongBan = soluongBan;
        this.dongiaMua = dongiaMua;
        this.thanhTien = thanhTien;
    }

    public String getIdHD() {
        return idHD;
    }

    public void setIdHD(String idHD) {
        this.idHD = idHD;
    }

    public int getIdNS() {
        return idNS;
    }

    public void setIdNS(int idNS) {
        this.idNS = idNS;
    }

    public float getSoluongBan() {
        return soluongBan;
    }

    public void setSoluongBan(float soluongBan) {
        this.soluongBan = soluongBan;
    }

    public float getDongiaMua() {
        return dongiaMua;
    }

    public void setDongiaMua(float dongiaMua) {
        this.dongiaMua = dongiaMua;
    }

    public float getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(float thanhTien) {
        this.thanhTien = thanhTien;
    }
    
    
}
