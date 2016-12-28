package com.example.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j
@RequestMapping(value = "/news")
public class NewsController {

    @ModelAttribute("page")
    public String module() {
        return "news";
    }

    @PreAuthorize("hasAuthority('NEWS_LIST_GET')")
    @RequestMapping
    public String demoList(Model model) {
        if (log.isDebugEnabled()) {
            log.debug("news list");
        }
        return "news/list";
    }

}
