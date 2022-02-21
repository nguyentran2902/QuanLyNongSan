/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLNS.Controller;

import QLNS.Dao.NhanVienDao;
import QLNS.Model.NhanVien;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Admin
 */
public class NhanVienService {

    private NhanVienDao nhanvienDao;

    public NhanVienService() {
        nhanvienDao = new NhanVienDao();
    }

    public NhanVien checkLogin(String user, String pass) throws SQLException {
        return nhanvienDao.checkLogin(user, pass);
    }

    public List<NhanVien> getListNhanVien() throws SQLException {
        return nhanvienDao.getListNhanVien();
    }

    public Boolean addNhanvien(NhanVien nv) throws SQLException {
        return nhanvienDao.addNhanvien(nv);
    }
     public Boolean editNhanvien(NhanVien nv) throws SQLException{
          return nhanvienDao.editNhanvien(nv);
     }
     
      public Boolean deleteNhanvien(String MaNV) throws SQLException {
           return nhanvienDao.deleteNhanvien(MaNV);
           
      }
      public NhanVien getNvbyId(String MaNV) throws SQLException {
             return nhanvienDao.getNvbyId(MaNV);
           
      }
      
        public Boolean existNhanVienInHD(String MaNV) throws SQLException {
            return nhanvienDao.existNhanVienInHD(MaNV);
        }
      
       public List<NhanVien> searchNhanVienByName(String name) throws SQLException {
             return nhanvienDao.searchNhanVienByName(name);
       }
       
         public Boolean changePassNv(String MaNV, String newPass) throws SQLException {
               return nhanvienDao.changePassNv(MaNV,newPass);
         }
}
