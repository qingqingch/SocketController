package com.ruishang.socketcontroller.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.text.Editable;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * UI的帮助类
 * 
 * @author wang.xy<br>
 * @version 2013-08-02 xu.xb 加入移动EditText光标的方法<br>
 * 
 */
public class UIUtil {
	/**
	 * 设置view的高度
	 * 
	 * @param view
	 *            指定的view
	 * @param height
	 *            指定的高度，以像素为单位
	 */
	public static void setViewHeight(View view, int height) {
		ViewGroup.LayoutParams params = view.getLayoutParams();
		params.height = height;
		view.setLayoutParams(params);
	}

	/**
	 * 设置不同颜色的文字
	 * 
	 * @param startPos
	 *            需要文字颜色不同的开始位置
	 * @param endPos
	 *            需要文字颜色不同的结束位置
	 * @param text
	 *            文字内容
	 * @param color
	 *            需要转化成的颜色
	 * @param tv
	 *            需要操作的textview
	 */
	public static void setColorfulText(int startPos, int endPos, String text, int color, TextView tv) {
		SpannableStringBuilder builder = new SpannableStringBuilder(text);
		builder.setSpan(new ForegroundColorSpan(color), startPos, endPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setText(builder);
	}

	/**
	 * 设置删除线
	 * 
	 * @param startPos
	 *            需要删除线的开始位置
	 * @param endPos
	 *            需要删除线的结束位置
	 * @param text
	 *            文字内容
	 * @param tv
	 *            需要操作的textview
	 */
	public static void setDeleteLineText(int startPos, int endPos, String text, TextView tv) {
		SpannableStringBuilder style = new SpannableStringBuilder(text);
		style.setSpan(new StrikethroughSpan(), startPos, endPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setText(style);
	}

	/**
	 * dip转换为px
	 * 
	 * @param context
	 *            上下文对象
	 * @param dipValue
	 *            dip值
	 * @return px值
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * px转换为dip
	 * 
	 * @param context
	 *            上下文对象
	 * @param pxValue
	 *            px值
	 * @return dip值
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static ImageView getImageViewFromBitmap(Context ctx, Bitmap bitmap) {
		ImageView imageView = new ImageView(ctx);
		imageView.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
		imageView.setAdjustViewBounds(true);
		imageView.setImageBitmap(bitmap);
		return imageView;
	}

	/**
	 * 把View绘制到Bitmap上
	 * 
	 * @param view
	 *            需要绘制的View
	 * @param width
	 *            该View的宽度
	 * @param height
	 *            该View的高度
	 * @return 返回Bitmap对象
	 */
	public static Bitmap getBitmapFromView(View view, int width, int height, Config bitmapConfig) {
		int widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
		int heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
		view.measure(widthSpec, heightSpec);
		view.layout(0, 0, width, height);
		Bitmap bitmap = Bitmap.createBitmap(width, height, bitmapConfig);
		Canvas canvas = new Canvas(bitmap);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
		view.draw(canvas);
		return bitmap;
	}

	/**
	 * 移动光标到最后
	 * 
	 * @param editText
	 *            输入框
	 */
	public static void moveCursolToEnd(EditText editText) {
		if (editText == null) {
			return;
		}
		Editable text = editText.getText();
		if (text != null) {
			Selection.setSelection(text, text.length());
		}
	}

	/**
	 * 移动光标到指定位置
	 * 
	 * @param editText
	 *            输入框
	 * @param index
	 *            位置
	 */
	public static void moveCursolToIndex(EditText editText, int index) {
		if (editText == null) {
			return;
		}
		Editable text = editText.getText();
		if (text != null) {
			Selection.setSelection(text, text.length());
		}
	}

	/**
	 * 设置View显示,判断了是否已显示
	 */
	public static void setViewVisible(View view) {
		if (view != null && view.getVisibility() != View.VISIBLE) {
			view.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 设置ViewGone,判断了是否已显示
	 */
	public static void setViewGone(View view) {
		if (view != null && view.getVisibility() == View.VISIBLE) {
			view.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置粗体
	 * 
	 * @param textView
	 */
	public static void setBoldText(TextView textView) {
		if (textView == null) {
			return;
		}
		textView.getPaint().setFakeBoldText(true);
	}
}
