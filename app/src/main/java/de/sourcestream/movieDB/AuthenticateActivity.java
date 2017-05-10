package de.sourcestream.movieDB;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.sourcestream.movieDB.constants.AppConstants;
import de.sourcestream.movieDB.google_plus.GooglePlusApi;
import de.sourcestream.movieDB.google_plus.SocialNetworksUpdateDataInterface;
import de.sourcestream.movieDB.preference.UserPreference;
import de.sourcestream.movieDB.utils.AlertUtils;
import de.sourcestream.movieDB.utils.BDevice;
import de.sourcestream.movieDB.utils.TextUtils;

public class AuthenticateActivity extends AppCompatActivity implements View.OnClickListener,SocialNetworksUpdateDataInterface,AppConstants {

    public static String IS_FROM_ALERT_AND_SUGGESTION = "is_form_alert_and_suggestion";
    private TextInputLayout edtEmailId,edtFirstName, edtLastName, edtAuthenticateLastName, edtAuthenticateFirstName, edtAuthenticateEmail;
    private TextView txtEmail, txtFirstName, txtLastName;
    private UserPreference mUserPreference;
    private LinearLayout lnrBottomLayout;
    private static final String TAG = AuthenticateActivity.class.getSimpleName();
    private ImageView imgGooglePlus;
    private GooglePlusApi googlePlusApi;
    private ProgressDialog customProgress;
    private boolean isFromSugesstion = false, isFromAlertAndSugesstion = false;
    private boolean isNamedEnabled = false;
    private LinearLayout lnrMediaLayout, lnrNamedLayout, lnrAuthenticatedLayout;
    private Button btnSignIn;
    private String facebookId="",googlePlusId="",twitterId="", linkedinId="", fromWhere = "";
    private View emailDivider, firstNameDivider, lastNameDivider;
    private String mAuthentication = "", surveyTitle = "", surveyLink = "", mAuthenticateLink = "", mArticleId = "";

//    private String color = "", TITLE = "", description = "", rss = "", link="";
//    private int id = -1, articleId = -1;
//    private CategoryList.Data.Category.Article article;
//    private CategoryArticles.Article mArticle;
//    private CategoryArticles.HeadlineArticle mHeadlineArticle;
//    private CategoryList.Data.HeadlineArticle mCategoryHeaderArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
            getSupportActionBar().setTitle("LogIn");
        }
        setContentView(R.layout.activity_authenticate);
        init();
        setUpDefaults();
        setUpEvents();
    }

    private void init() {
        lnrAuthenticatedLayout = (LinearLayout) findViewById(R.id.lnr_authenticate_layout);
        lnrNamedLayout = (LinearLayout) findViewById(R.id.lnr_named_layout);
        lnrMediaLayout = (LinearLayout) findViewById(R.id.lnr_media_layout);
        btnSignIn = (Button) findViewById(R.id.sign_in);
        txtEmail = (TextView) findViewById(R.id.txt_email);
        txtFirstName = (TextView) findViewById(R.id.txt_first_name);
        txtLastName = (TextView) findViewById(R.id.txt_last_name);
        edtEmailId = (TextInputLayout) findViewById(R.id.email);
        edtFirstName = (TextInputLayout) findViewById(R.id.first_name);
        edtLastName = (TextInputLayout) findViewById(R.id.last_name);
        edtAuthenticateEmail = (TextInputLayout) findViewById(R.id.edt_email);
        edtAuthenticateLastName = (TextInputLayout) findViewById(R.id.edt_last_name);
        edtAuthenticateFirstName = (TextInputLayout) findViewById(R.id.edt_first_name);
        emailDivider = (View) findViewById(R.id.email_divider);
        firstNameDivider = (View) findViewById(R.id.first_name_divider);
        lastNameDivider = (View) findViewById(R.id.last_name_divider);
        lnrBottomLayout = (LinearLayout) findViewById(R.id.lnr_media_layout);
        mUserPreference = new UserPreference(this);
        imgGooglePlus = (ImageView) findViewById(R.id.googleplus_icon);
    }

    private void setUpDefaults() {
        if(!TextUtils.isEmpty(mUserPreference.getGooglePlusId())){
            Intent intent = new Intent(AuthenticateActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        mAuthentication = "authenticated";

        btnSignIn.setVisibility(View.GONE);
        isNamedEnabled = false;
        lnrAuthenticatedLayout.setVisibility(View.GONE);
        lnrNamedLayout.setVisibility(View.GONE);

        googlePlusApi = new GooglePlusApi();
        googlePlusApi.initGooglePlus(AuthenticateActivity.this);

    }

    private void setUpEvents() {
        imgGooglePlus.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        if(isNamedEnabled){
            edtFirstName.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(!TextUtils.isEmpty(edtFirstName.getEditText().getText().toString()) && !TextUtils.isEmpty(edtLastName.getEditText().getText().toString()) && !TextUtils.isEmpty(edtEmailId.getEditText().getText().toString()) && TextUtils.isValidEmail(edtEmailId.getEditText().getText().toString().trim())){
                        if(TextUtils.isEmpty(facebookId) && TextUtils.isEmpty(twitterId) && TextUtils.isEmpty(googlePlusId) && TextUtils.isEmpty(linkedinId)){
                            btnSignIn.setText(getText(R.string.ok));
                        }else{
                            btnSignIn.setText(getText(R.string.thats_ok));
                        }
                        btnSignIn.setVisibility(View.VISIBLE);
                    }else{
                        btnSignIn.setVisibility(View.GONE);
                    }
                }
            });

            edtEmailId.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(!TextUtils.isEmpty(edtFirstName.getEditText().getText().toString()) && !TextUtils.isEmpty(edtLastName.getEditText().getText().toString()) && !TextUtils.isEmpty(edtEmailId.getEditText().getText().toString())&& TextUtils.isValidEmail(edtEmailId.getEditText().getText().toString().trim())){
                        if(TextUtils.isEmpty(facebookId) && TextUtils.isEmpty(twitterId) &&TextUtils.isEmpty(googlePlusId)&& TextUtils.isEmpty(linkedinId)){
                            btnSignIn.setText(getText(R.string.ok));
                        }else{
                            btnSignIn.setText(getText(R.string.thats_ok));
                        }
                        btnSignIn.setVisibility(View.VISIBLE);
                    }else{
                        btnSignIn.setVisibility(View.GONE);
                    }
                }
            });

            edtLastName.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(!TextUtils.isEmpty(edtFirstName.getEditText().getText().toString()) && !TextUtils.isEmpty(edtLastName.getEditText().getText().toString()) && !TextUtils.isEmpty(edtEmailId.getEditText().getText().toString())&& TextUtils.isValidEmail(edtEmailId.getEditText().getText().toString().trim())){
                        if(TextUtils.isEmpty(facebookId) && TextUtils.isEmpty(twitterId) &&TextUtils.isEmpty(googlePlusId)&& TextUtils.isEmpty(linkedinId)){
                            btnSignIn.setText(getText(R.string.ok));
                        }else{
                            btnSignIn.setText(getText(R.string.thats_ok));
                        }
                        btnSignIn.setVisibility(View.VISIBLE);
                    }else{
                        btnSignIn.setVisibility(View.GONE);
                    }
                }
            });

            edtLastName.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_GO) {
                        authenticate();
                    }
                    return false;
                }
            });

            edtAuthenticateLastName.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_GO) {
                        authenticate();
                    }
                    return false;
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == googlePlusApi.RC_SIGN_IN) {
            if (resultCode == RESULT_OK && data != null) {
                customProgress = new ProgressDialog(AuthenticateActivity.this);
                customProgress.setCancelable(false);
                customProgress.show();

                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.googleplus_icon:
                if (BDevice.isInternetConnected(AuthenticateActivity.this)) {
                    fromWhere = "google plus";
                    googlePlusApi.callGooglePlusSignInIntent();
                } else {
                    AlertUtils.showToast(AuthenticateActivity.this, getString(R.string.check_your_internet_connection));
                }
                break;

            case R.id.sign_in:
                authenticate();
                break;
        }
    }


    @Override
    public void socialNetworksUpdateUserDataInterface(String socialNetworkType, String id, String email,
            String userName, String firstName, String lastName, String birthday, String gender, String userImg) {
//        Log.d("ss","first name "+firstName+" last name "+);
        facebookId="";googlePlusId="";twitterId="";linkedinId="";
        btnSignIn.setVisibility(View.VISIBLE);
        btnSignIn.setText(getText(R.string.thats_ok));
        if(isNamedEnabled){
            lnrAuthenticatedLayout.setVisibility(View.GONE);
            lnrNamedLayout.setVisibility(View.VISIBLE);
        }else{
            lnrAuthenticatedLayout.setVisibility(View.VISIBLE);
            lnrNamedLayout.setVisibility(View.GONE);
        }
//        Log.d(TAG, "socialNetworksUpdateUserDataInterface: "+email+"=>"+userName+"=>"+lastName+"=>"+firstName+"=>"+birthday);
        switch (socialNetworkType){
            case GOOGLE_PLUS:
//                setTrackerEventName(getString(R.string.google_plus_authenticate_event), getString(R.string.success_event),getString(R.string.email_event), AuthenticateActivity.class.getSimpleName());
                googlePlusId = id;
                break;
            case FACEBOOK:
//                setTrackerEventName(getString(R.string.facebook_authenticate_event), getString(R.string.success_event),getString(R.string.email_event), AuthenticateActivity.class.getSimpleName());
                facebookId = id;
                break;
            case TWITTER:
//                setTrackerEventName(getString(R.string.twitter_authenticate_event), getString(R.string.success_event),getString(R.string.email_event), AuthenticateActivity.class.getSimpleName());
                twitterId = id;
                break;
            case LINKEDIN:
                linkedinId = id;
                break;
        }
        if(isNamedEnabled){
            if (TextUtils.isEmpty(firstName)){
                edtFirstName.getEditText().setText(firstName);
            }else {
                edtFirstName.getEditText().setText(firstName);
            }
            edtLastName.getEditText().setText(lastName);
            edtEmailId.getEditText().setText(email);
        }else{

            if(TextUtils.isEmpty(email)){
                txtEmail.setVisibility(View.GONE);
                emailDivider.setVisibility(View.GONE);
                edtAuthenticateEmail.setVisibility(View.VISIBLE);
            }else{
                txtEmail.setVisibility(View.VISIBLE);
                emailDivider.setVisibility(View.VISIBLE);
                edtAuthenticateEmail.setVisibility(View.GONE);
                txtEmail.setText(email);
            }

            if(TextUtils.isEmpty(firstName)){
                txtFirstName.setVisibility(View.GONE);
                firstNameDivider.setVisibility(View.GONE);
                edtAuthenticateFirstName.setVisibility(View.VISIBLE);
            }else{
                txtFirstName.setVisibility(View.VISIBLE);
                firstNameDivider.setVisibility(View.VISIBLE);
                edtAuthenticateFirstName.setVisibility(View.GONE);
                txtFirstName.setText(firstName);
            }

            if(TextUtils.isEmpty(lastName)){
                txtLastName.setVisibility(View.GONE);
                lastNameDivider.setVisibility(View.GONE);
                edtAuthenticateLastName.setVisibility(View.VISIBLE);
            }else{
                txtLastName.setVisibility(View.VISIBLE);
                lastNameDivider.setVisibility(View.VISIBLE);
                edtAuthenticateLastName.setVisibility(View.GONE);
                txtLastName.setText(lastName);
            }

        }
        if(!TextUtils.isEmpty(googlePlusId)){
            btnSignIn.setText(getResources().getString(R.string.thats_ok));
            mUserPreference.setDefaultAuthentication(firstName, lastName, email,"",facebookId,googlePlusId,twitterId,linkedinId);
            lnrMediaLayout.setVisibility(View.GONE);
//        btnSignIn.setText(getResources().getString(R.string.logout));
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
//        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            final GoogleSignInAccount acct = result.getSignInAccount();
            String gPlusId, email, userName, userImg, birthday;
            String firstName="", lastName="";
            if(!googlePlusApi.googleApiClient.isConnected())
                return;

            try{
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(googlePlusApi.googleApiClient);
            }catch (Exception e){
                e.printStackTrace();
            }
//            Log.d(TAG, "currentPerson: " + currentPerson);
//            Log.d(TAG, "Plus.PeopleApi.getCurrentPerson(googlePlusApi.googleApiClient): " + Plus.PeopleApi.getCurrentPerson(googlePlusApi.googleApiClient));
//            Log.d(TAG, "acct.getId(): " + acct.getId());
//            Log.d(TAG, "acct.getEmail(): " + acct.getEmail());
//            Log.d(TAG, "acct.getDisplayName(): " + acct.getDisplayName());
//            Log.d(TAG, "acct.getPhotoUrl(): " + acct.getPhotoUrl());

//            if (currentPerson != null) {
//                Log.d(TAG, "currentPerson.getBirthday()" + currentPerson.getBirthday());
//                Log.d(TAG, "currentPerson.getGender()" + currentPerson.getGender());
//            }

            gPlusId = (acct.getId() != null) ? acct.getId().toString() : "";
            email = (acct.getEmail() != null) ? acct.getEmail().toString() : "";
            userName = (acct.getDisplayName() != null) ? acct.getDisplayName().toString() : "";

            Log.d(TAG , "userName" + userName);
            userImg = (acct.getPhotoUrl() != null) ? acct.getPhotoUrl().toString() : "";

            if(!TextUtils.isEmpty(userName)) {

                firstName  = userName.substring(0,userName.indexOf(' '));
                Log.d(TAG, "acct.FirstName (): " + firstName);

                lastName =     userName.substring(userName.indexOf(' ') + 1, userName.length());
                Log.d(TAG, "acct.lastName (): " + lastName);
            }
//            birthday = (currentPerson.getBirthday() != null) ? currentPerson.getBirthday().toString() : "";
            // String gender = (currentPerson.getGender() != null) ? currentPerson.getBirthday().toString() : ""

            socialNetworksUpdateUserDataInterface(GOOGLE_PLUS, gPlusId, email, userName, firstName, lastName, "", "", userImg);


            // String socialNetworkType, String id, String email,
            //  String userName, String firstName, String lastName, String birthday, String gender, String userImg

            //String socialNetworkType, String id, String email,

            googlePlusApi.callGooglePlusSignOut();
        }else{
//            if(!TextUtils.isEmpty(fromWhere)){
//                if(fromWhere.equalsIgnoreCase("facebook")){
////                    setTrackerEventName(getString(R.string.facebook_authenticate_event), getString(R.string.failed_event),getString(R.string.email_event), AuthenticateActivity.class.getSimpleName());
//                }else if(fromWhere.equalsIgnoreCase("google plus")){
////                    setTrackerEventName(getString(R.string.google_plus_authenticate_event), getString(R.string.failed_event),getString(R.string.email_event), AuthenticateActivity.class.getSimpleName());
//                }else if(fromWhere.equalsIgnoreCase("twitter")){
//                    setTrackerEventName(getString(R.string.twitter_authenticate_event), getString(R.string.failed_event),getString(R.string.email_event), AuthenticateActivity.class.getSimpleName());
//                }
//            }
        }

        if (!isFinishing() && customProgress.isShowing()) {
            customProgress.dismiss();
        }
    }

