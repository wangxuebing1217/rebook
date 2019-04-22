package pri.xy.rebook.common.service;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class HttpClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientService.class);

    @Autowired(required=false)
    private CloseableHttpClient httpClient;

    @Autowired(required=false)
    private RequestConfig requestConfig;

    /**
     * 实现httpClient的get提交
     * url:localhost:8091/addUser?id=1&name=tom
     * 参数设定
     * uri:表示请求路径
     * Map<String,String> params 进行数据封装
     * charset:字符集编码
     * 
     * 编码思路:
     * 	1.判断是否有参数
     * 		有:uri拼接参数
     *      没有:表示该请求不需要参数
     *  2.判断编码是否为null,则设定默认值utf-8
     *  
     *  3.通过httpClient对象发送请求,之后获取返回值数据
     * @throws URISyntaxException 
     */
    public String doGet(String uri,Map<String, String> params, String charset) throws URISyntaxException{
    	
    	//拼接参数格式
    	if(params !=null){
    		URIBuilder builder = new URIBuilder(uri);
    		for (Map.Entry<String, String> entry : params.entrySet()) {
    			//将参数赋值builder
    			builder.setParameter(entry.getKey(), entry.getValue());
			}
    		//url:localhost:8091/addUser?id=1&name=tom
    		uri = builder.build().toString();
    	}
    	
    	//判断编码是否为null
    	if(StringUtils.isEmpty(charset)){
    		charset = "UTF-8";
    	}
    	
    	//定义get请求对象
    	HttpGet httpGet = new HttpGet(uri);
    	//定义请求连接的时长
    	httpGet.setConfig(requestConfig);
    	
    	//发送请求
    	try {
    		CloseableHttpResponse httpResponse = 
    		    	httpClient.execute(httpGet);
    		
    		//判断请求是否正确
    		if(httpResponse.getStatusLine().getStatusCode() == 200){
    			
    			//按照指定字符集解析字符串
    			String result = 
    					EntityUtils.toString(httpResponse.getEntity(),charset);
    			httpResponse.close();
    			return result;
    		}
    		httpResponse.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    //表示只有uri不需要参数
    public String doGet(String uri) throws URISyntaxException{
    	
    	return doGet(uri, null, null);
    }
    
    //需要uri和参数
    public String doGet(String uri,Map<String, String> params) throws URISyntaxException{
    	
    	return doGet(uri, params, null);
    }
    
    
    /**
     * 实现httpClient的post提交方式
     * 1.创建POST请求的对象
     * 2.添加请求的参数(请求的链接时长)
     * 3.将需要传递的参数通过form表单的形式进行数据封装
     * 4.发出http请求获取响应信息
     * 5.判断响应是否成功.之后返回数据
     * @throws UnsupportedEncodingException 
     */
    public String doPost(String uri,Map<String, String> params,String charset) throws UnsupportedEncodingException{
    	
    	//1.创建post请求对象
    	HttpPost httpPost = new HttpPost(uri);
    	
    	//2.添加请求的设定参数
    	httpPost.setConfig(requestConfig);
    	
    	if(charset == null){
    		//给定默认值
    		charset = "UTF-8";
    	}
    	
    	if(params !=null){
	    	//3.定义Form表单的封装对象
	    	//3.1定义参数的集合
	    	List<NameValuePair> parameters = new ArrayList<NameValuePair>();
	    	
	    	//3.2为集合赋值
	    	for (Map.Entry<String, String> entry : params.entrySet()) {
				
	    		parameters.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
	    	//3.3为form表单实体对象赋值
	    	UrlEncodedFormEntity formEntity = 
	    			new UrlEncodedFormEntity(parameters,charset); 
	
	    	//3.4将表单赋值post请求
	    	httpPost.setEntity(formEntity);
    	}
    	//4.发送请求获取返回值参数
    	try {
    		CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
    		//5.判断返回值是否有效
    		if(httpResponse.getStatusLine().getStatusCode() == 200){
    			//表示数据是有效的
    			String result = EntityUtils.toString(httpResponse.getEntity(),charset);
    			httpResponse.close();
    			return result;
    		}
    		//6.将返回值对象关闭
    		httpResponse.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    public String doPost(String uri) throws Exception{
    	
    	return doPost(uri, null, null);
    }
    
    public String doPost(String uri,Map<String,String> params) throws Exception{
    	
    	return doPost(uri, params, null);
    }
}
