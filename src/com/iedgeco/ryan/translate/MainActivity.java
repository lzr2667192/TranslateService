package com.iedgeco.ryan.translate;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.iedgeco.ryan.translate.config.StaticDef;
import com.iedgeco.ryan.translate.service.ITranslate;

public class MainActivity extends Activity {

	private static final String TAG = "Translator";
	
	private EditText etInput, etTranslation;
	private Spinner spFrom, spTo;
	private Button btTranslate;
	
	private static final int TRANSLATE_COMPLETED = 0;
	private static final int TRANSLATE_ERROR = 1;
	
	
	//data
	private String[] langShortNames;
	
	//controller
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TRANSLATE_COMPLETED:
				String result = (String) msg.obj;
				etTranslation.setText(result);
				etInput.selectAll();
				break;
			case TRANSLATE_ERROR:
				Log.e(TAG, "translation error");
				break;
			default:
				break;
			}
		}
		
	};
	
	//service
	private ITranslate mTranslateService;
	private ServiceConnection serviceConn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			//if service disconnected
			btTranslate.setEnabled(false);
			mTranslateService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			//if service connected
			mTranslateService = ITranslate.Stub.asInterface(service);
			if(mTranslateService != null)
				btTranslate.setEnabled(true);
			else{
				Log.e(TAG, "Unable to acquire TranslateService!");
				btTranslate.setEnabled(false);
			}
		}
	};
	
	//launch a thread for translation
	private void doTranslate(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = null;
				try {
					String input = etInput.getText().toString();
					//use the translate service
					result = mTranslateService.traslate(input);
					//send message callback
					Message msg = new Message();
					msg.obj = result;
					if(result == null){
						msg.what = TRANSLATE_ERROR;
						throw new Exception("Failed to get a translation...");
					}
					msg.what = TRANSLATE_COMPLETED;
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					Log.e(TAG, "Error happened while translating...", e);
				}
			}
		}).start();
	}
	
	private void doBindService(){
		Intent service = new Intent(StaticDef.ACTION_TRANALATE);
		bindService(service, serviceConn, Context.BIND_AUTO_CREATE);
	}
	
	private void doUnBindService(){
		unbindService(serviceConn);
	}
	
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.bt_translate:
			doTranslate();
			break;
		default:
			break;
		}
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //find views
        etInput = (EditText) findViewById(R.id.et_input);
        etTranslation = (EditText) findViewById(R.id.et_translation);
        spFrom = (Spinner) findViewById(R.id.sp_from);
        spTo = (Spinner) findViewById(R.id.sp_to);
        btTranslate = (Button) findViewById(R.id.bt_translate);
        
        //get language values from resource
        langShortNames = getResources().getStringArray(R.array.language_values);
        
        //initialize Spinner items
        ArrayAdapter<?> fromAdapter = ArrayAdapter.createFromResource(
        		this, 
        		R.array.languages, 
        		android.R.layout.simple_spinner_item);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spFrom.setAdapter(fromAdapter);
        spFrom.setSelection(0);			//input language is Chinese
        
        ArrayAdapter<?> toAdapter = ArrayAdapter.createFromResource(
        		this, 
        		R.array.languages, 
        		android.R.layout.simple_spinner_item);
        toAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spTo.setAdapter(toAdapter);
        spTo.setSelection(1);			//to be translated into English
        
        //etInput.selectAll();
        
        //bind translate service
        doBindService();
    }
    
    @Override
    protected void onDestroy() {
    	doUnBindService();
    	super.onDestroy();
    }
}
