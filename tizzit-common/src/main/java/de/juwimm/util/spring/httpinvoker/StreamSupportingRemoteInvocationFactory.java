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

import org.springframework.remoting.support.RemoteInvocationFactory;
import org.springframework.remoting.support.DefaultRemoteInvocationFactory;
import org.springframework.remoting.support.RemoteInvocation;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Decorates another <code>RemoteInvocationFactory</code> to add stream
 * support to the invocation.  See both
 * <code>StreamSupportingHttpInvokerProxyFactoryBean</code> and
 * <code>StreamSupportingRemoteInvocation</code> for more detail.  If no
 * specific <code>RemoteInvocationFactory</code> delegate is set on this
 * decorator, then a new instance of
 * <code>DefaultRemoteInvocationFactory</code> will be instantiated and used.
 * 
 * @author Andy DePue
 * @since 1.2.3
 * @see StreamSupportingHttpInvokerProxyFactoryBean
 * @see StreamSupportingRemoteInvocation
 * @see RemoteInvocationFactory
 * @see DefaultRemoteInvocationFactory
 * @see RemoteInvocationDecorator
 * @see RemoteInvocation 
 */
public class StreamSupportingRemoteInvocationFactory
  implements RemoteInvocationFactory
{
  private RemoteInvocationFactory delegate = new DefaultRemoteInvocationFactory();
  private boolean matchOnMethodSignature = true;

  //
  // METHODS FROM INTERFACE RemoteInvocationFactory
  //

  public RemoteInvocation createRemoteInvocation(final MethodInvocation methodInvocation)
  {
    final RemoteInvocation source = getDelegate().createRemoteInvocation(methodInvocation);
    if(StreamSupportingRemoteInvocation.isInputStreamInvocation(methodInvocation, getMatchOnMethodSignature())) {
      final StreamSupportingRemoteInvocation ret = new StreamSupportingRemoteInvocation(source);
      ret.setupInputStreamParam(getMatchOnMethodSignature());
      return ret;
    } else {
      return source;
    }
  }
  
  
  


  //
  // SIMPLE PROPERTY ACCESSORS
  //

  public RemoteInvocationFactory getDelegate()
  {
    return this.delegate;
  }

  public void setDelegate(final RemoteInvocationFactory delegate)
  {
    this.delegate = delegate;
  }

  public boolean getMatchOnMethodSignature()
  {
    return this.matchOnMethodSignature;
  }

  /**
   * Configures how the system will determine whether a service method should
   * receive streaming support.
   * If a service method has no InputStream parameters or return
   * values then the invocation will proceed in exactly the same fashion as
   * the standard HttpInvoker (the invocation will actually be delegated to
   * the underlying CommonsHttpInvoker implementation).  By default the system
   * will determine if a service method should be invoked with streaming
   * support based on the signature of the method in the service interface,
   * not on the actual parameters passed into a particular invocation.  For
   * example, this method:
   * <code><pre>
   * void method(Object param1);
   * </pre></code>
   * will not have streaming support, even if client code invoked it like so:
   * <code><pre>
   * InputStream in = ...;
   * service.method(in);
   * </pre></code>
   * <p><b>WARNING:</b> setting this property to <code>false</code> is an
   * experimental feature and may not function properly. 
   *
   * @param matchOnMethodSignature <code>false</code> to match based on the
   *                               parameter types being passed into a remote
   *                               service invocation.  <code>true</code> to
   *                               match based on the signature of the
   *                               service interface.
   */
  public void setMatchOnMethodSignature(final boolean matchOnMethodSignature)
  {
    this.matchOnMethodSignature = matchOnMethodSignature;
  }


}
