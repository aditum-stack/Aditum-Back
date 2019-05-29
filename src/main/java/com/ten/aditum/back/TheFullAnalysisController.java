package com.ten.aditum.back;

import com.ten.aditum.back.model.AditumCode;
import com.ten.aditum.back.model.ResultModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
@RequestMapping(value = "/full")
public class TheFullAnalysisController {

    private final TheFullAnalysisBootstarp fullAnalysisBootstarp;

    @Autowired
    public TheFullAnalysisController(TheFullAnalysisBootstarp fullAnalysisBootstarp) {
        this.fullAnalysisBootstarp = fullAnalysisBootstarp;
    }

    @RequestMapping(value = "run", method = RequestMethod.GET)
    public ResultModel fullRequest() {
        CompletableFuture.runAsync(fullAnalysisBootstarp::runFullAnalysis);
        return new ResultModel(AditumCode.OK, "全量分析已启动...");
    }
}
