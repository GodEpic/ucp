package com.yum.ucp.modules.common.entity;

import com.atlassian.jira.rest.client.api.domain.RoleActor;
import com.atlassian.jira.rest.client.api.domain.Transition;

import java.util.List;

/**
 * Created by Administrator on 2016/12/15.
 */
public class IssueResponse {
    private String key;
    private List<Transition> transitionIterator;
    private List<RoleActor> roleActors;
    public    IssueResponse( String key,List<Transition> transitionIterator,List<RoleActor> roleActors) {
        this.key = key;
        this.transitionIterator = transitionIterator;
        this.roleActors = roleActors;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Transition> getTransitionIterator() {
        return transitionIterator;
    }

    public void setTransitionIterator(List<Transition> transitionIterator) {
        this.transitionIterator = transitionIterator;
    }

    public List<RoleActor> getRoleActors() {
        return roleActors;
    }

    public void setRoleActors(List<RoleActor> roleActors) {
        this.roleActors = roleActors;
    }
}
