package dhw.ict.puchipuchikun;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.view.MotionEvent;

public class GameMode extends PuchiView{

	private Timer timer ;
	
	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		// TODO 自動生成されたメソッド・スタブ
		super.onWindowVisibilityChanged(visibility);
		
		if(visibility == VISIBLE)
			this.startTimer();
		else
			this.cancelTimer();
	}
	

	private void cancelTimer(){
		this.timer.cancel();
	}

	private void startTimer(){
		
		if(timer!=null){
			timer.cancel();
		}
		
		final android.os.Handler handler = new android.os.Handler();
		timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO 自動生成されたメソッド・スタブ
				for(int i=0;i<puchiDraw.size();i++){
					
					if(puchiDraw.get(i).getCoolTime() > 0 )
						puchiDraw.get(i).decrementCoolTime();
					else
						puchiDraw.get(i).setPush(false);
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO 自動生成されたメソッド・スタブ
							invalidate();
						}
					});
						
				}
			}
		},0,500);
	}
	
	public GameMode(Context context) {
		super(context);
		// TODO 自動生成されたコンストラクター・スタブ
		
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		
		for(int i=0;i<puchiDraw.size();i++){
			
			if(puchiDraw.get(i).isPush() )
				puchiDraw.get(i).setCoolTime();			
		}	
		
		return true;
	}
	
	
}
