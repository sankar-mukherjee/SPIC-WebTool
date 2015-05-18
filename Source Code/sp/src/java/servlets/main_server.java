/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import js.server2;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 *
 * @author mukherjee
 */
@WebServlet(name = "main_server", urlPatterns = {"/main_server"})
public class main_server extends HttpServlet {

    public final String wrok_dir = "C:\\SPIC\\";
    public long start;
    public ArrayList<String> data;
    public ArrayList<String> IP;
    double startTime;
    double stopTime;
    public String words = "Wait for the Beep,Click Submit";
    public int dommino_size = 0;
    public int c_dommino_size = 0;

    public File startfile = new File(wrok_dir + "start");
    public File switchNewDom = new File(wrok_dir + "mark.txt");
    public File IP_file = new File(wrok_dir + "IPs");
    public File Del_IP_file = new File(wrok_dir + "del_IP");

    static Logger log = Logger.getLogger(server2.class.getName());

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            System.out.println();
            start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void start() throws FileNotFoundException {
        DOMConfigurator.configure("log4j.xml");
        ArrayList<String> ips = new ArrayList<String>();
        String ip1 = "";
        String ip2 = "";
        String ip = "";
        String chainRepet = "";
        int chainCount = 0;

        boolean cc = true;
        String[] marking = {"X", "Y"};

        String dommino_path = wrok_dir + "data\\";

        File f = new File(wrok_dir + "IPs");
        if (f.exists()) {
            f.delete();
        }
        restartTime();

        try {
            ArrayList<String> allDominos = getAllDominos(dommino_path);   //get all dominno in data dir
            data = build_dominno(allDominos.get(0));              //get the first one
            dommino_size = data.size();

            ServerSocket socket = new ServerSocket(8765);
            while (true) {

                // Restarts the whole experiment
                if (startfile.exists()) {
                    allDominos = getAllDominos(dommino_path);   //get all dominno in data dir
                    data = build_dominno(allDominos.get(0));              //get the first one
                    dommino_size = data.size();
                    chainCount = 0;
                    chainRepet = "";
                    startfile.delete();
                    restartTime();
                }

                if (switchNewDom.exists()) {                // for new chain
                    chainCount = chainCount + 1;
                    if (chainCount < allDominos.size()) {
                        data = build_dominno(allDominos.get(chainCount));
                        dommino_size = data.size();
                    } else {
                        chainRepet = "Chains are repeted";
                        log.info("X,X,X,X,X");
                    }
                    switchNewDom.delete();
                    log.info("N,N,N,N,N");
                }

                Socket insocket = socket.accept();

                if (ips.size() == 2 && cc) {
                    ip1 = ips.get(0);
                    ip2 = ips.get(1);
                    cc = false;
                }
                BufferedReader in = new BufferedReader(new InputStreamReader(insocket.getInputStream()));
                PrintWriter out = new PrintWriter(insocket.getOutputStream(), true);
                ip = in.readLine();

                if (!ips.contains(ip)) {
                    ips.add(ip);
                    writeIP(ip);
                }
                if (ips.size() > 2) {
                    out.println("More than 3 computers connected");
                    if (Del_IP_file.exists()) {
                        ips.clear();
                        Del_IP_file.delete();
                        if (IP_file.exists()) {
                            IP_file.delete();
                        }
                    }
                } else if (ips.size() == 1) {
                    ip1 = ips.get(0);
                    out.println(normal_page(ip1));
                } else if (ips.size() == 2 && ips.contains(ip)) {

                    if (data.isEmpty()) {
                        chainCount = chainCount + 1;
                        if (chainCount >= allDominos.size()) {
                            chainRepet = "End of All Trials";
                            log.info("-,-,-,-,-");
                        } else {
                            data = build_dominno(allDominos.get(chainCount));
                            dommino_size = data.size();
                        }
                    }

                    if (ip.equals(marking[1])) {
                        stopTime = elapsedTime();
                        if (chainCount < allDominos.size()) {
                            log.info(allDominos.get(chainCount) + "," + ip + "," + startTime + "," + stopTime + "," + words);
                        }
                        System.out.println("i am master " + ip + "," + "," + chainCount + "," + words);
                        String tmp1 = ip1;
                        String tmp2 = ip2;
                        ip1 = tmp2;
                        ip2 = tmp1;
                        if (!data.isEmpty()) {
                            words = data.get(0);
                            data.remove(0);
                            c_dommino_size = dommino_size - data.size();

                        } else {
                            words = "The End,Thank You";
                        }
                        marking[1] = "X";
                    }
                    if (ip.equals(ip1)) {
                        out.println(refresh_page(dommino_size, c_dommino_size));
                        marking[0] = ip;
                    } else if (ip.equals(ip2)) {
                        out.println(good_page2(words, chainRepet, dommino_size, c_dommino_size));
                        marking[1] = ip;
                        startTime = elapsedTime();
                    }
//                    System.out.println("original ip " + "\t" + marking[0] + "\t" + marking[1] + "\n"
//                            + "current ip" + "\t" + ip1 + "\t" + ip2 + "\n"
//                            + "ball is in" + "\t" + marking[1] + "\n"
//                            + "massage " + "\t" + ip);
                }
                insocket.close();
            }

        } catch (Exception e) {
        }
    }

