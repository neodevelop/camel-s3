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

import java.io.InputStream;
import java.util.Properties;

import org.apache.camel.test.junit4.CamelTestSupport;
import org.jets3t.service.StorageService;
import org.jets3t.service.acl.AccessControlList;
import org.jets3t.service.impl.rest.httpclient.RestStorageService;
import org.jets3t.service.model.StorageBucket;
import org.jets3t.service.model.StorageObject;
import org.jets3t.service.security.ProviderCredentials;

public abstract class As3TestSupport extends CamelTestSupport {
	public static final String TARGET_SERVICE_S3 = "AmazonS3";
	public static final String TARGET_SERVICE_GS = "GoogleStorage";

	protected String TEST_PROPERTIES_FILENAME = "test.properties";
	protected Properties testProperties = null;

	public As3TestSupport() throws Exception {
		// Load test properties
		InputStream propertiesIS = ClassLoader
				.getSystemResourceAsStream(TEST_PROPERTIES_FILENAME);
		if (propertiesIS == null) {
			throw new Exception(
					"Unable to load test properties file from classpath: "
							+ TEST_PROPERTIES_FILENAME);
		}
		this.testProperties = new Properties();
		this.testProperties.load(propertiesIS);
	}

	protected abstract ProviderCredentials getCredentials() throws Exception;

	protected abstract RestStorageService getStorageService(
			ProviderCredentials credentials) throws Exception;

	protected abstract String getTargetService();

	protected abstract AccessControlList buildAccessControlList();

	/**
	 * @param testName
	 * @return unique per-account and per-test bucket name
	 */
	protected String getBucketNameForTest(String testName) throws Exception {
		return "test-" + getCredentials().getAccessKey().toLowerCase() + "-"
				+ testName.toLowerCase();
	}

	protected StorageBucket createBucketForTest(String testName)
			throws Exception {
		String bucketName = getBucketNameForTest(testName);
		StorageService service = getStorageService(getCredentials());
		return service.getOrCreateBucket(bucketName);
	}

	protected void deleteAllObjectsInBucket(String bucketName) {
		try {
			RestStorageService service = getStorageService(getCredentials());
			for (StorageObject o : service.listObjects(bucketName)) {
				service.deleteObject(bucketName, o.getKey());
			}
		} catch (Exception e) {
			// This shouldn't happen, but if it does don't ruin the test
			e.printStackTrace();
		}
	}

	protected void cleanupBucketForTest(String testName,
			boolean deleteAllObjects) {
		try {
			RestStorageService service = getStorageService(getCredentials());
			String bucketName = getBucketNameForTest(testName);

			if (deleteAllObjects) {
				deleteAllObjectsInBucket(bucketName);
			}

			service.deleteBucket(bucketName);
		} catch (Exception e) {
			// This shouldn't happen, but if it does don't ruin the test
			e.printStackTrace();
		}
	}

	protected void cleanupBucketForTest(String testName) {
		this.cleanupBucketForTest(testName, true);
	}

}
