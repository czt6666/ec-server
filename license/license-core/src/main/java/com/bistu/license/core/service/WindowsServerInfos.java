package com.bistu.license.core.service;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * <p>用于获取客户Windows服务器的基本信息</p>
 */
@Slf4j
public class WindowsServerInfos extends AServerInfos {

    @Override
    protected String getCPUSerial() throws Exception {
        String result = "";
        try {
            // 创建临时文件作为VBScript脚本来获取CPU序列号
            File file = File.createTempFile("tmp", ".vbs");
            // 在JVM退出时删除
            file.deleteOnExit();
            FileWriter fw = new FileWriter(file);
            // VBScript脚本内容，用于获取windows下的cpu序列号
            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                    + "Set colItems = objWMIService.ExecQuery _ \n" + "   (\"Select * from Win32_Processor\") \n"
                    + "For Each objItem in colItems \n" + "    Wscript.Echo objItem.ProcessorId \n"
                    + "    exit for  ' do the first cpu only! \n" + "Next \n";
            // 写入脚本
            fw.write(vbs);
            fw.close();
            // 开启进程执行VBScript脚本
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            // 获取脚本执行结果
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
            // 删除临时文件
            file.delete();
        } catch (Exception e) {
            log.error("获取cpu信息错误", e);
        }
        return result.trim();
    }

    @Override
    protected String getMainBoardSerial() throws Exception {

        String result = "";
        try {
            File file = File.createTempFile("realhowto", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new FileWriter(file);

            String vbs = "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
                    + "Set colItems = objWMIService.ExecQuery _ \n" + "   (\"Select * from Win32_BaseBoard\") \n"
                    + "For Each objItem in colItems \n" + "    Wscript.Echo objItem.SerialNumber \n"
                    + "    exit for  ' do the first cpu only! \n" + "Next \n";

            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
            file.delete();
        } catch (Exception e) {
            log.error("获取主板信息错误", e);
        }
        return result.trim();
    }


}
