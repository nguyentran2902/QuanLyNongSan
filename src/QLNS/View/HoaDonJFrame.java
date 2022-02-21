/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package QLNS.View;

import QLNS.Controller.HdDetailService;
import QLNS.Controller.HoaDonService;
import QLNS.Controller.KhachHangService;
import QLNS.Controller.NhanVienService;
import QLNS.Controller.NongSanService;
import QLNS.Model.HoaDon;
import QLNS.Model.HoaDonDeTail;
import QLNS.Model.KhachHang;
import QLNS.Model.NhanVien;
import QLNS.Model.NongSan;
import QLNS.Threading.ClockThread;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Admin
 */
public class HoaDonJFrame extends javax.swing.JFrame {

    private DefaultTableModel dtfListNongsan;
    private DefaultTableModel dtfListHoaDon;
    private DefaultTableModel dtfHoaDonAdd;
    private DefaultTableModel dtfNongSanAdd;
    private DefaultTableModel dtfKhachHang;
    private HoaDonService hdService;
    private HdDetailService hdDetailService;
    private KhachHangService khService;
    private NhanVienService nvService;
    private NongSanService nsService;

    private Locale localeVN = new Locale("vi", "VN");
    private NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private List<NongSan> listNsFull;
    private List<NongSan> listNsAdd = new ArrayList();

    /**
     * Creates new form HoaDonJFrame
     */
    public HoaDonJFrame() throws SQLException {

        initComponents();
        initClock();
        setLocationRelativeTo(null);
        renderTableHoadonAdd();
        refeshTotalPrice();
        refreshSoHd();

        txtNameLogin.setText(LoginForm.nvLogIn.getTenNV());
        txt_AddHd_Nvban.setText(LoginForm.nvLogIn.getTenNV());
        txtBophan.setText(LoginForm.nvLogIn.getBoPhan());
        nsService = new NongSanService();
        listNsFull = nsService.getListNongSan();
        renderTableNSAdd();
        nsService.RenderTypeNsToCbb(cbbTypeNs);
        btn_QlHd_Delete.setEnabled(false);

    }

    private void refreshSoHd() throws SQLException {
        String soHd = hdService.SetNewIdHoaDon();
        txt_addHD_IdHd.setText(soHd);
    }

    private void initClock() {
        ClockThread th1 = new ClockThread(lblClock, "hh:mm:ss aa");
        ClockThread th2 = new ClockThread(txt_Addhd_ngayTao, "dd/MM/yyyy HH:mm:ss");
        th1.start();
        th2.start();

    }

