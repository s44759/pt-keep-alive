package cn.nkym.alive;

import cn.nkym.visit.PTVisit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Slf4j
public class PTAlive {


    public static void keepAlive(){
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
        int count = 1;
        String name;
        while (StringUtils.isNotBlank(properties.getProperty("pt.uri" + count))){
            name = properties.getProperty("pt.name" + count);
            if (StringUtils.isBlank(name)){
                name = "用户自定义PT站点" + count;
            }
            PTVisit.visit(properties.getProperty("pt.uri" + count), properties.getProperty("pt.cookie" + count), name);
            count++;
        }
        System.out.println("所有PT站点访问完成\r\n");
    }
}
