package session;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * Created by jun.
 */
public class HttpResponseWrapper extends HttpServletResponseWrapper {
    private boolean committed;

    private long contentWritten;

    private long contentLength;

    private HttpRequestWrapper httpRequestWrapper;

    public HttpResponseWrapper(HttpServletResponse response, HttpRequestWrapper httpRequestWrapper) {
        super(response);
        this.httpRequestWrapper = httpRequestWrapper;
    }

    @Override
    public void addHeader(String name, String value) {
        if ("Content-Length".equalsIgnoreCase(name)) {
            setContentLength(Long.parseLong(value));
        }
        super.addHeader(name, value);
    }

    @Override
    public void setContentLength(int len) {
        setContentLength((long) len);
        super.setContentLength(len);
    }

    private void setContentLength(long len) {
        this.contentLength = len;
        doCheckContentLength(0);
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        commitResponse();
        super.sendRedirect(location);
    }

    @Override
    public void sendError(int sc) throws IOException {
        commitResponse();
        super.sendError(sc);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        commitResponse();
        super.sendError(sc, msg);
    }

    @Override
    public void flushBuffer() throws IOException {
        commitResponse();
        super.flushBuffer();
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriterProxy(super.getWriter());
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStreamProxy(super.getOutputStream());
    }

    private void commitResponse() {
        if (!committed) {
            httpRequestWrapper.saveSession();
            committed = true;
        }
    }

    private void doCheckContentLength(long contentLengthToWrite) {
        this.contentWritten += contentLengthToWrite;
        boolean isBodyFullyWritten = this.contentLength > 0
                && this.contentWritten >= this.contentLength;
        int bufferSize = getBufferSize();
        boolean requiresFlush = bufferSize > 0 && this.contentWritten >= bufferSize;
        if (isBodyFullyWritten || requiresFlush) {
            commitResponse();
        }
    }

    private void doCheckContentLength(boolean content) {
        doCheckContentLength(content ? 4 : 5);
    }

//    private void checkContentLength(char content) {
//        doCheckContentLength(1);
//    }

    private void checkContentLength(Object content) {
        checkContentLength(String.valueOf(content));
    }

    private void checkContentLength(byte[] content) {
        doCheckContentLength(content == null ? 0 : content.length);
    }

    private void checkContentLength(char[] content) {
        doCheckContentLength(content == null ? 0 : content.length);
    }

//    private void checkContentLength(int content) {
//        checkContentLength(String.valueOf(content));
//    }

    private void checkContentLength(float content) {
        checkContentLength(String.valueOf(content));
    }

    private void checkContentLength(double content) {
        checkContentLength(String.valueOf(content));
    }

    private void checkContentLength(String content) {
        doCheckContentLength(content.length());
    }

    private void checkContentLengthLn() {
        checkContentLength("\r\n");
    }


    private class ServletOutputStreamProxy extends ServletOutputStream {
        private final ServletOutputStream delegate;

        ServletOutputStreamProxy(ServletOutputStream delegate) {
            super();
            this.delegate = delegate;
        }

        public void flush() throws IOException {
            commitResponse();
            this.delegate.flush();
        }

        public void close() throws IOException {
            commitResponse();
            this.delegate.close();
        }

        public int hashCode() {
            return this.delegate.hashCode();
        }

        public boolean equals(Object obj) {
            return this.delegate.equals(obj);
        }

        public void print(boolean b) throws IOException {
            doCheckContentLength(b);
            this.delegate.print(b);
        }

        public void print(char c) throws IOException {
            doCheckContentLength(c);
            this.delegate.print(c);
        }

        public void print(double d) throws IOException {
            checkContentLength(d);
            this.delegate.print(d);
        }

        public void print(float f) throws IOException {
            checkContentLength(f);
            this.delegate.print(f);
        }

        public void print(int i) throws IOException {
            doCheckContentLength(i);
            this.delegate.print(i);
        }

        public void print(long l) throws IOException {
            doCheckContentLength(l);
            this.delegate.print(l);
        }

        public void print(String s) throws IOException {
            checkContentLength(s);
            this.delegate.print(s);
        }

        public void println() throws IOException {
            checkContentLengthLn();
            this.delegate.println();
        }

        public void println(boolean b) throws IOException {
            doCheckContentLength(b);
            checkContentLengthLn();
            this.delegate.println(b);
        }

        public void println(char c) throws IOException {
            doCheckContentLength(c);
            checkContentLengthLn();
            this.delegate.println(c);
        }

        public void println(double d) throws IOException {
            checkContentLength(d);
            checkContentLengthLn();
            this.delegate.println(d);
        }

        public void println(float f) throws IOException {
            checkContentLength(f);
            checkContentLengthLn();
            this.delegate.println(f);
        }

        public void println(int i) throws IOException {
            doCheckContentLength(i);
            checkContentLengthLn();
            this.delegate.println(i);
        }

