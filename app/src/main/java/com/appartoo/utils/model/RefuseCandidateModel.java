package com.appartoo.utils.model;

/**
 * Created by alexandre on 16-08-25.
 */

public class RefuseCandidateModel {
    private String profile_id;
    private String conversationId;

    public RefuseCandidateModel(String profile_id, String conversationId) {
        this.profile_id = profile_id;
        this.conversationId = conversationId;
    }

    public String getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }


}
