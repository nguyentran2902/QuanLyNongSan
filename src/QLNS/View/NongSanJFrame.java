/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package QLNS.View;

import QLNS.Model.NhanVien;
import QLNS.Controller.NongSanService;
import QLNS.Model.NongSan;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Admin
 */
public class NongSanJFrame extends javax.swing.JFrame {

    private NongSanService nsService;
    private DefaultTableModel defaultTableModel;
    private Locale localeVN = new Locale("vi", "VN");
    private NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
    private byte[] image_product = null;
    private int idNS;

    /**
     * Creates new form NongSanJFrame
     */
    public NongSanJFrame() throws SQLException, IOException {
        nsService = new NongSanService();
        initComponents();
        setLocationRelativeTo(null);
        this.setTitle("DANH MỤC NÔNG SẢN");
        nsService.RenderTypeNsToCbb(cbbTypeNS);
        defaultTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

        tblNongSan.setModel(defaultTableModel);

        defaultTableModel.addColumn("Mã NS");
        defaultTableModel.addColumn("Tên NS");
        defaultTableModel.addColumn("Phân loại");
        defaultTableModel.addColumn("Số lượng");
        defaultTableModel.addColumn("Đơn vị tính");
        defaultTableModel.addColumn("Đơn giá bán");

        setTableData(nsService.getListNongSan());

        //setup header Table
        DefaultTableCellRenderer tbl_Header = new DefaultTableCellRenderer();
        tbl_Header.setHorizontalAlignment(JLabel.CENTER);
        tbl_Header.setFont(new Font("Arial", Font.BOLD, (int) 13));
        tbl_Header.setPreferredSize(new Dimension(100, 35));
        tbl_Header.setBackground(new Color(0,161,12));
        tbl_Header.setForeground(Color.white);

        tblNongSan.getTableHeader().setDefaultRenderer(tbl_Header);

        //setup row Table
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tblNongSan.setDefaultRenderer(Object.class, centerRenderer);

        tblNongSan.setAutoResizeMode(tblNongSan.AUTO_RESIZE_NEXT_COLUMN);
        TableColumnModel colModel = tblNongSan.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(40);
        colModel.getColumn(2).setPreferredWidth(110);

        tblNongSan.requestFocus();
        tblNongSan.changeSelection(0, 0, false, false);
        setRowData(0);

        btnSaveAdd.setVisible(false);
        btnCancel.setVisible(false);
        cbbSort.setForeground(Color.GREEN);

    }

    //render Bang Nong san
    private void setTableData(List<NongSan> ListNS) {
        for (NongSan ns : ListNS) {
            defaultTableModel.addRow(new Object[]{ns.getIdNS(), ns.getTenNS(),
                ns.getPhanLoai(), ns.getSoluongKho(),
                ns.getDonviTinh(), currencyVN.format(ns.getDongiaBan())});
        }
    }

