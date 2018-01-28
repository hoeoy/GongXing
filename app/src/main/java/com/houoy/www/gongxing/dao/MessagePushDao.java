package com.houoy.www.gongxing.dao;

import com.houoy.www.gongxing.model.MessagePush;

import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import java.util.List;

public class MessagePushDao {
    private static MessagePushDao messagePushDao = null;

    private MessagePushDao() {
    }

    public static MessagePushDao getInstant() {
        if (messagePushDao == null) {
            messagePushDao = new MessagePushDao();
        }
        return messagePushDao;
    }


    public Boolean addMessagePush(MessagePush messagePush) throws DbException {
        //User user = new User("Kevingo","caolbmail@gmail.com","13299999999",new Date());
        //db.save(user);//保存成功之后【不会】对user的主键进行赋值绑定
        //db.saveOrUpdate(user);//保存成功之后【会】对user的主键进行赋值绑定
        //db.saveBindingId(user);//保存成功之后【会】对user的主键进行赋值绑定,并返回保存是否成功
        Boolean result = DBHelper.db.saveBindingId(messagePush);
        return result;
    }

    public List<MessagePush> findMessagePush(int start, int limit) throws DbException {
        //List<User> users = DBHelper.db.findAll(User.class);
        //showDbMessage("【dbFind#findAll】第一个对象:"+users.get(0).toString());

        //User user = DBHelper.db.findById(User.class, 1);
        //showDbMessage("【dbFind#findById】第一个对象:" + user.toString());

        //long count = DBHelper.db.selector(User.class).where("name","like","%kevin%").and("email","=","caolbmail@gmail.com").count();//返回复合条件的记录数
        //showDbMessage("【dbFind#selector】复合条件数目:" + count);

        List<MessagePush> messages = DBHelper.db.selector(MessagePush.class)
//                .where("name","like","%kevin%")
//                .and("email", "=", "caolbmail@gmail.com")
                .orderBy("time", true)
                .limit(limit) //只查询几条记录
                .offset(start) //偏移两个,从第三个记录开始返回,limit配合offset达到sqlite的limit m,n的查询
                .findAll();
        return messages;
    }

    public Integer deleteMessagePush(int id) throws DbException {
        //DBHelper.db.delete(users.get(0)); //删除第一个对象
        //DBHelper.db.delete(User.class);//删除表中所有的User对象【慎用】
        //DBHelper.db.delete(users); //删除users对象集合
        //users =  DBHelper.db.findAll(User.class);
        // showDbMessage("【dbDelete#delete】数据库中还有user数目:" + users.size());

        WhereBuilder whereBuilder = WhereBuilder.b();
        whereBuilder.and("id", "=", id);
//        whereBuilder.and("id", ">", "5").or("id", "=", "1").expr(" and mobile > '2015-12-29 00:00:01' ");
        int result = DBHelper.db.delete(MessagePush.class, whereBuilder);
        return result;
    }

    public void clearMessagePush() throws DbException {
        DBHelper.db.delete(MessagePush.class);
    }
}
