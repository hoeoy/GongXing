package com.houoy.www.gongxing.model;

import org.xutils.db.annotation.Column;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by andyzhao on 3/4/2018.
 */
@Data
@NoArgsConstructor
public class BaseModel implements Serializable {
    //公共属性
    @Column(name = "id", isId = true)
    private Integer id;
    @Column(name = "ts")
    private String ts;
}
