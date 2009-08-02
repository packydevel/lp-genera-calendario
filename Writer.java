import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;

import com.lowagie.text.pdf.PdfWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        pdf = new Document(PageSize.A4,5,5,5,5);
        try {
            // crea il writer che ascolta il documento e directs a PDF-stream to a file
            PdfWriter.getInstance(pdf, new FileOutputStream(controllaFile(nomefile)));
        }
        catch (FileNotFoundException fnfe) {}
        catch (DocumentException de) {}
        // apertura documento
        pdf.open();

    }

    /**scrive su tutti i formati implementati */
    public void writeALL() {
        String crlf = "\r\n";
        try {
            bwTXT = creaFile(nomefile + ".txt");
            bwHTML = creaFile(nomefile + ".html");
            initHtml("Calendario "+ nomefile);
            for (int gg = 0; gg < alGiornate.size(); gg++) {
                ArrayList<AccoppiamentoVO> alAccopp = alGiornate.get(gg);
                bwTXT.write("Giornata " + (gg+1) + crlf);
                initTableHTML("Giornata " + (gg+1));
                int size = alAccopp.size();
                for (int i = 0; i < size; i++){
                    String partita = accoppiamenti(alAccopp.get(i), i, size);
                    bwTXT.write(partita+crlf);
                    initTableRowHTML(partita);
                }
                bwTXT.write(crlf);
                closeTableHTML();
            }
            bwTXT.close();
            bwHTML.close();
        } catch (IOException ioe) {ioe.printStackTrace();}
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