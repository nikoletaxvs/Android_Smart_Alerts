package com.example.smart_alert;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.sql.PreparedStatement;

public class DbHelper {
    SQLiteDatabase DB;
    public DbHelper(SQLiteDatabase DB){
        this.DB =DB;
    }

    public void insertToLocalDB(String emailTXT,String subTXT) {
        if(emailUnique(emailTXT)) {
            DB.execSQL("Insert or ignore into Userdetails (email,subscribed) Values(?,?)",
                    new String[]{emailTXT, subTXT});
        }

    }
    public void updateLocalDB(String emailTXT,Boolean sub,Context context) {
        if(emailExists(emailTXT)){
            String subTXT = Boolean.toString(sub);

            DB.execSQL("UPDATE Userdetails SET subscribed= ? Where email=?",new String[]{subTXT,emailTXT});
            Toast.makeText(context,"Data was updated",Toast.LENGTH_LONG).show();}
        else{
            Toast.makeText(context,"This title doesn't exist",Toast.LENGTH_LONG).show();
        }

    }
    public String getSubscriptionStatus(String emailTXT,Context context) {
        String sub = "unknown";
        Cursor cursor = DB.rawQuery("Select * from Userdetails where email= ?", new String[]{emailTXT});
        try {
            if (cursor.getCount() == 0) {
                //showMessage("Title does not exist", "",context);
            } else {
                StringBuilder builder = new StringBuilder();

                while (cursor.moveToNext()) {
                    builder.append("email:").append(cursor.getString(0)).append("\n")
                            .append("sub ").append(cursor.getString(1));
                            sub= cursor.getString(1);
                }

              //  showMessage("Result", builder.toString(),context);

            }
        }catch (Exception e) {
            Toast.makeText(context,"Error",Toast.LENGTH_LONG).show();
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return sub;
    }
    private void showMessage(String s1, String s2,Context context) {
        new AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle(s1)
                .setMessage(s2)
                .show();
    }
    private boolean emailExists(String email){
        Cursor cursor = DB.rawQuery("Select email from Userdetails WHERE email = ?", new String[] {email});
        return cursor.getCount()>0;
    }
    private boolean emailUnique(String email) {
        Cursor cursor = DB.rawQuery("Select email from Userdetails WHERE email = ?", new String[] {email});
        return cursor.getCount()==0 && email.length()>0 ;
    }
}