    private void refeshTotalPrice() {
        float giamGia = 0;
        if (txt_AddHd_TichLuy.getText() != "") {
            try {
                giamGia = hdService.getGiamGia((float) (long) currencyVN.parse(txt_AddHd_TichLuy.getText()));
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }

        float tongGiaBan = 0;
        for (NongSan ns : listNsAdd) {
            tongGiaBan += ns.getSoluongKho() * ns.getDongiaBan();
        }
        float thanhTien = tongGiaBan - giamGia * tongGiaBan;
        txt_AddHd_tongTien.setText(currencyVN.format(tongGiaBan));
        txt_AddHd_giamGia.setText("- " + currencyVN.format(giamGia * tongGiaBan));
        txt_AddHd_thanhTien.setText(currencyVN.format(thanhTien));
    }

    public void renderTableHoadon() throws SQLException {
        hdService = new HoaDonService();
        khService = new KhachHangService();
        nvService = new NhanVienService();
        dtfListHoaDon = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblListHoaDon.setModel(dtfListHoaDon);

        dtfListHoaDon.addColumn("STT");
        dtfListHoaDon.addColumn("ID Hóa đơn");
        dtfListHoaDon.addColumn("Tên Thành viên");
        dtfListHoaDon.addColumn("Nhân viên bán");
        dtfListHoaDon.addColumn("Ngày bán");
        dtfListHoaDon.addColumn("Thành tiền");

        setDataTableHoaDon(hdService.getListHoaDon());

        //setup header Table
        DefaultTableCellRenderer tbl_Header = new DefaultTableCellRenderer();
        tbl_Header.setHorizontalAlignment(JLabel.CENTER);
        tbl_Header.setFont(new Font("Arial", Font.BOLD, (int) 13));
        tbl_Header.setPreferredSize(new Dimension(100, 35));
        tbl_Header.setBackground(new Color(0, 161, 12));
        tbl_Header.setForeground(Color.white);

        tblListHoaDon.getTableHeader().setDefaultRenderer(tbl_Header);

        //setup row Table
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblListHoaDon.setDefaultRenderer(Object.class, centerRenderer);
        tblListHoaDon.setAutoResizeMode(tblListHoaDon.AUTO_RESIZE_NEXT_COLUMN);
        TableColumnModel colModel = tblListHoaDon.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(10);
        colModel.getColumn(1).setPreferredWidth(50);

        colModel.getColumn(4).setPreferredWidth(100);
        colModel.getColumn(5).setPreferredWidth(50);

        setRowDataHD(0);

    }

    private void setDataTableHoaDon(List<HoaDon> listHD) throws SQLException {
        dtfListHoaDon.setRowCount(0);
        for (int i = 0; i < listHD.size(); i++) {
            KhachHang kh = khService.getKhbyMaThe(listHD.get(i).getMaThe());
            NhanVien nv = nvService.getNvbyId(listHD.get(i).getIdNV() + "");
            String ngayban = sdf.format(listHD.get(i).getNgayBan());

            dtfListHoaDon.addRow(new Object[]{i + 1, listHD.get(i).getIdHD(), kh.getTenKH(),
                nv.getTenNV(), ngayban, currencyVN.format(listHD.get(i).getThanhTien())});
        }
        tblListHoaDon.requestFocus();
        tblListHoaDon.changeSelection(0, 0, false, false);
    }

    public void setRowDataHD(int row) throws SQLException {
        if (tblListHoaDon.getRowCount() > 0) {
            if (LoginForm.nvLogIn.getTenNV().equalsIgnoreCase(tblListHoaDon.getValueAt(row, 3) + "")
                    || LoginForm.nvLogIn.getBoPhan().equalsIgnoreCase("Quản lý")) {
                btn_QlHd_Delete.setEnabled(true);
            } else {
                btn_QlHd_Delete.setEnabled(false);
            }
            hdDetailService = new HdDetailService();
            nsService = new NongSanService();
            HoaDon hdb = hdService.getHdById(tblListHoaDon.getValueAt(row, 1) + "");

            txt_QlHd_IdHd.setText(hdb.getIdHD());
            txtTenNv.setText(tblListHoaDon.getValueAt(row, 3) + "");
            txtNgaytao.setText(tblListHoaDon.getValueAt(row, 4) + "");
            txt_QlHd_tongTien.setText(currencyVN.format(hdb.getTongTienBan()) + "");
            txt_QlHd_thanhTien.setText(currencyVN.format(hdb.getThanhTien()) + "");
            txt_QlHd_giamGia.setText("- " + currencyVN.format(hdb.getGiamGia()) + "");

            KhachHang kh = khService.getKhbyNameAndHd(tblListHoaDon.getValueAt(row, 2) + "", tblListHoaDon.getValueAt(row, 1) + "");

            if (kh != null) {
                txt_QlHd_TenKh.setText(kh.getTenKH());
                txt_QlHd_tichLuy.setText(currencyVN.format(kh.getTichLuy()));
                txt_QlHd_address.setText(kh.getDiachiKH());
                txt_QlHd_Phone.setText(kh.getSdtKH());
                txt_QlHd_maThe.setText(kh.getMaThe());

            }

            renderTableNongSan(tblListHoaDon.getValueAt(row, 1) + "");

        }
    }

    public void renderTableNongSan(String maHD) throws SQLException {
        dtfListNongsan = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblListNongSan.setModel(dtfListNongsan);

        dtfListNongsan.addColumn("STT");
        dtfListNongsan.addColumn("Id NS");
        dtfListNongsan.addColumn("Tên Nông Sản");
        dtfListNongsan.addColumn("Đơn vị tính");
        dtfListNongsan.addColumn("Số lượng");
        dtfListNongsan.addColumn("Giá mua");
        dtfListNongsan.addColumn("Thành tiền");

        List<HoaDonDeTail> listHdDetail = hdDetailService.getListNongSanBan(maHD);

        for (int i = 0; i < listHdDetail.size(); i++) {
            NongSan ns = nsService.getNSbyId(listHdDetail.get(i).getIdNS() + "");
            dtfListNongsan.addRow(new Object[]{i + 1, ns.getIdNS(), ns.getTenNS(), ns.getDonviTinh(), listHdDetail.get(i).getSoluongBan(),
                currencyVN.format(listHdDetail.get(i).getDongiaMua()), currencyVN.format(listHdDetail.get(i).getThanhTien())});
        }

       
        //setup header Table
        DefaultTableCellRenderer tbl_Header = new DefaultTableCellRenderer();
        tbl_Header.setHorizontalAlignment(JLabel.CENTER);
        tbl_Header.setFont(new Font("Arial", Font.BOLD, (int) 13));
        tbl_Header.setPreferredSize(new Dimension(100, 35));
        tbl_Header.setBackground(new Color(0,161,12));
        tbl_Header.setForeground(Color.white);

        tblListNongSan.getTableHeader().setDefaultRenderer(tbl_Header);

        //setup row Table
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblListNongSan.setDefaultRenderer(Object.class, centerRenderer);
        tblListNongSan.setAutoResizeMode(tblListNongSan.AUTO_RESIZE_NEXT_COLUMN);
        TableColumnModel colModel = tblListNongSan.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(30);
        colModel.getColumn(1).setPreferredWidth(30);
        colModel.getColumn(2).setPreferredWidth(100);

    }

    public void renderTableNSAdd() throws SQLException {
        hdService = new HoaDonService();
        dtfNongSanAdd = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblNongSanAdd.setModel(dtfNongSanAdd);

        dtfNongSanAdd.addColumn("Id NS");
        dtfNongSanAdd.addColumn("Tên Nông sản");
        dtfNongSanAdd.addColumn("Số lượng kho");
        dtfNongSanAdd.addColumn("Đơn vị tính");
        dtfNongSanAdd.addColumn("Giá bán");

        setDataTableNongSanAdd(listNsFull);

        //setup header Table
        DefaultTableCellRenderer tbl_Header = new DefaultTableCellRenderer();
        tbl_Header.setHorizontalAlignment(JLabel.CENTER);
        tbl_Header.setFont(new Font("Arial", Font.BOLD, (int) 13));
        tbl_Header.setPreferredSize(new Dimension(100, 35));
        tbl_Header.setBackground(new Color(0, 161, 12));
        tbl_Header.setForeground(Color.white);

        tblNongSanAdd.getTableHeader().setDefaultRenderer(tbl_Header);

        //setup row Table
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblNongSanAdd.setDefaultRenderer(Object.class, centerRenderer);
        tblNongSanAdd.setAutoResizeMode(tblNongSanAdd.AUTO_RESIZE_NEXT_COLUMN);
        TableColumnModel colModel = tblNongSanAdd.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(30);

    }

    private void setDataTableNongSanAdd(List<NongSan> listNs) throws SQLException {
        dtfNongSanAdd.setRowCount(0);
        for (NongSan ns : listNs) {

            dtfNongSanAdd.addRow(new Object[]{ns.getIdNS(), ns.getTenNS(), ns.getSoluongKho(),
                ns.getDonviTinh(), currencyVN.format(ns.getDongiaBan())});

        }
    }

    public void renderTableHoadonAdd() throws SQLException {
        hdService = new HoaDonService();
        dtfHoaDonAdd = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblHoaDonAdd.setModel(dtfHoaDonAdd);

        dtfHoaDonAdd.addColumn("STT");
        dtfHoaDonAdd.addColumn("Id NS");
        dtfHoaDonAdd.addColumn("Tên Nông sản");
        dtfHoaDonAdd.addColumn("Đơn vị tính");
        dtfHoaDonAdd.addColumn("Số lượng");
        dtfHoaDonAdd.addColumn("Giá bán");
        dtfHoaDonAdd.addColumn("Thành tiền");

        //setup header Table
        DefaultTableCellRenderer tbl_Header = new DefaultTableCellRenderer();
        tbl_Header.setHorizontalAlignment(JLabel.CENTER);
        tbl_Header.setFont(new Font("Arial", Font.BOLD, (int) 13));
        tbl_Header.setPreferredSize(new Dimension(100, 35));
        tbl_Header.setBackground(new Color(0, 161, 12));
        tbl_Header.setForeground(Color.white);

        tblHoaDonAdd.getTableHeader().setDefaultRenderer(tbl_Header);

        //setup row Table
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblHoaDonAdd.setDefaultRenderer(Object.class, centerRenderer);
        tblHoaDonAdd.setAutoResizeMode(tblHoaDonAdd.AUTO_RESIZE_NEXT_COLUMN);
        TableColumnModel colModel = tblHoaDonAdd.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(30);
        colModel.getColumn(1).setPreferredWidth(50);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialogAddSl = new javax.swing.JDialog();
        jLabel22 = new javax.swing.JLabel();
        lbl_DialogNameNs = new javax.swing.JLabel();
        txt_DialogSlNs = new javax.swing.JTextField();
        lbl_DialogDvtNS = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        btn_DialogAddNs = new javax.swing.JButton();
        btn_Dialog_EditSl = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        btn_DialogCancel = new javax.swing.JButton();
        dialogChooseKH = new javax.swing.JDialog();
        jPanel7 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        txtSearchKh = new javax.swing.JTextField();
        btnAddKh = new javax.swing.JButton();
        btnCancelKh = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblKhachHang = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        cbb_DialogKh_search = new javax.swing.JComboBox<>();
        menuEditNs = new javax.swing.JPopupMenu();
        menuItemEdit = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        menuItemXoa = new javax.swing.JMenuItem();
        dialog_AddNewKh = new javax.swing.JDialog();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txt_AddKh_maThe = new javax.swing.JLabel();
        txt_AddKh_name = new javax.swing.JTextField();
        txt_AddKh_cccd = new javax.swing.JTextField();
        txt_AddKh_Address = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        txt_AddKh_phone = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        btnBack = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblClock = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        btnAddHd = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        btnQlHd = new javax.swing.JButton();
        txtNameLogin = new javax.swing.JLabel();
        txtBophan = new javax.swing.JLabel();
        pnlCard = new javax.swing.JPanel();
        pnlAddHd = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        txtSearchNs = new javax.swing.JTextField();
        cbbTypeNs = new javax.swing.JComboBox<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblNongSanAdd = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblHoaDonAdd = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        txt_addHD_IdHd = new javax.swing.JLabel();
        txt_Addhd_ngayTao = new javax.swing.JLabel();
        txt_AddHd_Nvban = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txt_AddHd_tongTien = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        ckbThanhVien = new javax.swing.JCheckBox();
        jLabel30 = new javax.swing.JLabel();
        txt_AddHd_giamGia = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        txt_AddHd_thanhTien = new javax.swing.JLabel();
        txt_AddHd_MaThe = new javax.swing.JLabel();
        txt_AddHd_NameKh = new javax.swing.JLabel();
        txt_AddHd_Address = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txt_AddHd_TichLuy = new javax.swing.JLabel();
        txt_AddHd_Phone = new javax.swing.JLabel();
        pnlQlHd = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblListHoaDon = new javax.swing.JTable();
        cbbSearch = new javax.swing.JComboBox<>();
        txtSearchHd = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblListNongSan = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        txt_QlHd_tongTien = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtNgaytao = new javax.swing.JLabel();
        txt_QlHd_IdHd = new javax.swing.JLabel();
        txtTenNv = new javax.swing.JLabel();
        txt_QlHd_TenKh = new javax.swing.JLabel();
        txt_QlHd_Phone = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txt_QlHd_maThe = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        txt_QlHd_giamGia = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        txt_QlHd_thanhTien = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        btn_QlHd_Delete = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        txt_QlHd_address = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        txt_QlHd_tichLuy = new javax.swing.JLabel();

        dialogAddSl.setTitle("Nhập số lượng mua");
        dialogAddSl.setMinimumSize(new java.awt.Dimension(500, 251));

        jLabel22.setBackground(new java.awt.Color(0, 102, 0));
        jLabel22.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 102, 0));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("NHẬP SỐ LƯỢNG");

