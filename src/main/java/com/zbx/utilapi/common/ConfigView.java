package com.zbx.utilapi.common;

import lombok.Data;

import java.util.Set;

/**
 * @创建人 zbx
 * @创建时间 2021/9/11
 * @描述
 */
@Data
public class ConfigView {

    private String downLoad;

    private Set<String> parseTo5;

    private Set<String> parseTo6;

}
