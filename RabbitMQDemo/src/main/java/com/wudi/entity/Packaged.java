package com.wudi.entity;

import lombok.Data;


/**
 * @author Dillon Wu
 * @Title: Packaged
 * @Description: 测试实体
 * @date 2019/10/14 13:22
 */
@Data
public class Packaged {
    private String id;

    private String name;

    private String description;

    public Packaged(){}

    public Packaged(String id, String name, String description){
        this.id=id;
        this.name=name;
        this.description=description;
    }
}
