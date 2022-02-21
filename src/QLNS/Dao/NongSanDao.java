/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLNS.Dao;

import QLNS.Model.NongSan;
import java.sql.Statement;
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
public class NongSanDao {

    public List<NongSan> getListNongSan() throws SQLException {
        List<NongSan> listNS = new ArrayList<>();
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "SELECT * FROM NONGSAN";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ResultSet rs = ppst.executeQuery();
        while (rs.next()) {
            NongSan ns = new NongSan();
            ns.setIdNS(rs.getInt("MaNS"));
            ns.setTenNS(rs.getString("TenNS"));
            ns.setPhanLoai(rs.getString("Phanloai"));
            ns.setSoluongKho(rs.getFloat("SL_Kho"));
            ns.setDonviTinh(rs.getString("Donvi_Tinh"));
            ns.setDongiaBan(rs.getFloat("Dongiaban"));
            ns.setHinhAnh(rs.getBytes("AnhNS"));

            listNS.add(ns);

        }
        return listNS;
    }

    public Boolean addNongSan(NongSan ns) throws SQLException {
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "INSERT into NONGSAN( TenNS, Phanloai,SL_Kho,"
                + "Donvi_Tinh, Dongiaban, AnhNS) values(?,?,?,?,?,?)";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ppst.setString(1, ns.getTenNS());
        ppst.setString(2, ns.getPhanLoai());
        ppst.setFloat(3, ns.getSoluongKho());
        ppst.setString(4, ns.getDonviTinh());
        ppst.setFloat(5, ns.getSoluongKho());
        ppst.setBytes(6, ns.getHinhAnh());

        return ppst.executeUpdate() > 0;
    }

    public Boolean editNongSan(NongSan ns) throws SQLException {
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "UPDATE  NONGSAN set  TenNS = ?, Phanloai = ?,SL_Kho = ?,"
                + "Donvi_Tinh = ?, Dongiaban = ?, AnhNS = ? where MaNS = ?";

        PreparedStatement ppst = conn.prepareStatement(sql);

        ppst.setString(1, ns.getTenNS());
        ppst.setString(2, ns.getPhanLoai());
        ppst.setFloat(3, ns.getSoluongKho());
        ppst.setString(4, ns.getDonviTinh());
        ppst.setFloat(5, ns.getDongiaBan());
        ppst.setBytes(6, ns.getHinhAnh());
        ppst.setInt(7, ns.getIdNS());

        return ppst.executeUpdate() > 0;
    }

    public Boolean deleteNongSan(String MaNS) throws SQLException {
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "Delete NONGSAN Where MaNS = ?";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ppst.setString(1, MaNS);

        return ppst.executeUpdate() > 0;
    }

    public Boolean existNongSanInHD(String MaNS) throws SQLException {
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "SELECT COUNT(MaNS) FROM CHITIET_HDB Where MaNS = ?";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ppst.setString(1, MaNS);
        int ns = 0;
        ResultSet rs = ppst.executeQuery();
        while (rs.next()) {
            ns = rs.getInt(1);
        }
        return ns > 0;
    }

    public NongSan getNsbyId(String MaNS) throws SQLException {
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "select * from NONGSAN Where MaNS = ?";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ppst.setString(1, MaNS);
        ResultSet rs = ppst.executeQuery();
        NongSan ns = new NongSan();

        while (rs.next()) {
            ns.setIdNS(rs.getInt("MaNS"));
            ns.setTenNS(rs.getString("TenNS"));
            ns.setPhanLoai(rs.getString("Phanloai"));
            ns.setSoluongKho(rs.getFloat("SL_Kho"));
            ns.setDonviTinh(rs.getString("Donvi_Tinh"));
            ns.setDongiaBan(rs.getFloat("Dongiaban"));
            ns.setHinhAnh(rs.getBytes("AnhNS"));

        }
        return ns;

    }

