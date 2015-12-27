package com.mrqyoung.rpc.usesdk;

import java.io.IOException;

import com.mrqyoung.rpc.target.Target;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.webserver.WebServer;

import android.os.Bundle;
import android.app.Activity;

public class MainActivity extends Activity {

	private static final int PORT = 8000;
	private WebServer webServer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		simpleTestOfSdk();
		startRPCServer();

	}

	private void simpleTestOfSdk() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Target sdk = Target.getInstance();
				System.out.print("12 + 34 = ");
				System.out.println(sdk.add(12, 34));
				System.out.print("78 - 56 = ");
				System.out.println(sdk.subtract(78, 56));
				try {
					System.out.print("http find IP [203.208.48.146] : ");
					System.out.println(sdk.httpGet("203.208.48.146"));
					System.out.print("Device: ");
					System.out.println(sdk.getDeviceInfo());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * ��ʼ��XmlRpcServer��handle������󣬿�������
	 */
	private void startRPCServer() {
		webServer = new WebServer(PORT);
		XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();
		PropertyHandlerMapping phm = new PropertyHandlerMapping();
		/**
		 * ����SDK�в����˵���ģʽ���ⲿ�޷�ֱ�ӳ�ʼ������Ҫͨ��һ������������ɳ�ʼ��
		 * ���SDK�еĶ������ֱ��new�������������Լ�InstanceFactory�඼����Ҫ��
		 */
		phm.setRequestProcessorFactoryFactory(new InstanceFactory());
		phm.setVoidMethodEnabled(true);
		try {
			/**
			 * ΪĿ��SDK���hander���󶨵�"sdk"�������������
			 * ��client�˵���ʱʹ�õľ���"sdk"���ƣ������Target������ķ���
			 * ����Target.add()���������ⲿ��ӦΪsdk.add()
			 */
			phm.addHandler("sdk", Target.class);
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		xmlRpcServer.setHandlerMapping(phm);
		try {
			webServer.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * �ù���������Target���г�ʼ��
	 * �μ� https://ws.apache.org/xmlrpc/handlerCreation.html
	 */
	private class InstanceFactory implements RequestProcessorFactoryFactory {
		@Override
		public RequestProcessorFactory getRequestProcessorFactory(Class arg0)
				throws XmlRpcException {
			// TODO Auto-generated method stub
			return factory;
		}

		private final Target instance = Target.getInstance();
		private final RequestProcessorFactory factory = new MyRequestProcessorFactory();

		private class MyRequestProcessorFactory implements
				RequestProcessorFactory {
			public Object getRequestProcessor(XmlRpcRequest xmlRpcRequest)
					throws XmlRpcException {
				return instance;
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (webServer != null) {
			webServer.shutdown();
		}
	}

}
