package com.ysy.voicediary.ui.activity;

import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.ysy.voicediary.Constants;
import com.ysy.voicediary.R;
import com.ysy.voicediary.base.BaseActivity;
import com.ysy.voicediary.bean.DiaryBean;
import com.ysy.voicediary.utils.DataBaseUtil;
import com.ysy.voicediary.utils.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 写日记
 */
public class MainActivity extends BaseActivity {


    @BindView(R.id.ed_input)
    EditText edInput;
    @BindView(R.id.bt_record)
    LinearLayout btRecord;
    @BindView(R.id.update_time)
    TextView updateTime;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.tv_textLength)
    TextView tvTextLength;
    @BindView(R.id.bt_complete)
    Button btComplete;
    @BindView(R.id.et_title)
    EditText etTitle;

    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private StringBuffer buffer = new StringBuffer();

    int ret = 0; // 函数调用返回值

    private int diary_type;//日记类型

    private long diary_id = -1;//日记id

    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        diary_type = getIntent().getIntExtra(Constants.DIARY_TYPE, 0);
        //如果是修改
        if(getIntent().getLongExtra(Constants.UPDATE_ID,-1) != -1){
            diary_id = getIntent().getLongExtra(Constants.UPDATE_ID,-1);
            DiaryBean diaryBean = DataBaseUtil.getInstance().getDaoSession().getDiaryBeanDao().load(diary_id);
            etTitle.setText(diaryBean.getTitle());
            edInput.setText(diaryBean.getContent());
            updateTime.setText(TimeUtils.millis2String(diaryBean.getUpdate_time()));
            tvTextLength.setText(diaryBean.getContent().length()+"字");
        }else{
            updateTime.setText(TimeUtils.getNowString());
        }
        edInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvTextLength.setText(s.length() + "字");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void initData() {
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(this, mInitListener);
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                LogUtils.d("test1", "初始化失败，错误码：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
                ToastUtils.showShort("初始化失败，错误码：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            }
        }
    };

    @OnClick({R.id.bt_record, R.id.bt_complete})
    public void onViewClicked(View view) {
        if (null == mIat) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            ToastUtils.showShort("创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
            return;
        }
        switch (view.getId()) {
            case R.id.bt_record:
                buffer.setLength(0);
                // 设置参数
                setParam();
                boolean isShowDialog = true;
                if (isShowDialog) {
                    // 显示听写对话框
                    mIatDialog.setListener(mRecognizerDialogListener);
                    mIatDialog.show();
                    ToastUtils.showShort("请开始说话");
                } else {
                    // 不显示听写对话框
//                    ret = mIat.startListening(mRecognizerListener);
                    if (ret != ErrorCode.SUCCESS) {
                        ToastUtils.showShort("听写失败,错误码：" + ret + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
                        LogUtils.d("test1", "听写失败,错误码：" + ret + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
                    } else {
                        ToastUtils.showShort("请开始说话");
                    }
                }
                break;
            case R.id.bt_complete://完成
                if (etTitle.getText().length() == 0) {
                    ToastUtils.showShort("请填写标题");
                    return;
                }
                if (edInput.getText().length() == 0) {
                    ToastUtils.showShort("请填写内容");
                    return;
                }
                if(getIntent().getLongExtra(Constants.UPDATE_ID,-1) != -1){
                    DiaryBean diaryBean = new DiaryBean();
                    diaryBean.setDiaryId(diary_id);
                    diaryBean.setTitle(etTitle.getText().toString());
                    diaryBean.setContent(edInput.getText().toString());
                    diaryBean.setType(diary_type);
                    diaryBean.setUpdate_time(TimeUtils.getNowMills());
                    DataBaseUtil.getInstance().getDaoSession().getDiaryBeanDao().update(diaryBean);
                }else{
                    DiaryBean diaryBean = new DiaryBean();
                    diaryBean.setTitle(etTitle.getText().toString());
                    diaryBean.setContent(edInput.getText().toString());
                    diaryBean.setType(diary_type);
                    diaryBean.setUpdate_time(TimeUtils.getNowMills());
                    DataBaseUtil.getInstance().getDaoSession().getDiaryBeanDao().insert(diaryBean);
                }
                finish();
                break;
        }
    }

    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            String text = JsonParser.parseIatResult(results.getResultString());

            String sn = null;
            // 读取json结果中的sn字段
            try {
                JSONObject resultJson = new JSONObject(results.getResultString());
                sn = resultJson.optString("sn");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mIatResults.put(sn, text);

            StringBuffer resultBuffer = new StringBuffer();
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults.get(key));
            }

            edInput.setText(edInput.getText().toString() + resultBuffer.toString());
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            ToastUtils.showShort(error.getErrorDescription());
            LogUtils.d("test1", error.getErrorCode() + ":" + error.getErrorDescription());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
//    /**
//     * 听写监听器。
//     */
//    private RecognizerListener mRecognizerListener = new RecognizerListener() {
//
//        @Override
//        public void onBeginOfSpeech() {
//            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
//            showTip("开始说话");
//        }
//
//        @Override
//        public void onError(SpeechError error) {
//            // Tips：
//            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
//
//            showTip(error.getPlainDescription(true));
//
//        }
//
//        @Override
//        public void onEndOfSpeech() {
//            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
//            showTip("结束说话");
//        }
//
//        @Override
//        public void onResult(RecognizerResult results, boolean isLast) {
//            Log.d(TAG, results.getResultString());
//            System.out.println(flg++);
//            if (resultType.equals("json")) {
//
//                printResult(results);
//
//            }else if(resultType.equals("plain")) {
//                buffer.append(results.getResultString());
//                mResultText.setText(buffer.toString());
//                mResultText.setSelection(mResultText.length());
//            }
//
//            if (isLast & cyclic) {
//                // TODO 最后的结果
//                Message message = Message.obtain();
//                message.what = 0x001;
//                han.sendMessageDelayed(message,100);
//            }
//        }
//
//        @Override
//        public void onVolumeChanged(int volume, byte[] data) {
//            ToastUtils.showShort("当前正在说话，音量大小：" + volume);
//            LogUtils.d("test1", "返回音频数据："+data.length);
//        }
//
//        @Override
//        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
//            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
//            // 若使用本地能力，会话id为null
//            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
//            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
//            //		Log.d(TAG, "session id =" + sid);
//            //	}
//        }
//    };
}
