/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLNS.Dao;

import QLNS.Model.KhachHang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class KhachHangDao {

    public List<KhachHang> getListKhachHang() throws SQLException {
        List<KhachHang> listKH = new ArrayList<>();
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "SELECT * FROM KHACHHANG";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ResultSet rs = ppst.executeQuery();
        while (rs.next()) {
            KhachHang kh = new KhachHang();
            kh.setMaThe(rs.getString("MaThe"));
            kh.setCCCD(rs.getString("CCCD"));
            kh.setTenKH(rs.getString("TenKH"));
            kh.setSdtKH(rs.getString("SDT_KH"));
            kh.setDiachiKH(rs.getString("DiaChi_KH"));
            kh.setNgaytao(rs.getDate("Ngaytao"));
            kh.setTichLuy(rs.getFloat("Tichluy"));

            listKH.add(kh);

        }
        return listKH;
    }

    public Boolean addKhachHang(KhachHang kh) throws SQLException {
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "INSERT into KHACHHANG( MaThe,CCCD,TenKH, DiaChi_KH,SDT_KH,Ngaytao,Tichluy )"
                + " values(?,?,?,?,?,?,?)";

        java.sql.Date sqlDate = new java.sql.Date(kh.getNgaytao().getTime());
        PreparedStatement ppst = conn.prepareStatement(sql);
        ppst.setString(1, kh.getMaThe());
        ppst.setString(2, kh.getCCCD());
        ppst.setString(3, kh.getTenKH());
        ppst.setString(4, kh.getDiachiKH());
        ppst.setString(5, kh.getSdtKH());
        ppst.setDate(6, sqlDate);
        ppst.setFloat(7, kh.getTichLuy());

        return ppst.executeUpdate() > 0;
    }

    public Boolean editKhachHang(KhachHang kh) throws SQLException {
        int rs = 0;
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "UPDATE  KHACHHANG set  TenKH = ?, DiaChi_KH = ?,SDT_KH = ? where MaThe = ?";

        PreparedStatement ppst = conn.prepareStatement(sql);

        ppst.setString(1, kh.getTenKH());
        ppst.setString(2, kh.getDiachiKH());
        ppst.setString(3, kh.getSdtKH());
        ppst.setString(4, kh.getMaThe());

        return ppst.executeUpdate() > 0;
    }

    public Boolean deleteKhachHang(String MaThe) throws SQLException {
        Connection conn = JDBCConnection.getJDBCConnection();

        String sql = "Delete KHACHHANG Where MaThe = ?";
        PreparedStatement ppst = conn.prepareStatement(sql);

        ppst.setString(1, MaThe);

        return ppst.executeUpdate() > 0;

    }

    public KhachHang getKhbyMaThe(String MaThe) throws SQLException {
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "select * from KHACHHANG Where MaThe = ?";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ppst.setString(1, MaThe);
        ResultSet rs = ppst.executeQuery();
        KhachHang kh = new KhachHang();

        while (rs.next()) {
            kh.setMaThe(rs.getString("MaThe"));
            kh.setCCCD(rs.getString("CCCD"));
            kh.setTenKH(rs.getString("TenKH"));
            kh.setSdtKH(rs.getString("SDT_KH"));
            kh.setDiachiKH(rs.getString("DiaChi_KH"));
            kh.setNgaytao(rs.getDate("Ngaytao"));
            kh.setTichLuy(rs.getFloat("Tichluy"));
        }
        return kh;

    }

    public Boolean existKhachHangInHD(String MaThe) throws SQLException {
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "SELECT COUNT(MaThe) FROM HOADONBAN Where MaThe = ?";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ppst.setString(1, MaThe);
        int kh = 0;
        ResultSet rs = ppst.executeQuery();
        while (rs.next()) {
            kh = rs.getInt(1);
        }
        return kh > 0;
    }

    public KhachHang getKhbyNameAndHd(String name, String maHd) throws SQLException {
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "SELECT HOADONBAN.MaThe,CCCD,TenKH,SDT_KH,DiaChi_KH,Ngaytao,Tichluy   FROM HOADONBAN,KHACHHANG "
                + "WHERE HOADONBAN.MaThe = KHACHHANG.MaThe and MaHDB = ? and TenKH = ?";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ppst.setString(1, maHd);
        ppst.setString(2, name);
        ResultSet rs = ppst.executeQuery();
        KhachHang kh = new KhachHang();

        while (rs.next()) {
            kh.setMaThe(rs.getString("MaThe"));
            kh.setCCCD(rs.getString("CCCD"));
            kh.setTenKH(rs.getString("TenKH"));
            kh.setSdtKH(rs.getString("SDT_KH"));
            kh.setDiachiKH(rs.getString("DiaChi_KH"));
            kh.setNgaytao(rs.getDate("Ngaytao"));
            kh.setTichLuy(rs.getFloat("Tichluy"));
        }
        return kh;

    }

    public List<KhachHang> searchKhachHangByName(String name) throws SQLException {
        Connection conn = JDBCConnection.getJDBCConnection();

        String sql = "select * from KHACHHANG Where TenKH LIKE N'%" + name + "%'";
        List<KhachHang> listKH = new ArrayList<>();

        Statement stt = conn.createStatement();

        ResultSet rs = stt.executeQuery(sql);

        while (rs.next()) {
            KhachHang kh = new KhachHang();
            kh.setMaThe(rs.getString("MaThe"));
            kh.setCCCD(rs.getString("CCCD"));
            kh.setTenKH(rs.getString("TenKH"));
            kh.setSdtKH(rs.getString("SDT_KH"));
            kh.setDiachiKH(rs.getString("DiaChi_KH"));
            kh.setNgaytao(rs.getDate("Ngaytao"));
            kh.setTichLuy(rs.getFloat("Tichluy"));

            listKH.add(kh);

        }
        return listKH;

    }

    public List<KhachHang> searchKhachHangByMaThe(String mathe) throws SQLException {
        Connection conn = JDBCConnection.getJDBCConnection();

        String sql = "select * from KHACHHANG Where MaThe LIKE '%" + mathe + "%'";
        List<KhachHang> listKH = new ArrayList<>();

        Statement stt = conn.createStatement();

        ResultSet rs = stt.executeQuery(sql);

        while (rs.next()) {
            KhachHang kh = new KhachHang();
            kh.setMaThe(rs.getString("MaThe"));
            kh.setCCCD(rs.getString("CCCD"));
            kh.setTenKH(rs.getString("TenKH"));
            kh.setSdtKH(rs.getString("SDT_KH"));
            kh.setDiachiKH(rs.getString("DiaChi_KH"));
            kh.setNgaytao(rs.getDate("Ngaytao"));
            kh.setTichLuy(rs.getFloat("Tichluy"));

            listKH.add(kh);

        }
        return listKH;

    }

    public String CountSoMaThe() throws SQLException {
        String maThe = "";
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "SELECT TOP 1 * FROM KHACHHANG "
                + "ORDER BY MaThe DESC";

        Statement stt = conn.createStatement();
        ResultSet rs = stt.executeQuery(sql);

        while (rs.next()) {
            maThe = rs.getString("MaThe");
        }
        return maThe;
    }

}
