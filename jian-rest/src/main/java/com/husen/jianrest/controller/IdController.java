package com.husen.jianrest.controller;

import com.husen.service.intf.IdService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Created by HuSen on 2018/10/16 13:29.
 */
@RestController
@Slf4j
@RequestMapping("/jian")
public class IdController {
    private final IdService idService;
    @Autowired
    public IdController(IdService idService) {
        this.idService = idService;
    }

    @GetMapping("/genId")
    public Mono<Long> genId() {
        return Mono.just(idService.genId());
    }


}