    // return time (in seconds) since this object was created
    public double elapsedTime() {
        long now = System.currentTimeMillis();
        //return (now - start) / 1000.0;
        return (now - start) / 1e4;
    }

    public String normal_page(String ip) {

        String ss = "";

        ss = ss.concat("<head>\n"
                + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                + "        <title>Normal Page</title>\n"
                + "    </head>\n"
                + "    <body>\n"
                + "        <table border=\"1\" style=\"width: 300px;height: 300px; position:fixed; margin-left:-150px; margin-top:-150px; top:50%;left:50%;\">            \n"
                + "            <tbody>\n"
                + "                <tr style=\"height: 5%;\"><td><H1 style=\"text-align: center;\">SPIC</H1></td></tr>\n"
                + "                <tr><td>                        \n"
                + "                        <table border=\"0\" align=\"center\" style=\"text-align: center;font-size: x-large;\" style=\"width: 90%;\">\n"
                + "                            <tbody>\n"
                + "                                <tr>\n"
                + "                                    <td> Connected IP </td>\n"
                + "                                </tr>\n"
                + "                                <tr>\n"
                + "                                    <td>" + ip + "</td>\n"
                + "                                </tr>\n"
                + "                                <tr>\n"
                + "                                    <td>Wait for other computer</td>\n"
                + "                                </tr>\n"
                + "                            </tbody>\n"
                + "                        </table>\n"
                + "                    </td></tr>\n"
                + "            </tbody>\n"
                + "        </table>\n"
                + "    </body>");
        return ss;
    }

    public String good_page2(String sp, String msg, int t, int c) {
        String tmp[] = sp.split(",");

        tmp[1] = "";

        String nc = "";
        if (c == 1) {
            nc = "New Trial";
        } else {
            nc = "";
        }

        String ss = "";
        ss = ss.concat("<head>\n"
                + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                + "        <title>Good Page</title>\n"
                + "<script type=\"text/javascript\" src=\"javascript/voicedetection.js\"></script>"
                + "<script type=\"text/javascript\" src=\"javascript/vad.js\"></script>"
                + "    </head>\n"
                + "    <body>\n"
                + "<h1>" + msg + "</h1>"
                + "<h1>" + nc + "</h1>"
                + "<form action=\"newDommino\" method=\"post\">\n"
                + "            <a style=\"float: right\">\n"
                + "            <input type=\"submit\" name=\"button1\" value=\"New Chain\" /></a>\n"
                + "        </form>"
                + "<progress style=\"position:fixed; width: 60%; margin-left:-150px; margin-top:-180px; top:40%;left:25%;\" value=\"" + c + "\" max=\"" + t + "\"></progress>"
                + "        <table border=\"1\" style=\"width: 300px;height: 300px; position:fixed; margin-left:-150px; margin-top:-150px; top:50%;left:50%;\">            \n"
                + "            <tbody>                \n"
                + "                <tr><td>                        \n"
                + "                        <table border=\"0\" align=\"center\" style=\"width: 90%;text-align: center; font-size: xx-large\">\n"
                + "                            <tbody>\n"
                + "                                <tr style=\"height: 100px;\">\n"
                + "                                    <td>" + tmp[0] + "</td>\n"
                + "                                </tr>\n"
                + "                                <tr><td colspan=\"2\">\n"
                + "                                        <button style=\"width:100%;height: 50px; \"type=\"button\" onClick=\"window.location.reload();\">Submit</button>\n"
                + "                                    </td></tr>\n"
                + "                            </tbody>\n"
                + "                        </table>\n"
                + "                    </td></tr>\n"
                + "            </tbody>\n"
                + "        </table>\n"
                + "    </body>");

        return ss;
    }

