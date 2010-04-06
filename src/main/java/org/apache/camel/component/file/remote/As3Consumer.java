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

import java.util.List;

import org.apache.camel.Processor;
import org.apache.camel.component.file.GenericFile;
import org.apache.camel.util.FileUtil;
import org.apache.camel.util.ObjectHelper;
import org.jets3t.service.model.S3Object;

public class As3Consumer extends RemoteFileConsumer<S3Object> {
	protected String endpointPath;

	public As3Consumer(RemoteFileEndpoint<S3Object> endpoint,
			Processor processor, RemoteFileOperations<S3Object> operations) {
		super(endpoint, processor, operations);
		this.endpointPath = endpoint.getConfiguration().getDirectory();
	}

	@Override
	protected void pollDirectory(String fileName,
			List<GenericFile<S3Object>> fileList) {
		if (fileName == null) {
			return;
		}

		// remove trailing /
		fileName = FileUtil.stripTrailingSeparator(fileName);

		if (log.isTraceEnabled()) {
			log.trace("Polling directory: " + fileName);
		}
		List<S3Object> files = operations.listFiles(fileName);
		for (S3Object file : files) {
			if (file.getDataInputFile().isDirectory()) {
				RemoteFile<S3Object> remote = asRemoteFile(fileName, file);
				if (endpoint.isRecursive() && isValidFile(remote, true)) {
					// recursive scan and add the sub files and folders
					// String directory = fileName + "/" + file.getName();
					String directory = fileName + "/"
							+ file.getDataInputFile().getName();
					pollDirectory(directory, fileList);
				}
				// } else if (file.isFile()) {
			} else if (file.getDataInputFile().isFile()) {
				RemoteFile<S3Object> remote = asRemoteFile(fileName, file);
				if (isValidFile(remote, false)) {
					if (isInProgress(remote)) {
						if (log.isTraceEnabled()) {
							log
									.trace("Skipping as file is already in progress: "
											+ remote.getFileName());
						}
					} else {
						// matched file so add
						fileList.add(remote);
					}
				}
			} else {
				log.debug("Ignoring unsupported remote file type: " + file);
			}
		}
	}

	private RemoteFile<S3Object> asRemoteFile(String directory, S3Object file) {
		RemoteFile<S3Object> answer = new RemoteFile<S3Object>();

		answer.setEndpointPath(endpointPath);
		answer.setFile(file);
		// answer.setFileName(file.getName());
		answer.setFileName(file.getDataInputFile().getName());
		// answer.setFileNameOnly(file.getName());
		answer.setFileNameOnly(file.getDataInputFile().getName());
		// answer.setFileLength(file.getSize());
		answer.setFileLength(file.getDataInputFile().getTotalSpace());
		// if (file.getTimestamp() != null) {
		if (file.getLastModifiedDate() != null) {
			// answer.setLastModified(file.getTimestamp().getTimeInMillis());
			answer.setLastModified(file.getLastModifiedDate().getTime());
		}
		answer.setHostname(((RemoteFileConfiguration) endpoint
				.getConfiguration()).getHost());

		// all ftp files is considered as relative
		answer.setAbsolute(false);

		// create a pseudo absolute name
		// String absoluteFileName = (ObjectHelper.isNotEmpty(directory) ?
		// directory + "/" : "") + file.getName();
		String absoluteFileName = (ObjectHelper.isNotEmpty(directory) ? directory
				+ "/"
				: "")
				+ file.getDataInputFile().getName();
		answer.setAbsoluteFilePath(absoluteFileName);

		// the relative filename, skip the leading endpoint configured path
		String relativePath = ObjectHelper
				.after(absoluteFileName, endpointPath);
		// skip leading /
		relativePath = FileUtil.stripLeadingSeparator(relativePath);
		answer.setRelativeFilePath(relativePath);

		return answer;
	}

}