    //Render Nong san theo hang
    private void setRowData(int row) throws SQLException, IOException {
        NongSan ns = nsService.getNSbyId(tblNongSan.getValueAt(row, 0) + "");

        idNS = Integer.valueOf(tblNongSan.getValueAt(row, 0)+"");
        nameNS.setText(String.valueOf(tblNongSan.getValueAt(row, 1)));
        cbbTypeNS.setSelectedItem(String.valueOf(tblNongSan.getValueAt(row, 2)));
        soluongNS.setText(String.valueOf(tblNongSan.getValueAt(row, 3)));
        dvtNS.setText(String.valueOf(tblNongSan.getValueAt(row, 4)));
        giabanNS.setText(ns.getDongiaBan() + "");

        image_product = ns.getHinhAnh();
        if (image_product != null) {
            Image img = nsService.createImageFromByteArray(image_product);
            lblHinhAnh.setIcon(new ImageIcon(img));
        } else {
            ImageIcon icon = new ImageIcon(getClass().getResource("/QLNS/images/image-default.png"));
            lblHinhAnh.setIcon(icon);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        txt_tittle = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        nameNS = new javax.swing.JTextField();
        soluongNS = new javax.swing.JTextField();
        dvtNS = new javax.swing.JTextField();
        giabanNS = new javax.swing.JTextField();
        cbbTypeNS = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        lblHinhAnh = new javax.swing.JLabel();
        btnChonFile = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        scrTblNS = new javax.swing.JScrollPane();
        tblNongSan = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jtfSearch = new javax.swing.JTextField();
        cbbSort = new javax.swing.JComboBox<>();
        jPanel6 = new javax.swing.JPanel();
        btnEdit = new javax.swing.JButton();
        btnSaveAdd = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBounds(new java.awt.Rectangle(1, 1, 1, 1));

        jPanel1.setBackground(new java.awt.Color(0, 161, 12));

        jLabel1.setBackground(new java.awt.Color(0, 161, 12));
        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DANH MỤC NÔNG SẢN                  ");

        btnBack.setBackground(new java.awt.Color(255, 252, 248));
        btnBack.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/baseline_reply_black_18dp.png"))); // NOI18N
        btnBack.setText("Trở về");
        btnBack.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel4.setBackground(new java.awt.Color(204, 221, 251));

        txt_tittle.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        txt_tittle.setForeground(new java.awt.Color(0, 102, 255));
        txt_tittle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txt_tittle.setText("THÔNG TIN CHI TIẾT:");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 102, 0));
        jLabel3.setText("Tên Nông sản:");

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 102, 0));
        jLabel4.setText("Phân loại:");

        jLabel5.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 102, 0));
        jLabel5.setText("Số lượng:");

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 102, 0));
        jLabel6.setText("Đơn vị tính:");

        jLabel7.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 102, 0));
        jLabel7.setText("Giá bán:");

        nameNS.setBackground(new java.awt.Color(255, 252, 248));
        nameNS.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        nameNS.setForeground(new java.awt.Color(0, 153, 204));

        soluongNS.setBackground(new java.awt.Color(255, 252, 248));
        soluongNS.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        soluongNS.setForeground(new java.awt.Color(0, 153, 204));

        dvtNS.setBackground(new java.awt.Color(255, 252, 248));
        dvtNS.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        dvtNS.setForeground(new java.awt.Color(0, 153, 204));

        giabanNS.setBackground(new java.awt.Color(255, 252, 248));
        giabanNS.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        giabanNS.setForeground(new java.awt.Color(0, 153, 204));

        cbbTypeNS.setBackground(new java.awt.Color(255, 252, 248));
        cbbTypeNS.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        cbbTypeNS.setForeground(new java.awt.Color(0, 153, 204));

        jLabel9.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 102, 0));
        jLabel9.setText("Hình ảnh");

        btnChonFile.setBackground(new java.awt.Color(255, 191, 8));
        btnChonFile.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnChonFile.setForeground(new java.awt.Color(255, 255, 255));
        btnChonFile.setText("Chọn file");
        btnChonFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonFileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(soluongNS, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dvtNS, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(giabanNS, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameNS, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbbTypeNS, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(30, 30, 30))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnChonFile)
                            .addComponent(lblHinhAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 104, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txt_tittle, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(84, 84, 84))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(txt_tittle, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameNS, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbbTypeNS, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(soluongNS, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dvtNS, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(giabanNS, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblHinhAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(btnChonFile, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel5.setBackground(new java.awt.Color(204, 221, 251));

        scrTblNS.setBackground(new java.awt.Color(255, 0, 51));
        scrTblNS.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 153, 0)));

        tblNongSan.setBackground(new java.awt.Color(204, 200, 244));
        tblNongSan.setFont(new java.awt.Font("Arial", 0, 13)); // NOI18N
        tblNongSan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblNongSan.setRowHeight(28);
        tblNongSan.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblNongSan.setShowGrid(false);
        tblNongSan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNongSanMouseClicked(evt);
            }
        });
        scrTblNS.setViewportView(tblNongSan);

        jLabel8.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 102, 0));
        jLabel8.setText("Tìm kiếm (theo tên):");

        jtfSearch.setBackground(new java.awt.Color(255, 252, 248));
        jtfSearch.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jtfSearch.setForeground(new java.awt.Color(0, 102, 255));
        jtfSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jtfSearchKeyReleased(evt);
            }
        });

        cbbSort.setBackground(new java.awt.Color(255, 252, 248));
        cbbSort.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        cbbSort.setForeground(new java.awt.Color(0, 102, 0));
        cbbSort.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "------------ Sắp xếp ------------", "   Số lượng tăng dần", "   Số lượng giảm dần", "   Giá tăng dần", "   Giá giảm dần" }));
        cbbSort.setMinimumSize(new java.awt.Dimension(158, 25));
        cbbSort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbbSortActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(scrTblNS)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jtfSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbbSort, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbbSort, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jtfSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(scrTblNS))
        );

        cbbSort.getAccessibleContext().setAccessibleDescription("");

        jPanel6.setBackground(new java.awt.Color(204, 221, 251));

        btnEdit.setBackground(new java.awt.Color(255, 191, 8));
        btnEdit.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/baseline_edit_note_white_24dp.png"))); // NOI18N
        btnEdit.setText("Sửa thông tin");
        btnEdit.setBorder(null);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnSaveAdd.setBackground(new java.awt.Color(0, 102, 255));
        btnSaveAdd.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnSaveAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnSaveAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/baseline_save_white_24dp.png"))); // NOI18N
        btnSaveAdd.setText("Lưu NS mới");
        btnSaveAdd.setBorder(null);
        btnSaveAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveAddActionPerformed(evt);
            }
        });

        btnCancel.setBackground(new java.awt.Color(204, 0, 0));
        btnCancel.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnCancel.setForeground(new java.awt.Color(255, 255, 255));
        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/baseline_backspace_white_24dp.png"))); // NOI18N
        btnCancel.setText("Hủy bỏ");
        btnCancel.setBorder(null);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(85, Short.MAX_VALUE)
                .addComponent(btnSaveAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(131, 131, 131)
                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(131, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSaveAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        jPanel7.setBackground(new java.awt.Color(204, 221, 251));

        btnAdd.setBackground(new java.awt.Color(0, 153, 0));
        btnAdd.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/baseline_queue_white_24dp.png"))); // NOI18N
        btnAdd.setText("Thêm mới");
        btnAdd.setBorder(null);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnDelete.setBackground(new java.awt.Color(255, 0, 0));
        btnDelete.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/baseline_delete_forever_white_24dp.png"))); // NOI18N
        btnDelete.setText("Xóa");
        btnDelete.setBorder(null);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnRefresh.setBackground(new java.awt.Color(0, 102, 255));
        btnRefresh.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        btnRefresh.setForeground(new java.awt.Color(255, 255, 255));
        btnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/QLNS/images/baseline_cached_white_24dp.png"))); // NOI18N
        btnRefresh.setText("Làm mới");
        btnRefresh.setBorder(null);
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(95, 95, 95)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(80, 80, 80))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(57, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        new MainJFrame().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void tblNongSanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNongSanMouseClicked
        // TODO add your handling code here:
        int row = tblNongSan.getSelectedRow();
        try {
            setRowData(row);
        } catch (SQLException ex) {
            ex.printStackTrace();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        btnEdit.setVisible(true);
        btnAdd.setEnabled(true);
        btnDelete.setEnabled(true);
        btnSaveAdd.setVisible(false);
        btnCancel.setVisible(false);
    }//GEN-LAST:event_tblNongSanMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed

        
        txt_tittle.setText("THÊM NÔNG SẢN MỚI:");
        nameNS.setText("");
        cbbTypeNS.setSelectedIndex(0);
        soluongNS.setText("");
        dvtNS.setText("");
        giabanNS.setText("");
        nameNS.requestFocus();
        image_product = null;
        ImageIcon icon = new ImageIcon(getClass().getResource("/QLNS/images/image-default.png"));
        lblHinhAnh.setIcon(icon);
        btnEdit.setVisible(false);
        btnAdd.setEnabled(false);
        btnDelete.setEnabled(false);

        btnSaveAdd.setVisible(true);
        btnCancel.setVisible(true);
    }//GEN-LAST:event_btnAddActionPerformed

    private void refreshListNS() throws SQLException, IOException {

        defaultTableModel.setRowCount(0);
        try {
            setTableData(nsService.getListNongSan());
        } catch (SQLException ex) {
            Logger.getLogger(NongSanJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        setRowData(0);
        cbbSort.setSelectedIndex(0);
        jtfSearch.setText("");
        txt_tittle.setText("THÔNG TIN CHI TIẾT:");
        tblNongSan.requestFocus();
        tblNongSan.changeSelection(0, 0, false, false);
    }

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        try {
            refreshListNS();
        } catch (SQLException ex) {
            Logger.getLogger(NongSanJFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NongSanJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        btnEdit.setVisible(true);
        btnAdd.setEnabled(true);
        btnDelete.setEnabled(true);
        btnSaveAdd.setVisible(false);
        btnCancel.setVisible(false);
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        int row = tblNongSan.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(NongSanJFrame.this,
                    "Vui lòng chọn nông sản muốn xóa", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } else {
            int confirm = JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc chắn muốn xóa sản phẩm này?",
                    "Thông báo", JOptionPane.YES_NO_CANCEL_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                String idNS = String.valueOf(tblNongSan.getValueAt(row, 0));
                try {

                    if (nsService.existNongSanInHD(idNS)) {
                        JOptionPane.showMessageDialog(NongSanJFrame.this, "Không thể xóa sản phẩm \n"
                                + "Sản phẩm còn tồn tại trong danh mục hóa đơn",
                                "Thông báo", JOptionPane.ERROR_MESSAGE);

                    } else {
                        if (nsService.deleteNongSan(idNS)) {
                            JOptionPane.showMessageDialog(NongSanJFrame.this,
                                    "Xóa sản phẩm thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            refreshListNS();

                        } else {
                            JOptionPane.showMessageDialog(NongSanJFrame.this, "Xóa sản phẩm không thành công", "Thông báo", JOptionPane.ERROR_MESSAGE);

                        }
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();

                }

            }
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void jtfSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jtfSearchKeyReleased
        // TODO add your handling code here:
        String name = jtfSearch.getText();
        try {
            defaultTableModel.setRowCount(0);
            setTableData(nsService.searchNongSanByNameFromDB(name));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jtfSearchKeyReleased

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        try {
            // TODO add your handling code here:
            refreshListNS();
        } catch (SQLException ex) {
            Logger.getLogger(NongSanJFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NongSanJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        btnEdit.setVisible(true);
        btnAdd.setEnabled(true);
        btnDelete.setEnabled(true);
        btnSaveAdd.setVisible(false);
        btnCancel.setVisible(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveAddActionPerformed
        try {
            // TODO add your handling code here:

            if (isCorrect()) {
                NongSan ns = new NongSan();

                ns.setTenNS(nameNS.getText());
                ns.setPhanLoai(String.valueOf(cbbTypeNS.getSelectedItem()));
                ns.setSoluongKho(Float.valueOf(soluongNS.getText()));
                ns.setDonviTinh(dvtNS.getText());
                ns.setDongiaBan(Float.valueOf(giabanNS.getText()));
                ns.setHinhAnh(image_product);
                try {
                    if (nsService.addNongSan(ns)) {
                        JOptionPane.showMessageDialog(NongSanJFrame.this, "Thêm sản phẩm thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        refreshListNS();
                    } else {
                        JOptionPane.showMessageDialog(NongSanJFrame.this, "Thêm sản phẩm không thành công", "Thông báo", JOptionPane.ERROR_MESSAGE);

                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    Logger.getLogger(NongSanJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        } catch (ParseException ex) {
            Logger.getLogger(NongSanJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_btnSaveAddActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        try {
            // TODO add your handling code here:

            if (isCorrect()) {
                NongSan ns = new NongSan();
                ns.setIdNS(idNS);
                ns.setTenNS(nameNS.getText());
                ns.setPhanLoai(String.valueOf(cbbTypeNS.getSelectedItem()));
                ns.setSoluongKho(Float.valueOf(soluongNS.getText()));
                ns.setDonviTinh(dvtNS.getText());
                ns.setDongiaBan(Float.valueOf(giabanNS.getText()));

                ns.setHinhAnh(image_product);

                int confirm = JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc chắn muốn sửa thông tin sản phẩm này?",
                        "Thông báo", JOptionPane.YES_NO_CANCEL_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        if (nsService.editNongSan(ns)) {
                            JOptionPane.showMessageDialog(NongSanJFrame.this, "Sửa thông tin sản phẩm thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            refreshListNS();

                        } else {
                            JOptionPane.showMessageDialog(NongSanJFrame.this, "Sửa thông tin sản phẩm không thành công", "Lỗi", JOptionPane.ERROR_MESSAGE);

                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        Logger.getLogger(NongSanJFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(NongSanJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnEditActionPerformed


    private void cbbSortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbbSortActionPerformed
        // TODO add your handling code here:
        List<NongSan> list = null;
        try {
            list = nsService.searchNongSanByNameFromDB(jtfSearch.getText());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        if (cbbSort.getSelectedIndex() == 0) {
            try {
                refreshListNS();
            } catch (SQLException ex) {
                Logger.getLogger(NongSanJFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(NongSanJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            btnEdit.setVisible(true);
            btnAdd.setEnabled(true);
            btnDelete.setEnabled(true);
            btnSaveAdd.setVisible(false);
            btnCancel.setVisible(false);
        }
        if (cbbSort.getSelectedIndex() == 1) {
            try {
                renderAfterSort(nsService.getListNsSluongASC(list));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (cbbSort.getSelectedIndex() == 2) {
            try {
                renderAfterSort(nsService.getListNsSluongDESC(list));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (cbbSort.getSelectedIndex() == 3) {
            try {
                renderAfterSort(nsService.getListNsGiaASC(list));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (cbbSort.getSelectedIndex() == 4) {
            try {
                renderAfterSort(nsService.getListNsGiaDESC(list));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }//GEN-LAST:event_cbbSortActionPerformed

    private void btnChonFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonFileActionPerformed
        // TODO add your handling code here:
        JFileChooser Chooser = new JFileChooser();
        Chooser.setAcceptAllFileFilterUsed(false);
        Chooser.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp"));
        int retVal = Chooser.showOpenDialog(null);

        if (retVal == JFileChooser.APPROVE_OPTION) {
            File file = Chooser.getSelectedFile();
            String fileName = file.getAbsolutePath();
            Image imageIcon = new ImageIcon(fileName).getImage()
                    .getScaledInstance(lblHinhAnh.getWidth(), lblHinhAnh.getHeight(), Image.SCALE_SMOOTH);

            lblHinhAnh.setIcon(new ImageIcon(imageIcon));
            try {
                image_product = nsService.toByteArray(imageIcon);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


    }//GEN-LAST:event_btnChonFileActionPerformed

    public void renderAfterSort(List<NongSan> list) throws SQLException, IOException {
        defaultTableModel.setRowCount(0);

        setTableData(list);

        setRowData(0);
        tblNongSan.requestFocus();
        tblNongSan.changeSelection(0, 0, false, false);
        btnEdit.setVisible(true);
        btnAdd.setEnabled(true);
        btnDelete.setEnabled(true);
        btnSaveAdd.setVisible(false);
        btnCancel.setVisible(false);
    }

    //Check number
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    //Check đầu vào input
    public boolean isCorrect() throws ParseException {

        Boolean isOK = true;
        while (true) {
            if (nameNS.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Tên nông sản không được để trống",
                        "Lỗi nhập thông tin", JOptionPane.ERROR_MESSAGE);
                isOK = false;
                nameNS.requestFocus();
                break;
            }
            if (soluongNS.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Số lượng nông sản không được để trống",
                        "Lỗi nhập thông tin", JOptionPane.ERROR_MESSAGE);
                isOK = false;
                soluongNS.requestFocus();
                break;
            } else if (!isNumeric(soluongNS.getText())) {
                JOptionPane.showMessageDialog(rootPane, "Số lượng nông sản phải là 1 số",
                        "Lỗi nhập thông tin", JOptionPane.ERROR_MESSAGE);
                isOK = false;
                soluongNS.requestFocus();

                break;
            }
            if (dvtNS.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Đơn vị tính không được để trống",
                        "Lỗi nhập thông tin", JOptionPane.ERROR_MESSAGE);
                isOK = false;
                dvtNS.requestFocus();
                break;
            }
            if (giabanNS.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Giá bán nông sản không được để trống",
                        "Lỗi nhập thông tin", JOptionPane.ERROR_MESSAGE);
                isOK = false;
                giabanNS.requestFocus();
                break;
            }
            if (!isNumeric(giabanNS.getText())) {
                JOptionPane.showMessageDialog(rootPane, "Giá bán nông sản phải là 1 số",
                        "Lỗi nhập thông tin", JOptionPane.ERROR_MESSAGE);
                isOK = false;
                giabanNS.requestFocus();

                break;
            }

            break;
        }
        return isOK;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnChonFile;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSaveAdd;
    private javax.swing.JComboBox<String> cbbSort;
    private javax.swing.JComboBox<String> cbbTypeNS;
    private javax.swing.JTextField dvtNS;
    private javax.swing.JTextField giabanNS;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JTextField jtfSearch;
    private javax.swing.JLabel lblHinhAnh;
    private javax.swing.JTextField nameNS;
    private javax.swing.JScrollPane scrTblNS;
    private javax.swing.JTextField soluongNS;
    private javax.swing.JTable tblNongSan;
    private javax.swing.JLabel txt_tittle;
    // End of variables declaration//GEN-END:variables
}
