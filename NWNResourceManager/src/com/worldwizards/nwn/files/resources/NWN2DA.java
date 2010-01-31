package com.worldwizards.nwn.files.resources;

import com.worldwizards.nwn.files.NWNResource;
import com.worldwizards.util.ByteBufferUtils;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import com.worldwizards.util.CharBufferReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NWN2DA
        extends NWNResource {

    public static void register() {
        NWNResource.registerHandler(2017, "2da", NWN2DA.class);
    }
    private static Charset charset = Charset.forName("ISO-8859-15");
    private static CharsetDecoder decoder = charset.newDecoder();
    private String version;
    private String defaultEntry = "****";
    private List<String> headers = new ArrayList<String>();
    private List<List<String>> rows = new ArrayList<List<String>>();

    public NWN2DA(String name, short type, InputStream strm) {
        super(name, type);
        ByteBuffer buff = ByteBufferUtils.fromInputStream(strm);
        init(name, type, buff);
    }

    public NWN2DA(String name, short type, ByteBuffer buff) {
        super(name, type);
        init(name, type, buff);
    }

    private void init(String name, short type, ByteBuffer buff) {
        CharBuffer cbuffer = null;
        try {
            cbuffer = decoder.decode(buff);
        } catch (CharacterCodingException ex) {
            ex.printStackTrace();
            return;
        }
        CharBufferReader rdr = new CharBufferReader(cbuffer);
        BufferedReader brdr = new BufferedReader(rdr);
        boolean firstRow = true;
        try {
            version = brdr.readLine().trim();
            String defaultLine = brdr.readLine().trim();
            if (defaultLine.startsWith("DEFAULT:")) {
                defaultEntry = defaultLine.substring(8).trim();
                if (defaultEntry.charAt(0) == '"') {
                    defaultEntry = defaultEntry.substring(1, defaultEntry.length() - 1);
                }
            }
            List currentRow = null;
            while (brdr.ready()) {
                String row = brdr.readLine();
                while (row.equals("")){ // skip empty line
                    row = brdr.readLine();
                }
                boolean firstColumn = true;
                StringTokenizer tok = new StringTokenizer(row, " ");
                while (tok.hasMoreTokens()) {
                    String entry = tok.nextToken();
                    if (entry.charAt(0) == '"') {
                        entry = entry.substring(1, entry.length() - 1);
                    }
                    if (firstRow) { // do headers
                        headers.add(entry);
                    } else { // do rows
                        if (firstColumn) { // just throw away and reset
                            firstColumn = false;
                        } else {
                            if (entry.equals("****")) {
                                currentRow.add(defaultEntry);
                            } else {
                                currentRow.add(entry);
                            }
                        }
                    }
                }
                if (firstRow) {
                    firstRow = false;
                } else {
                    rows.add(currentRow);
                }
                currentRow = new ArrayList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getColNumber(String colname) {
        for (int i = 0; i < headers.size(); i++) {
            if (colname.equals(headers.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public String getEntry(int rownum, int colnum) {
        try {
            Logger.getLogger(this.getClass().getName()).log(
                    Level.FINEST, "NWN2DA.getEntry: " + rownum + "," + colnum);
            List row = (List) rows.get(rownum);
            if (row == null) {
                Logger.getLogger(this.getClass().getName()).log(
                        Level.SEVERE, "getEntry: Row " + rownum + "does not exist");
                return null;
            }
            String entry = (String) row.get(colnum);
            if (entry == null) {
                Logger.getLogger(this.getClass().getName()).log(
                        Level.SEVERE, "getEntry: Col " + colnum + " in row " +
                        rownum + "does not exist");
            }
            return entry;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds for " + rownum + "," + colnum);
            throw e;
        }
     
    }

    public void setEntry(int rownum, int colnum, String value) {
        List row = (List) rows.get(rownum);
        if (row != null) {
            row.set(colnum, value);
        }
    }

    public String[] getColumnHeaders() {
        String[] headerNames = new String[headers.size()];
        return headers.toArray(headerNames);
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public int getRowCount() {
        return rows.size();
    }

    public int getColumnCount() {
        return headers.size();
    }

    private int[] columnWidths() {
        int[] widths = new int[getColumnCount()];
        Arrays.fill(widths, 0);
        for (int i = 0; i < widths.length; i++) {
            widths[i] = Math.max(widths[i], getColumnHeaders()[i].length());
        }
        for (List<String> row : rows) {
            for (int i = 0; i < row.size(); i++) {
                widths[i] = Math.max(widths[i], row.get(i).length());
            }
        }
        return widths;
    }

    public void saveToFile(File file) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        PrintWriter w = new PrintWriter(file);
        // write file header info
        w.print("2DA V2.0\n\n");
        // print headers
        // first skip the numbers column 
        w.format("%5s", "");
        // now loop and print the headers in max size +2 sized columns
        int[] colWidths = columnWidths();
        int i = 0;
        for (String header : getColumnHeaders()) {
            w.format("%" + (colWidths[i++] + 2) + "s", header);
        }
        w.println();
        // now print rows
        int rowcount = 0;
        for (List<String> row : rows) {
            i = 0;
            w.format("%5d", rowcount++);
            for (String val : row) {
                w.format("%" + (colWidths[i++] + 2) + "s", val);
            }
            w.println();
        }
        w.flush();
        w.close();
    }
}
