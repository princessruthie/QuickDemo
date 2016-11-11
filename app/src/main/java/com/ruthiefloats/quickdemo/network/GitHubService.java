package com.ruthiefloats.quickdemo.network;

import com.ruthiefloats.quickdemo.models.Member;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * TODO: add a class header comment.
 */

public interface GitHubService {

    @GET("orgs/{org}/members")
    Call<List<Member>> listMemberObjects(@Path("org") String organization);
}
