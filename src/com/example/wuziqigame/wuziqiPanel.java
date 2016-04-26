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

	// Ϊ�˷�ֹ������ʹ��float����
	private float mLineHeight;
	private int MAX_LINE = 10;
	private int MAX_COUNT_IN_LINE = 5;

	public Paint mPaint;

	private Bitmap mWhitePiece;
	private Bitmap mBlackPiece;
	// ����ȷ�������Ӵ�С
	private float radioPieceOfLineHeight = 3 * 1.0f / 4;
	// ����˭����
	private boolean mIsWhite = true;
	// ����һ�����ϴ�����ӵ�λ����Ϣ
	private ArrayList<Point> mWhiteArray = new ArrayList<Point>();
	private ArrayList<Point> mBlackArray = new ArrayList<Point>();

	// �ж���Ϸ�Ƿ��������˭��Ӯ��
	private boolean mIsGameOver;
	private boolean mIsWhiteWinner;

	public wuziqiPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint();
		init();
	}

	// ��ʼ����
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

	// ���Լ�������в���
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);

		int heighttSize = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		// ���εĿ��
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
		// ��������
		drawBoard(canvas);
		// ��������
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
		// �����Ϸ������������ķ�����������Ӧ
		if (mIsGameOver)
			return false;
		int action = event.getAction();
		if (action == MotionEvent.ACTION_UP) {

			int x = (int) event.getX();
			int y = (int) event.getY();

			Point p = getValidPoint(x, y);
			Log.d("MainActivity", "�����:" + "x" + x + "y" + y);
			if (mWhiteArray.contains(p) || mBlackArray.contains(p)) {
				return false;
			}
			if (mIsWhite) {
				mWhiteArray.add(p);
			} else {
				mBlackArray.add(p);
			}
			Log.d("MainActivity", "�ػ�");
			// �ػ�
			invalidate();
			Log.d("MainActivity", "�ػ�ɹ�");
			mIsWhite = !mIsWhite;
		}

		return true;
	}

	private Point getValidPoint(int x, int y) {
		// TODO Auto-generated method stub
		return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));

	}
	
	// �߼��ж��Ƿ������Ϸ
	private void checkGameOver() {
		// TODO Auto-generated method stub
		boolean whiteWin = chechFiveInLine(mWhiteArray);
		boolean blackWin = chechFiveInLine(mBlackArray);
		if (whiteWin || blackWin) {
			mIsGameOver = true;
			mIsWhiteWinner = whiteWin;

			String text = mIsWhiteWinner ? "����ʤ��" : "����ʤ��";
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

	// �жϺ����Ƿ�ﵽĿ��
	private boolean checkHorizontal(int x, int y, List<Point> points) {
		int count = 1;
		// ��
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
			if (points.contains(new Point(x - i, y))) {
				count++;
			} else {
				break;
			}
		}
		if (count == MAX_COUNT_IN_LINE)
			return true;
		// ��
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

	// ��ֱ
	private boolean checkVertical(int x, int y, List<Point> points) {
		int count = 1;
		// ��
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
			if (points.contains(new Point(x, y + i))) {
				count++;
			} else {
				break;
			}
		}
		if (count == MAX_COUNT_IN_LINE)
			return true;
		// ��
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

	// ���ϵ�����
	private boolean checkRightDiagonal(int x, int y, List<Point> points) {
		int count = 1;
		// ����
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
			if (points.contains(new Point(x + i, y - i))) {
				count++;
			} else {
				break;
			}
		}
		if (count == MAX_COUNT_IN_LINE)
			return true;
		// ����
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

	// ���ϵ������ж�
	private boolean checkLeftDiagonal(int x, int y, List<Point> points) {
		int count = 1;
		// ����
		for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
			if (points.contains(new Point(x - i, y - i))) {
				count++;
			} else {
				break;
			}
		}
		if (count == MAX_COUNT_IN_LINE)
			return true;
		// ����
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
			// ����
			canvas.drawLine(startX, y, endX, y, mPaint);
			// ����
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
	 * View�Ĵ洢�ڻָ�,�����������xmlһ��ID
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
