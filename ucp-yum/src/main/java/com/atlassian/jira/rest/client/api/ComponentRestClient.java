/*
 * Copyright (C) 2010 Atlassian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.atlassian.jira.rest.client.api;

import com.atlassian.jira.rest.client.api.domain.Component;
import com.atlassian.jira.rest.client.api.domain.input.ComponentInput;
import com.atlassian.util.concurrent.Promise;

import java.net.URI;

/**
 * The com.atlassian.jira.rest.client.api handling component resources
 *
 * @since v2.0
 */
public interface ComponentRestClient {
	/**
	 * @param componentUri URI to selected component resource
	 * @return complete information about selected component
	 * @throws RestClientException in case of problems (connectivity, malformed messages, etc.)
	 */
	Promise<Component> getComponent(URI componentUri);

	Promise<Component> createComponent(String projectKey, ComponentInput componentInput);

	Promise<Component> updateComponent(URI componentUri, ComponentInput componentInput);

	Promise<Void> removeComponent(URI componentUri,  URI moveIssueToComponentUri);

	Promise<Integer> getComponentRelatedIssuesCount(URI componentUri);
}
