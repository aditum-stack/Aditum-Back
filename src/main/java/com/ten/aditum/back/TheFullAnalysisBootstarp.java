package com.ten.aditum.back;

import com.ten.aditum.back.personas.label.AddressAnalysor;
import com.ten.aditum.back.personas.label.PhoneAnalysor;
import com.ten.aditum.back.personas.label.TripsAnalysor;
import com.ten.aditum.back.personas.model.*;
import com.ten.aditum.back.statistic.base.CommunityAnalyzer;
import com.ten.aditum.back.statistic.device.DeviceCountAnalyzer;
import com.ten.aditum.back.statistic.device.DeviceHeatAnalyzer;
import com.ten.aditum.back.statistic.device.DeviceLogAnalyzer;
import com.ten.aditum.back.statistic.device.DeviceTotalAnalyzer;
import com.ten.aditum.back.statistic.person.AccessAddressAnalyzer;
import com.ten.aditum.back.statistic.person.AccessIntervalAnalyzer;
import com.ten.aditum.back.statistic.person.AccessTimeAnalyzer;
import com.ten.aditum.back.util.TimeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;


/**
 * 启动一次全量分析
 */
@Slf4j
@Component
public class TheFullAnalysisBootstarp {

    private final CommunityAnalyzer communityAnalyzer;
    private final DeviceCountAnalyzer deviceCountAnalyzer;
    private final DeviceHeatAnalyzer deviceHeatAnalyzer;
    private final DeviceLogAnalyzer deviceLogAnalyzer;
    private final DeviceTotalAnalyzer deviceTotalAnalyzer;
    private final AccessAddressAnalyzer accessAddressAnalyzer;
    private final AccessIntervalAnalyzer accessIntervalAnalyzer;
    private final AccessTimeAnalyzer accessTimeAnalyzer;
    private final AddressAnalysor addressAnalysor;
    private final PhoneAnalysor phoneAnalysor;
    private final TripsAnalysor tripsAnalysor;
    private final AnalysorFive analysorFive;
    private final AnalysorFour analysorFour;
    private final AnalysorMix analysorMix;
    private final AnalysorOne analysorOne;
    private final AnalysorSeven analysorSeven;
    private final AnalysorSix analysorSix;
    private final AnalysorThree analysorThree;
    private final AnalysorTwo analysorTwo;
    private final AnalysorZero analysorZero;

    @Autowired
    public TheFullAnalysisBootstarp(DeviceCountAnalyzer deviceCountAnalyzer, CommunityAnalyzer communityAnalyzer, DeviceHeatAnalyzer deviceHeatAnalyzer, AnalysorSix analysorSix, AnalysorFour analysorFour, AnalysorThree analysorThree, AnalysorTwo analysorTwo, DeviceLogAnalyzer deviceLogAnalyzer, DeviceTotalAnalyzer deviceTotalAnalyzer, AnalysorSeven analysorSeven, AccessAddressAnalyzer accessAddressAnalyzer, AccessIntervalAnalyzer accessIntervalAnalyzer, AccessTimeAnalyzer accessTimeAnalyzer, AnalysorMix analysorMix, AnalysorZero analysorZero, AddressAnalysor addressAnalysor, PhoneAnalysor phoneAnalysor, AnalysorOne analysorOne, TripsAnalysor tripsAnalysor, AnalysorFive analysorFive) {
        this.deviceCountAnalyzer = deviceCountAnalyzer;
        this.communityAnalyzer = communityAnalyzer;
        this.deviceHeatAnalyzer = deviceHeatAnalyzer;
        this.analysorSix = analysorSix;
        this.analysorFour = analysorFour;
        this.analysorThree = analysorThree;
        this.analysorTwo = analysorTwo;
        this.deviceLogAnalyzer = deviceLogAnalyzer;
        this.deviceTotalAnalyzer = deviceTotalAnalyzer;
        this.analysorSeven = analysorSeven;
        this.accessAddressAnalyzer = accessAddressAnalyzer;
        this.accessIntervalAnalyzer = accessIntervalAnalyzer;
        this.accessTimeAnalyzer = accessTimeAnalyzer;
        this.analysorMix = analysorMix;
        this.analysorZero = analysorZero;
        this.addressAnalysor = addressAnalysor;
        this.phoneAnalysor = phoneAnalysor;
        this.analysorOne = analysorOne;
        this.tripsAnalysor = tripsAnalysor;
        this.analysorFive = analysorFive;
    }

