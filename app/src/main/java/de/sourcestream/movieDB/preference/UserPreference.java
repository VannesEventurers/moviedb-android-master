package de.sourcestream.movieDB.preference;

import android.content.Context;
import android.content.SharedPreferences;

import de.sourcestream.movieDB.utils.TextUtils;

public class UserPreference {

    private static final String PREF_NAME = "user_preference";
    private static final String CITY_LANE_VIDEO_PATH = "city_lane_video-path";
    private static final String EMAIL = "email";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String AZURE_ID = "azure_id";
    private static final String FACEBOOK_ID = "facebook_id";
    private static final String GOOGLE_PLUS_ID = "google_plus_id";
    private static final String TWITTER_ID = "twitter_id";
    private static final String LINKEDIN_ID = "linkedin_id";
    private static final String USER_ID = "user_id";
    private static final String AUTHENTICATED_VIA = "authenticated_via";
    private static final String MOBILE_APP_AUTHENTICATED = "mobile_app_authenticated";
    private static final String MOBILE_APP_AUTHENTICATED_VIA = "mobile_app_authenticated_via";
    private static final String ACCESS_TOKEN="access_token";
    private static final String REFERSH_TOKEN="refresh_token";
    private static final String GCM_ACCESS_TOKEN_RESPONSE="gcm_response";
    private static final String HOME_LAUNCHED="home_launched";
    private static final String SELECTED_LINK="selected_link";
    private static final String AGENT_ID="agent_id";
    private static final String INITIAL_STATUS="initial_status";

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    public UserPreference(Context context) {
        mPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }

    public void logout() {
        mEditor.clear();
        mEditor.commit();
    }
    public void setHomeLaunched(boolean status){
        mEditor.putBoolean(HOME_LAUNCHED,status);
        mEditor.commit();
    }

    public boolean getHomeLaunched(){
        return mPreferences.getBoolean(HOME_LAUNCHED,false);
    }

    public void setAccessToken(String accessToken){
        mEditor.putString(ACCESS_TOKEN,accessToken);
        mEditor.commit();
    }

    public String getAccessToken(){
        return mPreferences.getString(ACCESS_TOKEN,null);
    }

    public void setRefershToken(String refershToken){
        mEditor.putString(REFERSH_TOKEN,refershToken);
        mEditor.commit();
    }

    public String getRefershToken(){
        return mPreferences.getString(REFERSH_TOKEN,null);
    }

    public void setGcmAccessTokenResponse(String gcmAccessTokenResponse){
        mEditor.putString(GCM_ACCESS_TOKEN_RESPONSE,gcmAccessTokenResponse);
        mEditor.commit();
    }

    public String getGcmAccessTokenResponse(){
        return mPreferences.getString(GCM_ACCESS_TOKEN_RESPONSE,null);
    }

    public void setVideoPath(String path) {
        mEditor.putString(CITY_LANE_VIDEO_PATH, path);
        mEditor.commit();
    }

    public String getVideoPath() {
        return mPreferences.getString(CITY_LANE_VIDEO_PATH, null);
    }

    public void setDefaultAuthentication(String firstName, String lastName, String email, String azureId, String fbId, String googlePlusId, String twitterId) {
        mEditor.putString(EMAIL, email);
        mEditor.putString(FIRST_NAME, firstName);
        mEditor.putString(LAST_NAME, lastName);
        mEditor.putString(FACEBOOK_ID,fbId);
        mEditor.putString(GOOGLE_PLUS_ID,googlePlusId);
        mEditor.putString(TWITTER_ID,twitterId);
        mEditor.putString(AZURE_ID, azureId);
        mEditor.commit();
    }

    public void setDefaultAuthentication(String firstName, String lastName, String email, String azureId, String fbId, String googlePlusId, String twitterId, String linkedinId) {
        mEditor.putString(EMAIL, email);
        mEditor.putString(FIRST_NAME, firstName);
        mEditor.putString(LAST_NAME, lastName);
        mEditor.putString(FACEBOOK_ID,fbId);
        mEditor.putString(GOOGLE_PLUS_ID,googlePlusId);
        mEditor.putString(TWITTER_ID,twitterId);
        mEditor.putString(LINKEDIN_ID, linkedinId);
        mEditor.putString(AZURE_ID, azureId);
        mEditor.commit();
    }

    public boolean hasDefaultAuthentication() {
        return !TextUtils.isEmpty(getFirstName());
    }

    public String getFirstName() {
        return mPreferences.getString(FIRST_NAME, "");
    }

    public String getLastName() {
        return mPreferences.getString(LAST_NAME, "");
    }

    public String getEmail() {
        return mPreferences.getString(EMAIL, "");
    }

    public String getFacebookId() {
        return mPreferences.getString(FACEBOOK_ID, "");
    }

    public String getGooglePlusId() {
        return mPreferences.getString(GOOGLE_PLUS_ID, "");
    }

    public String getTwitterId() {
        return mPreferences.getString(TWITTER_ID, "");
    }

    public String getLinkedinId(){
        return mPreferences.getString(LINKEDIN_ID, "");
    }

    public String getAzureId() {
        return mPreferences.getString(AZURE_ID, "");
    }

    public void setUserId(String userId) {
        mEditor.putString(USER_ID, userId);
        mEditor.commit();
    }

    public String getUserId() {
        return mPreferences.getString(USER_ID, "");
    }

    public void setAuthenticatedVia(String authenticatedVia) {
        mEditor.putString(AUTHENTICATED_VIA, authenticatedVia);
        mEditor.commit();
    }

    public String getAuthenticatedVia() {
        return mPreferences.getString(AUTHENTICATED_VIA, "");
    }

    public void setMobileAppAuthenticated(boolean mobileAppAuthenticated) {
        mEditor.putBoolean(MOBILE_APP_AUTHENTICATED, mobileAppAuthenticated);
        mEditor.commit();
    }

    public boolean getMobileAppAuthenticated() {
        return mPreferences.getBoolean(MOBILE_APP_AUTHENTICATED, false);
    }

    public void setMobileAppAuthenticatedVia(String mobileAppAuthenticatedVia){
        mEditor.putString(MOBILE_APP_AUTHENTICATED_VIA, mobileAppAuthenticatedVia);
        mEditor.commit();
    }

    public String getMobileAppAuthenticatedVia(){
        return mPreferences.getString(MOBILE_APP_AUTHENTICATED_VIA,null);
    }

    public void setSelectedLink(String selectedLink){
        mEditor.putString(SELECTED_LINK,selectedLink);
        mEditor.commit();
    }

    public String getSelectedLink(){
        return mPreferences.getString(SELECTED_LINK,null);
    }
    public void setAgentId(String agentId) {
        mEditor.putString(AGENT_ID,agentId);
        mEditor.commit();
    }

    public String getAgentId() {
        return mPreferences.getString(AGENT_ID,"");
    }

    public void setInitialStatus(String initialStatus){
        mEditor.putString(INITIAL_STATUS, initialStatus);
        mEditor.commit();
    }

    public String getInitialStatus(){
        return mPreferences.getString(INITIAL_STATUS, "0");
    }


}
