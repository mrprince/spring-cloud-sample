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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@Log4j
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @ModelAttribute("navSection")
    public String module() {
        return "user";
    }


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
        return "users/list";
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String getUserPage(Model model) {
        if (log.isDebugEnabled()) {
            log.debug("Getting user create form");
        }
        model.addAttribute("roles", roleService.getRoles());
        return "users/user";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public String getUserPage(@PathVariable(value = "id") Long id, Model model) {
        if (log.isDebugEnabled()) {
            log.debug("Getting user create form");
        }
        User user = userService.getUserById(id);
        if (user != null) {
            List<Long> selectedRoleIdList = user.getRoles().parallelStream().map(Role::getId).collect(Collectors.toList());
            model.addAttribute("roles", roleService.getRoles());
            model.addAttribute("user", user);
            model.addAttribute("selectedRoleIdList", selectedRoleIdList);
        }
        return "users/user";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public String userSave(@ModelAttribute("user") User user) {
        if (log.isDebugEnabled()) {
            log.debug("User create");
        }
        userService.save(user);
        return "redirect:/users";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/users";
    }

}
