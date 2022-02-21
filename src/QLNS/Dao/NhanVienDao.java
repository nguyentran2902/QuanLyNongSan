/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLNS.Dao;

import QLNS.Model.NhanVien;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class NhanVienDao {

    public List<NhanVien> getListNhanVien() throws SQLException {
        List<NhanVien> listNV = new ArrayList<>();
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "SELECT * FROM NHANVIEN";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ResultSet rs = ppst.executeQuery();
        while (rs.next()) {
            NhanVien nv = new NhanVien();
            nv.setIdNV(rs.getInt("MaNV"));
            nv.setTenNV(rs.getString("TenNV"));
            nv.setBoPhan(rs.getString("Bophan"));
            nv.setDiachiNV(rs.getString("Diachi_NV"));
            nv.setSdtNV(rs.getString("SDT_NV"));
            nv.setGioitinhNV(rs.getBoolean("GioiTinh"));
            nv.setNgaySinh(rs.getDate("Ngaysinh"));
            nv.setTaiKhoan(rs.getString("Taikhoan"));
            nv.setMatKhau(rs.getString("Matkhau"));
            nv.setTrangThai(rs.getBoolean("Trangthai"));

            listNV.add(nv);

        }

        return listNV;
    }

    public Boolean addNhanvien(NhanVien nv) throws SQLException {
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "INSERT into NHANVIEN( TenNV, Bophan,Diachi_NV,SDT_NV, GioiTinh, Ngaysinh,"
                + "Taikhoan, Matkhau, Trangthai) values(?,?,?,?,?,?,?,?,?)";

        PreparedStatement ppst = conn.prepareStatement(sql);
        java.sql.Date sqlDate = null;
        if (nv.getNgaySinh() != null) {
            sqlDate = new java.sql.Date(nv.getNgaySinh().getTime());
        }
        ppst.setString(1, nv.getTenNV());
        ppst.setString(2, nv.getBoPhan());
        ppst.setString(3, nv.getDiachiNV());
        ppst.setString(4, nv.getSdtNV());
        ppst.setBoolean(5, nv.getGioitinhNV());
        ppst.setDate(6, sqlDate);
        ppst.setString(7, nv.getTaiKhoan());
        ppst.setString(8, nv.getMatKhau());
        ppst.setBoolean(9, nv.getTrangThai());

        return ppst.executeUpdate() > 0;

    }

    public Boolean editNhanvien(NhanVien nv) throws SQLException {
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "UPDATE  NHANVIEN set  TenNV = ?, Bophan = ?,Diachi_NV  = ?,"
                + "SDT_NV = ?, Ngaysinh = ?,Gioitinh = ?, Taikhoan = ?, Matkhau = ?, "
                + "Trangthai = ?  where MaNV = ?";

        PreparedStatement ppst = conn.prepareStatement(sql);
        java.sql.Date sqlDate = null;
        if (nv.getNgaySinh() != null) {
            sqlDate = new java.sql.Date(nv.getNgaySinh().getTime());
        }
        ppst.setString(1, nv.getTenNV());
        ppst.setString(2, nv.getBoPhan());
        ppst.setString(3, nv.getDiachiNV());
        ppst.setString(4, nv.getSdtNV());
        ppst.setDate(5, sqlDate);
        ppst.setBoolean(6, nv.getGioitinhNV());
        ppst.setString(7, nv.getTaiKhoan());
        ppst.setString(8, nv.getMatKhau());
        ppst.setBoolean(9, nv.getTrangThai());
        ppst.setInt(10, nv.getIdNV());

        return ppst.executeUpdate() > 0;

    }

    public Boolean deleteNhanvien(String MaNV) throws SQLException {
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "Delete NHANVIEN Where MaNV = ?";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ppst.setString(1, MaNV);
        return ppst.executeUpdate() > 0;

    }

    public NhanVien getNvbyId(String MaNV) throws SQLException {
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "select * from NHANVIEN Where MaNV = ?";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ppst.setString(1, MaNV);
        ResultSet rs = ppst.executeQuery();
        NhanVien nv = new NhanVien();

        while (rs.next()) {

            nv.setIdNV(rs.getInt("MaNV"));
            nv.setTenNV(rs.getString("TenNV"));
            nv.setBoPhan(rs.getString("Bophan"));
            nv.setDiachiNV(rs.getString("Diachi_NV"));
            nv.setSdtNV(rs.getString("SDT_NV"));
            nv.setGioitinhNV(rs.getBoolean("GioiTinh"));
            nv.setNgaySinh(rs.getDate("Ngaysinh"));
            nv.setTaiKhoan(rs.getString("Taikhoan"));
            nv.setMatKhau(rs.getString("Matkhau"));
            nv.setTrangThai(rs.getBoolean("Trangthai"));

        }
        return nv;

    }

    public Boolean existNhanVienInHD(String MaNV) throws SQLException {
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "SELECT COUNT(MaNV) FROM HOADONBAN Where MaNV = ?";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ppst.setString(1, MaNV);
        int nv = 0;
        ResultSet rs = ppst.executeQuery();
        while (rs.next()) {
            nv = rs.getInt(1);
        }
        return nv > 0;
    }

    public List<NhanVien> searchNhanVienByName(String name) throws SQLException {
        List<NhanVien> listNV = new ArrayList<>();
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "SELECT * FROM NHANVIEN Where TenNV LIKE  N'%" + name + "%'";

        Statement stt = conn.createStatement();
        ResultSet rs = stt.executeQuery(sql);
        while (rs.next()) {
            NhanVien nv = new NhanVien();
            nv.setIdNV(rs.getInt("MaNV"));
            nv.setTenNV(rs.getString("TenNV"));
            nv.setBoPhan(rs.getString("Bophan"));
            nv.setDiachiNV(rs.getString("Diachi_NV"));
            nv.setSdtNV(rs.getString("SDT_NV"));
            nv.setGioitinhNV(rs.getBoolean("GioiTinh"));
            nv.setNgaySinh(rs.getDate("Ngaysinh"));
            nv.setTaiKhoan(rs.getString("Taikhoan"));
            nv.setMatKhau(rs.getString("Matkhau"));
            nv.setTrangThai(rs.getBoolean("Trangthai"));

            listNV.add(nv);

        }

        return listNV;
    }

    public NhanVien checkLogin(String user, String pass) throws SQLException {
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "select * from NHANVIEN Where Taikhoan = ? and Matkhau = ?";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ppst.setString(1, user);
        ppst.setString(2, pass);
        ResultSet rs = ppst.executeQuery();

        while (rs.next()) {

            NhanVien nv = new NhanVien();
            nv.setIdNV(rs.getInt("MaNV"));
            nv.setTenNV(rs.getString("TenNV"));
            nv.setBoPhan(rs.getString("Bophan"));
            nv.setDiachiNV(rs.getString("Diachi_NV"));
            nv.setSdtNV(rs.getString("SDT_NV"));
            nv.setGioitinhNV(rs.getBoolean("GioiTinh"));
            nv.setNgaySinh(rs.getDate("Ngaysinh"));
            nv.setTaiKhoan(rs.getString("Taikhoan"));
            nv.setMatKhau(rs.getString("Matkhau"));
            nv.setTrangThai(rs.getBoolean("Trangthai"));

            return nv;

        }
        return null;
    }
    
    
    
    public Boolean changePassNv(String MaNV, String newPass) throws SQLException {
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "Update NHANVIEN set Matkhau=? Where MaNV = ?";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ppst.setString(1, newPass);
         ppst.setString(2, MaNV);

        return ppst.executeUpdate() > 0;

    }
}
