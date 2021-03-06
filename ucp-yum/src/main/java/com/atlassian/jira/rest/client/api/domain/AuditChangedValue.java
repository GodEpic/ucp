package com.atlassian.jira.rest.client.api.domain;

import com.google.common.base.Objects;




/**
 * Represents a value that has changed in object related to Audit Record.
 *
 * @since v2.0
 */
public class AuditChangedValue {

    private final String fieldName;


    private final String changedTo;


    private final String changedFrom;

    public AuditChangedValue(final String fieldName,  final String changedTo,  final String changedFrom) {
        this.fieldName = fieldName;
        this.changedTo = changedTo;
        this.changedFrom = changedFrom;
    }

    public String getFieldName() {
        return fieldName;
    }


    public String getChangedTo() {
        return changedTo;
    }


    public String getChangedFrom() {
        return changedFrom;
    }

    protected Objects.ToStringHelper getToStringHelper() {
        return Objects.toStringHelper(this).
                add("fieldName", fieldName).
                add("changedFrom", changedFrom).
                add("changedTo", changedTo);
    }

    @Override
    public boolean equals(final Object o) {
        if (o instanceof AuditChangedValue) {
            final AuditChangedValue that = (AuditChangedValue) o;
            return  Objects.equal(this.fieldName, that.fieldName)
                    && Objects.equal(this.changedFrom, that.changedFrom)
                    && Objects.equal(this.changedTo, that.changedTo);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fieldName, changedFrom, changedTo);
    }

}
