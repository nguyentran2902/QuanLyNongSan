/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLNS.Dao;

import QLNS.Model.DoanhThu;
import QLNS.Model.HoaDonDeTail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;

/**
 *
 * @author Admin
 */
public class HdDetailDao {

    public List<HoaDonDeTail> getListNongSanBan(String idHD) throws SQLException {
        List<HoaDonDeTail> listHdDetail = new ArrayList<>();
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "SELECT * FROM CHITIET_HDB WHERE MaHDB = ?";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ppst.setString(1, idHD);
        ResultSet rs = ppst.executeQuery();
        while (rs.next()) {
            HoaDonDeTail hdDetail = new HoaDonDeTail();
            hdDetail.setIdHD(rs.getString("MaHDB"));
            hdDetail.setIdNS(rs.getInt("MaNS"));
            hdDetail.setSoluongBan(rs.getFloat("Soluong_ban"));
            hdDetail.setDongiaMua(rs.getFloat("Dongiamua"));
            hdDetail.setThanhTien(rs.getFloat("Thanhtien"));

            listHdDetail.add(hdDetail);

        }
        return listHdDetail;
    }

    public List<DoanhThu> getDoanhThuTheoThang(String year) throws SQLException {
        List<DoanhThu> listDTT = new ArrayList<>();
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "select DATEPART(MM,Ngayban) as Thang, sum(ThanhTien) as DoanhThu "
                + "from HOADONBAN where YEAR(Ngayban)=? group by DATEPART(MM,Ngayban)";
             

        PreparedStatement ppst = conn.prepareStatement(sql);
        ppst.setString(1, year);
        ResultSet rs = ppst.executeQuery();
        while (rs.next()) {
            DoanhThu dtt = new DoanhThu();
            dtt.setItem(rs.getString("Thang"));
            dtt.setDoanhThu(rs.getFloat("DoanhThu"));

            listDTT.add(dtt);

        }
        return listDTT;
    }
    
    public List<DoanhThu> getDoanhThuTheoPL(String year) throws SQLException {
        List<DoanhThu> listDTPL = new ArrayList<>();
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "select PhanLoai, sum(CHITIET_HDB.ThanhTien) as DoanhThu from HOADONBAN, NONGSAN, CHITIET_HDB"
                + " where HOADONBAN.MaHDB = CHITIET_HDB.MaHDB and CHITIET_HDB.MaNS = NONGSAN.MaNS and "
                + "	YEAR(Ngayban) = ? group by PhanLoai";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ppst.setString(1, year);
        ResultSet rs = ppst.executeQuery();
        while (rs.next()) {
            DoanhThu dtt = new DoanhThu();
            dtt.setItem(rs.getString("PhanLoai"));
            dtt.setDoanhThu(rs.getFloat("DoanhThu"));

            listDTPL.add(dtt);

        }
        return listDTPL;
    }
    
     public DoanhThu getDoanhThuTheoNam(String year) throws SQLException {
       
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "select count(*) as TongHD, sum(HOADONBAN.ThanhTien) as DoanhThu from HOADONBAN"
                + " where YEAR(Ngayban) = ?  ";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ppst.setString(1, year);
        ResultSet rs = ppst.executeQuery();
         DoanhThu dtn = new DoanhThu();
        while (rs.next()) {
           
            dtn.setItem(rs.getString("TongHD"));
            dtn.setDoanhThu(rs.getFloat("DoanhThu"));

           

        }
        return dtn;
    }
      public void RenderYearToCbb(JComboBox cbb) throws SQLException {
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "select DISTINCT DATEPART(YY,Ngayban) as Nam from HOADONBAN "
                + "order by DATEPART(YY,Ngayban) desc";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ResultSet rs = ppst.executeQuery();

        while (rs.next()) {
            cbb.addItem(rs.getString("Nam"));
        }
    }
}
