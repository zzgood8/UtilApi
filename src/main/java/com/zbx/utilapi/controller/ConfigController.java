package com.zbx.utilapi.controller;

import com.zbx.utilapi.common.ConfigView;
import com.zbx.utilapi.common.Result;
import com.zbx.utilapi.util.ConfigRecord;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @创建人 zbx
 * @创建时间 2021/9/10
 * @描述
 */
@CrossOrigin
@RequestMapping("/config")
@RestController
public class ConfigController {


    private final ConfigRecord configRecord;

    public ConfigController() {
        this.configRecord = ConfigRecord.getConfig();
    }

    @GetMapping("/ls")
    public Result<ConfigView> getConfig() {
        ConfigView configView = new ConfigView();
        configView.setParseTo5(configRecord.getParseTo5());
        configView.setParseTo6(configRecord.getParseTo6());
        configView.setDownLoad(configRecord.getDownFileDir());
        return Result.success(configView);
    }

    @PostMapping("update")
    public Result<String> updateDownLoad(@RequestBody ConfigView config) {
        String downLoad = config.getDownLoad();
        Set<String> parseTo5 = config.getParseTo5();
        Set<String> parseTo6 = config.getParseTo6();
//        System.out.println(downLoad);
        if (downLoad != null) {
            String pattern = "^((./)|([A-Z]:/))(\\w+/)+";
            if (downLoad.matches(pattern)) {
                configRecord.setDownFileDir(downLoad);
            } else {
                return Result.failed("下载路径配置错误");
            }
        }

        if (parseTo5 != null) {
            configRecord.update("5",parseTo5);
        }

        if (parseTo6 != null) {
            configRecord.update("6",parseTo6);
        }
        return Result.success();
    }
}
