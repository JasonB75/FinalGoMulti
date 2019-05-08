package comjasonb75.finalgomulti;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MAINACTIVITY";
    public Button mSendButton, mConnectButton;
    public TextView mShowText, mConnectText, mIdText;
    GoogleSignInAccount signedInAccount;
    public EditText mSendEdit;
    private String mDisplayName;
    private String mPlayerId;
    Context mContext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSendButton = findViewById(R.id.button);
        mConnectButton = findViewById(R.id.button2);
        mShowText = findViewById(R.id.textView);
        mConnectText = findViewById(R.id.textView2);
        mSendEdit = findViewById(R.id.editText);
        mIdText = findViewById(R.id.textView3);



       // GoogleSignInOptions signInOptions = GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN;

        GoogleSignInClient signIncleint = GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build());


        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignInIntent();
            }
        });

        if (GoogleSignIn.getLastSignedInAccount(this) != null)
        {
            mShowText.setText(GoogleSignIn.getLastSignedInAccount(this).getDisplayName());
            PlayersClient playersClient = Games.getPlayersClient(this,signedInAccount);
            playersClient.getCurrentPlayer()
                    .addOnCompleteListener(new OnCompleteListener<Player>() {
                        @Override
                        public void onComplete(@NonNull Task<Player> task) {
                            String displayName;
                            if (task.isSuccessful()) {
                                displayName = task.getResult().getDisplayName();
                                mShowText.setText(displayName);
                            } else {
                                Exception e = task.getException();
                                mShowText.setText(e.getMessage());
                            }

                        }
                    });
       }

    }
    private void startSignInIntent() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, 9001);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9001) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // The signed in account is stored in the result.
                signedInAccount = result.getSignInAccount();
            } else {
                String message = result.getStatus().getStatusMessage();
                if (message == null || message.isEmpty()) {
                    message = "Error signing in";
                }
                new AlertDialog.Builder(this).setMessage(message)
                        .setNeutralButton(android.R.string.ok, null).show();
            }
        }
    }
}
