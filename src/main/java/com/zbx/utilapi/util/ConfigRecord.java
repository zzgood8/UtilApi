package com.zbx.utilapi.util;

import jdk.nashorn.internal.runtime.regexp.joni.Config;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;

/**
 * @创建人 zbx
 * @创建时间 2021/9/10
 * @描述
 */
@Slf4j
public class ConfigRecord {

    private final static ConfigRecord configRecord = new ConfigRecord();

    private final File configFile = new File("./config/备份配置.txt");

    private final File configDir = new File("./config");

    private String downFileDir = "./down/";

    private Set<String> parseTo6;

    private Set<String> parseTo5;

    private ConfigRecord() {
        createDir();
        init();
    }

    public static ConfigRecord getConfig() {
        return configRecord;
    }

    /**
     *
     * @param key
     * @param value
     */
    private void addConfig(String key, String value) {

        if (key.equals("5")) {
            parseTo5.add(value);
        }
        if (key.equals("6")) {
            parseTo6.add(value);
        }
    }

    public void setDownFileDir(String downFileDir) {
        this.downFileDir = downFileDir;
    }

    public String getDownFileDir() {
        return downFileDir;
    }

    /**
     * 向配置文件添加记录
     * @param
     * @param
     */
    private void saveConfig() {
        try {
            FileWriter writer = new FileWriter(configFile);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            for (String s : parseTo5) {
                String str = "5 " + s;
                bufferedWriter.write(str);
                bufferedWriter.newLine();

            }
            for (String s : parseTo6) {
                String str = "6 " + s;
                bufferedWriter.write(str);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void update(String key, Set<String> value) {
        if (key.equals("5")) {
            parseTo5.clear();
        }
        if (key.equals("6")) {
            parseTo6.clear();
        }
        for (String s : value) {
            addConfig(key, s);
        }
        saveConfig();
    }

    public Set<String> getParseTo6() {
        return parseTo6;
    }

    public Set<String> getParseTo5() {
        return parseTo5;
    }

    /**
     * 创建配置文件夹
     */
    private void createDir() {
        if (configDir.exists()) {
            if (!configDir.isDirectory()) {
                if (!configDir.delete() || !configDir.mkdir()) {
                    log.error("创建配置文件夹错误!请手动创建文件夹: {}","./config");
                    System.exit(0);
                }
            }
        } else {
            if (!configDir.mkdir()) {
                log.error("创建配置文件夹错误!请手动创建文件夹: {}","./config");
                System.exit(0);
            }
        }
    }

    /**
     * 读取默认配置文件
     */
    private void init() {
        parseTo5 = Collections.synchronizedSet(new HashSet<>());
        parseTo6 = Collections.synchronizedSet(new HashSet<>());
        try {
            FileReader reader = new FileReader(configFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lines = line.trim().split(" ");
                if (lines.length == 2){
                    String key = lines[0];
                    String value = lines[1];
//                    System.out.println("save :" + key +"->" + value);
                    addConfig(key, value);
                }
            }
        } catch (FileNotFoundException e) {
            try {
                if (!configFile.createNewFile()) {
                    log.error("创建配置文件错误!请手动创建文件: {}","./config/备份配置.txt");
                    System.exit(0);
                }
            } catch (IOException ioException) {
                log.error("创建配置文件错误!请手动创建文件: {}","./config/备份配置.txt");
                System.exit(0);
            }
        } catch (IOException e) {
            log.error("读取配置文件出错: {}",e.getMessage());
            System.exit(0);
        }
    }
}
