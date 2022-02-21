/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLNS.Controller;

import QLNS.Dao.HoaDonDao;
import QLNS.Model.HoaDon;
import QLNS.Model.HoaDonDeTail;
import QLNS.Model.NongSan;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Admin
 */
public class HoaDonService {

    private HoaDonDao hdDao;

    public HoaDonService() {
        hdDao = new HoaDonDao();
    }

    public List<HoaDon> getListHoaDon() throws SQLException {
        List<HoaDon> ListHd = hdDao.getListHoaDon();
        Collections.reverse(ListHd);
        return ListHd;
    }

    public List<HoaDonDeTail> getListHdDetail(String MaHDB) throws SQLException {
        return hdDao.getListHdDetail(MaHDB);
    }

    public Boolean deleteHoaDon(HoaDon hd, List<HoaDonDeTail> ListHdDetail) throws SQLException {
        return hdDao.deleteHoaDon(hd, ListHdDetail);
    }

    public HoaDon getHdById(String idHd) throws SQLException {
        return hdDao.getHdById(idHd);
    }

    public Boolean addHoaDonFinally(HoaDon hd, List<NongSan> ListNs) throws SQLException {
        return hdDao.addHoaDonFinally(hd, ListNs);

    }

    public List<HoaDon> getListHdByTime(Date date1, Date date2) throws SQLException {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd 23:59:59");

        String firstTime = date1 == null ? "" : sdf1.format(date1);
        String lastTime = sdf2.format(date2);
        List<HoaDon> ListHd = hdDao.getListHdByTime(firstTime, lastTime);
        Collections.reverse(ListHd);
        return ListHd;
    }

    public List<HoaDon> getListHdByNameNV(String name) throws SQLException {
        List<HoaDon> ListHd = hdDao.getListHdByNameNV(name);
        Collections.reverse(ListHd);
        return ListHd;
    }

    public List<HoaDon> getListHdByNameKH(String name) throws SQLException {
        List<HoaDon> ListHd = hdDao.getListHdByNameKH(name);
        Collections.reverse(ListHd);
        return ListHd;
    }

    public String SetNewIdHoaDon() throws SQLException {
        String soHd = "";
        SimpleDateFormat df = new SimpleDateFormat("yyMMdd");
        Date date = new Date();
        String hdTam = "HD" + df.format(date);

        int count = hdDao.CountSoHd(hdTam) == "" ? 0 : Integer.valueOf(hdDao.CountSoHd(hdTam).substring(8));
        if (count >= 99) {
            soHd = hdTam + (count + 1);
        } else if (count >= 9) {
            soHd = hdTam + "0" + (count + 1);
        } else {
            soHd = hdTam + "00" + (count + 1);
        }

        return soHd;
    }

    //reset soluong khi mua
    public List<NongSan> tempResetSoluong(List<NongSan> listNsFull, int idNS, float quantity) {

        for (NongSan ns : listNsFull) {
            if (ns.getIdNS() == idNS) {
                ns.setSoluongKho(ns.getSoluongKho() - quantity);
            }
        }
        return listNsFull;
    }

    //reset soluong khi huy
    public List<NongSan> tempResetSoluongCancel(List<NongSan> listNsFull, NongSan ns) {

        for (NongSan nss : listNsFull) {
            if (nss.getIdNS() == ns.getIdNS()) {
                nss.setSoluongKho(nss.getSoluongKho() + ns.getSoluongKho());
            }
        }
        return listNsFull;
    }

    //check soluong khi nhap mua
    public Boolean checkSoluong(List<NongSan> listNsFull, int idNS, float quantity) {

        for (NongSan ns : listNsFull) {
            if ((ns.getIdNS() == idNS) && (ns.getSoluongKho() - quantity >= 0)) {
                return true;
            }
        }
        return false;
    }

    public float getGiamGia(float tichluy) {
        float giamGia = 0;
        if (tichluy >= 10000000) {
            giamGia = 0.05f;
        } else if (tichluy > 5000000) {
            giamGia = 0.03f;
        } else if (tichluy > 3000000) {
            giamGia = 0.02f;
        } else if (tichluy > 1000000) {
            giamGia = 0.01f;
        }
        return giamGia;
    }
}
