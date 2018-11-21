package com.ten.aditum.back.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 基于UUID 生成分布式全局唯一ID
 */
@Component
public class UidGenerator {

    public String generateUid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
