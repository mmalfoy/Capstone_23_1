package com.example.a23__project_1.fragmentFirst;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageButton;

import com.example.a23__project_1.data.DataMoreInfo;
import com.example.a23__project_1.R;
import com.example.a23__project_1.leftMenuBar.SettingActivity;
import com.example.a23__project_1.response.PositionResponse;
import com.example.a23__project_1.retrofit.RetrofitAPI;
import com.example.a23__project_1.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragFirstInfoActivity extends AppCompatActivity {
    private Intent intent;
    private String messege;
    private RecyclerView recyclerView;
//    private final Context mainContext;
    private ImageButton backbtn;
    private Adapter adapter;
    private ArrayList<DataMoreInfo> list_place;
    private RetrofitAPI apiService;
    private Call<PositionResponse> allPlaceCall;
    private List<Long> positionIdList;
    private com.example.a23__project_1.fragmentSecond.categoryAdapter categoryAdapter;
    private com.example.a23__project_1.fragmentSecond.PlaceListAdapter placeListAdapter;
    private List<PositionResponse.Result> resultList = new ArrayList<>();
    private static final String TAG = "FragmentFirstInfo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_frag_info);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_activity_first_fragment_menu);
        positionIdList = new ArrayList<>();
        
        intent = getIntent();
        if(intent.hasExtra("elements")){
            messege = intent.getStringExtra("elements");
        }

        //리사이클러뷰
        list_place = new ArrayList<>();

        switch (messege){
            case "more_category":
                getPositionList("카테고리더보기",list_place);
                break;
            case "more_place":
                getPositionList("카테고리더보기",list_place);
                break;
            case "cardview1":
                getPositionList("쇼핑몰",list_place);
                break;
            case "cardview2":
                getPositionList("지하철·기차역",list_place);
                break;
            case "cardview3":
                getPositionList("음식점",list_place);
                break;
            case "cardview4":
                getPositionList("공원",list_place);
                break;
            case "cardview5":
                getPositionList("공항",list_place);
                break;
            case "cardview6":
                getPositionList("카페",list_place);
                break;
            case "cardview7":
                getPositionList("관광",list_place);
                break;
            case "cardview8":
                getPositionList("거리",list_place);
                break;
            case "cardview9":
                getPositionList("상업지구",list_place);
                break;
            case "cardview10":
                getPositionList("스파",list_place);
                break;
            case "cardview11":
                getPositionList("놀이공원",list_place);
                break;
            case "cardview12":
                getPositionList("마트",list_place);
                break;
            case "0":
                getRegionList(list_place,0);
                break;
            case "1":
                getRegionList(list_place,1);
                break;
            case "2":
                getRegionList(list_place,2);
                break;
            case "3":
                getRegionList(list_place,3);
                break;
            case "4":
                getRegionList(list_place,4);
                break;
            case "5":
                getRegionList(list_place,5);
                break;
            default:
                try {

                    Integer.parseInt(messege);
                    Log.d("FragFirstInfo",messege);

                }catch(NumberFormatException e){
                    Log.d("FragFirstInfo","error");
                }

        }

        //횡 방향 스크롤시 아이템별 정지 위함
        SnapHelper snapHelper = new PagerSnapHelper();
        if (recyclerView.getOnFlingListener() == null)
            snapHelper.attachToRecyclerView(recyclerView);

        //툴바 옆, setting 관련 코드
        ImageButton btn_setting = (ImageButton) findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FragFirstInfoActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein_right, R.anim.stay);

            }
        });

        //menu 관련 코드
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_first_info_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<DataMoreInfo> getPositionList(String theme_id, ArrayList<DataMoreInfo> list_place) {

        apiService = RetrofitClient.getApiService();
        allPlaceCall = apiService.getAllPosition();
        allPlaceCall.enqueue(new Callback<PositionResponse>() {


            @Override
            public void onResponse(Call<PositionResponse> call, Response<PositionResponse> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, "성공");
                    List<PositionResponse.Result> positionList = response.body().getResult();


                    for (PositionResponse.Result result : positionList) {
                        positionIdList.add(result.getPlaceId());
                    }
                    settingPage(positionList, theme_id);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(new RecyclerFragFirstThemeAdapter(FragFirstInfoActivity.this, list_place));
                            recyclerView.setLayoutManager(new LinearLayoutManager(FragFirstInfoActivity.this,RecyclerView.VERTICAL,false));
                        }
                    });
                }
                else {
                    Log.d(TAG, "에러발생 .." + response.message());
                }
            }

            @Override
            public void onFailure(Call<PositionResponse> call, Throwable t) {
                Log.d(TAG, "onFalilure .. 카테고리 불러오기 연동 실패 ..., 메세지 : " + t.getMessage());
            }

        });
        return list_place;
    }
    public void getRegionList(ArrayList<DataMoreInfo> list_place,int position){
        apiService = RetrofitClient.getApiService();
        allPlaceCall = apiService.getAllPosition();
        allPlaceCall.enqueue(new Callback<PositionResponse>() {


            @Override
            public void onResponse(Call<PositionResponse> call, Response<PositionResponse> response) {
                if(response.isSuccessful()) {
                    Log.d(TAG, "성공");
                    List<PositionResponse.Result> positionList = response.body().getResult();


                    for (PositionResponse.Result result : positionList) {
                        positionIdList.add(result.getPlaceId());
                    }
                    getRegionInit(positionList, position);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(new RecyclerFragFirstThemeAdapter(FragFirstInfoActivity.this, list_place));
                            recyclerView.setLayoutManager(new LinearLayoutManager(FragFirstInfoActivity.this,RecyclerView.VERTICAL,false));
                        }
                    });
                }
                else {
                    Log.d(TAG, "에러발생 .." + response.message());
                }
            }

            @Override
            public void onFailure(Call<PositionResponse> call, Throwable t) {
                Log.d(TAG, "onFalilure .. 카테고리 불러오기 연동 실패 ..., 메세지 : " + t.getMessage());
            }

        });
    }
    private void getRegionInit(List<PositionResponse.Result> resultList,int position){
        int index;
        String name;
        String themaName;
        long placeid;
        Integer popular;
        PositionResponse.Result resultListIndex;

        for (index = 0; index < resultList.size(); index++) {
            resultListIndex = resultList.get(index);
            name = resultListIndex.getName();
            placeid = resultListIndex.getPlaceId();
            popular = resultListIndex.getPopular();
            switch(position){
                case 0: // 중구
                     if(name.equals("국립중앙박물관·용산가족공원")){
                        list_place.add(new DataMoreInfo(name,"아이템",R.drawable.yeouido ,false,popular,placeid));
                    }else if(name.equals("남산공원")) {
                        list_place.add(new DataMoreInfo(name, "아이템", R.drawable.namsan_park, false, popular, placeid));
                    }else if(name.equals("명동 관광특구")) {
                        list_place.add(new DataMoreInfo(name, "아이템", R.drawable.myongdong_tuk, false, popular, placeid));
                    }else if(name.equals("서울역")) {
                        list_place.add(new DataMoreInfo(name, "아이템", R.drawable.stn_seoul, false, popular, placeid));
                    }else if(name.equals("신세계백화점본점신관")) {
                    list_place.add(new DataMoreInfo(name, "아이템", R.drawable.shin_bon, false, popular, placeid));
                    }
                    break;
                case 1: // 종로구
                    if(name.equals("경복궁·서촌마을")){
                        list_place.add(new DataMoreInfo(name,"아이템",R.drawable.gbkkung ,false,popular,placeid));
                    }else if(name.equals("광화문·덕수궁")) {
                        list_place.add(new DataMoreInfo(name, "아이템", R.drawable.duksu, false, popular, placeid));
                    }else if(name.equals("북촌한옥마을")) {
                        list_place.add(new DataMoreInfo(name, "아이템", R.drawable.bukchon, false, popular, placeid));
                    }else if(name.equals("종로·청계 관광특구")) {
                        list_place.add(new DataMoreInfo(name, "아이템", R.drawable.jongro_chunggye, false, popular, placeid));
                    }else if(name.equals("창덕궁·종묘")) {
                        list_place.add(new DataMoreInfo(name, "아이템", R.drawable.changduckgung, false, popular, placeid));
                    }
                    break;
                case 2: // 송파구
                    if(name.equals("가든파이브툴")){
                        list_place.add(new DataMoreInfo(name,"아이템",R.drawable.gardenfive ,false,popular,placeid));
                    }else if(name.equals("롯데월드잠실점")) {
                        list_place.add(new DataMoreInfo(name, "아이템", R.drawable.lotte_world, false, popular, placeid));
                    }else if(name.equals("잠실 관광특구")) {
                        list_place.add(new DataMoreInfo(name, "아이템", R.drawable.jamsil_tour_special_gu, false, popular, placeid));
                    }else if(name.equals("잠실종합운동장")) {
                        list_place.add(new DataMoreInfo(name, "아이템", R.drawable.jamsil_total_stadium, false, popular, placeid));
                    }else if(name.equals("잠실한강공원")) {
                        list_place.add(new DataMoreInfo(name, "아이템", R.drawable.jamsil_hangang_park, false, popular, placeid));
                    }
                    break;
                case 3: // 강남구
                    if(name.equals("가로수길")) {
                        list_place.add(new DataMoreInfo(name, "아이템", R.drawable.garosu, false, popular, placeid));
                    }else if(name.equals("강남 MICE 관광특구")){
                        list_place.add(new DataMoreInfo(name,"아이템",R.drawable.gn_mice ,false,popular,placeid));
                    }else if(name.equals("강남역")) {
                        list_place.add(new DataMoreInfo(name, "아이템", R.drawable.stn_gn, false, popular, placeid));
                    }else if(name.equals("롯데백화점강남점")) {
                        list_place.add(new DataMoreInfo(name, "아이템", R.drawable.lotte_gn, false, popular, placeid));
                    }else if(name.equals("신세계백화점강남점")) {
                        list_place.add(new DataMoreInfo(name, "아이템", R.drawable.shin_gn, false, popular, placeid));
                    }
                    break;
                case 4: // 영등포구
                    if(name.equals("IFC몰")){
                        list_place.add(new DataMoreInfo(name,"아이템",R.drawable.ifc ,false,popular,placeid));
                    }else if(name.equals("롯데백화점영등포점")) {
                        list_place.add(new DataMoreInfo(name, "아이템", R.drawable.lotte_ydp, false, popular, placeid));
                    }else if(name.equals("신세계백화점타임스퀘어점")) {
                        list_place.add(new DataMoreInfo(name, "아이템", R.drawable.shin_timesquare, false, popular, placeid));
                    }else if(name.equals("여의도공원")) {
                        list_place.add(new DataMoreInfo(name, "아이템", R.drawable.yeouido_park, false, popular, placeid));
                    }else if(name.equals("영등포 타임스퀘어")) {
                        list_place.add(new DataMoreInfo(name, "아이템", R.drawable.time_square, false, popular, placeid));
                    }
                    break;
                case 5: // 금천구
                    if(name.equals("W몰")){
                        list_place.add(new DataMoreInfo(name,"아이템",R.drawable.wmall ,false,popular,placeid));
                    }else if(name.equals("가산디지털단지역")) {
                        list_place.add(new DataMoreInfo(name, "아이템", R.drawable.stn_gadi, false, popular, placeid));
                    }else if(name.equals("마리오아울렛1관")) {
                        list_place.add(new DataMoreInfo(name, "아이템", R.drawable.mario1, false, popular, placeid));
                    }else if(name.equals("마리오아울렛3관")) {
                        list_place.add(new DataMoreInfo(name, "아이템", R.drawable.mario3, false, popular, placeid));
                    }else if(name.equals("현대시티아울렛가산점")) {
                        list_place.add(new DataMoreInfo(name, "아이템", R.drawable.hyundae_gasan, false, popular, placeid));
                    }
                    break;

            }

        }

    }
    public ArrayList<DataMoreInfo> settingPage(List<PositionResponse.Result> resultList, String theme_id){
        int index;
        String name;
        String themaName;
        long placeid;
        Integer popular;
        PositionResponse.Result resultListIndex;
        long placeid_before = -1;

        for (index = 0; index < resultList.size(); index++) {
            resultListIndex = resultList.get(index);
            name = resultListIndex.getName();
            themaName = resultListIndex.getThema();
            placeid = resultListIndex.getPlaceId();
            popular = resultListIndex.getPopular();
            if(placeid!=placeid_before){
                if(theme_id.equals("카테고리더보기")){
                    list_place.add(new DataMoreInfo(name,"아이템",R.drawable.yeouido ,false,popular,placeid));
                }else if(theme_id.equals("쇼핑몰")&&(themaName.equals("쇼핑몰")||themaName.equals("백화점"))){
                    list_place.add(new DataMoreInfo(name,"아이템",R.drawable.yeouido ,false,popular,placeid));
                }else if(theme_id.equals("지하철·기차역")&&themaName.equals("지하철")){
                    list_place.add(new DataMoreInfo(name,"아이템",R.drawable.yeouido ,false,popular,placeid));
                }else if(theme_id.equals("음식점")&&themaName.equals("음식점")){
                    list_place.add(new DataMoreInfo(name,"아이템",R.drawable.yeouido ,false,popular,placeid));
                }else if(theme_id.equals("공원")&&themaName.equals("공원")){
                    list_place.add(new DataMoreInfo(name,"아이템",R.drawable.yeouido ,false,popular,placeid));
                }else if(theme_id.equals("공항")&&themaName.equals("공항")){
                    list_place.add(new DataMoreInfo(name,"아이템",R.drawable.yeouido ,false,popular,placeid));
                }else if(theme_id.equals("카페")&&themaName.equals("카페")){
                    list_place.add(new DataMoreInfo(name,"아이템",R.drawable.yeouido ,false,popular,placeid));
                }else if(theme_id.equals("관광")&&themaName.equals("관광지")){
                    list_place.add(new DataMoreInfo(name,"아이템",R.drawable.yeouido ,false,popular,placeid));
                }else if(theme_id.equals("거리")&&themaName.equals("골목 및 거리")){
                    list_place.add(new DataMoreInfo(name,"아이템 입니다.",R.drawable.yeouido ,false,popular,placeid));
                }else if(theme_id.equals("상업지구")&&themaName.equals("상업지구")){
                    list_place.add(new DataMoreInfo(name,"아이템",R.drawable.yeouido ,false,popular,placeid));
                }else if(theme_id.equals("스파")&&themaName.equals("스파")){
                    list_place.add(new DataMoreInfo(name,"아이템 입니다.",R.drawable.yeouido ,false,popular,placeid));
                }else if(theme_id.equals("놀이공원")&&themaName.equals("놀이공원")){
                    list_place.add(new DataMoreInfo(name,"아이템",R.drawable.yeouido ,false,popular,placeid));
                }else if(theme_id.equals("마트")&&themaName.equals("마트")){
                    list_place.add(new DataMoreInfo(name,"아이템",R.drawable.yeouido ,false,popular,placeid));
                }else{
                    Log.d("fragfirstinfo","placeid: "+placeid+", theme_id: "+theme_id+", themeName: "+themaName);
                }
            }
            placeid_before = placeid;

            Log.d("fragfirstinfo", "placeID: " + placeid + ", Popular: " + popular + ", Name: " + name + ", ThemaName: " + themaName);
        }

        return list_place;
    }


}