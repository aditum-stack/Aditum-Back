package com.ten.aditum.back.pycontroller;

import com.ten.aditum.back.model.AditumCode;
import com.ten.aditum.back.model.ResultModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
        String line = null;
        String[] arguments = new String[]{"python", "D:\\Users\\shihaowang\\Desktop\\Aditum-Personas\\com.ten.aditum\\personas\\strategy\\access\\AccessTimeClusteringModel.py"};
        try {
            Process process = Runtime.getRuntime().exec(arguments);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            int re = process.waitFor();
            System.out.println(re);
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.debug("PythonAccessTime [GET] clustering SUCCESS {}", line);
        return new ResultModel(AditumCode.OK, line);
    }

}
