package org.zakky.touchpairing.util;

import static android.bluetooth.BluetoothAdapter.*;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

/**
 * Bluetooth に関する処理を提供するユーティリティクラスです。
 *
 * @author zaki
 */
public final class BluetoothUtil {

    private static final BluetoothAdapter ADAPTER = getDefaultAdapter();

    /**
     * Bluetooth が有効になっているかどうかを返します。
     * 
     * @return
     * デバイスが搭載されていれば {@code true}、搭載されていない場合は {@code false}。
     */
    public static boolean isDevicePresent() {
        return ADAPTER != null;
    }

    /**
     * Bluetooth が有効になっているかどうかを返します。
     * 
     * @return
     * 有効になっていれば {@code true}、デバイスが存在しないか無効になっている場合は
     * {@code false}。
     */
    public static boolean isEnabled() {
        if (ADAPTER == null) {
            return false;
        }
        final boolean enabled = ADAPTER.isEnabled();
        return enabled;
    }

    /**
     * Bluetooth 有効化を要求するアクティビティを表示します。
     * 
     * <p>
     * 有効になったかどうかを、アクティビティの結果として受け取ります。
     * 結果を
     * </p>
     * <p>
     * デバイスが存在し有効化される場合は、要求元のアクティビティに対して
     * 無条件に {@link Activity#RESULT_OK} が返します。
     * </p>
     * 
     * @param requester
     * 要求元のアクティビティ。要求に対する結果は、このアクティビティに対して通知されます。
     * @param requestCode
     * 要求コード。
     * @return
     * 要求アクティビティを開始した場合は {@code true}、デバイスが存在しないなどの理由で
     * 開始しなかった場合は {@code false}。
     */
    public static boolean requestEnable(Activity requester, int requestCode) {
        if (ADAPTER == null) {
            return false;
        }

        final Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requester.startActivityForResult(i, requestCode);
        return true;
    }

    /**
     * コンストラクタ。インスタンス生成禁止。
     */
    private BluetoothUtil() {
        throw new AssertionError("instantiation prohibited.");
    }
}
