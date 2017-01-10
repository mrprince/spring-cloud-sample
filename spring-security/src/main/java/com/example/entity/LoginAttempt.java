package com.example.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "login_attempts")
public class LoginAttempt {
    @Id
    @GeneratedValue
    private int id;
    private String userName;
    private int attempts;
    private Date lastModified;
}
