package com.android.fisewatchlauncher.net;

import android.app.Application;
import android.graphics.Bitmap;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;

import java.io.File;
import java.util.List;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/10/14
 * @time 12:15
 */
public class HttpAPI {
    private HttpAPI() {
    }

    private static class SingletonHolder {
        private static final HttpAPI INSTANCE = new HttpAPI();
    }

    public static HttpAPI instance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(Application appContext) {
        OkGo.getInstance().init(appContext);
        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文
//            headers.put("commonHeaderKey2", "commonHeaderValue2");
//            HttpParams params = new HttpParams();
//            params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
//            params.put("commonParamsKey2", "这里支持中文参数");
            OkGo.getInstance()
                    .setCacheMode(CacheMode.NO_CACHE)
                    .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                    .setRetryCount(3)
            //如果不想让框架管理cookie（或者叫session的保持）,以下不需要
//              .setCookieStore(new MemoryCookieStore())            //cookie使用内存缓存（app退出后，cookie消失）
//                    .setCookieStore(new PersistentCookieStore(appContext))        //cookie持久化存储，如果cookie不过期，则一直有效
            //可以设置https的证书,以下几种方案根据需要自己设置
//                    .setCertificates()                                  //方法一：信任所有证书,不安全有风险
//              .setCertificates(new SafeTrustManager())            //方法二：自定义信任规则，校验服务端证书
//              .setCertificates(getAssets().open("srca.cer"))      //方法三：使用预埋证书，校验服务端证书（自签名证书）
//              //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
//               .setCertificates(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"))//

            //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
//               .setHostnameVerifier(new SafeHostnameVerifier())

            //可以添加全局拦截器，不需要就不要加入，错误写法直接导致任何回调不执行
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        return chain.proceed(chain.request());
//                    }
//                })

            //这两行同上，不需要就不要加入
//                    .addCommonHeaders(headers)  //设置全局公共头
//                    .addCommonParams(params)//设置全局公共参数
            ;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getString(String url, HttpHeaders headers, HttpParams params, StringCallback callback) {
        GetRequest<String> request = OkGo.<String>get(url)// 请求方式和请求url
                .tag(url);
        if (null != headers) {
            request.headers(headers);
        }
        if (null != params) {
            request.params(params);
        }
        request.execute(callback);
    }

    public void getString(String url, StringCallback callback) {
        getString(url, null, null, callback);
    }

    public void postString(String url, HttpHeaders headers, HttpParams params, StringCallback callback) {
        PostRequest<String> request = OkGo.<String>post(url)// 请求方式和请求url
                .tag(url);
        if (null != headers) {
            request.headers(headers);
        }
        if (null != params) {
            request.params(params);
        }
        request.execute(callback);
    }

    public void postString(String url, StringCallback callback) {
        postString(url, null, null, callback);
    }

    public void getBitmap(String url, BitmapCallback callback) {
        OkGo.<Bitmap>get(url)     // 请求方式和请求url
                .tag(url)                       // 请求的 tag, 主要用于取消对应的请求
                .execute(callback);
    }

    public void uploadJson(String url, String json, StringCallback callback) {
        OkGo.<String>post(url)     // 请求方式和请求url
                .tag(url)                       // 请求的 tag, 主要用于取消对应的请求
                .upJson(json)
                .execute(callback);
    }

    public void uploadFile(String url, List<File> files, StringCallback callback) {
        PostRequest<String> request = OkGo.<String>post(url)
//                .addFileParams("key",files) //一个key对应多个文件
                .tag(url);
        for (int i = 0; i < files.size(); i++) {
            request.params("file_" + i, files.get(i)); //一个key对应一个文件
        }
        request.execute(callback);
    }

    public void downloadNoProgress(String url, FileCallback callback) {
        OkGo.<File>get(url)
                .tag(url)
                .execute(callback);
    }

    public void downloadFile(String url, String filePath, DownloadListener listener) {
        GetRequest<File> request = OkGo.<File>get(url)
//               .headers()
//               .params("")
                ;
        OkDownload okDownload = OkDownload.getInstance();
        okDownload.request(url, request)// url作为tag
//                .extra1("任何数据类型都可以传")
                .folder(filePath)//指定当前下载任务的文件夹目录
//                .fileName()//指定下载的文件名
                .save()
                .register(listener)
                .start();
    }

    public void cancelRequest(String tag) {
        OkGo.getInstance().cancelTag(tag);
//        OkGo.getInstance().cancelAll();
//        OkGo.getInstance().cancelTag(okHttpClient,tag);//根据okhttpclient Tag取消请求
    }

    public void cancelAllRequest(String tag) {
        OkGo.getInstance().cancelAll();
    }

}
