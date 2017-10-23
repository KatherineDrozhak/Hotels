package com.example.katrindrozhak.hotels;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.katrindrozhak.hotels.adapter.HotelAdapter;
import com.example.katrindrozhak.hotels.adapter.OnItemClicked;
import com.example.katrindrozhak.hotels.dialog.ErrorDialog;
import com.example.katrindrozhak.hotels.model.Hotel;
import com.example.katrindrozhak.hotels.service.HotelApi;
import com.example.katrindrozhak.hotels.service.ServiceFactory;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnItemClicked {
    private ProgressBar loading;
    private List<Hotel> hotels = new ArrayList<>();
    private HotelAdapter cardAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DisposableObserver<List<Hotel>> disposable;
    private RecyclerView recyclerView;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        loading = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.hotel_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        cardAdapter = new HotelAdapter(this);
        recyclerView.setAdapter(cardAdapter);
        cardAdapter.setOnClick(this);

        onRefresh();
    }

    private void showHotelData() {
        HotelApi service = ServiceFactory.createRetrofitService(HotelApi.class, Constants.BASE_URL);
        Observable<List<Hotel>> call = service.getHotelData();
        disposable = call
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(hotelListObserver());
    }

    private DisposableObserver<List<Hotel>> hotelListObserver() {
        return new DisposableObserver<List<Hotel>>() {

            @Override
            public void onNext(@NonNull List<Hotel> hotels) {
                loading.setVisibility(View.GONE);
                MainActivity.this.hotels = hotels;
                cardAdapter.addData(MainActivity.this.hotels);
                sortByDefault();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                ErrorDialog errorDialog = new ErrorDialog();
                errorDialog.setCancelable(false);
                errorDialog.show(getFragmentManager(), Constants.DIALOG_TAG);
                e.printStackTrace();
                Log.e("Hotel error", e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sort_by_distance:
                sortByDistance();
                return true;
            case R.id.sort_by_number:
                sortByNumber();
                return true;
            default:
                return false;
        }
    }

    private void sortByDistance() {
        Collections.sort(hotels, new Comparator<Hotel>() {
            public int compare(Hotel o1, Hotel o2) {
                return o1.getDistance().compareTo(o2.getDistance());
            }
        });
        cardAdapter.notifyDataSetChanged();
    }

    private void sortByNumber() {
        Collections.sort(hotels, new Comparator<Hotel>() {
            @Override
            public int compare(Hotel o1, Hotel o2) {
                if (o1.getAvailableRooms().size() == o2.getAvailableRooms().size()) {
                    return 0;
                } else {
                    return o1.getAvailableRooms().size() < o2.getAvailableRooms().size() ? 1 : -1;
                }
            }
        });
        cardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        showHotelData();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void sortByDefault() {
        Collections.sort(hotels, new Comparator<Hotel>() {
            @Override
            public int compare(Hotel o1, Hotel o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        cardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        final Hotel hotel = hotels.get(position);
        showDetail(hotel);

    }

    private void showDetail(Hotel hotel) {
        Parcelable selected = Parcels.wrap(hotel);
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(Constants.DETAIL_KEY, selected);
        startActivity(intent);
    }
}
