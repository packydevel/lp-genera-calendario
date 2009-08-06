package org.lp.calendar.writers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.lp.calendar.AccoppiamentoVO;
import org.lp.calendar.Common;

public class Xls extends Write{
    private FileOutputStream fosXLS;
    private HSSFWorkbook wbXLS;
    private HSSFSheet sheet;
    private HSSFRow row;
    private HSSFCell cell;
    private HSSFCellStyle cs;
    private HSSFCellStyle cs2;
    private HSSFFont f;
    private HSSFFont f2;

    public Xls(String nome){
        super(nome);
    }

    public Xls(String nome, ArrayList<ArrayList<AccoppiamentoVO>> giornate, ArrayList<String> squadre) {
        super(nome, giornate, squadre);
    }
    
    public void writeOneSheet(){
        try {
            init(null);
            addSheet("Calendario", 0);
            int rownum = -1;
            for (int gg = 0; gg < getGiornate().size(); gg++) {
                ArrayList<AccoppiamentoVO> alAccopp = getGiornate().get(gg);
                addTitleRow(++rownum, "Giornata " + (gg+1));
                int size = alAccopp.size();
                for (int i = 0; i < size; i++){                    
                    AccoppiamentoVO singolo = alAccopp.get(i);
                    if (singolo.getRiposa()==-1)
                        addRowCells(++rownum, getSquadre().get(singolo.getCasa()-1),
                                getSquadre().get(singolo.getOspite()-1));
                    else
                        addRowCells(++rownum, "Riposa:",
                                getSquadre().get(singolo.getRiposa()-1));
                }
                addRow(++rownum);
            } //end for giornate
            // write the workbook to the output stream
            // close our file (don't blow out our file handles
            close();
        } catch (IOException ioe) {}
    }//end write
    
    public void writeMultiSheet(){
        try {
            init(null);  
            for (int gg = 0; gg < getGiornate().size(); gg++) {
                ArrayList<AccoppiamentoVO> alAccopp = getGiornate().get(gg);
                addSheet("Giornata " + (gg+1), gg);
                int size = alAccopp.size();
                for (int i = 0; i < size; i++){                    
                    AccoppiamentoVO singolo = alAccopp.get(i);
                    if (singolo.getRiposa()==-1)
                        addRowCells(i, getSquadre().get(singolo.getCasa()-1),
                                getSquadre().get(singolo.getOspite()-1));
                    else
                        addRowCells(i, "Riposa:",
                                getSquadre().get(singolo.getRiposa()-1));
                }                
            } //end for giornate            
            close();
        } catch (IOException ioe) {}
    }//end write

    @Override
    protected void init(String title) throws FileNotFoundException {
        // crea il file xls
        fosXLS = new FileOutputStream(
                                Common.controllaFile(getNomefile()+ getExt(WritersMode.XLS)));
        // create a new workbook
        wbXLS = new HSSFWorkbook();
        // create 2 cell styles
        cs = wbXLS.createCellStyle();
        cs2 = wbXLS.createCellStyle();
        // create 2 fonts objects
        f = wbXLS.createFont();
        f2 = wbXLS.createFont();
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
    }

    protected void addSheet(String name, int n) {
        // create a new sheet
        sheet = wbXLS.createSheet(name);
        // set the sheet name plain ascii
        wbXLS.setSheetName(n, name);
    }

    protected void addRow(int r){
        row = sheet.createRow(r);
    }

    protected void addTitleRow(int n, String text) {
        addRow(n);
        // create a numeric cell
        cell = row.createCell(0);
        cell.setCellStyle(cs);
        cell.setCellValue(new HSSFRichTextString(text));
    }

    protected void addRowCells(int r, String c1, String c2){
        // create a row
        addRow(r);
        // create a numeric cell
        cell = row.createCell(0);
        cell.setCellStyle(cs2);
        cell.setCellValue(new HSSFRichTextString(c1));
        cell = row.createCell(1);
        cell.setCellStyle(cs2);
        cell.setCellValue(new HSSFRichTextString(c2));
    }

    @Override
    protected void close() throws IOException {
        wbXLS.write(fosXLS);
        fosXLS.close();
    }
}