/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.apache.tomcat.util.http.fileupload.FileUtils;

/**
 *
 * @author mukherjee
 */
public class Admini {

    public final String wrok_dir = "C:\\SPIC\\";
    public Process p;

    public void getLog() throws IOException {
        String Name = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        File source = new File(wrok_dir + "log.log");
        File dest = new File(wrok_dir + "stat\\" + Name);

        if (source.exists()) {
            System.out.println(dest + "---" + source);
        }
        copyFileUsingStream(source, dest);

    }

    public void ClearAllLog() throws IOException {
        FileUtils.cleanDirectory(new File(wrok_dir + "stat\\"));
    }

    public void ClearCurlog() throws IOException {
        File file = new File(wrok_dir + "log.log");
        if (file.exists()) {
            file.delete();
        }
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(wrok_dir + "log.log")));
        java.util.Date date = new java.util.Date();
        out.println(new Timestamp(date.getTime()));
        out.println("trial,computer,startTime,stoptime,target,distract");
        out.close();
    }

    public String getCurrentlogs() throws IOException {
        String dominnos = new String();
        File folder = new File(wrok_dir + "stat\\");
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
            } else {
                dominnos = dominnos.concat(fileEntry.getName() + "\n");
            }
        }
        return dominnos;
    }

    public void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = new FileInputStream(source);
        OutputStream os = new FileOutputStream(dest);
        try {
//            is = new FileInputStream(source);
//            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    public String getCurrentChains() throws IOException {
        String dominnos = new String();
        File folder = new File(wrok_dir + "data\\");
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
            } else {
                dominnos = dominnos.concat(fileEntry.getName() + "\n");
            }
        }
        return dominnos;
    }

    public void deleteChains(String name) throws IOException {
        String dominnos = new String();
        File file = new File(wrok_dir + "data\\" + name);
        if (file.exists()) {
            file.delete();
        }
    }

    public String getConectedIP() throws FileNotFoundException, IOException {
        String dominnos = "";
        String Z = "";
        File f = new File(wrok_dir + "IPs");
        if (f.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            while ((Z = reader.readLine()) != null) {
                dominnos = dominnos.concat(Z + "\n");
            }
            reader.close();
        } else {
            dominnos = "No Computer is Connected";
        }
        return dominnos;
    }

    public void DeloneIP() throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(wrok_dir + "del_IP")));
        out.close();
    }

    public String showLogs(String name) throws IOException {
        String log = new String();
        File file = new File(wrok_dir + "stat\\" + name);
        if (file.exists()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line = "";
            while ((line = br.readLine()) != null) {
                line = line.replace(wrok_dir + "data\\", "");
                String tmp[] = line.split(",");
                if (tmp.length > 1) {
                    log = log.concat(tmp[0] + "\t" + tmp[1] + "\t\t" + tmp[2] + "\t" + tmp[3] + "\t" + tmp[4] + "\t" + "\n");
                } else {
                    log = log.concat(line + "\n");
                }
            }
            br.close();
        }
        return log;
    }

    public String showCurrentlogs() throws IOException {
        String log = new String();
        File file = new File(wrok_dir + "log.log");
        if (file.exists()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String line = "";
            while ((line = br.readLine()) != null) {
                line = line.replace(wrok_dir + "data\\", "");
                String tmp[] = line.split(",");
                if (tmp.length > 1) {
                    log = log.concat(tmp[0] + "\t" + tmp[1] + "\t\t" + tmp[2] + "\t" + tmp[3] + "\t" + tmp[4] + "\t" + "\n");
                } else {
                    log = log.concat(line + "\n");
                }
            }
            br.close();
        }
        return log;
    }

    public void Start_server() {
        try {
            p = Runtime.getRuntime().exec("notepad.exe");
            //p = Runtime.getRuntime().exec("java -jar \"C:\\Users\\mukherjee\\Desktop\\sp\\SERVER\\JavaApplication1.jar\"");
        } catch (IOException e) {
        }
    }

    public void Stop_server() {
        p.destroy();
    }

    public class multiple {

        private final ArrayList<String> first;
        private final String second;

        public multiple(ArrayList<String> first, String second) {
            this.first = first;
            this.second = second;
        }

        public ArrayList<String> getFirst() {
            return first;
        }

        public String getSecond() {
            return second;
        }
    }

    public ArrayList<String> dominnoSetup(String ip) throws FileNotFoundException, UnsupportedEncodingException, IOException {

        File f = new File(wrok_dir + ip);
        if(f.exists()){
            f.delete();
        }
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
        out.println("target,distractor,startTime");
        out.close();

        js.server2 s2 = new js.server2();
        String dommino_path = wrok_dir + "data2\\";
        ArrayList<String> domminos = s2.getAllDominos(dommino_path);
        ArrayList<String> data = s2.build_dominno(domminos.get(0));              //get the first one

        return data;
    }

    public String getwords(ArrayList<String> d, String file) throws IOException {
        String data = "";
        if (d.isEmpty() || d.get(0).equals("END")) {
            data = "---,---";
        } else {
            data = d.get(0);              //get the first one
            FileWriter fw = new FileWriter(wrok_dir + file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data + "," + System.currentTimeMillis()+"\n");
            bw.close();
        }
        return data;
    }

    public ArrayList<String> dominnoUpdate(ArrayList<String> d) {

        if (d.isEmpty()) {
            d.add("END");
        } else {
            d.remove(0);
        }
        return d;
    }

    public String getSelfCurrentChains() throws IOException {
        String dominnos = new String();
        File folder = new File(wrok_dir + "data2\\");
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
            } else {
                dominnos = dominnos.concat(fileEntry.getName() + "\n");
            }
        }
        return dominnos;
    }
    
    public void deleteChainsSelf(String name) throws IOException {
        String dominnos = new String();
        File file = new File(wrok_dir + "data2\\" + name);
        if (file.exists()) {
            file.delete();
        }
    }
    
