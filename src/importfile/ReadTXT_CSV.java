package importfile;

import fx_public.EncodeUtils;
import fx_public.UnicodeReader;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReadTXT_CSV {
    public List<HashMap<String, String>> readFile(int startrow, String cells, String filepath)
            throws Exception {
        //源文件
        File file = new File(filepath);
        if (!file.exists()) {
            // System.out.println("找不到该文件"+filepath);
            throw new IOException("找不到该文件" + filepath);
        }
        List<HashMap<String, String>> list = new ArrayList();
        String encode = EncodeUtils.getEncode(filepath, true);
        //这里要统一编码
        UnicodeReader read = new UnicodeReader(new FileInputStream(file), encode);
        BufferedReader bfr = new BufferedReader(read);

        String line = null;
        String cellNO[] = cells.split(",");
        int rownum = 1;
        while ((line = bfr.readLine()) != null) {
            if (rownum < startrow) {
                continue;
            }
            //byte[] utf8Bytes = line.getBytes("UTF-8");//将数据转换为UTF-8
            //line = new String(utf8Bytes, "UTF-8");
            HashMap<String, String> hashMap = new HashMap<String, String>();
            String item[] = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分
            if (cellNO[0].toUpperCase().equals("ALL")) {//全部列导入
                for (int j = 0; j < item.length; j++) {
                    if (item[j] == null) {
                        hashMap.put("cell" + j, "");
                    } else {
                        hashMap.put("cell" + j, item[j]);
                    }
                }
            } else {
                for (int j = 0; j < cellNO.length; j++) {
                    if (j > item.length - 1) {
                        hashMap.put("cell" + j, "");
                    } else {
                        hashMap.put("cell" + j, item[Integer.parseInt(cellNO[j]) - 1]);
                    }
                }
            }
            list.add(hashMap);
            //System.out.println("\n---------------");
            rownum++;
        }
        read.close();
        bfr.close();
        return list;
    }
}
