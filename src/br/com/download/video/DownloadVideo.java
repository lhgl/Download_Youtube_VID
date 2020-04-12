package br.com.download.video;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class DownloadVideo {

    public static String download_path = "D:\\Git\\Download_Youtube_VID";

    public static String fileName = "videos.txt";

    public static void main(String[] args) {

        getListaVideos().forEach(urlVideo -> {
            getdData(urlVideo, getParams());
        });

    }

    private static List<String> getListaVideos() {

        List<String> listaUrlVideos = new ArrayList<>();

        try {
            File file = new File(fileName);

            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
            while ((st = br.readLine()) != null) {
                listaUrlVideos.add(st);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return listaUrlVideos;
    }

    private static String getParams() {
        return "" +
                " -U" +
                " --write-info-json" +
                " --write-auto-sub" +
                " --write-sub" +
                " --sub-format vtt" +
                " --sub-lang pt" +
                " -o " + download_path + "\\videos\\%(id)s.%(ext)s" +
                " --recode-video mp4" +
                " --no-mtime" +
                " -f 135" +
                " --convert-subs srt";

    }

    private static void getdData(String url, String params) {

        String[] command =
                {
                        "cmd"
                };

        Process p;
        try {
            p = Runtime.getRuntime().exec(command);
            new Thread(new SyncBuffer(p.getErrorStream(), System.err)).start();
            new Thread(new SyncBuffer(p.getInputStream(), System.out)).start();
            PrintWriter stdin = new PrintWriter(p.getOutputStream());
            stdin.println("cd \"" + download_path + "\"");
            stdin.println("youtube-dl " + url + params);
            stdin.close();
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}	
