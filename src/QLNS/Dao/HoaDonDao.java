/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLNS.Dao;

import QLNS.Model.HoaDon;
import QLNS.Model.HoaDonDeTail;
import QLNS.Model.NongSan;
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
public class HoaDonDao {

    public List<HoaDon> getListHoaDon() throws SQLException {
        List<HoaDon> listHD = new ArrayList<>();

        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "SELECT * FROM HOADONBAN";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ResultSet rs = ppst.executeQuery();
        while (rs.next()) {
            HoaDon hd = new HoaDon();
            hd.setIdHD(rs.getString("MaHDB"));
            hd.setMaThe(rs.getString("MaThe"));
            hd.setIdNV(rs.getInt("MaNV"));
            hd.setNgayBan(rs.getTimestamp("Ngayban"));
            hd.setTongTienBan(rs.getFloat("Tongtien_ban"));
            hd.setGiamGia(rs.getFloat("GiamGia"));
            hd.setThanhTien(rs.getFloat("ThanhTien"));

            listHD.add(hd);

        }
        return listHD;
    }

    public List<HoaDonDeTail> getListHdDetail(String MaHDB) throws SQLException {
        List<HoaDonDeTail> listHdDetail = new ArrayList<>();

        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "SELECT * FROM CHITIET_HDB where MaHDB = ?";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ppst.setString(1, MaHDB);
        ResultSet rs = ppst.executeQuery();
        while (rs.next()) {
            HoaDonDeTail hdDt = new HoaDonDeTail();
            hdDt.setIdHD(rs.getString("MaHDB"));
            hdDt.setIdNS(rs.getInt("MaNS"));
            hdDt.setSoluongBan(rs.getFloat("Soluong_ban"));

            listHdDetail.add(hdDt);

        }
        return listHdDetail;
    }

