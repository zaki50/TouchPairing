/*
 * Copyright 2010 Android DEvelopers' cluB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id: KushikatsuHelper.java 95 2010-12-05 13:25:16Z makoto1975 $
 */
package org.zakky.touchpairing.util;

import static android.app.Activity.RESULT_FIRST_USER;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

/**
 * {@code KuShiKaTsu} を呼び出す際に有用な定数とメソッドを提供するクラスです。
 *
 * <p>
 * このコードは、呼び出し側のアプリにコピペされることを想定しています。
 * </p>
 */
public final class KushikatsuHelper {

    /**
     * {@code KuShiKaTsu} のパッケージ名です。
     */
    public static final String PACKAGE_NAME = "jp.andeb.kushikatsu";

    /**
     * {@link Intent} 送信に関する定数を集めたクラスです。
     */
    public static final class SendIntent {
        /**
         * {@link Intent} を Push 送信する際の {@code Intent} の {@code ACTION} 文字列です。
         */
        public static final String ACTION = PACKAGE_NAME + ".FELICA_INTENT";

        /**
         * {@link Intent} を Push 送信する際の {@code Intent} の {@code CATEGORY} 文字列です。
         */
        public static final String CATEGORY = Intent.CATEGORY_DEFAULT;

        /**
         * 相手側に転送したい {@link Intent} のキーです。
         *
         * <p>
         * 必須パラメータです。
         * </p>
         */
        public static final String EXTRA_INTENT = "EXTRA_INTENT";
    }

    /**
     * ブラウザ起動に関する定数を集めたクラスです。
     */
    public static final class StartBrowser {
        /**
         * ブラウザ起動要求を Push 送信する際の {@code Intent} の {@code ACTION} 文字列です。
         */
        public static final String ACTION = PACKAGE_NAME + ".FELICA_BROWSER";

        /**
         * ブラウザ起動要求を Push 送信する際の {@code Intent} の {@code CATEGORY} 文字列です。
         */
        public static final String CATEGORY = Intent.CATEGORY_DEFAULT;

        /**
         * 相手側に転送したい URL({@link String}) のキーです。
         *
         * <p>
         * 必須パラメータです。
         * </p>
         */
        public static final String EXTRA_URL = "EXTRA_URL";

        /**
         * 相手側に転送したいブラウザ起動パラメータ({@link String})のキーです。
         *
         * <p>
         * 任意パラメータです。
         * </p>
         */
        public static final String EXTRA_BROWSER_PARAM = "EXTRA_BROWSER_PARAM";
    }

    /**
     * メーラ起動に関する定数を集めたクラスです。
     */
    public static final class StartMailer {
        /**
         * メーラ起動要求を Push 送信する際の {@code Intent} の {@code ACTION} 文字列です。
         */
        public static final String ACTION = PACKAGE_NAME + ".FELICA_MAILER";

        /**
         * メーラ起動要求を Push 送信する際の {@code Intent} の {@code CATEGORY} 文字列です。
         */
        public static final String CATEGORY = Intent.CATEGORY_DEFAULT;

        /**
         * メール編集画面の To({@link String}{@code []}) のキーです。
         *
         * <p>
         * 任意パラメータです。
         * </p>
         */
        public static final String EXTRA_EMAIL = "EXTRA_EMAIL";

        /**
         * メール編集画面の Cc({@link String}{@code []}) のキーです。
         *
         * <p>
         * 任意パラメータです。
         * </p>
         */
        public static final String EXTRA_CC = "EXTRA_CC";

        /**
         * メール編集画面の Subject({@link String}) のキーです。
         *
         * <p>
         * 任意パラメータです。
         * </p>
         */
        public static final String EXTRA_SUBJECT = "EXTRA_SUBJECT";

        /**
         * メール編集画面の本文({@link String}) のキーです。
         *
         * <p>
         * 任意パラメータです。
         * </p>
         */
        public static final String EXTRA_TEXT = "EXTRA_TEXT";

        /**
         * メーラの起動パラメータ({@link String}) のキーです。
         *
         * <p>
         * 任意パラメータです。
         * </p>
         */
        public static final String EXTRA_MAIL_PARAM = "EXTRA_MAIL_PARAM";
    }

    /**
     * 共通パラメータのための定数を集めたクラスです。
     */
    public static final class CommonParam {
        /**
         * 送信タイムアウトの秒数({@code int})に対するキーです。
         */
        public static final String EXTRA_SEND_TIMEOUT = "EXTRA_SEND_TIMEOUT";

