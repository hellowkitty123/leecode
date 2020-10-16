package com.kkb.spider.csair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 查询国内往返航班（httprefer和单程不同）
 *
 */
public class SpiderGoAndBackAirCN {

	public static void main(String[] args) throws Exception {
		// 请求查询信息
		spiderQueryaoGo();
		spiderQueryaoBack();
		// 请求html
		spiderHtml();
		// 请求js
		spiderJs();
		// 请求css
		spiderCss();
		// 请求png
		spiderPng();
		// 请求jpg
		spiderJpg();

	}

	public static void spiderQueryaoGo() throws Exception {

		// 1.指定目标网站
		String url = "http://20.184.58.122/lua/B2C40/query/jaxb/direct/query.ao";
		// 2.发起请求
		HttpPost httpPost = new HttpPost(url);
		// 3. 设置请求参数
		httpPost.setHeader("Time-Local", getLocalDateTime());
		httpPost.setHeader("Requst",
				"POST /B2C40/query/jaxb/direct/query.ao HTTP/1.1");
		httpPost.setHeader("Request Method", "POST");
		httpPost.setHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		httpPost.setHeader(
				"Referer",
				"http://b2c.csair.com/B2C40/modules/bookingnew/main/flightSelectDirect.html?t=R&c1=CAN&c2=CTU&d1="
						+ getGoTime()
						+ "&d2="
						+ getBackTime()
						+ "&at=1&ct=0&it=0");
		httpPost.setHeader("Remote Address", "192.168.56.1");
		httpPost.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		httpPost.setHeader("Time-Iso8601", getISO8601Timestamp());
		httpPost.setHeader("Server Address", "192.168.200.100");
		httpPost.setHeader(
				"Cookie",
				"JSESSIONID=782121159357B98CA6112554CF44321E; sid=b5cc11e02e154ac5b0f3609332f86803; aid=8ae8768760927e280160bb348bef3e12; identifyStatus=N; userType4logCookie=M; userId4logCookie=13818791413; useridCookie=13818791413; userCodeCookie=13818791413; JSESSIONID=782121159357B98CA6112554CF44321E; temp_zh=cou%3D0%3Bsegt%3D%E5%8D%95%E7%A8%8B%3Btime%3D2018-01-13%3B%E5%B9%BF%E5%B7%9E-%E5%8C%97%E4%BA%AC%3B1%2C0%2C0%3B%26cou%3D1%3Bsegt%3D%E5%8D%95%E7%A8%8B%3Btime%3D"
						+ getGoTime()
						+ "%3B%E5%B9%BF%E5%B7%9E-%E6%88%90%E9%83%BD%3B1%2C0%2C0%3B%26cou%3D2%3Bsegt%3D%E6%9D%A5%E5%9B%9E%E7%A8%8B%3Btime%3D"
						+ getGoTime()
						+ "%3Bbtime%3D"
						+ getBackTime()
						+ "%3B%E5%B9%BF%E5%B7%9E-%E6%88%90%E9%83%BD%3B1%2C0%2C0%3B%26; WT-FPC=id=211.103.142.26-608782688.30635197:lv=1516172267004:ss=1516170709449:fs=1513243317440:pn=6:vn=10; language=zh_CN; WT.al_flight=WT.al_hctype(R)%3AWT.al_adultnum(1)%3AWT.al_childnum(0)%3AWT.al_infantnum(0)%3AWT.al_orgcity1(CAN)%3AWT.al_dstcity1(CTU)%3AWT.al_orgdate1("
						+ getGoTime()
						+ ")WT.al_orgdate2("
						+ getBackTime()
						+ ")");
		// 4.设置请求参数
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters
				.add(new BasicNameValuePair(
						"json",
						"{\"depcity\":\"CAN\", \"arrcity\":\"WUH\", \"flightdate\":\"20180220\", \"adultnum\":\"1\", \"childnum\":\"0\", \"infantnum\":\"0\", \"cabinorder\":\"0\", \"airline\":\"1\", \"flytype\":\"0\", \"international\":\"0\", \"action\":\"0\", \"segtype\":\"1\", \"cache\":\"0\", \"preUrl\":\"\", \"isMember\":\"\"}"));
		httpPost.setEntity(new UrlEncodedFormEntity(parameters));
		// 5. 发起请求
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = httpClient.execute(httpPost);
		// 6.获取返回值
		System.out.println(response != null);
	}

