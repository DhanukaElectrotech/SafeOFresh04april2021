package com.dhanuka.morningparcel.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dhanuka.morningparcel.R;
import com.example.librarymain.DhanukaMain;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dhanuka.morningparcel.Helper.CommonHelper;
import com.dhanuka.morningparcel.Helper.NetworkUtil;
import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.activity.AddLatLong;
import com.dhanuka.morningparcel.activity.ForgotPasswordActivity;
import com.dhanuka.morningparcel.activity.HomeActivity;
import com.dhanuka.morningparcel.activity.HomeStoreActivity;
import com.dhanuka.morningparcel.beans.LoginBean;
import com.dhanuka.morningparcel.beans.LoginBeanMain;
import com.dhanuka.morningparcel.fcm.Config;
import com.dhanuka.morningparcel.utils.JKHelper;


public class LoginDialog extends Dialog implements
		View.OnClickListener {
	Context ctx;
 	ImageView backbtnicon;
 	Resources res;
	LoginBeanMain mloginBean;
	ArrayList<LoginBean> mLoginList;
	String Email, Pass;
	EditText Username;
	EditText Password;
	Button Logme;

	TextView errortext;
	Button buttonerror;
	TextView textView, forgot, VersionCodee;
	Button btn_login;
	EditText et_user, et_pass;
	ImageView pass_show, pass_hide;
	Dialog ErrorDailog;
	ProgressDialog dialog;
	AlertDialog.Builder builder;
	SharedPreferences sharedPreferences;
	public static Preferencehelper prefs;
	CommonHelper commonHelper;
	// Gunjan 22/08
	String vercode = "";
	Typeface face;
	SharedPreferences prefsL;
	SharedPreferences.Editor mEditorL;
	TextView btn_register;

	public LoginDialog(Context context) {
		// TODO Auto-generated constructor stub
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		ctx = context;
		res = ctx.getResources();
	 	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		WindowManager.LayoutParams wlmp = getWindow().getAttributes();
		wlmp.gravity = Gravity.CENTER_HORIZONTAL;
		getWindow().setAttributes(wlmp);
		setTitle(null);
		setCancelable(true);
		setCanceledOnTouchOutside(true);
		setOnCancelListener(null);
		LayoutInflater inflate = (LayoutInflater) ctx
				.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
		View layout = inflate.inflate(R.layout.dialog_login, null);
		setContentView(layout);
		prefs = new Preferencehelper(ctx);

		ErrorDailog = new Dialog(ctx);
		ErrorDailog.setContentView(R.layout.custom_dialogbox);
		ErrorDailog.setCancelable(false);
		buttonerror = ErrorDailog.findViewById(R.id.clickok);
		errortext = ErrorDailog.findViewById(R.id.custom_dialogtext);

		
		// /////////set font///////////////


		backbtnicon = (ImageView) layout.findViewById(R.id.imgBackBtn);
		Username =layout. findViewById(R.id.usernameide);
		Password = layout.findViewById(R.id.passwordide);
		Logme = layout.findViewById(R.id.logmein);
		forgot = (TextView) layout.findViewById(R.id.txt_forgot);
		btn_login = (Button) layout.findViewById(R.id.logmein);
		et_user = (EditText) layout.findViewById(R.id.usernameide);
		et_pass = (EditText) layout.findViewById(R.id.passwordide);
		pass_show = (ImageView) layout.findViewById(R.id.img_show);
		pass_hide = (ImageView) layout.findViewById(R.id.img_hide);
		btn_register = (TextView) layout.findViewById(R.id.btn_register);
		VersionCodee = (TextView) layout.findViewById(R.id.Versioncodde);

		backbtnicon.setOnClickListener(this);

		face = Typeface.createFromAsset(ctx.getAssets(),
				"fonts/sansation-bold.ttf");

		forgot.setTypeface(face);
		Logme.setTypeface(face);
		btn_register.setTypeface(face);
		et_user.setTypeface(Typeface.createFromAsset(ctx.getAssets(),
				"fonts/sansation.ttf"));
		et_pass.setTypeface(Typeface.createFromAsset(ctx.getAssets(),
				"fonts/sansation.ttf"));
		btn_register.setOnClickListener(this);
		try {
			PackageInfo pInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
			String version = pInfo.versionName;
			VersionCodee.setTypeface(face);
			VersionCodee.setText("Version Code - " + version);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}




		forgot.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ctx.startActivity(new Intent(ctx, ForgotPasswordActivity.class));
			}
		});
		pass_hide.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int inputTypeValue = et_pass.getInputType();
				pass_show.setVisibility(View.VISIBLE);
				pass_hide.setVisibility(View.GONE);
				et_pass.setTransformationMethod(new PasswordTransformationMethod());
			}
		});
		pass_show.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int inputTypeValue = et_pass.getInputType();
				et_pass.setTransformationMethod(null);
				pass_show.setVisibility(View.GONE);
				pass_hide.setVisibility(View.VISIBLE);

			}
		});
		Logme.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Username.getText().toString().equalsIgnoreCase("")) {

					Username.setError("Please enter username");


				} else if (Password.getText().toString().equalsIgnoreCase("")) {
					Password.setError("Please enter password");
				} else {
					if (NetworkUtil.isConnectedToNetwork(ctx)) {

						LoginApi();
					} else {

						errortext.setText("No Internet connection found ");
						buttonerror.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								ErrorDailog.dismiss();
							}
						});
						ErrorDailog.show();
					}

				}
			}
		});
		
		
	}
	public void LoginApi() {
//		int vrCode = BuildConfig.VERSION_CODE;
//		vercode = BuildConfig.VERSION_NAME + "/" + String.valueOf(vrCode);
//		Log.d("uid", Username.getText().toString());
		Log.d("pwd", Password.getText().toString());
		mloginBean = new LoginBeanMain();

		final ProgressDialog mProgressBar = new ProgressDialog(ctx);
		mProgressBar.setTitle("Safe'O'Fresh");
		mProgressBar.setMessage("Loading...");
		mProgressBar.show();
		mProgressBar.setCancelable(false);

		StringRequest postRequest = new StringRequest(Request.Method.POST,  ctx.getString(R.string.URL_BASE_URL),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {


						try {

							prefs.setBannerPrefs("0");
							prefs.setPrefsPassword(Password.getText().toString());
							prefs.setPrefsEmail(Username.getText().toString());
							String responses= DhanukaMain.SafeOBuddyDecryptUtils(response);
							Log.e("Response1", responses);
							// startService(new Intent(ctx, UploadFCMActivity.class));

							LoginBeanMain mLoginbeanmain = new Gson().fromJson(responses
									, LoginBeanMain.class);
							mloginBean = mLoginbeanmain;

							if (mLoginbeanmain.getStrSuccess() == 1) {
								mLoginList = new ArrayList<>();
								Log.e("HJSGJ",mloginBean.getmListLogin().size()+"");
								prefs.setPrefsRunonce("0");
								prefs.setTokenValue(mloginBean.getmListLogin().get(0).getStrtoken());
								if (mLoginbeanmain.getmListLogin().get(0).getStruid().equalsIgnoreCase("0")) {
									mProgressBar.dismiss();

									errortext.setText("No Role is defined to this user or no such user exist");
									buttonerror.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											ErrorDailog.dismiss();
										}
									});
									ErrorDailog.show();
								} else if (mLoginbeanmain.getmListLogin().get(0).getStrusercategory().equalsIgnoreCase("1057") || mLoginbeanmain.getmListLogin().get(0).getStrusercategory().equalsIgnoreCase("1057")) {
									prefs.setPrefsLoginValue("1");
									prefs.setCID(mLoginbeanmain.getmListLogin().get(0).getStrval1());
									prefs.setPREFS_currentbal(mLoginbeanmain.getmListLogin().get(0).getStrcurrentbalance());
									prefs.setPrefsContactId(mLoginbeanmain.getmListLogin().get(0).getStruid());
									prefs.setPrefsUsercategory(mLoginbeanmain.getmListLogin().get(0).getStrusercategory());
									if (!mLoginbeanmain.getmListLogin().get(0).getStrblatlong().equalsIgnoreCase("0"))
									{
										ctx.startActivity(new Intent(ctx, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
										dismiss();

									}
									else
									{
										ctx.startActivity(new Intent(ctx, AddLatLong.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

									}


 								} else if (mLoginbeanmain.getmListLogin().get(0).getStrusercategory().equalsIgnoreCase("1058") || mLoginbeanmain.getmListLogin().get(0).getStrusercategory().equalsIgnoreCase("1058")) {
									prefs.setPrefsLoginValue("1");
									prefs.setCID(mLoginbeanmain.getmListLogin().get(0).getStrval1());
									prefs.setPrefsContactId(mLoginbeanmain.getmListLogin().get(0).getStruid());
									prefs.setPREFS_currentbal(mLoginbeanmain.getmListLogin().get(0).getStrcurrentbalance());
									prefs.setPrefsUsercategory(mLoginbeanmain.getmListLogin().get(0).getStrusercategory());
									//  startActivity(new Intent(ctx, HomeStoreActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

									if (!mLoginbeanmain.getmListLogin().get(0).getStrblatlong().equalsIgnoreCase("0"))
									{
										ctx.startActivity(new Intent(ctx, HomeStoreActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

									}
									else
									{
										ctx.startActivity(new Intent(ctx, AddLatLong.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

									}
								}


								else {
									mProgressBar.dismiss();

									errortext.setText("No Role is defined to this user or no such user exist");
									buttonerror.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											ErrorDailog.dismiss();
										}
									});
									ErrorDailog.show();

								}
								mProgressBar.dismiss();
							} else {
								mProgressBar.dismiss();


									errortext.setText("No Role is defined to this user or no such user exist");
									buttonerror.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											ErrorDailog.dismiss();
										}
									});
									ErrorDailog.show();

							}
						} catch (Exception e) {
							e.printStackTrace();

							mProgressBar.dismiss();

							errortext.setText("Login Error !!");
							buttonerror.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {

									ErrorDailog.dismiss();
								}
							});
							ErrorDailog.show();

						}


					}

				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

						errortext.setText("Something went wrong ");
						buttonerror.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								ErrorDailog.dismiss();
							}
						});
						ErrorDailog.show();
						try {

							mProgressBar.dismiss();
						} catch (Exception e) {
							e.printStackTrace();
						}


					}
				}
		) {
			@Override
			protected Map<String, String> getParams() {
				SharedPreferences prefsToken = ctx.getSharedPreferences("MORNING_PARCEL_TOKEN",
						ctx.MODE_PRIVATE);
				SharedPreferences.Editor mEditorprefsToken = prefsToken.edit();

				SharedPreferences prefF = ctx.getSharedPreferences(Config.SHARED_PREF, 0);
				String strtoken=  prefF.getString("regId", "NO GCM");

				Map<String, String> params = new HashMap<String, String>();
				try {
					// String param= Encrypt("method=LoginValidation1&uid=" + emailId + "&pwd=" + pasword);
					SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(ctx);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.remove("tokenval");
					editor.commit();
					String param=ctx.getString(R.string.URL_LOGIN_ENC)+"&uid=" + Username.getText().toString() + "&pwd=" + Password.getText().toString()+"&GCMID="+ strtoken+"&version="+vercode;
					Log.d("Beforeencrption",param);
					JKHelper jkHelper= new JKHelper();
					String finalparam= jkHelper.Encryptapi(param, ctx);
					params.put("val", finalparam);
					Log.d("afterencrption",finalparam);
					return params;

				}
				catch (Exception e) {
					e.printStackTrace();
					return params;
				}

			}
		};
		postRequest.setRetryPolicy(new DefaultRetryPolicy(
				60000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		Volley.newRequestQueue(ctx).add(postRequest);


	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.imgBackBtn) {
			this.dismiss();
		}
	}

}