        /**
         * 送信成功時に鳴らす音を以下のいずれかから指定します。
         *
         * <ul>
         *   <li>空文字列({@code String}): 成功時は無音にします</li>
         *   <li>KuShiKaTsu のサウンド名({@code String}): KuShiKaTsu が内部に持っている音を鳴らします</li>
         *   <li>呼び出しアプリのサウンドリソースID({@code int}: 呼び出しアプリのサウンドリソースから音を鳴らします</li>
         * </ul>
         *
         * <p>
         * このパラメータ指定にかかわらず、ユーザが音を鳴らさない設定にしている場合は音は鳴りません。
         * </p>
         * <p>
         * このパラメータの指定にかかわらず、バイブはユーザの設定に従います。
         * </p>
         */
        public static final String EXTRA_SOUND_ON_SENT = "EXTRA_SOUND_ON_SENT";
    }

    /*
     * 独自定義の result code 群。RESULT_OK と RESULT_CANCELED は android が規定
     * しているものです。
     */

    /**
     * Push 送信が正常に完了した場合({@code =}{@value #RESULT_UNEXPECTED_ERROR})。
     */
    public static final int RESULT_OK = Activity.RESULT_OK;
    /**
     * Push 送信がキャンセうされた場合({@code =}{@value #RESULT_UNEXPECTED_ERROR})。
     */
    public static final int RESULT_CANCELED = Activity.RESULT_CANCELED;
    /**
     * 予期しないエラーで送信が行えなかった
     * 場合({@code =}{@value #RESULT_UNEXPECTED_ERROR})。
     */
    public static final int RESULT_UNEXPECTED_ERROR = RESULT_FIRST_USER + 0;
    /**
     * Activity を起動した {@link Intent} に含まれる追加情報が不正なため送信が行えなかった
     * 場合({@code =}{@value #RESULT_INVALID_EXTRA})。
     */
    public static final int RESULT_INVALID_EXTRA = RESULT_FIRST_USER + 1;
    /**
     * FeliCa デバイスが搭載されていない端末の
     * 場合({@code =}{@value #RESULT_DEVICE_NOT_FOUND})。
     */
    public static final int RESULT_DEVICE_NOT_FOUND = RESULT_FIRST_USER + 2;
    /**
     * デバイスが他のアプリケーションによって占有されているため送信できなかった
     * 場合({@code =}{@value #RESULT_DEVICE_IN_USE})。
     */
    public static final int RESULT_DEVICE_IN_USE = RESULT_FIRST_USER + 3;
    /**
     * パラメータとして渡された {@link Intent} や URL などの情報が、{@code FeliCa Push}
     * 送信機能で送ることのできる上限を越えている場合({@code =}{@value #RESULT_TOO_BIG})。
     */
    public static final int RESULT_TOO_BIG = RESULT_FIRST_USER + 4;
    /**
     * 受信端末が見つからないため送信がタイムアウトした
     * 場合({@code =}{@value #RESULT_TIMEOUT})。
     */
    public static final int RESULT_TIMEOUT = RESULT_FIRST_USER + 5;
    /**
     * 端末のおサイフケータイ初期化が行われていないため、FeliCa デバイスを使用できない
     * 場合({@code =}{@value #RESULT_NOT_INITIALIZED})。
     */
    public static final int RESULT_NOT_INITIALIZED = RESULT_FIRST_USER + 6;
    /**
     * 端末のおサイフケータイロックのため FeliCa デバイスを使用できない
     * 場合({@code =}{@value #RESULT_DEVICE_LOCKED})。
     */
    public static final int RESULT_DEVICE_LOCKED = RESULT_FIRST_USER + 7;

    /**
     * KuShiKaTsu がもサウンドの名前({@value #SOUND_1})の定数です。
     */
    public static final String SOUND_1 = "se1";
    /**
     * KuShiKaTsu がもサウンドの名前({@value #SOUND_2})の定数です。
     */
    public static final String SOUND_2 = "se2";
    /**
     * KuShiKaTsu がもサウンドの名前({@value #SOUND_3})の定数です。
     */
    public static final String SOUND_3 = "se3";

    private KushikatsuHelper() {
        throw new AssertionError("instatiation prohibited.");
    }

    /**
     * KuShiKaTsuインストールチェック。
     *
     * <p>
     * 正確には、 {@code ACTION} が
     * {@value jp.andeb.kushikatsu.helper.KushikatsuHelper.SendIntent#ACTION}
     * である{@link Intent} に応答できるアプリが存在するかどうかをチェックします。
     * </p>
     *
     * @param context
     * パッケージマネージャ取得用のコンテキスト。{@code null} 禁止。
     * @return
     * インストールされていれば {@code true}、インストールされていなければ {@code false} を
     * 返します。
     * @throws IllegalArgumentException
     * {@code null} 禁止の引き数に {@code null} を渡した場合。
     */
    public static boolean isKushikatsuInstalled(final Context context) {
        if (context == null) {
            throw new IllegalArgumentException("'context' must not be null.");
        }
        final PackageManager pm = context.getPackageManager();

        final Intent intent = new Intent(SendIntent.ACTION);
        final List<ResolveInfo> resolveInfo = pm.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);

