package org.korek.spring.web.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CharsetFilter implements Filter
{

	private String encoding;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException
	{
		encoding = filterConfig.getInitParameter("requestEncoding");

		if (encoding == null)
		{
			encoding = "UTF-8";
		}

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		if (null == request.getCharacterEncoding())
		{
			request.setCharacterEncoding(encoding);
		}

		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");

		chain.doFilter(request, response);

	}

	@Override
	public void destroy()
	{

	}

}