	public static void spiderQueryaoBack() throws Exception {

		// 1.指定目标网站
		String url = "http://20.184.58.122/lua/B2C40/query/jaxb/direct/query.ao";
		// 2.发起请求
		HttpPost httpPost = new HttpPost(url);
		// 3. 设置请求参数
		httpPost.setHeader("Time-Local", getLocalDateTime());
		httpPost.setHeader("Requst",
				"POST /B2C40/query/jaxb/direct/query.ao HTTP/1.1");
		httpPost.setHeader("Request Method", "POST");
		httpPost.setHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		httpPost.setHeader(
				"Referer",
				"http://b2c.csair.com/B2C40/modules/bookingnew/main/flightSelectDirect.html?t=R&c1=CAN&c2=CTU&d1="
						+ getGoTime()
						+ "&d2="
						+ getBackTime()
						+ "&at=1&ct=0&it=0");
		httpPost.setHeader("Remote Address", "192.168.56.1");
		httpPost.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		httpPost.setHeader("Time-Iso8601", getISO8601Timestamp());
		httpPost.setHeader("Server Address", "192.168.200.100");
		httpPost.setHeader(
				"Cookie",
				"JSESSIONID=782121159357B98CA6112554CF44321E; sid=b5cc11e02e154ac5b0f3609332f86803; aid=8ae8768760927e280160bb348bef3e12; identifyStatus=N; userType4logCookie=M; userId4logCookie=13818791413; useridCookie=13818791413; userCodeCookie=13818791413; JSESSIONID=782121159357B98CA6112554CF44321E; temp_zh=cou%3D0%3Bsegt%3D%E5%8D%95%E7%A8%8B%3Btime%3D2018-01-13%3B%E5%B9%BF%E5%B7%9E-%E5%8C%97%E4%BA%AC%3B1%2C0%2C0%3B%26cou%3D1%3Bsegt%3D%E5%8D%95%E7%A8%8B%3Btime%3D"
						+ getGoTime()
						+ "%3B%E5%B9%BF%E5%B7%9E-%E6%88%90%E9%83%BD%3B1%2C0%2C0%3B%26cou%3D2%3Bsegt%3D%E6%9D%A5%E5%9B%9E%E7%A8%8B%3Btime%3D"
						+ getGoTime()
						+ "%3Bbtime%3D"
						+ getBackTime()
						+ "%3B%E5%B9%BF%E5%B7%9E-%E6%88%90%E9%83%BD%3B1%2C0%2C0%3B%26; WT-FPC=id=211.103.142.26-608782688.30635197:lv=1516171911263:ss=1516170709449:fs=1513243317440:pn=4:vn=10; language=zh_CN; WT.al_flight=WT.al_hctype(R)%3AWT.al_adultnum(1)%3AWT.al_childnum(0)%3AWT.al_infantnum(0)%3AWT.al_orgcity1(CAN)%3AWT.al_dstcity1(CTU)%3AWT.al_orgdate1("
						+ getGoTime()
						+ ")WT.al_orgdate2("
						+ getBackTime()
						+ ")");
		// 4.设置请求参数
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters
				.add(new BasicNameValuePair(
						"json",
						"{\"depcity\":\"CAN\", \"arrcity\":\"WUH\", \"flightdate\":\"20180220\", \"adultnum\":\"1\", \"childnum\":\"0\", \"infantnum\":\"0\", \"cabinorder\":\"0\", \"airline\":\"1\", \"flytype\":\"0\", \"international\":\"0\", \"action\":\"0\", \"segtype\":\"1\", \"cache\":\"0\", \"preUrl\":\"\", \"isMember\":\"\"}"));
		httpPost.setEntity(new UrlEncodedFormEntity(parameters));
		// 5. 发起请求
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = httpClient.execute(httpPost);
		// 6.获取返回值
		System.out.println(response != null);
	}

