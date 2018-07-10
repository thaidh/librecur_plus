package com.example.thaidh.app_test_recur;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.recur.InvalidRecurrenceRuleException;
import org.dmfs.rfc5545.recur.RecurrenceRule;
import org.dmfs.rfc5545.recur.RecurrenceRuleIterator;

import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);

//            RecurrenceRule rule = new RecurrenceRule("RSCALE=GREGORIAN;FREQ=MONTHLY;BYMONTHDAY=-29");
            RecurrenceRule rule = new RecurrenceRule("RSCALE=CHINESE;FREQ=MONTHLY;BYMONTHDAY=29,30,28");

//            RecurrenceRule rule = new RecurrenceRule("RSCALE=GREGORIAN;FREQ=WEEKLY");
//            RecurrenceRule rule = new RecurrenceRule("RSCALE=CHINESE;FREQ=WEEKLY");

//            RecurrenceRule rule = new RecurrenceRule("RSCALE=GREGORIAN;FREQ=MONTHLY");
//            RecurrenceRule rule = new RecurrenceRule("RSCALE=CHINESE;FREQ=MONTHLY");

//            RecurrenceRule rule = new RecurrenceRule("RSCALE=GREGORIAN;FREQ=YEARLY");
//            RecurrenceRule rule = new RecurrenceRule("RSCALE=CHINESE;FREQ=YEARLY");

            DateTime start = new DateTime(2018, 9/* 0-based month numbers! */,7, 10, 11, 30);
//            start = DateTime.now();

            /*Có timezone thì các nextInstance sẽ được trả về theo timezone đó */
            RecurrenceRuleIterator it = rule.iterator(start.getTimestamp(), TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

            /*Không chỉ ra timezone mặt định các nextInstance sẽ được trả về theo timezone mặt đinh UTC */
//            RecurrenceRuleIterator it = rule.iterator(start);


            int maxInstances = 100; // limit instances for rules that recur forever

            while (it.hasNext() && (!rule.isInfinite() || maxInstances-- > 0))
            {
                DateTime nextInstance = it.nextDateTime();
                // do something with nextInstance
                Log.i("AAAAAAAA", "onCreate: " + nextInstance.getDayOfMonth() + "/" + (nextInstance.getMonth() + 1) + "/" + nextInstance.getYear()
                + " " + nextInstance.getHours() + ":" + nextInstance.getMinutes() + ":" + nextInstance.getSeconds());
            }
        } catch (InvalidRecurrenceRuleException e) {
            e.printStackTrace();
        }
    }
}
