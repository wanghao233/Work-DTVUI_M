package com.changhong.tvos.dtv.tvap;

import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import javaTEA.tea;

/**
 * java
 * @author
 */
public class DtvPortInfoGetANDEncryptionPost {

	private static String REPORT = "DtvPortInfoGetANDEncryptionPost";

	/*
	 * getURLContent
	 */
	public String getURLContent(String urlStr) {

		HttpGet request = new HttpGet(urlStr);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response;
		String jsonstr = null;

		try {
			response = client.execute(request);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				jsonstr = EntityUtils.toString(response.getEntity(), "UTF-8");

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonstr;
	}

	/*	public void resolveConfig(String config) throws UnsupportedEncodingException {
			JSONObject js = JSONObject.fromObject(config);
			String url = js.getString("postUrl");
			boolean enable = js.getBoolean("enable");
			if (enable) {
				DTVReqInfo dr = new DTVReqInfo();
				dr.setDTVVersion("3.0");
				dr.setSubClass("program");
				REPORTinfo rp = new REPORTinfo();
				rp.setChannel("CCTV-1");
				rp.setChannelID("...");
				rp.setProgram("...");

				rp.setProgramID("...ID");
				dr.setREPORTinfo(rp);

				List<DTVReqInfo> list = new ArrayList<DTVReqInfo>();
				list.add(dr);
				JSONArray jo = JSONArray.fromObject(list);

				System.out.println(jo.toString());
				System.out.println(new DTVDemo().HTTPPOST(jo.toString(), url));
			}
		}*/

	/*
	 * EncryptedPOST
	 */
	public static String EncryptedPOST(String data, String url) {
		String returnvalue = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			System.out.println("create httppost:" + url);

			HttpPost method = new HttpPost(url);

			Log.i(REPORT, "DtvPortData done");
			HttpEntity se = getStringEntity(data);
			method.setEntity(se);
			HttpResponse res = httpClient.execute(method);
			HttpEntity entity = res.getEntity();

			String httpRes = EntityUtils.toString(entity);
			returnvalue = httpRes;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnvalue;
	}

	/*
	 * getStringEntity
	 */
	public static HttpEntity getStringEntity(String data) throws IOException {
		HashMap<String, String> map = new HashMap<String, String>();
		String endstr = tea.hex_en(data, DtvPortInfoKeyTools.getTeakey(), DtvPortInfoKeyTools.getTeano());

		Log.i(REPORT, "DtvPortInfo-Encryption");

		map.put("data", endstr);
		map.put("designation", "Telescope-a");

		ObjectMapper mapper = new ObjectMapper();
		StringWriter sw = new StringWriter();
		JsonGenerator gen = new JsonFactory().createJsonGenerator(sw);
		mapper.writeValue(gen, map);
		gen.close();

		String postData = sw.toString();
		System.out.println(postData);

		StringEntity se = new StringEntity(postData);
		se.setContentType("application/json;charset=utf-8");
		se.setContentEncoding("utf-8");
		return se;
	}

	/*	public void main(String[] args) throws UnsupportedEncodingException {
			DtvChannelManager get = new DtvChannelManager();
			get.resolveConfig(getURLContent("http://10.3.30.18:13000/api/v1/dtvConfig"));
		}*/
}
