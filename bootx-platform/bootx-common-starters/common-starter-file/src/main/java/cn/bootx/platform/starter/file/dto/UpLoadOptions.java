package cn.bootx.platform.starter.file.dto;

import lombok.Data;

import java.util.TreeMap;

@Data
public class UpLoadOptions {
    private String tmpSecretId;
    private String tmpSecretKey;
    private String sessionToken;

    private Long expiredTime;

    private TreeMap<String,String> formData;

    private String uploadUrl;

    private String key;

}
