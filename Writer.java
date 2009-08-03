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
    private ArrayList<ArrayList<AccoppiamentoVO>> alGiornate;
    private ArrayList<String> alSquadre;

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
    public void writeHtml(){
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
            bwHTML.close();
        } catch (IOException ioe) {ioe.printStackTrace();}
    } // end html

    /**Scrive su file pdf */
    public void writePdf(){
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
            bwHTML.close();
            pdf.close();
        } catch (IOException ioe) {ioe.printStackTrace();}
        catch (DocumentException de) {de.printStackTrace();}
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

    private void closeTableHTML() throws IOException{
        bwHTML.write("</table>\n");
    }

    private void initPDF() throws FileNotFoundException, DocumentException{
        pdf = new Document(PageSize.A4);
        // crea il writer che ascolta il documento e directs a PDF-stream to a file
        PdfWriter.getInstance(pdf, new FileOutputStream(controllaFile(nomefile+".pdf")));
        // apertura documento
        pdf.open();
    }

    private void addParagraphPdf(String testo, boolean bold) throws DocumentException{
        testo += "\n";
        if (bold)
            pdf.add(new Paragraph(testo, new Font(Font.TIMES_ROMAN, 12, Font.BOLD)));
        else
            pdf.add(new Paragraph(testo, new Font(Font.TIMES_ROMAN, 12)));
    }

    private String accoppiamenti(AccoppiamentoVO singolo, int i, int size){
        String partita = null;
        if ((i==size-1) && (singolo.getRiposa()!=0))
            partita = "Riposa: " + alSquadre.get(singolo.getRiposa()-1);
        else
            partita = alSquadre.get(singolo.getCasa()-1) + " - " +
                alSquadre.get(singolo.getOspite()-1);
        return partita;
    }
}