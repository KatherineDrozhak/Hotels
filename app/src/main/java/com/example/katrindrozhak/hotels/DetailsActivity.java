package com.example.katrindrozhak.hotels;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.katrindrozhak.hotels.model.Hotel;
import com.example.katrindrozhak.hotels.service.HotelApi;
import com.example.katrindrozhak.hotels.service.ServiceFactory;
import com.example.katrindrozhak.hotels.extensions.CropMarginTransformation;
import com.example.katrindrozhak.hotels.dialog.ErrorDialog;
import com.example.katrindrozhak.hotels.dialog.ErrorDialogListener;
import com.example.katrindrozhak.hotels.extensions.SpannableText;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class DetailsActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbar;
    private TextView nameDetail;
    private TextView addressDetail;
    private TextView starsDetail;
    private TextView distanceDetail;
    private TextView suitesAvailabilityDetail;
    private TextView latDetail;
    private TextView lonDetail;
    private ImageView imageHotel;
    private DisposableObserver<Hotel> disposables;
    private LinearLayout load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeUI();
        fetchDetails();
    }

    private void fetchDetails() {
        final Hotel mHotel = Parcels.unwrap(getIntent().getParcelableExtra(Constants.DETAIL_KEY));
        final HotelApi hotelApi = ServiceFactory.createRetrofitService(HotelApi.class, Constants.BASE_URL);

        Observable<Hotel> call = hotelApi.getHotel(mHotel.getId());
        disposables = call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(detailsObserver());
    }

    private DisposableObserver<Hotel> detailsObserver() {
        return new DisposableObserver<Hotel>() {
            @Override
            public void onNext(@NonNull Hotel hotel) {
                setupDetailsInfo(hotel);
                load.setVisibility(View.GONE);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
                ErrorDialog errorDialog = new ErrorDialog();
                errorDialog.setCancelable(false);
                errorDialog.setListener(new ErrorDialogListener() {
                    @Override
                    public void okPressed() {
                        onBackPressed();
                    }
                });
                errorDialog.show(getFragmentManager(), Constants.DIALOG_TAG);
            }

            @Override
            public void onComplete() {

            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }

    private void initializeUI() {
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        load = (LinearLayout) findViewById(R.id.loadLayout);
        nameDetail = (TextView) findViewById(R.id.name_detail);
        addressDetail = (TextView) findViewById(R.id.address_detail);
        starsDetail = (TextView) findViewById(R.id.stars_detail);
        distanceDetail = (TextView) findViewById(R.id.distance_detail);
        suitesAvailabilityDetail = (TextView) findViewById(R.id.suites_availability_detail);
        latDetail = (TextView) findViewById(R.id.lat);
        lonDetail = (TextView) findViewById(R.id.lon);
        imageHotel = (ImageView) findViewById(R.id.header);
    }

    private void updateImage(String imageId) {
        Picasso.with(DetailsActivity.this)
                .load(getString(R.string.image_url) + imageId)
                .placeholder(R.mipmap.ic_smile)
                .transform(new CropMarginTransformation())
                .into(imageHotel);
    }

    private void setupDetailsInfo(Hotel hotel) {
        updateImage(hotel.getImage());

        collapsingToolbar.setTitle(hotel.getName());
        nameDetail.setText(hotel.getName());
        addressDetail.setText(makeField(R.string.string_address, hotel.getAddress()));
        starsDetail.setText(makeField(R.string.string_stars, hotel.getStars().toString()));
        distanceDetail.setText(makeField(R.string.string_distance, hotel.getDistance().toString()));
        suitesAvailabilityDetail.setText(makeField(R.string.string_suites_av, hotel.getAvailableRoomsInfo()));
        latDetail.setText(makeField(R.string.string_lat, hotel.getLat().toString()));
        lonDetail.setText(makeField(R.string.string_lon, hotel.getLon().toString()));
    }

    private Spannable makeField(int titleStringId, String fieldValue) {
        String title = getString(titleStringId);
        return SpannableText.setupBoldText(title + fieldValue, 0, title.length());
    }
}
