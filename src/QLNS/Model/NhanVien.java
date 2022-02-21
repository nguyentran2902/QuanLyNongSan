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
public class NhanVien {
    private int idNV;
    private String tenNV;
    private String boPhan;
    private String diachiNV;
    private String sdtNV;
    private boolean gioitinhNV;
    private Date ngaySinh;
    private String taiKhoan;
    private String matKhau;
    private boolean trangThai;

    public NhanVien() {
    }

    public NhanVien( String tenNV, String boPhan, String diachiNV, String sdtNV,
            boolean gioitinhNV, Date ngaySinh, String taiKhoan, String matKhau, boolean trangThai) {
        this.tenNV = tenNV;
        this.boPhan = boPhan;
        this.diachiNV = diachiNV;
        this.sdtNV = sdtNV;
        this.gioitinhNV = gioitinhNV;
        this.ngaySinh = ngaySinh;
        this.taiKhoan = taiKhoan;
        this.matKhau = matKhau;
        this.trangThai = trangThai;
    }

    public int getIdNV() {
        return idNV;
    }

    public void setIdNV(int idNV) {
        this.idNV = idNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public String getBoPhan() {
        return boPhan;
    }

    public void setBoPhan(String boPhan) {
        this.boPhan = boPhan;
    }

    public String getDiachiNV() {
        return diachiNV;
    }

    public void setDiachiNV(String diachiNV) {
        this.diachiNV = diachiNV;
    }

    public String getSdtNV() {
        return sdtNV;
    }

    public void setSdtNV(String sdtNV) {
        this.sdtNV = sdtNV;
    }

    public boolean getGioitinhNV() {
        return gioitinhNV;
    }

    public void setGioitinhNV(boolean gioitinhNV) {
        this.gioitinhNV = gioitinhNV;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(String taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
    
    
    
}
