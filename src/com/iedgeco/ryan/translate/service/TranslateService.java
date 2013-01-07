package com.iedgeco.ryan.translate.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.iedgeco.ryan.translate.utils.Translator;

public class TranslateService extends Service {

	public static final String TAG = "TranslateService";
	
	//binder
	private final ITranslate.Stub mBinder = new ITranslate.Stub() {
		@Override
		public String traslate(String text) throws RemoteException {
			
			try {
				return Translator.translate(text);
			} catch (Exception e) {
				Log.e(TAG, "Failed to translate", e);
				return null;
			}
		}
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

}
