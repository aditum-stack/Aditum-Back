package com.ten.aditum.back.pycontroller;

import com.alibaba.fastjson.JSON;
import com.ten.aditum.back.config.PythonConstants;
import com.ten.aditum.back.controller.BaseController;
import com.ten.aditum.back.model.AditumCode;
import com.ten.aditum.back.model.ResultModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Python聚类分析，DeviceCount设备按天访问热度聚类分析
 */
@Slf4j
@RestController
@RequestMapping(value = "/py/device/access/")
public class PyDeviceCountController extends BaseController {

    private static final String[] ARGUMENTS = new String[]{
            PythonConstants.PYTHON_PATH,
            PythonConstants.PTTHON_PROGRAM_BATH_PATH + "DeviceCountClusteringModel.py"};

    /**
     * 获取设备热度聚类数据图
     * TODO 根据communityId获取
     *
     * @return base64 img
     */
    @RequestMapping(value = "/count/yesterday", method = RequestMethod.GET)
    public ResultModel getCountByDayClustering(String communityId) {
        log.info("PyDeviceCount yesterday [GET] clustering");
        if (communityId == null) {
//            return new ResultModel(AditumCode.ERROR, "communityId不能为空");
        }

        String key = "PyDeviceCount" + communityId;
        String originValue = jedis.get(key);
        if (originValue == null) {
            String base64Img = computeBase64(communityId);
            if (base64Img == null) {
                log.warn("PyDeviceCount [GET] [INIT] FAILURE : {} -> {}", communityId);
                return new ResultModel(AditumCode.ERROR);
            } else {
                ResultModel cache = new ResultModel(AditumCode.OK, base64Img);
                String value = JSON.toJSONString(cache);
                jedis.setex(key, VALID_TIME, value);
                log.info("PyDeviceCount [GET] [INIT] SUCCESS {}", cache);
                return cache;
            }
        } else {
            ResultModel cache = JSON.parseObject(originValue, ResultModel.class);
            log.info("PyDeviceCount [GET] [CACHE] SUCCESS {}", cache);
            return cache;
        }
    }

    /**
     * 调用Python脚本执行并返回base64图片字符串
     */
    private String computeBase64(String communityId) {
        log.info("PyDeviceCount 聚类算法开始计算...预计耗时10s");
        long start = System.currentTimeMillis();

        String base64 = null;
        try {
            Process process = Runtime.getRuntime().exec(ARGUMENTS);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            // python只返回一行base64，故只取第一行
            base64 = in.readLine();
            base64 = new String(base64.getBytes(), StandardCharsets.UTF_8);
            log.info("PyDeviceCount.py return base64 : {}", base64);
            in.close();
            int re = process.waitFor();
            if (re != 0) {
                log.warn("PyDeviceCount.py throws exception");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (base64 == null) {
            log.warn("PyDeviceCount base64 image is null");
            throw new RuntimeException("PyDeviceCount base64 image is null");
        }

        String[] s = base64.split("'");
        String base64Img = PythonConstants.BASE64_PREFIX + s[1];

        long end = System.currentTimeMillis();
        long durante = end - start;
        log.info("PyDeviceCount 聚类算法计算完成...耗时{}s", durante / 1000);

        return base64Img;
    }

}
