package com.bistu.common.util;

import com.alibaba.fastjson.JSONObject;
import com.bistu.common.config.exception.CommonJsonException;
import com.bistu.common.util.constants.Constants;
import com.bistu.common.util.constants.ErrorEnum;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.List;

/**
 * @author: SJJ
 * @description: 本后台接口系统常用的json工具类
 * @date: 2022/6/16
 */
public class CommonUtil {

    /**
     * 返回一个info为空对象的成功消息的json
     */
    public static JSONObject successJson() {
        return successJson(new JSONObject());
    }

    /**
     * 返回一个返回码为200的json
     */
    public static JSONObject successJson(Object info) {
        return successJson(Constants.SUCCESS_MSG, info);
    }

    /**
     * 返回一个返回码为200的自定义msg的json
     */
    public static JSONObject successJson(String msg, Object info) {
        return returnJson(Constants.SUCCESS_CODE, msg, info);
    }

    /**
     * 返回错误信息JSON
     */
    public static JSONObject errorJson(ErrorEnum errorEnum) {
        return errorJson(errorEnum, new JSONObject());
    }

    /**
     * 返回错误信息JSON,自定info
     */
    public static JSONObject errorJson(ErrorEnum errorEnum,Object info) {
        return errorJson(errorEnum, null, info);
    }

    /**
     * 返回错误信息JSON,自定msg, info
     */
    public static JSONObject errorJson(ErrorEnum errorEnum,String msg, Object info) {
        if (msg == null) {
            msg = errorEnum.getErrorMsg();
        }
        return returnJson(errorEnum.getErrorCode(), msg, info);
    }



    /**
     * 请求返回统一json格式
     * @author zxh
     * @param code 返回码
     * @param msg 返回描述信息
     * @param info 返回携带内容
     * @return 返回组建完毕的json对象
     * @CreateDate 2024/5/20
     * @UpdateLog
     */
    private static JSONObject returnJson(String code, String msg, Object info) {
        JSONObject resultJson = new JSONObject();
        resultJson.put("code", code);
        resultJson.put("msg", msg);
        if (info == null) {
            info = new JSONObject();
        }
        resultJson.put("info", info);
        return resultJson;
    }

    /**
     * 查询分页结果后的封装工具方法
     *
     * @param requestJson 请求参数json,此json在之前调用fillPageParam 方法时,已经将pageRow放入
     * @param list        查询分页对象list
     * @param totalCount  查询出记录的总条数
     */
    public static JSONObject successPage(final JSONObject requestJson, List<JSONObject> list, int totalCount) {
        int pageRow = requestJson.getIntValue("pageRow");
        int totalPage = getPageCounts(pageRow, totalCount);
        JSONObject result = successJson();
        JSONObject info = new JSONObject();
        info.put("list", list);
        info.put("totalCount", totalCount);
        info.put("totalPage", totalPage);
        result.put("info", info);
        return result;
    }

    /**
     * 查询分页结果后的封装工具方法
     *
     * @param list 查询分页对象list
     */
    public static JSONObject successPage(List<JSONObject> list) {
        JSONObject result = successJson();
        JSONObject info = new JSONObject();
        info.put("list", list);
        result.put("info", info);
        return result;
    }

    /**
     * 获取总页数
     *
     * @param pageRow   每页行数
     * @param itemCount 结果的总条数
     */
    private static int getPageCounts(int pageRow, int itemCount) {
        if (itemCount == 0) {
            return 1;
        }
        return itemCount % pageRow > 0 ?
                itemCount / pageRow + 1 :
                itemCount / pageRow;
    }

    /**
     * 将request参数值转为json
     */
    public static JSONObject request2Json(HttpServletRequest request) {
        JSONObject requestJson = new JSONObject();
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] pv = request.getParameterValues(paramName);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pv.length; i++) {
                if (pv[i].length() > 0) {
                    if (i > 0) {
                        sb.append(",");
                    }
                    sb.append(pv[i]);
                }
            }
            requestJson.put(paramName, sb.toString());
        }
        return requestJson;
    }

    /**
     * 将request转JSON
     * 并且验证非空字段
     */
    public static JSONObject convert2JsonAndCheckRequiredColumns(HttpServletRequest request, String requiredColumns) {
        JSONObject jsonObject = request2Json(request);
        hasAllRequired(jsonObject, requiredColumns);
        return jsonObject;
    }

    /**
     * 验证是否含有全部必填字段
     *
     * @param requiredColumns 必填的参数字段名称 逗号隔开 比如"userId,name,telephone"
     */
    public static void hasAllRequired(final JSONObject jsonObject, String requiredColumns) {
        if (!StringTools.isNullOrEmpty(requiredColumns)) {
            //验证字段非空
            String[] columns = requiredColumns.split(",");
            String missCol = "";
            for (String column : columns) {
                Object val = jsonObject.get(column.trim());
                if (StringTools.isNullOrEmpty(val)) {
                    missCol += column + "  ";
                }
            }
            if (!StringTools.isNullOrEmpty(missCol)) {
                jsonObject.clear();
                jsonObject.put("code", ErrorEnum.E_90003.getErrorCode());
                jsonObject.put("msg", "缺少必填参数:" + missCol.trim());
                jsonObject.put("info", new JSONObject());
                throw new CommonJsonException(jsonObject);
            }
        }
    }

    /**
     * 在分页查询之前,为查询条件里加上分页参数
     *
     * @param paramObject    查询条件json
     * @param defaultPageRow 默认的每页条数,即前端不传pageRow参数时的每页条数
     */
    private static void fillPageParam(final JSONObject paramObject, int defaultPageRow) {
        int pageNum = paramObject.getIntValue("pageNum");
        pageNum = pageNum == 0 ? 1 : pageNum;
        int pageRow = paramObject.getIntValue("pageRow");
        pageRow = pageRow == 0 ? defaultPageRow : pageRow;
        paramObject.put("offSet", (pageNum - 1) * pageRow);
        paramObject.put("pageRow", pageRow);
        paramObject.put("pageNum", pageNum);
        //删除此参数,防止前端传了这个参数,pageHelper分页插件检测到之后,拦截导致SQL错误
        paramObject.remove("pageSize");
    }

    /**
     * 返回png图片路径
     */
    public static JSONObject successpng(String pngpath){
        //       String opdpath =
        JSONObject result = successJson();
        JSONObject info = new JSONObject();
        info.put("pngpath",pngpath);
        result.put("info",info);
        return result;
    }

    /**
     * 分页查询之前的处理参数
     * 没有传pageRow参数时,默认每页10条.
     */
    public static void fillPageParam(final JSONObject paramObject) {
        fillPageParam(paramObject, 10);
    }
}