	public static void spiderHtml() throws Exception {

		// 1.指定目标网站
		String url = "http://20.184.58.122/lua/B2C40/modules/bookingnew/main/flightSelectDirect.html?t=S&c1=CAN&c2=CTU&d1=2018-01-17&at=1&ct=0&it=0";
		// 2.发起请求
		HttpPost httpPost = new HttpPost(url);
		// 3. 设置请求参数
		httpPost.setHeader("Time-Local", getLocalDateTime());
		httpPost.setHeader("Requst",
				"POST /B2C40/query/jaxb/direct/query.ao HTTP/1.1");
		httpPost.setHeader("Request Method", "POST");
		httpPost.setHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		httpPost.setHeader(
				"Referer",
				"http://b2c.csair.com/B2C40/modules/bookingnew/main/flightSelectDirect.html?t=S&c1=CAN&c2=WUH&d1=2018-02-20&at=1&ct=0&it=0");
		httpPost.setHeader("Remote Address", "192.168.56.1");
		httpPost.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		httpPost.setHeader("Time-Iso8601", getISO8601Timestamp());
		httpPost.setHeader("Server Address", "192.168.200.100");
		httpPost.setHeader(
				"Cookie",
				"JSESSIONID=782121159357B98CA6112554CF44321E; sid=b5cc11e02e154ac5b0f3609332f86803; aid=8ae8768760927e280160bb348bef3e12; identifyStatus=N; userType4logCookie=M; userId4logCookie=13818791413; useridCookie=13818791413; userCodeCookie=13818791413; temp_zh=cou%3D0%3Bsegt%3D%E5%8D%95%E7%A8%8B%3Btime%3D2018-01-13%3B%E5%B9%BF%E5%B7%9E-%E5%8C%97%E4%BA%AC%3B1%2C0%2C0%3B%26cou%3D1%3Bsegt%3D%E5%8D%95%E7%A8%8B%3Btime%3D2018-01-17%3B%E5%B9%BF%E5%B7%9E-%E6%88%90%E9%83%BD%3B1%2C0%2C0%3B%26; JSESSIONID=782121159357B98CA6112554CF44321E; WT-FPC=id=211.103.142.26-608782688.30635197:lv=1516170718655:ss=1516170709449:fs=1513243317440:pn=2:vn=10; language=zh_CN; WT.al_flight=WT.al_hctype(S)%3AWT.al_adultnum(1)%3AWT.al_childnum(0)%3AWT.al_infantnum(0)%3AWT.al_orgcity1(CAN)%3AWT.al_dstcity1(CTU)%3AWT.al_orgdate1(2018-01-17)");
		// 4.设置请求参数
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters
				.add(new BasicNameValuePair(
						"json",
						"{\"depcity\":\"CAN\", \"arrcity\":\"WUH\", \"flightdate\":\"20180220\", \"adultnum\":\"1\", \"childnum\":\"0\", \"infantnum\":\"0\", \"cabinorder\":\"0\", \"airline\":\"1\", \"flytype\":\"0\", \"international\":\"0\", \"action\":\"0\", \"segtype\":\"1\", \"cache\":\"0\", \"preUrl\":\"\", \"isMember\":\"\"}"));
		httpPost.setEntity(new UrlEncodedFormEntity(parameters));
		// 5. 发起请求
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = httpClient.execute(httpPost);
		// 6.获取返回值
		System.out.println(response != null);
	}

