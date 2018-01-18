package com.zz.shengyuan.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.zz.shengyuan.model.RequestParams;

public interface RequestParamsRepository extends JpaRepository<RequestParams,Long> {

    @Modifying
    @Transactional
    @Query("update RequestParams set version = ?1,versionCode = ?2 where id = ?3")
    void updateVersionAndVersionCode(String version, String versionCode, Long id);
    
}
