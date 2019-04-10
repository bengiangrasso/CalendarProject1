package edu.gwu.calendarproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task




class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {


    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var mGoogleApiClient: GoogleApiClient? = null

    private var login: Button? = null
    private var logout: Button? = null
    private lateinit var noAccountButton: Button
    private val RC_SIGN_IN = 9001
    private val requestCode = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        login = findViewById(R.id.loginButton)
        logout = findViewById(R.id.logoutButton)
        noAccountButton = findViewById(R.id.noAccount)
        noAccountButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        })
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account != null){
            updateUI(true)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            }
        else {
            updateUI(false)
        }
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

        login?.setOnClickListener(View.OnClickListener{
            val apiIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            startActivityForResult(apiIntent, requestCode)
        })

        logout?.setOnClickListener(View.OnClickListener{
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                object: ResultCallback<Status> {
                    override fun onResult(status: Status){
                        updateUI(false)
                    }
                }
            )
        })
    }
    private fun handleSignInResult(result: GoogleSignInResult){
        if(result.isSuccess){
            val acct = result.getSignInAccount()
            val acctName = acct!!.getDisplayName()
            val acctEmail = acct!!.getEmail()
        }
    }
    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.d("Error", "onConnectionFailed: " + p0)
    }
    private fun updateUI(isLogin: Boolean) {
        if (isLogin) {
            login?.visibility = View.GONE
            logout?.visibility = View.VISIBLE
        }
        else {
            login?.visibility = View.VISIBLE
            logout?.visibility = View.GONE
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            //val task = GoogleSignInResult.getSignedInAccountFromIntent(data)
            //handleSignInResult(task)
        }
    }

}
