/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLNS.Controller;

import QLNS.Dao.KhachHangDao;
import QLNS.Model.KhachHang;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class KhachHangService {

    private KhachHangDao KHDao;

    public KhachHangService() {
        KHDao = new KhachHangDao();
    }

    public List<KhachHang> getListKhachHang() throws SQLException {
        return KHDao.getListKhachHang();
    }

    public Boolean addKhachHang(KhachHang kh) throws SQLException {
        return KHDao.addKhachHang(kh);
    }

    public Boolean deleteKhachHang(String MaKH) throws SQLException {
        return KHDao.deleteKhachHang(MaKH);
    }

    public Boolean editKhachHang(KhachHang kh) throws SQLException {
        return KHDao.editKhachHang(kh);
    }
    
      public Boolean existKhachHangInHD(String MaKH) throws SQLException {
           return KHDao.existKhachHangInHD(MaKH);
      }

    public KhachHang getKhbyMaThe(String MaKH) throws SQLException {
        return KHDao.getKhbyMaThe(MaKH);
    }
    
    public KhachHang getKhbyNameAndHd(String name, String maHd) throws SQLException {
        return KHDao.getKhbyNameAndHd(name,maHd);
    }

    public List<KhachHang> searchKhachHangByName(List<KhachHang> listKH,String name) throws SQLException {
         List<KhachHang> listNew = new ArrayList();
         for(KhachHang kh:listKH) {
             if(kh.getTenKH().toUpperCase().contains(name.toUpperCase())) {
                listNew.add(kh);
            }
         }
         return listNew;
    }
    
     public List<KhachHang> searchKhachHangByName(String name) throws SQLException {
          return KHDao.searchKhachHangByName(name);
     }
      public List<KhachHang> searchKhachHangByMaThe(String mathe) throws SQLException {
          return KHDao.searchKhachHangByMaThe(mathe);
     }
    
     public String setNewMathe() throws SQLException {
         String maThe;
        int soThe = KHDao.CountSoMaThe()==""? 0:Integer.valueOf(KHDao.CountSoMaThe().substring(3));
        if (soThe >= 999) {
            maThe = "NSS" + (soThe + 1);
        } else if (soThe >= 99) {
            maThe = "NSS0" + (soThe + 1);
        } else if (soThe >= 9){
            maThe = "NSS00" + (soThe + 1);
        } else   maThe =  "NSS000" + (soThe + 1);

        return maThe;
    }

}
