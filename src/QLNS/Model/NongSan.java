/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLNS.Model;

/**
 *
 * @author Admin
 */
public class NongSan {

    private int idNS;
    private String tenNS;
    private String phanLoai;
    private float soluongKho;
    private String donviTinh;
    private float dongiaBan;
    private byte[] hinhAnh;

    public NongSan() {
    }

   public NongSan(int idNS ,String tenNS, float soluongKho) {
        this.idNS = idNS;
        this.tenNS = tenNS;
        this.soluongKho = soluongKho;
     
    }


    public NongSan(String tenNS, String phanLoai, float soluongKho, String donviTinh, float dongiaBan) {
        this.tenNS = tenNS;
        this.phanLoai = phanLoai;
        this.soluongKho = soluongKho;
        this.donviTinh = donviTinh;
        this.dongiaBan = dongiaBan;
    }

    public NongSan(int idNS, String tenNS, String phanLoai, float soluongKho, String donviTinh, float dongiaBan, byte[] hinhAnh) {
        this.idNS = idNS;
        this.tenNS = tenNS;
        this.phanLoai = phanLoai;
        this.soluongKho = soluongKho;
        this.donviTinh = donviTinh;
        this.dongiaBan = dongiaBan;
        this.hinhAnh = hinhAnh;
    }

    public int getIdNS() {
        return idNS;
    }

    public void setIdNS(int idNS) {
        this.idNS = idNS;
    }

    public String getTenNS() {
        return tenNS;
    }

    public void setTenNS(String tenNS) {
        this.tenNS = tenNS;
    }

    public String getPhanLoai() {
        return phanLoai;
    }

    public void setPhanLoai(String phanLoai) {
        this.phanLoai = phanLoai;
    }

    public float getSoluongKho() {
        return soluongKho;
    }

    public void setSoluongKho(float soluongKho) {
        this.soluongKho = soluongKho;
    }

    public String getDonviTinh() {
        return donviTinh;
    }

    public void setDonviTinh(String donviTinh) {
        this.donviTinh = donviTinh;
    }

    public float getDongiaBan() {
        return dongiaBan;
    }

    public void setDongiaBan(float dongiaBan) {
        this.dongiaBan = dongiaBan;
    }
   public byte[] getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(byte[] hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
}
