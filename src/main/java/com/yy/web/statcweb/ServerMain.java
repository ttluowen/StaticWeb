package com.yy.web.statcweb;

import java.util.Properties;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;

import com.yy.log.Logger;
import com.yy.util.NumberUtil;
import com.yy.util.PropertyUtil;
import com.yy.web.config.SystemConfig;

public class ServerMain {

	private static final int PORT = 92;

	/**
	 * 以 Java 应用程序单独运行时的初始化操作。
	 */
	public static void mainInit() {

		String root = System.getProperty("user.dir") + "\\";
		SystemConfig.setSystemPath(root);
		Logger.setSystemPath(root);
	}

	/**
	 * 执行入口。
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		mainInit();

		Properties config = PropertyUtil.read(SystemConfig.getSystemPath() + "config.properties");
		int port = NumberUtil.parseInt(config.get("port"));
		if (port == 0) {
			port = PORT;
		}

		Server server = new Server();
		server.setStopAtShutdown(true);

		ServerConnector connector1 = new ServerConnector(server);
		connector1.setPort(port);
		server.setConnectors(new Connector[] { connector1 });

		// 资源目录 列出文件名
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(true);
		resourceHandler.setResourceBase(SystemConfig.getSystemPath() + "root\\");
		resourceHandler.setStylesheet("");

		// 设置静态目录路径
		ContextHandler staticContextHandler = new ContextHandler();
		staticContextHandler.setContextPath("/");
		staticContextHandler.setHandler(resourceHandler);
		server.setHandler(staticContextHandler);

		System.out.println("服务启动成功，端口 " + port);

		// 打开浏览器。
		Runtime.getRuntime().exec("cmd /c start http://localhost:" + port);


		server.start();
		server.join();
	}
}