    public String good_page(String sp, String msg, int t, int c) {
        String tmp[] = sp.split(",");
        if (tmp[1].equals("XXX")) {
            tmp[1] = "";
        }
        int y = 0;

        int x = (Math.random() < 0.5) ? 0 : 1;
        if (x == 0) {
            y = 1;
        }

        String nc = "";
        if (c == 1) {
            nc = "New Trial";
        } else {
            nc = "";
        }

        String ss = "";
        ss = ss.concat("<head>\n"
                + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                + "        <title>Good Page</title>\n"
                + "<script type=\"text/javascript\" src=\"javascript/voicedetection.js\"></script>"
                + "<script type=\"text/javascript\" src=\"javascript/vad.js\"></script>"
                + "    </head>\n"
                + "    <body>\n"
                + "<h1>" + msg + "</h1>"
                + "<h1>" + nc + "</h1>"
                + "<form action=\"newDommino\" method=\"post\">\n"
                + "            <a style=\"float: right\">\n"
                + "            <input type=\"submit\" name=\"button1\" value=\"New Chain\" /></a>\n"
                + "        </form>"
                + "<progress style=\"position:fixed; width: 60%; margin-left:-150px; margin-top:-180px; top:40%;left:25%;\" value=\"" + c + "\" max=\"" + t + "\"></progress>"
                + "        <table border=\"1\" style=\"width: 300px;height: 300px; position:fixed; margin-left:-150px; margin-top:-150px; top:50%;left:50%;\">            \n"
                + "            <tbody>                \n"
                + "                <tr><td>                        \n"
                + "                        <table border=\"0\" align=\"center\" style=\"width: 90%;text-align: center; font-size: xx-large\">\n"
                + "                            <tbody>\n"
                + "                                <tr style=\"height: 100px;\">\n"
                + "                                    <td>" + tmp[x] + "</td>\n"
                + "                                </tr>\n"
                + "                                <tr style=\"height: 100px;\">\n"
                + "                                    <td>" + tmp[y] + "</td>\n"
                + "                                </tr>\n"
                + "                                <tr><td colspan=\"2\">\n"
                + "                                        <button style=\"width:100%;height: 50px; \"type=\"button\" onClick=\"window.location.reload();\">Submit</button>\n"
                + "                                    </td></tr>\n"
                + "                            </tbody>\n"
                + "                        </table>\n"
                + "                    </td></tr>\n"
                + "            </tbody>\n"
                + "        </table>\n"
                + "    </body>");

        return ss;
    }

    public String refresh_page(int t, int c) {

        String ss = "";
        ss = ss.concat("<head>\n"
                + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                + "        <title>Wait Page</title><meta http-equiv='refresh' content='0.5' />\n"
                + "    </head>\n"
                + "    <body>\n"
                + "<progress style=\"position:fixed; width: 60%; margin-left:-150px; margin-top:-180px; top:40%;left:25%;\" value=\"" + c + "\" max=\"" + t + "\"></progress>"
                + "        <table border=\"1\" style=\"width: 300px;height: 300px; position:fixed; margin-left:-150px; margin-top:-150px; top:50%;left:50%;\">           \n"
                + "            <tbody>\n"
                + "                <tr style=\"height: 5%;\"><td><H3 style=\"text-align: center;\">Wait</H3></td></tr>\n"
                + "                <tr><td>                        \n"
                + "                        <table border=\"0\" align=\"center\" style=\"width: 90%;text-align: center; font-size: xx-large\">\n"
                + "                            <tbody>\n"
                + "                                <tr style=\"height: 100px;\">\n"
                + "                                    <td style=\"background-color: lightpink\">Listen carefully</td>\n"
                + "                                </tr>\n"
                + "                            </tbody>\n"
                + "                        </table>\n"
                + "                    </td></tr>\n"
                + "            </tbody>\n"
                + "        </table>\n"
                + "    </body>");
        return ss;
    }

    public ArrayList<String> build_dominno(String path) {
        ArrayList<String> data2 = new ArrayList<String>();
        try {
            //BufferedReader br = new BufferedReader(new FileReader(path));
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            String line = "";
            ArrayList<String> data1 = new ArrayList<String>();
            br.readLine();
            br.readLine();
            while ((line = br.readLine()) != null) {
                data1.add(line);
            }
            br.close();

            for (String t : data1) {
                String[] tmp = t.split(",");

                if (tmp.length == 5) {
                    data2.add(tmp[0] + "," + tmp[3]);
                }
            }
            data2.removeAll(Arrays.asList(null, ""));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        //data.remove(0);
        return data2;
    }

    public void switchNewDominno() throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(wrok_dir + "mark.txt");
        writer.close();
    }

    public ArrayList<String> getAllDominos(String path) throws FileNotFoundException, UnsupportedEncodingException {
        ArrayList<String> dominnos = new ArrayList<String>();
        File folder = new File(path);
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                System.out.println("Dir exists");
            } else {
                dominnos.add(fileEntry.getPath());
            }
        }
        return dominnos;
    }

    public void restart() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(wrok_dir + "start");
        writer.close();
    }

    public void writeIP(String ip) throws IOException {
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(IP_file, true)));
        out.println(ip);
        out.close();
    }

    public void restartTime() {
        java.util.Date date = new java.util.Date();
        log.info(new Timestamp(date.getTime()));
        log.info("trial,computer,startTime,stoptime,target,distract");
        start = System.currentTimeMillis();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
