package com.example.auth0sampleapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.example.auth0sampleapp.ui.theme.Auth0SampleAppTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Auth0SampleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val account = Auth0(
                        getString(R.string.com_auth0_client_id),
                        getString(R.string.com_auth0_domain)
                    )
                    LoginScreen(
                        onLoginClicked = {
                            loginWithBrowser(account)
                        },
                        onLogoutClicked = {
                            logout(account)
                        }
                    )
                }
            }
        }
    }

    private fun loginWithBrowser(account: Auth0) {
        // Setup the WebAuthProvider, using the custom scheme and scope.
        WebAuthProvider.login(account)
            .withScheme(getString(R.string.com_auth0_scheme))
            .withScope("openid profile email read:current_user update:current_user_metadata")
            .withAudience("https://${getString(R.string.com_auth0_domain)}/api/v2/")

            // Launch the authentication passing the callback where the results will be received
            .start(this, object : Callback<Credentials, AuthenticationException> {
                override fun onFailure(exception: AuthenticationException) {
                    // Authentication failed!
                    println("vlad: ${exception.message}")
                    Toast.makeText(this@MainActivity, "Login failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(credentials: Credentials) {
                    // Authentication was successful!
                    Toast.makeText(this@MainActivity, "Login Success", Toast.LENGTH_SHORT).show()
                    showUserProfile(credentials.accessToken, account)
                }
            })
    }

    private fun logout(account: Auth0) {
        WebAuthProvider.logout(account)
            .withScheme("demo")
            .start(this, object: Callback<Void?, AuthenticationException> {
                override fun onSuccess(payload: Void?) {
                    // The user has been logged out!
                    Toast.makeText(this@MainActivity, "Logout Success", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(error: AuthenticationException) {
                    // Something went wrong!
                    println("vlad: ${error.message}")
                    Toast.makeText(
                        this@MainActivity,
                        "Logout failed: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun showUserProfile(accessToken: String, account: Auth0) {
        var client = AuthenticationAPIClient(account)

        // With the access token, call `userInfo` and get the profile from Auth0.
        client.userInfo(accessToken)
            .start(object : Callback<UserProfile, AuthenticationException> {
                override fun onFailure(exception: AuthenticationException) {
                    // Something went wrong!
                }

                override fun onSuccess(profile: UserProfile) {
                    // We have the user's profile!
                    val email = profile.email
                    val name = profile.name
                    Toast.makeText(this@MainActivity, "$email + $name", Toast.LENGTH_SHORT).show()
                }
            })
    }
}

