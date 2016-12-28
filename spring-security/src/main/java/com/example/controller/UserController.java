package com.example.controller;

import com.example.entity.Role;
import com.example.entity.User;
import com.example.service.RoleService;
import com.example.service.UserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequestMapping("/user")
@Controller
@Log4j
public class UserController {

    @ModelAttribute("navSection")
    public String module() {
        return "user";
    }

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {

        binder.registerCustomEditor(Set.class, "roles", new CustomCollectionEditor(Set.class) {
            @Override
            protected Object convertElement(Object element) {
                Role role = new Role();
                role.setId(Long.parseLong((String) element));
                return role;
            }
        });

    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping
    public String userList(Model model, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                           @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        if (log.isDebugEnabled()) {
            log.debug("User list");
        }
        Pageable pageable = new PageRequest(page - 1, size);
        Page<User> pageObj = userService.getAllUsers(pageable);
        model.addAttribute("list", pageObj);
        model.addAttribute("page", page);
        return "/user/list";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public String getUserPage(@RequestParam(value = "id", required = false, defaultValue = "0") Long id, Model model) {
        if (log.isDebugEnabled()) {
            log.debug("Getting user create form");
        }
        model.addAttribute("roles", roleService.getRoles());
        if (id > 0) {
            model.addAttribute("user",userService.getUserById(id));
        }
        return "user/user";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String userSave(@ModelAttribute("user") User user) {
        if (log.isDebugEnabled()) {
            log.debug("User create");
        }
        userService.save(user);
        return "redirect:/user";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(@Param("id") Long id) {
        userService.delete(id);
        return "redirect:/user";
    }


}
