package com.zz.shengyuan.util;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class Version implements Serializable{
    private static final long serialVersionUID = -1478879689730246406L;


    private String androidVersion;

    private String versionCode;

}
