package com.nibado.amazon.service.browser.servlet;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.nibado.amazon.lib.s3wrapper.S3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ObjectServlet extends HttpServlet {
    private static final Pattern OBJECT_ROUTE = Pattern.compile("/object/([a-zA-Z0-9-_]+)/(.+)");

    @Autowired
    private S3 s3;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        log.info(request.getRequestURI());

        Matcher m = OBJECT_ROUTE.matcher(request.getRequestURI());

        if (!m.matches()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        S3Object object = s3.get(m.group(1), m.group(2));

        String contentType = object.getObjectMetadata().getContentType();

        if (contentType.equals("application/octet-stream")) {
            contentType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(object.getKey());
        }

        response.setStatus(200);
        response.setContentLength((int) object.getObjectMetadata().getContentLength());
        response.setContentType(contentType);
        IOUtils.copy(object.getObjectContent(), response.getOutputStream());
        response.getOutputStream().close();
    }
}
