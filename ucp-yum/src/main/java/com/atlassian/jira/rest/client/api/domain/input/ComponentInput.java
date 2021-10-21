/*
 * Copyright (C) 2011 Atlassian
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

package com.atlassian.jira.rest.client.api.domain.input;

import com.atlassian.jira.rest.client.api.domain.AssigneeType;



/**
 * Details about project component to be created or updated;
 *
 * @since com.atlassian.jira.rest.client.api 0.3, server 4.4
 */
public class ComponentInput {

	private final String name;

	private final String description;

	private final String leadUsername;

	private final AssigneeType assigneeType;

	public ComponentInput( String name,  String description,  String leadUsername,  AssigneeType assigneeType) {
		this.name = name;
		this.description = description;
		this.leadUsername = leadUsername;
		this.assigneeType = assigneeType;
	}


	public String getName() {
		return name;
	}


	public String getDescription() {
		return description;
	}


	public String getLeadUsername() {
		return leadUsername;
	}


	public AssigneeType getAssigneeType() {
		return assigneeType;
	}

	//	private final String projectKey;
}
