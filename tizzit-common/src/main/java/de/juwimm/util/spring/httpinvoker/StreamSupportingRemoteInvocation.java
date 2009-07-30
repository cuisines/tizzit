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
import java.lang.reflect.InvocationTargetException;

import org.springframework.remoting.support.RemoteInvocation;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Decorates a <code>RemoteInvocation</code> to provide additional information
 * concerning <code>InputStream</code> parameters.  Note that the decorator
 * pattern was employed so that the stream support could be mixed with other
 * <code>RemoteInvocation</code> implementations (which may provide other
 * aspects, such as remote transaction propagation, etc).  Also, a custom
 * <code>RemoteInvocation</code> class was used instead of simply setting
 * attributes as we needed to make use of transient fields.
 * 
 * @author Andy DePue
 * @since 1.2.3
 * @see RemoteInvocationDecorator
 * @see RemoteInvocation 
 */
public class StreamSupportingRemoteInvocation extends RemoteInvocationDecorator
{
  private transient InputStream clientSideInputStream;
  private int inputStreamParam;
  private boolean inputStreamParamNull;

  public StreamSupportingRemoteInvocation()
  {
  }

  public StreamSupportingRemoteInvocation(final RemoteInvocation source)
  {
    super(source);
  }

  public static boolean isInputStreamInvocation(final MethodInvocation mi, final boolean matchOnMethodSignature)
  {
    return InputStream.class.isAssignableFrom(mi.getMethod().getReturnType()) ||
           getInputStreamParam(mi.getMethod().getParameterTypes(), mi.getArguments(), matchOnMethodSignature) != -1;
  }


  public static int getInputStreamParam(final Class[] paramTypes,
                                        final Object[] params,
                                        final boolean matchOnMethodSignature)
  {
    int inputStreamParam = -1;
    for(int i = paramTypes.length - 1;i >= 0;i--) {
      if((matchOnMethodSignature && InputStream.class.isAssignableFrom(paramTypes[i])) ||
         (!matchOnMethodSignature && params[i] instanceof InputStream)) {
        if(inputStreamParam == -1) {
          inputStreamParam = i;
        } else {
          throw new UnsupportedOperationException("Remote invocation supports at most 1 InputStream parameters.");
        }
      }
    }
    
    return inputStreamParam;
  }

  public void setupInputStreamParam(final boolean matchOnMethodSignature)
  {
    final Class[] paramTypes = getParameterTypes();
    final Object[] params = getArguments();
    if(paramTypes != null) {
      final int inputStreamParam = getInputStreamParam(paramTypes, params,
                                                       matchOnMethodSignature);
      setInputStreamParam(inputStreamParam);
      if(inputStreamParam != -1) {
        if(paramTypes[inputStreamParam] == null) {
          setInputStreamParamNull(true);
        } else {
          setInputStreamParamNull(false);
          setClientSideInputStream((InputStream)params[inputStreamParam]);
          params[inputStreamParam] = null;
        }
      }
    }
  }

  public Object invoke(final Object targetObject, final InputStream serverSideInputStream)
      throws IOException, IllegalAccessException, NoSuchMethodException, InvocationTargetException
  {
    if(getInputStreamParam() != -1 && !isInputStreamParamNull()) {
      final Object[] params = getArguments();
      params[getInputStreamParam()] = serverSideInputStream;
    } else {
      serverSideInputStream.close();
    }
    return invoke(targetObject);
  }


  //
  // SIMPLE PROPERTY ACCESSORS
  //

  public InputStream getClientSideInputStream()
  {
    return this.clientSideInputStream;
  }

  public void setClientSideInputStream(final InputStream clientSideInputStream)
  {
    this.clientSideInputStream = clientSideInputStream;
  }

  public int getInputStreamParam()
  {
    return this.inputStreamParam;
  }

  protected void setInputStreamParam(final int inputStreamParam)
  {
    this.inputStreamParam = inputStreamParam;
  }

  public boolean isInputStreamParamNull()
  {
    return this.inputStreamParamNull;
  }

  protected void setInputStreamParamNull(final boolean inputStreamParamNull)
  {
    this.inputStreamParamNull = inputStreamParamNull;
  }
}
