package com.houoy.www.gongxing.dao;

import com.houoy.www.gongxing.model.BaseModel;
import com.houoy.www.gongxing.util.StringUtil;

import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.lang.reflect.ParameterizedType;
import java.util.List;

//基础dao
public class BaseDao<T extends BaseDao, D extends BaseModel> {
    public Boolean add(D model) throws DbException {
        //User user = new User("Kevingo","caolbmail@gmail.com","13299999999",new Date());
        //db.save(user);//保存成功之后【不会】对user的主键进行赋值绑定
        //db.saveOrUpdate(user);//保存成功之后【会】对user的主键进行赋值绑定
        //db.saveBindingId(user);//保存成功之后【会】对user的主键进行赋值绑定,并返回保存是否成功
        Boolean result = DBHelper.db.saveBindingId(model);
        return result;
    }

    public void update(D model) throws DbException {
        DBHelper.db.saveOrUpdate(model);
    }

    public List<D> findPage(int start, int limit, String orderBy) throws DbException {
        //List<User> users = DBHelper.db.findAll(User.class);
        //showDbMessage("【dbFind#findAll】第一个对象:"+users.get(0).toString());

        //User user = DBHelper.db.findById(User.class, 1);
        //showDbMessage("【dbFind#findById】第一个对象:" + user.toString());

        //long count = DBHelper.db.selector(User.class).where("name","like","%kevin%").and("email","=","caolbmail@gmail.com").count();//返回复合条件的记录数
        //showDbMessage("【dbFind#selector】复合条件数目:" + count);

        if (StringUtil.isEmpty(orderBy)) {
            orderBy = "ts";
        }

        Class<D> modelClass = (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        List<D> models = DBHelper.db.selector(modelClass)
//                .where("name","like","%kevin%")
//                .and("email", "=", "caolbmail@gmail.com")
                .orderBy(orderBy, true)
                .limit(limit) //只查询几条记录
                .offset(start) //偏移两个,从第三个记录开始返回,limit配合offset达到sqlite的limit m,n的查询
                .findAll();
        return models;
    }

    public D findFirst() throws DbException {
        Class<D> modelClass = (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        List<D> models = DBHelper.db.selector(modelClass)
                .limit(1) //只查询几条记录
                .offset(0) //偏移两个,从第三个记录开始返回,limit配合offset达到sqlite的limit m,n的查询
                .findAll();
        if (models == null || models.size() < 1) {
            return null;
        }

        return models.get(0);
    }

    public List<D> findAll() throws DbException {
        Class<D> modelClass = (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        List<D> models = DBHelper.db.selector(modelClass).findAll();
        return models;
    }

    public Integer delete(int id) throws DbException {
        WhereBuilder whereBuilder = WhereBuilder.b();
        whereBuilder.and("id", "=", id);
        Class<D> modelClass = (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        int result = DBHelper.db.delete(modelClass, whereBuilder);
        return result;
    }

    public void clear() throws DbException {
        Class<D> modelClass = (Class<D>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        DBHelper.db.delete(modelClass);
    }
}
