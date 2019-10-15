package com.wudi.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Dillon Wu
 * @Title: Order
 * @Description: 测试实体
 * @date 2019/10/14 13:20
 */
@Data
public class Order implements Serializable {

    private String id;

    private String name;

    private String content;

    public Order(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Order(String id, String name, String content){
        this.id = id;
        this.name = name;
        this.content = content;
    }
}
