package com.zbx.utilapi.util;

import com.alibaba.fastjson.*;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @创建人 zbx
 * @创建时间 2021/8/24
 * @描述
 */
@Slf4j
@Component
public class JsonUtil {

    private final ConfigRecord configRecord = ConfigRecord.getConfig();

    private int changeNum = 0;

    /**
     * 读写json
     * @param
     * @param
     * @return
     */
    public int JsonFormat(InputStream in, String fileName) {

        InputStreamReader fileReader = new InputStreamReader(in,StandardCharsets.UTF_8);
//        OutputStreamWriter fileWriter = null;
        JSONReader reader = null;
        JSONWriter writer = null;
        FileOutputStream fileWritertt = null;
        OutputStreamWriter writer1 = null;

        try {
            fileWritertt = new FileOutputStream(configRecord.getDownFileDir()+fileName);
            writer1 = new OutputStreamWriter(fileWritertt, StandardCharsets.UTF_8);
            reader = new JSONReader(fileReader);
            reader.config(Feature.SupportAutoType,true);
            reader.startObject();
            writer = new JSONWriter(writer1);
            writer.config(SerializerFeature.WriteMapNullValue,true);
            writer.startObject();
            while(reader.hasNext()) {
                String key = reader.readString();
                Object value = reader.readObject();
                if ("paramModel".equals(key)) {
                    List<Object> goodsList = goodsList(value);
                    writer.writeKey(key);
                    writer.writeObject(goodsList);
                } else {
                    writer.writeKey(key);
                    writer.writeObject(value);
                }

            }
            int i = changeNum;
            changeNum = 0;
            return i;
        } catch (IOException e) {
            log.error("下载地址配置错误: {}", configRecord.getDownFileDir()+fileName);
            return -1;
        } catch (JSONException e) {
            log.error("{}解析异常: {}",fileName,e.getMessage());
            return -1;
        }
        finally {
            if (reader != null) {
                reader.endObject();
                try {
                    fileReader.close();
                    reader.close();
                } catch (IOException e) {
                    log.warn("读取流关闭失败: {}", e.getMessage());
                }
            }
            if (writer != null) {
                writer.endObject();
                try {
                    writer.close();
                    fileWritertt.close();
                    writer1.close();
                } catch (IOException e) {
                    log.warn("写入流关闭失败: {}", e.getMessage());
                }
            }
        }
    }

    /**
     * 遍历商品列表
     * @param o
     * @return
     */
    public List<Object> goodsList(Object o) {
        List<Object> list = new ArrayList<>();
        JSONArray array = (JSONArray) o;
        for (Object goods : array) {
            JSONObject obj = (JSONObject) goods;
            Map<String, Object> map = new HashMap<>();
            for (Map.Entry<String, Object> entry : obj.entrySet()) {
                if ("subModels".equals(entry.getKey())) {
                    List<Object> list1 = goodsList(entry.getValue());
                    map.put(entry.getKey(),list1);
                }else {
                    map.put(entry.getKey(), entry.getValue());
                }
            }
            String key = (String) map.get("modelNumber");
            int res = isSplit(key);
            if (key != null && res > 0) {
                //修改变量列表
                Object parameters = map.get("parameters");
                List<Object> updap = updateVar(parameters, String.valueOf(res));
                Object ignoreParameters = map.get("ignoreParameters");
                List<Object> ignp = updateVar(ignoreParameters, String.valueOf(res));
                map.put("parameters",updap);
                map.put("ignoreParameters",ignp);
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 修改变量
     * @param o
     * @param value
     * @return
     */
    public List<Object> updateVar(Object o, String value) {
        List<Object> list = new ArrayList<>();
        JSONArray array = (JSONArray) o;
        for (Object goods : array) {
            JSONObject obj = (JSONObject) goods;
            Map<String, Object> map = new HashMap<>();
            for (Map.Entry<String, Object> entry : obj.entrySet()) {
                map.put(entry.getKey(), entry.getValue());
            }
            String key = (String) map.get("name");
            String val = (String) map.get("value");
//            System.out.println("name="+key+"    value="+val);
            if ("SBFX".equals(key)) {
                changeNum++;
                map.put("value", value);
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 判断当前产品是否需要替换
     * @param key 产品编码
     * @return 是否需要替换
     */
    private int isSplit(String key) {
        for (String s : configRecord.getParseTo5()) {
            if (s.equals(key)) {
                return 5;
            }
        }
        for (String s : configRecord.getParseTo6()) {
            if (s.equals(key)) {
                return 6;
            }
        }
        return -1;
    }
}
