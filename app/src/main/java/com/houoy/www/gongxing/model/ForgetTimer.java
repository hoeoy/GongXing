package com.houoy.www.gongxing.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 忘记密码时候，获取验证码存储验证码时间,用来计时
 * Created by andyzhao on 2018/4/19.
 */
@lombok.Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "forget_timer")
public class ForgetTimer extends BaseModel {
    @Column(name = "last_time")
    private String lastTime;
}
