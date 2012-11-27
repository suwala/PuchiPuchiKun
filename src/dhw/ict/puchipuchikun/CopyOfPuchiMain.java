package dhw.ict.puchipuchikun;

import java.util.ArrayList;
import java.util.List;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class CopyOfPuchiMain extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener{

	public Integer disW;
	public Integer disH;
	
	public ImageView iv;
	public Region region;
	public Integer crashIndex =-1;
	public List<PuchiDraw> puchiDraw = new ArrayList<PuchiDraw>();
	public MediaPlayer[] se = new MediaPlayer[6];
	public Integer counter;
	public Toast toast=null;
	private Boolean isGameMode = false;
	private Handler gameHandler = new Handler();
	
	private Runnable GameTimer = new Runnable() {
		
		@Override
		public void run() {
			// TODO 自動生成されたメソッド・スタブ
			gameHandler.postDelayed(GameTimer,500);
			
			drawPuchi();
		}
	};
	
	public void setCounte(){
		this.counter++;
		if(this.toast == null){
			this.toast = Toast.makeText(this,this.counter.toString()+"プチ" , Toast.LENGTH_SHORT);
		}else{//setTextを使えば次々と表示されるぞ
			this.toast.setText(this.counter.toString()+"プチ");
		}
		
		
		this.toast.setGravity(Gravity.RIGHT | Gravity.TOP,0,0);
		this.toast.show();
		
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO 自動生成されたメソッド・スタブ
		//this.rushMode = sharedPreferences.getBoolean("rush", false);
			
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = this.getSharedPreferences("PuchiPrefs", MODE_PRIVATE);
        //this.setContentView(new PuchiView(this));
        pref.registerOnSharedPreferenceChangeListener(this);
        
        this.readPref();
        
        for(int i=0;i<this.se.length;i++){
        	this.se[i] = MediaPlayer.create(this, getResources().getIdentifier("puchi"+String.valueOf(i+1), "raw", this.getPackageName()));
        }
        
        /*
        setContentView(R.layout.activity_puchi_main);
        
        //端末の画面サイズの取得
        Display disp = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        this.iv = (ImageView)findViewById(R.id.imageView1);
        
        this.disW = disp.getWidth();
        this.disH = disp.getHeight();
                
        this.readPref();
        this.isGameMode = this.checkGameMode();
        //this.makeRegions();        */
    }
    
    
    
    /*
     * Bitmap#createBitmapで作成したbitmapをImageViewにセットする場合
     * リサイズが掛かる？のでサイズにズレが生じる
     * このActivityが実際に描画されているサイズは480*724だったりするので
     * disp.getW&H()だと480*800ズレが生じるっぽい
     * 
     * Viewのサイズを取得するにはview#getWidth()で出来るが
     * onCreate()では画面が作られていないからか？0になるため使えない
     * onWindowFocusChangedだと画面が作られてから呼ばれるので取得できる
     * 
     * と思ったらImageViewにあるscaleType="matrix"
     * を設定するとリサイズされずに画面一杯に表示されるみたい
     * 他にもFIX_STARTやFIX_CENTERなどがある FIX_XYが無難？
     */
    @Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO 自動生成されたメソッド・スタブ
		super.onWindowFocusChanged(hasFocus);
		//this.disW = this.iv.getWidth();
		//this.disH = this.iv.getHeight();
		//this.iv.setImageBitmap(this.drawPuchi());
		
	}

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	
       
        Intent i = new Intent(CopyOfPuchiMain.this,Preferences.class);
        this.startActivity(i);
        
		return false;
    	
    }


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_puchi_main, menu);
        
        return true;
    }
	
    public Bitmap drawPuchi(){
    	
    	//画面と同サイズのbmを生成しcanvasにセット
    	Bitmap bm = Bitmap.createBitmap(this.disW,this.disH,Bitmap.Config.ARGB_8888);
    	Canvas canvas = new Canvas(bm);
    	//Drawable puchi;
    	//this.puchi.setBounds(this.region.getBounds());
    	//this.puchi.draw(canvas);
    	
    	int x = 0,y = 0,j = 0;
    	while(x < this.disW){

    		//描画領域を指定しDrawをcanvasに描画する
    		Rect rect = new Rect();
    		//rect.contains(0, 0, this.disW/4, this.disH/4);
    		rect.left = x;
    		rect.right = x+this.disW/4;
    		rect.top = y;
    		rect.bottom = y+this.disW/4;
    		//Region reg = new Region(x, y, x+this.disW/4, y+this.disH/5);//コンストラクタでも座標をセットできる

    		
    		if(this.crashIndex == -1)
    			this.puchiDraw.add(new PuchiDraw(rect));
    		Drawable puchi;
    		
            
    		
    		//押されたとき(true)　clashを描画する
    		if(this.puchiDraw.get(j).isPush()){
    			puchi = this.getResources().getDrawable(R.drawable.puchi3);
    		}else{
    			puchi = this.getResources().getDrawable(R.drawable.puchi2);
    		}
    		
    		
    		//描画範囲を指定してcanvasに描画　Rect=Boundsっぽい　Regionはプラスαの情報を持つ？　
    		puchi.setBounds(rect);
    		//this.puchi.setBounds(reg.getBounds());

    		puchi.draw(canvas);
    		
    		j++;
    		
    		y += rect.height();

    		if(y+rect.height() >= this.disH || y == this.disW/4 * 5 + this.disW/4/2){
    			y=0;
    			x+= rect.width();

    			if((this.disW/2 / x) % 2 == 0)
    				y+=this.disW/4/2;
    		}   		

    	}
    	//bmをImageViewにセット　ここで初めて画面に表示
    	//this.iv.setImageBitmap(bm);

    	Log.d("Touch",String.valueOf(this.puchiDraw.size()));
    	return bm;

    }

	@Override
	protected void onResume() {
		// TODO 自動生成されたメソッド・スタブ
		super.onResume();
		
		/*Bitmap bmp = this.drawPuchi();
        this.iv.setImageBitmap(bmp);
        this.iv.setOnTouchListener(this);
        
        if(this.isGameMode = this.checkGameMode()){
        	Log.d("PuchiMain","GameModeに移行します");
        }
        */
	}
	
	private Boolean checkGameMode(){
		
		SharedPreferences prefs = getSharedPreferences("PuchiPrefs", MODE_PRIVATE);
		return prefs.getBoolean(this.getResources().getString(R.string.key_game), false);
		
	}

	/*
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO 自動生成されたメソッド・スタブ
		
		Integer pointerIndex = event.getActionIndex();
		Float x = event.getX(pointerIndex);
		Float y = event.getY(pointerIndex);
		
		int i = 0;
		SharedPreferences pref = this.getSharedPreferences("PuchiPrefs", 0);
		
		
		for(PuchiDraw draw:this.puchiDraw){

			if(draw.getRect().contains(x.intValue(), y.intValue())){
				//Rect#contains()は指定した座標が含まれるかを判定する

				//押した時のみに反応
				if(event.getActionMasked() == MotionEvent.ACTION_DOWN || event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN){

					this.crashIndex=i;
					if(!this.puchiDraw.get(i).isPush()){
						this.pushPuchi(i);
						
					}
				}
				
				//ドラッグしたときの処理
				if(pref.getBoolean("rush", false)){
					if(event.getActionMasked() == MotionEvent.ACTION_MOVE){

						this.crashIndex=i;
						if(!this.puchiDraw.get(i).isPush()){
							this.pushPuchi(i);
						}
					}
				}
				
				
				
			}
			i++;
		}
		
		//離した時　何処で離しても再描画できるように分離
		if(event.getActionMasked() == MotionEvent.ACTION_UP){
			Boolean clear = true;
			for(PuchiDraw d:this.puchiDraw){
				if(!d.isPush()){
					clear = false;
					break;
				}
			}
			
			if(clear){
				for(int count =0;count<this.puchiDraw.size();count++){
					this.puchiDraw.get(count).setPush(false);
				}
				this.iv.setImageBitmap(this.drawPuchi());
				
			}
			
		}


		return true;
	}
	*/

	public void playSe(MediaPlayer mp){
		mp.seekTo(0);
		mp.start();
	}
	
	public void playSe(){
		this.playSe(this.se[(int) (Math.random()*5+0.5)]);
	}
	
	public void stopSe(MediaPlayer mp){
		mp.pause();
	}
	
	public void readPref(){
		SharedPreferences prefs = this.getSharedPreferences("puchi", MODE_PRIVATE);
		this.counter = prefs.getInt("counter", 0);
	}
	
	@Override
	protected void onStop() {
		// TODO 自動生成されたメソッド・スタブ
		super.onStop();
		
		this.writePref();
	}

	
	public void writePref(){
		SharedPreferences prefs = this.getSharedPreferences("puchi", MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("counter", this.counter);
		editor.commit();
	}
	
	public void pushPuchi(Integer i){
		this.crashIndex=i;
		if(!this.puchiDraw.get(i).isPush()){
			this.playSe(this.se[(int) (Math.random()*5+0.5)]);
			Log.d("aa",String.valueOf((int) (Math.random()*5+0.5)));
			this.puchiDraw.get(i).setPush(true);
			this.iv.setImageBitmap(this.drawPuchi());
			this.counter++;
			
			if(this.toast == null){
				this.toast = Toast.makeText(this,this.counter.toString()+"プチ" , Toast.LENGTH_SHORT);
			}else{//setTextを使えば次々と表示されるぞ
				this.toast.setText(this.counter.toString()+"プチ");
			}
			
			
			this.toast.setGravity(Gravity.RIGHT | Gravity.TOP,0,0);
			this.toast.show();
		}
	}
	
	public void timerTask(){
		
		
	}
	/*
	//描画するプチのサイズを決定する
	public void makeRegions(){
		Integer puthi_w,puthi_h;
		
		puthi_w = this.disW/4;
		puthi_h = this.disW/4;
		
		//puchiの形に合わせたPathオブジェクトの作成
		Path path = new Path();
		path.lineTo(0, 0);
		path.lineTo(0, puthi_h);
		path.lineTo(puthi_w, puthi_h);
		path.lineTo(puthi_w, 0);
		path.close();
		
		//PathオブジェクトからRegionオブジェクトを作成
		this.region = new Region();
		Region region2 = new Region(0,0,this.disW,this.disH);
		
		Path pathtmp = new Path();
		pathtmp.addPath(pathtmp, puthi_w,0);
		this.region.setPath(pathtmp, region2);
		
		
	}
    */
    
}
