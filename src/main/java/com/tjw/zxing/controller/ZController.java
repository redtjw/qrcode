package com.tjw.zxing.controller;

import com.tjw.zxing.service.ZService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author wb-tjw362524
 * @Date 17:28 2018/4/4
 */
@RestController
@RequestMapping("/tjw")
public class ZController {
    @Autowired
    private ZService zService;
    @Value("${qrcode.filepath}")
    private String path;
    @RequestMapping(value = "/test")
    public Map test1(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssS");
        String prefix = simpleDateFormat.format(new Date());
        String imagePath = zService.encode("userId:51111111111",300,300,path,prefix+".png");
        File iamge  = new File(imagePath);
        File log = new File("D://qrcodeimage/log.jpg");
        String logPath = zService.mixLog(iamge,log,null,null,path,prefix+"_log.jpg");
        Map map = new HashMap<>(4);
        map.put("image",imagePath);
        map.put("log",logPath);
        return map;
    }
}
