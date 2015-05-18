/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package js;

import java.sql.Timestamp;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

/**
 *
 * @author mukherjee
 */
public class server2 {

    public final String wrok_dir = "C:\\SPIC\\";
    public long start;
    public ArrayList<String> data;
    public ArrayList<String> IP;
    double startTime;
    double stopTime;
    public String words = "X";
    public int dommino_size = 0;
    public int c_dommino_size = 0;

    public File startfile = new File(wrok_dir + "start");
    public File switchNewDom = new File(wrok_dir + "mark.txt");
    public File IP_file = new File(wrok_dir + "IPs");
    public File Del_IP_file = new File(wrok_dir + "del_IP");

    static Logger log = Logger.getLogger(server2.class.getName());

    public void start() throws FileNotFoundException {

        DOMConfigurator.configure("C:\\SPIC\\log4j.xml");
        ArrayList<String> ips = new ArrayList<String>();
        String ip1 = "";
        String ip2 = "";
        String ip = "";
        String chainRepet = "";
        int chainCount = 0;

        boolean cc = true;
        boolean page_change = false;
        boolean page_g = false;
        boolean page_s = false;
        String[] marking = {"X", "Y"};

        String dommino_path = wrok_dir + "data\\";

        File f = new File(wrok_dir + "IPs");
        if (f.exists()) {
            f.delete();
        }
        java.util.Date date = new java.util.Date();
        log.info(new Timestamp(date.getTime()));
        log.info("trial,computer,startTime,stoptime,target,distract");
        start = System.currentTimeMillis();

        try {
            ArrayList<String> allDominos = getAllDominos(dommino_path);   //get all dominno in data dir
            data = build_dominno(allDominos.get(0));              //get the first one
            dommino_size = data.size();

            ServerSocket socket = new ServerSocket(8765);
            while (true) {

                // Restarts the whole experiment
                if (startfile.exists()) {
                    startfile.delete();
                    ips.clear();
                    ip1 = "";
                    ip2 = "";
                    ip = "";
                    chainRepet = "";
                    chainCount = 0;
                    cc = true;
                    words = "X";
                    marking[0] = "X";
                    marking[1] = "Y";

                    restartTime();
                    allDominos = getAllDominos(dommino_path);   //get all dominno in data dir
                    data = build_dominno(allDominos.get(0));              //get the first one
                    dommino_size = data.size();

                    if (IP_file.exists()) {
                        IP_file.delete();
                    }
                }

                if (switchNewDom.exists()) {                // for new chain

//                    if (!allDominos.isEmpty()) {
//                        allDominos.remove(0);
//                        if (!allDominos.isEmpty()) {
//                            data = build_dominno(allDominos.get(0));
//                            dommino_size = data.size();
//                        } else {
//                            chainRepet = "Chains are repeted";
//                            log.info("X,X,X,X,X");
//                        }
//                    } else {
//                        chainRepet = "Chains are repeted";
//                        log.info("X,X,X,X,X");
//                    }
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
                    words = data.get(0);
                    data.remove(0);
                    c_dommino_size = dommino_size - data.size();

                    ips.clear();
                    ip1 = "";
                    ip2 = "";
                    ip = "";
                    cc = true;
                    words = "X";
                    marking[0] = "X";
                    marking[1] = "Y";
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

                //for the index2 page which has only the button funcyion no voice detection                
                if (ip.contains("m")) {
                    String temp_msg[] = ip.split(",");
                    ip = temp_msg[0];
                    page_change = true;
                } else if (ip.contains("g")) {
                    String temp_msg[] = ip.split(",");
                    ip = temp_msg[0];
                    page_g = true;
                } else if (ip.contains("s")) {
                    String temp_msg[] = ip.split(",");
                    ip = temp_msg[0];
                    page_s = true;
                }

                if (!ips.contains(ip)) {
                    ips.add(ip);
                    writeIP(ip);
                    System.out.println("i am master " + ip + "," + "," + chainCount + "," + words);
                }
                if (ips.size() > 2) {
                    out.println("More than 3 computers connected");
                    if (Del_IP_file.exists()) {
                        ips.clear();
                        Del_IP_file.delete();
                        if (IP_file.exists()) {
                            IP_file.delete();
                        }
                        cc = true;
                    }
                } else if (ips.size() == 1) {
                    out.println(normal_page());
                } else if (ips.size() == 2 && ips.contains(ip)) {

                    if (data.isEmpty()) {
                        chainCount = chainCount + 1;
                        if (chainCount >= allDominos.size()) {
                            chainRepet = "End of All Trials";
                            log.info("-,-,-,-,-");
                        } else {
                            data = build_dominno(allDominos.get(chainCount));
                            dommino_size = data.size();

                            //code for welcome and normal page to display
                            ips.clear();
                            ip1 = "";
                            ip2 = "";
                            ip = "";
                            cc = true;
                            words = "X";
                            marking[0] = "X";
                            marking[1] = "Y";
                        }
                    }

                    if (ip.equals(marking[1])) {

                        stopTime = elapsedTime();
                        if (chainCount < allDominos.size()) {
                            log.info(allDominos.get(chainCount) + "," + ip + "," + startTime + "," + stopTime + "," + words);
                        }

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

                        if (words.equals("X")) {
                            out.println(welcome_page());
                        } else if (page_change) {
                            out.println(good_page_noV2(words, chainRepet, dommino_size, c_dommino_size));
                            page_change = false;
                        } else if (page_g) {
                            out.println(good_page_g2(words, chainRepet, dommino_size, c_dommino_size));
                            page_g = false;
                        } else if (page_g) {
                            out.println(good_page_s(words, chainRepet, dommino_size, c_dommino_size));
                            page_s = false;
                        }else {
                            out.println(good_page2(words, chainRepet, dommino_size, c_dommino_size));
                        }
                        marking[1] = ip;
                        startTime = elapsedTime();

                    } else {
                        out.println(refresh_page2());
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

    public static void main(String[] args) throws FileNotFoundException {

        //String dommino_path = "data\\";
        server2 d = new server2();
        d.start();
    }

    // return time (in seconds) since this object was created
    public double elapsedTime() {
        long now = System.currentTimeMillis();
        //return (now - start) / 1000.0;
        return (now - start) / 1e4;
    }

    public String normal_page() {

        String ss = "";

        ss = ss.concat("<head>\n"
                + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                + "        <title>Welcome</title><meta http-equiv='refresh' content='3' />\n"
                + "    </head>\n"
                + "    <body>\n"
                + "<a style=\"position:fixed; width: 60%; margin-left:-150px; margin-top:-180px; top:38%;left:25%;\">Your progress indicator bar</a>\n"
                + "<progress style=\"position:fixed; width: 60%; margin-left:-150px; margin-top:-180px; top:40%;left:25%;\" value=\"30\" max=\"100\"></progress>"
                + "        <table border=\"1\" style=\"width: 300px;height: 300px; position:fixed; margin-left:-150px; margin-top:-150px; top:50%;left:50%;\">            \n"
                + "            <tbody>\n"
                + "                <tr style=\"height: 5%;\"><td><H1 style=\"text-align: center;\">New Trial</H1></td></tr>\n"
                + "                <tr><td>                        \n"
                + "                        <table border=\"0\" align=\"center\" style=\"text-align: center;font-size: x-large;\" style=\"width: 90%;\">\n"
                + "                            <tbody>\n"
                + "                                <tr>\n"
                + "                                    <td>Wait for other computer</td>\n"
                + "                                </tr>\n"
                + "                                <tr>\n"
                + "                                    <td>Page will change as soon as other person is connected</td>\n"
                + "                                </tr>\n"
                + "                            </tbody>\n"
                + "                        </table>\n"
                + "                    </td></tr>\n"
                + "            </tbody>\n"
                + "        </table>\n"
                + "    </body>");
        return ss;
    }

    // page without the voice detection only button for index2.jsp
    public String welcome_page() {
        String ss = "";
        ss = ss.concat("<head>\n"
                + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                + "        <title>Good Page</title>\n"
                + "    </head>\n"
                + "    <body>\n"
                + "<a style=\"position:fixed; width: 60%; margin-left:-150px; margin-top:-180px; top:35%;left:25%;\">Your progress indicator bar</a>\n"
                + "<progress style=\"position:fixed; width: 60%; margin-left:-150px; margin-top:-180px; top:40%;left:25%;\" value=\"30\" max=\"100\"></progress>"
                + "        <table border=\"1\" style=\"width: 300px;height: 300px; position:fixed; margin-left:-150px; margin-top:-150px; top:50%;left:50%;\">            \n"
                + "            <tbody>                \n"
                + "                <tr><td>                        \n"
                + "                        <table border=\"0\" align=\"center\" style=\"width: 90%;text-align: center; font-size: xx-large\">\n"
                + "                            <tbody>\n"
                + "                                <tr style=\"height: 100px;\">\n"
                + "                                    <td>New Trial</td>\n"
                + "                                </tr>\n"
                + "                                <tr style=\"height: 100px;\">\n"
                + "                                    <td>Wait for the Signal Click Start </td>\n"
                + "                                </tr>\n"
                + "                                <tr><td colspan=\"2\">\n"
                + "                                        <button style=\"width:100%;height: 50px; \"type=\"button\" onClick=\"window.location.reload();\">Start</button>\n"
                + "                                    </td></tr>\n"
                + "                            </tbody>\n"
                + "                        </table>\n"
                + "                    </td></tr>\n"
                + "            </tbody>\n"
                + "        </table>\n"
                + "    </body>");

        return ss;
    }

    //================Beginner version only one word=============================================
    // only one word no distractor for beginner
    public String good_page(String sp, String msg, int t, int c) {
        String tmp[] = sp.split(",");

        tmp[1] = "";

        String nc = "";
        String color = "white";
        if (c == 1) {
            nc = "New Trial";
            color = "lightblue";
        } else {
            nc = "";
        }

        String ss = "";
        ss = ss.concat("<head>\n"
                + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                + "        <title>Good Page</title>\n"
                + "<script type=\"text/javascript\" src=\"javascript/SpeechRecognition.js\"></script>"
                + "<script type=\"text/javascript\" src=\"javascript/vad.js\"></script>"
                + "    </head>\n"
                + "    <body style=\"background-color: " + color + "\">\n"
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
                + "                            </tbody>\n"
                + "                        </table>\n"
                + "                    </td></tr>\n"
                + "            </tbody>\n"
                + "        </table>\n"
                + "    </body>");

        return ss;
    }

    // page without the voice detection only button for index2.jsp
    public String good_page_noV(String sp, String msg, int t, int c) {
        String tmp[] = sp.split(",");

        tmp[1] = "";

        String nc = "";
        String color = "white";
        if (c == 1) {
            nc = "New Trial";
            color = "lightblue";
        } else {
            nc = "";
        }

        String ss = "";
        ss = ss.concat("<head>\n"
                + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                + "        <title>Good Page</title>\n"
                + "    </head>\n"
                + "    <body style=\"background-color: " + color + "\">\n"
                + "<h1>" + msg + "</h1>"
                + "<h1>" + nc + "</h1>"
                + "<form action=\"newDommino\" method=\"post\">\n"
                + "            <a style=\"float: right\">\n"
                + "            <input type=\"submit\" name=\"button_noV\" value=\"New Chain\" /></a>\n"
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

    // page with google speech api for index3.jsp
    public String good_page_g(String sp, String msg, int t, int c) {
        String tmp[] = sp.split(",");

        tmp[1] = "";

        String nc = "";
        String color = "white";
        if (c == 1) {
            nc = "New Trial";
            color = "lightblue";
        } else {
            nc = "";
        }

        String ss = "";
        ss = ss.concat("<head>\n"
                + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                + "        <title>Good Page</title>\n"
                + "<script type=\"text/javascript\" src=\"javascript/googleSpeechapi.js\"></script>"
                + "    </head>\n"
                + "    <body style=\"background-color: " + color + "\">\n"
                + "<h1>" + msg + "</h1>"
                + "<h1>" + nc + "</h1>"
                + "<form action=\"newDommino\" method=\"post\">\n"
                + "            <a style=\"float: right\">\n"
                + "            <input type=\"submit\" name=\"button_g\" value=\"New Chain\" /></a>\n"
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
                + "                            </tbody>\n"
                + "                        </table>\n"
                + "                    </td></tr>\n"
                + "            </tbody>\n"
                + "        </table>\n"
                + "    </body>");

        return ss;
    }

    //================Advance version with distractor words=============================================
    // only voice detection
    public String good_page2(String sp, String msg, int t, int c) {
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
                + "<script type=\"text/javascript\" src=\"javascript/SpeechRecognition.js\"></script>"
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
                + "                            </tbody>\n"
                + "                        </table>\n"
                + "                    </td></tr>\n"
                + "            </tbody>\n"
                + "        </table>\n"
                + "    </body>");

        return ss;
    }

    //only button
    // page without the voice detection only button for index2.jsp
    public String good_page_noV2(String sp, String msg, int t, int c) {
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

    // page with google speech api for index3.jsp
    public String good_page_g2(String sp, String msg, int t, int c) {
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
                 + "<script type=\"text/javascript\" src=\"javascript/googleSpeechapi.js\"></script>"
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
                + "                            </tbody>\n"
                + "                        </table>\n"
                + "                    </td></tr>\n"
                + "            </tbody>\n"
                + "        </table>\n"
                + "    </body>");
        
        return ss;
    }
    
    //================Self traning with Advance version with distractor words=============================================
    // only voice detection
    public String good_page_s(String sp, String msg, int t, int c) {
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
                + "<script type=\"text/javascript\" src=\"javascript/SpeechRecognition.js\"></script>"
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
                + "                <tr><td>                        \n"
                + "                        <table border=\"0\" align=\"center\" style=\"width: 90%;text-align: center; font-size: xx-large\">\n"
                + "                            <tbody>\n"
                + "                                <tr style=\"height: 100px;\">\n"
                + "                                    <td style=\"background-color: lightpink\">Wait</td>\n"
                + "                                </tr>\n"
                + "                            </tbody>\n"
                + "                        </table>\n"
                + "                    </td></tr>\n"
                + "            </tbody>\n"
                + "        </table>\n"
                + "    </body>");
        return ss;
    }

    public String refresh_page2() {

        String ss = "";
        ss = ss.concat("<head>\n"
                + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n"
                + "        <title>Wait Page</title><meta http-equiv='refresh' content='0' />\n"
                + "    </head>\n"
                + "    <body>\n"
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
        log.info("xx,xx,xx,xx,xx,xx");
        java.util.Date date = new java.util.Date();
        log.info(new Timestamp(date.getTime()));
        log.info("trial,computer,startTime,stoptime,target,distract");
        start = System.currentTimeMillis();
    }

}
