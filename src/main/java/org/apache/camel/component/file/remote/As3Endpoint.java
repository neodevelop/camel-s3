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

import org.apache.camel.FailedToCreateConsumerException;
import org.apache.camel.Processor;
import org.apache.camel.component.file.GenericFileProducer;
import org.jets3t.service.model.S3Object;

public class As3Endpoint<T extends S3Object> extends
		RemoteFileEndpoint<S3Object> {

	public As3Endpoint() {
	}

	public As3Endpoint(String uri, RemoteFileComponent<S3Object> component,
			RemoteFileConfiguration configuration) {
		super(uri, component, configuration);
	}

	@Override
	protected RemoteFileConsumer<S3Object> buildConsumer(Processor processor) {
		try {
            return new As3Consumer(this, processor, createRemoteFileOperations());
        } catch (Exception e) {
            throw new FailedToCreateConsumerException(this, e);
        }
	}

	@Override
	protected GenericFileProducer<S3Object> buildProducer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScheme() {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected RemoteFileOperations<S3Object> createRemoteFileOperations() throws Exception {
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
}
