package com.example.crypto_secure_messenger;
//import android.content.ContextWrapper;
import android.widget.LinearLayout;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendRawTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import android.util.Log;
import java.io.File;
import org.web3j.crypto.*;
//import android.app.Activity;
import android.widget.Toast;
import android.content.Context;

import java.io.UnsupportedEncodingException;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;
import org.web3j.utils.Numeric;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import java.io.IOException;
import java.math.BigInteger;
import java.security.Provider;
import java.security.Security;
import android.widget.TextView;
import android.app.ActionBar.*;
import org.bouncycastle.jce.provider.*;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.*;
import java.io.*;

public class Web3_Stuff {

    //Context mContext;

    private final String password;
    private String walletPath;
    private File walletDir;
    private Credentials credentials;
    private Web3j web3;

    Web3_Stuff(Context mContext)
    {
        setupBouncyCastle();
        //this.mContext = mContext;
        this.password = "medium";
        this.walletPath = mContext.getFilesDir().getAbsolutePath();
        this.walletDir = new File(walletPath);
        // FIXME: Add your own API key here
        this.web3 = Web3j.build(new HttpService("https://goerli.infura.io/v3/efff1f32623f43e4947d29cca3e4f6f6"));
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
            //ECKeyPair eck = Keys.createEcKeyPair();
            //WalletFile wf = Wallet.createLight(password,eck);
            //String fileName = getWalletFileName(wf);
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
    /*
    private static String getWalletFileName(WalletFile wf) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("'UTC--'yyyy-MM-dd'T'HH-mm-ss.nVV'--'");
    }
    */
    public void Views(Context mContext, TextView tv) {
        File[] list_of_Files = walletDir.listFiles();
        for(File file : list_of_Files) {
            if (file.isFile()) {
                try {
                    Credentials credentials = WalletUtils.loadCredentials(password, file.getAbsolutePath());
                    //ECKeyPair eck = ECKeyPair.create(new BigInteger("317965bd552acdaa0a9b6e243cf35769e5dcf899e2c7fe8bcdfac0411a3b2804"));
                    //WalletFile wf = Wallet.
                    this.credentials = credentials;
                    //TextView dynamicTextView = new TextView(mContext);
                    //dynamicTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                    //dynamicTextView.setText(credentials.getAddress());
                    tv.setText(credentials.getAddress());
                    Toast.makeText(mContext, "Your address is " + credentials.getAddress(), Toast.LENGTH_LONG).show();
                    Log.d("Address ", credentials.getAddress());
                    Log.d("Credentials ", file.getAbsolutePath());
                    try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
                        String line = null;
                        String err = "CredLines ";
                        while((line = br.readLine()) != null) {
                            Log.d(err, line);
                            err = err + "+";
                        }
                    } catch(Exception e) {
                        Log.d("ReadErr ", e.toString());
                    }
                    return;
                } catch (Exception e) {
                    //Show Error
                    Log.d("Error 3 ", e.toString());
                }
            }
        }
    }

    public BigInteger getNonce() throws InterruptedException, ExecutionException {
        EthGetTransactionCount ethGetBleh = web3.ethGetTransactionCount(
                credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get();

        return ethGetBleh.getTransactionCount();
    }

    public void sendTrans(String address, String msg) throws InterruptedException, ExecutionException{
        try {
            //RawTransaction rt = RawTransaction.createTransaction(getNonce(), BigInteger.valueOf(50), BigInteger.valueOf(21000), "0x6a9A2Eb257a6E4B96134Bf03D58F161cE5DdA8F7", "Hi there!");
            //"0x6a9A2Eb257a6E4B96134Bf03D58F161cE5DdA8F7"
            RawTransaction rt2 = RawTransaction.createTransaction(getNonce(), BigInteger.valueOf(50), BigInteger.valueOf(210000), address, BigInteger.valueOf(100), toHex(msg));
            byte[] signedMessage = TransactionEncoder.signMessage(rt2, credentials);
            String hexValue = Numeric.toHexString(signedMessage);
            EthSendTransaction ethSendTransaction = web3.ethSendRawTransaction(hexValue).sendAsync().get();
            String transactionHash = ethSendTransaction.getTransactionHash();
        } catch(Exception e) {
                Log.d("SendErr", e.toString());
            }
    }

    public String toHex(String arg) throws UnsupportedEncodingException {
        return String.format("%x", new BigInteger(1, arg.getBytes("utf-8")));
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