	public static void spiderJs() throws Exception {

		// 1.指定目标网站
		String url = "http://20.184.58.122/lua/B2C40/dist/main/modules/common/requireConfig.js";
		// 2.发起请求
		HttpPost httpPost = new HttpPost(url);
		// 3. 设置请求参数
		httpPost.setHeader("Time-Local", getLocalDateTime());
		httpPost.setHeader("Requst",
				"POST /B2C40/query/jaxb/direct/query.ao HTTP/1.1");
		httpPost.setHeader("Request Method", "POST");
		httpPost.setHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		httpPost.setHeader(
				"Referer",
				"http://b2c.csair.com/B2C40/modules/bookingnew/main/flightSelectDirect.html?t=S&c1=CAN&c2=WUH&d1=2018-02-20&at=1&ct=0&it=0");
		httpPost.setHeader("Remote Address", "192.168.56.1");
		httpPost.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		httpPost.setHeader("Time-Iso8601", getISO8601Timestamp());
		httpPost.setHeader("Server Address", "192.168.200.100");
		httpPost.setHeader(
				"Cookie",
				"JSESSIONID=782121159357B98CA6112554CF44321E; sid=b5cc11e02e154ac5b0f3609332f86803; aid=8ae8768760927e280160bb348bef3e12; identifyStatus=N; userType4logCookie=M; userId4logCookie=13818791413; useridCookie=13818791413; userCodeCookie=13818791413; temp_zh=cou%3D0%3Bsegt%3D%E5%8D%95%E7%A8%8B%3Btime%3D2018-01-13%3B%E5%B9%BF%E5%B7%9E-%E5%8C%97%E4%BA%AC%3B1%2C0%2C0%3B%26cou%3D1%3Bsegt%3D%E5%8D%95%E7%A8%8B%3Btime%3D2018-01-17%3B%E5%B9%BF%E5%B7%9E-%E6%88%90%E9%83%BD%3B1%2C0%2C0%3B%26; JSESSIONID=782121159357B98CA6112554CF44321E; WT-FPC=id=211.103.142.26-608782688.30635197:lv=1516170718655:ss=1516170709449:fs=1513243317440:pn=2:vn=10; language=zh_CN; WT.al_flight=WT.al_hctype(S)%3AWT.al_adultnum(1)%3AWT.al_childnum(0)%3AWT.al_infantnum(0)%3AWT.al_orgcity1(CAN)%3AWT.al_dstcity1(CTU)%3AWT.al_orgdate1(2018-01-17)");
		// 4.设置请求参数
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters
				.add(new BasicNameValuePair(
						"json",
						"{\"depcity\":\"CAN\", \"arrcity\":\"WUH\", \"flightdate\":\"20180220\", \"adultnum\":\"1\", \"childnum\":\"0\", \"infantnum\":\"0\", \"cabinorder\":\"0\", \"airline\":\"1\", \"flytype\":\"0\", \"international\":\"0\", \"action\":\"0\", \"segtype\":\"1\", \"cache\":\"0\", \"preUrl\":\"\", \"isMember\":\"\"}"));
		httpPost.setEntity(new UrlEncodedFormEntity(parameters));
		// 5. 发起请求
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = httpClient.execute(httpPost);
		// 6.获取返回值
		System.out.println(response != null);
	}

	public static void spiderCss() throws Exception {

		// 1.指定目标网站
		String url = "http://20.184.58.122/lua/B2C40/dist/main/css/flight.css";
		// 2.发起请求
		HttpPost httpPost = new HttpPost(url);
		// 3. 设置请求参数
		httpPost.setHeader("Time-Local", getLocalDateTime());
		httpPost.setHeader("Requst",
				"POST /B2C40/query/jaxb/direct/query.ao HTTP/1.1");
		httpPost.setHeader("Request Method", "POST");
		httpPost.setHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		httpPost.setHeader("Referer",
				"http://b2c.csair.com/B2C40/modules/bookingnew/main/flightSelectDirect.html");
		httpPost.setHeader("Remote Address", "192.168.56.1");
		httpPost.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		httpPost.setHeader("Time-Iso8601", getISO8601Timestamp());
		httpPost.setHeader("Server Address", "192.168.200.100");
		httpPost.setHeader(
				"Cookie",
				"JSESSIONID=782121159357B98CA6112554CF44321E; sid=b5cc11e02e154ac5b0f3609332f86803; aid=8ae8768760927e280160bb348bef3e12; identifyStatus=N; userType4logCookie=M; userId4logCookie=13818791413; useridCookie=13818791413; userCodeCookie=13818791413; temp_zh=cou%3D0%3Bsegt%3D%E5%8D%95%E7%A8%8B%3Btime%3D2018-01-13%3B%E5%B9%BF%E5%B7%9E-%E5%8C%97%E4%BA%AC%3B1%2C0%2C0%3B%26cou%3D1%3Bsegt%3D%E5%8D%95%E7%A8%8B%3Btime%3D2018-01-17%3B%E5%B9%BF%E5%B7%9E-%E6%88%90%E9%83%BD%3B1%2C0%2C0%3B%26; JSESSIONID=782121159357B98CA6112554CF44321E; WT-FPC=id=211.103.142.26-608782688.30635197:lv=1516170718655:ss=1516170709449:fs=1513243317440:pn=2:vn=10; language=zh_CN; WT.al_flight=WT.al_hctype(S)%3AWT.al_adultnum(1)%3AWT.al_childnum(0)%3AWT.al_infantnum(0)%3AWT.al_orgcity1(CAN)%3AWT.al_dstcity1(CTU)%3AWT.al_orgdate1(2018-01-17)");
		// 4.设置请求参数
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters
				.add(new BasicNameValuePair(
						"json",
						"{\"depcity\":\"CAN\", \"arrcity\":\"WUH\", \"flightdate\":\"20180220\", \"adultnum\":\"1\", \"childnum\":\"0\", \"infantnum\":\"0\", \"cabinorder\":\"0\", \"airline\":\"1\", \"flytype\":\"0\", \"international\":\"0\", \"action\":\"0\", \"segtype\":\"1\", \"cache\":\"0\", \"preUrl\":\"\", \"isMember\":\"\"}"));
		httpPost.setEntity(new UrlEncodedFormEntity(parameters));
		// 5. 发起请求
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = httpClient.execute(httpPost);
		// 6.获取返回值
		System.out.println(response != null);
	}

