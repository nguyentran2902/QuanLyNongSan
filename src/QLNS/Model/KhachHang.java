/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLNS.Model;

import java.util.Date;

/**
 *
 * @author Admin
 */
public class KhachHang {
    private String MaThe;
    private String CCCD;
    private String tenKH;
    private String diachiKH;
    private String sdtKH;
    private Date Ngaytao;
    private float tichLuy;

    public KhachHang() {
    }

     public KhachHang(String MaThe, String CCCD, String tenKH, String diachiKH, String sdtKH, Date Ngaytao) {
        this.MaThe = MaThe;
        this.CCCD = CCCD;
        this.tenKH = tenKH;
        this.diachiKH = diachiKH;
        this.sdtKH = sdtKH;
        this.Ngaytao = Ngaytao;
    }
    public KhachHang(String MaThe, String CCCD, String tenKH, String diachiKH, String sdtKH, Date Ngaytao, float tichLuy) {
        this.MaThe = MaThe;
        this.CCCD = CCCD;
        this.tenKH = tenKH;
        this.diachiKH = diachiKH;
        this.sdtKH = sdtKH;
        this.Ngaytao = Ngaytao;
        this.tichLuy = tichLuy;
    }

    public String getMaThe() {
        return MaThe;
    }

    public void setMaThe(String MaThe) {
        this.MaThe = MaThe;
    }

    public String getCCCD() {
        return CCCD;
    }

    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getDiachiKH() {
        return diachiKH;
    }

    public void setDiachiKH(String diachiKH) {
        this.diachiKH = diachiKH;
    }

    public String getSdtKH() {
        return sdtKH;
    }

    public void setSdtKH(String sdtKH) {
        this.sdtKH = sdtKH;
    }

    public Date getNgaytao() {
        return Ngaytao;
    }

    public void setNgaytao(Date Ngaytao) {
        this.Ngaytao = Ngaytao;
    }

    public float getTichLuy() {
        return tichLuy;
    }

    public void setTichLuy(float tichLuy) {
        this.tichLuy = tichLuy;
    }

   
    
    
    
}
