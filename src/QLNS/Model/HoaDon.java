/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLNS.Model;

import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class HoaDon {

    private String idHD;
    private String maThe;
    private int idNV;
    private Timestamp ngayBan;
    private float tongTienBan;
    private float giamGia;
    private float thanhTien;

    public HoaDon() {
    }

      public HoaDon(String idHD, String maThe, float tongTienBan) {
        this.idHD = idHD;
        this.maThe = maThe;
        this.tongTienBan = tongTienBan;
        
    }
    
    public HoaDon(String idHD, int idNV, Timestamp ngayBan, float tongTienBan, float giamGia, float thanhTien) {
        this.idHD = idHD;
        this.idNV = idNV;
        this.ngayBan = ngayBan;
        this.tongTienBan = tongTienBan;
        this.giamGia = giamGia;
        this.thanhTien = thanhTien;
    }

    public HoaDon(String idHD, String maThe, int idNV, Timestamp ngayBan, float tongTienBan, float giamGia, float thanhTien) {
        this.idHD = idHD;
        this.maThe = maThe;
        this.idNV = idNV;
        this.ngayBan = ngayBan;
        this.tongTienBan = tongTienBan;
        this.giamGia = giamGia;
        this.thanhTien = thanhTien;
    }

    public String getIdHD() {
        return idHD;
    }

    public void setIdHD(String idHD) {
        this.idHD = idHD;
    }

    public String getMaThe() {
        return maThe;
    }

    public void setMaThe(String maThe) {
        this.maThe = maThe;
    }

    public int getIdNV() {
        return idNV;
    }

    public void setIdNV(int idNV) {
        this.idNV = idNV;
    }

    public Timestamp getNgayBan() {
        return ngayBan;
    }

    public void setNgayBan(Timestamp ngayBan) {
        this.ngayBan = ngayBan;
    }

    public float getTongTienBan() {
        return tongTienBan;
    }

    public void setTongTienBan(float tongTienBan) {
        this.tongTienBan = tongTienBan;
    }

    public float getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(float giamGia) {
        this.giamGia = giamGia;
    }

    public float getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(float thanhTien) {
        this.thanhTien = thanhTien;
    }

}