        final boolean result = (!resolveInfo.isEmpty());
        return result;
    }

    /**
     * KuShiKaTsuのマーケット画面を表示するための {@link Intent} を構築します。
     */
    public static Intent buildIntentForKushikatsuInstall() {
        final Uri uri = Uri.parse("market://details?id=" + PACKAGE_NAME);
        final Intent i = new Intent(Intent.ACTION_VIEW, uri);
        return i;
    }

    /**
     * 相手端末に {@link Intent} を送信するための {@link Intent} を構築します。
     *
     * @param remoteIntent
     * 相手端末に送信される {@link Intent}。{@code null} 禁止。
     * @return
     * KuShiKaTsu を使用して相手端末に {@link Intent} を送信するための {@link Intent}。
     * @throws IllegalArgumentException
     * {@code null} 禁止の引き数に {@code null} が渡された場合。
     */
    public static Intent buildIntentForSendIntent(Intent remoteIntent) {
        if (remoteIntent == null) {
            throw new IllegalArgumentException(
                    "'remoteIntent' must not be null.");
        }
        final Intent intent = new Intent(SendIntent.ACTION);
        intent.addCategory(SendIntent.CATEGORY);
        intent.putExtra(SendIntent.EXTRA_INTENT, remoteIntent);
        return intent;
    }

    /**
     * 相手端末にブラウザ起動要求を送信するための {@link Intent} を構築します。
     *
     * @param url
     * 相手端末に伝える URL 文字列。{@code null} 禁止。
     * @param browserParam
     * 相手端末でブラウザを起動する際の起動パラメータ。指定できる値は相手の端末に依存します。
     * 起動パラメータ不要な場合は {@code null} を指定してください。
     * @return
     * KuShiKaTsu を使用して相手端末にブラウザ起動要求を送信するための {@link Intent}。
     * @throws IllegalArgumentException
     * {@code null} 禁止の引き数に {@code null} が渡された場合。
     */
    public static Intent buildIntentForStartBrowser(String url,
            String browserParam) {
        if (url == null) {
            throw new IllegalArgumentException("'url' must not be null.");
        }
        final Intent intent = new Intent(StartBrowser.ACTION);
        intent.addCategory(StartBrowser.CATEGORY);
        intent.putExtra(StartBrowser.EXTRA_URL, url);
        if (browserParam != null) {
            intent.putExtra(StartBrowser.EXTRA_BROWSER_PARAM, browserParam);
        }
        return intent;
    }

    /**
     * 相手端末にメーラ起動要求を送信するための {@link Intent} を構築します。
     *
     * @param to
     * 相手端末のメール編集画面に表示する宛先アドレスの配列。
     * 宛先なしの場合は {@code null} を指定してください。
     * @param cc
     * 相手端末のメール編集画面に表示するCCアドレスの配列。
     * CCなしの場合は {@code null} を指定してください。
     * @param subject
     * 相手端末のメール編集画面に表示するサブジェクト。
     * サブジェクトなしの場合は {@code null} を指定してください。
     * @param body
     * 相手端末のメール編集画面に表示する本文。
     * 本文なしの場合は {@code null} を指定してください。
     * @param mailerParam
     * 相手端末でメーラを起動する際の起動パラメータ。指定できる値は相手の端末に依存します。
     * 起動パラメータ不要な場合は {@code null} を指定してください。
     * @return
     * KuShiKaTsu を使用して相手端末にメーラ起動要求を送信するための {@link Intent}。
     */
    public static Intent buildIntentForStartMailer(String[] to, String[] cc,
            String subject, String body, String mailerParam) {
        final Intent intent = new Intent(StartMailer.ACTION);
        intent.addCategory(StartMailer.CATEGORY);
        if (to != null) {
            intent.putExtra(StartMailer.EXTRA_EMAIL, to);
        }
        if (cc != null) {
            intent.putExtra(StartMailer.EXTRA_CC, cc);
        }
        if (subject != null) {
            intent.putExtra(StartMailer.EXTRA_SUBJECT, subject);
        }
        if (body != null) {
            intent.putExtra(StartMailer.EXTRA_TEXT, body);
        }
        if (mailerParam != null) {
            intent.putExtra(StartMailer.EXTRA_MAIL_PARAM, mailerParam);
        }
        return intent;
    }

    /**
     * KuShiKaTsu への送信要求インテントに、 Push 送信タイムアウトを指定します。
     *
     * @param intent
     * 送信要求インテント。 {@link #buildIntentForSendIntent(Intent)}、
     * {@link #buildIntentForStartBrowser(String, String)}、
     * {@link #buildIntentForStartMailer(String[], String[], String, String, String)}
     * で作成した {@link Intent} を渡してください。{@code null} 禁止。
     * @param timeoutSec
     * タイムアウトまでの時間を秒数で指定します。{@code 1} 以上の値を指定してください。
     * @throws IllegalArgumentException
     * 以下の場合にスローされます。
     * <ul>
     *   <li>{@code null} 禁止の引き数に {@code null} を渡した場合</li>
     *   <li>正数のみの引き数に {@code 0} や負数を渡した場合</li>
     * </ul>
     */
    public static void setSendTimeout(Intent intent, int timeoutSec) {
        if (intent == null) {
            throw new IllegalArgumentException("'intent' must not be null.");
        }
        if (timeoutSec <= 0) {
            throw new IllegalArgumentException("'timeoutSec' must be positive.");
        }
        intent.putExtra(CommonParam.EXTRA_SEND_TIMEOUT, timeoutSec);
    }

    /**
     * KuShiKaTsu への送信要求インテントに、 Push 送信成功時のサウンドを指定します。
     *
     * @param intent
     * 送信要求インテント。 {@link #buildIntentForSendIntent(Intent)}、
     * {@link #buildIntentForStartBrowser(String, String)}、
     * {@link #buildIntentForStartMailer(String[], String[], String, String, String)}
     * で作成した {@link Intent} を渡してください。{@code null} 禁止。
     * @param soundName
     * KuShiKaTsu が保持するサウンドの名前。 {@link #SOUND_1} または {@link #SOUND_2}
     * または {@link #SOUND_3} を指定してください。{@code null} 禁止。
     * @throws IllegalArgumentException
     * {@code null} 禁止の引き数に {@code null} を渡した場合。
     */
    public static void setSoundOnSent(Intent intent, String soundName) {
        if (intent == null) {
            throw new IllegalArgumentException("'intent' must not be null.");
        }
        if (soundName == null) {
            throw new IllegalArgumentException("'soundName' must not be null.");
        }
        intent.putExtra(CommonParam.EXTRA_SOUND_ON_SENT, soundName);
    }

    /**
     * KuShiKaTsu への送信要求インテントに、 Push 送信成功時のサウンドを指定します。
     *
     * @param intent
     * 送信要求インテント。 {@link #buildIntentForSendIntent(Intent)}、
     * {@link #buildIntentForStartBrowser(String, String)}、
     * {@link #buildIntentForStartMailer(String[], String[], String, String, String)}
     * で作成した {@link Intent} を渡してください。{@code null} 禁止。
     * @param soundResId
     * 送信依頼側のアプリが持つサウンドリソースの識別子。
     * @throws IllegalArgumentException
     * {@code null} 禁止の引き数に {@code null} を渡した場合。
     */
    public static void setSoundOnSent(Intent intent, int soundResId) {
        if (intent == null) {
            throw new IllegalArgumentException("'intent' must not be null.");
        }
        intent.putExtra(CommonParam.EXTRA_SOUND_ON_SENT, soundResId);
    }

    /**
     * KuShiKaTsu がインストールされていれば指定された {@link Intent} でアクティビティを
     * 開始します。インストールされていない場合はマーケットの KuShiKaTsu ページを表示するための
     * アクティビティを開始します。
     *
     * <p>
     * インストールされている場合は、{@link Activity#startActivityForResult(Intent, int)}
     * でアクティビティを開始するので、 {@code activity} の {@code onActivityResult()} で
     * リザルトコードを受け取ることができます。
     * </p>
     * <p>
     * インストールされていない場合は、 {@link Activity#startActivity(Intent)} でインストール
     * 用のアクティビティを開始します。
     * </p>
     *
     * @param activity
     * 起動元となるアクティビティ。KuShiKaTsu が起動された場合、このアクティビティが処理結果を
     * 受け取ります。
     * @param intent
     * KuShiKaTsu を起動するための {@link Intent}。 {@code null} 禁止。
     * @param requestCode
     * KuShiKaTsu のアクティビティを起動する際のリクエストコード。
     * @return
     * KuShiKaTsu を起動したかどうか。起動した場合は {@code true}、インストール用の画面を
     * 開くアクティビティを起動した場合は {@code false}。
     * @throws IllegalArgumentException
     * {@code null} 禁止の引き数に {@code null} を渡した場合。
     */
    public static boolean startKushikatsuForResult(final Activity activity,
            final Intent intent, final int requestCode, int installRequestCode) {
        if (activity == null) {
            throw new IllegalArgumentException("'activity' must not be null.");
        }
        if (!isKushikatsuInstalled(activity)) {
            // 未インストールの場合
            final Intent intentForInstall = buildIntentForKushikatsuInstall();
            activity.startActivityForResult(intentForInstall,
                    installRequestCode);
            return false;
        }
        // インストール済みの場合
        activity.startActivityForResult(intent, requestCode);
        return true;
    }

}
