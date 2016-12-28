package com.example.controller;

import com.example.entity.Role;
import com.example.entity.User;
import com.example.service.RoleService;
import com.example.service.UserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;

@RequestMapping("/user")
@Controller
@Log4j
public class UserController {

    @ModelAttribute("page")
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
    public ModelAndView userList(Model model) {
        if (log.isDebugEnabled()) {
            log.debug("User list");
        }
        return new ModelAndView("/user/list", "list", userService.getAllUsers());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView getUserCreatePage() {
        if (log.isDebugEnabled()) {
            log.debug("Getting user create form");
        }
        return new ModelAndView("user/user", "roles", roleService.getRoles());
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String userCreate(@ModelAttribute("user") User user) {
        if (log.isDebugEnabled()) {
            log.debug("User create");
        }
        userService.create(user);
        return "redirect:/user";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(@Param("id") Long id) {
        userService.delete(id);
        return "redirect:/user";
    }


}
