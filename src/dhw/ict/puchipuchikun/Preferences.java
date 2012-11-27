package dhw.ict.puchipuchikun;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;

public class Preferences extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.prefs_screen);
		getPreferenceManager().setSharedPreferencesName("PuchiPrefs");
		//xml.Preferencesの取得
		addPreferencesFromResource(R.xml.preference);
		
		
		final CheckBoxPreference rushPref = (CheckBoxPreference)this.findPreference("rush");
		final CheckBoxPreference gamePref = (CheckBoxPreference)this.findPreference("gameMode");
		rushPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO 自動生成されたメソッド・スタブ
				
				// Preference がチェックされているか  
				if(rushPref.isChecked()){
					gamePref.setChecked(false);
				}
				return false;
			}
		});
		
		gamePref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				// TODO 自動生成されたメソッド・スタブ
				
				// Preference がチェックされているか  
				if(gamePref.isChecked()){
					rushPref.setChecked(false);
				}
				return false;
			}
		});
		
	}
	
}
