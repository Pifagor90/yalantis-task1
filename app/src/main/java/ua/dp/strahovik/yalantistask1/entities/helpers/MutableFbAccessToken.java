package ua.dp.strahovik.yalantistask1.entities.helpers;


import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;

import java.util.Date;
import java.util.Set;

public class MutableFbAccessToken {
    private Date expires;
    private Set<String> permissions;
    private Set<String> declinedPermissions;
    private String token;
    private AccessTokenSource source;
    private Date lastRefresh;
    private String applicationId;
    private String userId;

    public AccessToken getAccessToken() {
        return new AccessToken(token, applicationId, userId,
                permissions, declinedPermissions, source, expires, lastRefresh);
    }


    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public Set<String> getDeclinedPermissions() {
        return declinedPermissions;
    }

    public void setDeclinedPermissions(Set<String> declinedPermissions) {
        this.declinedPermissions = declinedPermissions;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AccessTokenSource getSource() {
        return source;
    }

    public void setSource(AccessTokenSource source) {
        this.source = source;
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
