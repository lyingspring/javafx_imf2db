package importfile;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Pattern;

public class Imain {
    public static int allcount = 0;

    public static void main(String[] args) throws Exception {
        oracle_jdbc db = new oracle_jdbc();
        db.TestConnection();
        db.getConnection();
        String filename;
        int startrow;
        String cells;
        String tablename;
        System.out.print("输入EXCEL文件名,文件必须放在程序同一文件夹\n(输入all则根目录下EXCEL都导入):");
        Scanner scan = new Scanner(System.in);
        filename = scan.nextLine();
        System.out.print("输入开始行号:");
        startrow = Integer.valueOf(scan.nextLine());
        System.out.print("输入需导入的列号，用逗号分隔:");
        cells = scan.nextLine();
        System.out.print("输入需导入数据库表名:");
        tablename = scan.nextLine();

        if (!filename.equals("all")) {
            insertDBBatch(startrow, cells, tablename, filename, db);
            db.close();
            return;
        }

        File file = new File(System.getProperty("user.dir").toString());
        System.out.println("----------------");
        final String regex = ".*\\.xls.?";
        String[] paths2 = file.list(new FilenameFilter() {
            private Pattern pattern = Pattern.compile(regex);

            @Override
            public boolean accept(File dir, String name) {
                return pattern.matcher(name).matches();
            }
        });
        Arrays.sort(paths2, String.CASE_INSENSITIVE_ORDER);//排序
        for (String path : paths2) {
            System.out.println(path);
            insertDBBatch(startrow, cells, tablename, path, db);
        }

        db.close();

    }

    public static boolean checkUrl(String filepath, String regex) {


        try {
            if (filepath.matches(regex)) {
                File file = new File(filepath);
                if (file.exists()) {
                    return true;
                }
            } else {
                File file = new File(filepath);
                System.out.println("----------------");
                String[] paths2 = file.list(new FilenameFilter() {
                    private Pattern pattern = Pattern.compile(regex);

                    @Override
                    public boolean accept(File dir, String name) {
                        return pattern.matcher(name).matches();
                    }
                });
                if (paths2.length > 0) {
                    return true;
                } else {
                    return false;
                }

            }
        } catch (Exception e) {
            return false;
        }
        return false;

    }

    public static void importFile(String filename,
                                  int startrow,
                                  String cells,
                                  String tablename) throws Exception {
        oracle_jdbc db = new oracle_jdbc();
        db.TestConnection();
        db.getConnection();
        final String regex = "^(.*\\.xls.?|.*\\.txt|.*\\.csv)";
        cells = cells.replace("，", ",");
        if (filename.matches(regex)) {
            insertDBBatch(startrow, cells, tablename, filename, db);
            db.close();
            return;
        }

        //File file = new File(System.getProperty("user.dir").toString());
        File file = new File(filename);
        System.out.println("----------------");
        String[] paths2 = file.list(new FilenameFilter() {
            private Pattern pattern = Pattern.compile(regex);

            @Override
            public boolean accept(File dir, String name) {
                return pattern.matcher(name).matches();
            }
        });
        Arrays.sort(paths2, String.CASE_INSENSITIVE_ORDER);//排序
        for (String path : paths2) {
            System.out.println(path);
            insertDBBatch(startrow, cells, tablename, filename + "\\" + path, db);
        }

        db.close();
    }

    /**
     * 插入数据库一条提交一次
     *
     * @param startrow
     * @param cells
     * @param tablename
     * @param filename
     * @param db
     * @throws Exception
     */
    public static void insertDB(int startrow, String cells, String tablename,
                                String filename, oracle_jdbc db) throws Exception {
        DoExcel doexcel = new DoExcel();
        int count = 0;
        String sql;
        List<HashMap<String, String>> list = new ArrayList();
        if (filename.endsWith(".xlsx")) {
            list = doexcel.readExcel07(startrow, cells, System.getProperty("user.dir").toString() + "\\" + filename);
        } else {
            list = doexcel.readExcel(startrow, cells, System.getProperty("user.dir").toString() + "\\" + filename);
        }
        ResultSetMetaData data = db.getConn().createStatement().
                executeQuery("select * from " + tablename + " where rownum=1").getMetaData();
        long begin = System.currentTimeMillis();
        for (int i = 0; i < list.size(); i++) {
            sql = "insert into " + tablename + " values (";
            for (int j = 0; j < list.get(i).size(); j++) {
                sql = sql + "'" + list.get(i).get("cell" + j) + "',";
            }
            for (int j = 0; j < data.getColumnCount() - list.get(i).size(); j++) {
                sql = sql + "null,";
            }
            sql = sql.substring(0, sql.lastIndexOf(","));
            sql = sql + ")";
            System.out.println(sql);
            db.update(sql);
            count++;
            allcount++;
        }

        long end = System.currentTimeMillis();
        System.out.println(filename + "插入完成，共" + count + "条 " + (end - begin) + "ms");
        System.out.println("插入共" + allcount + "条");

    }

    /**
     * 批量插入数据库设置1000条提交一次
     *
     * @param startrow
     * @param cells
     * @param tablename
     * @param filename
     * @param db
     * @throws Exception
     */
    public static void insertDBBatch(int startrow, String cells, String tablename,
                                     String filename, oracle_jdbc db) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        DoExcel doexcel = new DoExcel();
        String filepath = System.getProperty("user.dir").toString() + "\\" + filename;
//        if(filename.contains("/")||filename.contains("\\")){//如果传入的是文件路径的话
//            filepath=filename;
//        }
        filepath = filename;//直接使用传入的文件路径
        ReadTXT_CSV readTXT_CSV = new ReadTXT_CSV();
        int count = 0;
        String sql;
        List<HashMap<String, String>> list = new ArrayList();
        if (filename.endsWith(".xlsx")) {
            list = doexcel.readExcelXlsx(startrow, cells, filepath);
        } else if (filename.endsWith(".xls")) {
            list = doexcel.readExcel(startrow, cells, filepath);
        } else {
            list = readTXT_CSV.readFile(startrow, cells, filepath);
        }
        ResultSetMetaData data = db.getConn().createStatement().
                executeQuery("select * from " + tablename + " where rownum=1").getMetaData();

        sql = "insert into " + tablename + " values (";
        for (int j = 0; j < data.getColumnCount(); j++) {
            sql = sql + "?,";
        }
        sql = sql.substring(0, sql.lastIndexOf(","));
        sql = sql + ")";
        connection = db.getConn();
        statement = connection.prepareStatement(sql);
        long begin = System.currentTimeMillis();
        for (int i = 0; i < list.size(); i++) {

            for (int j = 0; j < list.get(i).size(); j++) {
                statement.setString(j + 1, list.get(i).get("cell" + j));
            }
            for (int j = 0; j < data.getColumnCount() - list.get(i).size(); j++) {
                statement.setString(list.get(i).size() + j + 1, "");
            }
            statement.addBatch();
            if ((i + 1) % 1000 == 0) {
                statement.executeBatch();//1000条后才执行
                connection.commit();//提交
                statement.clearBatch();//清理
            }
            count++;
            allcount++;
        }
        statement.executeBatch();//执行
        connection.commit();//提交
        statement.clearBatch();//清理
        long end = System.currentTimeMillis();

        System.out.println(filename + "插入完成，共" + count + "条 " + (end - begin) + "ms");
        System.out.println("累计插入共" + allcount + "条");

    }

}