//    public String getConectedIP(){
//        
//    }
//    public void UploadServlet() throws IOException {
//        private boolean isMultipart;
//   private String filePath;
//   private int maxFileSize = 50 * 1024;
//   private int maxMemSize = 4 * 1024;
//   private File file ;
//
//   
//      isMultipart = ServletFileUpload.isMultipartContent(request);
//      response.setContentType("text/html");
//      java.io.PrintWriter out = response.getWriter( );
//      if( !isMultipart ){
//         out.println("<html>");
//         out.println("<head>");
//         out.println("<title>Servlet upload</title>");  
//         out.println("</head>");
//         out.println("<body>");
//         out.println("<p>No file uploaded</p>"); 
//         out.println("</body>");
//         out.println("</html>");
//         return;
//      }
//      DiskFileItemFactory factory = new DiskFileItemFactory();
//      // maximum size that will be stored in memory
//      factory.setSizeThreshold(maxMemSize);
//      // Location to save data that is larger than maxMemSize.
//      factory.setRepository(new File("c:\\temp"));
//
//      // Create a new file upload handler
//      ServletFileUpload upload = new ServletFileUpload(factory);
//      // maximum file size to be uploaded.
//      upload.setSizeMax( maxFileSize );
//
//      try{ 
//      // Parse the request to get file items.
//      List fileItems = upload.parseRequest(request);
//	
//      // Process the uploaded file items
//      Iterator i = fileItems.iterator();
//
//      out.println("<html>");
//      out.println("<head>");
//      out.println("<title>Servlet upload</title>");  
//      out.println("</head>");
//      out.println("<body>");
//      while ( i.hasNext () ) 
//      {
//         FileItem fi = (FileItem)i.next();
//         if ( !fi.isFormField () )	
//         {
//            // Get the uploaded file parameters
//            String fieldName = fi.getFieldName();
//            String fileName = fi.getName();
//            String contentType = fi.getContentType();
//            boolean isInMemory = fi.isInMemory();
//            long sizeInBytes = fi.getSize();
//            // Write the file
//            if( fileName.lastIndexOf("\\") >= 0 ){
//               file = new File( wrok_dir + 
//               fileName.substring( fileName.lastIndexOf("\\"))) ;
//            }else{
//               file = new File( wrok_dir + 
//               fileName.substring(fileName.lastIndexOf("\\")+1)) ;
//            }
//            fi.write( file ) ;
//            out.println("Uploaded Filename: " + fileName + "<br>");
//         }
//      }
//      out.println("</body>");
//      out.println("</html>");
//   }catch(Exception ex) {
//       System.out.println(ex);
//   }
//   }
//    
//}
//    }
}
