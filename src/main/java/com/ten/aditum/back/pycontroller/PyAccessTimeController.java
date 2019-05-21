package com.ten.aditum.back.pycontroller;

import com.ten.aditum.back.model.AditumCode;
import com.ten.aditum.back.model.ResultModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestController
@RequestMapping(value = "/py/access/")
public class PyAccessTimeController {

    /**
     * 获取用户时间行为偏好聚类数据图
     *
     * @return base64 img
     */
    @RequestMapping(value = "/time", method = RequestMethod.GET)
    public ResultModel getTimeClustering() {
        log.info("PythonAccessTime [GET] clustering");

        String base64 = null;
        String[] arguments = new String[]{
                PythonConstants.PYTHON_PATH,
                "D:\\Users\\shihaowang\\Desktop\\Aditum-Personas\\com.ten.aditum\\personas\\AccessTimeClusteringModel.py"};
        try {
            Process process = Runtime.getRuntime().exec(arguments);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            // python只返回一行base64，故只取第一行
            base64 = in.readLine();
            base64 = new String(base64.getBytes(), StandardCharsets.UTF_8);
            log.info("AccessTimeClusteringModel.py return base64 : {}", base64);
            in.close();
            int re = process.waitFor();
            if (re != 0) {
                log.warn("AccessTimeClusteringModel.py throws exception");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (base64 == null) {
            log.warn("PythonAccessTime base64 image is null");
            return new ResultModel(AditumCode.ERROR);
        }

        String[] s = base64.split("'");
        String base64Img = PythonConstants.BASE64_PREFIX + s[1];

        log.info("PythonAccessTime [GET] clustering SUCCESS {}", base64Img);
        return new ResultModel(AditumCode.OK, base64Img);
    }

}
