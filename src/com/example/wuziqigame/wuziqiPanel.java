package com.example.wuziqigame;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class wuziqiPanel extends View {
	private int mPanelWidth;

	// 为了防止出线误差，使用float类型
	private float mLineHeight;
	private int MAX_LINE = 10;
	private int MAX_COUNT_IN_LINE = 5;

	public Paint mPaint;

	private Bitmap mWhitePiece;
	private Bitmap mBlackPiece;
	// 比例确定，棋子大小
	private float radioPieceOfLineHeight = 3 * 1.0f / 4;
	// 申明谁先手
	private boolean mIsWhite = true;
	// 申明一个集合存放旗子的位置信息
	private ArrayList<Point> mWhiteArray = new ArrayList<Point>();
	private ArrayList<Point> mBlackArray = new ArrayList<Point>();

	// 判断游戏是否结束并且谁是赢家
	private boolean mIsGameOver;
	private boolean mIsWhiteWinner;

	public wuziqiPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint();
		init();
	}

	// 初始化笔
	private void init() {
		// TODO Auto-generated method stub
		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(5);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		Log.d("MainActivity", "init");
		mWhitePiece = BitmapFactory.decodeResource(getResources(),
				R.drawable.stone_w2);
		mBlackPiece = BitmapFactory.decodeResource(getResources(),
				R.drawable.stone_b1);

	}

	// 对自己界面就行测量
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);

		int heighttSize = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		// 期盼的宽度
		int width = Math.min(widthSize, heighttSize);
		if (widthMode == MeasureSpec.UNSPECIFIED) {
			width = heighttSize;
		} else if (heightMode == MeasureSpec.UNSPECIFIED) {
			width = widthSize;
		}
		setMeasuredDimension(width, width);
	}

	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		Log.d("MainActivity", "go");
		super.onDraw(canvas);
		// 绘制棋盘
		drawBoard(canvas);
		// 绘制棋子
		drawPiece(canvas);
		checkGameOver();
	}

	 protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	 super.onSizeChanged(w, h, oldw, oldh);
	 mPanelWidth = w;
	 mLineHeight = mPanelWidth * 1.0f/ MAX_LINE ;
	
	 int pieceWidth = (int) (mLineHeight * radioPieceOfLineHeight);
	 mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceWidth,
	 pieceWidth, false);
	 mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, pieceWidth,
	 pieceWidth, false);
	
	 }

	public boolean onTouchEvent(MotionEvent event) {
		// 如果游戏结束，让下面的方法不产生反应
		if (mIsGameOver)
			return false;
		int action = event.getAction();
		if (action == MotionEvent.ACTION_UP) {

			int x = (int) event.getX();
			int y = (int) event.getY();

			Point p = getValidPoint(x, y);
			Log.d("MainActivity", "点击了:" + "x" + x + "y" + y);
			if (mWhiteArray.contains(p) || mBlackArray.contains(p)) {
				return false;
			}
			if (mIsWhite) {
				mWhiteArray.add(p);
			} else {
				mBlackArray.add(p);
			}
			Log.d("MainActivity", "重绘");
			// 重绘
			invalidate();
			Log.d("MainActivity", "重绘成功");
			mIsWhite = !mIsWhite;
		}

		return true;
	}

	private Point getValidPoint(int x, int y) {
		// TODO Auto-generated method stub
		return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));

	}
	
	// 逻辑判断是否结束游戏
	private void checkGameOver() {
		// TODO Auto-generated method stub
		boolean whiteWin = chechFiveInLine(mWhiteArray);
		boolean blackWin = chechFiveInLine(mBlackArray);
		if (whiteWin || blackWin) {
			mIsGameOver = true;
			mIsWhiteWinner = whiteWin;

			String text = mIsWhiteWinner ? "白棋胜利" : "黑棋胜利";
			Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
		}
	}

	private boolean chechFiveInLine(ArrayList<Point> points) {
		// TODO Auto-generated method stub
		for (Point p : points) {
			int x = p.x;
			int y = p.y;
			boolean win = checkHorizontal(x, y, points);
			if (win)
				return true;
			win = checkVertical(x, y, points);
			if (win)
				return true;
			win = checkLeftDiagonal(x, y, points);
			if (win)
				return true;
			win = checkRightDiagonal(x, y, points);
			if (win)
				return true;
		}
		return false;
	}

	// 判断横向是否达到目标
	private boolean checkHorizontal(int x, int y, List<Point> points) {
		int count = 1;
		// 左
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
			if (points.contains(new Point(x - i, y))) {
				count++;
			} else {
				break;
			}
		}
		if (count == MAX_COUNT_IN_LINE)
			return true;
		// 右
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
			if (points.contains(new Point(x + i, y))) {
				count++;
			} else {
				break;
			}
		}
		if (count == MAX_COUNT_IN_LINE)
			return true;
		return false;
	}

	// 垂直
	private boolean checkVertical(int x, int y, List<Point> points) {
		int count = 1;
		// 上
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
			if (points.contains(new Point(x, y + i))) {
				count++;
			} else {
				break;
			}
		}
		if (count == MAX_COUNT_IN_LINE)
			return true;
		// 下
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
			if (points.contains(new Point(x, y - i))) {
				count++;
			} else {
				break;
			}
		}
		if (count == MAX_COUNT_IN_LINE)
			return true;
		return false;
	}

	// 右上到左下
	private boolean checkRightDiagonal(int x, int y, List<Point> points) {
		int count = 1;
		// 右上
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
			if (points.contains(new Point(x + i, y - i))) {
				count++;
			} else {
				break;
			}
		}
		if (count == MAX_COUNT_IN_LINE)
			return true;
		// 左上
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
			if (points.contains(new Point(x - i, y + i))) {
				count++;
			} else {
				break;
			}
		}
		if (count == MAX_COUNT_IN_LINE)
			return true;
		return false;
	}

	// 左上到右下判断
	private boolean checkLeftDiagonal(int x, int y, List<Point> points) {
		int count = 1;
		// 左上
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
			if (points.contains(new Point(x - i, y - i))) {
				count++;
			} else {
				break;
			}
		}
		if (count == MAX_COUNT_IN_LINE)
			return true;
		// 右下
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
			if (points.contains(new Point(x + i, y + i))) {
				count++;
			} else {
				break;
			}
		}
		if (count == MAX_COUNT_IN_LINE)
			return true;
		return false;
	}

	private void drawBoard(Canvas canvas) {
		// TODO Auto-generated method stub
		Log.d("MainActivity", "drawBoard");
		int w = mPanelWidth;
		float lineHeight = mLineHeight;
		for (int i = 0; i < MAX_LINE; i++) {
			int startX = (int) (lineHeight / 2);
			int endX = (int) (w - lineHeight / 2);

			int y = (int) ((0.5 + i) * lineHeight);
			// 横线
			canvas.drawLine(startX, y, endX, y, mPaint);
			// 纵向
			canvas.drawLine(y, startX, y, endX, mPaint);
		}

	}

	private void drawPiece(Canvas canvas) {
		// TODO Auto-generated method stub
		Log.d("MainActivity", "drawPiece");
		for (int i = 0, n = mWhiteArray.size(); i < n; i++) {
			Point whitePoint = mWhiteArray.get(i);
			canvas.drawBitmap(mWhitePiece, (whitePoint.x
					+ (1 - radioPieceOfLineHeight) / 2) * mLineHeight,
					(whitePoint.y + (1 - radioPieceOfLineHeight) / 2)
							* mLineHeight, null);
		}
		for (int i = 0, n = mBlackArray.size(); i < n; i++) {
			Point blackPoint = mBlackArray.get(i);
			canvas.drawBitmap(mBlackPiece, (blackPoint.x
					+ (1 - radioPieceOfLineHeight) / 2) * mLineHeight,
					(blackPoint.y + (1 - radioPieceOfLineHeight) / 2)
							* mLineHeight, null);
		}
	}

	public void start() {
		mWhiteArray.clear();
		mBlackArray.clear();
		mIsGameOver = false;
		mIsWhiteWinner = false;
		invalidate();
	}

	/**
	 * View的存储于恢复,必须给予他的xml一个ID
	 * */
	private static final String INSTANCE = "instance";
	private static final String INSTANCE_GAME_OVER = "instance_game_over";
	private static final String INSTANCE_WHITE_ARRAY = "instance_white_array";
	private static final String INSTANCE_BLACK_ARRAY = "instance_black_array";

	@Override
	protected Parcelable onSaveInstanceState() {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
		bundle.putBoolean(INSTANCE_GAME_OVER, mIsGameOver);
		bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY, mWhiteArray);
		bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY, mBlackArray);
		return bundle;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		// TODO Auto-generated method stub
		if (state instanceof Bundle) {
			Bundle bundle = (Bundle) state;
			mIsGameOver = bundle.getBoolean(INSTANCE_GAME_OVER);
			mWhiteArray = bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
			mBlackArray = bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
			super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
			return;
		}
		super.onRestoreInstanceState(state);
	}
}
