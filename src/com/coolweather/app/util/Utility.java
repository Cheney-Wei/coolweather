package com.coolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;
 
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
 

public class Utility {
	public synchronized static boolean handleProvincesResponse (CoolWeatherDB cooldb,String response){
		if( !TextUtils.isEmpty(response)){
			String [] allprovinces = response.split(",");
			if(allprovinces !=null && allprovinces.length > 0){
				for (String p : allprovinces){
					String [] array = p.split("\\|");
					Province province = new Province();
					province.setprovinceCode(array[0]);
					province.setprovinceName(array[1]);
					cooldb.saveProvince(province);
				
				}
				return true;
			}
		}
		return false;
	}
	public static boolean handleCitiesResponse(CoolWeatherDB cooldb,String response,int provinceId){
		if(!TextUtils.isEmpty(response)){
			String [] allcities = response.split(",");
			if(allcities != null && allcities.length > 0){
				for (String C: allcities){
					String [] array = C.split("\\|");
					City city = new City ();
					city.setCityCode(array[0]);
					city.setCityName(array [1]);
					city.setProvinceId(provinceId);
					cooldb.saveCity(city);
				}
				return true ;
			}
		}
		return false;
	}
	
	public static boolean handleCountiesResponse(CoolWeatherDB cooldb,String response,int cityId){
		if(!TextUtils.isEmpty(response)){
			String [] allcounties = response.split(",");
			if(allcounties != null && allcounties.length > 0){
				for (String C: allcounties){
					String [] array = C.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array [1]);
					county.setCityId(cityId);
					cooldb.saveCounty(county);
				}
				return true ;
			}
		}
		return false;
	}
	
	
	public static void handWeatherResponse(Context context,String response){
		try {
			JSONObject jsonobject = new JSONObject(response);
			JSONObject weatherInfo = jsonobject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void saveWeatherInfo(Context context,String cityName,
			String weatherCode,String temp1,String temp2,String weatherDesp,String publishTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyƒÍM‘¬d»’",Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
		}
	
}
