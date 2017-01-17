package reporteconexion;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterConfiguration;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.view.JRViewer;

/**
 * Clase que genera a través del driver JDBC y el archivo jasper generado por 
 * iReport un archivo pdf o html con los datos de la BBDD, además mostraremos
 * el pdf desde un JFrame
 */
public class ReporteConexion {
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conexion =
            (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/informes_di",
                                        "root", "campus");
        
            JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile("reporte_sesion1.jasper");

            JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, null, conexion);

            //Para PDF
            Exporter exporter;
            exporter = new JRPdfExporter();
            // new JRPdfExporter(); // o También así: Exporter exporter = new JRPdfExporter();

            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new
            java.io.File("reporteSesion1PDF.pdf")));
            SimplePdfExporterConfiguration configuration = new
            SimplePdfExporterConfiguration();
            exporter.setConfiguration(configuration);
            exporter.exportReport();
            
            /*// Para HTML
            Exporter exporter = new HtmlExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleHtmlExporterOutput(new
            java.io.File("reporteSesion1HTML.html")));
            SimpleHtmlExporterConfiguration configuration = new
            SimpleHtmlExporterConfiguration();
            exporter.setConfiguration(configuration);
            exporter.exportReport();*/
            
            // JFrame con una vista del pdf generado
            JFrame frame = new JFrame("Reporte");
            frame.getContentPane().add(new JRViewer(jasperPrint));
            frame.pack();
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