    /**
     * 启动全量分析
     */
    public void runFullAnalysis() {
        log.info("启动全量分析...");
        long start = System.currentTimeMillis();

        CompletableFuture<Void> voidCompletableFuture1 = CompletableFuture.runAsync(this.deviceCountAnalyzer::analysis);
        CompletableFuture<Void> voidCompletableFuture2 = CompletableFuture.runAsync(this.communityAnalyzer::analysis);
        CompletableFuture<Void> voidCompletableFuture3 = CompletableFuture.runAsync(this.deviceHeatAnalyzer::analysis);
        CompletableFuture<Void> voidCompletableFuture4 = CompletableFuture.runAsync(this.accessTimeAnalyzer::analysis);
        CompletableFuture<Void> voidCompletableFuture5 = CompletableFuture.runAsync(this.accessAddressAnalyzer::analysis);
        CompletableFuture<Void> voidCompletableFuture6 = CompletableFuture.runAsync(this.accessIntervalAnalyzer::analysis);
        CompletableFuture<Void> voidCompletableFuture7 = CompletableFuture.runAsync(this.analysorTwo::analysis);
        CompletableFuture<Void> voidCompletableFuture8 = CompletableFuture.runAsync(this.deviceLogAnalyzer::analysis);
        CompletableFuture<Void> voidCompletableFuture9 = CompletableFuture.runAsync(this.deviceTotalAnalyzer::analysis);
        CompletableFuture<Void> voidCompletableFuture10= CompletableFuture.runAsync(this.analysorSeven::analysis);
        CompletableFuture<Void> voidCompletableFuture11= CompletableFuture.runAsync(this.analysorFour::analysis);
        CompletableFuture<Void> voidCompletableFuture12= CompletableFuture.runAsync(this.analysorThree::analysis);
        CompletableFuture<Void> voidCompletableFuture13= CompletableFuture.runAsync(this.analysorSix::analysis);
        CompletableFuture<Void> voidCompletableFuture14= CompletableFuture.runAsync(this.analysorMix::analysis);
        CompletableFuture<Void> voidCompletableFuture15= CompletableFuture.runAsync(this.analysorZero::analysis);
        CompletableFuture<Void> voidCompletableFuture16= CompletableFuture.runAsync(this.addressAnalysor::analysis);
        CompletableFuture<Void> voidCompletableFuture17= CompletableFuture.runAsync(this.phoneAnalysor::analysis);
        CompletableFuture<Void> voidCompletableFuture18= CompletableFuture.runAsync(this.analysorOne::analysis);
        CompletableFuture<Void> voidCompletableFuture19= CompletableFuture.runAsync(this.tripsAnalysor::analysis);
        CompletableFuture<Void> voidCompletableFuture20= CompletableFuture.runAsync(this.analysorFive::analysis);

        CompletableFuture
                .allOf(voidCompletableFuture1,
                        voidCompletableFuture2,
                        voidCompletableFuture3,
                        voidCompletableFuture4,
                        voidCompletableFuture5,
                        voidCompletableFuture6,
                        voidCompletableFuture7,
                        voidCompletableFuture8,
                        voidCompletableFuture9,
                        voidCompletableFuture10,
                        voidCompletableFuture11,
                        voidCompletableFuture12,
                        voidCompletableFuture13,
                        voidCompletableFuture14,
                        voidCompletableFuture15,
                        voidCompletableFuture16,
                        voidCompletableFuture17,
                        voidCompletableFuture18,
                        voidCompletableFuture19,
                        voidCompletableFuture20)
                .join();

        long end = System.currentTimeMillis();
        log.info("全量分析结束...");

        long duration = end - start;
        String durationTime = TimeGenerator.getTimeFromSec(duration / 1000);
        log.info("全量分析共计花费{}", durationTime);
    }
}
