package com.okada.sakamichi.servlet.wrapper;

import com.okada.sakamichi.config.Constants;
import com.okada.sakamichi.util.JSONUtils;
import com.okada.sakamichi.SakaMichi;
import com.okada.sakamichi.SakaMichiContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Response {

    private HttpServletResponse httpServletResponse;

    public Response(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    public void json(String json) {
        httpServletResponse.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = httpServletResponse.getWriter();
            out.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public void json(Object json) {
        json(JSONUtils.objToStr(json));
    }

    public void view(String templateName) {
        SakaMichiContext sakaMichiContext = SakaMichi.getContext();

        HttpServletRequest servletRequest = sakaMichiContext.getRequest().getRaw();
        HttpServletResponse servletResponse = sakaMichiContext.getResponse().getRaw();
        try {
            servletRequest.getRequestDispatcher(Constants.DEFAULT_VIEW_PREFIX + templateName + Constants.DEFAULT_VIEW_SUFFIX)
                    .forward(servletRequest, servletResponse);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HttpServletResponse getRaw() {
        return httpServletResponse;
    }
}
