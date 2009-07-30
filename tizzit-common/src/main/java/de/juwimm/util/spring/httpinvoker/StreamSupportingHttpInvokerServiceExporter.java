/**
 * Copyright (c) 2009 Juwi MacMillan Group GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.juwimm.util.spring.httpinvoker;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectOutputStream;
import java.io.FilterOutputStream;
import java.io.FilterInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.remoting.support.RemoteInvocation;
import org.springframework.remoting.support.RemoteInvocationResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Extends <code>HttpInvokerServiceExporter</code> to allow
 * <code>InputStream</code> parameters to remote service methods and
 * <code>InputStream</code> return values from remote service methods.  See
 * the documentation for
 * <code>StreamSupportingHttpInvokerProxyFactoryBean</code> for important
 * restrictions and usage information.  Also see
 * <code>HttpInvokerServiceExporter</code> for general usage of the exporter
 * facility.
 *
 * @author Andy DePue
 * @since 1.2.3
 * @see StreamSupportingHttpInvokerProxyFactoryBean
 * @see HttpInvokerServiceExporter
 */
public class StreamSupportingHttpInvokerServiceExporter
  extends HttpInvokerServiceExporter
{
  private static final Log log = LogFactory.getLog(StreamSupportingHttpInvokerServiceExporter.class);
  
  private boolean emptyInputStreamParameterBeforeReturn = false;

  
  //
  // METHODS FROM CLASS HttpInvokerServiceExporter
  //
  
  protected RemoteInvocation readRemoteInvocation(final HttpServletRequest request, final InputStream is)
      throws IOException, ClassNotFoundException
  {
    final RemoteInvocation ret = super.readRemoteInvocation(request, new StreamSupportingHttpInvokerRequestExecutor.CloseShieldedInputStream(is));
    boolean closeIs = true;
    if(ret instanceof StreamSupportingRemoteInvocation) {
      final StreamSupportingRemoteInvocation ssri = (StreamSupportingRemoteInvocation)ret;
      if(ssri.getInputStreamParam() >= 0 && !ssri.isInputStreamParamNull()) {
        ssri.getArguments()[ssri.getInputStreamParam()] = new ParameterInputStream(is);
        closeIs = false;
      }
    }
    if(closeIs) {
      is.close();
    }
    return ret;
  }

  protected void writeRemoteInvocationResult(final HttpServletRequest request,
                                             final HttpServletResponse response,
                                             final RemoteInvocationResult result)
      throws IOException
  {
    if(hasStreamResult(result)) {
      response.setContentType(StreamSupportingHttpInvokerRequestExecutor.CONTENT_TYPE_SERIALIZED_OBJECT_WITH_STREAM);
    } else {
      response.setContentType(CONTENT_TYPE_SERIALIZED_OBJECT);
    }

    writeRemoteInvocationResult(request, response, result, response.getOutputStream());
  }

  protected void writeRemoteInvocationResult(final HttpServletRequest request,
                                             final HttpServletResponse response,
                                             final RemoteInvocationResult result,
                                             final OutputStream os)
      throws IOException
  {
    if(hasStreamResult(result)) {
      final OutputStream decoratedOut = decorateOutputStream(request, response, os);
      response.setHeader("Transfer-Encoding", "chunked");

      try {
        // We want to be able to close the ObjectOutputStream in order to
        // properly flush and clear it out, but we don't want it closing
        // our underlying OutputStream.
        final ObjectOutputStream oos = new ObjectOutputStream(new CloseShieldedOutputStream(new BufferedOutputStream(decoratedOut, 4096)));
        try {
          doWriteRemoteInvocationResult(result, oos);
          oos.flush();
        } finally {
          oos.close();
        }
        doWriteReturnInputStream((StreamSupportingRemoteInvocationResult)result, decoratedOut);
      } finally {
        decoratedOut.close();
      }
    } else {
      super.writeRemoteInvocationResult(request, response, result, os);
    }
  }

  protected RemoteInvocationResult invokeAndCreateResult(final RemoteInvocation invocation, final Object targetObject)
  {
    try {
      final Object value = invoke(invocation, targetObject);
      if(invocation instanceof StreamSupportingRemoteInvocation) {
        final Boolean closedInputStreamParam = getParameterInputStreamClosedFlag(invocation);
        if(value instanceof InputStream) {
          return new StreamSupportingRemoteInvocationResult((InputStream)value, closedInputStreamParam);
        } else {
          return new StreamSupportingRemoteInvocationResult(value, closedInputStreamParam);
        }
      } else {
        return new RemoteInvocationResult(value);
      }
    } catch (Throwable ex) {
      if(invocation instanceof StreamSupportingRemoteInvocation) {
        return new StreamSupportingRemoteInvocationResult(ex, getParameterInputStreamClosedFlag(invocation));
      } else {
        return new RemoteInvocationResult(ex);
      }
    } finally {
      final ParameterInputStream pi = getParameterInputStreamFrom(invocation);
      if(pi != null) {
        try {
          pi.doRealClose(getEmptyInputStreamParameterBeforeReturn());
        } catch(IOException e) {
          log.warn("Error while attempting to close InputStream parameter for RemoteInvocation '" + invocation + "'", e);
        }
      }
    }
  }
  
  
  
  
  
  //
  // HELPER METHODS
  //
  

  /**
   * See {@link #setEmptyInputStreamParameterBeforeReturn(boolean)}.
   *
   * @return <code>true</code> if any InputStream parameter should be
   *         "emptied" before sending the response to the client.
   * @see #setEmptyInputStreamParameterBeforeReturn(boolean)
   */
  public boolean getEmptyInputStreamParameterBeforeReturn()
  {
    return this.emptyInputStreamParameterBeforeReturn;
  }

  /**
   * Determines if this servlet should "empty" any InputStream parameter to a
   * service method before returning to the client.  This is provided as a
   * workaround for some servlet containers in order to ensure that if an
   * exception is thrown or the service method returns before the InputStream
   * parameter is read that the client will not block trying to send the
   * remaining InputStream to the server.  This means that in the face of an
   * exception or early return from a method that the client will still finish
   * uploading all of its data before it becomes aware of the situation,
   * taking up unnecessary time and bandwidth.  Because of this, a better
   * solution should be found to this problem in the future.  This property
   * defaults to <code>false</code>.
   *
   * @param emptyInputStreamParameterBeforeReturn
   *
   *
   * @see #getEmptyInputStreamParameterBeforeReturn()
   */
  public void setEmptyInputStreamParameterBeforeReturn(final boolean emptyInputStreamParameterBeforeReturn)
  {
    this.emptyInputStreamParameterBeforeReturn = emptyInputStreamParameterBeforeReturn;
  }

  protected boolean hasStreamResult(final RemoteInvocationResult result)
  {
    return result instanceof StreamSupportingRemoteInvocationResult &&
           ((StreamSupportingRemoteInvocationResult)result).getHasReturnStream();
  }

  protected void doWriteReturnInputStream(final StreamSupportingRemoteInvocationResult result, final OutputStream unbufferedChunkedOut)
      throws IOException
  {
    // We use the unbuffered chunked out with a custom buffer for optimum
    // performance - partly because we can't be sure that the returned
    // InputStream is itself buffered.
    final InputStream isResult = result.getServerSideInputStream();
    if(isResult != null) {
      try {
        final byte[] buffer = new byte[4096];
        int read;
        while((read = isResult.read(buffer)) != -1) {
          unbufferedChunkedOut.write(buffer, 0, read);
        }
      } finally {
        result.setServerSideInputStream(null);
        isResult.close();
      }
    }
  }

  protected ParameterInputStream getParameterInputStreamFrom(final RemoteInvocation invocation)
  {
    if(invocation instanceof StreamSupportingRemoteInvocation) {
      final StreamSupportingRemoteInvocation ssri = (StreamSupportingRemoteInvocation)invocation;
      if(ssri.getInputStreamParam() >= 0 && !ssri.isInputStreamParamNull()) {
        return (ParameterInputStream)ssri.getArguments()[ssri.getInputStreamParam()];
      }
    }
    
    return null;
  }
  
  protected Boolean getParameterInputStreamClosedFlag(final RemoteInvocation invocation)
  {
    final ParameterInputStream pi = getParameterInputStreamFrom(invocation);
    if(pi != null) {
      return pi.isClosed() ? Boolean.TRUE : Boolean.FALSE;
    } else {
      return null;
    }
  }


  /**
   * Shields an underlying OutputStream from being closed.
   */
  public static class CloseShieldedOutputStream extends FilterOutputStream
  {
    public CloseShieldedOutputStream(final OutputStream out)
    {
      super(out);
    }

    public void close() throws IOException
    {
      flush();
    }
  }


  /**
   * Tracks if an InputStream parameter is closed by a service method, if
   * any input method threw an exception during operation, and if the
   * service method read the InputStream to the end-of-stream.  Also provides
   * the ability to optionally read an InputStream to end-of-stream if the
   * service method did not.
   */
  public static class ParameterInputStream extends FilterInputStream
  {
    private boolean fullyRead = false;
    private boolean erroredOut = false;
    private boolean closed = false;

    public ParameterInputStream(final InputStream in)
    {
      super(in);
    }

    public boolean isFullyRead()
    {
      return this.fullyRead;
    }

    public boolean isErroredOut()
    {
      return this.erroredOut;
    }

    public boolean isClosed()
    {
      return this.closed;
    }

    public void doRealClose(final boolean emptyStream) throws IOException
    {
      if(!isClosed()) {
        if(log.isDebugEnabled()) log.debug("Service method failed to close InputStream parameter from remote invocation.  Will perform the close anyway.");
      }
      if(!isFullyRead() && emptyStream && !isErroredOut()) {
        final byte[] buf = new byte[4096];
        //noinspection StatementWithEmptyBody
        while(read(buf) != -1) ;
      }

      super.close();
    }

    protected int checkEos(final int read)
    {
      if(read == -1) {
        this.fullyRead = true;
      }
      return read;
    }

    protected IOException checkException(final IOException ioe)
    {
      this.erroredOut = true;
      return ioe;
    }

    protected void assertOpen() throws IOException
    {
      if(this.closed) {
        throw new IOException("Stream closed");
      }
    }


    //
    // METHODS FROM CLASS FilterInputStream
    //

    public int read() throws IOException
    {
      assertOpen();
      try {
        return checkEos(super.read());
      } catch(IOException e) {
        throw checkException(e);
      }
    }

    public int read(byte b[]) throws IOException
    {
      assertOpen();
      try {
        return checkEos(super.read(b));
      } catch(IOException e) {
        throw checkException(e);
      }
    }

    public int read(byte b[], int off, int len) throws IOException
    {
      assertOpen();
      try {
        return checkEos(super.read(b, off, len));
      } catch(IOException e) {
        throw checkException(e);
      }
    }

    public long skip(long n) throws IOException
    {
      assertOpen();
      try {
        return super.skip(n);
      } catch(IOException e) {
        throw checkException(e);
      }
    }

    public int available() throws IOException
    {
      assertOpen();
      try {
        return super.available();
      } catch(IOException e) {
        throw checkException(e);
      }
    }

    public void close() throws IOException
    {
      // Close will happen later.
      this.closed = true;
    }
  }
}
