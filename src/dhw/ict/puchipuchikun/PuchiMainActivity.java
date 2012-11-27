package dhw.ict.puchipuchikun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.inputmethodservice.Keyboard.Key;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class PuchiMainActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener{

	public Integer disW;
	public Integer disH;
	
	public ImageView iv;
	public Region region;
	public Integer crashIndex =-1;
	public List<PuchiDraw> puchiDraw = new ArrayList<PuchiDraw>();
	public MediaPlayer[] se = new MediaPlayer[6];
	public Integer counter;
	public Toast toast=null;
	
	private Map<Boolean, PuchiView> mode = new HashMap<Boolean, PuchiView>();
	
	
	//音・Toast・カウント・Viewのセットをやってます
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
	protected void onResume() {
		// TODO 自動生成されたメソッド・スタブ
		super.onResume();
        this.setContentView(this.mode.get(this.isGameMode()));		
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
        
        this.mode.put(true, new GameMode(this));
        this.mode.put(false, new NormalMode(this));

        pref.registerOnSharedPreferenceChangeListener(this);
        
        this.readPref();
        
        for(int i=0;i<this.se.length;i++){
        	this.se[i] = MediaPlayer.create(this, getResources().getIdentifier("puchi"+String.valueOf(i+1), "raw", this.getPackageName()));
        }        
        
        
        
    }
    
    private boolean isGameMode(){
    	
    	SharedPreferences pref = this.getSharedPreferences("PuchiPrefs", MODE_PRIVATE);
    	return pref.getBoolean("gameMode", false);
    }
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 自動生成されたメソッド・スタブ
		if(keyCode == KeyEvent.KEYCODE_MENU){
			Intent i = new Intent(PuchiMainActivity.this,Preferences.class);
			this.startActivity(i);
		}
		return super.onKeyDown(keyCode, event);
	}	 

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
}
