package com.shyky.library.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.shyky.library.BaseApplication;
import com.shyky.library.R;
import com.shyky.library.adapter.MonthAdapter;
import com.shyky.library.bean.MonthBean;
import com.shyky.library.util.LayoutUtil;
import com.shyky.library.util.ResourceUtil;
import com.shyky.util.DateUtil;
import com.shyky.util.TextUtil;

import java.util.ArrayList;
import java.util.List;

import static com.socks.library.KLog.d;

/**
 * 日期选择对话框
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.2
 * @email sj1510706@163.com
 * @date 2016/6/25
 * @since 1.0
 */
public class DateDialog extends Dialog implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = "DateDialog";
    private Button btnPrevious;
    private Button btnNext;
    private TextView tvMonthYear;
    private GridView gridView;
    private List<MonthBean> data;
    private MonthAdapter adapter;
    private int currentYear;
    private int currentMonth;
    private int currentDay;

    public DateDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public DateDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    public DateDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public DateDialog(@NonNull Context context, int year, int monthOfYear, int dayOfMonth) {
        this(context);
        currentYear = year;
        currentMonth = monthOfYear;
        currentDay = dayOfMonth;
    }

    public DateDialog(int year, int monthOfYear, int dayOfMonth) {
        this(BaseApplication.getContext(), year, monthOfYear, dayOfMonth);
    }

    private void init() {
        data = new ArrayList<>();
        adapter = new MonthAdapter(getContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View inflateView = LayoutUtil.inflate(R.layout.dialog_date_picker);
        initData();
        findView(inflateView);
        initView();
        setContentView(inflateView);
    }

    private void initData() {
        adapter.setData(data);
        currentYear = DateUtil.getCurrentYear();
        currentMonth = DateUtil.getCurrentMonth();
        currentDay = DateUtil.getCurrentDay();
        getMonthDays(currentYear, currentMonth);
    }

    private void findView(@NonNull View inflateView) {
        gridView = (GridView) inflateView.findViewById(R.id.gridView);
        tvMonthYear = (TextView) inflateView.findViewById(R.id.tv_month_year);
        btnPrevious = (Button) inflateView.findViewById(R.id.btn_previous_month);
        btnNext = (Button) inflateView.findViewById(R.id.btn_next_month);
    }

    private void initView() {
        btnPrevious.setText("上");
        btnNext.setText("下");
        tvMonthYear.setText(currentMonth + "," + currentYear);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        btnPrevious.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    private int todayPosition;

    public void getMonthDays(int currentYear, int currentMonth) {
        data.clear();
        // 添加顶部星期
        data.add(new MonthBean(ResourceUtil.getString(R.string.text_su), false));
        data.add(new MonthBean(ResourceUtil.getString(R.string.text_m), false));
        data.add(new MonthBean(ResourceUtil.getString(R.string.text_th), false));
        data.add(new MonthBean(ResourceUtil.getString(R.string.text_w), false));
        data.add(new MonthBean(ResourceUtil.getString(R.string.text_t), false));
        data.add(new MonthBean(ResourceUtil.getString(R.string.text_f), false));
        data.add(new MonthBean(ResourceUtil.getString(R.string.text_s), false));
        // 获取指定年份下的月份下的天数
        final int days = DateUtil.getDayOfMonth(currentYear, currentMonth);
        d(TAG, currentYear + "年" + currentMonth + "月份一共有" + days + "天");
        for (int j = 1; j <= days; j++) {
            boolean flag = false;
            // 判断是否为今天，是则选中
            if (j == currentDay) {
                flag = true;
                todayPosition = j - 1;
            }
            // 添加到数据源用于GridView显示
            data.add(new MonthBean(j, flag));
        }
    }

    private int year = currentYear;
    private int month = currentMonth;
    private int day = currentDay;

    @Override
    public void onClick(View v) {
        // 解决切换到上一个月或下一个月时，会选中相同日期（今天）问题
        currentDay = 0;
        if (year == currentYear && month == currentMonth)
            data.get(todayPosition).setChecked(true);

        if (v.getId() == R.id.btn_previous_month) {
            if (currentMonth < 1) {
                currentMonth = 12;
                currentYear--;
            }
            getMonthDays(currentYear, currentMonth--);
            tvMonthYear.setText(currentMonth + "," + currentYear);
        } else if (v.getId() == R.id.btn_next_month) {
            if (currentMonth > 12) {
                currentMonth = 1;
                currentYear++;
            }
            getMonthDays(currentYear, currentMonth++);
            tvMonthYear.setText(currentMonth + "," + currentYear);
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final MonthBean monthBean = adapter.getItem(position);
        if (position > 5 && !TextUtil.isEmptyAndNull(monthBean.getDayOfMonth()) && monthBean.getDay() != 0) {
            data.get(position).setChecked(true);
            adapter.notifyDataSetChanged();
        }
    }
}