package com.atlassian.jira.rest.client.api.domain.input;

import org.joda.time.DateTime;



/**
 * Input data for searching audit records
 *
 * @since v2.0.0
 */
public class AuditRecordSearchInput {


    private final Integer offset;

    private final Integer limit;

    private final String textFilter;

    private final DateTime from;

    private final DateTime to;

    public AuditRecordSearchInput(final Integer offset, final Integer limit, final String textFilter, final DateTime from, final DateTime to) {
        this.offset = offset;
        this.limit = limit;
        this.textFilter = textFilter;
        this.from = from;
        this.to = to;
    }


    public Integer getOffset() {
        return offset;
    }


    public Integer getLimit() {
        return limit;
    }


    public String getTextFilter() {
        return textFilter;
    }


    public DateTime getFrom() {
        return from;
    }


    public DateTime getTo() {
        return to;
    }
}
