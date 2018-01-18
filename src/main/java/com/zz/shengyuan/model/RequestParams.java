package com.zz.shengyuan.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class RequestParams implements Serializable{

    private static final long serialVersionUID = -3623764635121478068L;

    @Id
    @GeneratedValue
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyDate;
    private String mobile;
    private String nickName;
    private String password;
    private String token;
    private String version;
    private String versionCode;
    private String regidLogin;
    private String portraitPath;

}
