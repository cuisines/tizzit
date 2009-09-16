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
package org.tizzit.util.spring.httpinvoker;

import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;

/**
 * Extends <code>HttpInvokerProxyFactoryBean</code> to allow
 * <code>InputStream</code> parameters to remote service methods and
 * <code>InputStream</code> return values from remote service methods.
 * Stream content will be not be buffered and will be transmitted using the
 * &quot;chunked&quot; transfer encoding available in the HTTP protocol,
 * allowing any amount of data to be streamed to or from the service without
 * worrying about <code>OutOfMemoryErrors</code> (provided your servlet
 * container supports chunked encoding and does not attempt to fully buffer
 * streams).  If a service method has no InputStream parameters or return
 * values then the invocation will proceed in exactly the same fashion as the
 * standard HttpInvoker (the invocation will actually be delegated to the
 * underlying CommonsHttpInvoker implementation).  By default the system
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
 * This behavior can be changed by setting the
 * <code>matchOnMethodSignature</code> property of the
 * <code>StreamSupportingRemoteInvocationFactory</code> contained within the
 * <code>remoteInvocationFactory</code> property of this proxy factory bean to
 * <code>false</code>.  Note that setting this property to <code>false</code>
 * is an experimental feature and may not function properly.
 * 
 * <p><b>WARNING:</b> this is a simplistic implementation and so supports
 * only one InputStream parameter to a service method.  For example, these
 * method signatures are valid and supported:
 * <code><pre>
 * void method1(InputStream param1);
 * void method2(SomeSerializableType param1, InputStream param2);
 * MyType method3(InputStream param1, SomeSerializableType param2, ...);
 * InputStream method4(SomeType param1);
 * InputStream method5(InputStream param1);
 * InputStream method6(InputStream param1, SomeType param2);
 * </pre></code>
 * In other words, any combination of return type and parameters is acceptable
 * so long as there is only one <code>InputStream</code> parameter.  This
 * method signature would throw an exception upon invocation:
 * <code><pre>
 * void method7(InputStream param1, InputStream param2);
 * </pre></code>
 * <p>Also note that this implementation keeps track of whether the service
 * method explicitly closes the InputStream on the server side and then mirrors
 * that behavior on the client side.  In other words, if the service method
 * closes the InputStream parameter, then the InputStream that the client
 * code passed into the method on the client side will be closed as well,
 * otherwise the client side InputStream will still be "open" when the
 * service invocation returns. 
 * <p>Finally, configuration of this proxy factory bean is exactly the same
 * as for <code>HttpInvokerProxyFactoryBean</code>, except, of course, you
 * would reference the <code>StreamSupportingHttpInvokerProxyFactoryBean</code>
 * in your Spring &lt;bean ...&gt; declaration on the client side, and
 * <code>StreamSupportingHttpInvokerServiceExporter</code> on the server side.
 * Your service code can use the passed in <code>InputStream</code> parameter
 * just as if it were any other local (in-VM) <code>InputStream</code>.  It
 * can also create and return any InputStream, just as if it were a local
 * invocation.  The one exception is that the InputStream passed into the
 * service method will be a standard InputStream implementation.  If client
 * code passed in some custom InputStream implementation with additional
 * methods, then the server will not see that customized implementation or the
 * additional methods.  The only thing the service method will see is the raw
 * bytes that came from the stream.  The same applies going back the other way
 * as well (the return value from the service method).
 *
 * @author Andy DePue
 * @since 1.2.3
 *
 * @see StreamSupportingRemoteInvocationFactory#setMatchOnMethodSignature(boolean)
 * @see StreamSupportingHttpInvokerServiceExporter
 * @see HttpInvokerProxyFactoryBean
 * @see org.springframework.remoting.httpinvoker.HttpInvokerClientInterceptor
 * @see org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter
 * @see java.io.InputStream
 */
public class StreamSupportingHttpInvokerProxyFactoryBean
  extends HttpInvokerProxyFactoryBean
{
  public StreamSupportingHttpInvokerProxyFactoryBean()
  {
    setHttpInvokerRequestExecutor(new StreamSupportingHttpInvokerRequestExecutor());
    setRemoteInvocationFactory(new StreamSupportingRemoteInvocationFactory());
  }
}
