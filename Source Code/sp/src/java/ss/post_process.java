/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ss;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

/**
 *
 * @author mukherjee
 */
public class post_process {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {

        String work_dir = "C:\\SPIC\\data\\";

        post_process post = new post_process();
        ArrayList<String> fileList = new ArrayList<String>();
        fileList = post.getAllDominos(work_dir);
        int no_file = 0;

        for (String fileList1 : fileList) {
            post.change_fileContent(fileList1, no_file);
            no_file = no_file+2;
        }

    }

    public void change_fileContent(String source, int size) throws FileNotFoundException, UnsupportedEncodingException, IOException {

        ArrayList<String> data2 = new ArrayList<String>();
        String a1 = "";
        String a2 = "";

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(source), "UTF-8"));
            String line = "";
            ArrayList<String> data1 = new ArrayList<String>();
            a1 = br.readLine();
            a2 = br.readLine();
            while ((line = br.readLine()) != null) {
                String temp[] = line.split(",");
                if (temp.length > 0) {
                    data1.add(line);
                }
            }
            br.close();

//            data1.removeAll("");
            int train = (int) Math.round(data1.size() * 0.5);
            int trial = (int) Math.round(data1.size() * 0.5);

            System.out.println(data1.size() + "-" + train + "-" + trial + "-");

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data\\Trial" + size), "UTF-8"));
            bw.write(a1 + "\n");
            bw.write(a2 + "\n");
            for (int i = 0; i < train; i++) {
                bw.write(data1.get(i) + "\n");
            }
            bw.close();

            size=size+1;
            BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data\\Trial" + size), "UTF-8"));
            bw2.write(a1 + "\n");
            bw2.write(a2 + "\n");
            for (int i = train; i < data1.size(); i++) {
                bw2.write(data1.get(i) + "\n");
            }
            bw2.close();

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        System.out.println(source);
    }

    public ArrayList<String> getAllDominos(String path) throws FileNotFoundException, UnsupportedEncodingException, IOException {

        ArrayList<String> dominnos = new ArrayList<String>();
        File folder = new File(path);
        List<File> files = (List<File>) FileUtils.listFiles(folder, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        for (File file : files) {
            dominnos.add(file.getCanonicalPath());
//            System.out.println(file.getCanonicalPath());
        }

        return dominnos;
    }
}
