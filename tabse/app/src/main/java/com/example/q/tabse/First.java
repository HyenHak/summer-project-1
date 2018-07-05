package com.example.q.tabse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;
import java.io.*;
import java.util.*;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import static android.content.Context.MODE_PRIVATE;

public class First extends Fragment {
    private ListView listview;
    private ListViewAdapter adapter;
    private FloatingActionButton button;
    public static ArrayList<Person> data = new ArrayList<>();
    private String js = new String();
    private String FileName = new String();
    private Gson gson = new Gson();
    public static int pos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FileName = "data.json";

        try {
            File file = new File(getActivity().getFilesDir(), FileName);
            FileReader fr = new FileReader(file);
            BufferedReader bufrd = new BufferedReader(fr);

            Gson gson = new Gson();

            String ch;
            while ((ch = bufrd.readLine()) != null) {
                js = js + ch;
            }

            data = gson.fromJson(js, new TypeToken<ArrayList<Person>>() {
            }.getType());
            bufrd.close();
            fr.close();

            AscendingObj ascending = new AscendingObj();
            Collections.sort(this.data, ascending);
        }
        catch (Exception e) {
        }
    }

    public class ListViewAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        public ArrayList<Person> data;
        private int layout;

        public ListViewAdapter(Context context, int layout, ArrayList<Person> data) {
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.data = data;
            this.layout = layout;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Person getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item, parent, false);
            }

            Person person = data.get(position);

            TextView textview = (TextView) convertView.findViewById(R.id.textView);
            textview.setText(person.name);
            TextView textview2 = (TextView) convertView.findViewById(R.id.textView2);
            textview2.setText(person.phone.phone_number1);

            return convertView;
        }

        public void addItem(Person person) {
            data.add(person);
            dataChange();
        }

        public void remove(int position) {
            data.remove(position);
            dataChange();
        }

        public void dataChange() {
            adapter.notifyDataSetChanged();
        }
    }

    public void FileSave() {
        try {
            File file = new File(getActivity().getFilesDir(), FileName);
            FileWriter fw = new FileWriter(file);
            BufferedWriter bufwr = new BufferedWriter(fw);

            int i;
            String save = "[";
            for (i = 0; i < adapter.data.size(); i++) {
                if (i != 0) save = save + ",";
                save = save + gson.toJson(adapter.data.get(i), Person.class);
            }
            save = save + "]";
            bufwr.write(save);
            bufwr.close();
            fw.close();
        } catch (Exception e) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_first, null);

        button = (FloatingActionButton) view.findViewById(R.id.floatingActionButton2);
        listview = (ListView) view.findViewById(R.id.listview);
        adapter = new ListViewAdapter(getActivity(), R.layout.item, data);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                pos = position;
                Intent intent = new Intent(getActivity(), Main2Activity.class);
                startActivityForResult(intent, 0);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Person p = new Person();
                adapter.addItem(p);
                pos = adapter.data.size() - 1;
                Intent intent = new Intent(getActivity(), Main2Activity.class);
                startActivityForResult(intent, 0);
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        FileSave();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            int del = data.getIntExtra("삭제", 0);
            if (del == 1) {
                adapter.data.remove(pos);
                adapter.dataChange();
            }
            adapter.dataChange();
        }

        AscendingObj ascending = new AscendingObj();
        Collections.sort(this.data, ascending);
        adapter.dataChange();

        FileSave();
    }

    public class Phone_number {

        Phone_number(String phone_number1, String phone_number2, String phone_number3) {
            this.phone_number1 = phone_number1;
            this.phone_number2 = phone_number2;
            this.phone_number3 = phone_number3;
        }

        Phone_number(String phone_number1, String phone_number2) {
            this.phone_number1 = phone_number1;
            this.phone_number2 = phone_number2;
        }

        Phone_number(String phone_number1) {
            this.phone_number1 = phone_number1;
        }

        Phone_number() {
            phone_number3 = "";
            phone_number2 = "";
            phone_number1 = "";
        }

        @Override
        public boolean equals(Object phone) {
            Phone_number p = (Phone_number) phone;
            if (!this.phone_number1.equals(p.phone_number1)) return false;
            if (!this.phone_number2.equals(p.phone_number2)) return false;
            if (!this.phone_number3.equals(p.phone_number3)) return false;
            return true;
        }

        public String phone_number1;
        public String phone_number2;
        public String phone_number3;
    }

    public class Person {
        public String name;
        public String sex;
        public Phone_number phone = new Phone_number();
        public String email;
        public String department;

        Person() {
            name = "";
            sex = "";
            email = "";
            department = "";
        }

        Person(String name, String sex, Phone_number phone, String email, String department) {
            this.name = name;
            this.sex = sex;
            this.phone = phone;
            this.email = email;
            this.department = department;
        }

        @Override
        public String toString() {
            return "이름: " + name + "\n성별: " + sex + "\n전화번호1: " + phone.phone_number1
                    + "\n전화번호2: " + phone.phone_number2 + "\n전화번호3: " + phone.phone_number3
                    + "\n이메일: " + email + "\n부서: " + department;
        }

        @Override
        public boolean equals(Object person) {
            Person p = (Person) person;
            if (!this.name.equals(p.name)) return false;
            if (!this.sex.equals(p.sex)) return false;
            if (!this.email.equals(p.email)) return false;
            if (!this.department.equals(p.department)) return false;
            if (!this.phone.equals(p.phone)) return false;
            return true;
        }
    }

    class AscendingObj implements Comparator<Person> {

        @Override
        public int compare(Person o1, Person o2) {
            return o1.name.compareTo(o2.name);
        }
    }
}
