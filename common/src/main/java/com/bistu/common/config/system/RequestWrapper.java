package com.bistu.common.config.system;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * HttpServletRequest 包装类，允许在 Servlet 中多次读取请求体内容
 * 重写了 getInputStream()方法和 getReader() 方法，返回可以多次读取的流。
 */
public class RequestWrapper extends HttpServletRequestWrapper {

    private final byte[] body;

    /**
     * 构造 RequestWrapper 对象
     *
     * @param request 原始 HttpServletRequest 对象
     * @param context 请求体内容
     */
    public RequestWrapper(HttpServletRequest request, String context) {
        super(request);
        this.body = context.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 重写 getInputStream 方法，返回经过包装后的 ServletInputStream 对象
     *
     * @return 经过包装后的 ServletInputStream 对象
     */
    @Override
    public ServletInputStream getInputStream() {
        return new ServletInputStreamWrapper(new ByteArrayInputStream(body));
    }

    /**
     * 重写 getReader 方法，返回经过包装后的 BufferedReader 对象
     *
     * @return 经过包装后的 BufferedReader 对象
     */
    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }

    /**
     * 私有内部类，用于包装 ServletInputStream 对象
     */
    private static class ServletInputStreamWrapper extends ServletInputStream {
        private final ByteArrayInputStream inputStream;

        /**
         * 构造函数，传入待包装的 ByteArrayInputStream 对象
         *
         * @param inputStream 待包装的 ByteArrayInputStream 对象
         */
        public ServletInputStreamWrapper(ByteArrayInputStream inputStream) {
            this.inputStream = inputStream;
        }

        /**
         * 重写 read 方法，读取流中的下一个字节
         *
         * @return 读取到的下一个字节，如果已达到流的末尾，则返回-1
         */
        @Override
        public int read() {
            return inputStream.read();
        }

        /**
         * 覆盖 isFinished 方法，指示流是否已完成读取数据
         *
         * @return 始终返回 false，表示流未完成读取数据
         */
        @Override
        public boolean isFinished() {
            return false;
        }

        /**
         * 重写 isReady 方法，指示流是否准备好进行读取操作
         *
         * @return 始终返回 false，表示流未准备好进行读取操作
         */
        @Override
        public boolean isReady() {
            return false;
        }

        /**
         * 重写 setReadListener 方法，设置读取监听器
         *
         * @param readListener 读取监听器
         */
        @Override
        public void setReadListener(ReadListener readListener) {

        }
    }
}
