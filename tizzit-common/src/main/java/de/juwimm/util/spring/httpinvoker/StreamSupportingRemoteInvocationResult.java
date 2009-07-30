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

import org.springframework.remoting.support.RemoteInvocationResult;

/**
 * Adds support for <code>InputStream</code> return values from remote
 * service methods.  Also provides information to the remote caller on
 * whether the remote service method closed any InputStream parameter.
 * 
 * @author Andy DePue
 * @since 1.2.3
 * @see StreamSupportingHttpInvokerProxyFactoryBean
 * @see RemoteInvocationResult
 */
public class StreamSupportingRemoteInvocationResult extends RemoteInvocationResult
{
  private transient InputStream serverSideInputStream;
  private transient InputStream clientSideInputStream;
  private boolean hasReturnStream;
  private Boolean methodClosedParamInputStream;

  public StreamSupportingRemoteInvocationResult(final Object returnValue, final Boolean closedParamInputStream)
  {
    super(returnValue);
    this.serverSideInputStream = null;
    this.hasReturnStream = false;
    this.methodClosedParamInputStream = closedParamInputStream;
  }

  public StreamSupportingRemoteInvocationResult(final InputStream serverSideInputStream, final Boolean closedParamInputStream)
  {
    super((Object)null);
    this.serverSideInputStream = serverSideInputStream;
    this.hasReturnStream = true;
    this.methodClosedParamInputStream = closedParamInputStream;
  }

  public StreamSupportingRemoteInvocationResult(final Throwable exception, final Boolean closedParamInputStream)
  {
    super(exception);
    this.serverSideInputStream = null;
    this.hasReturnStream = false;
    this.methodClosedParamInputStream = closedParamInputStream;
  }
  
  public InputStream getClientSideInputStream()
  {
    return this.clientSideInputStream;
  }

  public void setClientSideInputStream(final InputStream clientSideInputStream)
  {
    this.clientSideInputStream = clientSideInputStream;
  }

  public InputStream getServerSideInputStream()
  {
    return this.serverSideInputStream;
  }

  public void setServerSideInputStream(final InputStream serverSideInputStream)
  {
    this.serverSideInputStream = serverSideInputStream;
  }

  public boolean getHasReturnStream()
  {
    return this.hasReturnStream;
  }

  public Boolean getMethodClosedParamInputStream()
  {
    return this.methodClosedParamInputStream;
  }

  public Object recreate() throws Throwable
  {
    // Throw an exception if this represents one.
    final Object superObject = super.recreate();
    
    if(getHasReturnStream()) {
      return getClientSideInputStream();
    } else {
      return superObject;
    }
  }
}