    public Boolean addHoaDonFinally(HoaDon hd, List<NongSan> ListNs) throws SQLException {
        Connection conn = JDBCConnection.getJDBCConnection();
        conn.setAutoCommit(false);
        Statement stt = conn.createStatement();
        
        //add hoa don ban
        String maThe = hd.getMaThe() == "" ? null : "'" + hd.getMaThe() + "'";
        String sql1 = "INSERT INTO HOADONBAN(MaHDB,MaThe,MaNV,Ngayban,Tongtien_ban,GiamGia,ThanhTien) values "
                + "('" + hd.getIdHD() + "'," + maThe + "," + hd.getIdNV() + ",'" + hd.getNgayBan() + "'," + hd.getTongTienBan()
                + "," + hd.getGiamGia() + "," + hd.getThanhTien() + ")";

        int rs1 = stt.executeUpdate(sql1);
        
        //add hoadonDetail va reset So luong
        int rs2 = 0;
        int rs3 = 0;
        for (NongSan ns : ListNs) {
            float thanhTien = ns.getDongiaBan() * ns.getSoluongKho();
            String sql2 = "INSERT into CHITIET_HDB( MaHDB, MaNS,Soluong_ban,Dongiamua,Thanhtien ) values"
                    + "('" + hd.getIdHD() + "'," + ns.getIdNS() + "," + ns.getSoluongKho() + "," + ns.getDongiaBan() + "," + thanhTien + ")";
            String sql3 = "UPDATE NONGSAN SET SL_Kho = SL_Kho -" + ns.getSoluongKho() + "WHERE MaNS =" + ns.getIdNS() + "";
            rs2 += stt.executeUpdate(sql2);
            rs3 += stt.executeUpdate(sql3);
        }
        
        //reset tich luy Khach hang
        int rs4 = maThe != null ? 0 : 1;
        if (maThe != null) {
            String sql4 = "UPDATE KHACHHANG SET TichLuy = TichLuy +" + hd.getTongTienBan() + "WHERE maThe ='" + hd.getMaThe() + "'";
            rs4 = stt.executeUpdate(sql4);
        }
        conn.commit();
        if (rs1 > 0 && rs4 > 0 && rs2 == ListNs.size() && rs3 == ListNs.size()) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean deleteHoaDon(HoaDon hd, List<HoaDonDeTail> ListHdDetail) throws SQLException {
        Connection conn = JDBCConnection.getJDBCConnection();

        conn.setAutoCommit(false);

        Statement stt = conn.createStatement();
        int rs1 = 0;
        for (HoaDonDeTail hdDt : ListHdDetail) {
            String sql1 = "UPDATE NONGSAN SET SL_Kho = SL_Kho +" + hdDt.getSoluongBan() + "WHERE MaNS =" + hdDt.getIdNS() + "";
            rs1 += stt.executeUpdate(sql1);
        }
        int rs2 = hd.getMaThe() != null ? 0 : 1;
        if(hd.getMaThe() != null) {
             String sql2 = "UPDATE KHACHHANG SET TichLuy = TichLuy -" + hd.getTongTienBan() + "WHERE maThe ='" + hd.getMaThe() + "'";
            rs2 = stt.executeUpdate(sql2);
        }

        String sql3 = "DELETE from CHITIET_HDB Where MaHDB = '" + hd.getIdHD() + "'";
        String sql4 = "DELETE from HOADONBAN Where MaHDB = '" + hd.getIdHD() + "'";
        int rs3 = stt.executeUpdate(sql3);
        int rs4 = stt.executeUpdate(sql4);

        conn.commit();
        if (rs1 == ListHdDetail.size()  && rs2 > 0 && rs3 >0 && rs4 > 0) {
            return true;
        } else {
            return false;
        }

    }

    public HoaDon getHdById(String idHd) throws SQLException {
        HoaDon hd = new HoaDon();
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "select * from HOADONBAN Where MaHDB = ?";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ppst.setString(1, idHd);
        ResultSet rs = ppst.executeQuery();

        while (rs.next()) {
            hd.setIdHD(rs.getString("MaHDB"));
            hd.setMaThe(rs.getString("MaThe"));
            hd.setIdNV(rs.getInt("MaNV"));
            hd.setNgayBan(rs.getTimestamp("Ngayban"));
            hd.setTongTienBan(rs.getFloat("Tongtien_ban"));
            hd.setGiamGia(rs.getFloat("GiamGia"));
            hd.setThanhTien(rs.getFloat("ThanhTien"));

        }
        return hd;
    }
     public List<HoaDon> getListHdByTime(String firstTime,String lastTime) throws SQLException {
        List<HoaDon> listHD = new ArrayList<>();

        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "SELECT * FROM HOADONBAN where Ngayban between ? and ?";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ppst.setString(1, firstTime);
        ppst.setString(2,lastTime);
        ResultSet rs = ppst.executeQuery();
        
        while (rs.next()) {
            HoaDon hd = new HoaDon();
            hd.setIdHD(rs.getString("MaHDB"));
            hd.setMaThe(rs.getString("MaThe"));
            hd.setIdNV(rs.getInt("MaNV"));
            hd.setNgayBan(rs.getTimestamp("Ngayban"));
            hd.setTongTienBan(rs.getFloat("Tongtien_ban"));
            hd.setGiamGia(rs.getFloat("GiamGia"));
            hd.setThanhTien(rs.getFloat("ThanhTien"));

            listHD.add(hd);

        }
        return listHD;
    }

    public List<HoaDon> getListHdByNameKH(String name) throws SQLException {
        List<HoaDon> listHD = new ArrayList<>();

        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "SELECT MaHDB,HOADONBAN.MaThe,MaNV,Ngayban,Tongtien_ban,GiamGia,ThanhTien   FROM HOADONBAN,KHACHHANG "
                + "WHERE HOADONBAN.MaThe = KHACHHANG.MaThe and TenKH LIKE  N'%" + name + "%'";

        Statement stt = conn.createStatement();
        ResultSet rs = stt.executeQuery(sql);
        while (rs.next()) {
            HoaDon hd = new HoaDon();
            hd.setIdHD(rs.getString("MaHDB"));
            hd.setMaThe(rs.getString("MaThe"));
            hd.setIdNV(rs.getInt("MaNV"));
            hd.setNgayBan(rs.getTimestamp("Ngayban"));
            hd.setTongTienBan(rs.getFloat("Tongtien_ban"));
            hd.setGiamGia(rs.getFloat("GiamGia"));
            hd.setThanhTien(rs.getFloat("ThanhTien"));

            listHD.add(hd);

        }
        return listHD;
    }

    public List<HoaDon> getListHdByNameNV(String name) throws SQLException {
        List<HoaDon> listHD = new ArrayList<>();

        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "SELECT MaHDB,MaThe,HOADONBAN.MaNV,Ngayban,Tongtien_ban,GiamGia,ThanhTien   FROM HOADONBAN,NHANVIEN "
                + "WHERE HOADONBAN.MaNV = NHANVIEN.MaNV and TenNV LIKE  N'%" + name + "%'";

        Statement stt = conn.createStatement();
        ResultSet rs = stt.executeQuery(sql);
        while (rs.next()) {
            HoaDon hd = new HoaDon();
            hd.setIdHD(rs.getString("MaHDB"));
            hd.setMaThe(rs.getString("MaThe"));
            hd.setIdNV(rs.getInt("MaNV"));
            hd.setNgayBan(rs.getTimestamp("Ngayban"));
            hd.setTongTienBan(rs.getFloat("Tongtien_ban"));
            hd.setGiamGia(rs.getFloat("GiamGia"));
            hd.setThanhTien(rs.getFloat("ThanhTien"));

            listHD.add(hd);

        }
        return listHD;
    }

    public String CountSoHd(String hdTam) throws SQLException {
        String maHDB = "";
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "SELECT Top 1 * FROM HOADONBAN "
                + "WHERE MaHDB LIKE  '%" + hdTam + "%' order by MaHDB desc";

        Statement stt = conn.createStatement();
        ResultSet rs = stt.executeQuery(sql);

        while (rs.next()) {
            maHDB = rs.getString("MaHDB");
        }
        return maHDB;
    }

}
