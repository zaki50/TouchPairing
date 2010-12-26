package org.zakky.touchpairing.ui;

import static org.zakky.touchpairing.util.KushikatsuHelper.buildIntentForKushikatsuInstall;
import static org.zakky.touchpairing.util.KushikatsuHelper.buildIntentForSendIntent;
import static org.zakky.touchpairing.util.KushikatsuHelper.isKushikatsuInstalled;

import org.zakky.touchpairing.R;
import org.zakky.touchpairing.util.BluetoothUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * 起動直後に表示されるメインアクティビティです。BT のペアリング要求を送信したりします。
 * 
 * TODO 串カツインストール完了時に、インストールボタンのステータス更新をする
 * 
 * @author zaki
 */
public final class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_PUSH_REMOTE_INSTALL_INTENT = 2;

    private Button installKushikatsuButton_;
    private Button installAppToRemoteButton_;
    private Button requestPairingButton_;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setupAllButtons();
    }

    @Override
    protected void onStart() {
        super.onStart();

        startEnableBluetoothRequestActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateButtonStatus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
        case REQUEST_ENABLE_BT:
            if (resultCode != RESULT_OK) {
                // BT が有効化されなかったので終了
                finish();
            }
            break;
        case REQUEST_PUSH_REMOTE_INSTALL_INTENT: {
            final Toast toast;
            if (resultCode == RESULT_OK) {
                toast = Toast.makeText(this, "FeliCa Push 送信成功。",
                        Toast.LENGTH_SHORT);
            } else if (resultCode == RESULT_CANCELED) {
                toast = Toast.makeText(this, "送信をキャンセルしました。",
                        Toast.LENGTH_SHORT);
            } else if (resultCode == 1) {
                toast = Toast.makeText(this, "FeliCa Push 送信に失敗しました。",
                        Toast.LENGTH_SHORT);
            } else if (resultCode == 2) {
                toast = Toast.makeText(this, "リクエストのパラメータが不正です。",
                        Toast.LENGTH_SHORT);
            } else if (resultCode == 3) {
                toast = Toast.makeText(this, "FeliCa デバイスがみつかりません。",
                        Toast.LENGTH_SHORT);
            } else if (resultCode == 4) {
                toast = Toast.makeText(this, "FeliCa デバイスは使用中です。",
                        Toast.LENGTH_SHORT);
            } else if (resultCode == 5) {
                toast = Toast.makeText(this, "データが大きすぎます。", Toast.LENGTH_SHORT);
            } else if (resultCode == 6) {
                toast = Toast.makeText(this, "送信がタイムアウトしました。",
                        Toast.LENGTH_SHORT);
            } else if (resultCode == 7) {
                toast = Toast.makeText(this, "おサイフケータイの初期化が未実施です。",
                        Toast.LENGTH_SHORT);
            } else if (resultCode == 8) {
                toast = Toast.makeText(this, "おサイフケータイロック中です。",
                        Toast.LENGTH_SHORT);
            } else {
                toast = Toast.makeText(this, "不明なエラーです: " + resultCode,
                        Toast.LENGTH_SHORT);
            }
            toast.show();
            break;
        }
        default:
            Log.e(TAG, "予期しないリクエストコード: " + requestCode);
            break;
        }
    }

    /**
     * ボタンをセットアップします。
     * 
     * <p>
     * ボタンのインスタンスの参照をメンバ変数へ格納するので、ボタンに対する操作を行う前に
     * このメソッドを呼び出す必要があります。
     * {@link #onCreate(Bundle)} での呼び出しを想定しています。
     * </p>
     */
    private void setupAllButtons() {
        setupInstallKushikatsuButton();
        setupInstallAppToRemoteButton();
        setupRequestPairingButton();
    }
    
    /**
     * 
     */
    private void setupInstallKushikatsuButton() {
        installKushikatsuButton_ = (Button) findViewById(R.id.install_kushikatsu_button);
        installKushikatsuButton_.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = buildIntentForKushikatsuInstall();
                startActivity(intent);
            }
        });
        installKushikatsuButton_.setEnabled(false);
    }

    private void setupInstallAppToRemoteButton() {
        final String packageName = getApplicationInfo().packageName;
        installAppToRemoteButton_ = (Button) findViewById(R.id.install_touch_paring_to_remote_button);
        installAppToRemoteButton_.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Uri uri = Uri.parse("market://details?id=" + packageName);
                final Intent remoteIntent = new Intent(Intent.ACTION_VIEW, uri);
                final Intent intent = buildIntentForSendIntent(remoteIntent);
                startActivityForResult(intent,
                        REQUEST_PUSH_REMOTE_INSTALL_INTENT);
            }
        });
    }

    private void setupRequestPairingButton() {
        requestPairingButton_ = (Button) findViewById(R.id.request_pairing_button);
        requestPairingButton_.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 表示されているボタンの有効/無効の切り替えを行います。
     */
    private void updateButtonStatus() {
        final boolean installed;
        installed = isKushikatsuInstalled(getApplicationContext());

        installKushikatsuButton_.setEnabled(!installed);
        installAppToRemoteButton_.setEnabled(installed);
        requestPairingButton_
                .setEnabled(installed && BluetoothUtil.isEnabled());
    }

    /**
     * ユーザに対して、Bluetooth 有効化を要求します。
     * 
     * <p>
     * 結果は {@link #onActivityResult(int, int, Intent)} に対して通知されます。
     * </p>
     */
    private void startEnableBluetoothRequestActivity() {
        BluetoothUtil.requestEnable(this, REQUEST_ENABLE_BT);
    }

}