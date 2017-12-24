package com.houoy.www.gongxing.util;

import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.x;

/**
 * Created by andyzhao on 2017/12/24.
 */

public class XUtilCallBack<ResultType> implements Callback.CacheCallback<ResultType> {
//public class XUtilCallBack<ResultType> implements Callback.CommonCallback<ResultType> {

    @Override
    public void onSuccess(ResultType result) {
        //可以根据公司的需求进行统一的请求成功的逻辑处理
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        Toast.makeText(x.app(), ex.getMessage(), Toast.LENGTH_LONG).show();
        if (ex instanceof HttpException) { //网络错误
            HttpException httpEx = (HttpException) ex;
            int responseCode = httpEx.getCode();
            String responseMsg = httpEx.getMessage();
            String errorResult = httpEx.getResult();
            //...
        } else { //其他错误
            //...
        }
    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }


    @Override
    public boolean onCache(ResultType result) {
        // 得到缓存数据, 缓存过期后不会进入这个方法.
        // 如果服务端没有返回过期时间, 参考params.setCacheMaxAge(maxAge)方法.
        //
        // * 客户端会根据服务端返回的 header 中 max-age 或 expires 来确定本地缓存是否给 onCache 方法.
        //   如果服务端没有返回 max-age 或 expires, 那么缓存将一直保存, 除非这里自己定义了返回false的
        //   逻辑, 那么xUtils将请求新数据, 来覆盖它.
        //
        // * 如果信任该缓存返回 true, 将不再请求网络;
        //   返回 false 继续请求网络, 但会在请求头中加上ETag, Last-Modified等信息,
        //   如果服务端返回304, 则表示数据没有更新, 不继续加载数据.
        //
        return false; //true: 信任缓存数据, 不再发起网络请求; false不信任缓存数据
    }
}