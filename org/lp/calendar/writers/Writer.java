package org.lp.calendar.writers;

import org.lp.calendar.AccoppiamentoVO;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**Effettua la scrittura del calendario su uno o più tipi di file
 *
 * @author Luca
 */
@SuppressWarnings("serial")
public class Writer {

    private final String curDir = System.getProperty("user.dir") + File.separator;    
    private BufferedWriter bwTXT, bwHTML;
    private Document pdf;
    private String nomefile;
    private ArrayList <ArrayList<AccoppiamentoVO>> alGiornate;
    private ArrayList<String> alSquadre;
    private FileOutputStream fosXLS1, fosXLS2;
    private HSSFWorkbook wbXLS1, wbXLS2;


    /**Costruttore
     *
     * @param nome nome del campionato
     * @param squadreAL lista squadre partecipanti
     * @param giornateAL lista risultato algoritmo
     */
    public Writer(String nome, ArrayList<String> squadreAL,
                ArrayList<ArrayList<AccoppiamentoVO>> giornateAL){
        nomefile = nome;
        alSquadre = squadreAL;
        alGiornate = giornateAL;
    }

    /**Restituisce la directory corrente
     * 
     * @return directory
     */
    public String getCurDir(){
        return curDir;
    }
    /**Scrive su file di testo */
    public void writeTXT() {
        String crlf = "\r\n";
        try {
            bwTXT = creaFile(nomefile + ".txt");
            for (int gg = 0; gg < alGiornate.size(); gg++) {
                ArrayList<AccoppiamentoVO> alAccopp = alGiornate.get(gg);                
                bwTXT.write("Giornata " + (gg+1) + crlf);
                int size = alAccopp.size();
                for (int i = 0; i < size; i++){
                    String partita = accoppiamenti(alAccopp.get(i), i, size);
                    bwTXT.write(partita+crlf);
                }
                bwTXT.write(crlf);
            }
            bwTXT.close();
        } catch (IOException ioe) {ioe.printStackTrace();}
    } //end txt

    /**Scrive su file html */
    public void writeHTML(){
        try {
            bwHTML = creaFile(nomefile + ".html");
            initHtml("Calendario "+ nomefile);
            for (int gg = 0; gg < alGiornate.size(); gg++) {
                ArrayList<AccoppiamentoVO> alAccopp = alGiornate.get(gg);
                initTableHTML("Giornata " + (gg+1));
                int size = alAccopp.size();
                for (int i = 0; i < size; i++){
                    String partita = accoppiamenti(alAccopp.get(i), i, size);
                    initTableRowHTML(partita);
                }
                closeTableHTML();
            }
            closeHTML();
            bwHTML.close();
        } catch (IOException ioe) {ioe.printStackTrace();}
    } // end html

    /**Scrive su file pdf */
    public void writePDF(){
        try {
            initPDF();
            for (int gg = 0; gg < alGiornate.size(); gg++) {
                ArrayList<AccoppiamentoVO> alAccopp = alGiornate.get(gg);
                addParagraphPdf("Giornata " + (gg+1), true);
                int size = alAccopp.size();
                for (int i = 0; i < size; i++){
                    String partita = accoppiamenti(alAccopp.get(i), i, size);
                    addParagraphPdf(partita, false);
                }
                addParagraphPdf("", false);
            }
            pdf.close();
        } catch (FileNotFoundException fnfe) {fnfe.printStackTrace();}
        catch (DocumentException de) {de.printStackTrace();}
    }

    /**scrive su tutti i formati implementati */
    public void writeALL() {
        String crlf = "\r\n";
        try {
            bwTXT = creaFile(nomefile + ".txt");
            bwHTML = creaFile(nomefile + ".html");
            initPDF();
            initHtml("Calendario "+ nomefile);
            for (int gg = 0; gg < alGiornate.size(); gg++) {
                ArrayList<AccoppiamentoVO> alAccopp = alGiornate.get(gg);
                String giornata = "Giornata " + (gg+1);
                bwTXT.write(giornata + crlf);
                initTableHTML(giornata);
                addParagraphPdf(giornata, true);
                int size = alAccopp.size();
                for (int i = 0; i < size; i++){
                    String partita = accoppiamenti(alAccopp.get(i), i, size);
                    bwTXT.write(partita+crlf);
                    initTableRowHTML(partita);
                    addParagraphPdf(partita, false);
                }
                bwTXT.write(crlf);
                closeTableHTML();
                addParagraphPdf("", false);
            }
            bwTXT.close();
            closeHTML();
            bwHTML.close();
            pdf.close();
        } catch (IOException ioe) {ioe.printStackTrace();}
        catch (DocumentException de) {de.printStackTrace();}
    }

