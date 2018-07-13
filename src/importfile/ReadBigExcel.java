package importfile;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ReadBigExcel extends DefaultHandler{

    public void processOneSheet(String filename) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader( pkg );
        SharedStringsTable sst = r.getSharedStringsTable();

        XMLReader parser = fetchSheetParser(sst);

        // To look up the Sheet Name / Sheet Order / rID,
        //  you need to process the core Workbook stream.
        // Normally it's of the form rId# or rSheet#
        InputStream sheet2 = r.getSheet("rId1");
        InputSource sheetSource = new InputSource(sheet2);
        parser.parse(sheetSource);
        // parser.getContentHandler().
        sheet2.close();
    }

    public void processAllSheets(String filename) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader( pkg );
        SharedStringsTable sst = r.getSharedStringsTable();

        XMLReader parser = fetchSheetParser(sst);

        Iterator<InputStream> sheets = r.getSheetsData();
        while(sheets.hasNext()) {
            System.out.println("Processing new sheet:\n");
            InputStream sheet = sheets.next();
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
            System.out.println("");
        }
    }

    public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
        XMLReader parser =
                XMLReaderFactory.createXMLReader(
                        "com.sun.org.apache.xerces.internal.parsers.SAXParser"
                );
        this.sst = sst;
        parser.setContentHandler(this);
        return parser;
    }


        private SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;

        private List<HashMap<String,String>> hashlisi = new ArrayList();
        private HashMap<String,String> hashMap=new HashMap<String,String>();
        private int colnum=0;
        private int vcolnum=0;
        private int rownum=1;




        public List<HashMap<String,String>> getHashlisi(){

            return hashlisi;
        }


       // public ReadBigExcel(SharedStringsTable sst) {
//            this.sst = sst;
//        }

        public void startElement(String uri, String localName, String name,
                                 Attributes attributes) throws SAXException {
            int i;
            // c => cell
            if(name.equals("c")) {
                System.out.print(colnum);
                // Print the cell reference
                System.out.print(attributes.getValue("r") + " --- ");
                i=Integer.parseInt(attributes.getValue("r").replaceAll("[A-Z]",""));


                if(rownum<i){
                    rownum=i;
                    colnum=0;
                }
                colnum++;


                // Figure out if the value is an index in the SST
                String cellType = attributes.getValue("t");
                if(cellType != null && cellType.equals("s")) {
                    nextIsString = true;
                } else {
                    nextIsString = false;
                }
            }
            // Clear contents cache
            lastContents = "";
        }

        public void endElement(String uri, String localName, String name)
                throws SAXException {
            // Process the last contents as required.
            // Do now, as characters() may be called more than once
            if(nextIsString) {
                int idx = Integer.parseInt(lastContents);
                lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
                nextIsString = false;
            }

            // v => contents of a cell
            // Output after we've seen the string contents
            if(name.equals("v")) {
                System.out.println(lastContents+" hhh");
            }

            if(colnum>0) {
                hashMap.put("cell" + colnum, lastContents);
            }
            if(name.equals("row")){//如果标签名称为 row ，这说明已到行尾
                HashMap<String,String> hashMap2=new HashMap<String,String>();
                hashMap2.putAll(hashMap);
                hashlisi.add(hashMap2);
                hashMap.clear();

            }
        }

        public void characters(char[] ch, int start, int length)
                throws SAXException {
            lastContents += new String(ch, start, length);
        }



}
