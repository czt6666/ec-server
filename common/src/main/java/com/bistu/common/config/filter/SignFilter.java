package com.bistu.common.config.filter;


import com.alibaba.fastjson.JSONObject;
import com.bistu.common.config.system.RequestWrapper;
import com.bistu.common.config.system.ResponseWrapper;
import com.bistu.common.util.CommonUtil;
import com.bistu.common.util.EncryptionUtil;
import com.bistu.common.util.constants.ErrorEnum;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author SJJ
 */
@Component
@Slf4j
public class SignFilter extends OncePerRequestFilter implements Filter {

	private final HttpServletResponse httpServletResponse;

	public SignFilter(HttpServletResponse httpServletResponse) {
		this.httpServletResponse = httpServletResponse;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
			if (request.getRequestURI().contains("/tags/getInfo")) {
				String signatureTest = request.getHeader("signature");
				byte[] sigBytes = Base64.getDecoder().decode(signatureTest);
				JSONObject sigObj = JSONObject.parseObject(new String(sigBytes));

				String signature = sigObj.getString("signature");
				String publicKey = sigObj.getString("publickey");
				String requestBody = getRequestBody(request);

				log.error("signFilter====>" + requestBody);
//                JSONObject bodyJson = JSONObject.parseObject(requestBody);
				EncryptionUtil.initPubKey(publicKey);
//
				RequestWrapper requestWrapper = new RequestWrapper(request, requestBody);
				ResponseWrapper responseWrapper = new ResponseWrapper(response);
				if (!EncryptionUtil.verifySignature(requestBody, signature)) {
					filterChain.doFilter(requestWrapper, responseWrapper);
					String errorResponseDataStr = CommonUtil.errorJson(ErrorEnum.E_402).toString();
					OutputStream outputStream = httpServletResponse.getOutputStream();
					outputStream.write(errorResponseDataStr.getBytes(StandardCharsets.UTF_8));
					outputStream.flush();
//                    throw new RuntimeException("签名校验不通过");
				} else {
					filterChain.doFilter(requestWrapper, response);
				}
			} else {
				filterChain.doFilter(request, response);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getRequestBody(HttpServletRequest request) throws IOException {
		BufferedReader reader = new BufferedReader(request.getReader());
		StringBuilder sb = new StringBuilder();
		String line;


		Map<Character, Integer> a = new HashMap<>();


		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}


}
