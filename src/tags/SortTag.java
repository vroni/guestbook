package tags;

import java.io.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

public class SortTag extends TagSupport {

   public int doStartTag() throws JspException {
      try {
         pageContext.getOut().print("This is my first tag!");
      } catch (IOException ioe) {
         throw new JspException("Error: IOException while writing to client" + ioe.getMessage());
      }
      return SKIP_BODY;
   }

   public int doEndTag() throws JspException {
      return SKIP_PAGE;
   }
}

//.tld file can be found in WEB-INF/jsp
//add this to your .jsp: <%@ taglib uri="taglib.tld" prefix="first" %>
//tags are included: <gb:sort/>
