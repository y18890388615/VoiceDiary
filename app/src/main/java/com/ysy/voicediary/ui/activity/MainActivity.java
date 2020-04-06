package com.ysy.voicediary.ui.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
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
import com.ysy.voicediary.bean.Section;
import com.ysy.voicediary.utils.DataBaseUtil;
import com.ysy.voicediary.utils.DialogUtil;
import com.ysy.voicediary.utils.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 写日记
 */
public class MainActivity extends BaseActivity implements AMapLocationListener {


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
    @BindView(R.id.starBar)
    RatingBar starBar;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.rl_time)
    RelativeLayout rlTime;

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

    private String address;//地址

    private boolean isOK;//是否完成地址变蓝

    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        diary_type = getIntent().getIntExtra(Constants.DIARY_TYPE, 0);
        if (diary_type == Constants.AFFAIRS) {//如果是待办，就要多重要级和时间
            starBar.setVisibility(View.VISIBLE);
            rlTime.setVisibility(View.VISIBLE);
        } else {
            starBar.setVisibility(View.INVISIBLE);
            rlTime.setVisibility(View.GONE);
        }
        //如果是修改
        if (getIntent().getLongExtra(Constants.UPDATE_ID, -1) != -1) {
            diary_id = getIntent().getLongExtra(Constants.UPDATE_ID, -1);
            DiaryBean diaryBean = DataBaseUtil.getInstance().getDaoSession().getDiaryBeanDao().load(diary_id);
            etTitle.setText(diaryBean.getTitle());
            edInput.setText(diaryBean.getContent());
            updateTime.setText(TimeUtils.millis2String(diaryBean.getUpdate_time()));
            tvTextLength.setText(diaryBean.getContent().length() + "字");
            starBar.setRating(diaryBean.getImportant());
            tvTime.setText(diaryBean.getTime());//修改时间
        } else {
            updateTime.setText(TimeUtils.getNowString());
        }
        edInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvTextLength.setText(s.length() + "字");
                if (edInput.length() >= 3 && !isOK) {
                    if (edInput.getText().toString().contains("省") || edInput.getText().toString().contains("市")) {
                        List<Section> sections = searchAllIndex(s.toString());
                        SpannableString ss = new SpannableString(edInput.getText().toString());
                        //设置0-2的字符颜色
                        for(int i =0;i<sections.size();i++){
//                            ss.setSpan(new ForegroundColorSpan(Color.RED), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            ss.setSpan(new URLSpan("https://www.baidu.com/s?ie=UTF-8&wd="+
                                            s.toString().substring(sections.get(i).start,sections.get(i).end)),
                                    sections.get(i).start, sections.get(i).end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
//                        //设置2-5的字符链接到电话簿，点击时触发拨号
//                        ss.setSpan(new URLSpan("tel:4155551212"), 2, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //设置9-11的字符为网络链接，点击时打开页面
//                        //设置13-15的字符点击时，转到写短信的界面，发送对象为10086
//                        ss.setSpan(new URLSpan("sms:10086"), 13, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        //粗体
//                        ss.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 5, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        //斜体
//                        ss.setSpan(new StyleSpan(android.graphics.Typeface.ITALIC), 7, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        //下划线
//                        ss.setSpan(new UnderlineSpan(), 10, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        //设置文本内容到textView
                        isOK = true;
                        edInput.setText(ss);
                        //不添加这一句，拨号，http，发短信的超链接不能执行.
                        edInput.setMovementMethod(LinkMovementMethod.getInstance());
                        edInput.setSelection(edInput.getText().length());
                    }
                }else{
                    isOK = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 查找省和市的位置
     * @param str
     * @return
     */
    private List<Section> searchAllIndex(String str) {
        List<Section> sections = new ArrayList<>();
        for(int i = 0;i<str.length();i++){
            if("省".equals(String.valueOf(str.charAt(i)))){
                int a = str.indexOf("省");//*第一个出现的索引位置
                sections.add(new Section(a - 2, a + 1));
            }else if("市".equals(String.valueOf(str.charAt(i)))){
                int a = str.indexOf("市");//*第一个出现的索引位置
                sections.add(new Section(a - 2, a + 1));
            }
        }
        return sections;

    }

    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Override
    public void initData() {
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(this, mInitListener);
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
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

    @OnClick({R.id.bt_record, R.id.bt_complete, R.id.tv_time})
    public void onViewClicked(View view) {
        if (null == mIat) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            ToastUtils.showShort("创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
            return;
        }
        switch (view.getId()) {
            case R.id.bt_record://语音录入
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
                starBar.getNumStars();
                if (getIntent().getLongExtra(Constants.UPDATE_ID, -1) != -1) {
                    DiaryBean diaryBean = new DiaryBean();
                    diaryBean.setDiaryId(diary_id);
                    diaryBean.setTitle(etTitle.getText().toString());
                    diaryBean.setContent(edInput.getText().toString());
                    diaryBean.setType(diary_type);
                    diaryBean.setTime(tvTime.getText().toString());
                    diaryBean.setImportant((int) starBar.getRating());
                    diaryBean.setAddress(address);
                    diaryBean.setUpdate_time(TimeUtils.getNowMills());
                    DataBaseUtil.getInstance().getDaoSession().getDiaryBeanDao().update(diaryBean);
                } else {
                    if(diary_type == Constants.AFFAIRS){
                        if(tvTime.getText().toString().equals("")){
                            ToastUtils.showShort("请填写日期");
                            return;
                        }
                    }
                    DiaryBean diaryBean = new DiaryBean();
                    diaryBean.setTitle(etTitle.getText().toString());
                    diaryBean.setContent(edInput.getText().toString());
                    diaryBean.setType(diary_type);
                    diaryBean.setTime(tvTime.getText().toString());
                    diaryBean.setImportant((int) starBar.getRating());
                    diaryBean.setPriority(0);
                    diaryBean.setAccount(SPUtils.getInstance().getString(Constants.ACCOUNT));
                    diaryBean.setAddress(address);
                    diaryBean.setUpdate_time(TimeUtils.getNowMills());
                    DataBaseUtil.getInstance().getDaoSession().getDiaryBeanDao().insert(diaryBean);
                }
                finish();
                break;
            case R.id.tv_time://选择时间
                DialogUtil.showDatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        tvTime.setText(year + "-" + (month + 1) + "-" + dayOfMonth);

                    }
                });
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

    private String save = "";
    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            String text = JsonParser.parseIatResult(results.getResultString());
            if (isLast) {
                return;
            }
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
            if (!save.equals(resultBuffer.toString())) {
                edInput.setText(edInput.getText().toString() + resultBuffer.toString());
                edInput.setSelection(edInput.getText().length());
                save = resultBuffer.toString();
            }
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

    /**
     * 地图对调
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                address = amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                amapLocation.getCountry();//国家信息
                amapLocation.getProvince();//省信息
                amapLocation.getCity();//城市信息
                amapLocation.getDistrict();//城区信息
                amapLocation.getStreet();//街道信息
                amapLocation.getStreetNum();//街道门牌号信息
                amapLocation.getCityCode();//城市编码
                amapLocation.getAdCode();//地区编码
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
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
