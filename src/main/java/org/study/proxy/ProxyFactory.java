package org.study.proxy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.Locale;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.study.proxy.handler.InvocationHandler;

/**
 * 这个一个代理的工厂类 1.该工厂是面向接口的 2.会对接口内所有的方法进行代理操作
 */
@SuppressWarnings({ "rawtypes" })
public class ProxyFactory {
	// 换行分割符
	private static final String separator = "\r\n";
	private static final String proxyClassName = "$Proxy";
	// 获取项目的根目录
	private static final String dir = System.getProperty("user.dir") + "\\src\\";
	private static final String DEFALUT_PACKAGE = "proxy";

	public static Object getProxyInstance(Class<?> targetInterface, InvocationHandler invocationHandler) {
		// 第一步：根据targetInterface和invocationHandler拼接代理对象$Proxy的字符串
		String proxyJavaString = createPorxyJavaString(targetInterface, invocationHandler);
		// 第二步：将字符串写出到一个文件中去，并且对它进行编译
		createJavaFileAndCompiler(proxyJavaString);
		// 第三步：将编译的java文件加载到JVM中去
		Class<?> proxyClass = loadeFileInToJVM("file:" + dir, DEFALUT_PACKAGE + "." + proxyClassName);
		try {
			Constructor<?> constructor = proxyClass.getConstructor(invocationHandler.getClass());
			Object newInstance = constructor.newInstance(invocationHandler);
			return newInstance;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 对代理的目标接口做方法的处理
	 * 
	 * @param targetInterface
	 *            被代理的目标接口
	 * @return
	 */
	private static String proxyMethodByTargetInterface(Class<?> targetInterface) {
		// 方法的字符串处理
		StringBuilder builder = new StringBuilder();
		if (targetInterface.isInterface()) {
			Method[] methods = targetInterface.getMethods();
			String name = targetInterface.getName();
			for (int i = 0, len = methods.length; i < len; i++) {
				Method method = methods[i];
				// 获取方法的返回值类型
				String returnType = method.getReturnType().toString();
				// 方法名
				String methodName = method.getName();
				// 方法的参数
				Parameter[] parameters = method.getParameters();
				int len2 = parameters.length;
				Parameter param;
				// 拼接代理的方法具体实现
				builder.append("    public " + returnType + " " + methodName + "(");
				if (len2 > 0) {
					for (int j = 0; j < len2 - 1; j++) {
						param = parameters[j];
						// 获取入参的类型
						String paramType = param.getType().getName();
						// 获取入参的name
						String paramName = param.getName();
						builder.append(paramType + " " + paramName + ", ");
					}
					builder.append(parameters[len2 - 1].getType().getName() + " " + parameters[len2 - 1].getName());
				}
				builder.append("){" + separator);
				builder.append("        try{" + separator);
				builder.append("            Method method = " + name + ".class.getMethod(\"" + methodName + "\"");
				if (len2 > 0) {
					for (int m = 0; m < len2; m++) {
						param = parameters[m];
						String name2 = param.getType().getName();
						builder.append(", " + name2 + ".class");
					}
				}
				builder.append(");" + separator);
				builder.append("            handler.call(this,method");
				if (len2 > 0) {
					for (int k = 0; k < len2; k++) {
						param = parameters[k];
						String paramName = param.getName();
						builder.append("," + paramName);
					}
				}
				builder.append(");" + separator);
				builder.append("        } catch (Exception e){ e.printStackTrace();}" + separator);
				builder.append("    }" + separator);
			}
		} else {
			throw new IllegalArgumentException(targetInterface.getSimpleName() + "必须是接口类型");
		}
		return builder.toString();
	}

	private static String createPorxyJavaString(Class<?> targetInterface, InvocationHandler invocationHandler) {
		StringBuilder builder = new StringBuilder();
		// 第一步：获取被代理的目标接口的方法
		String proxyMethodString = proxyMethodByTargetInterface(targetInterface);
		// 第二步：拼写代理的类
		String proxyTargetClassName = targetInterface.getName();
		builder.append("package " + DEFALUT_PACKAGE + ";" + separator);
		builder.append("import java.lang.reflect.Method;" + separator);
		builder.append("public class " + proxyClassName + " implements " + proxyTargetClassName + "{" + separator);
		// 声明代理对象
		String handler = invocationHandler.getClass().getName();
		builder.append("    private " + handler + " handler;" + separator);
		builder.append("    public " + proxyClassName + "(" + handler + " handler){" + separator);
		builder.append("        this.handler = handler;" + separator);
		builder.append("    }" + separator);
		builder.append(proxyMethodString);
		builder.append("}" + separator);
		return builder.toString();
	}

	/**
	 * 把写好的代理类的string编写为java文件，并且利用jdk的编译器编译成class文件
	 */
	public static void createJavaFileAndCompiler(String proxyJavaString) {
		// 生成的代理类放在项目根目录下的proxy文件中，这个下面定义的package proxy 这个包名一致
		File dirFile = new File(dir + File.separator + DEFALUT_PACKAGE);
		// 创建文件夹
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		// 写出java文件
		File javaFile = new File(dirFile, proxyClassName + ".java");
		try (FileWriter writer = new FileWriter(javaFile)) {
			writer.write(proxyJavaString);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		compilerJavaFile(javaFile);
	}

	/**
	 * 编译java文件
	 * 
	 * @param files
	 */
	public static void compilerJavaFile(File... files) {
		// 获取系统的java编译器
		JavaCompiler systemJavaCompiler = ToolProvider.getSystemJavaCompiler();
		// 获得系统文件管理器
		StandardJavaFileManager standardFileManager = systemJavaCompiler.getStandardFileManager(null, Locale.CHINA, Charset.defaultCharset());
		Iterable<? extends JavaFileObject> javaFileObjects = standardFileManager.getJavaFileObjects(files);
		CompilationTask task = systemJavaCompiler.getTask(null, standardFileManager, null, null, null, javaFileObjects);
		task.call();
		try {
			standardFileManager.close();
			// 将.java文件删掉，只剩下.class文件
			for (File f : files) { f.delete(); }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Class loadeFileInToJVM(String url, String filaName) {
		URLClassLoader loader = null;
		try {
			URL[] urls = { new URL(url) };
			loader = new URLClassLoader(urls);
			Class<?> loadClass = loader.loadClass(filaName);
			return loadClass;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != loader) {
					loader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
