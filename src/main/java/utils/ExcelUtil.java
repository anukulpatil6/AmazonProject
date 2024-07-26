package utils;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    public List<String[]> getExcelData(String filePath, String sheetName){
        List<String[]> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)){
            Sheet sheet = workbook.getSheet(sheetName);
            DataFormatter dataFormatter = new DataFormatter();


            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    int cellcount = row.getLastCellNum();
                    String[] rowData = new String[cellcount];
                    for (int i = 0; i < cellcount; i++) {
                        rowData[i] = dataFormatter.formatCellValue(row.getCell(i));
                    }
                    data.add(rowData);
                }
            }

        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}


