/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package QLNS.Controller;

import QLNS.Dao.HdDetailDao;
import QLNS.Model.DoanhThu;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author Admin
 */
public class ThongKeService {

    private HdDetailDao hdDetailDao;

    public ThongKeService() {
        hdDetailDao = new HdDetailDao();
    }

    public void RenderYearToCbb(JComboBox cbb) throws SQLException {
        hdDetailDao.RenderYearToCbb(cbb);
    }

    public DoanhThu getDoanhThuTheoNam(String year) throws SQLException {
        return hdDetailDao.getDoanhThuTheoNam(year);
    }

    public void showPieChart(JPanel jpnItem, String year) throws SQLException {

        List<DoanhThu> listDTT = hdDetailDao.getDoanhThuTheoThang(year);
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (listDTT != null) {
            for (DoanhThu dtt : listDTT) {
                dataset.addValue(dtt.getDoanhThu(), "Doanh thu", Integer.valueOf(dtt.getItem()));
            }
            JFreeChart barChart = ChartFactory.createBarChart(
                    "Biểu đồ thống kê doanh thu theo tháng".toUpperCase(),
                    "Tháng", "Doanh thu (đồng)",
                    dataset, PlotOrientation.VERTICAL, false, true, false);

            CategoryPlot categoryPlot = barChart.getCategoryPlot();
            categoryPlot.setBackgroundPaint(Color.gray);
            BarRenderer renderer = (BarRenderer) categoryPlot.getRenderer();
            Color clr3 = new Color(204, 21, 56);
            renderer.setSeriesPaint(0, clr3);

            ChartPanel chartPanel = new ChartPanel(barChart);
            chartPanel.setPreferredSize(new Dimension(jpnItem.getWidth(), jpnItem.getHeight()));

            jpnItem.removeAll();
            jpnItem.setLayout(new CardLayout());
            jpnItem.add(chartPanel);
            jpnItem.validate();
            jpnItem.repaint();

        }

    }

    public void showBarChart(JPanel jpnItem, String year) throws SQLException {
        DefaultPieDataset dataset = new DefaultPieDataset();
        List<DoanhThu> listDTPL = hdDetailDao.getDoanhThuTheoPL(year);
        if (listDTPL != null) {

            for (DoanhThu dtpl : listDTPL) {
                dataset.setValue(dtpl.getItem(), dtpl.getDoanhThu());
            }

            JFreeChart chart = ChartFactory.createPieChart3D("Doanh thu theo phân loại".toUpperCase(),
                    dataset, true, true, true);

            ChartPanel barpChartPanel = new ChartPanel(chart);
            barpChartPanel.setPreferredSize(new Dimension(jpnItem.getWidth(), jpnItem.getHeight()));
            jpnItem.setLayout(new CardLayout());
            jpnItem.removeAll();
            jpnItem.add(barpChartPanel);
            jpnItem.validate();
            jpnItem.repaint();
        }

    }

}
