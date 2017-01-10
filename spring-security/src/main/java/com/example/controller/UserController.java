package com.example.controller;

import com.example.entity.Role;
import com.example.entity.User;
import com.example.service.RoleService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/users")
@lombok.extern.slf4j.Slf4j
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
    @GetMapping
    public String userList(Model model, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                           @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        if (log.isDebugEnabled()) {
            log.debug("Getting user list.");
        }
        Pageable pageable = new PageRequest(page - 1, size);
        Page<User> pageObj = userService.getAllUsers(pageable);
        model.addAttribute("list", pageObj);
        model.addAttribute("page", page);
        return "users/list";
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/user")
    public String getUserPage(Model model) {
        if (log.isDebugEnabled()) {
            log.debug("Getting user create form.");
        }
        model.addAttribute("user",new User());
        model.addAttribute("roles", roleService.getRoles());
        return "users/user";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/user/{id}")
    public String getUserPage(@PathVariable(value = "id") Long id, Model model) {
        if (log.isDebugEnabled()) {
            log.debug("Getting user edit form by id " + id);
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
    @PostMapping("/user")
    public String userSave(@ModelAttribute @Valid User user, Model model, BindingResult bindingResult) {
        //get role
        model.addAttribute("roles", roleService.getRoles());

        if (log.isDebugEnabled()) {
            log.debug("Saving user " + user);
        }
        if (bindingResult.hasErrors()) {
            return "users/user";
        }
        try {
            userService.save(user);
        } catch (DataIntegrityViolationException e) {
            log.warn("Exception occurred when trying to save the user, assuming duplicate username", e);
            bindingResult.reject("error.user.global.duplicate");
            return "users/user";
        }
        return "redirect:/users";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        if (log.isDebugEnabled()) {
            log.debug("Deleting user by id " + id);
        }
        userService.delete(id);
        return "redirect:/users";
    }

}
