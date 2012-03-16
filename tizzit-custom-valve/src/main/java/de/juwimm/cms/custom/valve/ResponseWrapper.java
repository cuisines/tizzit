package de.juwimm.cms.custom.valve;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Context;
import org.apache.catalina.Globals;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;

public class ResponseWrapper extends Response {

	protected Response res;
	protected String cookiePath;
	protected String cookieDomain;

	public ResponseWrapper(Response res, String cookiePath, String cookieDomain) {
		this.res = res;
		this.cookiePath = cookiePath;
		this.cookieDomain = cookieDomain;
	}

	// Code for delegation of java.lang.Object methods to res

	public int hashCode() {
		return res.hashCode();
	}

	public boolean equals(Object object) {
		return res.equals(object);
	}

	public String toString() {
		return res.toString();
	}

	// Code for delegation of org.apache.catalina.connector.Response methods to
	// res

	public String getMessage() {
		return res.getMessage();
	}

	public Context getContext() {
		return res.getContext();
	}

	public void reset() {
		res.reset();
	}

	public void reset(int n, String string) {
		res.reset(n, string);
	}

	public boolean isError() {
		return res.isError();
	}

	public void flushBuffer() throws IOException {
		res.flushBuffer();
	}

	public void setError() {
		res.setError();
	}

	public boolean isClosed() {
		return res.isClosed();
	}

	public int getContentLength() {
		return res.getContentLength();
	}

	public String getInfo() {
		return res.getInfo();
	}

	public String getContentType() {
		return res.getContentType();
	}

	public void setContentLength(int n) {
		res.setContentLength(n);
	}

	public void setContentType(String string) {
		res.setContentType(string);
	}

	public ServletOutputStream getOutputStream() throws IOException {
		return res.getOutputStream();
	}

	public void recycle() {
		res.recycle();
	}

	public void clearEncoders() {
		res.clearEncoders();
	}

	public Connector getConnector() {
		return res.getConnector();
	}

	public void setConnector(Connector connector) {
		res.setConnector(connector);
	}

	public void setContext(Context context) {
		res.setContext(context);
	}

	public Request getRequest() {
		return res.getRequest();
	}

	public HttpServletResponse getResponse() {
		return res.getResponse();
	}

	public OutputStream getStream() {
		return res.getStream();
	}

	public void setStream(OutputStream outputStream) {
		res.setStream(outputStream);
	}

	public String getCharacterEncoding() {
		return res.getCharacterEncoding();
	}

	public Locale getLocale() {
		return res.getLocale();
	}

	public void setCharacterEncoding(String string) {
		res.setCharacterEncoding(string);
	}

	public void addCookie(Cookie cookie) {
		if (Globals.SESSION_COOKIE_NAME.equals(cookie.getName())) {
			if (cookiePath != null) {
				cookie.setPath(cookiePath);
			}
			if (cookieDomain != null) {
				cookie.setDomain(cookieDomain);
			}
		}
		res.addCookie(cookie);
	}

	public void addHeader(String string, String string1) {
		res.addHeader(string, string1);
	}

	public Cookie[] getCookies() {
		return res.getCookies();
	}

	public String getHeader(String string) {
		return res.getHeader(string);
	}

	public String[] getHeaderNames() {
		return res.getHeaderNames();
	}

	public void setRequest(Request request) {
		res.setRequest(request);
	}

	public boolean isCommitted() {
		return res.isCommitted();
	}

	public void addCookieInternal(Cookie cookie) {
		if (Globals.SESSION_COOKIE_NAME.equals(cookie.getName())) {
			if (cookiePath != null) {
				cookie.setPath(cookiePath);
			}
			if (cookieDomain != null) {
				cookie.setDomain(cookieDomain);
			}
		}
		res.addCookieInternal(cookie);
	}

	public void setLocale(Locale locale) {
		res.setLocale(locale);
	}

	public int getBufferSize() {
		return res.getBufferSize();
	}

	public void setBufferSize(int n) {
		res.setBufferSize(n);
	}

	public void setCoyoteResponse(org.apache.coyote.Response response) {
		res.setCoyoteResponse(response);
	}

	public org.apache.coyote.Response getCoyoteResponse() {
		return res.getCoyoteResponse();
	}

	public int getContentCount() {
		return res.getContentCount();
	}

	public void setAppCommitted(boolean flag) {
		res.setAppCommitted(flag);
	}

	public boolean isAppCommitted() {
		return res.isAppCommitted();
	}

	public boolean getIncluded() {
		return res.getIncluded();
	}

	public void setIncluded(boolean flag) {
		res.setIncluded(flag);
	}

	public void setSuspended(boolean flag) {
		res.setSuspended(flag);
	}

	public boolean isSuspended() {
		return res.isSuspended();
	}

	public ServletOutputStream createOutputStream() throws IOException {
		return res.createOutputStream();
	}

	public void finishResponse() throws IOException {
		res.finishResponse();
	}

	public PrintWriter getReporter() throws IOException {
		return res.getReporter();
	}

	public PrintWriter getWriter() throws IOException {
		return res.getWriter();
	}

	public void resetBuffer() {
		res.resetBuffer();
	}

	public String[] getHeaderValues(String string) {
		return res.getHeaderValues(string);
	}

	public int getStatus() {
		return res.getStatus();
	}

	public void addDateHeader(String string, long l) {
		res.addDateHeader(string, l);
	}

	public void addIntHeader(String string, int n) {
		res.addIntHeader(string, n);
	}

	public boolean containsHeader(String string) {
		return res.containsHeader(string);
	}

	public String encodeRedirectURL(String string) {
		return res.encodeRedirectURL(string);
	}

	public String encodeRedirectUrl(String string) {
		return res.encodeRedirectUrl(string);
	}

	public String encodeURL(String string) {
		return res.encodeURL(string);
	}

	public String encodeUrl(String string) {
		return res.encodeUrl(string);
	}

	public void sendAcknowledgement() throws IOException {
		res.sendAcknowledgement();
	}

	public void sendError(int n) throws IOException {
		res.sendError(n);
	}

	public void sendError(int n, String string) throws IOException {
		res.sendError(n, string);
	}

	public void sendRedirect(String string) throws IOException {
		res.sendRedirect(string);
	}

	public void setDateHeader(String string, long l) {
		res.setDateHeader(string, l);
	}

	public void setHeader(String string, String string1) {
		res.setHeader(string, string1);
	}

	public void setIntHeader(String string, int index) {
		res.setIntHeader(string, index);
	}

	public void setStatus(int n) {
		res.setStatus(n);
	}

	public void setStatus(int n, String string) {
		res.setStatus(n, string);
	}

}