	public static void spiderPng() throws Exception {

		// 1.指定目标网站
		String url = "http://20.184.58.122/lua/B2C40/dist/main/images/common.png";
		// 2.发起请求
		HttpPost httpPost = new HttpPost(url);
		// 3. 设置请求参数
		httpPost.setHeader("Time-Local", getLocalDateTime());
		httpPost.setHeader("Requst",
				"POST /B2C40/query/jaxb/direct/query.ao HTTP/1.1");
		httpPost.setHeader("Request Method", "POST");
		httpPost.setHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		httpPost.setHeader(
				"Referer",
				"http://b2c.csair.com/B2C40/modules/bookingnew/main/flightSelectDirect.html?t=S&c1=CAN&c2=WUH&d1=2018-02-20&at=1&ct=0&it=0");
		httpPost.setHeader("Remote Address", "192.168.56.1");
		httpPost.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		httpPost.setHeader("Time-Iso8601", getISO8601Timestamp());
		httpPost.setHeader("Server Address", "192.168.200.100");
		httpPost.setHeader(
				"Cookie",
				"JSESSIONID=782121159357B98CA6112554CF44321E; sid=b5cc11e02e154ac5b0f3609332f86803; aid=8ae8768760927e280160bb348bef3e12; identifyStatus=N; userType4logCookie=M; userId4logCookie=13818791413; useridCookie=13818791413; userCodeCookie=13818791413; temp_zh=cou%3D0%3Bsegt%3D%E5%8D%95%E7%A8%8B%3Btime%3D2018-01-13%3B%E5%B9%BF%E5%B7%9E-%E5%8C%97%E4%BA%AC%3B1%2C0%2C0%3B%26cou%3D1%3Bsegt%3D%E5%8D%95%E7%A8%8B%3Btime%3D2018-01-17%3B%E5%B9%BF%E5%B7%9E-%E6%88%90%E9%83%BD%3B1%2C0%2C0%3B%26; JSESSIONID=782121159357B98CA6112554CF44321E; WT-FPC=id=211.103.142.26-608782688.30635197:lv=1516170718655:ss=1516170709449:fs=1513243317440:pn=2:vn=10; language=zh_CN; WT.al_flight=WT.al_hctype(S)%3AWT.al_adultnum(1)%3AWT.al_childnum(0)%3AWT.al_infantnum(0)%3AWT.al_orgcity1(CAN)%3AWT.al_dstcity1(CTU)%3AWT.al_orgdate1(2018-01-17)");
		// 4.设置请求参数
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters
				.add(new BasicNameValuePair(
						"json",
						"{\"depcity\":\"CAN\", \"arrcity\":\"WUH\", \"flightdate\":\"20180220\", \"adultnum\":\"1\", \"childnum\":\"0\", \"infantnum\":\"0\", \"cabinorder\":\"0\", \"airline\":\"1\", \"flytype\":\"0\", \"international\":\"0\", \"action\":\"0\", \"segtype\":\"1\", \"cache\":\"0\", \"preUrl\":\"\", \"isMember\":\"\"}"));
		httpPost.setEntity(new UrlEncodedFormEntity(parameters));
		// 5. 发起请求
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = httpClient.execute(httpPost);
		// 6.获取返回值
		System.out.println(response != null);
	}