//    @Override
//    protected boolean closeOnBackAction() {
//        BDevice.hideSoftKeyboard(this, edtFirstName);
//        finish();
//        return false;
//        //return super.closeOnBackAction();
//    }
//
//    @Override
//    public void onBackPressed() {
//        onUserClose();
//    }
//
//    private void onUserClose() {
//        finish();
//    }

    private void authenticate() {

        if(isNamedEnabled){
            if (TextUtils.isEmpty(edtEmailId.getEditText().getText())) {
                edtEmailId.setError(getString(R.string.email_must_not_be_empty));
                return;
            }

            if (!TextUtils.isValidEmail(edtEmailId.getEditText().getText().toString().trim())) {
                edtEmailId.setError(getString(R.string.invalid_email));
                return;
            }

            edtEmailId.setError(null);

            if (TextUtils.isEmpty(edtFirstName.getEditText().getText())) {
                edtFirstName.setError(getString(R.string.first_name_must_not_empty));
                return;
            }

            edtFirstName.setError(null);

            if (TextUtils.isEmpty(edtLastName.getEditText().getText())) {
                edtLastName.setError(getString(R.string.last_name_must_not_empty));
                return;
            }

            edtLastName.setError(null);
        }else{
            if (edtAuthenticateEmail.getVisibility() == View.VISIBLE) {

                if (TextUtils.isEmpty(edtAuthenticateEmail.getEditText().getText())) {
                    edtAuthenticateEmail.setError(getString(R.string.email_must_not_be_empty));
                    return;
                }

                if (!TextUtils.isValidEmail(edtAuthenticateEmail.getEditText().getText().toString().trim())) {
                    edtAuthenticateEmail.setError(getString(R.string.invalid_email));
                    return;
                }

                edtAuthenticateEmail.setError(null);
            } else  if (txtEmail.getVisibility() == View.VISIBLE){
                if (TextUtils.isEmpty(txtEmail.getText().toString()) || txtEmail.getText().toString().equalsIgnoreCase(getResources().getString(R.string.email))) {
                    AlertUtils.showToast(this, getString(R.string.email_must_not_be_empty));
                    return;
                }

                if (!TextUtils.isValidEmail(txtEmail.getText().toString().trim())) {
                    AlertUtils.showToast(this, getString(R.string.invalid_email));
                    return;
                }
            }

           /* if (TextUtils.isEmpty(txtFirstName.getText()) || txtFirstName.getText().toString().equalsIgnoreCase(getResources().getString(R.string.first_name))) {
                AlertUtils.showAlert(this, getString(R.string.first_name_must_not_empty));
                return;
            }*/

            if (edtAuthenticateFirstName.getVisibility() == View.VISIBLE) {
                if (TextUtils.isEmpty(edtAuthenticateFirstName.getEditText().getText())) {
                    edtAuthenticateFirstName.setError(getString(R.string.first_name_must_not_empty));
                    return;
                }
                edtAuthenticateFirstName.setError(null);
            } else  if (txtFirstName.getVisibility() == View.VISIBLE){
                if (TextUtils.isEmpty(txtFirstName.getText()) || txtFirstName.getText().toString().equalsIgnoreCase(getResources().getString(R.string.first_name))) {
                    AlertUtils.showToast(this, getString(R.string.first_name_must_not_empty));
                    return;
                }
            }

            if (edtAuthenticateLastName.getVisibility() == View.VISIBLE) {
                if (TextUtils.isEmpty(edtAuthenticateLastName.getEditText().getText())) {
                    edtAuthenticateLastName.setError(getString(R.string.last_name_must_not_empty));
                    return;
                }
                edtAuthenticateLastName.setError(null);
            } else  if (txtLastName.getVisibility() == View.VISIBLE){
                if (TextUtils.isEmpty(txtLastName.getText()) || txtLastName.getText().toString().equalsIgnoreCase(getResources().getString(R.string.last_name))) {
                    AlertUtils.showToast(this, getString(R.string.last_name_must_not_empty));
                    return;
                }
            }
        }

        sendResult();
    }

    public void sendResult() {
//        setTrackerEventName(getString(R.string.authenticate), getString(R.string.clicked_event),getString(R.string.email_event), AuthenticateActivity.class.getSimpleName());
        BDevice.hideSoftKeyboard(this, lnrBottomLayout);
        if(!TextUtils.isEmpty(facebookId) || !TextUtils.isEmpty(googlePlusId) || !TextUtils.isEmpty(twitterId) || !TextUtils.isEmpty(linkedinId)) {
            mUserPreference.setAuthenticatedVia("authenticated");
        }else{
            mUserPreference.setAuthenticatedVia("named");
        }
        if(isNamedEnabled){
            mUserPreference.setDefaultAuthentication(edtFirstName.getEditText().getText().toString().trim(), edtLastName.getEditText().getText().toString().trim(), edtEmailId.getEditText().getText().toString().trim(), "", facebookId,googlePlusId,twitterId, linkedinId);
        }else{
            String email="", firstName="", lastName="";
            if (edtAuthenticateEmail.getVisibility() == View.VISIBLE) {
                email = edtAuthenticateEmail.getEditText().getText().toString().trim();
            } else  if (txtEmail.getVisibility() == View.VISIBLE){
                email = txtEmail.getText().toString().trim();
            }

            if (edtAuthenticateFirstName.getVisibility() == View.VISIBLE) {
                firstName =  edtAuthenticateFirstName.getEditText().getText().toString().trim();
            } else  if (txtFirstName.getVisibility() == View.VISIBLE){
                firstName =  txtFirstName.getText().toString().trim();
            }

            if (edtAuthenticateLastName.getVisibility() == View.VISIBLE) {
                lastName = edtAuthenticateLastName.getEditText().getText().toString().trim();
            } else  if (txtLastName.getVisibility() == View.VISIBLE){
                lastName = txtLastName.getText().toString().trim();
            }

            mUserPreference.setDefaultAuthentication(firstName, lastName, email,"",facebookId,googlePlusId,twitterId,linkedinId);
        }

//        if(isFromAlertAndSugesstion){
//            setResult(RESULT_OK);
//        }else {
//            Intent data = new Intent();
//            data.putExtra(WebViewActivity.ARTICLE_ID, mArticleId);
//            if (!TextUtils.isEmpty(mAuthentication) && !TextUtils.isEmpty(getApp().getAppPreference().getIsFromSuggestion()) && getApp()
//                    .getAppPreference().getIsFromSuggestion().equalsIgnoreCase("survey")) {
//                data.putExtra(SurveyFragment.SURVEY_TITLE, surveyTitle);
//                data.putExtra(SurveyFragment.SURVEY_LINK, surveyLink);
//                if (getIntent().hasExtra(WebViewActivity.RESUME_URL)) {
//                    data.putExtra(WebViewActivity.RESUME_URL, getIntent().getExtras().getString(WebViewActivity.RESUME_URL));
//                }
//            } else if (!TextUtils.isEmpty(mAuthentication)) {
//                data.putExtra(WebViewActivity.GUID, mAuthenticateLink);
//            } else {
//                data.putExtra(CategoryFragment.IS_FROM_SUGESTION, isFromSugesstion);
//            }
//
//            setResult(RESULT_OK, data);
//        }
        Intent intent = new Intent(AuthenticateActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
//        onUserClose();
    }

//    private void onUserClose() {
//        if(isNamedEnabled){
//            if (TextUtils.isEmpty(edtEmailId.getEditText().getText()) || !TextUtils.isValidEmail(edtEmailId.getEditText().getText().toString().trim()) || TextUtils.isEmpty(edtFirstName.getEditText().getText()) || TextUtils.isEmpty(edtLastName.getEditText().getText())) {
//                finish();
//                return;
//            }
//        }else{
//            if(TextUtils.isEmpty(facebookId) && TextUtils.isEmpty(twitterId) && TextUtils.isEmpty(googlePlusId) && TextUtils.isEmpty(linkedinId)){
//                finish();
//                return;
//            }
//
//            if (edtAuthenticateEmail.getVisibility() == View.VISIBLE) {
//                if (TextUtils.isEmpty(edtAuthenticateEmail.getEditText().getText()) || edtAuthenticateEmail.getEditText().getText().toString().equalsIgnoreCase(getResources().getString(R.string.email)) || !TextUtils.isValidEmail(edtAuthenticateEmail.getEditText().getText().toString().trim())) {
//                    finish();
//                    return;
//                }
//            }else  if (txtEmail.getVisibility() == View.VISIBLE){
//                if (TextUtils.isEmpty(txtEmail.getText().toString()) || txtEmail.getText().toString().equalsIgnoreCase(getResources().getString(R.string.email)) || !TextUtils.isValidEmail(txtEmail.getText().toString().trim())) {
//                    finish();
//                    return;
//                }
//            }
//
//            if (edtAuthenticateFirstName.getVisibility() == View.VISIBLE) {
//                if (TextUtils.isEmpty(edtAuthenticateFirstName.getEditText().getText())) {
//                    finish();
//                    return;
//                }
//            } else  if (txtFirstName.getVisibility() == View.VISIBLE){
//                if (TextUtils.isEmpty(txtFirstName.getText()) || txtFirstName.getText().toString().equalsIgnoreCase(getResources().getString(R.string.first_name))) {
//                    finish();
//                    return;
//                }
//            }
//
//            if (edtAuthenticateLastName.getVisibility() == View.VISIBLE) {
//                if (TextUtils.isEmpty(edtAuthenticateLastName.getEditText().getText())) {
//                    finish();
//                    return;
//                }
//            } else  if (txtLastName.getVisibility() == View.VISIBLE){
//                if (TextUtils.isEmpty(txtLastName.getText()) || txtLastName.getText().toString().equalsIgnoreCase(getResources().getString(R.string.last_name))) {
//                    finish();
//                    return;
//                }
//            }
//        }
//
//
//        AlertDialog.Builder builder = AlertUtils.getBuilder(this);
//        builder.setMessage(R.string.do_you_want_save);
//        builder.setPositiveButton(R.string.yes_caps, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                loadAuthenticate();
//            }
//        });
//        builder.setNegativeButton(R.string.cancel_caps, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//               finish();
//            }
//        });
//        builder.create().show();
//    }
//
//    @Override
//    protected boolean closeOnBackAction() {
//        onUserClose();
//        return false;
//        //return super.closeOnBackAction();
//    }

    @Override
    protected void onResume() {
//        setTrackerScreenName(getString(R.string.authenticate_screen), AuthenticateActivity.class.getSimpleName());
        super.onResume();
    }

}
