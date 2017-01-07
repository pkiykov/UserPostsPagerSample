package com.pkiykov.userpostspagersample.data.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pkiykov.userpostspagersample.AddressesModel;
import com.pkiykov.userpostspagersample.CompaniesModel;
import com.pkiykov.userpostspagersample.GeosModel;
import com.pkiykov.userpostspagersample.UsersModel;
import com.pkiykov.userpostspagersample.data.model.Address;
import com.pkiykov.userpostspagersample.data.model.Company;
import com.pkiykov.userpostspagersample.data.model.Geo;
import com.pkiykov.userpostspagersample.data.model.User;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, "user_posts", null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(User.CREATE_TABLE);
        db.execSQL(Address.CREATE_TABLE);
        db.execSQL(Company.CREATE_TABLE);
        db.execSQL(Geo.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertDataIntoDb(User user) {

        Address address = user.getAddress();
        Geo geo = address.getGeo();
        Company company = user.getCompany();

        User.Insert_user insertUser = new UsersModel.Insert_user(getReadableDatabase());
        Address.Insert_address insertAddress = new AddressesModel.Insert_address(getReadableDatabase());
        Company.Insert_company insertCompany = new CompaniesModel.Insert_company(getReadableDatabase());
        Geo.Insert_geo insertGeo = new GeosModel.Insert_geo(getReadableDatabase());

        insertUser.bind(user.getId(), user.getName(), user.getUsername(), user.getEmail(), user.getPhone(), user.getWebsite());
        insertAddress.bind(user.getId(), address.getStreet(), address.getSuite(), address.getCity(), address.getZipcode());
        insertCompany.bind(user.getId(), company.getName(), company.getCatchPhrase(), company.getBs());
        insertGeo.bind(user._id(), geo.getLat(), geo.getLng());

        long u = insertUser.program.executeUpdateDelete();
        long a = insertAddress.program.executeUpdateDelete();
        long c = insertCompany.program.executeUpdateDelete();
        long g = insertGeo.program.executeUpdateDelete();

        Log.d("MyTag", "user = " + u);
        Log.d("MyTag", "address = " + a);
        Log.d("MyTag", "company = " + c);
        Log.d("MyTag", "geo = " + g);
    }
}
