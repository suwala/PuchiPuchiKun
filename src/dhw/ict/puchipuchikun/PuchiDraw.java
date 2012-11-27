package dhw.ict.puchipuchikun;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class PuchiDraw{

	private Rect rect;
	private Boolean push;
	private int coolTime;

	public PuchiDraw(Rect r){
		this.rect = r;
		this.push = false;
	}
	
	public Rect getRect(){
		return this.rect;
	}
	
	public void setPush(Boolean p){
		this.push = p;
	}
	
	public Boolean isPush(){
		return this.push;
	}
	
	public void setCoolTime(){
		this.coolTime = (int)(Math.random()*10);
	}
	
	public int getCoolTime(){
		return this.coolTime;
	}
	
	public void decrementCoolTime(){
		if(this.coolTime > 0)
			this.coolTime--;
	}
	
}
