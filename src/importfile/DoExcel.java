package importfile;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DoExcel {

    public void testImportExcel() throws IOException {
        /**
         * 注意这只是07版本以前的做法对应的excel文件的后缀名为.xls
         * 07版本和07版本以后的做法excel文件的后缀名为.xlsx
         */
        //创建新工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //新建工作表
        HSSFSheet sheet = workbook.createSheet("hello");
        //创建行,行号作为参数传递给createRow()方法,第一行从0开始计算
        HSSFRow row = sheet.createRow(0);
        //创建单元格,row已经确定了行号,列号作为参数传递给createCell(),第一列从0开始计算
        HSSFCell cell = row.createCell(2);
        //设置单元格的值,即C1的值(第一行,第三列)
        cell.setCellValue("hello sheet");
        //输出到磁盘中
        FileOutputStream fos = new FileOutputStream(new File("E:\\root\\sheet\\11.xls"));
        workbook.write(fos);
        workbook.close();
        fos.close();
    }

    public void testReadExcel() throws Exception {
        //创建输入流
        FileInputStream fis = new FileInputStream(new File("C:\\Users\\woaim\\Desktop\\1.xls"));
        //通过构造函数传参
        HSSFWorkbook workbook = new HSSFWorkbook(fis);
        //获取工作表
        HSSFSheet sheet = workbook.getSheetAt(0);
        //获取行,行号作为参数传递给getRow方法,第一行从0开始计算
        HSSFRow row = sheet.getRow(5);
        //获取单元格,row已经确定了行号,列号作为参数传递给getCell,第一列从0开始计算
        HSSFCell cell = row.getCell(2);
        //设置单元格的值,即C1的值(第一行,第三列)
        String cellValue = cell.getStringCellValue();
        System.out.println("第一行第三列的值是" + cellValue);
        workbook.close();
        fis.close();
    }

    /**
     * 读取XLS结尾的EXCEL文件
     *
     * @param startrow
     * @param cells
     * @param filepath
     * @return
     * @throws Exception
     */
    public List<HashMap<String, String>> readExcel(int startrow, String cells, String filepath) throws Exception {
        //创建输入流
        FileInputStream fis = new FileInputStream(new File(filepath));
        //通过构造函数传参
        HSSFWorkbook workbook = new HSSFWorkbook(fis);
        //获取工作表
        HSSFSheet sheet = workbook.getSheetAt(0);
        List<HashMap<String, String>> list = new ArrayList();
        String cellNO[] = cells.split(",");
        String cellValue = "";
        for (int i = startrow - 1; i < sheet.getLastRowNum() + 1; i++) {
            HSSFRow row = sheet.getRow(i);
            HashMap<String, String> map = new HashMap<String, String>();
            if (cellNO[0].toUpperCase().equals("ALL")) {//所有的列都导入
                for (int j = 0; j < row.getLastCellNum() + 1; j++) {
                    HSSFCell cell = row.getCell(j);
                    if (cell == null) {
                        map.put("cell" + j, "");
                        continue;
                    }
                    cell.setCellType(CellType.STRING);
                    cellValue = cell.getStringCellValue();
                    map.put("cell" + j, cellValue);
                    // System.out.println("第"+i+1+"行第"+cellNO[j]+"列的值是"+cellValue);
                }
            } else {//只导入前台输入的列
                for (int j = 0; j < cellNO.length; j++) {
                    HSSFCell cell = row.getCell(Integer.valueOf(cellNO[j]) - 1);
                    if (cell == null) {
                        map.put("cell" + j, "");
                        continue;
                    }
                    cell.setCellType(CellType.STRING);
                    cellValue = cell.getStringCellValue();
                    map.put("cell" + j, cellValue);
                    // System.out.println("第"+i+1+"行第"+cellNO[j]+"列的值是"+cellValue);
                }
            }
            list.add(map);
        }

        workbook.close();
        fis.close();
        return list;
    }

    /**
     * 用户模式。该模式不会一下子整个文件load进来放在内存里，而是一行一行的读取，这样就能避免内存溢出
     *
     * @param startrow
     * @param cells
     * @param filepath
     * @return
     * @throws Exception
     */
    public List<HashMap<String, String>> readExcelXlsx(int startrow, String cells, String filepath) throws Exception {
        List<HashMap<String, String>> list = new ArrayList();
        List<HashMap<String, String>> listold = new ArrayList();
        String cellNO[] = cells.split(",");
        ReadBigExcel readexcel = new ReadBigExcel();
        //System.out.println("11");
        readexcel.processOneSheet(filepath);
        listold = readexcel.getHashlisi();

        for (int i = 0; i < listold.size(); i++) {
            if (i < startrow - 1) {
                continue;
            }
            HashMap<String, String> hashMap = new HashMap<String, String>();
            if(cellNO[0].toUpperCase().equals("ALL")){
                hashMap=listold.get(i);
            }
            else {
                for (int j = 0; j < cellNO.length; j++) {
                    hashMap.put("cell" + j, listold.get(i).get("cell" + cellNO[j]));
                }
            }
            list.add(hashMap);
        }

        return list;
    }


    /**
     * Apache poi的普通模式读取会抛内存溢出不建议使用
     * 读取XLSX结尾的EXCEL文件
     *
     * @param startrow
     * @param cells
     * @param filepath
     * @return
     * @throws Exception
     */
    public List<HashMap<String, String>> readExcel07(int startrow, String cells, String filepath) throws Exception {
        //创建输入流
        FileInputStream fis = new FileInputStream(new File(filepath));
        //通过构造函数传参
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        //获取工作表
        XSSFSheet sheet = workbook.getSheetAt(0);
        List<HashMap<String, String>> list = new ArrayList();
        String cellNO[] = cells.split(",");
        String cellValue = "";
        for (int i = startrow - 1; i < sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            HashMap<String, String> map = new HashMap<String, String>();
            for (int j = 0; j < cellNO.length; j++) {
                XSSFCell cell = row.getCell(Integer.valueOf(cellNO[j]) - 1);
                cell.setCellType(CellType.STRING);
                cellValue = cell.getStringCellValue();
                map.put("cell" + j, cellValue);
                // System.out.println("第"+i+1+"行第"+cellNO[j]+"列的值是"+cellValue);
            }
            list.add(map);
        }

        workbook.close();
        fis.close();
        return list;
    }


}
