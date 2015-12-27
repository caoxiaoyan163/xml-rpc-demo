package com.mrqyoung.rpc.target;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;


public class Target {

	private Target() {
		System.out.println("new Target");
	}

	private static class Holder {
		private static final Target target = new Target();
	}

	/**
	 * SDK ͨ������õ���ģʽ������Ψһ����
	 */
	public static Target getInstance() {
		return Holder.target;
	}

	/**
	 * SDK �еĻ�����������1���ӷ�
	 * @param i1
	 * @param i2
	 * @return
	 */
	public int add(int i1, int i2) {
		return i1 + i2;
	}

	/**
	 * SDK �еĻ�����������1������
	 * @param i1
	 * @param i2
	 * @return
	 */
	public int subtract(int i1, int i2) {
		return i1 - i2;
	}
	
	/**
	 * ���ڲ������������󲢷������ݵ�����
	 * ����ʹ�� ipip.net ��IP��ѯ�ӿ�
	 * @param String ipAddr: ��Ҫ����IP��ַ
	 * @return String
	 * @throws Exception 
	 */
	public String httpGet(String ipAddr) throws Exception {
		String url = "http://freeapi.ipip.net/" + ipAddr; 
		HttpGet httpGet = new HttpGet(url);
		HttpResponse httpResponse = new DefaultHttpClient().execute(httpGet);
		if (httpResponse == null) return "<Null>";
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if (statusCode != 200) return "<Http Error: " + statusCode +">";
		String result = EntityUtils.toString(httpResponse.getEntity());
		if (result.isEmpty()) return "<Empty>";
		return result;
	}
	
	/**
	 * ��ȡ Android �豸��һЩ����Ϣ
	 * @return
	 * @throws JSONException
	 */
	public String getDeviceInfo() throws Exception {
		JSONObject json = new JSONObject();
		json.put("deviceName", android.os.Build.MODEL);
		json.put("apiLevel", android.os.Build.VERSION.SDK_INT);
		json.put("androidVersion", android.os.Build.VERSION.RELEASE);
		json.put("manufactuer", android.os.Build.MANUFACTURER);
		json.put("product", android.os.Build.PRODUCT);
		return json.toString();
	}

}
