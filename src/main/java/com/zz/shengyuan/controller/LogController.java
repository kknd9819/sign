package com.zz.shengyuan.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Controller
@Slf4j
public class LogController {

    /**
     * 只能用网页查看当天的日志记录 以往的日志记录只能去服务器看
     * @return {@link String}
     */
    @GetMapping("/log")
    public String getLog(Model model) {
        String[] split = getCurrentLog().split("\n");
        Flux<String> lists = Flux.fromIterable(Arrays.asList(split));
        model.addAttribute("lists", lists);
        return "log";
    }

    private String getCurrentLog() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(new File("./sign.log")))) {
            String s;
            while ((s = br.readLine()) != null) {
                sb.append(s).append("\n");
            }
            br.close();
        } catch (IOException e1) {
            log.error("【日志页面】：网页输出日志异常", e1);
        }

        return sb.toString();
    }

}