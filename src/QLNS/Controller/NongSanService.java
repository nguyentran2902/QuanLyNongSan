/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLNS.Controller;

import QLNS.Dao.NongSanDao;
import QLNS.Model.NongSan;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JComboBox;

/**
 *
 * @author Admin
 */
public class NongSanService {

    private NongSanDao nsDao;

    public NongSanService() {
        nsDao = new NongSanDao();
    }

    public List<NongSan> getListNongSan() throws SQLException {
        return nsDao.getListNongSan();
    }

    public Boolean addNongSan(NongSan ns) throws SQLException {
        return nsDao.addNongSan(ns);
    }

    public Boolean deleteNongSan(String MaNS) throws SQLException {
        return nsDao.deleteNongSan(MaNS);
    }

    public Boolean editNongSan(NongSan ns) throws SQLException {
        return nsDao.editNongSan(ns);
    }

    public NongSan getNSbyId(String MaNS) throws SQLException {
        return nsDao.getNsbyId(MaNS);
    }

    public List<NongSan> getListNSByTime(Date date1, Date date2) throws SQLException {
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd 23:59:59");
        String firstTime = date1 == null ? "" : sdf1.format(date1);
        String lastTime = sdf2.format(date2);
        return nsDao.getListNSByTime(firstTime, lastTime);
    }

    public Boolean existNongSanInHD(String MaNS) throws SQLException {
        return nsDao.existNongSanInHD(MaNS);
    }

    public List<NongSan> searchNongSanByNameFromDB(String name) throws SQLException {
        return nsDao.searchNongSanByNameFromDB(name);
    }

    public void RenderTypeNsToCbb(JComboBox cbb) throws SQLException {
        nsDao.RenderTypeNsToCbb(cbb);
    }

    public List<NongSan> searchNongSanByNameAndType(List<NongSan> ListNS, String name, String type) throws SQLException {
        List<NongSan> ListNsNew = new ArrayList();
        for (NongSan ns : ListNS) {
            if ((ns.getPhanLoai().equalsIgnoreCase(type)) && ns.getTenNS().toUpperCase().contains(name.toUpperCase())) {
                ListNsNew.add(ns);
            }
        }
        return ListNsNew;
    }

    public List<NongSan> searchNongSanByType(List<NongSan> ListNS, String type) throws SQLException {
        List<NongSan> ListNsNew = new ArrayList();
        for (NongSan ns : ListNS) {
            if (ns.getPhanLoai().equalsIgnoreCase(type)) {
                ListNsNew.add(ns);
            }
        }
        return ListNsNew;
    }

    public List<NongSan> searchNSbyName(List<NongSan> ListNS, String name) throws SQLException {
        List<NongSan> ListNsNew = new ArrayList();
        for (NongSan ns : ListNS) {
            if (ns.getTenNS().toUpperCase().contains(name.toUpperCase())) {
                ListNsNew.add(ns);
            }
        }
        return ListNsNew;
    }

    public List<NongSan> getListNsSluongASC(List<NongSan> list) throws SQLException {
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(j).getSoluongKho() < list.get(i).getSoluongKho()) {
                    NongSan ns = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, ns);
                }
            }

        }
        return list;
    }

    public List<NongSan> getListNsSluongDESC(List<NongSan> list) throws SQLException {
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(j).getSoluongKho() > list.get(i).getSoluongKho()) {
                    NongSan ns = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, ns);
                }
            }

        }
        return list;
    }

    public List<NongSan> getListNsGiaASC(List<NongSan> list) throws SQLException {
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(j).getDongiaBan() < list.get(i).getDongiaBan()) {
                    NongSan ns = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, ns);
                }
            }

        }
        return list;
    }

    public List<NongSan> getListNsGiaDESC(List<NongSan> list) throws SQLException {
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(j).getDongiaBan() > list.get(i).getDongiaBan()) {
                    NongSan ns = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, ns);
                }
            }

        }
        return list;
    }

    public byte[] toByteArray(Image img) throws IOException {
        BufferedImage bImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bImage.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "png", bos);

        byte[] data = bos.toByteArray();
        return data;
    }

    public Image createImageFromByteArray(byte[] data) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        BufferedImage bImage = ImageIO.read(bis);

        Image img = bImage.getScaledInstance(bImage.getWidth(), bImage.getHeight(), bImage.SCALE_SMOOTH);

        return img;
    }
}