	public static void spiderJpg() throws Exception {

		// 1.指定目标网站
		String url = "http://20.184.58.122/lua/B2C40/dist/main/images/loadingimg.jpg";
		// 2.发起请求
		HttpPost httpPost = new HttpPost(url);
		// 3. 设置请求参数
		httpPost.setHeader("Time-Local", getLocalDateTime());
		httpPost.setHeader("Requst",
				"POST /B2C40/query/jaxb/direct/query.ao HTTP/1.1");
		httpPost.setHeader("Request Method", "POST");
		httpPost.setHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		httpPost.setHeader(
				"Referer",
				"http://b2c.csair.com/B2C40/modules/bookingnew/main/flightSelectDirect.html?t=S&c1=CAN&c2=WUH&d1=2018-02-20&at=1&ct=0&it=0");
		httpPost.setHeader("Remote Address", "192.168.56.1");
		httpPost.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
		httpPost.setHeader("Time-Iso8601", getISO8601Timestamp());
		httpPost.setHeader("Server Address", "192.168.200.100");
		httpPost.setHeader(
				"Cookie",
				"JSESSIONID=782121159357B98CA6112554CF44321E; sid=b5cc11e02e154ac5b0f3609332f86803; aid=8ae8768760927e280160bb348bef3e12; identifyStatus=N; userType4logCookie=M; userId4logCookie=13818791413; useridCookie=13818791413; userCodeCookie=13818791413; temp_zh=cou%3D0%3Bsegt%3D%E5%8D%95%E7%A8%8B%3Btime%3D2018-01-13%3B%E5%B9%BF%E5%B7%9E-%E5%8C%97%E4%BA%AC%3B1%2C0%2C0%3B%26cou%3D1%3Bsegt%3D%E5%8D%95%E7%A8%8B%3Btime%3D2018-01-17%3B%E5%B9%BF%E5%B7%9E-%E6%88%90%E9%83%BD%3B1%2C0%2C0%3B%26; JSESSIONID=782121159357B98CA6112554CF44321E; WT-FPC=id=211.103.142.26-608782688.30635197:lv=1516170718655:ss=1516170709449:fs=1513243317440:pn=2:vn=10; language=zh_CN; WT.al_flight=WT.al_hctype(S)%3AWT.al_adultnum(1)%3AWT.al_childnum(0)%3AWT.al_infantnum(0)%3AWT.al_orgcity1(CAN)%3AWT.al_dstcity1(CTU)%3AWT.al_orgdate1(2018-01-17)");
		// 4.设置请求参数
		ArrayList<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
		parameters
				.add(new BasicNameValuePair(
						"json",
						"{\"depcity\":\"CAN\", \"arrcity\":\"WUH\", \"flightdate\":\"20180220\", \"adultnum\":\"1\", \"childnum\":\"0\", \"infantnum\":\"0\", \"cabinorder\":\"0\", \"airline\":\"1\", \"flytype\":\"0\", \"international\":\"0\", \"action\":\"0\", \"segtype\":\"1\", \"cache\":\"0\", \"preUrl\":\"\", \"isMember\":\"\"}"));
		httpPost.setEntity(new UrlEncodedFormEntity(parameters));
		// 5. 发起请求
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = httpClient.execute(httpPost);
		// 6.获取返回值
		System.out.println(response != null);
	}

	public static String getLocalDateTime() {
		DateFormat df = new SimpleDateFormat("dd/MMM/yyyy'T'HH:mm:ss +08:00",
				Locale.ENGLISH);
		String nowAsISO = df.format(new Date());
		return nowAsISO;

	}

	public static String getISO8601Timestamp() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+08:00");
		String nowAsISO = df.format(new Date());
		return nowAsISO;
	}

	public static String getGoTime() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String nowAsISO = df.format(new Date());
		return nowAsISO;
	}

	public static String getBackTime() {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, +1);// 把日期往前减少一天，若想把日期向后推一天则将负数改为正数
		date = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		return dateString;
	}
}
