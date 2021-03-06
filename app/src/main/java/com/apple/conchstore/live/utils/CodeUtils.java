package com.apple.conchstore.live.utils;

/**
 * Created by tantan on 2017/8/7.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by yao
 * 用于图片验证码的工具类
 */
public class CodeUtils {
    private static final String[] CHARS1 = {
            "0","1","2","3","4","5","6","7","8","9"
    };
    private static final String[] CHARS2 = {
            "加","减","乘以"
    };

    private static CodeUtils mCodeUtils;
    private int mPaddingLeft, mPaddingTop;
    private StringBuilder mBuilder = new StringBuilder();
    private Random mRandom = new Random();

    //Default Settings
    private static final int DEFAULT_CODE_LENGTH = 6;//验证码的长度  这里是6位
    private static final int DEFAULT_FONT_SIZE = 60;//字体大小
    private static final int DEFAULT_LINE_NUMBER = 3;//多少条干扰线
    private static final int BASE_PADDING_LEFT = 20; //左边距
    private static final int RANGE_PADDING_LEFT = 30;//左边距范围值
    private static final int BASE_PADDING_TOP = 65;//上边距
    private static final int RANGE_PADDING_TOP = 13;//上边距范围值
    private static final int DEFAULT_WIDTH = 300;//默认宽度.图片的总宽
    private static final int DEFAULT_HEIGHT = 90;//默认高度.图片的总高
    private static final int DEFAULT_COLOR = 0xDF;//默认背景颜色值

    private String code;
    private int result;

    public static CodeUtils getInstance() {
        if(mCodeUtils == null) {
            mCodeUtils = new CodeUtils();
        }
        return mCodeUtils;
    }

    //生成验证码图片
    public Bitmap createBitmap() {
        mPaddingLeft = 0; //每次生成验证码图片时初始化
        mPaddingTop = 0;

        Bitmap bitmap = Bitmap.createBitmap(DEFAULT_WIDTH, DEFAULT_HEIGHT, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        code = createCode();

        canvas.drawColor(Color.rgb(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR));
        Paint paint = new Paint();
        paint.setTextSize(DEFAULT_FONT_SIZE);

        for (int i = 0; i < code.length(); i++) {
            randomTextStyle(paint);
            randomPadding();
            canvas.drawText(code.charAt(i) + "" , mPaddingLeft, mPaddingTop, paint);
        }

        //干扰线
        for (int i = 0; i < DEFAULT_LINE_NUMBER; i++) {
            drawLine(canvas, paint);
        }

        canvas.save(Canvas.ALL_SAVE_FLAG);//保存
        canvas.restore();
        return bitmap;
    }
    /**
     * 得到图片中的验证码字符串
     * @return
     */
    public String getCode() {
        return code;
    }
    /**
     * 得到图片中的验证码计算的结果
     * @return
     */
    public int getResult() {
        return result;
    }

    //生成验证码
    public String createCode() {
        mBuilder.delete(0, mBuilder.length()); //使用之前首先清空内容

//        for (int i = 0; i < DEFAULT_CODE_LENGTH; i++) {
//            mBuilder.append(CHARS[mRandom.nextInt(CHARS.length)]);
//        }
        int position1 = mRandom.nextInt(CHARS1.length);
        mBuilder.append(CHARS1[position1]);
        int position2 = mRandom.nextInt(CHARS2.length);
        mBuilder.append(CHARS2[position2]);
        int position3;
        if (1==position2){
            position3=mRandom.nextInt(position1+1);
            mBuilder.append(CHARS1[position3]);
            result=position1-position3;
        }else {
            position3=mRandom.nextInt(CHARS1.length);
            mBuilder.append(CHARS1[position3]);
            if (0==position2){
                result=position1+position3;
            }else {
                result=position1*position3;
            }
        }

        return mBuilder.toString();
    }

    //生成干扰线
    private void drawLine(Canvas canvas, Paint paint) {
        int color = randomColor();
        int startX = mRandom.nextInt(DEFAULT_WIDTH);
        int startY = mRandom.nextInt(DEFAULT_HEIGHT);
        int stopX = mRandom.nextInt(DEFAULT_WIDTH);
        int stopY = mRandom.nextInt(DEFAULT_HEIGHT);
        paint.setStrokeWidth(1);
        paint.setColor(color);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    //随机颜色
    private int randomColor() {
        mBuilder.delete(0, mBuilder.length()); //使用之前首先清空内容

        String haxString;
        for (int i = 0; i < 3; i++) {
            haxString = Integer.toHexString(mRandom.nextInt(0xFF));
            if (haxString.length() == 1) {
                haxString = "0" + haxString;
            }

            mBuilder.append(haxString);
        }

        return Color.parseColor("#" + mBuilder.toString());
    }

    //随机文本样式
    private void randomTextStyle(Paint paint) {
        int color = randomColor();
        paint.setColor(color);
        paint.setFakeBoldText(mRandom.nextBoolean());  //true为粗体，false为非粗体
        float skewX = mRandom.nextInt(11) / 10;
        skewX = mRandom.nextBoolean() ? skewX : -skewX;
        paint.setTextSkewX(skewX); //float类型参数，负数表示右斜，整数左斜
//        paint.setUnderlineText(true); //true为下划线，false为非下划线
//        paint.setStrikeThruText(true); //true为删除线，false为非删除线
    }

    //随机间距
    private void randomPadding() {
        mPaddingLeft += BASE_PADDING_LEFT + mRandom.nextInt(RANGE_PADDING_LEFT)+20;
        mPaddingTop = BASE_PADDING_TOP + mRandom.nextInt(RANGE_PADDING_TOP);
    }
}
