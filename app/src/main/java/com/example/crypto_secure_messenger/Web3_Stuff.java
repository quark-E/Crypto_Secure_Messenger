package com.example.crypto_secure_messenger;
//import android.content.ContextWrapper;
import android.widget.LinearLayout;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import android.util.Log;
import java.io.File;
import org.web3j.crypto.*;
//import android.app.Activity;
import android.widget.Toast;
import android.content.Context;
import java.security.Provider;
import java.security.Security;
import android.widget.TextView;
import android.app.ActionBar.*;
import org.bouncycastle.jce.provider.*;


public class Web3_Stuff {

    //Context mContext;

    private final String password;
    private String walletPath;
    private File walletDir;

    Web3_Stuff(Context mContext)
    {
        setupBouncyCastle();
        //this.mContext = mContext;
        this.password = "medium";
        this.walletPath = mContext.getFilesDir().getAbsolutePath();
        this.walletDir = new File(walletPath);
        // FIXME: Add your own API key here
        Web3j web3 = Web3j.build(new HttpService("https://goerli.infura.io/v3/efff1f32623f43e4947d29cca3e4f6f6"));
        try {
            Web3ClientVersion clientVersion = web3.web3ClientVersion().sendAsync().get();
            if(!clientVersion.hasError()){
                //Connected
                String msg = "cool";
                Log.d("Success! ",msg);
            }
            else {
                //Show Error
                Log.d("okay ", "bleh");
            }
        }
        catch (Exception e) {
            Log.d("Error ",e.toString());
            //Show Error
        }
        //FIXME: Use your own password here
        //final String password = "medium";
        //String walletPath = mContext.getFilesDir().getAbsolutePath();
        //File walletDir  = new File(walletPath);

        try{
            WalletUtils.generateNewWalletFile(password, walletDir);
            Log.d("Wallet! ", "Yal");
            Log.d("Wall path ", walletDir.toString());
        }
        catch (Exception e){
            //Display an Error
            Log.d("Error 2 ", e.toString());
        }
        /*
        File[] list_of_Files = walletDir.listFiles();
        for(File file : list_of_Files) {
            if (file.isFile()) {
                try {
                    Credentials credentials = WalletUtils.loadCredentials(password, file.getAbsolutePath());
                    Toast.makeText(mContext, "Your address is " + credentials.getAddress(), Toast.LENGTH_LONG).show();
                    Log.d("Address ", credentials.getAddress());

                    TextView dynamicTextView = new TextView(mContext);
                    dynamicTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    dynamicTextView.setText(credentials.getAddress());
                    LinearLayout ll = new LinearLayout(mContext);
                    ll.addView(dynamicTextView);

                    break;
                } catch (Exception e) {
                    //Show Error
                    Log.d("Error 3 ", e.toString());
                }
            }
        }
        */
    }

    public void Views(Context mContext, TextView tv) {
        File[] list_of_Files = walletDir.listFiles();
        for(File file : list_of_Files) {
            if (file.isFile()) {
                try {
                    Credentials credentials = WalletUtils.loadCredentials(password, file.getAbsolutePath());
                    //TextView dynamicTextView = new TextView(mContext);
                    //dynamicTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    //dynamicTextView.setText(credentials.getAddress());
                    tv.setText(credentials.getAddress());
                    Toast.makeText(mContext, "Your address is " + credentials.getAddress(), Toast.LENGTH_LONG).show();
                    Log.d("Address ", credentials.getAddress());
                    return;
                } catch (Exception e) {
                    //Show Error
                    Log.d("Error 3 ", e.toString());
                }
            }
        }
    }

    private void setupBouncyCastle() {
        final Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
        if (provider == null) {
            // Web3j will set up the provider lazily when it's first used.
            return;
        }
        if (provider.getClass().equals(BouncyCastleProvider.class)) {
            // BC with same package name, shouldn't happen in real life.
            return;
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);
        Security.insertProviderAt(new BouncyCastleProvider(), 1);
    }

}
