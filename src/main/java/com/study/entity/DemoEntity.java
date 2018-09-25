package com.study.entity;

/**
 * @ClassName: DemoEntity
 * @Description TODO
 * @Author: zyd
 * @Date: 2018/9/25 14:01
 * @Version: 1.0
 */
public class DemoEntity {

    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DemoEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public DemoEntity() {
    }
}
