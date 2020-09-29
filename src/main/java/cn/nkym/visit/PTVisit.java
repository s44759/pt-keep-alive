package cn.nkym.visit;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

@Slf4j
public class PTVisit {

    public static void visit(String uri, String cookie, String name){

        if (StringUtils.isBlank(cookie)){
            System.out.println(name + "未配置cookie，打开系统默认浏览器进行");
            if (Desktop.isDesktopSupported()) {
                try {
                    // 创建一个URI实例
                    URI ur = URI.create(uri);
                    // 获取当前系统桌面扩展
                    Desktop dp = Desktop.getDesktop();
                    // 判断系统桌面是否支持要执行的功能
                    if (dp.isSupported(Desktop.Action.BROWSE)) {
                        // 获取系统默认浏览器打开链接
                        dp.browse(ur);
                    }

                } catch (Exception e) {
                    System.out.println("调用浏览器打开" + name + "失败");
                    log.error("调用浏览器打开" + name + "失败", e);
                }
            }
            return;
        }

        //需要访问的PT网址
        HttpPost httpPost = new HttpPost(uri);

        CloseableHttpClient httpClient = HttpClients.createDefault();

        //设置连接超时时间和数据交互超时时间
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(6000).setConnectTimeout(6000).build();
        httpPost.setConfig(requestConfig);

        //向请求中添加头信息
        httpPost.addHeader("Connection", "keep-alive");
        httpPost.addHeader("Cache-Control", "max-age=0");
        httpPost.addHeader("Upgrade-Insecure-Requests", "1");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.125 Safari/537.36");
        httpPost.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        httpPost.addHeader("Sec-Fetch-Site", "none");
        httpPost.addHeader(" Sec-Fetch-Mode", "navigate");
        httpPost.addHeader("Sec-Fetch-User", "?1");
        httpPost.addHeader("Sec-Fetch-Dest", "document");
        httpPost.addHeader("Accept-Encoding", "gzip, deflate, br");
        httpPost.addHeader("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8,ja;q=0.7,und;q=0.6");
        httpPost.addHeader("Cookie", cookie);
        CloseableHttpResponse response = null;
        int code = -1;
        try {
            response = httpClient.execute(httpPost);
            code = response.getStatusLine().getStatusCode();
            if (code == 302){
                Header header = response.getFirstHeader("location"); // 跳转的目标地址是在response的 HTTP-HEAD 中的，location的值
                String value = header.getValue();// 这就是跳转后的地址，再向这个地址发出新申请，以便得到跳转后的信息是啥。
                System.out.println("访问" + name + "出现异常[" + code + "]");
                System.out.println(value);
            } else {
                System.out.println("访问" + name + "\t" + code);
            }
        } catch (IOException e) {
            System.out.println("访问" + name + "出现异常[" + code + "]");
            log.error("访问" + name + "出现异常[" + code + "]", e);
            if (Desktop.isDesktopSupported()) {
                try {
                    // 创建一个URI实例
                    URI ur = URI.create(uri);
                    // 获取当前系统桌面扩展
                    Desktop dp = Desktop.getDesktop();
                    // 判断系统桌面是否支持要执行的功能
                    if (dp.isSupported(Desktop.Action.BROWSE)) {
                        // 获取系统默认浏览器打开链接
                        dp.browse(ur);
                    }

                } catch (Exception ex) {
                    System.out.println("调用浏览器打开" + name + "失败");
                    log.error("调用浏览器打开" + name + "失败", ex);
                }
            }
        }
    }
}