        lbl_DialogNameNs.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lbl_DialogNameNs.setForeground(new java.awt.Color(0, 102, 0));
        lbl_DialogNameNs.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_DialogNameNs.setText("Tên nông sản");

        lbl_DialogDvtNS.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        lbl_DialogDvtNS.setForeground(new java.awt.Color(0, 102, 0));
        lbl_DialogDvtNS.setText("(dvt)");

        btn_DialogAddNs.setBackground(new java.awt.Color(0, 153, 0));
        btn_DialogAddNs.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btn_DialogAddNs.setForeground(new java.awt.Color(255, 255, 255));
        btn_DialogAddNs.setText("+ Thêm");
        btn_DialogAddNs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DialogAddNsActionPerformed(evt);
            }
        });

        btn_Dialog_EditSl.setBackground(new java.awt.Color(255, 153, 0));
        btn_Dialog_EditSl.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        btn_Dialog_EditSl.setForeground(new java.awt.Color(255, 255, 255));
        btn_Dialog_EditSl.setText("Sửa");
        btn_Dialog_EditSl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Dialog_EditSlActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(105, 105, 105)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_DialogAddNs, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Dialog_EditSl, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(btn_DialogAddNs, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btn_Dialog_EditSl, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        btn_DialogCancel.setBackground(new java.awt.Color(255, 0, 0));
        btn_DialogCancel.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btn_DialogCancel.setForeground(new java.awt.Color(255, 255, 255));
        btn_DialogCancel.setText("Hủy");
        btn_DialogCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_DialogCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(btn_DialogCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(btn_DialogCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dialogAddSlLayout = new javax.swing.GroupLayout(dialogAddSl.getContentPane());
        dialogAddSl.getContentPane().setLayout(dialogAddSlLayout);
        dialogAddSlLayout.setHorizontalGroup(
            dialogAddSlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(dialogAddSlLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(lbl_DialogNameNs, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txt_DialogSlNs, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_DialogDvtNS, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
            .addGroup(dialogAddSlLayout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dialogAddSlLayout.setVerticalGroup(
            dialogAddSlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(dialogAddSlLayout.createSequentialGroup()
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(dialogAddSlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_DialogSlNs, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_DialogNameNs, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_DialogDvtNS, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(dialogAddSlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        dialogChooseKH.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        dialogChooseKH.setTitle("KHÁCH HÀNG");
        dialogChooseKH.setMinimumSize(new java.awt.Dimension(860, 550));

        jPanel7.setMinimumSize(new java.awt.Dimension(860, 550));
        jPanel7.setPreferredSize(new java.awt.Dimension(839, 518));

        jLabel23.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 102, 0));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("DANH SÁCH KHÁCH HÀNG");

        txtSearchKh.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        txtSearchKh.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchKhKeyReleased(evt);
            }
        });

        btnAddKh.setBackground(new java.awt.Color(0, 102, 204));
        btnAddKh.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        btnAddKh.setForeground(new java.awt.Color(255, 255, 255));
        btnAddKh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/select-icon.png"))); // NOI18N
        btnAddKh.setText("Chọn");
        btnAddKh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddKhActionPerformed(evt);
            }
        });

        btnCancelKh.setBackground(new java.awt.Color(204, 0, 0));
        btnCancelKh.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        btnCancelKh.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelKh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/baseline_backspace_white_24dp.png"))); // NOI18N
        btnCancelKh.setText("Hủy");
        btnCancelKh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelKhActionPerformed(evt);
            }
        });

        tblKhachHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblKhachHang.setRowHeight(26);
        tblKhachHang.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane6.setViewportView(tblKhachHang);

        jButton4.setBackground(new java.awt.Color(0, 153, 0));
        jButton4.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/baseline_queue_white_24dp.png"))); // NOI18N
        jButton4.setText("Thêm mới");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        cbb_DialogKh_search.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        cbb_DialogKh_search.setForeground(new java.awt.Color(0, 102, 0));
        cbb_DialogKh_search.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tìm kiếm theo tên", "Tìm kiếm theo mã thẻ" }));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(232, 232, 232)
                        .addComponent(btnAddKh, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(100, 100, 100)
                        .addComponent(btnCancelKh, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(cbb_DialogKh_search, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtSearchKh, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 789, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbb_DialogKh_search, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSearchKh, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(34, 34, 34)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddKh, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancelKh, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );

        javax.swing.GroupLayout dialogChooseKHLayout = new javax.swing.GroupLayout(dialogChooseKH.getContentPane());
        dialogChooseKH.getContentPane().setLayout(dialogChooseKHLayout);
        dialogChooseKHLayout.setHorizontalGroup(
            dialogChooseKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogChooseKHLayout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        dialogChooseKHLayout.setVerticalGroup(
            dialogChooseKHLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogChooseKHLayout.createSequentialGroup()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 17, Short.MAX_VALUE))
        );

        menuEditNs.setAlignmentY(0.0F);
        menuEditNs.setLightWeightPopupEnabled(false);
        menuEditNs.setVerifyInputWhenFocusTarget(false);

        menuItemEdit.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        menuItemEdit.setForeground(new java.awt.Color(255, 204, 0));
        menuItemEdit.setText("Sửa số lượng");
        menuItemEdit.setAlignmentX(0.0F);
        menuItemEdit.setAlignmentY(0.0F);
        menuItemEdit.setMargin(new java.awt.Insets(0, 0, 0, 0));
        menuItemEdit.setPreferredSize(new java.awt.Dimension(119, 32));
        menuItemEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemEditActionPerformed(evt);
            }
        });
        menuEditNs.add(menuItemEdit);
        menuEditNs.add(jSeparator2);

        menuItemXoa.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        menuItemXoa.setForeground(new java.awt.Color(204, 0, 0));
        menuItemXoa.setText("Xóa");
        menuItemXoa.setPreferredSize(new java.awt.Dimension(119, 32));
        menuItemXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemXoaActionPerformed(evt);
            }
        });
        menuEditNs.add(menuItemXoa);

        dialog_AddNewKh.setTitle("Tạo thành viên mới");
        dialog_AddNewKh.setMinimumSize(new java.awt.Dimension(550, 550));

        jPanel12.setMinimumSize(new java.awt.Dimension(550, 550));
        jPanel12.setName(""); // NOI18N

        jPanel13.setBackground(new java.awt.Color(0, 161, 12));

        jLabel24.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("TẠO THẺ THÀNH VIÊN");

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
        );

        jLabel25.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 102, 0));
        jLabel25.setText("Mã thẻ:");

        jLabel27.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(0, 102, 0));
        jLabel27.setText("Họ và tên:");

        jLabel29.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(0, 102, 0));
        jLabel29.setText("Số CCCD:");

        jLabel31.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 102, 0));
        jLabel31.setText("Địa chỉ:");

        txt_AddKh_maThe.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_AddKh_maThe.setForeground(new java.awt.Color(0, 102, 255));

        txt_AddKh_name.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_AddKh_name.setForeground(new java.awt.Color(0, 102, 255));

        txt_AddKh_cccd.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_AddKh_cccd.setForeground(new java.awt.Color(0, 102, 255));

        txt_AddKh_Address.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_AddKh_Address.setForeground(new java.awt.Color(0, 102, 255));

        jButton7.setBackground(new java.awt.Color(0, 153, 0));
        jButton7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/baseline_queue_white_24dp.png"))); // NOI18N
        jButton7.setText("Thêm mới");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(255, 0, 0));
        jButton8.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/baseline_backspace_white_24dp.png"))); // NOI18N
        jButton8.setText("Hủy");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(0, 102, 0));
        jLabel35.setText("Điện thoại:");

        txt_AddKh_phone.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_AddKh_phone.setForeground(new java.awt.Color(0, 102, 255));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel25)
                            .addComponent(jLabel27)
                            .addComponent(jLabel29)
                            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabel35, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel31, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_AddKh_maThe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_AddKh_name)
                            .addComponent(txt_AddKh_cccd)
                            .addComponent(txt_AddKh_Address)
                            .addComponent(txt_AddKh_phone, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGap(126, 126, 126)
                        .addComponent(jButton7)
                        .addGap(55, 55, 55)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(113, Short.MAX_VALUE))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_AddKh_maThe, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_AddKh_name, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_AddKh_cccd, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_AddKh_Address, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_AddKh_phone, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60))
        );

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dialog_AddNewKhLayout = new javax.swing.GroupLayout(dialog_AddNewKh.getContentPane());
        dialog_AddNewKh.getContentPane().setLayout(dialog_AddNewKhLayout);
        dialog_AddNewKhLayout.setHorizontalGroup(
            dialog_AddNewKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        dialog_AddNewKhLayout.setVerticalGroup(
            dialog_AddNewKhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("QUẢN LÝ HÓA ĐƠN");
        setBackground(new java.awt.Color(204, 221, 251));

        jPanel1.setBackground(new java.awt.Color(0, 161, 12));

        btnBack.setBackground(new java.awt.Color(255, 252, 248));
        btnBack.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/baseline_reply_black_18dp.png"))); // NOI18N
        btnBack.setText("Trở về");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("QUẢN LÝ HÓA ĐƠN");

        lblClock.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lblClock.setForeground(new java.awt.Color(255, 255, 255));
        lblClock.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblClock.setText("8:8:8 AM");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblClock, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 20, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBack, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblClock, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel2.setBackground(new java.awt.Color(204, 194, 244));

        jToolBar1.setBackground(new java.awt.Color(204, 194, 244));
        jToolBar1.setRollover(true);

        btnAddHd.setBackground(new java.awt.Color(204, 194, 244));
        btnAddHd.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnAddHd.setForeground(new java.awt.Color(0, 102, 0));
        btnAddHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/add-hd.png"))); // NOI18N
        btnAddHd.setText("Thêm hóa đơn");
        btnAddHd.setFocusable(false);
        btnAddHd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAddHd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddHdActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAddHd);
        jToolBar1.add(jSeparator1);

        btnQlHd.setBackground(new java.awt.Color(204, 194, 244));
        btnQlHd.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnQlHd.setForeground(new java.awt.Color(0, 102, 0));
        btnQlHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/edit-hd.png"))); // NOI18N
        btnQlHd.setText("Quản lý hóa đơn");
        btnQlHd.setFocusable(false);
        btnQlHd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnQlHd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnQlHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQlHdActionPerformed(evt);
            }
        });
        jToolBar1.add(btnQlHd);

        txtNameLogin.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtNameLogin.setForeground(new java.awt.Color(0, 102, 255));
        txtNameLogin.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        txtBophan.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtBophan.setForeground(new java.awt.Color(0, 102, 255));
        txtBophan.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNameLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                    .addComponent(txtBophan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtNameLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBophan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pnlCard.setBackground(new java.awt.Color(204, 221, 251));
        pnlCard.setLayout(new java.awt.CardLayout());

        pnlAddHd.setBackground(new java.awt.Color(204, 221, 251));

        jPanel5.setBackground(new java.awt.Color(204, 221, 251));

        jLabel16.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 102, 0));
        jLabel16.setText("Tìm kiếm:");

        txtSearchNs.setBackground(new java.awt.Color(255, 252, 248));
        txtSearchNs.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtSearchNs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchNsKeyReleased(evt);
            }
        });

        cbbTypeNs.setBackground(new java.awt.Color(255, 252, 248));
        cbbTypeNs.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        cbbTypeNs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả" }));
        cbbTypeNs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbTypeNsActionPerformed(evt);
            }
        });

        jScrollPane4.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 153, 0)));

        tblNongSanAdd.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        tblNongSanAdd.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblNongSanAdd.setRowHeight(28);
        tblNongSanAdd.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblNongSanAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNongSanAddMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblNongSanAdd);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 541, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSearchNs, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbbTypeNs, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSearchNs, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cbbTypeNs, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jPanel6.setBackground(new java.awt.Color(204, 221, 251));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 0));
        jLabel2.setText("Id hóa đơn:");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 0));
        jLabel3.setText("Ngày tạo:");

        jLabel11.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 102, 0));
        jLabel11.setText("Nhân viên bán:");

        jLabel13.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 102, 0));
        jLabel13.setText("Mã thẻ:");

        jLabel14.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 102, 0));
        jLabel14.setText("Tên KH:");

        jLabel15.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 102, 0));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Địa chỉ:");

        jScrollPane3.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 153, 0)));

        tblHoaDonAdd.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        tblHoaDonAdd.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblHoaDonAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblHoaDonAdd.setRowHeight(28);
        tblHoaDonAdd.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblHoaDonAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonAddMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblHoaDonAdd);

        jButton1.setBackground(new java.awt.Color(0, 153, 0));
        jButton1.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/baseline_save_white_24dp.png"))); // NOI18N
        jButton1.setText("Lưu hóa đơn");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 153, 0));
        jButton2.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/print-hd.png"))); // NOI18N
        jButton2.setText("In hóa đơn");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(0, 102, 204));
        jButton3.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/baseline_cached_white_24dp.png"))); // NOI18N
        jButton3.setText("Làm mới");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        txt_addHD_IdHd.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_addHD_IdHd.setForeground(new java.awt.Color(0, 102, 255));
        txt_addHD_IdHd.setText("jLabel17");

        txt_Addhd_ngayTao.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_Addhd_ngayTao.setForeground(new java.awt.Color(0, 102, 255));
        txt_Addhd_ngayTao.setText("jLabel18");

        txt_AddHd_Nvban.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_AddHd_Nvban.setForeground(new java.awt.Color(0, 102, 255));
        txt_AddHd_Nvban.setText("jLabel19");

        jLabel20.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 102, 0));
        jLabel20.setText("Tổng tiền:");

        txt_AddHd_tongTien.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        txt_AddHd_tongTien.setForeground(new java.awt.Color(255, 0, 0));
        txt_AddHd_tongTien.setText("jLabel21");

        jPanel8.setBackground(new java.awt.Color(0, 153, 0));

        ckbThanhVien.setBackground(new java.awt.Color(0, 153, 0));
        ckbThanhVien.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        ckbThanhVien.setForeground(new java.awt.Color(255, 255, 255));
        ckbThanhVien.setText("Thành viên");
        ckbThanhVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ckbThanhVienActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ckbThanhVien, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(ckbThanhVien, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel30.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(0, 102, 0));
        jLabel30.setText("Giảm giá:");

        txt_AddHd_giamGia.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        txt_AddHd_giamGia.setForeground(new java.awt.Color(255, 0, 0));
        txt_AddHd_giamGia.setText("jLabel31");

        jLabel32.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 102, 0));
        jLabel32.setText("Thành tiền:");

        txt_AddHd_thanhTien.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        txt_AddHd_thanhTien.setForeground(new java.awt.Color(255, 0, 0));
        txt_AddHd_thanhTien.setText("jLabel33");

        txt_AddHd_MaThe.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_AddHd_MaThe.setForeground(new java.awt.Color(0, 102, 255));

        txt_AddHd_NameKh.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_AddHd_NameKh.setForeground(new java.awt.Color(0, 102, 255));

        txt_AddHd_Address.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_AddHd_Address.setForeground(new java.awt.Color(0, 102, 255));

        jLabel17.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 102, 0));
        jLabel17.setText("Tích lũy:");

        jLabel18.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 102, 0));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("SĐT:");

        txt_AddHd_TichLuy.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_AddHd_TichLuy.setForeground(new java.awt.Color(255, 0, 0));

        txt_AddHd_Phone.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_AddHd_Phone.setForeground(new java.awt.Color(0, 102, 255));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_AddHd_tongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jButton1)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txt_AddHd_giamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(jLabel32)
                        .addGap(18, 18, 18)
                        .addComponent(txt_AddHd_thanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(37, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_AddHd_Nvban, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_addHD_IdHd, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_Addhd_ngayTao, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_AddHd_TichLuy, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_AddHd_MaThe, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_AddHd_Address, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_AddHd_NameKh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_AddHd_Phone, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(15, 15, 15))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txt_addHD_IdHd, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_AddHd_NameKh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_AddHd_Address, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_AddHd_MaThe, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txt_Addhd_ngayTao, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_AddHd_Nvban, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_AddHd_TichLuy, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_AddHd_Phone, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_AddHd_tongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_AddHd_giamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_AddHd_thanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34))
        );

        javax.swing.GroupLayout pnlAddHdLayout = new javax.swing.GroupLayout(pnlAddHd);
        pnlAddHd.setLayout(pnlAddHdLayout);
        pnlAddHdLayout.setHorizontalGroup(
            pnlAddHdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAddHdLayout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(20, 20, 20)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );
        pnlAddHdLayout.setVerticalGroup(
            pnlAddHdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAddHdLayout.createSequentialGroup()
                .addGroup(pnlAddHdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlCard.add(pnlAddHd, "card3");

        pnlQlHd.setBackground(new java.awt.Color(204, 221, 251));

        jPanel3.setBackground(new java.awt.Color(204, 221, 251));

        jScrollPane1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 153, 0)));

        tblListHoaDon.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        tblListHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblListHoaDon.setRowHeight(28);
        tblListHoaDon.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblListHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblListHoaDonMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblListHoaDon);

        cbbSearch.setBackground(new java.awt.Color(255, 252, 248));
        cbbSearch.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cbbSearch.setForeground(new java.awt.Color(0, 102, 0));
        cbbSearch.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tìm kiếm theo tên Khách hàng", "Tìm kiếm theo tên Nhân viên" }));
        cbbSearch.setPreferredSize(new java.awt.Dimension(202, 35));

        txtSearchHd.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtSearchHd.setForeground(new java.awt.Color(0, 102, 255));
        txtSearchHd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtSearchHdKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 666, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(cbbSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(93, 93, 93)
                        .addComponent(txtSearchHd)))
                .addGap(15, 15, 15))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbbSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearchHd, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(204, 221, 251));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 0));
        jLabel4.setText("Id hóa đơn:");

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 102, 0));
        jLabel5.setText("Ngày tạo:");

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 102, 0));
        jLabel6.setText("Danh sách sản phẩm đã mua:");

        jLabel7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 102, 0));
        jLabel7.setText("Tên khách hàng:");

        jLabel8.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 102, 0));
        jLabel8.setText("Địa chỉ:");

        jLabel9.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 102, 0));
        jLabel9.setText("Số điện thoại:");

        jScrollPane2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 153, 0)));

        tblListNongSan.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        tblListNongSan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblListNongSan.setRowHeight(28);
        tblListNongSan.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(tblListNongSan);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 102, 0));
        jLabel10.setText("Tổng tiền:");

        txt_QlHd_tongTien.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_QlHd_tongTien.setForeground(new java.awt.Color(255, 0, 0));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 102, 0));
        jLabel12.setText("Nhân viên bán:");

        txtNgaytao.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtNgaytao.setForeground(new java.awt.Color(0, 102, 255));

        txt_QlHd_IdHd.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_QlHd_IdHd.setForeground(new java.awt.Color(0, 102, 255));

        txtTenNv.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtTenNv.setForeground(new java.awt.Color(0, 102, 255));

        txt_QlHd_TenKh.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_QlHd_TenKh.setForeground(new java.awt.Color(0, 102, 255));

        txt_QlHd_Phone.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_QlHd_Phone.setForeground(new java.awt.Color(0, 102, 255));

        jLabel21.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 102, 0));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Mã thẻ:");

        txt_QlHd_maThe.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_QlHd_maThe.setForeground(new java.awt.Color(0, 102, 255));

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 102, 0));
        jLabel26.setText("Giảm giá:");

        txt_QlHd_giamGia.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_QlHd_giamGia.setForeground(new java.awt.Color(255, 0, 0));

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(0, 102, 0));
        jLabel28.setText("Thành tiền:");

        txt_QlHd_thanhTien.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txt_QlHd_thanhTien.setForeground(new java.awt.Color(255, 0, 0));

        jButton5.setBackground(new java.awt.Color(255, 153, 0));
        jButton5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/print-hd.png"))); // NOI18N
        jButton5.setText("In Hóa đơn");

        btn_QlHd_Delete.setBackground(new java.awt.Color(255, 0, 0));
        btn_QlHd_Delete.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btn_QlHd_Delete.setForeground(new java.awt.Color(255, 255, 255));
        btn_QlHd_Delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/baseline_delete_forever_white_24dp.png"))); // NOI18N
        btn_QlHd_Delete.setText("Xóa");
        btn_QlHd_Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_QlHd_DeleteActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(0, 102, 255));
        jButton9.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/baseline_cached_white_24dp.png"))); // NOI18N
        jButton9.setText("Làm mới");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(btn_QlHd_Delete, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_QlHd_Delete, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        txt_QlHd_address.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_QlHd_address.setForeground(new java.awt.Color(0, 102, 255));

        jLabel33.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(0, 102, 0));
        jLabel33.setText("Tích lũy:");

        txt_QlHd_tichLuy.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_QlHd_tichLuy.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel6)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel4)
                                                        .addComponent(jLabel12))
                                                    .addGap(18, 18, 18)
                                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(txtTenNv, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(txt_QlHd_IdHd, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                                    .addComponent(jLabel5)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(txtNgaytao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(txt_QlHd_tichLuy, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGap(29, 29, 29)
                                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel4Layout.createSequentialGroup()
                                                    .addGap(30, 30, 30)
                                                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(15, 15, 15)
                                                    .addComponent(txt_QlHd_maThe, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                                        .addComponent(jLabel8)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(txt_QlHd_address, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                                        .addComponent(jLabel7)
                                                        .addGap(15, 15, 15)
                                                        .addComponent(txt_QlHd_TenKh, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                                    .addGap(12, 12, 12)
                                                    .addComponent(jLabel9)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(txt_QlHd_Phone, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txt_QlHd_tongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txt_QlHd_giamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel28)
                                        .addGap(18, 18, 18)
                                        .addComponent(txt_QlHd_thanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_QlHd_IdHd, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNgaytao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTenNv, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_QlHd_maThe, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_QlHd_TenKh, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_QlHd_address, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_QlHd_tichLuy, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txt_QlHd_Phone, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_QlHd_tongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_QlHd_giamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_QlHd_thanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlQlHdLayout = new javax.swing.GroupLayout(pnlQlHd);
        pnlQlHd.setLayout(pnlQlHdLayout);
        pnlQlHdLayout.setHorizontalGroup(
            pnlQlHdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlQlHdLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlQlHdLayout.setVerticalGroup(
            pnlQlHdLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlQlHdLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnlCard.add(pnlQlHd, "card2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(pnlCard, javax.swing.GroupLayout.PREFERRED_SIZE, 561, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        new MainJFrame().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnAddHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddHdActionPerformed
        try {
            // TODO add your handling code here:
            CardLayout layout = (CardLayout) pnlCard.getLayout();
            layout.first(pnlCard);
            refreshAddHd();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnAddHdActionPerformed

    private void btnQlHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQlHdActionPerformed
        try {
            // TODO add your handling code here:
            CardLayout layout = (CardLayout) pnlCard.getLayout();
            layout.last(pnlCard);
            renderTableHoadon();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnQlHdActionPerformed

    private void tblListHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblListHoaDonMouseClicked
        int row = tblListHoaDon.getSelectedRow();

        try {
            setRowDataHD(row);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_tblListHoaDonMouseClicked

    private void txtSearchHdKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchHdKeyReleased
        // TODO add your handling code here:
        String name = txtSearchHd.getText();
        dtfListHoaDon.setRowCount(0);
        if (cbbSearch.getSelectedIndex() == 0) {
            if (name.isEmpty()) {
                try {
                    setDataTableHoaDon(hdService.getListHoaDon());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else  try {
                setDataTableHoaDon(hdService.getListHdByNameKH(name));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else  try {
            setDataTableHoaDon(hdService.getListHdByNameNV(name));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        txtSearchHd.requestFocus();
    }//GEN-LAST:event_txtSearchHdKeyReleased

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void tblNongSanAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNongSanAddMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            int row = tblNongSanAdd.getSelectedRow();
            dialogAddSl.setVisible(true);
            dialogAddSl.setLocationRelativeTo(null);
            lbl_DialogNameNs.setText(tblNongSanAdd.getValueAt(row, 1) + ":");
            lbl_DialogDvtNS.setText("( " + tblNongSanAdd.getValueAt(row, 3) + " )");
            txt_DialogSlNs.setText("");
            txt_DialogSlNs.requestFocus();
            btn_DialogAddNs.setVisible(true);
            btn_Dialog_EditSl.setVisible(false);
        }
    }//GEN-LAST:event_tblNongSanAddMouseClicked

    private void btn_DialogCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DialogCancelActionPerformed
        // TODO add your handling code here:
        dialogAddSl.setVisible(false);
    }//GEN-LAST:event_btn_DialogCancelActionPerformed

    //Thêm số lượng NS mua
    private void btn_DialogAddNsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_DialogAddNsActionPerformed
        // TODO add your handling code here:
        int row = tblNongSanAdd.getSelectedRow();
        if (txt_DialogSlNs.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(dialogAddSl, "Số lượng không được để trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txt_DialogSlNs.requestFocus();
        } else if (!isNumeric(txt_DialogSlNs.getText()) || Float.valueOf(txt_DialogSlNs.getText()) <= 0) {
            JOptionPane.showMessageDialog(dialogAddSl, "Số lượng phải là 1 số lớn hơn 0", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txt_DialogSlNs.requestFocus();
        } else if (!hdService.checkSoluong(listNsFull, (int) tblNongSanAdd.getValueAt(row, 0), Float.valueOf(txt_DialogSlNs.getText()))) {
            JOptionPane.showMessageDialog(dialogAddSl, "Số lượng vượt quá số lượng trong kho",
                    "Thông báo!!", JOptionPane.ERROR_MESSAGE);
        } else {
            Boolean isDuplicate = false;
            float quan = Float.valueOf(txt_DialogSlNs.getText());

            for (int i = 0; i < listNsAdd.size(); i++) {
                if (Integer.valueOf(tblNongSanAdd.getValueAt(row, 0) + "") == listNsAdd.get(i).getIdNS()) {

                    listNsAdd.get(i).setSoluongKho(listNsAdd.get(i).getSoluongKho() + quan);
                    listNsFull = hdService.tempResetSoluong(listNsFull, listNsAdd.get(i).getIdNS(), quan);

                    setDataTblHdAdd(listNsAdd);
                    try {
                        setDataTableNongSanAdd(listNsFull);
                        txtSearchNs.setText("");
                        cbbTypeNs.setSelectedIndex(0);

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    isDuplicate = true;
                    dialogAddSl.setVisible(false);
                    break;
                }
            }

            if (!isDuplicate) {
                float price = 0;

                try {
                    price = (float) (long) currencyVN.parse(tblNongSanAdd.getValueAt(row, 4) + "");
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                NongSan ns = new NongSan();
                ns.setIdNS(Integer.valueOf(tblNongSanAdd.getValueAt(row, 0) + ""));
                ns.setTenNS(tblNongSanAdd.getValueAt(row, 1) + "");
                ns.setDonviTinh(tblNongSanAdd.getValueAt(row, 3) + "");
                ns.setSoluongKho(quan);
                ns.setDongiaBan(price);
                float totalPrice = quan * price;

                listNsAdd.add(ns);

                listNsFull = hdService.tempResetSoluong(listNsFull, ns.getIdNS(), quan);

                setDataTblHdAdd(listNsAdd);

                try {
                    setDataTableNongSanAdd(listNsFull);
                    txtSearchNs.setText("");
                    cbbTypeNs.setSelectedIndex(0);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                dialogAddSl.setVisible(false);
            }

        }
    }//GEN-LAST:event_btn_DialogAddNsActionPerformed
    private void setDataTblHdAdd(List<NongSan> list) {

        dtfHoaDonAdd.setRowCount(0);
        for (int i = 0; i < list.size(); i++) {
            dtfHoaDonAdd.addRow(new Object[]{i + 1, list.get(i).getIdNS(), list.get(i).getTenNS(),
                list.get(i).getDonviTinh(), list.get(i).getSoluongKho(), currencyVN.format(list.get(i).getDongiaBan()),
                currencyVN.format(list.get(i).getSoluongKho() * list.get(i).getDongiaBan())});
        }
        refeshTotalPrice();
    }

    private void cbbTypeNsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbTypeNsActionPerformed
        // TODO add your handling code here:
        dtfNongSanAdd.setRowCount(0);
        if (cbbTypeNs.getSelectedIndex() == 0) {
            try {
                setDataTableNongSanAdd(listNsFull);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            String type = cbbTypeNs.getSelectedItem().toString();
            try {
                setDataTableNongSanAdd(nsService.searchNongSanByType(listNsFull, type));
            } catch (SQLException ex) {
                ex.printStackTrace();

            }
        }
    }//GEN-LAST:event_cbbTypeNsActionPerformed

    private void txtSearchNsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchNsKeyReleased
        // TODO add your handling code here:
        String name = txtSearchNs.getText();
        dtfNongSanAdd.setRowCount(0);
        if (cbbTypeNs.getSelectedIndex() == 0) {
            try {
                setDataTableNongSanAdd(nsService.searchNSbyName(listNsFull, name));
            } catch (SQLException ex) {
                ex.printStackTrace();

            }

        } else {
            try {

                String type = cbbTypeNs.getSelectedItem().toString();
                setDataTableNongSanAdd(nsService.searchNongSanByNameAndType(listNsFull, name, type));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
    }//GEN-LAST:event_txtSearchNsKeyReleased

    private void btnAddKhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddKhActionPerformed
        // TODO add your handling code here:
        int row = tblKhachHang.getSelectedRow();
        txt_AddHd_MaThe.setText(tblKhachHang.getValueAt(row, 1) + "");
        txt_AddHd_NameKh.setText(tblKhachHang.getValueAt(row, 2) + "");
        txt_AddHd_Address.setText(tblKhachHang.getValueAt(row, 3) + "");
        txt_AddHd_Phone.setText(tblKhachHang.getValueAt(row, 4) + "");
        txt_AddHd_TichLuy.setText(tblKhachHang.getValueAt(row, 5) + "");

        dialogChooseKH.setVisible(false);
        refeshTotalPrice();

    }//GEN-LAST:event_btnAddKhActionPerformed

    private void setDataTblKh(List<KhachHang> listKH) {

        dtfKhachHang.setRowCount(0);

        for (int i = 0; i < listKH.size(); i++) {
            dtfKhachHang.addRow(new Object[]{i + 1, listKH.get(i).getMaThe(), listKH.get(i).getTenKH(),
                listKH.get(i).getDiachiKH(), listKH.get(i).getSdtKH(), currencyVN.format(listKH.get(i).getTichLuy())});

        }
    }
    private void btnCancelKhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelKhActionPerformed
        // TODO add your handling code here:
        ckbThanhVien.setSelected(false);
        dialogChooseKH.setVisible(false);
    }//GEN-LAST:event_btnCancelKhActionPerformed

    private void ckbThanhVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ckbThanhVienActionPerformed
        // TODO add your handling code here:
        khService = new KhachHangService();
        if (ckbThanhVien.isSelected()) {
            dialogChooseKH.setVisible(true);
            dialogChooseKH.setLocationRelativeTo(null);

            dtfKhachHang = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            tblKhachHang.setModel(dtfKhachHang);

            dtfKhachHang.addColumn("STT");
            dtfKhachHang.addColumn("Mã thẻ");
            dtfKhachHang.addColumn("Tên Khách hàng");
            dtfKhachHang.addColumn("Địa chỉ");
            dtfKhachHang.addColumn("Số điện thoại");
            dtfKhachHang.addColumn("Tích lũy");
            //setup header Table
            DefaultTableCellRenderer tbl_Header = new DefaultTableCellRenderer();
            tbl_Header.setHorizontalAlignment(JLabel.CENTER);
            tbl_Header.setFont(new Font("Arial", Font.BOLD, (int) 13));
            tbl_Header.setPreferredSize(new Dimension(100, 35));
            tbl_Header.setBackground(new Color(0, 161, 12));
            tbl_Header.setForeground(Color.white);

            tblKhachHang.getTableHeader().setDefaultRenderer(tbl_Header);

            //setup row Table
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            tblKhachHang.setDefaultRenderer(Object.class, centerRenderer);
            tblKhachHang.setAutoResizeMode(tblKhachHang.AUTO_RESIZE_NEXT_COLUMN);

            TableColumnModel colModel = tblKhachHang.getColumnModel();
            colModel.getColumn(0).setPreferredWidth(30);
            colModel.getColumn(3).setPreferredWidth(130);

            try {
                setDataTblKh(khService.getListKhachHang());

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            tblKhachHang.requestFocus();
            tblKhachHang.changeSelection(0, 0, false, false);
            ckbThanhVien.setSelected(true);

        } else {
            txt_AddHd_MaThe.setText("");
            txt_AddHd_NameKh.setText("");
            txt_AddHd_Address.setText("");
            txt_AddHd_TichLuy.setText("");
            txt_AddHd_Phone.setText("");
            refeshTotalPrice();
        }


    }//GEN-LAST:event_ckbThanhVienActionPerformed

    private void txtSearchKhKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSearchKhKeyReleased
        // TODO add your handling code here:
        String key = txtSearchKh.getText();

        if (cbb_DialogKh_search.getSelectedIndex() == 0) {
            try {
                setDataTblKh(khService.searchKhachHangByName(key));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                setDataTblKh(khService.searchKhachHangByMaThe(key));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

    }//GEN-LAST:event_txtSearchKhKeyReleased

    private void tblHoaDonAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonAddMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            menuEditNs.show(tblHoaDonAdd, evt.getX(), evt.getY());
        }

    }//GEN-LAST:event_tblHoaDonAddMouseClicked

    //Sửa số lượng nông sản mua
    private void btn_Dialog_EditSlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Dialog_EditSlActionPerformed
        // TODO add your handling code here:
        int row = tblHoaDonAdd.getSelectedRow();
        if (txt_DialogSlNs.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(dialogAddSl, "Số lượng không được để trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txt_DialogSlNs.requestFocus();
        } else if (!isNumeric(txt_DialogSlNs.getText()) || Float.valueOf(txt_DialogSlNs.getText()) == 0) {
            JOptionPane.showMessageDialog(dialogAddSl, "Số lượng phải là 1 số lớn hơn 0", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txt_DialogSlNs.requestFocus();

        } else try {
            if (!hdService.checkSoluong(nsService.getListNongSan(), (int) tblHoaDonAdd.getValueAt(row, 1), Float.valueOf(txt_DialogSlNs.getText()))) {
                JOptionPane.showMessageDialog(dialogAddSl, "Số lượng vượt quá số lượng trong kho",
                        "Thông báo!!", JOptionPane.ERROR_MESSAGE);

            } else {
                for (int i = 0; i < listNsAdd.size(); i++) {
                    if ((int) tblHoaDonAdd.getValueAt(row, 1) == listNsAdd.get(i).getIdNS()) {

                        listNsFull = hdService.tempResetSoluong(listNsFull, listNsAdd.get(i).getIdNS(),
                                Float.valueOf(txt_DialogSlNs.getText()) - listNsAdd.get(i).getSoluongKho());
                        listNsAdd.get(i).setSoluongKho(Float.valueOf(txt_DialogSlNs.getText()));
                        setDataTblHdAdd(listNsAdd);
                        try {
                            setDataTableNongSanAdd(listNsFull);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                dialogAddSl.setVisible(false);

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btn_Dialog_EditSlActionPerformed

    private void menuItemEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemEditActionPerformed
        // TODO add your handling code here:

        dialogAddSl.setVisible(true);
        btn_DialogAddNs.setVisible(false);
        btn_Dialog_EditSl.setVisible(true);
        int row = tblHoaDonAdd.getSelectedRow();
        txt_DialogSlNs.setText(tblHoaDonAdd.getValueAt(row, 4) + "");
        lbl_DialogNameNs.setText("( " + tblHoaDonAdd.getValueAt(row, 2) + " )");
        lbl_DialogDvtNS.setText(tblHoaDonAdd.getValueAt(row, 3) + "");
        txt_DialogSlNs.requestFocus();


    }//GEN-LAST:event_menuItemEditActionPerformed

    private void menuItemXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemXoaActionPerformed
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(rootPane,
                "Bạn chắc chắn xóa sản phẩm này khỏi hóa đơn", "Thông báo", JOptionPane.OK_CANCEL_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int row = tblHoaDonAdd.getSelectedRow();
            for (int i = 0; i < listNsAdd.size(); i++) {
                if ((int) tblHoaDonAdd.getValueAt(row, 1) == listNsAdd.get(i).getIdNS()) {

                    listNsFull = hdService.tempResetSoluongCancel(listNsFull, listNsAdd.get(i));
                    listNsAdd.remove(i);
                    try {
                        setDataTableNongSanAdd(listNsFull);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                    setDataTblHdAdd(listNsAdd);

                    break;
                }

            }

        }

    }//GEN-LAST:event_menuItemXoaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            // TODO add your handling code here:
            addHoaDonFinally();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        dialog_AddNewKh.setVisible(false);
        ckbThanhVien.setSelected(false);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            // TODO add your handling code here:
            dialogChooseKH.setVisible(false);
            dialog_AddNewKh.setVisible(true);
            dialog_AddNewKh.setLocationRelativeTo(null);
            txt_AddKh_maThe.setText(khService.setNewMathe());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        if (isCorrect()) {
            KhachHang kh = new KhachHang();
            kh.setMaThe(txt_AddKh_maThe.getText());
            kh.setCCCD(txt_AddKh_cccd.getText());
            kh.setTenKH(txt_AddKh_name.getText());
            kh.setDiachiKH(txt_AddKh_Address.getText());
            kh.setSdtKH(txt_AddKh_phone.getText());
            Date date = new Date();
            kh.setNgaytao(date);
            kh.setTichLuy(0);

            try {
                if (khService.addKhachHang(kh)) {
                    JOptionPane.showMessageDialog(this,
                            "Thêm khách hàng thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

                    txt_AddHd_MaThe.setText(txt_AddKh_maThe.getText());
                    txt_AddHd_NameKh.setText(txt_AddKh_name.getText());
                    txt_AddHd_Address.setText(txt_AddKh_Address.getText());
                    txt_AddHd_Phone.setText(txt_AddKh_phone.getText());
                    txt_AddHd_TichLuy.setText(currencyVN.format(0));

                    dialog_AddNewKh.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Thêm khách hàng không thành công", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }

    }//GEN-LAST:event_jButton7ActionPerformed

    public void refreshAddHd() throws SQLException {
        listNsFull = nsService.getListNongSan();
        setDataTableNongSanAdd(listNsFull);
        listNsAdd.removeAll(listNsAdd);
        dtfHoaDonAdd.setRowCount(0);
        refreshSoHd();
        refeshTotalPrice();
        ckbThanhVien.setSelected(false);
        txt_AddHd_Address.setText("");
        txt_AddHd_MaThe.setText("");
        txt_AddHd_NameKh.setText("");
        txt_AddHd_TichLuy.setText("");
        txt_AddHd_Phone.setText("");

    }
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            refreshAddHd();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btn_QlHd_DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_QlHd_DeleteActionPerformed
        // TODO add your handling code here:
        int comfirm = JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc chắc muốn xóa hóa đơn này",
                "Thông báo!", JOptionPane.OK_CANCEL_OPTION);
        if (comfirm == JOptionPane.YES_OPTION) {
            try {
                HoaDon hd = new HoaDon();
                float tongtienBan = (float) (long) currencyVN.parse(txt_QlHd_tongTien.getText());
                hd.setIdHD(txt_QlHd_IdHd.getText());
                hd.setMaThe(txt_QlHd_maThe.getText());
                hd.setTongTienBan(tongtienBan);

                List<HoaDonDeTail> ListHdDetail = hdService.getListHdDetail(txt_QlHd_IdHd.getText());

                if (hdService.deleteHoaDon(hd, ListHdDetail)) {

                    JOptionPane.showMessageDialog(rootPane, "Xóa hóa đơn thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    refreshQlHd();
                } else {
                    JOptionPane.showMessageDialog(rootPane, "Xóa không thành công", "Thông báo", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                Logger.getLogger(HoaDonJFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(HoaDonJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btn_QlHd_DeleteActionPerformed
    public void refreshQlHd() throws SQLException {

        setDataTableHoaDon(hdService.getListHoaDon());
        setRowDataHD(0);

    }

    private void addHoaDonFinally() throws SQLException, ParseException {
        if (listNsAdd.size() == 0) {
            JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn sản phẩm", "Thông báo", JOptionPane.ERROR_MESSAGE);
        } else {
            HoaDon hd = new HoaDon();
            Timestamp Ngayban = new Timestamp(sdf.parse(txt_Addhd_ngayTao.getText()).getTime());
            float thanhTien = (float) (long) currencyVN.parse(txt_AddHd_thanhTien.getText());
            float giamGia = (float) (long) currencyVN.parse(txt_AddHd_giamGia.getText().substring(2));
            float tongtienBan = (float) (long) currencyVN.parse(txt_AddHd_tongTien.getText());
            hd.setIdHD(txt_addHD_IdHd.getText());
            hd.setIdNV(LoginForm.nvLogIn.getIdNV());
            hd.setNgayBan(Ngayban);
            hd.setMaThe(txt_AddHd_MaThe.getText());
            hd.setTongTienBan(tongtienBan);
            hd.setGiamGia(giamGia);
            hd.setThanhTien(thanhTien);

            if (hdService.addHoaDonFinally(hd, listNsAdd)) {
                JOptionPane.showMessageDialog(rootPane, "Thêm hóa đơn thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                refreshAddHd();

            } else {
                JOptionPane.showMessageDialog(rootPane, "Thêm hóa đơn không thành công", "Lỗi", JOptionPane.ERROR_MESSAGE);

            }

        }

    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    //Check đầu vào input
    public boolean isCorrect() {
        Boolean isOK = true;
        while (true) {
            if (txt_AddKh_name.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog_AddNewKh, "Tên khách hàng không được để trống",
                        "Lỗi nhập thông tin", JOptionPane.ERROR_MESSAGE);
                isOK = false;
                txt_AddKh_name.requestFocus();
                break;
            }
            if (txt_AddKh_cccd.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog_AddNewKh, "Số CCCD không được để trống",
                        "Lỗi nhập thông tin", JOptionPane.ERROR_MESSAGE);
                isOK = false;
                txt_AddKh_cccd.requestFocus();
                break;
            }
            if (txt_AddKh_Address.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog_AddNewKh, "Địa chỉ khách hàng không được để trống",
                        "Lỗi nhập thông tin", JOptionPane.ERROR_MESSAGE);

                isOK = false;
                txt_AddKh_Address.requestFocus();
                break;
            }

            if (txt_AddKh_phone.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog_AddNewKh, "Số điện thoại không được để trống",
                        "Lỗi nhập thông tin", JOptionPane.ERROR_MESSAGE);
                isOK = false;
                txt_AddKh_phone.requestFocus();
                break;
            }

            break;
        }
        return isOK;
    }

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddHd;
    private javax.swing.JButton btnAddKh;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCancelKh;
    private javax.swing.JButton btnQlHd;
    private javax.swing.JButton btn_DialogAddNs;
    private javax.swing.JButton btn_DialogCancel;
    private javax.swing.JButton btn_Dialog_EditSl;
    private javax.swing.JButton btn_QlHd_Delete;
    private javax.swing.JComboBox<String> cbbSearch;
    private javax.swing.JComboBox<String> cbbTypeNs;
    private javax.swing.JComboBox<String> cbb_DialogKh_search;
    private javax.swing.JCheckBox ckbThanhVien;
    private javax.swing.JDialog dialogAddSl;
    private javax.swing.JDialog dialogChooseKH;
    private javax.swing.JDialog dialog_AddNewKh;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblClock;
    private javax.swing.JLabel lbl_DialogDvtNS;
    private javax.swing.JLabel lbl_DialogNameNs;
    private javax.swing.JPopupMenu menuEditNs;
    private javax.swing.JMenuItem menuItemEdit;
    private javax.swing.JMenuItem menuItemXoa;
    private javax.swing.JPanel pnlAddHd;
    private javax.swing.JPanel pnlCard;
    private javax.swing.JPanel pnlQlHd;
    private javax.swing.JTable tblHoaDonAdd;
    private javax.swing.JTable tblKhachHang;
    private javax.swing.JTable tblListHoaDon;
    private javax.swing.JTable tblListNongSan;
    private javax.swing.JTable tblNongSanAdd;
    private javax.swing.JLabel txtBophan;
    private javax.swing.JLabel txtNameLogin;
    private javax.swing.JLabel txtNgaytao;
    private javax.swing.JTextField txtSearchHd;
    private javax.swing.JTextField txtSearchKh;
    private javax.swing.JTextField txtSearchNs;
    private javax.swing.JLabel txtTenNv;
    private javax.swing.JLabel txt_AddHd_Address;
    private javax.swing.JLabel txt_AddHd_MaThe;
    private javax.swing.JLabel txt_AddHd_NameKh;
    private javax.swing.JLabel txt_AddHd_Nvban;
    private javax.swing.JLabel txt_AddHd_Phone;
    private javax.swing.JLabel txt_AddHd_TichLuy;
    private javax.swing.JLabel txt_AddHd_giamGia;
    private javax.swing.JLabel txt_AddHd_thanhTien;
    private javax.swing.JLabel txt_AddHd_tongTien;
    private javax.swing.JTextField txt_AddKh_Address;
    private javax.swing.JTextField txt_AddKh_cccd;
    private javax.swing.JLabel txt_AddKh_maThe;
    private javax.swing.JTextField txt_AddKh_name;
    private javax.swing.JTextField txt_AddKh_phone;
    private javax.swing.JLabel txt_Addhd_ngayTao;
    private javax.swing.JTextField txt_DialogSlNs;
    private javax.swing.JLabel txt_QlHd_IdHd;
    private javax.swing.JLabel txt_QlHd_Phone;
    private javax.swing.JLabel txt_QlHd_TenKh;
    private javax.swing.JLabel txt_QlHd_address;
    private javax.swing.JLabel txt_QlHd_giamGia;
    private javax.swing.JLabel txt_QlHd_maThe;
    private javax.swing.JLabel txt_QlHd_thanhTien;
    private javax.swing.JLabel txt_QlHd_tichLuy;
    private javax.swing.JLabel txt_QlHd_tongTien;
    private javax.swing.JLabel txt_addHD_IdHd;
    // End of variables declaration//GEN-END:variables

}
