package com.reptiles;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Application {

    public static void main(String[] args) throws IOException {
        Document document = Jsoup.connect("https://www.58pic.com/").get();
        Element sliderList = document.getElementsByAttributeValue("class","slider-list").get(0);
        Elements aList = sliderList.getElementsByTag("a");
        for (Element a : aList) {
            String imgUrl = a.attr("data-url");
            if (imgUrl != null && imgUrl.length() > 0) {
                    String imgName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
                    doGet("http:" + imgUrl, imgName);
            }
        }
    }

    public static String doGet(String url, String imgName) {
        System.out.println(imgName + " 正在爬取");
        try {
            HttpClient client = new DefaultHttpClient();
            //发送get请求
            HttpGet request = new HttpGet(url);
            request.setHeader("Referer", "https://www.58pic.com/");
            request.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
            HttpResponse response = client.execute(request);
            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                InputStream inputStream = response.getEntity().getContent();
                FileUtils.copyToFile(inputStream,new File("D:\\java\\" + imgName));
                System.out.println(imgName + " 爬取结束");
                System.out.println("---------------------");
                return null;
            } else {
                System.out.println(response.getStatusLine().getStatusCode());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