        public void println(long l) throws IOException {
            doCheckContentLength(l);
            checkContentLengthLn();
            this.delegate.println(l);
        }

        public void println(String s) throws IOException {
            checkContentLength(s);
            checkContentLengthLn();
            this.delegate.println(s);
        }

        public void write(byte[] b) throws IOException {
            checkContentLength(b);
            this.delegate.write(b);
        }

        public void write(byte[] b, int off, int len) throws IOException {
            doCheckContentLength(len);
            this.delegate.write(b, off, len);
        }

        public void write(int b) throws IOException {
            doCheckContentLength(b);
            this.delegate.write(b);
        }

        @Override
        public boolean isReady() {
            return delegate.isReady();
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            delegate.setWriteListener(writeListener);
        }


        public String toString() {
            return getClass().getName() + "[delegate=" + this.delegate.toString() + "]";
        }
    }
    
    private class PrintWriterProxy extends PrintWriter {
        private final PrintWriter delegate;

        PrintWriterProxy(PrintWriter delegate) {
            super(delegate);
            this.delegate = delegate;
        }

        public void flush() {
            commitResponse();
            this.delegate.flush();
        }

        public void close() {
            commitResponse();
            this.delegate.close();
        }

        public int hashCode() {
            return this.delegate.hashCode();
        }

        public boolean equals(Object obj) {
            return this.delegate.equals(obj);
        }

        public String toString() {
            return getClass().getName() + "[delegate=" + this.delegate.toString() + "]";
        }

        public boolean checkError() {
            return this.delegate.checkError();
        }

        public void write(int c) {
            doCheckContentLength(c);
            this.delegate.write(c);
        }

        public void write(char[] buf, int off, int len) {
            doCheckContentLength(len);
            this.delegate.write(buf, off, len);
        }

        public void write(char[] buf) {
            checkContentLength(buf);
            this.delegate.write(buf);
        }

        public void write(String s, int off, int len) {
            doCheckContentLength(len);
            this.delegate.write(s, off, len);
        }

        public void write(String s) {
            checkContentLength(s);
            this.delegate.write(s);
        }

        public void print(boolean b) {
            doCheckContentLength(b);
            this.delegate.print(b);
        }

        public void print(char c) {
            doCheckContentLength(c);
            this.delegate.print(c);
        }

        public void print(int i) {
            doCheckContentLength(i);
            this.delegate.print(i);
        }

        public void print(long l) {
            doCheckContentLength(l);
            this.delegate.print(l);
        }

        public void print(float f) {
            checkContentLength(f);
            this.delegate.print(f);
        }

        public void print(double d) {
            checkContentLength(d);
            this.delegate.print(d);
        }

        public void print(char[] s) {
            checkContentLength(s);
            this.delegate.print(s);
        }

        public void print(String s) {
            checkContentLength(s);
            this.delegate.print(s);
        }

        public void print(Object obj) {
            checkContentLength(obj);
            this.delegate.print(obj);
        }

        public void println() {
            checkContentLengthLn();
            this.delegate.println();
        }

        public void println(boolean x) {
            doCheckContentLength(x);
            checkContentLengthLn();
            this.delegate.println(x);
        }

        public void println(char x) {
            doCheckContentLength(x);
            checkContentLengthLn();
            this.delegate.println(x);
        }

        public void println(int x) {
            doCheckContentLength(x);
            checkContentLengthLn();
            this.delegate.println(x);
        }

        public void println(long x) {
            doCheckContentLength(x);
            checkContentLengthLn();
            this.delegate.println(x);
        }

        public void println(float x) {
            checkContentLength(x);
            checkContentLengthLn();
            this.delegate.println(x);
        }

        public void println(double x) {
            checkContentLength(x);
            checkContentLengthLn();
            this.delegate.println(x);
        }

        public void println(char[] x) {
            checkContentLength(x);
            checkContentLengthLn();
            this.delegate.println(x);
        }

        public void println(String x) {
            checkContentLength(x);
            checkContentLengthLn();
            this.delegate.println(x);
        }

        public void println(Object x) {
            checkContentLength(x);
            checkContentLengthLn();
            this.delegate.println(x);
        }

        public PrintWriter printf(String format, Object... args) {
            return this.delegate.printf(format, args);
        }

        public PrintWriter printf(Locale l, String format, Object... args) {
            return this.delegate.printf(l, format, args);
        }

        public PrintWriter format(String format, Object... args) {
            return this.delegate.format(format, args);
        }

        public PrintWriter format(Locale l, String format, Object... args) {
            return this.delegate.format(l, format, args);
        }

        public PrintWriter append(CharSequence csq) {
            doCheckContentLength(csq.length());
            return this.delegate.append(csq);
        }

        public PrintWriter append(CharSequence csq, int start, int end) {
            doCheckContentLength(end - start);
            return this.delegate.append(csq, start, end);
        }

        public PrintWriter append(char c) {
            doCheckContentLength(c);
            return this.delegate.append(c);
        }
    }
}
