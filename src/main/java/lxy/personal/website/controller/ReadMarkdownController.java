package lxy.personal.website.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author lxy
 * @Date 2021/11/1 10:55
 * @Description
 */

/*
* https://gitee.com/was666/as-editor/raw/master/README.md
* */

@RestController
@Slf4j
class ReadMarkdownController {

    @PostMapping("/lxy/md")
    public String getIOContent(String link) throws IOException {
        URL realUrl = new URL(link);
        HttpURLConnection httpurl = (HttpURLConnection) realUrl.openConnection();
        httpurl.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(httpurl.getInputStream()));
        String result = "";
        String line;
        while((line = br.readLine()) != null) {
            result += line+"\n";
        }
        return result;
    }

}
