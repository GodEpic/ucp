package com.atlassian.jira.rest.client.api.domain.input;

import com.atlassian.jira.rest.client.api.domain.AuditAssociatedItem;
import com.atlassian.jira.rest.client.api.domain.AuditChangedValue;
import com.google.common.base.Objects;

/**
 * Represents record from JIRA Audit Log.
 *
 * @since v2.0
 */
public class AuditRecordInput {

    private final String summary;

    private final String category;


    private final AuditAssociatedItem objectItem;


    private final Iterable<AuditAssociatedItem> associatedItem;


    private final Iterable<AuditChangedValue> changedValues;

    public AuditRecordInput(final String category, final String summary,
                        final AuditAssociatedItem objectItem,
                        final Iterable<AuditAssociatedItem> associatedItem,
                        final Iterable<AuditChangedValue> changedValues) {
        this.summary = summary;
        this.category = category;
        this.objectItem = objectItem;
        this.associatedItem = associatedItem;
        this.changedValues = changedValues;
    }

    public String getSummary() {
        return summary;
    }

    public String getCategory() {
        return category;
    }

    public AuditAssociatedItem getObjectItem() {
        return objectItem;
    }

    public Iterable<AuditAssociatedItem> getAssociatedItems() {
        return associatedItem;
    }

    public Iterable<AuditChangedValue> getChangedValues() {
        return changedValues;
    }

    protected Objects.ToStringHelper getToStringHelper() {
        return Objects.toStringHelper(this).
                add("summary", summary).
                add("category", category).
                add("objectItem", objectItem).
                add("associatedItem", associatedItem).
                add("changedValues", changedValues);
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof AuditRecordInput) {
            final AuditRecordInput that = (AuditRecordInput) o;
            return Objects.equal(this.summary, that.summary)
                    && Objects.equal(this.category, that.category)
                    && Objects.equal(this.objectItem, that.objectItem)
                    && Objects.equal(this.associatedItem, that.associatedItem)
                    && Objects.equal(this.changedValues, that.changedValues);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(summary, category, objectItem, associatedItem, changedValues);
    }

}
