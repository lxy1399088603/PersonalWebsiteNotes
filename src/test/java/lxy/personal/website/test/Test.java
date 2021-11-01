package lxy.personal.website.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author lxy
 * @Date 2021/11/1 13:17
 * @Description
 */
public class Test {

    @org.junit.Test
    public void test1() throws IOException {
        String url = "https://gitee.com/was666/as-editor/raw/master/README.md";
        URL realUrl = new URL(url);
        HttpURLConnection httpurl = (HttpURLConnection) realUrl.openConnection();
//        httpurl.setRequestProperty("accept", "*/*");
//        httpurl.setRequestProperty("Connection", "keep-alive");
//        httpurl.setRequestProperty("Content-Encoding","gzip");
//
//        httpurl.setRequestProperty("Content-Type","application/javascript;charset=UTF-8");
//        httpurl.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        httpurl.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(httpurl.getInputStream()));
        String result = "";
        String line = null;
        while((line = in.readLine()) != null) {
            result += line;
        }
        System.out.println(result);
    }
}
