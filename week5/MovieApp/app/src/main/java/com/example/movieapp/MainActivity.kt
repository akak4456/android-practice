package com.example.movieapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.security.MessageDigest


class MainActivity : AppCompatActivity() {
    val locationData: MutableLiveData<Location> = MutableLiveData()
    val PERMISSIONS_REQUEST_CODE = 100
    val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val ACTION_AND_ADVENTURE = "28,12"
    private val COMEDY = "35"
    private val SF = "878"
    private val HORROR = "27"
    private val THRILLER = "53"
    private val CRIME = "80"
    private lateinit var context: Context

    private lateinit var tmdbRetrofit: Retrofit
    private lateinit var tmdbService: TMDBRetrofitService

    private lateinit var openWeatherRetrofit: Retrofit
    private lateinit var openWeatherService: OpenWeatherRetrofitService

    private lateinit var binding: ActivityMainBinding

    private lateinit var mainAdapter: MainAdapter

    private val mainTitleList: Array<String> = arrayOf("최신영화 전용관", "액션/어드벤처", "마음껏 웃어보세요!", "SF 어드벤처 영화","공포영화","액션 스릴러 영화","범죄 영화")
    private val subTitleList: Array<String> = arrayOf("따끈한 신작 영화들을 즐기는 시간", "액션과 모험의 세계로 떠납시다!", "코메디 영화", "","","","범인은 과연 누구였을까요?")
    /*
    주의 main과 sub는 1:1대응이 되어야 함
    ex) 최신영화 전용관 은 따끈한 신작 영화들을 즐기는 시간와 같이
    index가 대응이 되어야 함
     */

    private val mainInfoList: ArrayList<MainInfo> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        binding.mainRv.layoutManager = LinearLayoutManager(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        initRetrofit()
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context as MainActivity, REQUIRED_PERMISSIONS[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(context as MainActivity, REQUIRED_PERMISSIONS[1])) {
                Snackbar.make(binding.mainLayout, "이 앱을 실행하려면 GPS 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인") { // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                    ActivityCompat.requestPermissions(context as MainActivity, REQUIRED_PERMISSIONS,
                            PERMISSIONS_REQUEST_CODE)
                }.show()
            } else {
                ActivityCompat.requestPermissions(context as MainActivity, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
            }

        } else {
            startGPS()
        }

