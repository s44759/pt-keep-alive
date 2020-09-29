package cn.nkym;

import cn.nkym.alive.PTAlive;
import cn.nkym.alive.PTAliveThread;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class PTApplication {

    public static void main(String[] args) {
        Properties properties = new Properties();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("user.properties");
        } catch (FileNotFoundException e) {
            log.error("获取user.properties出现异常", e);
            System.out.println("获取user.properties出现异常");
            System.exit(0);
        }
        try {
            properties.load(fileInputStream);
        } catch (IOException e) {
            log.error("载入user.properties出现异常", e);
            System.out.println("载入user.properties出现异常");
            System.exit(0);
        }
        if ("1".equals(properties.getProperty("multithreading"))){
            PTAliveThread.keepAlive();
        } else {
            PTAlive.keepAlive();
        }
    }
}