    public void writeXLS1(){
        try {
            // create a new file
            fosXLS1 = new FileOutputStream(controllaFile(nomefile+"_v1.xls"));
            // create a new workbook
            wbXLS1 = new HSSFWorkbook();
            // create a new sheet
            HSSFSheet s = wbXLS1.createSheet(nomefile);
            // declare a row object reference
            HSSFRow r = null;
            // declare a cell object reference
            HSSFCell c = null;
            // create 3 cell styles
            HSSFCellStyle cs = wbXLS1.createCellStyle();
            HSSFCellStyle cs2 = wbXLS1.createCellStyle();
            // create 2 fonts objects
            HSSFFont f = wbXLS1.createFont();
            HSSFFont f2 = wbXLS1.createFont();
            //set font 1 to 12 point type
            f.setFontHeightInPoints((short) 12);
            // make it bold
            //arial is the default font
            f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            //set font 2 to 10 point type
            f2.setFontHeightInPoints((short) 11);
            //set cell stlye
            cs.setFont(f);
            cs2.setFont(f2);
            // set the sheet name plain ascii
            wbXLS1.setSheetName(0, "Calendario");
            int rownum = -1;
            for (int gg = 0; gg < alGiornate.size(); gg++) {
                ArrayList<AccoppiamentoVO> alAccopp = alGiornate.get(gg);
                // create a row
                r = s.createRow(++rownum);
                // create a numeric cell
                c = r.createCell(0);
                c.setCellStyle(cs);
                c.setCellValue(new HSSFRichTextString("Giornata " + (gg+1)));
                int size = alAccopp.size();
                for (int i = 0; i < size; i++){
                    // create a row
                    r = s.createRow(++rownum);
                    // create a numeric cell
                    c = r.createCell(0);
                    c.setCellStyle(cs2);
                    AccoppiamentoVO singolo = alAccopp.get(i);
                    if (singolo.getRiposa()==-1){
                        c.setCellValue(new HSSFRichTextString(alSquadre.get(singolo.getCasa()-1)));
                        c = r.createCell(1);
                        c.setCellStyle(cs2);
                        c.setCellValue(new HSSFRichTextString(alSquadre.get(singolo.getOspite()-1)));
                    } else {
                        c.setCellValue(new HSSFRichTextString("Riposa:"));
                        c = r.createCell(1);
                        c.setCellStyle(cs2);
                        c.setCellValue(new HSSFRichTextString(alSquadre.get(singolo.getRiposa()-1)));
                    }
                }
                r = s.createRow(++rownum);               
            } //end for giornate
            // write the workbook to the output stream
            // close our file (don't blow out our file handles
            wbXLS1.write(fosXLS1);
            fosXLS1.close();
        } catch (IOException ioe) {}
    }//end writeXLS1

