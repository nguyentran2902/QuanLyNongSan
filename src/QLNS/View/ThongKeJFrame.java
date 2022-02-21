/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package QLNS.View;

import QLNS.Model.HoaDon;
import QLNS.Controller.HdDetailService;
import QLNS.Controller.HoaDonService;
import QLNS.Controller.KhachHangService;
import QLNS.Controller.NhanVienService;
import QLNS.Controller.NongSanService;
import QLNS.Controller.ThongKeService;
import QLNS.Model.DoanhThu;
import QLNS.Model.HoaDonDeTail;
import QLNS.Model.KhachHang;
import QLNS.Model.NhanVien;
import QLNS.Model.NongSan;
import QLNS.Threading.ClockThread;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class ThongKeJFrame extends javax.swing.JFrame {

    private int isSelectItem = 1;
    private DefaultTableModel dtfListNsThongKe;
    private DefaultTableModel dtfListHdThongKe;
    private HoaDonService hdService;
    private HdDetailService hdDetailService;
    private KhachHangService khService;
    private NhanVienService nvService;
    private NongSanService nsService;
    private ThongKeService tkService;

    private Locale localeVN = new Locale("vi", "VN");
    private NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Creates new form ThongKeJFrame
     */
    public ThongKeJFrame() throws SQLException, ParseException {
        hdService = new HoaDonService();
        nsService = new NongSanService();
        hdDetailService = new HdDetailService();
          tkService = new ThongKeService();
        initComponents();
        setLocationRelativeTo(null);
        initClock();
        txtLastDate.setDate(new Date());
        renderByCbb(null, new Date());
        tkService.RenderYearToCbb(cbb_BieuDo_chooseYear);

    }

    public void renderTableHoadon(List<HoaDon> listHD) throws SQLException {
        hdService = new HoaDonService();
        khService = new KhachHangService();
        nvService = new NhanVienService();
        dtfListHdThongKe = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblThongKe.setModel(dtfListHdThongKe);

        dtfListHdThongKe.addColumn("STT");
        dtfListHdThongKe.addColumn("ID Hóa đơn");
        dtfListHdThongKe.addColumn("Ngày bán");
        dtfListHdThongKe.addColumn("Nhân viên bán");
        dtfListHdThongKe.addColumn("Tên Thành viên");

        dtfListHdThongKe.addColumn("Thành tiền");

        setDataTableHoaDon(listHD);

      
        //setup header Table
        DefaultTableCellRenderer tbl_Header = new DefaultTableCellRenderer();
        tbl_Header.setHorizontalAlignment(JLabel.CENTER);
        tbl_Header.setFont(new Font("Arial", Font.BOLD, (int) 13));
        tbl_Header.setPreferredSize(new Dimension(100, 35));
        tbl_Header.setBackground(new Color(0,161,12));
        tbl_Header.setForeground(Color.white);

        tblThongKe.getTableHeader().setDefaultRenderer(tbl_Header);

        //setup row Table
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblThongKe.setDefaultRenderer(Object.class, centerRenderer);
        tblThongKe.setAutoResizeMode(tblThongKe.AUTO_RESIZE_NEXT_COLUMN);
        TableColumnModel colModel = tblThongKe.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(20);
        colModel.getColumn(1).setPreferredWidth(50);

        colModel.getColumn(4).setPreferredWidth(100);
        colModel.getColumn(5).setPreferredWidth(50);

    }

    private void setDataTableHoaDon(List<HoaDon> listHD) throws SQLException {
        dtfListHdThongKe.setRowCount(0);
        for (int i = 0; i < listHD.size(); i++) {
            KhachHang kh = khService.getKhbyMaThe(listHD.get(i).getMaThe());
            NhanVien nv = nvService.getNvbyId(listHD.get(i).getIdNV() + "");
            String ngayban = sdf.format(listHD.get(i).getNgayBan());

            dtfListHdThongKe.addRow(new Object[]{i + 1, listHD.get(i).getIdHD(), ngayban,
                nv.getTenNV(), kh.getTenKH(), currencyVN.format(listHD.get(i).getThanhTien())});
        }

    }

    public void renderTableNongSan(List<NongSan> ListNs) throws SQLException {
        dtfListNsThongKe = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblThongKe.setModel(dtfListNsThongKe);

        dtfListNsThongKe.addColumn("STT");
        dtfListNsThongKe.addColumn("Id NS");
        dtfListNsThongKe.addColumn("Tên Nông Sản");
        dtfListNsThongKe.addColumn("Đơn vị tính");
        dtfListNsThongKe.addColumn("Số lượng đã bán");
        dtfListNsThongKe.addColumn("Doanh thu");

       
        //setup header Table
        DefaultTableCellRenderer tbl_Header = new DefaultTableCellRenderer();
        tbl_Header.setHorizontalAlignment(JLabel.CENTER);
        tbl_Header.setFont(new Font("Arial", Font.BOLD, (int) 13));
        tbl_Header.setPreferredSize(new Dimension(100, 35));
        tbl_Header.setBackground(new Color(0,161,12));
        tbl_Header.setForeground(Color.white);

        tblThongKe.getTableHeader().setDefaultRenderer(tbl_Header);

        //setup row Table
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblThongKe.setDefaultRenderer(Object.class, centerRenderer);
        tblThongKe.setAutoResizeMode(tblThongKe.AUTO_RESIZE_NEXT_COLUMN);
        TableColumnModel colModel = tblThongKe.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(10);
        colModel.getColumn(1).setPreferredWidth(10);
        colModel.getColumn(2).setPreferredWidth(100);

        setDataTableNs(ListNs);

    }

    private void setDataTableNs(List<NongSan> ListNs) throws SQLException {
        dtfListHdThongKe.setRowCount(0);

        for (int i = 0; i < ListNs.size(); i++) {
            Float thanhTien = ListNs.get(i).getSoluongKho() * ListNs.get(i).getDongiaBan();
            dtfListNsThongKe.addRow(new Object[]{i + 1, ListNs.get(i).getIdNS(), ListNs.get(i).getTenNS(), ListNs.get(i).getDonviTinh(), ListNs.get(i).getSoluongKho(),
                currencyVN.format(thanhTien)});
        }

    }

    private void renderByCbb(Date date1, Date date2) throws SQLException, ParseException {
        if (cbbThongKe.getSelectedIndex() == 0) {
            renderTableHoadon(hdService.getListHdByTime(date1, date2));
            renderDoanhThuByHd();
        } else {
            renderTableNongSan(nsService.getListNSByTime(date1, date2));
            renderDoanhThuByNs();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        dialogHdDetail = new javax.swing.JDialog();
        jPanel5 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblShowHdDetail = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        txt_HdDt_tongTien = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txt_HdDt_giamGia = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txt_HdDt_thanhTien = new javax.swing.JLabel();
        txt_HdDt_idHd = new javax.swing.JLabel();
        txt_HdDt_ngayTao = new javax.swing.JLabel();
        txt_HdDt_NvBan = new javax.swing.JLabel();
        txt_HdDt_tenKh = new javax.swing.JLabel();
        txt_HdDt_diaChi = new javax.swing.JLabel();
        txt_HdDt_sdt = new javax.swing.JLabel();
        btnClose = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblClock = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnBieuDo = new javax.swing.JButton();
        btnThongKe = new javax.swing.JButton();
        pnlCard = new javax.swing.JPanel();
        pnlThongKe = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lblTitleSum = new javax.swing.JLabel();
        lblCountHD = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lbltotalDoanhThu = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        cbbThongKe = new javax.swing.JComboBox<>();
        txtFirstDate = new com.toedter.calendar.JDateChooser();
        txtLastDate = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        btnFresh = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblThongKe = new javax.swing.JTable();
        pnlBieuDo = new javax.swing.JPanel();
        pnlItem1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        pnlItem2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        cbb_BieuDo_chooseYear = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        txt_bieuDo_tongHd = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txt_bieuDo_doanhThu = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel9 = new javax.swing.JPanel();

        dialogHdDetail.setTitle("THÔNG TIN CHI TIẾT");
        dialogHdDetail.setMinimumSize(new java.awt.Dimension(680, 530));

        jPanel5.setMinimumSize(new java.awt.Dimension(755, 551));

        jLabel4.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("CHI TIẾT HÓA ĐƠN");

        jPanel6.setBackground(new java.awt.Color(255, 252, 248));

        jLabel5.setBackground(new java.awt.Color(0, 102, 0));
        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 102, 0));
        jLabel5.setText("Id Hóa đơn:");

        jLabel7.setBackground(new java.awt.Color(0, 102, 0));
        jLabel7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 102, 0));
        jLabel7.setText("Ngày tạo:");

        jLabel8.setBackground(new java.awt.Color(0, 102, 0));
        jLabel8.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 102, 0));
        jLabel8.setText("Nhân viên bán:");

        jLabel9.setBackground(new java.awt.Color(0, 102, 0));
        jLabel9.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 102, 0));
        jLabel9.setText("Họ tên KH:");

        jLabel10.setBackground(new java.awt.Color(0, 102, 0));
        jLabel10.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 102, 0));
        jLabel10.setText("Địa chỉ:");

        jLabel11.setBackground(new java.awt.Color(0, 102, 0));
        jLabel11.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 102, 0));
        jLabel11.setText("Số điện thoại:");

        jScrollPane2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 153, 51)));

        tblShowHdDetail.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tblShowHdDetail.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        tblShowHdDetail.setModel(new javax.swing.table.DefaultTableModel(
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
        tblShowHdDetail.setRowHeight(26);
        jScrollPane2.setViewportView(tblShowHdDetail);

        jLabel12.setBackground(new java.awt.Color(0, 102, 0));
        jLabel12.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 102, 0));
        jLabel12.setText("Tổng tiền:");

        txt_HdDt_tongTien.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        txt_HdDt_tongTien.setForeground(new java.awt.Color(255, 0, 0));
        txt_HdDt_tongTien.setText("jLabel13");

        jLabel14.setBackground(new java.awt.Color(0, 102, 0));
        jLabel14.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 102, 0));
        jLabel14.setText("Giảm giá:");

        txt_HdDt_giamGia.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        txt_HdDt_giamGia.setForeground(new java.awt.Color(255, 0, 0));
        txt_HdDt_giamGia.setText("jLabel15");

        jLabel16.setBackground(new java.awt.Color(0, 102, 0));
        jLabel16.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 102, 0));
        jLabel16.setText("Thành tiền:");

        txt_HdDt_thanhTien.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        txt_HdDt_thanhTien.setForeground(new java.awt.Color(255, 0, 0));
        txt_HdDt_thanhTien.setText("jLabel17");

        txt_HdDt_idHd.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_HdDt_idHd.setForeground(new java.awt.Color(0, 102, 255));

        txt_HdDt_ngayTao.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_HdDt_ngayTao.setForeground(new java.awt.Color(0, 102, 255));

        txt_HdDt_NvBan.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_HdDt_NvBan.setForeground(new java.awt.Color(0, 102, 255));

        txt_HdDt_tenKh.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_HdDt_tenKh.setForeground(new java.awt.Color(0, 102, 255));

        txt_HdDt_diaChi.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_HdDt_diaChi.setForeground(new java.awt.Color(0, 102, 255));

        txt_HdDt_sdt.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txt_HdDt_sdt.setForeground(new java.awt.Color(0, 102, 255));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(18, 18, 18)
                        .addComponent(txt_HdDt_tongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_HdDt_giamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_HdDt_thanhTien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txt_HdDt_NvBan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txt_HdDt_ngayTao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txt_HdDt_idHd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_HdDt_tenKh, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txt_HdDt_sdt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                                .addComponent(txt_HdDt_diaChi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGap(15, 15, 15))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_HdDt_idHd, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_HdDt_tenKh, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_HdDt_ngayTao, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_HdDt_diaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_HdDt_NvBan, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_HdDt_sdt, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_HdDt_tongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_HdDt_giamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_HdDt_thanhTien, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        btnClose.setBackground(new java.awt.Color(0, 102, 204));
        btnClose.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 255, 255));
        btnClose.setText("OK");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(282, 282, 282)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dialogHdDetailLayout = new javax.swing.GroupLayout(dialogHdDetail.getContentPane());
        dialogHdDetail.getContentPane().setLayout(dialogHdDetailLayout);
        dialogHdDetailLayout.setHorizontalGroup(
            dialogHdDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        dialogHdDetailLayout.setVerticalGroup(
            dialogHdDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogHdDetailLayout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("THỐNG KÊ - BÁO CÁO");

        jPanel1.setBackground(new java.awt.Color(0, 161, 12));

        jButton1.setBackground(new java.awt.Color(255, 252, 248));
        jButton1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/baseline_reply_black_18dp.png"))); // NOI18N
        jButton1.setText("Trở về");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("     THỐNG KÊ");
        jLabel1.setName(""); // NOI18N

        lblClock.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblClock.setForeground(new java.awt.Color(255, 255, 255));
        lblClock.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jButton1)
                .addGap(13, 13, 13)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 831, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblClock, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(lblClock, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.setBackground(new java.awt.Color(204, 239, 251));

        btnBieuDo.setBackground(new java.awt.Color(255, 252, 248));
        btnBieuDo.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnBieuDo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/bieudo.png"))); // NOI18N
        btnBieuDo.setText("Biểu đồ");
        btnBieuDo.setFocusable(false);
        btnBieuDo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnBieuDo.setMaximumSize(new java.awt.Dimension(100, 89));
        btnBieuDo.setMinimumSize(new java.awt.Dimension(100, 89));
        btnBieuDo.setName(""); // NOI18N
        btnBieuDo.setPreferredSize(new java.awt.Dimension(100, 89));
        btnBieuDo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBieuDo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBieuDoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBieuDoMouseExited(evt);
            }
        });
        btnBieuDo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBieuDoActionPerformed(evt);
            }
        });

        btnThongKe.setBackground(new java.awt.Color(235, 212, 23));
        btnThongKe.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnThongKe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/thongkeHD.png"))); // NOI18N
        btnThongKe.setText("Thống kê");
        btnThongKe.setFocusable(false);
        btnThongKe.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnThongKe.setMaximumSize(new java.awt.Dimension(120, 92));
        btnThongKe.setMinimumSize(new java.awt.Dimension(100, 92));
        btnThongKe.setName(""); // NOI18N
        btnThongKe.setPreferredSize(new java.awt.Dimension(100, 89));
        btnThongKe.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnThongKe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnThongKeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnThongKeMouseExited(evt);
            }
        });
        btnThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThongKeActionPerformed(evt);
            }
        });

        pnlCard.setBackground(new java.awt.Color(204, 235, 251));
        pnlCard.setLayout(new java.awt.CardLayout());

        pnlThongKe.setBackground(new java.awt.Color(204, 235, 251));

        jPanel3.setBackground(new java.awt.Color(170, 209, 247));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 153, 0)));

        lblTitleSum.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblTitleSum.setForeground(new java.awt.Color(0, 102, 0));
        lblTitleSum.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitleSum.setText("Tổng hóa đơn:");

        lblCountHD.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblCountHD.setForeground(new java.awt.Color(255, 0, 0));
        lblCountHD.setText("43");

        jLabel6.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 102, 0));
        jLabel6.setText("Doanh thu:");

        lbltotalDoanhThu.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lbltotalDoanhThu.setForeground(new java.awt.Color(255, 0, 0));
        lbltotalDoanhThu.setText("12312323");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(lblTitleSum, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(lblCountHD, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(204, 204, 204)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbltotalDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitleSum, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCountHD, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbltotalDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(170, 209, 247));
        jPanel4.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 153, 0)));

        cbbThongKe.setBackground(new java.awt.Color(255, 252, 248));
        cbbThongKe.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        cbbThongKe.setForeground(new java.awt.Color(0, 102, 0));
        cbbThongKe.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hóa đơn", "Sản phẩm" }));
        cbbThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbThongKeActionPerformed(evt);
            }
        });

        txtFirstDate.setBackground(new java.awt.Color(255, 252, 248));
        txtFirstDate.setDateFormatString("dd/MM/yyyy");
        txtFirstDate.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        txtFirstDate.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        txtLastDate.setBackground(new java.awt.Color(255, 252, 248));
        txtLastDate.setDateFormatString("dd/MM/yyyy");
        txtLastDate.setDebugGraphicsOptions(javax.swing.DebugGraphics.BUFFERED_OPTION);
        txtLastDate.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        txtLastDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtLastDateMouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 0));
        jLabel2.setText("Từ ngày:");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 0));
        jLabel3.setText("đến:");

        btnSearch.setBackground(new java.awt.Color(255, 204, 0));
        btnSearch.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/search-2.png"))); // NOI18N
        btnSearch.setText("Thống kê");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnFresh.setBackground(new java.awt.Color(0, 132, 249));
        btnFresh.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnFresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/fresh.png"))); // NOI18N
        btnFresh.setText("Làm mới");
        btnFresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cbbThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtFirstDate, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtLastDate, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnFresh, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnFresh, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cbbThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtFirstDate, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                                .addComponent(txtLastDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 102));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 153, 0)));
        jScrollPane1.setForeground(new java.awt.Color(255, 255, 0));

        tblThongKe.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        tblThongKe.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tblThongKe.setModel(new javax.swing.table.DefaultTableModel(
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
        tblThongKe.setRowHeight(26);
        tblThongKe.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblThongKe.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblThongKe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblThongKeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblThongKe);

        javax.swing.GroupLayout pnlThongKeLayout = new javax.swing.GroupLayout(pnlThongKe);
        pnlThongKe.setLayout(pnlThongKeLayout);
        pnlThongKeLayout.setHorizontalGroup(
            pnlThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThongKeLayout.createSequentialGroup()
                .addGroup(pnlThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );
        pnlThongKeLayout.setVerticalGroup(
            pnlThongKeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThongKeLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8))
        );

        pnlCard.add(pnlThongKe, "card3");

        pnlBieuDo.setBackground(new java.awt.Color(204, 235, 251));

        pnlItem1.setBackground(new java.awt.Color(255, 255, 255));
        pnlItem1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(255, 153, 0)));

        javax.swing.GroupLayout pnlItem1Layout = new javax.swing.GroupLayout(pnlItem1);
        pnlItem1.setLayout(pnlItem1Layout);
        pnlItem1Layout.setHorizontalGroup(
            pnlItem1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlItem1Layout.setVerticalGroup(
            pnlItem1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 268, Short.MAX_VALUE)
        );

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 0, 1, new java.awt.Color(255, 153, 0)));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 796, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        pnlItem2.setBackground(new java.awt.Color(255, 255, 255));
        pnlItem2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 1, 1, new java.awt.Color(255, 153, 0)));

        javax.swing.GroupLayout pnlItem2Layout = new javax.swing.GroupLayout(pnlItem2);
        pnlItem2.setLayout(pnlItem2Layout);
        pnlItem2Layout.setHorizontalGroup(
            pnlItem2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlItem2Layout.setVerticalGroup(
            pnlItem2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 308, Short.MAX_VALUE)
        );

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 153, 0)));

        cbb_BieuDo_chooseYear.setBackground(new java.awt.Color(123, 183, 246));
        cbb_BieuDo_chooseYear.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        cbb_BieuDo_chooseYear.setForeground(new java.awt.Color(0, 102, 0));
        cbb_BieuDo_chooseYear.setMinimumSize(new java.awt.Dimension(61, 38));
        cbb_BieuDo_chooseYear.setName(""); // NOI18N
        cbb_BieuDo_chooseYear.setPreferredSize(new java.awt.Dimension(61, 38));
        cbb_BieuDo_chooseYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbb_BieuDo_chooseYearActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 102, 0));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Tổng số hóa đơn:");

        txt_bieuDo_tongHd.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txt_bieuDo_tongHd.setForeground(new java.awt.Color(255, 0, 0));
        txt_bieuDo_tongHd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_bieuDo_tongHd.setText("Tổng số hóa đơn:");

        jLabel17.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 102, 0));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Doanh thu:");

        txt_bieuDo_doanhThu.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txt_bieuDo_doanhThu.setForeground(new java.awt.Color(255, 0, 0));
        txt_bieuDo_doanhThu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_bieuDo_doanhThu.setText("Tổng số hóa đơn:");

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator1.setAlignmentX(2.0F);
        jSeparator1.setAlignmentY(2.0F);

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator2.setAlignmentX(2.0F);
        jSeparator2.setAlignmentY(2.0F);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbb_BieuDo_chooseYear, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_bieuDo_tongHd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_bieuDo_doanhThu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(cbb_BieuDo_chooseYear, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(txt_bieuDo_tongHd, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txt_bieuDo_doanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 0, 1, new java.awt.Color(255, 153, 0)));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlBieuDoLayout = new javax.swing.GroupLayout(pnlBieuDo);
        pnlBieuDo.setLayout(pnlBieuDoLayout);
        pnlBieuDoLayout.setHorizontalGroup(
            pnlBieuDoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBieuDoLayout.createSequentialGroup()
                .addGroup(pnlBieuDoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlItem2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBieuDoLayout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlBieuDoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlItem1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );
        pnlBieuDoLayout.setVerticalGroup(
            pnlBieuDoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBieuDoLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(pnlBieuDoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlBieuDoLayout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(pnlItem1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlItem2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlCard.add(pnlBieuDo, "card2");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(btnThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnBieuDo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(pnlCard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(btnThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(btnBieuDo, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlCard, javax.swing.GroupLayout.PREFERRED_SIZE, 619, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        new MainJFrame().setVisible(true);
        this.dispose();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnThongKeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnThongKeMouseEntered
        // TODO add your handling code here:
        btnThongKe.setBackground(new Color(235, 212, 23));

    }//GEN-LAST:event_btnThongKeMouseEntered

    private void btnBieuDoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBieuDoMouseEntered
        // TODO add your handling code here:
        btnBieuDo.setBackground(new Color(235, 212, 23));
    }//GEN-LAST:event_btnBieuDoMouseEntered

    private void btnBieuDoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBieuDoMouseExited
        // TODO add your handling code here:
        if (isSelectItem == 1)
            btnBieuDo.setBackground(new Color(255, 252, 248));
    }//GEN-LAST:event_btnBieuDoMouseExited

    private void btnThongKeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnThongKeMouseExited
        // TODO add your handling code here:
        if (isSelectItem == 2)
            btnThongKe.setBackground(new Color(255, 252, 248));
    }//GEN-LAST:event_btnThongKeMouseExited

    private void btnThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThongKeActionPerformed
        // TODO add your handling code here:
        btnThongKe.setBackground(new Color(235, 212, 23));
        btnBieuDo.setBackground(new Color(255, 252, 248));
        isSelectItem = 1;
        CardLayout layout = (CardLayout) pnlCard.getLayout();
        layout.first(pnlCard);
    }//GEN-LAST:event_btnThongKeActionPerformed

    private void btnBieuDoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBieuDoActionPerformed
        // TODO add your handling code here:
        btnBieuDo.setBackground(new Color(235, 212, 23));
        btnThongKe.setBackground(new Color(255, 252, 248));
        isSelectItem = 2;
        CardLayout layout = (CardLayout) pnlCard.getLayout();
        layout.last(pnlCard);
      
        try {
            String year = cbb_BieuDo_chooseYear.getSelectedItem()+"";
          renderBieuDoDoanhThu(year);
        } catch (SQLException ex) {
            Logger.getLogger(ThongKeJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_btnBieuDoActionPerformed

    private void renderBieuDoDoanhThu(String year) throws SQLException {
            tkService.showPieChart(pnlItem2,year);
             tkService.showBarChart(pnlItem1,year);
             DoanhThu dtNam = tkService.getDoanhThuTheoNam(year);
             txt_bieuDo_tongHd.setText(dtNam.getItem());
             txt_bieuDo_doanhThu.setText(currencyVN.format(dtNam.getDoanhThu()));
    }
    private void cbbThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbThongKeActionPerformed
        try {
            renderByCbb(txtFirstDate.getDate(), txtLastDate.getDate());
        } catch (SQLException ex) {
            Logger.getLogger(ThongKeJFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(ThongKeJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_cbbThongKeActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed

        Date date1 = txtFirstDate.getDate();

        Date date2 = txtLastDate.getDate();
        if (date1 == null) {
            txtFirstDate.setDate(null);
        }
        if (date2 == null) {
            JOptionPane.showMessageDialog(rootPane, "Vui lòng nhập đúng định dạng ngày \n"
                    + "Ví dụ: 18/11/2021", "Thông báo", JOptionPane.ERROR_MESSAGE);
            txtLastDate.requestFocus();
            txtLastDate.setBackground(Color.red);
        } else {

            if (cbbThongKe.getSelectedIndex() == 0) {
                try {
                    renderTableHoadon(hdService.getListHdByTime(date1, date2));
                    renderDoanhThuByHd();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            } else try {
                renderTableNongSan(nsService.getListNSByTime(date1, date2));
                renderDoanhThuByNs();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

        }
    }//GEN-LAST:event_btnSearchActionPerformed
    private void renderDoanhThuByHd() throws ParseException {
        float sum = 0;
        for (int i = 0; i < dtfListHdThongKe.getRowCount(); i++) {
            sum += (float) (long) currencyVN.parse((tblThongKe.getValueAt(i, 5) + ""));
        }
        lblTitleSum.setText("Tổng số hóa đơn:");
        lblCountHD.setText(dtfListHdThongKe.getRowCount() + "");
        lbltotalDoanhThu.setText(currencyVN.format(sum));
    }

    private void renderDoanhThuByNs() throws ParseException {
        float sum = 0;
        for (int i = 0; i < dtfListNsThongKe.getRowCount(); i++) {
            sum += (float) (long) currencyVN.parse((tblThongKe.getValueAt(i, 5) + ""));
        }
        lblTitleSum.setText("Tổng loại sản phẩm:");
        lblCountHD.setText(dtfListNsThongKe.getRowCount() + "");
        lbltotalDoanhThu.setText(currencyVN.format(sum));
    }
    private void txtLastDateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtLastDateMouseClicked
        // TODO add your handling code here:
        txtLastDate.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_txtLastDateMouseClicked

    private void btnFreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFreshActionPerformed
        // TODO add your handling code here:
        txtLastDate.setDate(new Date());
        txtFirstDate.setDate(null);
        try {
            renderByCbb(null, new Date());
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            Logger.getLogger(ThongKeJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnFreshActionPerformed

    private void tblThongKeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblThongKeMouseClicked
        if (evt.getClickCount() == 2) {
            int row = tblThongKe.getSelectedRow();
            if (cbbThongKe.getSelectedIndex() == 0) {
                dialogHdDetail.setVisible(true);
                dialogHdDetail.setLocationRelativeTo(null);
                try {
                    ShowHdDetail(row);
                } catch (SQLException ex) {
                    Logger.getLogger(ThongKeJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                ShowNsDetail(row);
            }
        }
    }//GEN-LAST:event_tblThongKeMouseClicked

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        dialogHdDetail.setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void cbb_BieuDo_chooseYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbb_BieuDo_chooseYearActionPerformed
        // TODO add your handling code here:
        String year = cbb_BieuDo_chooseYear.getSelectedItem()+"";
        try {
            renderBieuDoDoanhThu(year);
        } catch (SQLException ex) {
            Logger.getLogger(ThongKeJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_cbb_BieuDo_chooseYearActionPerformed

    private void ShowHdDetail(int row) throws SQLException {
        HoaDon hd = hdService.getHdById(tblThongKe.getValueAt(row, 1) + "");
        List<HoaDonDeTail> listHdDetail = hdDetailService.getListNongSanBan(tblThongKe.getValueAt(row, 1) + "");
        KhachHang kh = khService.getKhbyMaThe(hd.getMaThe() + "");

        txt_HdDt_idHd.setText(hd.getIdHD());
        txt_HdDt_ngayTao.setText(sdf.format(hd.getNgayBan()));
        txt_HdDt_NvBan.setText(tblThongKe.getValueAt(row, 3) + "");
        txt_HdDt_tenKh.setText(kh.getTenKH());
        txt_HdDt_diaChi.setText(kh.getDiachiKH());
        txt_HdDt_sdt.setText(kh.getSdtKH());
        txt_HdDt_tongTien.setText(currencyVN.format(hd.getTongTienBan()));
        txt_HdDt_giamGia.setText("- " + currencyVN.format(hd.getGiamGia()));
        txt_HdDt_thanhTien.setText(currencyVN.format(hd.getThanhTien()));

        DefaultTableModel dtfShowHdDeTail = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblShowHdDetail.setModel(dtfShowHdDeTail);

        dtfShowHdDeTail.addColumn("STT");
        dtfShowHdDeTail.addColumn("Tên NS");
        dtfShowHdDeTail.addColumn("Đơn vị tính");
        dtfShowHdDeTail.addColumn("Số lượng bán");
        dtfShowHdDeTail.addColumn("Giá bán");
        dtfShowHdDeTail.addColumn("Thành tiền");

       
        //setup header Table
        DefaultTableCellRenderer tbl_Header = new DefaultTableCellRenderer();
        tbl_Header.setHorizontalAlignment(JLabel.CENTER);
        tbl_Header.setFont(new Font("Arial", Font.BOLD, (int) 13));
        tbl_Header.setPreferredSize(new Dimension(100, 35));
        tbl_Header.setBackground(new Color(0,161,12));
        tbl_Header.setForeground(Color.white);

        tblShowHdDetail.getTableHeader().setDefaultRenderer(tbl_Header);

        //setup row Table
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblShowHdDetail.setDefaultRenderer(Object.class, centerRenderer);
        tblShowHdDetail.setAutoResizeMode(tblShowHdDetail.AUTO_RESIZE_NEXT_COLUMN);
        TableColumnModel colModel = tblShowHdDetail.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(20);
        colModel.getColumn(1).setPreferredWidth(50);

        dtfShowHdDeTail.setRowCount(0);

        for (int i = 0; i < listHdDetail.size(); i++) {
            NongSan ns = nsService.getNSbyId(listHdDetail.get(i).getIdNS() + "");
            Float thanhTien = listHdDetail.get(i).getSoluongBan() * listHdDetail.get(i).getDongiaMua();
            dtfShowHdDeTail.addRow(new Object[]{i + 1, ns.getTenNS(), ns.getDonviTinh(), listHdDetail.get(i).getSoluongBan(),
                listHdDetail.get(i).getDongiaMua(), currencyVN.format(thanhTien)});
        }
    }

    private void ShowNsDetail(int row) {

    }

    private void initClock() {
        ClockThread th = new ClockThread(lblClock, "hh:mm:ss aa");
        th.start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ThongKeJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ThongKeJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ThongKeJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ThongKeJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ThongKeJFrame().setVisible(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } catch (ParseException ex) {
                    Logger.getLogger(ThongKeJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBieuDo;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnFresh;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnThongKe;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbbThongKe;
    private javax.swing.JComboBox<String> cbb_BieuDo_chooseYear;
    private javax.swing.JDialog dialogHdDetail;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
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
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblClock;
    private javax.swing.JLabel lblCountHD;
    private javax.swing.JLabel lblTitleSum;
    private javax.swing.JLabel lbltotalDoanhThu;
    private javax.swing.JPanel pnlBieuDo;
    private javax.swing.JPanel pnlCard;
    private javax.swing.JPanel pnlItem1;
    private javax.swing.JPanel pnlItem2;
    private javax.swing.JPanel pnlThongKe;
    private javax.swing.JTable tblShowHdDetail;
    private javax.swing.JTable tblThongKe;
    private com.toedter.calendar.JDateChooser txtFirstDate;
    private com.toedter.calendar.JDateChooser txtLastDate;
    private javax.swing.JLabel txt_HdDt_NvBan;
    private javax.swing.JLabel txt_HdDt_diaChi;
    private javax.swing.JLabel txt_HdDt_giamGia;
    private javax.swing.JLabel txt_HdDt_idHd;
    private javax.swing.JLabel txt_HdDt_ngayTao;
    private javax.swing.JLabel txt_HdDt_sdt;
    private javax.swing.JLabel txt_HdDt_tenKh;
    private javax.swing.JLabel txt_HdDt_thanhTien;
    private javax.swing.JLabel txt_HdDt_tongTien;
    private javax.swing.JLabel txt_bieuDo_doanhThu;
    private javax.swing.JLabel txt_bieuDo_tongHd;
    // End of variables declaration//GEN-END:variables
}