    public List<NongSan> getListNSByTime(String firstTime, String lastTime) throws SQLException {
        List<NongSan> listNS = new ArrayList<>();
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "SELECT NONGSAN.MaNS,TenNS,Donvi_Tinh,DonGiaban, sum(CHITIET_HDB.Soluong_ban) as SL_TB  "
                + "FROM NONGSAN, HOADONBAN, CHITIET_HDB WHERE NONGSAN.MaNS = CHITIET_HDB.MaNS and "
                + "HOADONBAN.MaHDB = CHITIET_HDB.MaHDB and Ngayban between ? and ? "
                + "group by NONGSAN.MaNS,TenNS,Donvi_Tinh,DonGiaban order by SL_TB desc";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ppst.setString(1, firstTime);
        ppst.setString(2, lastTime);
        ResultSet rs = ppst.executeQuery();
        while (rs.next()) {
            NongSan ns = new NongSan();
            ns.setIdNS(rs.getInt("MaNS"));
            ns.setTenNS(rs.getString("TenNS"));
            ns.setDonviTinh(rs.getString("Donvi_Tinh"));
            ns.setDongiaBan(rs.getFloat("Dongiaban"));
            ns.setSoluongKho(rs.getFloat("SL_TB"));

            listNS.add(ns);
        }
        return listNS;
    }

    public List<NongSan> searchNongSanByNameFromDB(String name) throws SQLException {
        List<NongSan> listNS = new ArrayList<>();
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "SELECT * FROM NONGSAN Where TenNS LIKE  N'%" + name + "%'";

        Statement stt = conn.createStatement();
        ResultSet rs = stt.executeQuery(sql);
        while (rs.next()) {
            NongSan ns = new NongSan();
            ns.setIdNS(rs.getInt("MaNS"));
            ns.setTenNS(rs.getString("TenNS"));
            ns.setPhanLoai(rs.getString("Phanloai"));
            ns.setSoluongKho(rs.getFloat("SL_Kho"));
            ns.setDonviTinh(rs.getString("Donvi_Tinh"));
            ns.setDongiaBan(rs.getFloat("Dongiaban"));
            ns.setHinhAnh(rs.getBytes("AnhNS"));
            listNS.add(ns);

        }

        return listNS;
    }

    public List<NongSan> searchNongSanByType(String type) throws SQLException {
        List<NongSan> listNS = new ArrayList<>();
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "SELECT * FROM NONGSAN Where Phanloai = ?";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ppst.setString(1, type);
        ResultSet rs = ppst.executeQuery();
        while (rs.next()) {
            NongSan ns = new NongSan();
            ns.setIdNS(rs.getInt("MaNS"));
            ns.setTenNS(rs.getString("TenNS"));
            ns.setPhanLoai(rs.getString("Phanloai"));
            ns.setSoluongKho(rs.getFloat("SL_Kho"));
            ns.setDonviTinh(rs.getString("Donvi_Tinh"));
            ns.setDongiaBan(rs.getFloat("Dongiaban"));
            ns.setHinhAnh(rs.getBytes("AnhNS"));
            listNS.add(ns);

        }

        return listNS;
    }

    public List<NongSan> searchNongSanByNameAndType(String name, String type) throws SQLException {
        List<NongSan> listNS = new ArrayList<>();
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "SELECT * FROM NONGSAN Where TenNS LIKE N'%" + name + "%' "
                + "and Phanloai = N'" + type + "'";

        Statement stt = conn.createStatement();
        ResultSet rs = stt.executeQuery(sql);
        while (rs.next()) {
            NongSan ns = new NongSan();
            ns.setIdNS(rs.getInt("MaNS"));
            ns.setTenNS(rs.getString("TenNS"));
            ns.setPhanLoai(rs.getString("Phanloai"));
            ns.setSoluongKho(rs.getFloat("SL_Kho"));
            ns.setDonviTinh(rs.getString("Donvi_Tinh"));
            ns.setDongiaBan(rs.getFloat("Dongiaban"));
            ns.setHinhAnh(rs.getBytes("AnhNS"));
            listNS.add(ns);

        }

        return listNS;
    }

    public void RenderTypeNsToCbb(JComboBox cbb) throws SQLException {
        Connection conn = JDBCConnection.getJDBCConnection();
        String sql = "select DISTINCT  Phanloai from NONGSAN ";

        PreparedStatement ppst = conn.prepareStatement(sql);
        ResultSet rs = ppst.executeQuery();

        while (rs.next()) {
            cbb.addItem(rs.getString("Phanloai"));
        }
    }
}
