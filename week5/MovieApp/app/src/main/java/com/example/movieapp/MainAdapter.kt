package com.example.movieapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.arasthel.spannedgridlayoutmanager.SpanSize
import com.arasthel.spannedgridlayoutmanager.SpannedGridLayoutManager
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.MainInfoItemBinding
import com.example.movieapp.databinding.MainRvHeaderBinding
import com.example.movieapp.databinding.MainRvWithWeatherBinding
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainAdapter(
    private val context: Context,
    private val info: ArrayList<MainInfo>,
    private val tmdbService: TMDBRetrofitService
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TYPE_HEADER = 0
    val TYPE_ITEM = 1
    val TYPE_WITH_WEATHER = 2
    private var pageNumList = IntArray(info.size) { 1 }
    private var weatherPage = 1

    inner class WithWeatherViewHolder(private val binding: MainRvWithWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var recyclerView: RecyclerView = binding.rv
        fun setData(model: MainInfo) {
            if (model.mainTitle.isEmpty()) {
                binding.mainTitleTv.visibility = View.GONE
            } else {
                binding.mainTitleTv.text = model.mainTitle
            }
            binding.subTitleTv.text = model.subTitle
        }
    }

    inner class HeaderViewHolder(private val binding: MainRvHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setHeader() {
            binding.loginImage.clipToOutline = true

            UserApiClient.instance.me { user, error ->
                if(error != null){
                    Log.d("KaKao","error 발생")
                    binding.loginImage.setImageDrawable(context.resources.getDrawable(R.drawable.profile1))
                }else{
                    Log.d("KaKao","회원번호: ${user?.id}")
                    Log.d("KaKao","닉네임: ${user?.kakaoAccount?.profile?.nickname}")
                    Log.d("KaKao","프로필 링크: ${user?.kakaoAccount?.profile?.profileImageUrl}")
                    Log.d("KaKao","썸네일 링크: ${user?.kakaoAccount?.profile?.thumbnailImageUrl}")
                    Log.d("KaKao","이메일: ${user?.kakaoAccount?.email}")

                    val circularProgressDrawable = CircularProgressDrawable(context)
                    circularProgressDrawable.setColorSchemeColors(Color.parseColor("#ff7f00"))
                    circularProgressDrawable.strokeWidth = 6f
                    circularProgressDrawable.centerRadius = 40f
                    circularProgressDrawable.start()

                    Glide.with(context)
                        .load(user?.kakaoAccount?.profile?.profileImageUrl)
                        .placeholder(circularProgressDrawable)
                        .into(binding.loginImage)

                    binding.loginImage.setOnClickListener{

                        val fragment = ProfileFragment(user?.kakaoAccount?.profile?.profileImageUrl!!,user?.kakaoAccount?.profile?.nickname!!,user?.kakaoAccount?.email!!)
                        fragment.setStyle(DialogFragment.STYLE_NORMAL,R.style.Dialog)
                        fragment.show((context as MainActivity).supportFragmentManager.beginTransaction(),"dialog_event")
                    }
                }
            }
        }
    }

    inner class ItemViewHolder(private val binding: MainInfoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var recyclerView: RecyclerView = binding.mainInfoHorizontalRv
        fun setData(model: MainInfo, position: Int) {
            binding.mainTitleTv.text = model.mainTitle

            if (model.subTitle.isEmpty()) {
                binding.subTitleTv.visibility = View.GONE
            } else {
                binding.subTitleTv.text = model.subTitle
            }

            binding.mainInfoTop.setOnClickListener {
                val intent = Intent(context, ListEachActivity::class.java)
                intent.putExtra("title", model.mainTitle)
                if (position == 0) {
                    intent.putExtra("isTrend", true)
                } else {
                    intent.putExtra("isTrend", false)
                    intent.putExtra("genre", model.genre)
                }

                context.startActivity(intent)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return TYPE_HEADER
        } else if (position == 4) {
            return TYPE_WITH_WEATHER
        } else {
            return TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var holder: RecyclerView.ViewHolder? = null
        if (viewType == TYPE_HEADER) {
            val bindind =
                MainRvHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            holder = HeaderViewHolder(bindind)
        } else if (viewType == TYPE_ITEM) {
            val bindind =
                MainInfoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            holder = ItemViewHolder(bindind)
        } else if (viewType == TYPE_WITH_WEATHER) {
            val bindind =
                MainRvWithWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            holder = WithWeatherViewHolder(bindind)
        }

        return holder!!
    }

    override fun getItemCount(): Int = info.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            val headerViewHolder: HeaderViewHolder = holder
            headerViewHolder.setHeader()
        } else if (holder is WithWeatherViewHolder) {
            val withWeatherViewHolder: WithWeatherViewHolder = holder
            val rv: RecyclerView = withWeatherViewHolder.recyclerView
            val horizontalAdapter = MainWithWeatherAdapter(context)
            rv.adapter = horizontalAdapter
            horizontalAdapter.setList(info[position - 1].results)

            val manager = StaggeredGridLayoutManager(2,LinearLayoutManager.HORIZONTAL)
            rv.layoutManager = manager
            withWeatherViewHolder.setData(info[position - 1])
            rv.addItemDecoration(WithWeatherDecoration(convertDpToPixel(5.0f,context).toInt()))

            rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val itemTotalCount = horizontalAdapter.itemCount - 1
                    if (!rv.canScrollHorizontally(1)) {
                        horizontalAdapter.deleteLoading()
                        if (weatherPage< info[position-1].total_pages) {
                            CoroutineScope(IO).launch {
                                weatherPage++
                                var results: List<Result>? = null
                                    results = withContext(IO) {
                                        tmdbService.getMovieListByGenre(
                                            TMDBRetrofitClient.apiKey,
                                            info[position-1].genre,
                                            "ko-KR",
                                            weatherPage
                                        )
                                    }.results
                                horizontalAdapter.setList(results!!)
                                withContext(Main) {
                                    horizontalAdapter.notifyItemRangeChanged(
                                        itemTotalCount,
                                        results!!.size + 2
                                    )
                                }
                            }
                        }
                    }
                }
            })
        } else {
            val newPos = position - 1
            val itemViewHolder: ItemViewHolder = holder as ItemViewHolder
            val rv: RecyclerView = itemViewHolder.recyclerView
            rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val horizontalAdapter = MainHorizontalAdapter(context)
            rv.adapter = horizontalAdapter
            horizontalAdapter.setList(info[newPos].results)
            rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisibleItemPosition =
                        (rv.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                    val itemTotalCount = horizontalAdapter.itemCount - 1
                    if (!rv.canScrollHorizontally(1) && lastVisibleItemPosition == itemTotalCount) {
                        horizontalAdapter.deleteLoading()
                        if (pageNumList[newPos] < info[newPos].total_pages) {
                            CoroutineScope(IO).launch {
                                pageNumList[newPos]++
                                var results: List<Result>? = null
                                if (newPos == 0) {
                                    results = withContext(IO) {
                                        tmdbService.getTodayTrend(
                                            TMDBRetrofitClient.apiKey,
                                            "ko-KR",
                                            pageNumList[newPos]
                                        )
                                    }.results
                                } else {
                                    results = withContext(IO) {
                                        tmdbService.getMovieListByGenre(
                                            TMDBRetrofitClient.apiKey,
                                            info[newPos].genre,
                                            "ko-KR",
                                            pageNumList[newPos]
                                        )
                                    }.results
                                }
                                horizontalAdapter.setList(results!!)
                                withContext(Main) {
                                    horizontalAdapter.notifyItemRangeInserted(
                                        itemTotalCount + 1,
                                        results!!.size + 1
                                    )
                                }
                            }
                        }
                    }
                }
            })

            itemViewHolder.setData(info[newPos], newPos)
        }
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources
            .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}