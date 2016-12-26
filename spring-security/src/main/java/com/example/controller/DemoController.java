package com.example.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j
@RequestMapping(value = "/demo")
public class DemoController {

    @PreAuthorize("hasAuthority('DEMO_LIST_GET')")
    @RequestMapping
    public String demoList(Model model) {
        if (log.isDebugEnabled()) {
            log.debug("demo list");
        }
        return "list";
    }

}
