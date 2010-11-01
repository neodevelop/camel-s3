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

import org.jets3t.service.Constants;
import org.jets3t.service.Jets3tProperties;
import org.jets3t.service.ServiceException;
import org.jets3t.service.acl.AccessControlList;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.impl.rest.httpclient.RestStorageService;
import org.jets3t.service.security.AWSCredentials;
import org.jets3t.service.security.ProviderCredentials;

public abstract class RestS3ServiceToAmazonStorage extends As3TestSupport {
	public RestS3ServiceToAmazonStorage() throws Exception {
		super();
	}

	@Override
	protected String getTargetService() {
		return TARGET_SERVICE_S3;
	}

	@Override
	protected ProviderCredentials getCredentials() {
		return new AWSCredentials(testProperties.getProperty("aws.accesskey"),
				testProperties.getProperty("aws.secretkey"));
	}

	@Override
	protected RestStorageService getStorageService(
			ProviderCredentials credentials) throws ServiceException {
		Jets3tProperties properties = new Jets3tProperties();
		properties.setProperty("s3service.s3-endpoint",
				Constants.S3_DEFAULT_HOSTNAME);
		return new RestS3Service(credentials, null, null, properties);
	}

	@Override
	protected AccessControlList buildAccessControlList() {
		return new AccessControlList();
	}
}
