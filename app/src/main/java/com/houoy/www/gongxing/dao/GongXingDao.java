package com.houoy.www.gongxing.dao;

import com.houoy.www.gongxing.model.ClientInfo;
import com.houoy.www.gongxing.model.MessagePush;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

public class GongXingDao {
    //    private DBHelper dbHelper;
    //    public GongXingDao(Context context) {
//        dbHelper = new DBHelper(context);
//    }
    public DbManager db;

    private static GongXingDao gongXingDao = null;

    private GongXingDao() {
    }

    public static GongXingDao getInstant() {
        if (gongXingDao == null) {
            gongXingDao = new GongXingDao();
        }
        return gongXingDao;
    }


    public void initDb() {
        //本地数据的初始化
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("gongxing_db") //设置数据库名
                .setDbVersion(1) //设置数据库版本,每次启动应用时将会检查该版本号,
                //发现数据库版本低于这里设置的值将进行数据库升级并触发DbUpgradeListener
                .setAllowTransaction(false)//设置是否开启事务,默认为false关闭事务
                .setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity<?> table) {
                        db.getDatabase().enableWriteAheadLogging();
                        //balabala...
                    }
                })//设置数据库创建时的Listener
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                        //balabala...
                    }
                });//设置数据库升级时的Listener,这里可以执行相关数据库表的相关修改,比如alter语句增加字段等
        //.setDbDir(null);//设置数据库.db文件存放的目录,默认为包名下databases目录下
        db = x.getDb(daoConfig);
    }

    public Boolean addMessagePush(MessagePush messagePush) throws DbException {
        //User user = new User("Kevingo","caolbmail@gmail.com","13299999999",new Date());
        //db.save(user);//保存成功之后【不会】对user的主键进行赋值绑定
        //db.saveOrUpdate(user);//保存成功之后【会】对user的主键进行赋值绑定
        //db.saveBindingId(user);//保存成功之后【会】对user的主键进行赋值绑定,并返回保存是否成功
        Boolean result = db.saveBindingId(messagePush);
        return result;
    }

    public List<MessagePush> findMessagePush(int start, int limit) throws DbException {
        //List<User> users = db.findAll(User.class);
        //showDbMessage("【dbFind#findAll】第一个对象:"+users.get(0).toString());

        //User user = db.findById(User.class, 1);
        //showDbMessage("【dbFind#findById】第一个对象:" + user.toString());

        //long count = db.selector(User.class).where("name","like","%kevin%").and("email","=","caolbmail@gmail.com").count();//返回复合条件的记录数
        //showDbMessage("【dbFind#selector】复合条件数目:" + count);

        List<MessagePush> messages = db.selector(MessagePush.class)
//                .where("name","like","%kevin%")
//                .and("email", "=", "caolbmail@gmail.com")
                .orderBy("time", true)
                .limit(limit) //只查询几条记录
                .offset(start) //偏移两个,从第三个记录开始返回,limit配合offset达到sqlite的limit m,n的查询
                .findAll();
        return messages;
    }

    public Integer deleteMessagePush(int id) throws DbException {
        //db.delete(users.get(0)); //删除第一个对象
        //db.delete(User.class);//删除表中所有的User对象【慎用】
        //db.delete(users); //删除users对象集合
        //users =  db.findAll(User.class);
        // showDbMessage("【dbDelete#delete】数据库中还有user数目:" + users.size());

        WhereBuilder whereBuilder = WhereBuilder.b();
        whereBuilder.and("id", "=", id);
//        whereBuilder.and("id", ">", "5").or("id", "=", "1").expr(" and mobile > '2015-12-29 00:00:01' ");
        int result = db.delete(MessagePush.class, whereBuilder);
        return result;
    }

    public void clearMessagePush() throws DbException {
        db.delete(MessagePush.class);
    }


    public Boolean setUser(ClientInfo clientInfo) throws DbException {
        //如果之前有用户则删除
        List<ClientInfo> users = findUsers();
        if (users != null && users.size() > 0) {
            clearUser();
        }

        Boolean result = db.saveBindingId(clientInfo);
        return result;
    }

    public ClientInfo findUser() throws DbException {
        List<ClientInfo> users = db.selector(ClientInfo.class)
                .limit(1) //只查询几条记录
                .offset(0) //偏移两个,从第三个记录开始返回,limit配合offset达到sqlite的limit m,n的查询
                .findAll();
        if (users == null || users.size() < 1) {
            return null;
        }

        return users.get(0);
    }

    public List<ClientInfo> findUsers() throws DbException {
        List<ClientInfo> users = db.selector(ClientInfo.class).findAll();
        return users;
    }

    public Integer deleteUser(int id) throws DbException {
        WhereBuilder whereBuilder = WhereBuilder.b();
        whereBuilder.and("id", "=", id);
        int result = db.delete(ClientInfo.class, whereBuilder);
        return result;
    }

    public void clearUser() throws DbException {
        db.delete(ClientInfo.class);
    }
}
