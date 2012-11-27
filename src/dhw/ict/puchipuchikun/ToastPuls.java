package dhw.ict.puchipuchikun;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class ToastPuls extends Toast{

	private static Toast sToast = null;
	
	public ToastPuls(Context context) {
		super(context);
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	@Override
	public void show(){
		ToastPuls.setToast(this);
		Log.d("aa","aaa");
		super.setGravity(Gravity.RIGHT | Gravity.TOP,0,0);
		super.show();
	}
	
	public static void setToast(Toast toast){
		if(sToast != null){
			sToast.cancel();
		}
		sToast = toast;
	}
	
	public static void cancelToast(){
		if(sToast != null){
			sToast.cancel();
		}
		sToast = null;
	}

}
