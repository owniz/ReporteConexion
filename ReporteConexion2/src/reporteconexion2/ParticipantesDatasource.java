package reporteconexion2;

import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
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
 *
 * @author dam2
 */

public class ParticipantesDatasource implements JRDataSource {
    private List<Participante> listaParticipantes = new ArrayList<>();
    private int indiceParticipanteActual = -1;
    
    @Override
    public boolean next() throws JRException {
        return ++indiceParticipanteActual<listaParticipantes.size();
    }

    @Override
    public Object getFieldValue(JRField jrField) throws JRException {
        Object valor = null;
        
        if("id".equals(jrField.getName())) {
            valor = listaParticipantes.get(indiceParticipanteActual).getId();
        } else if("nombre".equals(jrField.getName())) {
            valor = listaParticipantes.get(indiceParticipanteActual).getNombre();
        } else if("username".equals(jrField.getName())) {
            valor = listaParticipantes.get(indiceParticipanteActual).getUsername(); 
        } else if("password".equals(jrField.getName())) {
            valor = listaParticipantes.get(indiceParticipanteActual).getPassword();
        } else if("comentarios".equals(jrField.getName())) {
            valor = listaParticipantes.get(indiceParticipanteActual).getComentarios();
        }
        
        return valor;
    }
    
    public void addParticipante(Participante participante){
        this.listaParticipantes.add(participante);
    }
    
    public static void main(String[] args) {
        ParticipantesDatasource datasource = new ParticipantesDatasource();

        for (int i = 1; i<= 12; i++) {
            Participante p = new Participante(i, "Participante " + i, "Usuario "
                                    + i, "Pass " + i, "Comentarios para " + i);
            datasource.addParticipante(p);
        } 
           
        try{
            JasperReport reporte = (JasperReport)
            JRLoader.loadObjectFromFile("reporte2.jasper");
            JasperPrint jasperPrint = JasperFillManager.fillReport(reporte, null, datasource);
            Exporter exporter = new JRPdfExporter();

            exporter.setExporterInput(new
            SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new
            SimpleOutputStreamExporterOutput(new
            java.io.File("reporte2PDF.pdf")));
            SimplePdfExporterConfiguration configuration = new
            SimplePdfExporterConfiguration();
            exporter.setConfiguration(configuration);

            exporter.exportReport();
        } catch (JRException ex) {
            Logger.getLogger(ParticipantesDatasource.class.getName()).log(Level.SEVERE,
                               null, ex);
        }
    }
}
