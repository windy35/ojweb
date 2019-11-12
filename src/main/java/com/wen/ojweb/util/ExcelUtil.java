package com.wen.ojweb.util;

import com.wen.ojweb.model.Ojuser;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 导入Excel文件相关操作
 */
public class ExcelUtil {
//    在对Excel文件操作前，需要对版本进行判断。
    public static boolean isExcel2003(String filePath)
    {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    public static boolean isExcel2007(String filePath)
    {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    //有些单元格为Numeric格式，带有指数E。因此，若想获取其String类型时，需要进行格式转换。
    public static String getStringFromNumericCell(Cell cell)
    {
        return new DecimalFormat("#").format(cell.getNumericCellValue());
    }

    /**
     * 导入用户信息
     * @param file
     * @return
     */
    public List<Ojuser> importData(File file)
    {
        Workbook wb = null;
        List<Ojuser> list = new ArrayList();
        try
        {
            if (ExcelUtil.isExcel2007(file.getPath())) {
                try {
                    wb = new XSSFWorkbook(new FileInputStream(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                wb = new HSSFWorkbook(new FileInputStream(file));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }

        Sheet sheet = wb.getSheetAt(0);//获取第一张表
        //getLastRowNum()并非获取实际行数。因此，需要coder自行判断，是否已经到了最后一行（有效行）。
        for (int i = 0; i < sheet.getLastRowNum(); i++)
        {
            Row row = sheet.getRow(i);//获取索引为i的行，以0开始
            String name= row.getCell(0).getStringCellValue();//获取第i行的索引为0的单元格数据
            String pw= row.getCell(1).getStringCellValue();//获取第i行的索引为0的单元格数据
            if (name==null && pw==null)
            {
                break;
            }
            Ojuser ojuser = new Ojuser();
            ojuser.setUsername(name);
            ojuser.setPassword(pw);
            ojuser.setRole(0);
            ojuser.setSex(false);
            list.add(ojuser);
        }
        try
        {
            wb.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 导出用户信息
     * @param list
     * @param templetFilePath
     * @param exportFilePath
     */
    public static void exportHeroInfo(List<Ojuser> list,String templetFilePath, String exportFilePath){
        try {
            File exportFile=new File(exportFilePath);
            File templetFile= new File(templetFilePath);
            Workbook workBook;

            if(!exportFile.exists()){
                exportFile.createNewFile();
            }

            FileOutputStream out = new FileOutputStream(exportFile);
            FileInputStream fis = new FileInputStream(templetFile);
            if(isExcel2007(templetFile.getPath())){
                workBook=new XSSFWorkbook(fis);
            }else {
                workBook=new HSSFWorkbook(fis);
            }

            Sheet sheet=workBook.getSheetAt(0);

            int rowIndex = 1 ;
            for (Ojuser item : list) {
                Row row=sheet.createRow(rowIndex);
                row.createCell(0).setCellValue(item.getUsername());
                row.createCell(1).setCellValue(item.getPassword());
                rowIndex++;
            }

            workBook.write(out);
            out.flush();
            out.close();

            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
