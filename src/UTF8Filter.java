import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public final class UTF8Filter implements Filter {
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html; charset=UTF-8");
    response.setCharacterEncoding("UTF-8");
    chain.doFilter(request, response);
    
  }

  public void destroy() 
  { 
  }

  public void init(FilterConfig filterConfig) 
  {
  }

}

