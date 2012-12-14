package dhw.ict.puchipuchikun;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Region;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

public abstract class PuchiView extends View implements OnSharedPreferenceChangeListener{

	private Integer dispWidth,dispHeight;
	protected Context context;
	private Bitmap puchi,crush;
	protected List<PuchiDraw> puchiDraw = new ArrayList<PuchiDraw>();
	private List<Region>regionList = new ArrayList<Region>();
	private Boolean rushMode;
	
	public PuchiView(Context context) {
		super(context);
		// TODO 自動生成されたコンストラクター・スタブ
		this.context = context;
		
		Display disp = ((WindowManager)this.context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		
		this.dispWidth = disp.getWidth();
		this.dispHeight= disp.getHeight();
		
		int w = this.dispWidth/4 ,h = this.dispWidth/4;
		
		this.puchi = BitmapFactory.decodeResource(getResources(), R.drawable.newpuchi1);
		this.crush = BitmapFactory.decodeResource(getResources(), R.drawable.newpuchi2);
		
		this.puchi = Bitmap.createScaledBitmap(this.puchi, w, h, false);
		this.crush = Bitmap.createScaledBitmap(this.crush, w, h, false);
		
		//座標の計算　Regionの作成
		for(int i=0;i<this.dispWidth/w;i++){
			for(int j=0;j<this.dispHeight/h;j++){
				if(i%2==1){
					this.puchiDraw.add(new PuchiDraw(new Rect(i*w, j*h+h/2, i*w+w, j*h+h+h/2)));
					if((j*h+h/2)+h*3>=this.dispHeight)
						break;
				
				}else
					this.puchiDraw.add(new PuchiDraw(new Rect(i*w, j*h, i*w+w, j*h+h)));
							
			}
			this.regionList.add(new Region(i*w, 0, i*w+w, h));			
		}
		
		
		SharedPreferences pref = this.context.getSharedPreferences("PuchiPrefs", context.MODE_PRIVATE);
		pref.registerOnSharedPreferenceChangeListener(this);
		
		//ここと
		this.rushMode = pref.getBoolean("rush", false);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO 自動生成されたメソッド・スタブ
		super.onDraw(canvas);
		canvas.drawColor(Color.rgb(221, 183, 134));//ddb786
		
		Bitmap draw;
		for(PuchiDraw d:this.puchiDraw){
			
			if(d.isPush()){
				draw = this.crush;
			}else{
				draw = this.puchi;
			}			
			canvas.drawBitmap(draw, d.getRect().left,d.getRect().top, null);
		}		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO 自動生成されたメソッド・スタブ
		
		Integer pointerIndex = event.getActionIndex();
		Float x = event.getX(pointerIndex);
		Float y = event.getY(pointerIndex);
		int i=0;
		
		for(PuchiDraw draw:this.puchiDraw){
			if(draw.getRect().contains(x.intValue(), y.intValue())){
				//Rect#contains()は指定した座標が含まれるかを判定する
				//押した時のみに反応
				if(!draw.isPush()){
					if(event.getActionMasked() == MotionEvent.ACTION_DOWN || event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN){

						((PuchiMainActivity)context).playSe();
						((PuchiMainActivity)context).setCounte();
						this.puchiDraw.get(i).setPush(true);
						this.invalidate();

					}
					//ドラッグ時　NormalModeで書くべきか
					if(event.getActionMasked() == MotionEvent.ACTION_MOVE)
						if(this.rushMode){
							((PuchiMainActivity)context).setCounte();
							((PuchiMainActivity)context).playSe();
							this.puchiDraw.get(i).setPush(true);
							this.invalidate();
						}
				}				
			}
			i++;
		}
		
		if(event.getActionMasked() == MotionEvent.ACTION_UP){
			if(this.allPush())
				this.resetDraw();

		}
		return true;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO 自動生成されたメソッド・スタブ
		this.rushMode = sharedPreferences.getBoolean("rush", false);
		Log.d("draw","PrefChange!");
	}
	
	private boolean allPush(){
		for(int j=0;j<this.puchiDraw.size();j++){
			
			if(!this.puchiDraw.get(j).isPush())
				return false;
		}
		return true;
	}
	
	private void resetDraw(){
		for(int j=0;j<this.puchiDraw.size();j++){
			this.puchiDraw.get(j).setPush(false);
			this.invalidate();
		}
	}	
}
