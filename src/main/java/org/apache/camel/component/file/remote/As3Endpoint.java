/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.file.remote;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.component.file.GenericFileComponent;
import org.apache.camel.component.file.GenericFileConfiguration;
import org.apache.camel.component.file.GenericFileConsumer;
import org.apache.camel.component.file.GenericFileEndpoint;
import org.apache.camel.component.file.GenericFileOperations;
import org.apache.camel.component.file.GenericFileProducer;
import org.jets3t.service.model.S3Object;

public class As3Endpoint<T extends S3Object> extends GenericFileEndpoint<S3Object> {

	private GenericFileConfiguration configuration;

	public As3Endpoint() {
		super();
	}

	public As3Endpoint(String uri, GenericFileComponent<S3Object> component,
			GenericFileConfiguration configuration) {
		super(uri, component);
		this.configuration = configuration;
	}

	@Override
	public String getScheme() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected GenericFileOperations<S3Object> createRemoteFileOperations() throws Exception {
        /*
        // configure ftp client
        FTPClient client = ftpClient;
        
        if (client == null) {
            // must use a new client if not explicit configured to use a custom client
            client = createFtpClient();
        }

        if (ftpClientParameters != null) {
            IntrospectionSupport.setProperties(client, ftpClientParameters);
        }
        
        if (ftpClientConfigParameters != null) {
            // client config is optional so create a new one if we have parameter for it
            if (ftpClientConfig == null) {
                ftpClientConfig = new FTPClientConfig();
            }
            IntrospectionSupport.setProperties(ftpClientConfig, ftpClientConfigParameters);
        }

        FtpOperations operations = new FtpOperations(client, getFtpClientConfig());
        operations.setEndpoint(this);
        return operations;
        */
		return new As3Operations();
    }

	/**
     * Used by FtpComponent to provide additional parameters for the FTPClient
     */
    void setFtpClientParameters(Map<String, Object> ftpClientParameters) {
        //this.ftpClientParameters = ftpClientParameters;
    }

    /**
     * Used by FtpComponent to provide additional parameters for the FTPClientConfig
     */
    void setFtpClientConfigParameters(Map<String, Object> ftpClientConfigParameters) {
        //this.ftpClientConfigParameters = ftpClientConfigParameters;
    }

	@Override
	public GenericFileConsumer<S3Object> createConsumer(Processor arg0)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Exchange createExchange(GenericFile<S3Object> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericFileProducer<S3Object> createProducer() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char getFileSeparator() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isAbsolute(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
