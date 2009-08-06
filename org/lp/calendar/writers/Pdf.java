package org.lp.calendar.writers;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.lp.calendar.AccoppiamentoVO;
import org.lp.calendar.Common;

public class Pdf extends Write{

    private Document pdf;

    public Pdf(String nome){
        super(nome);
    }

    public Pdf(String nome, ArrayList<ArrayList<AccoppiamentoVO>> giornate, ArrayList<String> squadre) {
        super(nome, giornate, squadre);
    }

    @Override
    public void write(){
        try {
            init(null);
            for (int gg = 0; gg < getGiornate().size(); gg++) {
                ArrayList<AccoppiamentoVO> alAccopp = getGiornate().get(gg);
                addParagraphPdf("Giornata " + (gg+1), true);
                int size = alAccopp.size();
                for (int i = 0; i < size; i++){
                    String partita = Common.accoppiamenti(alAccopp.get(i), getSquadre(), i, size);
                    addParagraphPdf(partita, false);
                }
                addParagraphPdf("", false);
            }
            close();
        } catch (IOException ioe) {ioe.printStackTrace();}
        catch (DocumentException de) {de.printStackTrace();}
    }

    /**inizializza il pdf e apre lo stream
     *
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    @Override
    protected void init(String title) throws IOException, DocumentException{
        pdf = new Document(PageSize.A4);
        // crea il writer che ascolta il documento e directs a PDF-stream to a file
        PdfWriter.getInstance(pdf, new FileOutputStream(
                                Common.controllaFile(getNomefile()+ getExt(WritersMode.PDF))));
        // apertura documento
        pdf.open();
    }

    /**aggiunge al pdf il paragrafo composto dal testo
     *
     * @param testo testo da scrivere
     * @param bold true= testo da grassettare
     * @throws DocumentException
     */
    protected void addParagraphPdf(String testo, boolean bold) throws DocumentException{
        testo += "\n";
        if (bold)
            pdf.add(new Paragraph(testo, new Font(Font.TIMES_ROMAN, 12, Font.BOLD)));
        else
            pdf.add(new Paragraph(testo, new Font(Font.TIMES_ROMAN, 12)));
    }

    @Override
    protected void close(){
        pdf.close();
    }
}