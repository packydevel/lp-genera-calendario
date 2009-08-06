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

    public Xls(String nome, ArrayList<ArrayList<AccoppiamentoVO>> giornate, ArrayList<String> squadre) {
        super(nome, giornate, squadre);
    }

    @Override
    public void write(){
        try {
            initXLS();
            // create a new sheet
            sheet = wbXLS.createSheet(getNomefile());
            // set the sheet name plain ascii
            wbXLS.setSheetName(0, "Calendario");
            int rownum = -1;
            for (int gg = 0; gg < getGiornate().size(); gg++) {
                ArrayList<AccoppiamentoVO> alAccopp = getGiornate().get(gg);
                // create a row
                addRow(++rownum);
                // create a numeric cell
                cell = row.createCell(0);
                cell.setCellStyle(cs);
                cell.setCellValue(new HSSFRichTextString("Giornata " + (gg+1)));
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

    public void initXLS() throws FileNotFoundException {
        // crea il file xls
        fosXLS = new FileOutputStream(Common.controllaFile(getNomefile()+"_v1.xls"));
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

    public void addRow(int r){
        row = sheet.createRow(r);
    }

    public void addRowCells(int r, String c1, String c2){
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
    public void close() throws IOException {
        wbXLS.write(fosXLS);
        fosXLS.close();
    }
/*
    public void writeXLS2() {
        try {
            // create a new file
            fosXLS2 = new FileOutputStream(controllaFile(nomefile+"_v2.xls"));
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
*/
}