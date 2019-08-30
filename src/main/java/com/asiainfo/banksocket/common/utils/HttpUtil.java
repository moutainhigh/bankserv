package com.asiainfo.banksocket.common.utils;

import org.apache.http.Header;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HTTP请求工具类。
* ClassName:HttpClientUtil <br/>
* Date: 2016年9月6日 下午10:35:35 <br/>
* @author yinyanzhen
* @since V1.0.0
*/
public class HttpUtil {
    
    private static CloseableHttpClient httpClient    = HttpClients.createDefault();
    
    private static RequestConfig       requestConfig = RequestConfig.custom()
        .setSocketTimeout(30000)
        .setConnectTimeout(30000).build();
    
    /**
     * doPostJson:(提交json报文post请求,不附加头信息). <br/>
     * @author yinyanzhen
     * @since V1.0.0
     */
    public static HttpResult doPostJson(String url, String json)
            throws ClientProtocolException, IOException {
    	return doPostJson(url, json,null);
    }
    
    /**
     * 提交json数据。
    * @author yinyz
    * @param url 请求URL
    * @param json json字符串
    * @return 返回结果
    * @throws ClientProtocolException 
    * @throws IOException 
    * @since V1.0.0
     */
    public static HttpResult doPostJson(String url, String json,Map<String,String> headers)
        throws ClientProtocolException, IOException {
        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        
        //存在头时,添加头
        headers.forEach((key,val)->{
        	if(!key.toLowerCase().equals("content-length")){
        		httpPost.addHeader(new BasicHeader(key,val));
        	}
        });
        if (json != null) {
            // 构造一个请求实体
            StringEntity stringEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(stringEntity);
        }
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpClient.execute(httpPost);
            Header[] resHeaders=response.getAllHeaders();
            ConcurrentHashMap<String,String> returnHeaders= new ConcurrentHashMap<>();
            for(Header header:resHeaders){
            	returnHeaders.put(header.getName(), header.getValue());
            }
            return new HttpResult(response.getStatusLine().getStatusCode(),
                EntityUtils.toString(response.getEntity(), "UTF-8"),
                returnHeaders);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
    
    /**
     * doGet:(get请求,附带参数). <br/>
     * @author yinyanzhen
     * @param url
     * @param paramMap
     * @param headers 可为空
     */
    public static HttpResult doGet(String url, Map<String, String> paramMap,Map<String,String> headers) 
    		throws URISyntaxException, ClientProtocolException, IOException {
        URIBuilder builder = new URIBuilder(url);
        paramMap.forEach((k,v)->{
        	builder.addParameter(k, v);
        });
        return doGet(builder.build().toString(),headers);
    }
    
    /**
     * doGet:(get请求). <br/>
     * @author yinyanzhen
     * @param url
     * @param headers 可为空
     */
    public static HttpResult doGet(String url,Map<String,String> headers) 
    		throws ClientProtocolException, IOException {
        //创建httpClient对象
        CloseableHttpResponse response = null;
        HttpGet httpGet = new HttpGet(url);
        //存在头时,添加头
        headers.forEach((key,val)->{
        	if(!key.toLowerCase().equals("content-length")){
        		httpGet.addHeader(new BasicHeader(key,val));
        	}
        });
        //设置请求参数
        httpGet.setConfig(requestConfig);
        try {
            //执行请求
            response = httpClient.execute(httpGet);
            Header[] resHeaders=response.getAllHeaders();
            ConcurrentHashMap<String,String> returnHeaders= new ConcurrentHashMap<>();
            for(Header header:resHeaders){
            	returnHeaders.put(header.getName(), header.getValue());
            }
            return new HttpResult(response.getStatusLine().getStatusCode(),
                EntityUtils.toString(response.getEntity(), "UTF-8"),
                returnHeaders);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
    
    private HttpUtil(){}
    
    /**
     * http返回结果对象。
    * ClassName: HttpResult <br/>
    * date: 2016年9月7日 上午10:32:15 <br/>
    * @author yinyz
    * @version HttpClientUtil
    * @since V1.0.0
     */
    public static class HttpResult {
        /**
         * 状态码。
         */
        private Integer statusCode;
        
        /**
         * 结果数据。
         */
        private String  data;
        /**
         * http响应头
         */
        private ConcurrentHashMap<String,String> headers;
        
        /**
        * Creates a new instance of HttpResult.
        * @param code 状态码
        * @param data 结果数据
         */
        public HttpResult(Integer code, String data,ConcurrentHashMap<String,String> headers) {
            this.statusCode = code;
            this.data = data;
            this.headers=headers;
        }
        
        /**
        * Creates a new instance of HttpResult.
         */
        public HttpResult() {
        }
        
        public Integer getCode() {
            return statusCode;
        }
        
        public void setCode(Integer code) {
            this.statusCode = code;
        }
        
        public String getData() {
            return data;
        }
        
        public void setData(String data) {
            this.data = data;
        }

		public ConcurrentHashMap<String,String> getHeaders() {
			return headers;
		}

		public void setHeaders(ConcurrentHashMap<String,String> headers) {
			this.headers = headers;
		}
        
    }
}
