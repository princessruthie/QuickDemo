package com.ruthiefloats.quickdemo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ruthiefloats.quickdemo.adapters.MemberAdapter;
import com.ruthiefloats.quickdemo.models.Member;
import com.ruthiefloats.quickdemo.network.GitHubService;
import com.ruthiefloats.quickdemo.network.GitHubServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "oh yeah logtag";
    // for basic authentication, put your credentials in gradle.properties
    String username = BuildConfig.GIT_HUB_USER_NAME;
    String password = BuildConfig.GIT_HUB_PASSWORD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /*use user input to build a Call and use Response to
    populate RecyclerView
     */
    public void onButtonClick(View view) {
        if (isNetworkAvailable()) {

            EditText editText = (EditText) findViewById(R.id.editText);
            final String searchTerm = String.valueOf(editText.getText());
            GitHubService service = GitHubServiceGenerator.generateService(GitHubService.class, username, password);
            Call<List<Member>> call = service.listMemberObjects(searchTerm);
            call.enqueue(new Callback<List<Member>>() {
                @Override
                public void onResponse(Call<List<Member>> call, retrofit2.Response<List<Member>> response) {
                    if (response.isSuccessful()) {
                        List<Member> memberList = response.body();
                        if (memberList != null) {
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewMembers);
                            MemberAdapter adapter = new MemberAdapter(getApplicationContext(), memberList);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                getResources().getString(R.string.bad_news) + " \"" + searchTerm + "\". ",
                                Toast.LENGTH_LONG).show();
                        Log.i(LOG_TAG, "list null");
                    }
                }

                @Override
                public void onFailure(Call<List<Member>> call, Throwable t) {
                    Log.i(LOG_TAG, "error " + t);
                }
            });
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_network), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
