package com.houoy.www.gongxing.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 关于中的菜单目录
 * Created by andyzhao on 2018/4/19.
 */
@lombok.Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "menu_about")
public class AboutMenu extends BaseModel {
    @Column(name = "menu_name")
    private String menu_name;

    @Column(name = "menu_code")
    private String menu_code;

    @Column(name = "url")
    private String url;
    @Column(name = "has_update")
    private Boolean has_update;
    @Column(name = "newest_version_code")
    private Integer newestVersionCode;
    @Column(name = "newest_version_name")
    private String newestVersionName;
    @Column(name = "current_version_code")
    private Integer currentVersionCode;
    @Column(name = "current_version_name")
    private String currentVersionName;
    @Column(name = "downloading_version_code")
    private Integer downloadingVersionCode;
    @Column(name = "progress")
    private Integer progress;
    @Column(name = "progressSize")
    private Long progressSize;
    //安装包大小
    @Column(name = "size")
    private String size;
    //版本说明
    @Column(name = "comment")
    private String comment;
}