    public void writeXLS2() {
        try {
            // create a new file
            fosXLS2 = new FileOutputStream(controllaFile(nomefile+"_v2.xls"));
            // create a new workbook
            wbXLS2 = new HSSFWorkbook();
            // declare a row object reference
            HSSFRow r = null;
            // declare a cell object reference
            HSSFCell c = null;
            // create 3 cell styles
            HSSFCellStyle cs = wbXLS2.createCellStyle();
            HSSFCellStyle cs2 = wbXLS2.createCellStyle();
            // create 2 fonts objects
            HSSFFont f = wbXLS2.createFont();
            HSSFFont f2 = wbXLS2.createFont();
            //set font 1 to 12 point type
            f.setFontHeightInPoints((short) 12);
            // make it bold
            //arial is the default font
            f.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            //set font 2 to 10 point type
            f2.setFontHeightInPoints((short) 11);
            //set cell stlye
            cs.setFont(f);
            cs2.setFont(f2);
            for (int gg = 0; gg < alGiornate.size(); gg++) {
                ArrayList<AccoppiamentoVO> alAccopp = alGiornate.get(gg);
                // create a new sheet
                HSSFSheet s = wbXLS2.createSheet("Giornata " + (gg+1));
                // set the sheet name plain ascii
                wbXLS2.setSheetName(gg, "Giornata " + (gg+1));
                int size = alAccopp.size();
                for (int i = 0; i < size; i++){
                    // create a row
                    r = s.createRow(i);
                    // create a numeric cell
                    c = r.createCell(0);
                    c.setCellStyle(cs2);
                    AccoppiamentoVO singolo = alAccopp.get(i);
                    if (singolo.getRiposa()==-1){
                        c.setCellValue(new HSSFRichTextString(alSquadre.get(singolo.getCasa()-1)));
                        c = r.createCell(1);
                        c.setCellStyle(cs2);
                        c.setCellValue(new HSSFRichTextString(alSquadre.get(singolo.getOspite()-1)));
                    } else {
                        c.setCellValue(new HSSFRichTextString("Riposa:"));
                        c = r.createCell(1);
                        c.setCellStyle(cs2);
                        c.setCellValue(new HSSFRichTextString(alSquadre.get(singolo.getRiposa()-1)));
                    }
                }
            } //end for giornate
            // write the workbook to the output stream
            // close our file (don't blow out our file handles
            wbXLS2.write(fosXLS2);
            fosXLS2.close();
        } catch (IOException ioe) {}
    }

    /**crea il file, se esiste lo cancella, e inizializza il bufferedwriter
     *
     * @param nome nome del file
     * @return nuovo oggetto BufferedWriter
     * @throws IOException
     */
    private BufferedWriter creaFile(String nome) throws IOException{
        File file = controllaFile(nome);
        file.createNewFile();
        return new BufferedWriter(new FileWriter(file));
    }

    private File controllaFile(String nome){
        File file = new File(curDir + nome);
        if (file.exists())
            file.delete();
        return file;
    }

    /**inizializza l'html e scrive il codice iniziale
     *
     * @param titolo titolo dell'html
     * @throws IOException
     */
    private void initHtml(String titolo) throws IOException{
        bwHTML.write("<html>\n<head>");
        if (titolo!=null)
            bwHTML.write("\n<title>"+ titolo + "</title>\n");
        bwHTML.write("</head>\n<body>\n");
    }

    private void initTableHTML(String testo) throws IOException{
        bwHTML.write("<table>\n<tr><td><b>" + testo + "</b></td></tr>\n");
    }

    private void initTableRowHTML(String testo) throws IOException{
        bwHTML.write("<tr><td>" + testo + "</td></tr>\n");
    }

    /**Scrive il tag di chiusura della tabella
     *
     * @throws IOException
     */
    private void closeTableHTML() throws IOException{
        bwHTML.write("</table>\n");
    }

    private void closeHTML() throws IOException{
        bwHTML.write("</body></html>");
    }

    /**inizializza il pdf e apre lo stream
     *
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    private void initPDF() throws FileNotFoundException, DocumentException{
        pdf = new Document(PageSize.A4);
        // crea il writer che ascolta il documento e directs a PDF-stream to a file
        PdfWriter.getInstance(pdf, new FileOutputStream(controllaFile(nomefile+".pdf")));
        // apertura documento
        pdf.open();
    }

    /**aggiunge al pdf il paragrafo composto dal testo
     * 
     * @param testo testo da scrivere
     * @param bold true= testo da grassettare
     * @throws DocumentException
     */
    private void addParagraphPdf(String testo, boolean bold) throws DocumentException{
        testo += "\n";
        if (bold)
            pdf.add(new Paragraph(testo, new Font(Font.TIMES_ROMAN, 12, Font.BOLD)));
        else
            pdf.add(new Paragraph(testo, new Font(Font.TIMES_ROMAN, 12)));
    }

    private String accoppiamenti(AccoppiamentoVO singolo, int i, int size){
        String partita = null;
        if ((i==size-1) && (singolo.getRiposa()!=-1))
            partita = "Riposa: " + alSquadre.get(singolo.getRiposa()-1);
        else
            partita = alSquadre.get(singolo.getCasa()-1) + " - " +
                alSquadre.get(singolo.getOspite()-1);
        return partita;
    }
}