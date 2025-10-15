package com.bistu.common.config.filter;


import com.bistu.common.util.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author SJJ
 */
@Component
@Slf4j
public class RequestFilter extends OncePerRequestFilter implements Filter {

//    @Autowired
//    TokenService tokenService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {

			/***
			 * 20230113，sjj，拦截器放行打开文件及放缩
			 * 同步TokenService.java中getUserInfoFromCache防止token为空跳回登录
			 */
			final String uri = request.getRequestURI().startsWith("/") ? request.getRequestURI().substring(1) : request.getRequestURI();
			// 特殊情况 判断一下url是不是指定的url
			// if (!jniruing) "服务开小差"
			if (uri.contains("ofdviewer/openFile") || uri.contains("ofdviewer/openFileCon") || uri.contains("path/")) {
//                System.out.println("放行");
				/**
				 * 20230116,hyj,add用户登录的情况下，三种指定的url需记录token
				 * 无用户登录直接放行token=null
				 */
				// 每个请求记录一个traceId,可以根据traceId搜索出本次请求的全部相关日志
				MDC.put("traceId", UUID.randomUUID().toString().replace("-", "").substring(0, 12));
//                 setUsername(request);
				/** 2023/12/13-ts-add: 解决由于MDC获取不到token导致当前登录用户无权限的问题*/
				String token = request.getHeader("token");
				if (!StringTools.isNullOrEmpty(token)) {
					MDC.put("token", token);
				}

//                setProductId(request);
				// 使request中的body可以重复读取 https://juejin.im/post/6858037733776949262#heading-4
				request = new ContentCachingRequestWrapper(request);
				filterChain.doFilter(request, response);
				return;
			}
			// 每个请求记录一个traceId,可以根据traceId搜索出本次请求的全部相关日志
			MDC.put("traceId", UUID.randomUUID().toString().replace("-", "").substring(0, 12));
//             setUsername(request);

			/** 2023/12/13-ts-add: 解决由于MDC获取不到token导致当前登录用户无权限的问题*/
			String token = request.getHeader("token");
			if (!StringTools.isNullOrEmpty(token)) {
				MDC.put("token", token);
			}

//            setProductId(request);
			// 使request中的body可以重复读取 https://juejin.im/post/6858037733776949262#heading-4
			request = new ContentCachingRequestWrapper(request);
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			throw e;
		} finally {
			// 清理ThreadLocal
			MDC.clear();
		}
	}

	/**
	 * 将url参数中的productId放入ThreadLocal
	 */
//    private void setProductId(HttpServletRequest request) {
//
//        String productIdStr = request.getParameter("productId");
//        log.info("rizhi================>",productIdStr);
//        if (!StringTools.isNullOrEmpty(productIdStr)) {
//            log.debug("url中productId = {}", productIdStr);
//            MDC.put("productId", productIdStr);
//        }
//    }

//    private void setUsername(HttpServletRequest request) {
//        //通过token解析出username
//        String token = request.getHeader("token");
//        if (!StringTools.isNullOrEmpty(token)) {
//            MDC.put("token", token);
//            try {
//                SessionUserInfo info = tokenService.getUserInfo();
//                if (info != null) {
//                    String username = info.getUsername();
//                    MDC.put("username", username);
//                }
//            } catch (CommonJsonException e) {
//                log.info("无效的token:{}", token);
//            }
//        }
//    }

}