        CoroutineScope(Main).launch {
            //아마 여기에 waiting bar를 그리는 것이 낫지 않을까?
            val trendRet = async(IO) {
                tmdbService.getTodayTrend(TMDBRetrofitClient.apiKey, "ko-KR")
            }
            val actionAndAdventureRet = async(IO) {
                tmdbService.getMovieListByGenre(TMDBRetrofitClient.apiKey, ACTION_AND_ADVENTURE, "ko-KR")
            }
            val comedyRet = async(IO) {
                tmdbService.getMovieListByGenre(TMDBRetrofitClient.apiKey, COMEDY, "ko-KR")
            }
            val sfRet = async(IO) {
                tmdbService.getMovieListByGenre(TMDBRetrofitClient.apiKey, SF, "ko-KR")
            }
            val horrorRet = async(IO) {
                tmdbService.getMovieListByGenre(TMDBRetrofitClient.apiKey, HORROR, "ko-KR")
            }
            val thrillerRet = async(IO) {
                tmdbService.getMovieListByGenre(TMDBRetrofitClient.apiKey, THRILLER, "ko-KR")
            }
            val crimeRet = async(IO) {
                tmdbService.getMovieListByGenre(TMDBRetrofitClient.apiKey, CRIME, "ko-KR")
            }

            val trend = trendRet.await()
            val actionAndAdventure = actionAndAdventureRet.await()
            val comedy = comedyRet.await()
            val sf = sfRet.await()
            val horror = horrorRet.await()
            val thriller = thrillerRet.await()
            val crime = crimeRet.await()

            locationData.observe(context as MainActivity, Observer{
                if(it != null){
                    Log.d("Location","Location Data Loaded")
                    //모든 작업은 location data가 불려온 뒤에 한다
                    Log.d("Location","latitude: ${it.latitude}, longitude: ${it.longitude}")
                    openWeatherService.getWeatherByLoc(it.latitude,it.longitude,OpenWeatherRetrofitClient.apiKey).enqueue(object: Callback<Weather> {
                        override fun onFailure(call: Call<Weather>, t: Throwable) {
                            t.printStackTrace()
                        }

                        override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                            val weather = response?.body()?.weather?.get(0)?.main
                            Log.d("TMP",weather!!)

                            mainInfoList.add(MainInfo(mainTitleList[0], subTitleList[0],
                                    trend.results as MutableList<Result>, trend.total_pages, "none"))
                            mainInfoList.add(MainInfo(mainTitleList[1], subTitleList[1], actionAndAdventure.results as MutableList<Result>, actionAndAdventure.total_pages, ACTION_AND_ADVENTURE))
                            mainInfoList.add(MainInfo(mainTitleList[2], subTitleList[2], comedy.results as MutableList<Result>, comedy.total_pages, COMEDY))
                            if(weather!! == "Thunderstorm"){
                                mainInfoList.add(MainInfo("밖에 번개가 치는 군요!", "이럴 때 공포 영화를 보는 것은 어떨까요?", horror.results as MutableList<Result>, horror.total_pages, HORROR))
                            }else if(weather!! == "Drizzle"){
                                mainInfoList.add(MainInfo("이슬비가 내려요!", "모험을 떠나고 싶지는 않으신가요?", actionAndAdventure.results, actionAndAdventure.total_pages, ACTION_AND_ADVENTURE))
                            }else if(weather!! == "Rain"){
                                mainInfoList.add(MainInfo("밖에 비가 내려요!", "이럴 때는 스릴러가 제격이죠!", thriller.results as MutableList<Result>, thriller.total_pages, THRILLER))
                            }else if(weather!! == "Snow"){
                                mainInfoList.add(MainInfo("눈이 내립니다!", "무언가 몽환적이네요!", sf.results as MutableList<Result>, sf.total_pages,SF))
                            }else if(weather!! == "Clear"){
                                mainInfoList.add(MainInfo("날이 화창합니다!", "이럴 때는 코미디죠!", comedy.results, comedy.total_pages, COMEDY))
                            }else if(weather!! == "Clouds"){
                                mainInfoList.add(MainInfo("구름이 꼈습니다!","무언가 심상치 않은 분위기군요", crime.results as MutableList<Result>,crime.total_pages,CRIME))
                            }else if(weather!! == "Mist" || weather!! == "Smoke" || weather!! == "Haze" || weather!! == "Dust" || weather!! == "Fog" || weather!! == "Sand" || weather!! == "Ash"){
                                mainInfoList.add(MainInfo("밖에 무언가 답답합니다!", "이럴 때는 스릴러가 제격이죠!", thriller.results as MutableList<Result>, thriller.total_pages, THRILLER))
                            }else if(weather!! == "Squall" || weather!! == "Tornado"){
                                mainInfoList.add(MainInfo("밖에 바람이 세차게 불어요!", "이럴 때는 스릴러가 제격이죠!", thriller.results as MutableList<Result>, thriller.total_pages, THRILLER))
                            }else{
                                mainInfoList.add(MainInfo("밖에 비가 내려요!", "이럴 때는 스릴러가 제격이죠!", thriller.results as MutableList<Result>, thriller.total_pages, THRILLER))
                            }
                            mainInfoList.add(MainInfo(mainTitleList[3], subTitleList[3], sf.results as MutableList<Result>, sf.total_pages, SF))
                            mainInfoList.add(MainInfo(mainTitleList[4], subTitleList[4], horror.results as MutableList<Result>, horror.total_pages, HORROR))
                            mainInfoList.add(MainInfo(mainTitleList[5], subTitleList[5], thriller.results as MutableList<Result>, thriller.total_pages, THRILLER))
                            mainInfoList.add(MainInfo(mainTitleList[6], subTitleList[6], crime.results as MutableList<Result>, crime.total_pages, CRIME))
                            mainAdapter = MainAdapter(context, mainInfoList, tmdbService)
                            binding.mainRv.adapter = mainAdapter

                        }

                    })
                }
            })
        }
        binding.bottomAppbar.homeBtnOffImg.visibility = View.INVISIBLE
        binding.bottomAppbar.homeTv.setTextColor(Color.WHITE)
        binding.bottomAppbar.watchlistLayout.setOnClickListener{
            val intent = Intent(context,WatchlistActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun initRetrofit() {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClientBuilder.addInterceptor(logging)
        val httpClient = httpClientBuilder.build()
        tmdbRetrofit = TMDBRetrofitClient.getInstance(httpClient)
        tmdbService = tmdbRetrofit.create(TMDBRetrofitService::class.java)

        openWeatherRetrofit = OpenWeatherRetrofitClient.getInstance(httpClient)
        openWeatherService = openWeatherRetrofit.create(OpenWeatherRetrofitService::class.java)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.size == REQUIRED_PERMISSIONS.size) {
            var check_result = true
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false
                    break
                }
            }
            if (check_result) {
                startGPS()
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {
                    Snackbar.make(binding.mainLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인") { finish() }.show()
                } else {
                    Snackbar.make(binding.mainLayout, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인") { finish() }.show()
                }

            }
        }
    }

    @SuppressLint("MissingPermission")
    fun startGPS() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                locationData.value = location
            }else{
                //이것은 emulator를 위해 임시로 설정한 것임
                val loc = Location("")
                loc.latitude = 37.4482043
                loc.longitude = 126.6628039
                locationData.value = loc
            }
        }
    }



}