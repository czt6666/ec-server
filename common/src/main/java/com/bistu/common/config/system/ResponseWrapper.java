package com.bistu.common.config.system;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

public class ResponseWrapper extends HttpServletResponseWrapper {
     private final ByteArrayOutputStream outputStream;
     private ServletOutputStream servletOutputStream;
     private PrintWriter writer;

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
        this.outputStream = new ByteArrayOutputStream();
    }

    @Override
    public ServletOutputStream getOutputStream() {
        if (servletOutputStream == null){
            servletOutputStream = new ServletOutputStreamWrapper(outputStream);
        }
        return servletOutputStream;
    }

    public PrintWriter getWriter(){
        if (writer == null){
            writer = new PrintWriter(getOutputStream());

        }
        return writer;
    }

    public String getResponseData(String charsetName){
        Charset charset = Charset.forName(charsetName);
        byte[] bytes = outputStream.toByteArray();
        return new String(bytes,charset);
    }

    public void setResponseData(String responseData, String charsetName){
        Charset charset = Charset.forName(charsetName);
        byte[] bytes = responseData.getBytes(charset);
        outputStream.reset();
        try {
            outputStream.write(bytes);
        }catch (IOException e){

        }
        setCharacterEncoding(charsetName);
    }

    private static class ServletOutputStreamWrapper extends ServletOutputStream{

        private final ByteArrayOutputStream outputStream;

        private ServletOutputStreamWrapper(ByteArrayOutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }

        @Override
        public void write(int b) {
            outputStream.write(b);
        }
    }
}
