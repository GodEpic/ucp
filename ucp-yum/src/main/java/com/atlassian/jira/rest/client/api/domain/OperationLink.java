/*
 * Copyright (C) 2014 Atlassian
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

package com.atlassian.jira.rest.client.api.domain;

import com.google.common.base.Objects;
import com.google.common.base.Optional;



/**
 * Represents operations link
 *
 * @since 2.0
 */
public class OperationLink implements Operation {
	 private final String id;
	 private final String styleClass;
	private final String label;
	 private final String title;
	private final String href;
	 private final Integer weight;
	 private final String iconClass;

	public OperationLink( final String id,  final String styleClass, final String label,  final String title,
			final String href,  final Integer weight,  final String iconClass) {
		this.id = id;
		this.styleClass = styleClass;
		this.iconClass = iconClass;
		this.label = label;
		this.title = title;
		this.href = href;
		this.weight = weight;
	}


	@Override
	public String getId() {
		return id;
	}

	@Override
	public <T> Optional<T> accept(final OperationVisitor<T> visitor) {
		return visitor.visit(this);
	}


	public String getStyleClass() {
		return styleClass;
	}

	public String getLabel() {
		return label;
	}


	public String getTitle() {
		return title;
	}

	public String getHref() {
		return href;
	}


	public Integer getWeight() {
		return weight;
	}


	public String getIconClass() {
		return iconClass;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof OperationLink) {
			OperationLink that = (OperationLink) o;
			return Objects.equal(id, that.id)
					&& Objects.equal(styleClass, that.styleClass)
					&& Objects.equal(label, that.label)
					&& Objects.equal(title, that.title)
					&& Objects.equal(href, that.href)
					&& Objects.equal(weight, that.weight)
					&& Objects.equal(iconClass, that.iconClass);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id, styleClass, label, title, href, weight, iconClass);
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("id", id)
				.add("styleClass", styleClass)
				.add("label", label)
				.add("title", title)
				.add("href", href)
				.add("weight", weight)
				.add("iconClass", iconClass)
				.toString();
	}
}
