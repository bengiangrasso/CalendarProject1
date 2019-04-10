package edu.gwu.calendarproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status


class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {


    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var mGoogleApiClient: GoogleApiClient? = null

    private var login: Button? = null

    private val requestCode = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        login = findViewById(R.id.loginButton)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()

        login?.setOnClickListener(View.OnClickListener{
            val apiIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            startActivityForResult(apiIntent, requestCode)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        })
    }
    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.d("Error", "onConnectionFailed: " + p0)
    }

}
