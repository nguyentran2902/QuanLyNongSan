/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLNS.Controller;

import QLNS.Dao.HdDetailDao;
import QLNS.Model.HoaDonDeTail;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Admin
 */
public class HdDetailService {

    private HdDetailDao HdDetailDao;

    public HdDetailService() {
        HdDetailDao = new HdDetailDao();
    }

    public List<HoaDonDeTail> getListNongSanBan(String idHD) throws SQLException {
        return HdDetailDao.getListNongSanBan(idHD);
    }

   
}
